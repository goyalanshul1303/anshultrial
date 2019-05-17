package com.app.carton.provider;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

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

/**
 * Created by aggarwal.swati on 3/14/19.
 */

public class OrderDetailFragment extends Fragment implements View.OnClickListener {

    private static View view;

    private ProgressBar progressBar;
    private Button addQuotationBtn;
    TextView productName, email,contactName, quantity,printingType, consumerScale, cartonType, corrugationType,sheetLayerType;
    String orderId,productId;
    LinearLayout parentLL;
    boolean isFromAwarded ;
    public OrderDetailFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            isFromAwarded = getArguments().containsKey("isFromAwardedScreen") ? getArguments().getBoolean("isFromAwardedScreen") : false;
            orderId = getArguments().containsKey("orderId") ? getArguments().getString("orderId") : "";
            productId = getArguments().containsKey("productId")? getArguments().getString("productId") :"";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.placed_order_details, container, false);
        initViews();
        return view;
    }


    // Initiate Views
    private void initViews() {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        addQuotationBtn = (Button)view.findViewById(R.id.addQuotationBtn);
        quantity = (TextView)view.findViewById(R.id.expectedQuantity);
        sheetLayerType = (TextView)view.findViewById(R.id.sheetLayerType);
        productName = (TextView)view.findViewById(R.id.productName);
        printingType = (TextView)view.findViewById(R.id.printingType);
        corrugationType = (TextView)view.findViewById(R.id.corrugationType);
        parentLL= (LinearLayout)view.findViewById(R.id.parentLL);
        cartonType = (TextView)view.findViewById(R.id.cartonType);
        addQuotationBtn.setOnClickListener(this);
        new FetchOrderDetailsTask().execute();
        addQuotationBtn.setVisibility(View.GONE);
        if (!isFromAwarded){
            addQuotationBtn.setText("Update Order Status");
            // show status change button
        }

    }
    public class FetchDetailsTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {

            try {
                SpannableStringBuilder string = new SpannableStringBuilder(WebServiceConstants.GET_SINGLE_PRODUCT);
                string.append(productId);
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
                try {
                    object = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (null!=object) {
                    if (!object.optString("status").isEmpty() && (Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_BAD_REQUEST
                            || Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_UNAUTHORIZED)) {
                        Toast.makeText(getActivity(), "Something went wrong please try again",
                                Toast.LENGTH_LONG).show();
                    } else {
                        parseListingData(object);

                    }
                }
            }else  {
                Toast.makeText(getActivity(), "Something went wrong please try again",
                        Toast.LENGTH_LONG).show();
            }

            }else{
                FragmentManager fragmentManager = MainActivity.fragmentManager;
                fragmentManager.popBackStackImmediate();
            }

        }
    }

    private void parseListingData(JSONObject result) {
        addQuotationBtn.setVisibility(View.VISIBLE);
        Utils.setDetailsTextField("Product Name", getActivity(), productName, result.optString("name"));
        Utils.setDetailsTextField("Carton Type", getActivity(), cartonType, result.optString("cartonType"));
        Utils.setDetailsTextField("Sheet Layer Type", getActivity(), sheetLayerType, result.optString("sheetLayerType"));

        Utils.setDetailsTextField("Corrugation Type", getActivity(), corrugationType, String.valueOf(result.optString("corrugationType")));
        Utils.setDetailsTextField("Printing Type", getActivity(), printingType, String.valueOf(result.optString("printingType")));

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addQuotationBtn){
            if (!isFromAwarded){
                AddQuotationFragment fragment = new AddQuotationFragment();
                Bundle bundle = new Bundle();
                bundle.putString("orderId", orderId);
                bundle.putString("productId", productId);
                fragment.setArguments(bundle);
                MainActivity.addActionFragment(fragment);
            }else{
                // update order status
                new UpdateOrderStatusTask().execute();
            }

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) { ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true); }

        getActivity().setTitle("Order Details");
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
//            Utils.setDetailsTextField("Product Name", getActivity(), productName, object.optString("productName"));
            Utils.setDetailsTextField("Quantity ", getActivity(), quantity, object.optString("quantity"));
            orderId = object.optString("id");
            productId = object.optString("productId");

    }

    public class UpdateOrderStatusTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... arg0) {

            try {
                SpannableStringBuilder string = new SpannableStringBuilder(WebServiceConstants.UPDATE_ORDER_STATUS);
                string.append("/");
                string.append(orderId);
                string.append("/");
                string.append(String.valueOf(2));
                URL url = new URL(string.toString());

//                URL url = new URL(WebServiceConstants.UPDATE_ORDER_STATUS);
//                JSONObject object = new JSONObject();
//                object.put("orderId", orderId);
//                object.put("statusId", 2);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("PUT");
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
                    if ((!object.optString("status").isEmpty() && ((Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_BAD_REQUEST)
                            || (Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_UNAUTHORIZED))) || ((Integer.valueOf(object.optString("status")) ==  HttpURLConnection.HTTP_FORBIDDEN))) {

                        Toast.makeText(getActivity(), "Something went wrong please try again",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Order status update Successfully",
                                Toast.LENGTH_LONG).show();



                    }
                }else{
                    progressBar.setVisibility(View.GONE);

                }
            }else  {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Something went wrong please try again",
                        Toast.LENGTH_LONG).show();
            }


        }
    }
}
