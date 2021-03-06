package com.app.carton.provider;

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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by aggarwal.swati on 2/12/19.
 */

public class ProductDetailsFragment extends Fragment implements View.OnClickListener {

    private static View view;

    private ProgressBar progressBar;
    private Button offerPriceBtn;
    TextView productName, email,grammage, quantity,printingType, consumerScale, cartonType, corrugationType,sheetLayerType;
    String consumerId,productId;
    private String price = "";

    public ProductDetailsFragment() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) { ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true); }
        getActivity().setTitle("Product Details");
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
        getActivity().setTitle("Placed Orders");

        return view;
    }


    // Initiate Views
    private void initViews() {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        offerPriceBtn = (Button)view.findViewById(R.id.offerPriceBtn);
        quantity = (TextView)view.findViewById(R.id.expectedQuantity);
        sheetLayerType = (TextView)view.findViewById(R.id.sheetLayerType);
        productName = (TextView)view.findViewById(R.id.productName);
        printingType = (TextView)view.findViewById(R.id.printingType);
        corrugationType = (TextView)view.findViewById(R.id.corrugationType);
        cartonType = (TextView)view.findViewById(R.id.cartonType);
        grammage = (TextView)view.findViewById(R.id.grammage);

        offerPriceBtn.setOnClickListener(this);
        new FetchDetailsTask().execute();

    }
    public class FetchDetailsTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
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
         productName.setText(result.optString("name"));

        cartonType.setText(result.optString("cartonType"));
        sheetLayerType.setText(result.optString("sheetLayerType"));

       quantity.setText(result.optString("quantity"));
        grammage.setText(String.valueOf(result.optString("grammage") + " gsm"));

        corrugationType.setText(String.valueOf(result.optString("corrugationType")));
        printingType.setText(String.valueOf(result.optString("printingType")));

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.offerPriceBtn){
            showAddpriceDialog();
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
                SpannableStringBuilder string = new SpannableStringBuilder(WebServiceConstants.ADD_PRODUCT_OFFER);
                string.append(productId);
                URL url = new URL(string.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("PUT");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", SharedPreferences.getString(getActivity(), SharedPreferences.KEY_AUTHTOKEN));
                Calendar cal = Calendar.getInstance();
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                JSONObject object = new JSONObject();
                AddOfferRequest request = new AddOfferRequest();
                request.setProviderId(SharedPreferences.getString(getActivity(), "entityId"));
                request.setPriceOffer(price);
                request.setOfferDate(sdf.format(cal.getTime()));

                Gson gson = new Gson();
//                new GsonBuilder().create().toJson(this, Producto.class);
                String json = gson.toJson(request);

//                selectedIdString = selectedIdString.replace("[","").replace("]","").replaceAll("\\s","").trim();
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(String.valueOf(json));

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
            if (isVisible()) {
                if (null != result && result.equalsIgnoreCase("true")) {
                    Toast.makeText(getActivity(), "Offer added successfully", Toast.LENGTH_LONG).show();
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
