package com.app.carton.consumer;


import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.carton.orders.R;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aggarwal.swati on 12/27/18.
 */

public class CreateOrderFragment extends Fragment implements View.OnClickListener {
    private static View view;

    private static EditText quantityET;


    CreateOrderRequest request = new CreateOrderRequest();
    private ProgressBar progressBar;
    DataView data = new DataView();
    private Button createOrderBtn, inviteQuotations;
    private EditText height, width,length;
    String productId;
    Spinner productsSpinner;
    boolean isFromProductDetail = false;
    TextView productName;
    private ArrayList<ProductsDetailsItem> productList = new ArrayList<>();
    private String productNameString;
    private DimensionClass dimension;

    public CreateOrderFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.create_order, container, false);

        isFromProductDetail = getArguments().containsKey("isFromProductDetail") ? getArguments().getBoolean("isFromProductDetail") : false;
           productId = getArguments().containsKey("productId")? getArguments().getString("productId") :"";


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initViews();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) { ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true); }

        getActivity().setTitle("Place Order");
    }

    // Initiate Views
    private void initViews() {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        createOrderBtn = (Button)view.findViewById(R.id.createOrderBtn);
        quantityET = (EditText) view.findViewById(R.id.quantityET);
         productsSpinner = (Spinner) view.findViewById(R.id.productsSpinner);
        createOrderBtn.setOnClickListener(this);
        height = (EditText) view.findViewById(R.id.heightLL);
        width = (EditText) view.findViewById(R.id.widthLL);
        length = (EditText)view.findViewById(R.id.lengthLL);
        productName = (TextView) view.findViewById(R.id.productName);
        inviteQuotations = (Button)view.findViewById(R.id.inviteQuotations);
        inviteQuotations.setOnClickListener(this);
        if (isFromProductDetail){
            productsSpinner.setVisibility(View.GONE);
            height.setText(dimension.getHeight() + "");
            width.setText(dimension.getWidth() + "");
            length.setText(dimension.getLength() + "");
            productName.setText(productNameString);
        }else{
            productName.setVisibility(View.GONE);
            productsSpinner.setVisibility(View.VISIBLE);
        }
        List<String> productListString = new ArrayList<String>();
       for (int i= 0 ; i< productList.size() ; i ++){
           productListString.add(productList.get(i).name);
       }


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterProducts = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, productListString);

        // Drop down layout style - list view with radio button
        dataAdapterProducts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        productsSpinner.setAdapter(dataAdapterProducts);

        productsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                productId = (productList.get(i).id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    @Override
    public void onClick(View view) {
       
        if (view.getId() == R.id.createOrderBtn || view.getId() == R.id.inviteQuotations) {
            // move to next screen

            if (!TextUtils.isEmpty(quantityET.getText())) {
                request.setQuantity(Integer.parseInt(quantityET.getText().toString()));
            } else {
                Toast.makeText(getActivity(), "Please input quantity", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(width.getText())) {
                Toast.makeText(getActivity(), "Please input box width", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(height.getText())) {
                Toast.makeText(getActivity(), "Please input box height", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(length.getText())) {
                Toast.makeText(getActivity(), "Please input box length", Toast.LENGTH_SHORT).show();
                return;
            }

            DimensionClass dimensionClass = new DimensionClass();
            dimensionClass.setHeight(Integer.parseInt(height.getText().toString()));
            dimensionClass.setWidth(Integer.parseInt(width.getText().toString()));
            dimensionClass.setLength(Integer.parseInt(length.getText().toString()));

            request.setDimension(dimensionClass);
            if (view.getId() == R.id.inviteQuotations){
                request.setQuotesInvited(true);
            }
            request.setProductId(productId);

            new SendPostRequest().execute();

//            postDataToServer();


        }
    }

    public void setProductList(ArrayList<ProductsDetailsItem> productList) {
        this.productList = productList;

    }

    public void setProductName(String productName) {
        this.productNameString = productName;
    }

    public void setDimension(DimensionClass dimension) {
        this.dimension = dimension;
    }


    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL(WebServiceConstants.CREATE_ORDER);
                JSONObject object = null;
                Gson gson = new Gson();
                String json = gson.toJson(request);
                try {
                    object = new JSONObject(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
                writer.write(object.toString());

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
            progressBar.setVisibility(View.GONE);
            try {
                JSONObject object = new JSONObject(result);
                if (!object.optString("status").isEmpty() && (Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_BAD_REQUEST
                        || Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_UNAUTHORIZED)
                        || Integer.valueOf(object.optString("status"))== HttpURLConnection.HTTP_FORBIDDEN) {
                    Toast.makeText(getActivity(), object.optString("message"),
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Order Created successfully",
                            Toast.LENGTH_LONG).show();
                    new MainActivity().replaceLoginFragment(new ConsumerProductsListFragment ());
                }

//                new MainActivity().replaceLoginFragment(new ChangePasswordFragment());
            } catch (JSONException e) {
                e.printStackTrace();
            }
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