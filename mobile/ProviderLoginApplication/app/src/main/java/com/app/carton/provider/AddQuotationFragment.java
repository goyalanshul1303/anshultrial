package com.app.carton.provider;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Locale;

/**
 * Created by aggarwal.swati on 3/27/19.
 */

public class AddQuotationFragment extends Fragment implements View.OnClickListener {
    private static View view;

    private static Button createQuotationBtn;

    AddQuotationRequest request = new AddQuotationRequest();
    DataView data = new DataView();
    String orderId, productId;
    EditText orderQuotation,orderFulDate;
    TextView orderStartDate;
    ImageView startImageView, fulfilImageView;
    private ProgressBar progressBar;
    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    DatePickerDialog.OnDateSetListener dateFulFill;

    public AddQuotationFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            orderId = getArguments().containsKey("orderId") ? getArguments().getString("orderId") : "";
            productId = getArguments().containsKey("productId") ? getArguments().getString("productId") : "";

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.create_quotation_layout, container, false);
        initViews();
        setHasOptionsMenu(true);
        return view;
    }

    // Initiate Views
    private void initViews() {
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year) ;
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                startImageView.setVisibility(View.GONE);
                updateLabel(orderStartDate);
            }

        };
//        dateFulFill = new DatePickerDialog.OnDateSetListener() {
//
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                  int dayOfMonth) {
//                // TODO Auto-generated method stub
//                myCalendar.set(Calendar.YEAR, year);
//                myCalendar.set(Calendar.MONTH, monthOfYear);
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                fulfilImageView.setVisibility(View.GONE);
//
//                updateLabel(orderFulDate);
//            }
//
//        };
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        orderFulDate = (EditText) view.findViewById(R.id.orderFullFillET);
        orderStartDate = (TextView) view.findViewById(R.id.orderStartDateET);
        startImageView = (ImageView) view.findViewById(R.id.startDateImage);
//        fulfilImageView = (ImageView)view.findViewById(R.id.orderfillImage);

        orderFulDate.setOnClickListener(this);
        orderStartDate.setOnClickListener(this);
        createQuotationBtn = (Button) view.findViewById(R.id.createQuotationBtn);
        orderQuotation = (EditText) view.findViewById(R.id.amountET);
        createQuotationBtn.setOnClickListener(this);
        startImageView.setOnClickListener(this);
//        fulfilImageView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.createQuotationBtn) {
            Utils.hideKeypad(getActivity(), view);
            // move to next screen

            if (!TextUtils.isEmpty(orderQuotation.getText().toString()) && !TextUtils.isEmpty(orderStartDate.getText().toString())
            && !TextUtils.isEmpty(orderFulDate.getText().toString())){
                request.setOrderId(orderId);
                request.setProviderId(SharedPreferences.getString(getActivity(), "entityId"));
                request.setNoOfDays(Integer.parseInt(orderFulDate.getText().toString()));
                request.setOrderStartDate(orderStartDate.getText().toString());
                request.setQuoteAmount(Integer.parseInt(orderQuotation.getText().toString()));

                showAlertDialog();
            }else{
                Toast.makeText(getActivity(), "Please input all fields to proceed further", Toast.LENGTH_SHORT).show();
            }



        }
//        else if ( view.getId()== R.id.orderFullFillET){
//            new DatePickerDialog(getActivity(), dateFulFill, myCalendar
//                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//        }
        else if (view.getId() == R.id.startDateImage || view.getId() == R.id.orderStartDateET) {
            new DatePickerDialog(getActivity(), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    }

    private void updateLabel(TextView label) {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        label.setVisibility(View.VISIBLE);
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
                        || Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_FORBIDDEN) {
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

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getActivity().setTitle("Quotation Details");
    }

    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.over_flow_item);
        if (item != null)
            item.setVisible(false);
    }

    private void showAlertDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Confirm Quotation")
                .setMessage("Are you sure you want to add the quotation? \n" +
                        "• Quotation Amount: " + Integer.parseInt(orderQuotation.getText().toString()))

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        new AddQuotationAsyncTask().execute();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }
}
