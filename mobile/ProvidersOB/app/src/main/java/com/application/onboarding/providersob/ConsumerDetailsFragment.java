package com.application.onboarding.providersob;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.aggarwalswati.providersob.R;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
 * Created by aggarwal.swati on 12/2/18.
 */

public class ConsumerDetailsFragment extends Fragment implements View.OnClickListener {
    private static View view;

    private static EditText companyName, contactName, email, websitelink, mobile, landline, foundationYear, annulaincome,
            gst, panCardET, creditLimitET, registeredAdd1, registeredAdd2, registeredAdd3, registeredPincode,
            registeredCountry, corresAdd1, corresAdd2, corresAdd3, corresCountry, corresPincode,quantityET,vendorET;
    private static Button nextButton;
    private Spinner frequencySpinner, consumerTypeSpinner, consumerScaleSpinner,registeredState, corresState;
    FlowLayout typesCartonCheckboxLL;

    private CheckBox sameAsRegistered;
    private int isSample = -1;


    int registeredStatePOs;

    ConsumerRequest request = new ConsumerRequest();
    DataView data = new DataView();

    private RadioGroup isSmapleRG;
    private EditText creditDaysET,otherVendorET;
    List<AddressClass> addresses = new ArrayList<>();
    private  ProgressBar progressBar;
    List<PhoneClass> phones = new ArrayList<>();
    String registeredStateString, corespondingStateString;

