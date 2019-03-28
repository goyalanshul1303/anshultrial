package com.app.carton.provider;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
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

/**
 * Created by aggarwal.swati on 12/27/18.
 */

public class ProviderLoginFragment extends Fragment implements View.OnClickListener {
    private static View view;

    private static EditText emailid, password;
    private static Button loginButton;
    private static CheckBox show_hide_password;
    private static ProgressBar progressBar;

    public ProviderLoginFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login, container, false);
        initViews();
        setListeners();
        return view;
    }

    // Initiate Views
    private void initViews() {

        emailid = (EditText) view.findViewById(R.id.login_emailid);
        password = (EditText) view.findViewById(R.id.login_password);
        loginButton = (Button) view.findViewById(R.id.loginBtn);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        show_hide_password = (CheckBox) view
                .findViewById(R.id.show_hide_password);

    }

    // Set Listeners
    private void setListeners() {
        loginButton.setOnClickListener(this);


        // Set check listener over checkbox for showing and hiding password
        show_hide_password
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton button,
                                                 boolean isChecked) {

                        // If it is checkec then show password else hide
                        // password
                        if (isChecked) {

                            show_hide_password.setText(R.string.hide_pwd);// change
                            // checkbox
                            // text

                            password.setInputType(InputType.TYPE_CLASS_TEXT);
                            password.setTransformationMethod(HideReturnsTransformationMethod
                                    .getInstance());// show password
                        } else {
                            show_hide_password.setText(R.string.show_pwd);// change
                            // checkbox
                            // text

                            password.setInputType(InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            password.setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());// hide password

                        }

                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                checkValidation();
                break;


        }

    }

    // Check Validation before login
    private void checkValidation() {
        // Get email id and password
        String getEmailId = emailid.getText().toString();
        String getPassword = password.getText().toString();

        // Check for both field is empty or not
        if (getEmailId.equals("") || getEmailId.length() == 0
                || getPassword.equals("") || getPassword.length() == 0) {

            Toast.makeText(getActivity(),
                    "Enter both credentials.", Toast.LENGTH_SHORT).show();

        } else
            Toast.makeText(getActivity(), "Do Login.", Toast.LENGTH_SHORT)
                    .show();
        new SendPostRequest().execute();

//        new MainActivity().replaceLoginFragment(new ChangePasswordFragment());

    }

    public void getAuthorizationResponse() {
        new SendAuthorizationRequest().execute();
    }


    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL(WebServiceConstants.LOGIN);

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("username", "abcd12245e");
                postDataParams.put("password", "swati22");
                Log.e("params", postDataParams.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(Utils.getPostDataString(postDataParams));

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
            JSONObject object = null;

            progressBar.setVisibility(View.GONE);
            try {
                object = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (null!=object && !object.optString("status").isEmpty() && (Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_BAD_REQUEST
                    || Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_UNAUTHORIZED)) {
                Toast.makeText(getActivity(), object.optString("message"),
                        Toast.LENGTH_LONG).show();
            }else if (null!=object &&!object.optString("token").isEmpty()) {
                Toast.makeText(getActivity(), "Login Successful",
                        Toast.LENGTH_LONG).show();

                SharedPreferences.putString(getActivity(),
                        SharedPreferences.KEY_AUTHTOKEN,
                        object.optString("token"));
                getAuthorizationResponse();

            } else{
                Toast.makeText(getActivity(), "Something Went Wrong",
                        Toast.LENGTH_LONG).show();
            }


        }
    }

    public class SendAuthorizationRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL(WebServiceConstants.PERMISSION);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
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
            try {
                object = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (null!=object && !object.optString("status").isEmpty() && (Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_BAD_REQUEST
                    || Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_UNAUTHORIZED)) {
                Toast.makeText(getActivity(), object.optString("message"),
                        Toast.LENGTH_LONG).show();
            }else if (null!=object && null!=object.optJSONObject("dbUser")) {
                JSONObject dbUserObj = object.optJSONObject("dbUser");
                SharedPreferences.putString(getActivity(),"entityId", dbUserObj.optString("entityId"));
                if (null!=dbUserObj.optJSONArray("roles")){
                    JSONArray rolesArray = dbUserObj.optJSONArray("roles");
                    for (int i = 0 ; i < 1; i++){
                        JSONObject rolesObj = rolesArray.optJSONObject(i);
                        if (null!=rolesObj && rolesObj.optString("code").equalsIgnoreCase("role.provider")){
                            if (null != SharedPreferences.getString(getActivity(), SharedPreferences.KEY_CHANGED_PASSWORD)&& SharedPreferences.getString(getActivity(), SharedPreferences.KEY_CHANGED_PASSWORD).equalsIgnoreCase("1")){
                                new MainActivity().replaceLoginFragment(new ChooseActivityFragment());
                            }else{
                                new MainActivity().replaceLoginFragment(new ChangePasswordFragment());
                            }

                        }else{
                            Toast.makeText(getActivity(), "Something Went Wrong",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }else{
                Toast.makeText(getActivity(), "Something Went Wrong",
                        Toast.LENGTH_LONG).show();
            }

        }


    }


}