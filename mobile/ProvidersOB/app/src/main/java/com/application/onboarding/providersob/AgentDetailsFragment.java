package com.application.onboarding.providersob;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by aggarwal.swati on 2/12/19.
 */

public class AgentDetailsFragment extends Fragment implements View.OnClickListener {

    private static View view;

    private ProgressBar progressBar;
    private String urlType;
    DataView data = new DataView();
    private ConsumersItemAdapter adapter;
    ArrayList<ConsumerDetailsItem> consumerDetailsItems = new ArrayList<>();
    private Button addProductBtn;
    TextView custmerName, email,contactName, quantity, pan, website, foundationYear,consumerType, consumerScale, cartonType, sample,expectedQuantityFrequency;
String id;
    public AgentDetailsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String customerType = "";
        if (getArguments() != null){
            customerType = getArguments().containsKey("urlType") ? getArguments().getString("urlType") : "";
            id = getArguments().containsKey("selectedId") ? getArguments().getString("selectedId") : "";
        }
        if (customerType.equalsIgnoreCase("consumers")){
            urlType = WebServiceConstants.CREATE_CONSUMER;
        }else{
            urlType = WebServiceConstants.CREATE_PROVIDER;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.agent_boarded_details, container, false);
        initViews();
        return view;
    }


    // Initiate Views
    private void initViews() {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        addProductBtn = (Button)view.findViewById(R.id.addProductBtn);
        quantity = (TextView)view.findViewById(R.id.expectedQuantity);
        expectedQuantityFrequency = (TextView)view.findViewById(R.id.expectedQuantityFrequency);
        custmerName = (TextView)view.findViewById(R.id.customerName);
        email = (TextView)view.findViewById(R.id.customerEmail);
        contactName = (TextView)view.findViewById(R.id.contactName);
        consumerScale = (TextView)view.findViewById(R.id.consumerScale);
        consumerType = (TextView)view.findViewById(R.id.consumerTye);
        foundationYear = (TextView)view.findViewById(R.id.foundationYear);
        pan = (TextView)view.findViewById(R.id.panNumber);
        website= (TextView)view.findViewById(R.id.website);
        sample = (TextView)view.findViewById(R.id.sampleCollection);
        cartonType = (TextView)view.findViewById(R.id.cartonType);
        addProductBtn.setOnClickListener(this);
        new FetchDetailsTask().execute();

    }
    public class FetchDetailsTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... arg0) {

            try {
                SpannableStringBuilder string = new SpannableStringBuilder(urlType);
                string.append("/");
                string.append(id);
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

//
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
//
                    } else {
                        parseListingData(object);

                    }
                }
            }else  {
                Toast.makeText(getActivity(), "Something went wrong please try again",
                        Toast.LENGTH_LONG).show();
            }


        }
    }

    private void parseListingData(JSONObject result) {
        Utils.setDetailsTextField("Customer Name", getActivity(), custmerName, result.optString("consumerName"));
        Utils.setDetailsTextField("Contact Name", getActivity(), contactName, result.optString("contactName"));

        Utils.setDetailsTextField("Email", getActivity(), email, result.optString("email"));
        Utils.setDetailsTextField("Website Link", getActivity(), website, result.optString("website"));

        Utils.setDetailsTextField("Foundation Year", getActivity(), foundationYear, result.optString("foundationYear"));
        Utils.setDetailsTextField("Company PAN ", getActivity(), pan, result.optString("companyPAN"));

        Utils.setDetailsTextField("Consumer Type", getActivity(), consumerType, result.optString("consumerType"));

        Utils.setDetailsTextField("Consumer Scale", getActivity(), consumerScale, result.optString("consumerScale"));
        String cartonTypeString = "";
        if (null!= result.optString("cartonType")){
            try {
                 JSONArray cartonArray = new JSONArray(result.optString("cartonType"));
                if (null!=cartonArray){
                    ArrayList<String> stringArray = new ArrayList<String>();
                    for(int i = 0, count = cartonArray.length(); i< count; i++)
                    {
                        try {
                            String string = cartonArray.getString(i);
                            stringArray.add(string);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                   cartonTypeString =  Utils.toCSV(stringArray);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Utils.setDetailsTextField("Carton Type", getActivity(), cartonType, cartonTypeString);
        Utils.setDetailsTextField("Expected Quantity Frequency", getActivity(), expectedQuantityFrequency, result.optString("expectedQuantityFrequency"));

        Utils.setDetailsTextField("Quantity ", getActivity(), quantity, result.optString("expectedQuantity"));

        Utils.setDetailsTextField("Is Sample Collected", getActivity(), sample, String.valueOf(result.optBoolean("sampleCollection")));

    }

    @Override
    public void onClick(View view) {

    }
}