    public ConsumerDetailsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.consumer_first_details, container, false);
        initViews();
        return view;
    }

    // Initiate Views
    private void initViews() {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        companyName = (EditText) view.findViewById(R.id.consumerNameET);
        contactName = (EditText) view.findViewById(R.id.contactPersonET);
        mobile = (EditText) view.findViewById(R.id.mobileET);
        landline = (EditText) view.findViewById(R.id.landlineET);
        websitelink = (EditText) view.findViewById(R.id.websiteET);
        foundationYear = (EditText) view.findViewById(R.id.foundationYearET);
        annulaincome = (EditText) view.findViewById(R.id.incomeET);
        nextButton = (Button) view.findViewById(R.id.nextBtn);
        nextButton.setOnClickListener(this);
        frequencySpinner = (Spinner)view.findViewById(R.id.spinnerFrequency);
        typesCartonCheckboxLL = (FlowLayout) view.findViewById(R.id.typesCartonCheckboxLL);
        consumerScaleSpinner = (Spinner) view.findViewById(R.id.spinnerConsumerSCale);
        consumerTypeSpinner = (Spinner) view.findViewById(R.id.spinnerConsumerType);
        panCardET = (EditText) view.findViewById(R.id.panCardET);
        gst = (EditText) view.findViewById(R.id.gst);
        email = (EditText) view.findViewById(R.id.emailEditText);
        isSmapleRG = (RadioGroup) view.findViewById(R.id.sampleRG);
        creditLimitET = (EditText) view.findViewById(R.id.creditLimitET);
        creditDaysET = (EditText) view.findViewById(R.id.creditDaysET);
        sameAsRegistered = (CheckBox) view.findViewById(R.id.same_address);
        registeredAdd1 = (EditText) view.findViewById(R.id.registeredAddressET1);
        registeredAdd2 = (EditText) view.findViewById(R.id.registeredAddressET2);
        registeredAdd3 = (EditText) view.findViewById(R.id.registeredAddressET3);
        registeredCountry = (EditText) view.findViewById(R.id.registeredCountry);
        registeredCountry.setText("India");
        registeredCountry.setEnabled(false);
        registeredPincode = (EditText) view.findViewById(R.id.registeredPincode);
        registeredState = (Spinner) view.findViewById(R.id.registeredState);
        corresAdd1 = (EditText) view.findViewById(R.id.correspondenceAddressET1);
        corresAdd2 = (EditText) view.findViewById(R.id.correspondenceAddressET2);
        corresAdd3 = (EditText) view.findViewById(R.id.correspondenceAddressET3);
        corresCountry = (EditText) view.findViewById(R.id.correspondenceCountry);
        corresState = (Spinner) view.findViewById(R.id.correspondenceState);
        corresCountry.setEnabled(false);
        corresCountry.setText("India");
        corresPincode = (EditText) view.findViewById(R.id.correspondencePincode);
        quantityET = (EditText)view.findViewById(R.id.quantityET);
        vendorET = (EditText) view.findViewById(R.id.vendorET);
        otherVendorET= (EditText)view.findViewById(R.id.otherVendorET);
        inflateDataView();


    }

    private void inflateDataView() {

        data.setTypesOfCartons(new LinkedHashMap<Integer, String>());
        data.setConsumerFrequency(new LinkedHashMap<Integer, String>());
        data.setConsumerScale(new LinkedHashMap<Integer, String>());
        data.setConsumerType(new LinkedHashMap<Integer, String>());
        data.setStatesMap(new LinkedHashMap<String, String>());
        List<String> frequency = new ArrayList<String>();
        List<String> consumerType = new ArrayList<String>();
        List<String> consumerScale = new ArrayList<String>();
        List<String> states = new ArrayList<String>();

        for (Map.Entry<Integer, String> entry : data.getConsumerFrequency().entrySet()) /** Loop through all entrys in the HashMap **/ {
            frequency.add(entry.getValue());
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, frequency);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        for (Map.Entry<Integer, String> entry : data.getConsumerScale().entrySet()) /** Loop through all entrys in the HashMap **/ {
            consumerScale.add(entry.getValue());
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterScale = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, consumerScale);

        // Drop down layout style - list view with radio button
        dataAdapterScale.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        for (Map.Entry<Integer, String> entry : data.getConsumerType().entrySet()) /** Loop through all entrys in the HashMap **/ {
            consumerType.add(entry.getValue());
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterType = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, consumerType);

        // Drop down layout style - list view with radio button
        dataAdapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        for (Map.Entry<String, String> entry : data.getStatesMap().entrySet()) /** Loop through all entrys in the HashMap **/ {
            states.add(entry.getValue());
        }
        // Creating adapter for spinner
        ArrayAdapter<String> statesAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, states);

        // Drop down layout style - list view with radio button
        statesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        consumerScaleSpinner.setAdapter(dataAdapterScale);
        consumerTypeSpinner.setAdapter(dataAdapterType);
        frequencySpinner.setAdapter(dataAdapter);
        corresState.setAdapter(statesAdapter);
        registeredState.setAdapter(statesAdapter);
        consumerTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                request.setConsumerType((Integer) Utils.getElementByIndex(data.getConsumerType(), i));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        consumerScaleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                request.setConsumerScale((Integer) Utils.getElementByIndex(data.getConsumerScale(), i));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        frequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                request.setExpectedQuantityFrequency((Integer) Utils.getElementByIndex(data.getConsumerFrequency(), i));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        registeredState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                registeredStateString = (String) Utils.getElementByIndex(data.getStatesMap(), i);
                registeredStatePOs = i;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        corresState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                corespondingStateString = (String) Utils.getElementByIndex(data.getStatesMap(), i);

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


        isSmapleRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.yesSmaple:
                        isSample = 1;
                        request.setSampleCollection(true);
                        break;
                    case R.id.noSmaple:
                        isSample = 0;
                        request.setSampleCollection(false);
                        break;
                    default:
                        isSample = -1;
                }

            }
        });

        sameAsRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sameAsRegistered.isChecked()) {
                    corresAdd1.setText(registeredAdd1.getText());
                    corresAdd2.setText(registeredAdd2.getText());
                    corresAdd3.setText(registeredAdd3.getText());
                    corresPincode.setText(registeredPincode.getText());
                    corresState.setSelection(registeredStatePOs);
                    corresCountry.setText(registeredCountry.getText());
                }
            }
        });

    }


    @Override
    public void onClick(View view) {
        AddressClass rgistredAddress = new AddressClass();
        AddressClass corresAddress = new AddressClass();
        addresses = new ArrayList<>();
        if (view.getId() == R.id.nextBtn) {
            // move to next screen
            if (!TextUtils.isEmpty(companyName.getText())) {
                request.setConsumerName(companyName.getText().toString());
            } else {
                Toast.makeText(getActivity(), "Please input company name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(contactName.getText())) {
                request.setContactName(contactName.getText().toString());

            } else {
                Toast.makeText(getActivity(), "Please input contact name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(email.getText())) {
                request.setEmail(email.getText().toString());

            } else {
                Toast.makeText(getActivity(), "Please input email address", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!TextUtils.isEmpty(panCardET.getText())) {
                request.setCompanyPAN(panCardET.getText().toString());

            } else {
                Toast.makeText(getActivity(), "Please input PAN number", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(gst.getText())) {
                request.setGstin(gst.getText().toString());

            } else {
                Toast.makeText(getActivity(), "Please input GST number", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!TextUtils.isEmpty(registeredAdd1.getText())) {
                rgistredAddress.setAddressLine1(registeredAdd1.getText().toString());

            } else {
                Toast.makeText(getActivity(), "Please input registered address line 1", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(registeredAdd2.getText())) {
                rgistredAddress.setAddressLine2(registeredAdd2.getText().toString());

            } else {
                Toast.makeText(getActivity(), "Please input registered address line 2", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(registeredAdd3.getText())) {
                rgistredAddress.setAddressLine3(registeredAdd3.getText().toString());

            } else {
                Toast.makeText(getActivity(), "Please input registered address line 3", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(registeredStateString)) {
                rgistredAddress.setState(registeredStateString);

            } else {
                Toast.makeText(getActivity(), "Please input registered address state", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(registeredCountry.getText())) {
                rgistredAddress.setCountry("IN");

            }
            if (!TextUtils.isEmpty(registeredPincode.getText())) {
                rgistredAddress.setPincode(Integer.parseInt(registeredPincode.getText().toString()));

            } else {
                Toast.makeText(getActivity(), "Please input registered address pincode", Toast.LENGTH_SHORT).show();
                return;
            }


            rgistredAddress.setType(1);
            addresses.add(rgistredAddress);

            if (!TextUtils.isEmpty(corresAdd1.getText())) {
                corresAddress.setAddressLine1(corresAdd1.getText().toString());

            } else {
                Toast.makeText(getActivity(), "Please input correspondence address line 1", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(corresAdd2.getText())) {
                corresAddress.setAddressLine2(corresAdd2.getText().toString());

            } else {
                Toast.makeText(getActivity(), "Please input correspondence address line 2", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(corresAdd3.getText())) {
                corresAddress.setAddressLine3(corresAdd3.getText().toString());

            } else {
                Toast.makeText(getActivity(), "Please input correspondence address line 3", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(corespondingStateString)) {
                corresAddress.setState(corespondingStateString);

            } else {
                Toast.makeText(getActivity(), "Please input correspondence address state", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(corresCountry.getText())) {
                corresAddress.setCountry(corresCountry.getText().toString());

            }
            if (!TextUtils.isEmpty(corresPincode.getText())) {
                corresAddress.setPincode(Integer.parseInt(corresPincode.getText().toString()));

            } else {
                Toast.makeText(getActivity(), "Please input correspondence address pincode", Toast.LENGTH_SHORT).show();
                return;
            }

            corresAddress.setType(2);
            addresses.add(corresAddress);

            request.setAddresses(addresses);

            if (TextUtils.isEmpty(mobile.getText().toString())) {
                Toast.makeText(getActivity(), "Please input mobile number", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mobile.getText().toString().length() < 10) {
                Toast.makeText(getActivity(), "Please input valid mobile number", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(websitelink.getText())) {
                request.setWebsite(websitelink.getText().toString());

            } else {
                Toast.makeText(getActivity(), "Please input website link", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!TextUtils.isEmpty(foundationYear.getText())) {
                request.setFoundationYear(Integer.parseInt(foundationYear.getText().toString()));

            } else {
                Toast.makeText(getActivity(), "Please input foundation year", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(annulaincome.getText())) {
                request.setAnnualIncome(Integer.parseInt(annulaincome.getText().toString()));
            } else {
                Toast.makeText(getActivity(), "Please input annual income", Toast.LENGTH_SHORT).show();
                return;
            }
            if (request.getConsumerType() == -1){
                Toast.makeText(getActivity(), "Please select consumer type", Toast.LENGTH_SHORT).show();
                return;
            }
            if(request.getConsumerScale() == -1) {
                Toast.makeText(getActivity(), "Please select consumer scale", Toast.LENGTH_SHORT).show();
                return;
            }
            if (request.getCartonType().isEmpty()) {
                Toast.makeText(getActivity(), "Please select carton type", Toast.LENGTH_SHORT).show();
                return;
            }
            if(quantityET.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please select quanity", Toast.LENGTH_SHORT).show();
                return;
            }else{
                request.setExpectedQuantity(Integer.parseInt(quantityET.getText().toString()));
            }
            if(request.getExpectedQuantityFrequency() == -1){
                Toast.makeText(getActivity(), "Please select frequency", Toast.LENGTH_SHORT).show();
                return;
            }
            if (isSample == -1) {
                Toast.makeText(getActivity(), "Please select is smaple provided or not", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(creditLimitET.getText())) {
                request.setPrepaymentPercent(Integer.parseInt(creditLimitET.getText().toString()));
            } else {
                Toast.makeText(getActivity(), "Please input pre payment percentage", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(creditDaysET.getText())) {
                request.setMaxCreditDays(Integer.parseInt(creditDaysET.getText().toString()));
            } else {
                Toast.makeText(getActivity(), "Please input number of days for credit", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(vendorET.getText())) {
                request.setCurrentVendor((vendorET.getText().toString()));
            } else {
                Toast.makeText(getActivity(), "Please input vendor name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(otherVendorET.getText())) {
                request.setOtherVendor(otherVendorET.getText().toString());
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
            PhoneClass mobilePhone = new PhoneClass();
            PhoneClass landlinePhone = new PhoneClass();
            phones = new ArrayList<>();
            try {
                if (!TextUtils.isEmpty(mobile.getText())) {
                    mobilePhone.setType(1);
                    mobilePhone.setNumber(Integer.parseInt(mobile.getText().toString()));

                    phones.add(mobilePhone);
                }

                if (!TextUtils.isEmpty(landline.getText())) {
                    landlinePhone.setType(2);
                    landlinePhone.setNumber(Integer.parseInt(landline.getText().toString()));
                    phones.add(landlinePhone);

                }
                request.setPhones(phones);
                URL url = new URL("https://cartonwale-api-gateway.appspot.com/api/consumer-service/consumers");
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

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_CREATED) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
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
