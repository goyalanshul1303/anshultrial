package com.app.carton.consumer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.carton.orders.R;
import com.google.gson.Gson;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aggarwal.swati on 2/12/19.
 */

public class QuotationListingFragment extends Fragment implements View.OnClickListener {

    private static View view;

    private RecyclerView quotationListView;

    private ProgressBar progressBar;
    DataView data = new DataView();
    private QuotationItemsAdapter adapter;
    View viewNoQuationsAdded;
    private ArrayList<QuotationData> quotationDataArrayList;
    String orderId,quoteId;
    private int awardPosition;
    private boolean isFromOpenOrders;
    private TextView quantity, productName;
    private String productId;
    private TextView productDetailLInk;

    public QuotationListingFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            orderId = getArguments().containsKey("orderId") ? getArguments().getString("orderId") : "";
            isFromOpenOrders = getArguments().containsKey("isFromOpenOrders") ? getArguments().getBoolean("isFromOpenOrders") : false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.quotation_list, container, false);
        initViews();
        return view;
    }


    // Initiate Views
    private void initViews() {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        viewNoQuationsAdded = (View)view.findViewById(R.id.viewNoQuotationAdded);
        quotationListView = (RecyclerView)view.findViewById(R.id.quotationRecyclerView);
        productName = (TextView)view.findViewById(R.id.productName);
        quantity = (TextView)view.findViewById(R.id.quantityOrder);
        productDetailLInk = (TextView) view.findViewById(R.id.productDetailLInk);
        productName.setOnClickListener(this);
        new FetchOrderDetailsTask().execute();

    }
    public class FetchDetailsTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... arg0) {

            try {
                SpannableStringBuilder string = new SpannableStringBuilder(WebServiceConstants.GET_ORDER_QUOTATIONS);
                string.append(orderId);
                URL url = new URL(string.toString());


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
//                conn.setDoInput(true);
//                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", SharedPreferences.getString(getActivity(), SharedPreferences.KEY_AUTHTOKEN));

                InputStream inputStream;

                if (conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                    inputStream = conn.getInputStream();
                } else {
                    inputStream = conn.getErrorStream();
                }

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp, response = "";
                while ((temp = bufferedReader.readLine()) != null) {
                    response += temp;
                }

                return response.toString();


            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            JSONObject object = null;

            progressBar.setVisibility(View.GONE);
            if (null != result) {
                if(result.trim().charAt(0) == '[') {
                    Log.e("Response is : " , "JSONArray");
                    parseDetailsData(result);
                } else if(result.trim().charAt(0) == '{') {
                    try {
                        object = new JSONObject(result);
                        if (null != object && !object.optString("status").isEmpty() && ( Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_BAD_REQUEST
                                || Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_UNAUTHORIZED)
                                || Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_FORBIDDEN) {
                            Toast.makeText(getActivity(), "Something went wrong please try again",
                                    Toast.LENGTH_LONG).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else  {
                    Toast.makeText(getActivity(), "Something went wrong please try again",
                            Toast.LENGTH_LONG).show();
                }


            }else  {
                Toast.makeText(getActivity(), "Something went wrong please try again",
                        Toast.LENGTH_LONG).show();
            }


        }
    }

    private void parseDetailsData(String result) {
        try {
            JSONArray list = new JSONArray(result);
            if (null!= list && list.length() > 0 ){
                Gson gson=new Gson();
                quotationDataArrayList = new ArrayList<>();
                for (int i = 0 ; i < list.length() ;i ++){
                    QuotationData item=gson.fromJson(String.valueOf((list.optJSONObject(i))),QuotationData.class);
                    quotationDataArrayList.add(item);
                }
                adapter = new QuotationItemsAdapter(getActivity(), quotationDataArrayList);
                quotationListView.setLayoutManager(new LinearLayoutManager(getActivity()));
                quotationListView.setAdapter(adapter);
                adapter.setFromOpenOrders(isFromOpenOrders);
                viewNoQuationsAdded.setVisibility(View.GONE);
                quotationListView.setVisibility(View.VISIBLE);
               adapter.SetOnItemClickListener(new QuotationItemsAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(View view, int position) {
                    QuotationData testObjtem = quotationDataArrayList.get((Integer) view.getTag());
                    awardPosition = position;
                    quoteId = testObjtem.id;
//                    new AwardQuotationTask().execute();

                    HashMap<String, String> paramMap = new HashMap<String,String>();
                    paramMap.put( "MID" , "FPKfWX03151357461470");
// Key in your staging and production MID available in your dashboard
                    paramMap.put( "ORDER_ID" , "order1");
                    paramMap.put( "CUST_ID" , "cust123");
                    paramMap.put( "MOBILE_NO" , "7777777777");
                    paramMap.put( "EMAIL" , "username@emailprovider.com");
                    paramMap.put( "CHANNEL_ID" , "WAP");
                    paramMap.put( "TXN_AMOUNT" , "100.12");
                    paramMap.put( "WEBSITE" , "WEBSTAGING");
// This is the staging value. Production value is available in your dashboard
                    paramMap.put( "INDUSTRY_TYPE_ID" , "Retail");
// This is the staging value. Production value is available in your dashboard
                    paramMap.put( "CALLBACK_URL", "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=order1");
                    paramMap.put( "CHECKSUMHASH" , "w2QDRMgp1234567JEAPCIOmNgQvsi+BhpqijfM9KvFfRiPmGSt3Ddzw+oTaGCLneJwxFFq5mqTMwJXdQE2EzK4px2xruDqKZjHupz9yXev4=");
                    PaytmOrder order = new PaytmOrder(paramMap);
                    PaytmPGService Service = PaytmPGService.getStagingService();

                    Service.initialize(order, null);
                    Service.startPaymentTransaction(getActivity(), true, true, new PaytmPaymentTransactionCallback() {
                        /*Call Backs*/
                        public void someUIErrorOccurred(String inErrorMessage) {}
                        public void onTransactionResponse(Bundle inResponse) {
                            Toast.makeText(getActivity(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();

                        }
                        public void networkNotAvailable() {}
                        public void clientAuthenticationFailed(String inErrorMessage) {}
                        public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {}
                        public void onBackPressedCancelTransaction() {}
                        public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {}
                    });
                }
            });


            }else{
                // no consumers added . please add consumer first
                viewNoQuationsAdded.setVisibility(View.VISIBLE);
                quotationListView.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }




    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.productName){
            Fragment newFragment = new ProductDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("selectedId", SharedPreferences.getString(getActivity(), "entityId"));
            bundle.putString("productId", productId);
            newFragment.setArguments(bundle);
            MainActivity.addActionFragment(newFragment);
        }

    }

    public class AwardQuotationTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... arg0) {

            try {
                URL url = new URL(WebServiceConstants.AWARD_QUOTATION+quoteId);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", SharedPreferences.getString(getActivity(), SharedPreferences.KEY_AUTHTOKEN));
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
//                writer.write(object.toString());

                writer.flush();
                writer.close();
                os.close();


                InputStream inputStream;

                if (conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                    inputStream = conn.getInputStream();
                } else {
                    inputStream = conn.getErrorStream();
                }

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp, response = "";
                while ((temp = bufferedReader.readLine()) != null) {
                    response += temp;
                }

                return response.toString();


            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            JSONObject object = null;

            progressBar.setVisibility(View.GONE);
            if (null != result) {
                try {
                    object = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (null!=object) {
                    if (!object.optString("status").isEmpty() && (Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_BAD_REQUEST
                            || Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_UNAUTHORIZED)
                            || Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_FORBIDDEN) {
                        Toast.makeText(getActivity(), "Something went wrong please try again",
                                Toast.LENGTH_LONG).show();
                    } else {
//                        parseListingData(object);
                        QuotationData testObjtem = quotationDataArrayList.get((Integer) awardPosition);
                        testObjtem.setAwarded(true);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "Quotation awarded successfully",
                                Toast.LENGTH_LONG).show();

                    }
                }
            }else  {
                Toast.makeText(getActivity(), "Something went wrong please try again",
                        Toast.LENGTH_LONG).show();
            }


        }
    }
    public class FetchOrderDetailsTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... arg0) {

            try {
                SpannableStringBuilder string = new SpannableStringBuilder(WebServiceConstants.GET_ORDERS);
                string.append("/");
                string.append(orderId);
                URL url = new URL(string.toString());


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
//                conn.setDoInput(true);
//                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", SharedPreferences.getString(getActivity(), SharedPreferences.KEY_AUTHTOKEN));

                InputStream inputStream;

                if (conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                    inputStream = conn.getInputStream();
                } else {
                    inputStream = conn.getErrorStream();
                }

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp, response = "";
                while ((temp = bufferedReader.readLine()) != null) {
                    response += temp;
                }

                return response.toString();


            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            JSONObject object = null;

            if (null != result) {
                try {
                    object = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (null!=object) {
                    if ((!object.optString("status").isEmpty() && ((Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_BAD_REQUEST)
                            || (Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_UNAUTHORIZED))) || ((Integer.valueOf(object.optString("status")) ==  HttpURLConnection.HTTP_FORBIDDEN))) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Something went wrong please try again",
                                Toast.LENGTH_LONG).show();
                    } else {

                        new FetchDetailsTask().execute();
                        parseOrderListingData(object);


                    }
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Something went wrong please try again",
                            Toast.LENGTH_LONG).show();
                }
            }else  {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Something went wrong please try again",
                        Toast.LENGTH_LONG).show();
            }


        }
    }
    void  parseOrderListingData(JSONObject object){
            Utils.setDetailsTextField("Name", getActivity(), productName, object.optString("productName"));
        Utils.setDetailsTextField("Quantity ", getActivity(), quantity, object.optString("quantity"));
        orderId = object.optString("id");
        productId = object.optString("productId");

    }

}
