package com.example.aggarwalswati.providersob;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aggarwal.swati on 12/2/18.
 */

public class ProviderFirstDetailsFragment extends Fragment implements View.OnClickListener {
    private static View view;

    private static EditText companyName, contactName, email, websitelink, mobile, landline, registeredaddress, correspondenceAddress, foundationYear, annulaincome,
            nooFClientsET, gst, panCardET;
    private static Button nextButton;
    private Spinner operatingHrsSpinner, typesPrintingSpinner;
    FlowLayout typesOfBoxes, typesCartonCheckboxLL, typesOfPrinting, typeOfCorrugation;

    private TextView typesOfBoxesTV;
    private static CheckBox sameAsRegistered;

    private static FragmentManager fragmentManager;
    RequestData request = new RequestData();
    DataView data = new DataView();
    private EditText capacityET;
    private EditText dieCuttingET;

    public ProviderFirstDetailsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.provider_first_details, container, false);
        initViews();
        return view;
    }

    // Initiate Views
    private void initViews() {
        fragmentManager = getActivity().getSupportFragmentManager();
        companyName = (EditText) view.findViewById(R.id.companyNameET);
        contactName = (EditText) view.findViewById(R.id.contactPersonET);
        mobile = (EditText) view.findViewById(R.id.mobileET);
        landline = (EditText) view.findViewById(R.id.landlineET);
        registeredaddress = (EditText) view.findViewById(R.id.registeredAddressET);
        correspondenceAddress = (EditText) view.findViewById(R.id.corresAddressET);
        websitelink = (EditText) view.findViewById(R.id.websiteET);
        foundationYear = (EditText) view.findViewById(R.id.foundationYearET);
        annulaincome = (EditText) view.findViewById(R.id.incomeET);
        nextButton = (Button) view.findViewById(R.id.nextBtn);
        nextButton.setOnClickListener(this);
        operatingHrsSpinner = (Spinner) view.findViewById(R.id.spinnerOperatingHour);
        typesOfBoxes = (FlowLayout) view.findViewById(R.id.typeBoxesCheckboxLL);
        typesOfBoxesTV = (TextView) view.findViewById(R.id.boxesSupportedTV);
        typesCartonCheckboxLL = (FlowLayout) view.findViewById(R.id.typesCartonCheckboxLL);
        typesPrintingSpinner = (Spinner) view.findViewById(R.id.typesPrintingSpinner);
        typeOfCorrugation = (FlowLayout) view.findViewById(R.id.typeOfCorrugationLL);
        panCardET = (EditText) view.findViewById(R.id.panCardET);
        gst = (EditText) view.findViewById(R.id.gst);
        email = (EditText)view.findViewById(R.id.emailEditText);
        nooFClientsET = (EditText) view.findViewById(R.id.numberClientsET);
        capacityET = (EditText)view.findViewById(R.id.capacityET);
        dieCuttingET = (EditText)view.findViewById(R.id.dieCuttingET);
        inflateDataView();


    }

    private void inflateDataView() {

        data.setBoxType(new LinkedHashMap<Integer, String>());
        data.setCorrugationType(new LinkedHashMap<Integer, String>());
        data.setOperatingHrs(new LinkedHashMap<String, String>());
        data.setTypeOfPrinting(new LinkedHashMap<Integer, String>());
        data.setTypesOfCartons(new LinkedHashMap<Integer, String>());
        List<String> operatingHrs = new ArrayList<String>();
        List<String> typesPrinting = new ArrayList<String>();

        for (Map.Entry<String, String> entry : data.getOperatingHrs().entrySet()) /** Loop through all entrys in the HashMap **/ {
            operatingHrs.add(entry.getValue());
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, operatingHrs);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (Map.Entry<Integer, String> entry : data.getTypeOfPrinting().entrySet()) /** Loop through all entrys in the HashMap **/ {
            typesPrinting.add(entry.getValue());
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterPrinting = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, typesPrinting);

        // Drop down layout style - list view with radio button
        dataAdapterPrinting.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        operatingHrsSpinner.setAdapter(dataAdapter);
        typesPrintingSpinner.setAdapter(dataAdapterPrinting);
        operatingHrsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    request.setOperatingHours((String) Utils.getElementByIndex(data.getOperatingHrs(), i));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        typesPrintingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    request.setPrintingType((Integer) Utils.getElementByIndex(data.getTypeOfPrinting(), i));

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
                            request.getCartontype().add((Integer) key);
                        }
                    } else {
                        if (null != key && request.getCartontype().contains(key)) {
                            request.getCartontype().remove(key);

                        }
                    }
                    request.setCartontype(request.getCartontype());
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
                            request.getSupportedSheetLayers().add((Integer)key);
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
        if (view.getId() == R.id.nextBtn) {
            // move to next screen
            if (!TextUtils.isEmpty(companyName.getText())) {
                request.setCompanyName(companyName.getText().toString());
            } else {
                Toast.makeText(getActivity(), "Please input company name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(contactName.getText())) {
                request.setCompanyName(contactName.getText().toString());

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
            if (!TextUtils.isEmpty(websitelink.getText())) {
                request.setWebsite(websitelink.getText().toString());

            } else {
                Toast.makeText(getActivity(), "Please input website link", Toast.LENGTH_SHORT).show();
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
            if (!TextUtils.isEmpty(mobile.getText())) {
                JSONObject object = new JSONObject();
                try {
                    object.put("type", "mobile");
                    object.put("number", (mobile.getText().toString()));
                    request.getPhones().add(object);
                    request.setPhones(request.getPhones());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                Toast.makeText(getActivity(), "Please input mobile number", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(landline.getText())) {
                JSONObject object = new JSONObject();
                try {
                    object.put("type", "landline");
                    object.put("number", (landline.getText().toString()));
                    request.getPhones().add(object);
                    request.setPhones(request.getPhones());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            if (!TextUtils.isEmpty(registeredaddress.getText())) {
                JSONObject object = new JSONObject();
//                object.put("")
//                request.setEmail(email.getText().toString());

            } else {
                Toast.makeText(getActivity(), "Please input registered address", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(correspondenceAddress.getText())) {
//                request.setEmail(email.getText().toString());

            } else {
                Toast.makeText(getActivity(), "Please input correspondence address", Toast.LENGTH_SHORT).show();
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
            if (!TextUtils.isEmpty(nooFClientsET.getText())) {
                request.setClientCount(Integer.parseInt(nooFClientsET.getText().toString()));
            } else {
                Toast.makeText(getActivity(), "Please input number of clients", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(capacityET.getText())) {
                request.setFactoryCapacity(Integer.parseInt(capacityET.getText().toString()));
            } else {
                Toast.makeText(getActivity(), "Please input number of boxes produced per day", Toast.LENGTH_SHORT).show();
                return;
            }
            if (request.getOperatingHours().isEmpty() || request.getOperatingHours().equalsIgnoreCase("-1")) {
                Toast.makeText(getActivity(), "Please select operating hours", Toast.LENGTH_SHORT).show();
                return;
            }
            if (request.getCartontype().isEmpty()) {
                Toast.makeText(getActivity(), "Please select carton type", Toast.LENGTH_SHORT).show();
                return;
            }
            if ( request.getPrintingType() == -1) {
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
            if (!TextUtils.isEmpty(dieCuttingET.getText())) {
                request.setDieCuttingChargesperThousand(Integer.parseInt(dieCuttingET.getText().toString()));
            } else {
                Toast.makeText(getActivity(), "Please input die cutting charges", Toast.LENGTH_SHORT).show();
                return;
            }
            if (request.isCreditSupported) {
                request.setDieCuttingChargesperThousand(Integer.parseInt(dieCuttingET.getText().toString()));
            } else {
                Toast.makeText(getActivity(), "Please input die cutting charges", Toast.LENGTH_SHORT).show();
                return;
            }

        }
    }


}
