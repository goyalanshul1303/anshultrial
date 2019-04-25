package com.application.onboarding.providersob;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by aggarwal.swati on 2/12/19.
 */

public class ProductDetailsFragment extends Fragment implements View.OnClickListener {

    private static View view;

    private ProgressBar progressBar;
    private Button addOrderBtn;
    TextView productName, email,contactName, quantity,printingType, consumerScale, cartonType, corrugationType,sheetLayerType;
    String consumerId,productId;
    private String productNameString;
    DimensionClass dimensionClass = new DimensionClass();
    public ProductDetailsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            consumerId = getArguments().containsKey("selectedId") ? getArguments().getString("selectedId") : "";
            productId = getArguments().containsKey("productId")? getArguments().getString("productId") :"";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.added_product_details, container, false);
        initViews();
        return view;
    }


    // Initiate Views
    private void initViews() {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        addOrderBtn = (Button)view.findViewById(R.id.addOrderBtn);
        quantity = (TextView)view.findViewById(R.id.expectedQuantity);
        sheetLayerType = (TextView)view.findViewById(R.id.sheetLayerType);
        productName = (TextView)view.findViewById(R.id.productName);
        printingType = (TextView)view.findViewById(R.id.printingType);
        corrugationType = (TextView)view.findViewById(R.id.corrugationType);
        cartonType = (TextView)view.findViewById(R.id.cartonType);
        addOrderBtn.setOnClickListener(this);
        new FetchDetailsTask().execute();

    }
    public class FetchDetailsTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... arg0) {

            try {
                SpannableStringBuilder string = new SpannableStringBuilder(WebServiceConstants.GET_SINGLE_PRODUCT);
                string.append(consumerId);
                string.append("/");
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
            if (null != result && isVisible()) {
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
                    FragmentManager fragmentManager = MainActivity.fragmentManager;
                    fragmentManager.popBackStackImmediate();


            }


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Product Details");
    }

    private void parseListingData(JSONObject result) {
        productNameString = result.optString("name");
        Utils.setDetailsTextField("Name", getActivity(), productName, result.optString("name"));

        Utils.setDetailsTextField("Carton Type", getActivity(), cartonType, result.optString("cartonType"));
        Utils.setDetailsTextField("Sheet Layer Type", getActivity(), sheetLayerType, result.optString("sheetLayerType"));
            quantity.setVisibility(View.GONE);
//        Utils.setDetailsTextField("Quantity ", getActivity(), quantity, result.optString("quantity"));

        Utils.setDetailsTextField("Corrugation Type", getActivity(), corrugationType, String.valueOf(result.optString("corrugationType")));
        Utils.setDetailsTextField("Printing Type", getActivity(), printingType, String.valueOf(result.optString("printingType")));
        Gson gson = new Gson();
         dimensionClass = gson.fromJson(String.valueOf(result.optJSONObject("dimension")),DimensionClass.class);

    }

    @Override
    public void onClick(View view) {
//        if (view.getId() == R.id.addOrderBtn){
//            CreateOrderFragment fragment = new CreateOrderFragment();
//            Bundle bundle = new Bundle();
//            bundle.putBoolean("isFromProductDetail", true);
//            bundle.putString("productId", productId);
//            fragment.setDimension(dimensionClass);
//            fragment.setProductName(productNameString);
//            fragment.setArguments(bundle);
//            MainActivity.addActionFragment(fragment);
//        }

    }
}
