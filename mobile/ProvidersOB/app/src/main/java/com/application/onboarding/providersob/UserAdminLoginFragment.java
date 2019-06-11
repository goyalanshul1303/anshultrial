package com.application.onboarding.providersob;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by aggarwal.swati on 12/2/18.
 */

public class UserAdminLoginFragment extends Fragment implements View.OnClickListener {
    private static View view;

    private static EditText emailid, password;
    private static Button loginButton;
    private static ImageButton show_hide_password;
    private static ProgressBar progressBar;
    private boolean isChecked = true;

    public UserAdminLoginFragment() {

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
        show_hide_password = (ImageButton) view
                .findViewById(R.id.showHidePassword);
    }

    // Set Listeners
    private void setListeners() {
        loginButton.setOnClickListener(this);


        // Set check listener over checkbox for showing and hiding password
        show_hide_password.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (isChecked) {
                    Drawable tempImage = getResources().getDrawable(R.drawable.group_black);

                    show_hide_password.setImageDrawable(tempImage);// change
                    // checkbox
                    // text

                    password.setInputType(InputType.TYPE_CLASS_TEXT);
                    password.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());// show password
                    isChecked = false;
                } else {
//                            show_hide_password.setText(R.string.show_pwd);// change
                    // checkbox
                    // text
                    Drawable tempImage = getResources().getDrawable(R.drawable.group_2);

                    show_hide_password.setImageDrawable(tempImage);
                    password.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());// hide password
                    isChecked = true;

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

        } else{
            new SendPostRequest().execute();

        }
//            Toast.makeText(getActivity(), "Do Login.", Toast.LENGTH_SHORT)
//                    .show();



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
//                postDataParams.put("username", "goyalanshul1303");
//                postDataParams.put("password", "abc123");
                postDataParams.put("username", emailid.getText().toString());
                postDataParams.put("password", password.getText().toString());
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
                if (null!=dbUserObj.optJSONArray("roles")){
                    JSONArray rolesArray = dbUserObj.optJSONArray("roles");
                        for (int i = 0 ; i < 1; i++){
                           JSONObject rolesObj = rolesArray.optJSONObject(i);
                           if (null!=rolesObj && rolesObj.optString("code").equalsIgnoreCase("role.seller.admin")){
                               if (object.optBoolean("enabled")){
                                   new MainActivity().replaceLoginFragment(new ChooseListActivityFragment());
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

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Login");
    }
}

