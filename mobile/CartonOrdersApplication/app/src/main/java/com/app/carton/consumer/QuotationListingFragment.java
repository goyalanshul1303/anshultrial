package com.app.carton.consumer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    String orderId, quoteId;
    private int awardPosition;
    private boolean isFromOpenOrders;
    private TextView quantity, productName;
    private String productId;
    private TextView productDetailLInk;
    private int quantityString;
    private String productNameString;

    public QuotationListingFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            orderId = getArguments().containsKey("orderId") ? getArguments().getString("orderId") : "";
            isFromOpenOrders = getArguments().containsKey("isFromOpenOrders") ? getArguments().getBoolean("isFromOpenOrders") : false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.quotation_list, container, false);
        initViews();
        setHasOptionsMenu(true);
        return view;
    }


    // Initiate Views
    private void initViews() {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        viewNoQuationsAdded = (View) view.findViewById(R.id.viewNoQuotationAdded);
        quotationListView = (RecyclerView) view.findViewById(R.id.quotationRecyclerView);
        productName = (TextView) view.findViewById(R.id.productName);
        quantity = (TextView) view.findViewById(R.id.quantityOrder);
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
            if (isVisible()) {
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
            } else {
                FragmentManager fragmentManager = MainActivity.fragmentManager;
                fragmentManager.popBackStackImmediate();
            }


        }
    }

    private void parseDetailsData(String result) {
        try {
            JSONArray list = new JSONArray(result);
            if (null != list && list.length() > 0) {
                Gson gson = new Gson();
                quotationDataArrayList = new ArrayList<>();
                for (int i = 0; i < list.length(); i++) {
                    QuotationData item = gson.fromJson(String.valueOf((list.optJSONObject(i))), QuotationData.class);
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

                        showAlertDialoge(testObjtem, position);

                    }
                });


            } else {
                // no consumers added . please add consumer first
                viewNoQuationsAdded.setVisibility(View.VISIBLE);
                quotationListView.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void showAlertDialoge(final QuotationData testObjtem , final int position) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Award Quotation")
                .setMessage("Are you sure you want to award this quotation? \n" +
                        "• Quotation Amount Name: "+ testObjtem.quoteAmount +"\n"
                        )

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        awardPosition = position;
                        quoteId = testObjtem.id;
                        Intent intent = new Intent(getActivity(), PaymentActivity.class);
                        intent.putExtra("productName", productNameString);
                        intent.putExtra("price", testObjtem.quoteAmount);
                        intent.putExtra("quantity", quantityString);
                        startActivityForResult(intent, MainActivity.REQUEST_CODE);
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.productName) {
            Fragment newFragment = new ProductDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("selectedId", SharedPreferences.getString(getActivity(), "entityId"));
            bundle.putString("productId", productId);
            newFragment.setArguments(bundle);
            MainActivity.addActionFragment(newFragment);
        }

    }
    public void awardQuoteMethod(){
     new AwardQuotationTask().execute();
    }

    public class AwardQuotationTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... arg0) {

            try {
                URL url = new URL(WebServiceConstants.AWARD_QUOTATION + quoteId);

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
                if (null != object) {
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
            } else {
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
            if (isVisible()) {
                if (null != result) {
                    try {
                        object = new JSONObject(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (null != object) {
                        if ((!object.optString("status").isEmpty() && ((Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_BAD_REQUEST)
                                || (Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_UNAUTHORIZED))) || ((Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_FORBIDDEN))) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "Something went wrong please try again",
                                    Toast.LENGTH_LONG).show();
                        } else {

                            new FetchDetailsTask().execute();
                            parseOrderListingData(object);


                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Something went wrong please try again",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Something went wrong please try again",
                            Toast.LENGTH_LONG).show();
                }
            } else {
                FragmentManager fragmentManager = MainActivity.fragmentManager;
                fragmentManager.popBackStackImmediate();
            }


        }
    }

    void parseOrderListingData(JSONObject object) {
       productName.setText(object.optString("productName"));
       productNameString = object.optString("productName");
       quantityString = object.optInt("quantity");
       
      quantity.setText(object.optString("quantity") +" Nos");
        orderId = object.optString("id");
        productId = object.optString("productId");

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Quotations");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.over_flow_item);
        if(item!=null)
            item.setVisible(false);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO your code to hide item here
        super.onCreateOptionsMenu(menu, inflater);
    }

}
