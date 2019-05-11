package com.application.onboarding.providersob;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by aggarwal.swati on 5/11/19.
 */

public class AddedOffersListFragment extends Fragment implements View.OnClickListener {

    private static View view;

    private RecyclerView offerRecyclerView;

    private ProgressBar progressBar;
    private String urlType;
    DataView data = new DataView();
    private AddedOffersAdapter adapter;
    View viewNoProductAdded;
    ArrayList<OffersData> offersDataArrayList = new ArrayList<>();
    private Button offerpriceBtn;
    String customerType = "";
    private String selectedId = "";
    private String price;
    private String productId;


    public AddedOffersListFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null){
            productId = getArguments().containsKey("productId")? getArguments().getString("productId") :"";

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.products_list, container, false);
        initViews();
        return view;
    }


    // Initiate Views
    private void initViews() {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        offerRecyclerView = (RecyclerView) view.findViewById(R.id.productsRecyclerView);
        offerpriceBtn= (Button) view.findViewById(R.id.offerpriceBtn);
        parseListingData(offersDataArrayList);
        offerpriceBtn.setOnClickListener(this);
        offerpriceBtn.setVisibility(View.VISIBLE);

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Offers List");

    }



    private void parseListingData(ArrayList<OffersData> offersData) {
            if (null!= offersData && offersData.size() > 0 ) {
                adapter = new AddedOffersAdapter(getActivity(), offersDataArrayList);
                offerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                offerRecyclerView.setAdapter(adapter);
                offerRecyclerView.setVisibility(View.VISIBLE);

            }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.offerpriceBtn){
            showAddpriceDialog();
        }
    }

    public void setOfferList(ArrayList<OffersData> offerList) {
        this.offersDataArrayList = offerList;

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
