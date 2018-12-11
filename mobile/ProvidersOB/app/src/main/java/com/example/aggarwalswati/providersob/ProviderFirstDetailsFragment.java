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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

    private static EditText companyName, contactName, email, websitelink, mobile, landline, foundationYear, annulaincome,
            nooFClientsET, gst, panCardET, creditLimitET, registeredAdd1, registeredAdd2, registeredAdd3, registeredPincode,
            registeredState, registeredCountry, corresAdd1, corresAdd2, corresAdd3, corresCountry, corresState, corresPincode;
    private static Button nextButton;
    private Spinner operatingHrsSpinner, typesPrintingSpinner;
    FlowLayout typesOfBoxes, typesCartonCheckboxLL, typesOfPrinting, typeOfCorrugation;

    private CheckBox sameAsRegistered;
    private int isCredit = -1;
    private int isManufacture = -1;
    private int isLogistics = -1;
    private int isQuality = -1;
    private LinearLayout creditSupoortLL;
    private int isCapacity = -1;

    RequestData request = new RequestData();
    DataView data = new DataView();
    private EditText capacityET;
    private EditText dieCuttingET;
    private RadioGroup isCreditRG, isLogisticsRG, qualityRG, manufactureRG, capacityRG;
    private EditText creditDaysET;
    List<AddressClass> addresses = new ArrayList<>();

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
        companyName = (EditText) view.findViewById(R.id.companyNameET);
        contactName = (EditText) view.findViewById(R.id.contactPersonET);
        mobile = (EditText) view.findViewById(R.id.mobileET);
        landline = (EditText) view.findViewById(R.id.landlineET);
