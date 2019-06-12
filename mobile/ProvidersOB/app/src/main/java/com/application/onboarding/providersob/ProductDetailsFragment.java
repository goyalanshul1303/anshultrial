package com.application.onboarding.providersob;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by aggarwal.swati on 2/12/19.
 */

public class ProductDetailsFragment extends Fragment implements View.OnClickListener {

    private static View view;

    private ProgressBar progressBar;
    private Button addOrderBtn, viewOffersBtn;
    TextView productName, email,contactName, quantity,printingType, consumerScale, cartonType, corrugationType,sheetLayerType;
    String consumerId,productId;
    private String productNameString;
    DimensionClass dimensionClass = new DimensionClass();
    private String price;
    ArrayList<OffersData> offersList = new ArrayList<>();

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
        viewOffersBtn = (Button)view.findViewById(R.id.viewOffersBtn);
        quantity = (TextView)view.findViewById(R.id.expectedQuantity);
        sheetLayerType = (TextView)view.findViewById(R.id.sheetLayerType);
        productName = (TextView)view.findViewById(R.id.productName);
        printingType = (TextView)view.findViewById(R.id.printingType);
        corrugationType = (TextView)view.findViewById(R.id.corrugationType);
        cartonType = (TextView)view.findViewById(R.id.cartonType);
        addOrderBtn.setVisibility(View.VISIBLE);
        addOrderBtn.setOnClickListener(this);
        viewOffersBtn.setOnClickListener(this);
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
                        progressBar.setVisibility(View.VISIBLE);
                        new FetchOffersTask().execute();
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
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) { ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true); }
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
    public class FetchOffersTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... arg0) {

            try {
                SpannableStringBuilder string = new SpannableStringBuilder(WebServiceConstants.GET_SINGLE_PRODUCT_OFFERS);

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
                offersList = new ArrayList<>();
                if (null!=object) {
                    if (!object.optString("status").isEmpty() && (Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_BAD_REQUEST
                            || Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_UNAUTHORIZED)) {
                        Toast.makeText(getActivity(), "Something went wrong please try again",
                                Toast.LENGTH_LONG).show();
                    } else {
                        // show offers strip
                        if (object.has("offers")){
                            try {
                                Gson gson=new Gson();
                                for (int i =0 ; i< object.getJSONArray("offers").length(); i ++){
                                    OffersData item=gson.fromJson(String.valueOf(object.getJSONArray("offers").getJSONObject(i)),OffersData.class);
                                    offersList.add(item);
                                }
                                if (offersList.size()>0){
                                    viewOffersBtn.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
            }else  {
                FragmentManager fragmentManager = MainActivity.fragmentManager;
                fragmentManager.popBackStackImmediate();
            }


        }
    }



    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addOrderBtn){
            showAddpriceDialog();
        }
       else if (view.getId() == R.id.viewOffersBtn){
            AddedOffersListFragment fragment = new AddedOffersListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("productId",productId);
            fragment.setOfferList (offersList);
            fragment.setArguments(bundle);
            MainActivity.addActionFragment(fragment);
        }

    }

    private void showAddpriceDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        final EditText edittext = new EditText(getActivity());
        alert.setTitle("Enter Your Offer Price");

        alert.setView(edittext);

        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                Editable textValue = edittext.getText();
                if (textValue.toString().isEmpty()){
                    Toast.makeText(getActivity(), "Please input price ", Toast.LENGTH_LONG);
                }else{
                    price= textValue.toString();
                    new AddPriceTask().execute();
                }

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
                dialog.dismiss();
            }
        });

        alert.show();
    }

    public class AddPriceTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... arg0) {

            try {
                SpannableStringBuilder string = new SpannableStringBuilder(WebServiceConstants.ADD_PRICE);
                string.append(productId);
                string.append("/");
                string.append(price);
                URL url = new URL(string.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("PUT");
                conn.setDoInput(true);
                conn.setDoOutput(true);
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

            progressBar.setVisibility(View.GONE);
            if (isVisible()) {
                if (null != result && result.equalsIgnoreCase("true")) {
                    Toast.makeText(getActivity(), "Price Added Successfully",
                            Toast.LENGTH_LONG).show();
                    FragmentManager fragmentManager = MainActivity.fragmentManager;
                    fragmentManager.popBackStackImmediate();
                } else {
                    Toast.makeText(getActivity(), "Something went wrong please try again",
                            Toast.LENGTH_LONG).show();
                }

            }else{

                FragmentManager fragmentManager = MainActivity.fragmentManager;
                fragmentManager.popBackStackImmediate();

            }
        }
    }
}
