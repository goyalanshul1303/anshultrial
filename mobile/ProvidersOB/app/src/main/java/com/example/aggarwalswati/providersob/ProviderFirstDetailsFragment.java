package com.example.aggarwalswati.providersob;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
    nooFClientsET,factoryCapacityET ;
    private static Button nextButton;
    private Spinner operatingHrsSpinner;
    LinearLayout typesOfBoxes, typesCartonCheckboxLL, typesOfPrinting;

    private TextView typesOfBoxesTV;
    private static CheckBox sameAsRegistered;

    private static FragmentManager fragmentManager;

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
        typesOfBoxes = (LinearLayout) view.findViewById(R.id.typeBoxesCheckboxLL);
        typesOfBoxesTV = (TextView) view.findViewById(R.id.boxesSupportedTV);
        typesCartonCheckboxLL = (LinearLayout)view.findViewById(R.id.typesCartonCheckboxLL);
        typesOfPrinting = (LinearLayout) view.findViewById(R.id.typesPrintingCheckboxLL);
        inflateDataView();



    }

    private void inflateDataView() {
        DataView data = new DataView();
        data.setBoxType(new LinkedHashMap<String, String>());
        data.setCorrugationType(new LinkedHashMap<String, String>());
        data.setOperatingHrs(new LinkedHashMap<String, String>());
        data.setTypeOfPrinting(new LinkedHashMap<String, String>());
        data.setTypesOfCartons(new LinkedHashMap<String, String>());
        List<String> operatingHrs = new ArrayList<String>();
        for (Map.Entry <String, String> entry : data.getOperatingHrs().entrySet()) /** Loop through all entrys in the HashMap **/ {
//           for(int i = 0 ; i <= data.getOperatingHrs().size(); i ++){/** Loop through all values in HashMap**/
               operatingHrs.add(entry.getValue());
//            }
        }
                    // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, operatingHrs);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        operatingHrsSpinner.setAdapter(dataAdapter);
        for (Map.Entry <String, String> entry : data.getTypesOfCartons().entrySet()){
            CheckBox cb = new CheckBox(getActivity());
            cb.setText(entry.getValue());
            typesCartonCheckboxLL.addView(cb);
        }
        for (Map.Entry <String, String> entry : data.getTypeOfPrinting().entrySet()){
            CheckBox cb = new CheckBox(getActivity());
            cb.setText(entry.getValue());
            typesOfPrinting.addView(cb);
        }
        for (Map.Entry <String, String> entry : data.getBoxType().entrySet()){
            CheckBox cb = new CheckBox(getActivity());
            cb.setText(entry.getValue());
            typesOfBoxes.addView(cb);
        }


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.nextBtn){
            // move to next screen
        }
    }
}