//        registeredaddress = (EditText) view.findViewById(R.id.registeredAddressET);
//        correspondenceAddress = (EditText) view.findViewById(R.id.corresAddressET);
        websitelink = (EditText) view.findViewById(R.id.websiteET);
        foundationYear = (EditText) view.findViewById(R.id.foundationYearET);
        annulaincome = (EditText) view.findViewById(R.id.incomeET);
        nextButton = (Button) view.findViewById(R.id.nextBtn);
        nextButton.setOnClickListener(this);
        operatingHrsSpinner = (Spinner) view.findViewById(R.id.spinnerOperatingHour);
        typesOfBoxes = (FlowLayout) view.findViewById(R.id.typeBoxesCheckboxLL);
        typesCartonCheckboxLL = (FlowLayout) view.findViewById(R.id.typesCartonCheckboxLL);
        typesPrintingSpinner = (Spinner) view.findViewById(R.id.typesPrintingSpinner);
        typeOfCorrugation = (FlowLayout) view.findViewById(R.id.typeOfCorrugationLL);
        panCardET = (EditText) view.findViewById(R.id.panCardET);
        gst = (EditText) view.findViewById(R.id.gst);
        email = (EditText) view.findViewById(R.id.emailEditText);
        nooFClientsET = (EditText) view.findViewById(R.id.numberClientsET);
        capacityET = (EditText) view.findViewById(R.id.capacityET);
        dieCuttingET = (EditText) view.findViewById(R.id.dieCuttingET);
        capacityRG = (RadioGroup) view.findViewById(R.id.shareCapacityRG);
        isLogisticsRG = (RadioGroup) view.findViewById(R.id.logisticsRG);
        isCreditRG = (RadioGroup) view
                .findViewById(R.id.creditSupportRG);
        manufactureRG = (RadioGroup) view.findViewById(R.id.manufactureRG);
        qualityRG = (RadioGroup) view.findViewById(R.id.qualityInspectionRG);
        creditSupoortLL = (LinearLayout) view.findViewById(R.id.creditSupoortLL);
        creditLimitET = (EditText) view.findViewById(R.id.creditLimitET);
        creditDaysET = (EditText) view.findViewById(R.id.creditDaysET);
        sameAsRegistered = (CheckBox) view.findViewById(R.id.same_address);
        registeredAdd1 = (EditText) view.findViewById(R.id.registeredAddressET1);
        registeredAdd2 = (EditText) view.findViewById(R.id.registeredAddressET2);
        registeredAdd3 = (EditText) view.findViewById(R.id.registeredAddressET3);
        registeredCountry = (EditText) view.findViewById(R.id.registeredCountry);
        registeredPincode = (EditText) view.findViewById(R.id.registeredPincode);
        registeredState = (EditText) view.findViewById(R.id.registeredState);
        corresAdd1 = (EditText) view.findViewById(R.id.correspondenceAddressET1);
        corresAdd2 = (EditText) view.findViewById(R.id.correspondenceAddressET2);
        corresAdd3 = (EditText) view.findViewById(R.id.correspondenceAddressET3);
        corresCountry = (EditText) view.findViewById(R.id.correspondenceCountry);
        corresState = (EditText) view.findViewById(R.id.correspondenceState);
        corresPincode = (EditText) view.findViewById(R.id.correspondencePincode);

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
        isCreditRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.yesCredit:
                        isCredit = 1;
                        creditSupoortLL.setVisibility(View.VISIBLE);
                        request.setCreditSupported(true);

                        break;
                    case R.id.noCredit:
                        isCredit = 0;
                        creditSupoortLL.setVisibility(View.GONE);
                        request.setCreditSupported(false);

                        break;
                    default:
                        isCredit = -1;
                }

            }
        });
        manufactureRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.yesManufacture:
                        isManufacture = 1;
                        request.setManufactureWithProvidedMaterial(true);

                        break;
                    case R.id.noManufacture:
                        isManufacture = 0;
                        request.setManufactureWithProvidedMaterial(false);
                        break;
                    default:
                        isManufacture = -1;
                }

            }
        });
        qualityRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.yesQuality:
                        isQuality = 1;
                        request.setAllowQualityInspect(true);
                        break;
                    case R.id.noQuality:
                        isQuality = 0;
                        request.setAllowQualityInspect(false);
                        break;
                    default:
                        isQuality = -1;
                }

            }
        });
        isLogisticsRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.yesLogistics:
                        isLogistics = 1;
                        request.setLogisticAvailable(true);

                        break;
                    case R.id.noLogistics:
                        isLogistics = 0;
                        request.setLogisticAvailable(false);

                        break;
                    default:
                        isLogistics = -1;
                }
            }
        });
        capacityRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.yesshareCapacity:
                        isCapacity = 1;
                        request.setShareCapacity(true);

                        break;
                    case R.id.noshareCapacity:
                        isCapacity = 0;
                        request.setShareCapacity(false);

                        break;
                    default:
                        isCapacity = -1;
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
                    corresState.setText(registeredState.getText());
                    corresCountry.setText(registeredCountry.getText());
                }
            }
        });

    }


    @Override
    public void onClick(View view) {
        AddressClass rgistredAddress = new AddressClass();
        AddressClass corresAddress = new AddressClass();
        if (view.getId() == R.id.nextBtn) {
            // move to next screen
            if (!TextUtils.isEmpty(companyName.getText())) {
                request.setCompanyName(companyName.getText().toString());
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
            if (!TextUtils.isEmpty(registeredState.getText())) {
                rgistredAddress.setState(registeredState.getText().toString());

            } else {
                Toast.makeText(getActivity(), "Please input registered address state", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(registeredCountry.getText())) {
                rgistredAddress.setCountry(registeredCountry.getText().toString());

            } else {
                Toast.makeText(getActivity(), "Please input registered address country", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(registeredPincode.getText())) {
                rgistredAddress.setPincode(Integer.parseInt(registeredPincode.getText().toString()));

            } else {
                Toast.makeText(getActivity(), "Please input registered address pincode", Toast.LENGTH_SHORT).show();
                return;
            }


            rgistredAddress.setType("registered");
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
            if (!TextUtils.isEmpty(corresState.getText())) {
                corresAddress.setState(corresState.getText().toString());

            } else {
                Toast.makeText(getActivity(), "Please input correspondence address state", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(corresCountry.getText())) {
                corresAddress.setCountry(corresCountry.getText().toString());

            } else {
                Toast.makeText(getActivity(), "Please input correspondence address country", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(corresPincode.getText())) {
                corresAddress.setPincode(Integer.parseInt(corresPincode.getText().toString()));

            } else {
                Toast.makeText(getActivity(), "Please input correspondence address pincode", Toast.LENGTH_SHORT).show();
                return;
            }

            corresAddress.setType("correspondence");
            addresses.add(corresAddress);

            request.setAddresses(addresses);

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
                if (mobile.getText().toString().length() < 10) {
                    Toast.makeText(getActivity(), "Please input valid mobile number", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(getActivity(), "Please input mobile number", Toast.LENGTH_SHORT).show();
                }
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
            if (!TextUtils.isEmpty(dieCuttingET.getText())) {
                request.setDieCuttingChargesperThousand(Integer.parseInt(dieCuttingET.getText().toString()));
            } else {
                Toast.makeText(getActivity(), "Please input die cutting charges", Toast.LENGTH_SHORT).show();
                return;
            }
            if (isCredit == -1) {

                Toast.makeText(getActivity(), "Please select is credit allowed or not", Toast.LENGTH_SHORT).show();
                return;
            } else {
                if (isCredit == 1) {
                    if (!TextUtils.isEmpty(creditLimitET.getText())) {
                        request.setCreditLimit(Integer.parseInt(creditLimitET.getText().toString()));
                    } else {
                        Toast.makeText(getActivity(), "Please input credit limit", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!TextUtils.isEmpty(creditDaysET.getText())) {
                        request.setCreditDays(Integer.parseInt(creditDaysET.getText().toString()));
                    } else {
                        Toast.makeText(getActivity(), "Please input number of days for credit", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
            }
            if (isLogistics == -1) {
                Toast.makeText(getActivity(), "Please select is logistics available", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isQuality == -1) {
                Toast.makeText(getActivity(), "Please select open to allow quality inspection", Toast.LENGTH_SHORT).show();
                return;
            }
            if (isCapacity == -1) {
                Toast.makeText(getActivity(), "Please select open to share capacity changes", Toast.LENGTH_SHORT).show();
                return;
            }
            if (isManufacture == -1) {
                Toast.makeText(getActivity(), "Please select open to  manufacture with material provided", Toast.LENGTH_SHORT).show();
                return;
            }
//            postDataToserver();

        }
    }


}
