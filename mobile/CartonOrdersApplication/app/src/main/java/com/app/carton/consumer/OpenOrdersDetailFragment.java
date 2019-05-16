package com.app.carton.consumer;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.carton.orders.R;
import com.google.gson.Gson;

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

/**
 * Created by aggarwal.swati on 5/12/19.
 */

public class OpenOrdersDetailFragment extends Fragment implements View.OnClickListener {

    private static View view;

    TextView quotationPlacedDate, quotationEndDate,quotationStartDate, quotationAmount;

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

    public OpenOrdersDetailFragment() {

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
        view = inflater.inflate(R.layout.open_order_detail, container, false);
        initViews();
        return view;
    }


    // Initiate Views
    private void initViews() {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        productName = (TextView)view.findViewById(R.id.productName);
        quantity = (TextView)view.findViewById(R.id.quantityOrder);
        productDetailLInk = (TextView) view.findViewById(R.id.productDetailLInk);
        productName.setOnClickListener(this);
        quotationPlacedDate = (TextView)view.findViewById(R.id.quotationPlacedDate);
        quotationStartDate = (TextView)view.findViewById(R.id.quotationStartDate);
        quotationEndDate = (TextView)view.findViewById(R.id.quotationEndDate);
        quotationAmount = (TextView)view.findViewById(R.id.quotationAmount);
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
            if (isVisible()){
                if (null != result) {
                    if (result.trim().charAt(0) == '[') {
                        Log.e("Response is : ", "JSONArray");
                        parseDetailsData(result);
                    } else if (result.trim().charAt(0) == '{') {
                        try {
                            object = new JSONObject(result);
                            if (null != object && !object.optString("status").isEmpty() && (Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_BAD_REQUEST
                                    || Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_UNAUTHORIZED)
                                    || Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_FORBIDDEN) {
                                Toast.makeText(getActivity(), "Something went wrong please try again",
                                        Toast.LENGTH_LONG).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Something went wrong please try again",
                                Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(getActivity(), "Something went wrong please try again",
                            Toast.LENGTH_LONG).show();
                }
            }
            else{
                FragmentManager fragmentManager = MainActivity.fragmentManager;
                fragmentManager.popBackStackImmediate();
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

    @Override
    public void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) { ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true); }
        getActivity().setTitle("Quotations");
    }
}