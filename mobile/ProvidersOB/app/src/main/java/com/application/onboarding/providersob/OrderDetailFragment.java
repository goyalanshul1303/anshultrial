package com.application.onboarding.providersob;

import android.content.Context;
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
import android.widget.ImageView;
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
import java.util.ArrayList;

/**
 * Created by aggarwal.swati on 3/14/19.
 */

public class OrderDetailFragment extends Fragment implements View.OnClickListener {

    private static View view;

    private ProgressBar progressBar;
    private Button addQuotationBtn, checkQuotations;
    TextView productName, email,contactName, quantity,printingType, cartonType, corrugationType,sheetLayerType;
    String orderId,productId;
    LinearLayout parentLL,statuLL;
    boolean isForQuotations ;
    private int orderStatus;
    private ArrayList<OrderStatus> statusarrayList = new ArrayList<>();

    public OrderDetailFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            isForQuotations = getArguments().containsKey("isForQuotations") ? getArguments().getBoolean("isForQuotations") : false;
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
        checkQuotations= (Button)view.findViewById(R.id.checkQuotations);
        quantity = (TextView)view.findViewById(R.id.expectedQuantity);
        sheetLayerType = (TextView)view.findViewById(R.id.sheetLayerType);
        productName = (TextView)view.findViewById(R.id.productName);
        printingType = (TextView)view.findViewById(R.id.printingType);
        corrugationType = (TextView)view.findViewById(R.id.corrugationType);
        parentLL= (LinearLayout)view.findViewById(R.id.parentLL);
        cartonType = (TextView)view.findViewById(R.id.cartonType);
        addQuotationBtn.setOnClickListener(this);
        checkQuotations.setOnClickListener(this);
        new FetchOrderDetailsTask().execute();
        statuLL = (LinearLayout) view.findViewById(R.id.statuLL);
        if (isForQuotations){
            checkQuotations.setVisibility(View.VISIBLE);
            addQuotationBtn.setVisibility(View.GONE);
            // show status change button
        }else{
            checkQuotations.setVisibility(View.GONE);
            addQuotationBtn.setVisibility(View.GONE);
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
//        addQuotationBtn.setVisibility(View.VISIBLE);
//        if ( isFromAwarded) {
            if (!isForQuotations && orderStatus >=4 && orderStatus !=10){
                addQuotationBtn.setVisibility(View.VISIBLE);
            }else{
                addQuotationBtn.setVisibility(View.GONE);
            }
//                addQuotationBtn.setVisibility(View.GONE);

//        }

        addOrderStatus(orderStatus, statusarrayList);
        Utils.setDetailsTextField("Product Name", getActivity(), productName, result.optString("name"));
        Utils.setDetailsTextField("Carton Type", getActivity(), cartonType, result.optString("cartonType"));
        Utils.setDetailsTextField("Sheet Layer Type", getActivity(), sheetLayerType, result.optString("sheetLayerType"));
        Utils.setDetailsTextField("Corrugation Type", getActivity(), corrugationType, String.valueOf(result.optString("corrugationType")));
        Utils.setDetailsTextField("Printing Type", getActivity(), printingType, String.valueOf(result.optString("printingType")));

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addQuotationBtn){
//            if (!isFromAwarded){
//                AddQuotationFragment fragment = new AddQuotationFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString("orderId", orderId);
//                bundle.putString("productId", productId);
//                fragment.setArguments(bundle);
//                MainActivity.addActionFragment(fragment);
//            }else{
                // update order status
                new UpdateOrderStatusTask().execute();
//            }

        }else if(view.getId() == R.id.checkQuotations){
                Bundle bundle = new Bundle();
                bundle.putString("orderId", orderId);
                Fragment newFragment = new QuotationListingFragment();
                newFragment.setArguments(bundle);
                MainActivity.addActionFragment(newFragment);
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
            orderStatus = object.optInt("orderStatus");
            productId = object.optString("productId");
            Gson gson = new Gson();
         if (null!= object.optJSONArray("statuses")){
            for (int i = 0 ;i <   object.optJSONArray("statuses").length(); i++){
                OrderStatus orderStatus = gson.fromJson(String.valueOf(object.optJSONArray("statuses").opt(i)), OrderStatus.class);
                statusarrayList.add( orderStatus);
            }

        }


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
                orderStatus += 1;
                string.append(String.valueOf(orderStatus));
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

                            Toast.makeText(getActivity(), "Something went wrong please try again",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(), "Order status update Successfully",
                                    Toast.LENGTH_LONG).show();
                            FragmentManager fragmentManager = MainActivity.fragmentManager;
                            fragmentManager.popBackStackImmediate();

                        }
                    } else {
                        progressBar.setVisibility(View.GONE);

                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Something went wrong please try again",
                            Toast.LENGTH_LONG).show();
                }
            }else{
                FragmentManager fragmentManager = MainActivity.fragmentManager;
                fragmentManager.popBackStackImmediate();
            }


        }
    }
    private void addOrderStatus(int status, ArrayList<OrderStatus> arrayList) {
        LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int i =0;
        for ( i = 0 ; i < arrayList.size() ; i++){
            View v = vi.inflate(R.layout.order_status, null);
            // fill in any details dynamically here
            TextView textView = (TextView) v.findViewById(R.id.statusText);
            TextView textDateView = (TextView) v.findViewById(R.id.statusDate);
            TextView onText = (TextView)v.findViewById(R.id.onText);
            OrderStatus statusObj = arrayList.get(i);
            onText.setVisibility(View.VISIBLE);
            textDateView.setVisibility(View.VISIBLE);
            textDateView.setText(Utils.getDate(statusObj.statusDate));
            textView.setText(Utils.getOrderStatusText(statusObj.status));
            ImageView view = (ImageView) v.findViewById(R.id.doneImage);
            view.setBackgroundResource(R.drawable.thumbsup);
//            else{
//                view.setBackgroundResource(R.drawable.undelivered);
//            }
            statuLL.addView(v);
        }
        for ( int j = i+1  ; j <= 9 ; j++){
            View v = vi.inflate(R.layout.order_status, null);
            TextView textView = (TextView) v.findViewById(R.id.statusText);
            textView.setText(Utils.getOrderStatusText(j));
            ImageView view = (ImageView) v.findViewById(R.id.doneImage);
            view.setBackgroundResource(R.drawable.undelivered);
            TextView textDateView = (TextView) v.findViewById(R.id.statusDate);
            TextView onText = (TextView)v.findViewById(R.id.onText);
            onText.setVisibility(View.GONE);
            textDateView.setVisibility(View.GONE);
            statuLL.addView(v);

        }
    }
}
