package com.application.onboarding.providersob;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by aggarwal.swati on 2/18/19.
 */

public class AddProductFragment extends Fragment implements View.OnClickListener {
    private static View view;

    private static EditText productName,additionalDetails, grammage;
    private static Button nextButton;
    private Spinner  typesPrintingSpinner;
    FlowLayout typesCartonCheckboxLL, typesOfPrinting, typeOfCorrugation;

    AddProductRequest request = new AddProductRequest();
    DataView data = new DataView();

    private ProgressBar progressBar;
    private EditText height, width;
    String registeredStateString, corespondingStateString;
    private EditText quantity;
    private EditText length;
    private String consumerId;
    private RadioGroup cartonType, typeBoxesRG,typeCorrugationRG;
    private int cartonTypeSelected = -1;

    public AddProductFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            consumerId = getArguments().containsKey("consumerId") ? getArguments().getString("consumerId") : "";
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) { ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true); }

        getActivity().setTitle("Add Product");


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.create_product_layout, container, false);
        initViews();
        return view;
    }

    // Initiate Views
    private void initViews() {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        productName = (EditText) view.findViewById(R.id.productNameET);
        nextButton = (Button) view.findViewById(R.id.nextBtn);
        nextButton.setOnClickListener(this);
        quantity = (EditText)view.findViewById(R.id.quantityET);
        typeBoxesRG = (RadioGroup) view.findViewById(R.id.typeBoxesRG);
        cartonType = (RadioGroup) view.findViewById(R.id.cartonTypeRg);
        typesPrintingSpinner = (Spinner) view.findViewById(R.id.typesPrintingSpinner);
        typeCorrugationRG = (RadioGroup) view.findViewById(R.id.typeCorrugationRG);
        height = (EditText) view.findViewById(R.id.height);
        width = (EditText) view.findViewById(R.id.width);
        length = (EditText)view.findViewById(R.id.length);
        grammage = (EditText)view.findViewById(R.id.grammage);
        additionalDetails= (EditText)view.findViewById(R.id.additionalDetails);
        
        inflateDataView();


    }

    private void inflateDataView() {

        data.setBoxType(new LinkedHashMap<Integer, String>());

        data.setCorrugationType(new LinkedHashMap<Integer, String>());
        data.setTypeOfPrinting(new LinkedHashMap<Integer, String>());
        List<String> typesPrinting = new ArrayList<String>();
        ArrayList<String> corrugationType = new ArrayList<String>();
        ArrayList<String> typesOfBoxes = new ArrayList<>();

        for (Map.Entry<Integer, String> entry : data.getBoxType().entrySet()) /** Loop through all entrys in the HashMap **/ {
            typesOfBoxes.add(entry.getValue());
        }
        addRadioButtons(typesOfBoxes, typeBoxesRG);
        for (Map.Entry<Integer, String> entry : data.getCorrugationType().entrySet()) /** Loop through all entrys in the HashMap **/ {
            corrugationType.add(entry.getValue());
        }
        addRadioButtons(corrugationType, typeCorrugationRG);

        for (Map.Entry<Integer, String> entry : data.getTypeOfPrinting().entrySet()) /** Loop through all entrys in the HashMap **/ {
            typesPrinting.add(entry.getValue());
        }


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterPrinting = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, typesPrinting);

        // Drop down layout style - list view with radio button
        dataAdapterPrinting.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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

        cartonType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.corrugatedRg:
                        request.setCartonType(1);
                        break;
                    case R.id.dieCuttRg:
                        request.setCartonType(2);
                        break;
                    default:
                        cartonTypeSelected = -1;
                }

            }
        });
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.nextBtn) {
            Utils.hideKeypad(getActivity(), view);
            // move to next screen
            if (!TextUtils.isEmpty(productName.getText())) {
                request.setName(productName.getText().toString());
            } else {
                Toast.makeText(getActivity(), "Please input product name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (request.getCartonType() == -1) {
                Toast.makeText(getActivity(), "Please select carton type", Toast.LENGTH_SHORT).show();
                return;
            }
            if (request.getPrintingType() == -1) {
                Toast.makeText(getActivity(), "Please select printing type", Toast.LENGTH_SHORT).show();
                return;
            }

            if (request.getCorrugationType() == -1) {
                Toast.makeText(getActivity(), "Please select corrugation type", Toast.LENGTH_SHORT).show();
                return;
            }
            if (request.getSheetLayerType()== -1) {
                Toast.makeText(getActivity(), "Please select corrugation type", Toast.LENGTH_SHORT).show();
                return;
            }


            if (!TextUtils.isEmpty(quantity.getText())) {
                request.setQuantity(Integer.parseInt(quantity.getText().toString()));
            } else {
                Toast.makeText(getActivity(), "Please input quantity", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(width.getText())) {
                Toast.makeText(getActivity(), "Please input box width", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(height.getText())) {
                Toast.makeText(getActivity(), "Please input box height", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(length.getText())) {
                Toast.makeText(getActivity(), "Please input box length", Toast.LENGTH_SHORT).show();
                return;
            }

            DimensionClass dimensionClass = new DimensionClass();
            dimensionClass.setHeight(Integer.parseInt(height.getText().toString()));
            dimensionClass.setWidth(Integer.parseInt(width.getText().toString()));
            dimensionClass.setLength(Integer.parseInt(length.getText().toString()));

            request.setDimension(dimensionClass);
            request.setConsumerId(consumerId);
            request.setAdditionalComments(additionalDetails.getText().toString());
            request.setGrammage(grammage.getText().toString());

            new AddProductAsyncTask().execute();

//            postDataToServer();


        }
    }


    public class AddProductAsyncTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL(WebServiceConstants.ADD_PRODUCT);
                JSONObject object = null;
                Gson gson = new Gson();
//                new GsonBuilder().create().toJson(this, Producto.class);
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
            if (isVisible()) {
                try {
                    JSONObject object = new JSONObject(result);
                    if (!object.optString("status").isEmpty() && (Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_BAD_REQUEST
                            || Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_UNAUTHORIZED)) {
                        Toast.makeText(getActivity(), object.optString("message"),
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Product Created successfully",
                                Toast.LENGTH_LONG).show();
                        new MainActivity().replaceLoginFragment(new ChooseListActivityFragment());
                    }

//                new MainActivity().replaceLoginFragment(new ChangePasswordFragment());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                FragmentManager fragmentManager = MainActivity.fragmentManager;
                fragmentManager.popBackStackImmediate();

            }

        }
    }

    public void addRadioButtons(ArrayList< String> data, RadioGroup radioGroup) {

            RadioGroup ll = new RadioGroup(getActivity());
            ll.setOrientation(LinearLayout.HORIZONTAL);
            if (null!=radioGroup){
                radioGroup.removeAllViews();
            }
            for (int i = 0; i < data.size(); i++) {
                RadioButton rdbtn = new RadioButton(getActivity());
                rdbtn.setId(View.generateViewId());
                rdbtn.setText(data.get(i));
                rdbtn.setTag(data.get(i));
                rdbtn.setOnClickListener(listener);
                ll.addView(rdbtn);

            }
        radioGroup.addView(ll);

    }
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view instanceof RadioButton) {
                String tag = (String) view.getTag();
                if (tag.contains("Ply")){
                    Object key = Utils.getKeyFromValue(data.getBoxType(),tag.toString());
                    if (null != key) {
                        request.setSheetLayerType((Integer) key);

                    }
                }else{
                    Object key = Utils.getKeyFromValue(data.getCorrugationType(),tag.toString());
                    if (null != key) {
                        request.setCorrugationType((Integer) key);

                    }
                }


            }
        }
    };

}
