package com.app.carton.orders;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by aggarwal.swati on 12/27/18.
 */

public class CreateOrderFragment extends Fragment implements View.OnClickListener {
    private static View view;

    private static EditText quantityET;
    private Spinner consumerTypeSpinner, typesPrintingSpinner;
    FlowLayout typesCartonCheckboxLL, typesOfBoxes, typeOfCorrugation;

    CreateOrderRequest request = new CreateOrderRequest();
    private ProgressBar progressBar;
    DataView data = new DataView();
    private Button createOrderBtn,previousSample , newSample;

    public CreateOrderFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.create_order, container, false);
        initViews();
        return view;
    }

    // Initiate Views
    private void initViews() {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        typesOfBoxes = (FlowLayout) view.findViewById(R.id.typeBoxesCheckboxLL);
        typesCartonCheckboxLL = (FlowLayout) view.findViewById(R.id.typesCartonCheckboxLL);
        typesPrintingSpinner = (Spinner) view.findViewById(R.id.typesPrintingSpinner);
        typeOfCorrugation = (FlowLayout) view.findViewById(R.id.typeOfCorrugationLL);
        consumerTypeSpinner = (Spinner) view.findViewById(R.id.spinnerConsumerType);
        createOrderBtn = (Button)view.findViewById(R.id.createOrderBtn);
        quantityET = (EditText) view.findViewById(R.id.quantityET);
        previousSample = (Button)view.findViewById(R.id.previousSample);
        newSample = (Button)view.findViewById(R.id.newSample);
        createOrderBtn.setOnClickListener(this);
        inflateDataView();


    }

    private void inflateDataView() {

        data.setBoxType(new LinkedHashMap<Integer, String>());
        data.setConsumerType(new LinkedHashMap<Integer, String>());
        data.setCorrugationType(new LinkedHashMap<Integer, String>());
        data.setTypeOfPrinting(new LinkedHashMap<Integer, String>());
        data.setTypesOfCartons(new LinkedHashMap<Integer, String>());
        List<String> typesPrinting = new ArrayList<String>();
        List<String> consumerTypes = new ArrayList<String>();


        for (Map.Entry<Integer, String> entry : data.getConsumerType().entrySet()) /** Loop through all entrys in the HashMap **/ {
            consumerTypes.add(entry.getValue());
        }

        for (Map.Entry<Integer, String> entry : data.getTypeOfPrinting().entrySet()) /** Loop through all entrys in the HashMap **/ {
            typesPrinting.add(entry.getValue());
        }


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterPrinting = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, typesPrinting);

        // Drop down layout style - list view with radio button
        dataAdapterPrinting.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // attaching data adapter to spinner
        typesPrintingSpinner.setAdapter(dataAdapterPrinting);

        typesPrintingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                request.setPrintingType((Integer) Utils.getElementByIndex(data.getTypeOfPrinting(), i));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterConsumer = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, consumerTypes);

        // Drop down layout style - list view with radio button
        dataAdapterConsumer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // attaching data adapter to spinner
        consumerTypeSpinner.setAdapter(dataAdapterConsumer);

        consumerTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                request.setConsumerType((Integer) Utils.getElementByIndex(data.getConsumerType(), i));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        for (Map.Entry<Integer, String> entry : data.getTypesOfCartons().entrySet()) {
            final CheckBox cb = new CheckBox(getActivity());
            cb.setText(entry.getValue());
            typesCartonCheckboxLL.addView(cb);
            cb.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Object key = Utils.getKeyFromValue(data.getTypesOfCartons(), cb.getText().toString());
                    if (cb.isChecked()) {
                        if (null != key) {
                            request.getCartonType().add((Integer) key);
                        }
                    } else {
                        if (null != key && request.getCartonType().contains(key)) {
                            request.getCartonType().remove(key);

                        }
                    }
                    request.setCartonType(request.getCartonType());
                }
            });
        }

        for (Map.Entry<Integer, String> entry : data.getBoxType().entrySet()) {
            final CheckBox cb = new CheckBox(getActivity());
            cb.setText(entry.getValue());
            typesOfBoxes.addView(cb);
            cb.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Object key = Utils.getKeyFromValue(data.getBoxType(), cb.getText().toString());
                    if (cb.isChecked()) {
                        if (null != key) {
                            request.getSupportedSheetLayers().add((Integer) key);
                        }
                    } else {
                        if (null != key && request.getSupportedSheetLayers().contains(key)) {
                            request.getSupportedSheetLayers().remove(key);

                        }
                    }
                    request.setSupportedSheetLayers(request.getSupportedSheetLayers());
                }
            });
        }
        for (Map.Entry<Integer, String> entry : data.getCorrugationType().entrySet()) {
            final CheckBox cb = new CheckBox(getActivity());
            cb.setText(entry.getValue());
            typeOfCorrugation.addView(cb);
            cb.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Object key = Utils.getKeyFromValue(data.getCorrugationType(), cb.getText().toString());
                    if (cb.isChecked()) {
                        if (null != key) {
                            request.getCorrugationType().add((Integer) key);
                        }
                    } else {
                        if (null != key && request.getCorrugationType().contains(key)) {
                            request.getCorrugationType().remove(key);

                        }
                    }
                    request.setCorrugationType(request.getCorrugationType());
                }
            });
        }


    }


    @Override
    public void onClick(View view) {
       
        if (view.getId() == R.id.createOrderBtn) {
            // move to next screen

            if (request.getConsumerType() == -1) {
                Toast.makeText(getActivity(), "Please select consumer type", Toast.LENGTH_SHORT).show();
                return;
            }

            if (request.getCartonType().isEmpty()) {
                Toast.makeText(getActivity(), "Please select carton type", Toast.LENGTH_SHORT).show();
                return;
            }
            if (request.getPrintingType() == -1) {
                Toast.makeText(getActivity(), "Please select printing type", Toast.LENGTH_SHORT).show();
                return;
            }

            if (request.getCorrugationType().isEmpty()) {
                Toast.makeText(getActivity(), "Please select corrugation type", Toast.LENGTH_SHORT).show();
                return;
            }

            if (request.getSupportedSheetLayers().isEmpty()) {
                Toast.makeText(getActivity(), "Please select supported sheets type", Toast.LENGTH_SHORT).show();
                return;
            }
            if (quantityET.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please select quanity", Toast.LENGTH_SHORT).show();
                return;
            } else {
                request.setExpectedQuantity(Integer.parseInt(quantityET.getText().toString()));
            }



            new SendPostRequest().execute();

//            postDataToServer();


        }
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
            Toast.makeText(getActivity(), result,
                    Toast.LENGTH_LONG).show();
        }
    }
}