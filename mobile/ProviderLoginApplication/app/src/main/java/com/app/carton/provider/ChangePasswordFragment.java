package com.app.carton.provider;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


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
 * Created by aggarwal.swati on 8/25/15.
 */
public class ChangePasswordFragment extends Fragment
        implements
        OnClickListener {

    protected final String ID_CHANGE_PASSWORD = "change_password";
    private View view;
    TextView oldPassError, newPassError, confirmPassError;
    Button changePassword;
    private EditText oldPasswordEditText, newPassowrdEditText,
            confirmPasswordEditText;



    private FragmentActivity mActivity;
    private ProgressBar progressBar;
    private String oldPasswod,newPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.change_password, container, false);
        inflateViews();
        return view;
    }

    private void inflateViews() {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        oldPasswordEditText = (EditText) view.findViewById(R.id.oldPasswordEditText);
        newPassowrdEditText = (EditText) view.findViewById(R.id.newPasswordEditText);
        confirmPasswordEditText = (EditText) view.findViewById(R.id.changeConfirmEditText);
        oldPassError = (TextView)view.findViewById(R.id.oldPassError);
        newPassError = (TextView)view.findViewById(R.id.newPassError);
        confirmPassError = (TextView)view.findViewById(R.id.confirmPassError);
        changePassword = (Button) view.findViewById(R.id.btn_change_password);
        changePassword.setOnClickListener(this);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_change_password:
                Utils.hideKeypad(mActivity, mActivity.getCurrentFocus());
                submitForm();

                break;

        }

    }

    /**
     * Validating form
     */
    private void submitForm() {
        if (!validateOldPassword()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        if (!validateConfirmPassword()) {
            return;
        }
        oldPasswod = oldPasswordEditText.getText().toString();
        newPassword = newPassowrdEditText.getText().toString();
        new SubmitPassword().execute();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (FragmentActivity) activity;
    }


    private boolean validateOldPassword() {

        if (oldPasswordEditText.getText().toString().trim()
                .isEmpty()) {
            oldPassError
                    .setVisibility(View.VISIBLE);
            requestFocus(oldPasswordEditText);

            return false;
        }else{
            oldPassError
                    .setVisibility(View.GONE);
        }

        return true;
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Reset Password");
    }


    private boolean validatePassword() {
        if (newPassowrdEditText.getText().toString().trim()
                .isEmpty()) {
            newPassError.setVisibility(View.VISIBLE);
            requestFocus(newPassowrdEditText);
            return false;
        } else {
            newPassError.setVisibility(View.GONE);
        }

        return true;
    }

    private boolean validateConfirmPassword() {
        if (confirmPasswordEditText.getText().toString().trim()
                .isEmpty()) {
            confirmPassError.setVisibility(View.VISIBLE);
            requestFocus(confirmPasswordEditText);
            return false;

        } else if (!confirmPasswordEditText
                .getText()
                .toString()
                .trim()
                .equalsIgnoreCase(
                        newPassowrdEditText.getText().toString()
                                .trim())) {
            confirmPassError.setVisibility(View.VISIBLE);
            confirmPassError.setText("Your passwords do not match, Please try again.");
            requestFocus(confirmPasswordEditText);
            return false;

        } else {
            confirmPassError.setVisibility(View.GONE);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onDetach() {
        Utils.hideKeypad(getActivity(), getActivity().getCurrentFocus());
        super.onDetach();
    }


    public class SubmitPassword extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);

        }

        protected String doInBackground(String... arg0) {


            try {

                URL url = new URL(WebServiceConstants.CHANGE_PASSWORD);
                JSONObject object = null;
                try {
                    object = new JSONObject();
                    object.put("oldPassword", oldPasswod);
                    object.put("newPassword", newPassword);
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
                        || Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_UNAUTHORIZED)) {
                    Toast.makeText(getActivity(), object.optString("message"),
                            Toast.LENGTH_LONG).show();
                } else {
                    SharedPreferences.putString(getActivity(), SharedPreferences.KEY_CHANGED_PASSWORD, "1");
                    Toast.makeText(getActivity(), "Passwords changed successfully, please login again with new password",
                            Toast.LENGTH_LONG).show();
                    new MainActivity().replaceLoginFragment(new ProviderLoginFragment());
                }

//                new MainActivity().replaceLoginFragment(new ChangePasswordFragment());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
