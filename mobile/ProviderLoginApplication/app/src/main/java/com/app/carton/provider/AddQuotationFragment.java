package com.app.carton.provider;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by aggarwal.swati on 3/27/19.
 */

public class AddQuotationFragment extends Fragment implements View.OnClickListener {
    private static View view;

    private static Button createQuotationBtn;

    AddQuotationRequest request = new AddQuotationRequest();
    DataView data = new DataView();
    String orderId, productId;
    EditText orderStartDate, orderQuotation, orderFulDate;
    private ProgressBar progressBar;
    final Calendar myCalendar = Calendar.getInstance();


    public AddQuotationFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            orderId = getArguments().containsKey("orderId") ? getArguments().getString("orderId") : "";
            productId = getArguments().containsKey("productId")? getArguments().getString("productId") :"";

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.create_quotation_layout, container, false);
        initViews();
        return view;
    }

    // Initiate Views
    private void initViews() {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(orderStartDate);
            }

        };
        final DatePickerDialog.OnDateSetListener dateFulFill = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(orderFulDate);
            }

        };
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        orderFulDate = (EditText)view.findViewById(R.id.orderFullFillET);
        orderStartDate = (EditText)view.findViewById(R.id.orderStartDateET);
        createQuotationBtn = (Button)view.findViewById(R.id.createQuotationBtn);
        orderQuotation = (EditText) view.findViewById(R.id.amountET);
        createQuotationBtn.setOnClickListener(this);
        orderStartDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        orderFulDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), dateFulFill, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }




    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.createQuotationBtn) {
            Utils.hideKeypad(getActivity(), view);
            // move to next screen

            request.setOrderId(orderId);
            request.setProviderId(SharedPreferences.getString(getActivity(), "entityId"));
            request.setOrderFulfillmentDate(orderFulDate.getText().toString());
            request.setOrderStartDate(orderStartDate.getText().toString());
            request.setQuoteAmount(Integer.parseInt(orderQuotation.getText().toString()));

            new AddQuotationAsyncTask().execute();

//            postDataToServer();


        }
    }

    private void updateLabel(EditText label) {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        label.setText(sdf.format(myCalendar.getTime()));
    }
    public class AddQuotationAsyncTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL(WebServiceConstants.ADD_QUOTATION);
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
            try {
                JSONObject object = new JSONObject(result);
                if (!object.optString("status").isEmpty() && (Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_BAD_REQUEST
                        || Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_UNAUTHORIZED)
                        || Integer.valueOf(object.optString("status"))== HttpURLConnection.HTTP_FORBIDDEN) {
                    Toast.makeText(getActivity(), object.optString("message"),
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Quotation Created successfully",
                            Toast.LENGTH_LONG).show();
                    new MainActivity().replaceLoginFragment(new PlacedOrderListFragment());
                }

//                new MainActivity().replaceLoginFragment(new ChangePasswordFragment());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
