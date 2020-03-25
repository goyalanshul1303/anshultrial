package com.application.onboarding.providersob;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static android.app.Activity.RESULT_OK;

/**
 * Created by aggarwal.swati on 2/12/19.
 */

public class ProductDetailsFragment extends Fragment implements View.OnClickListener {

    private static View view;

    private ProgressBar progressBar;
    private Button addOrderBtn, viewOffersBtn , getImageFromGalleryButton, uploadImageOnServerButton;
    TextView productName, email, additionalComments, quantity,dimensions;
    String consumerId, productId;
    private String productNameString;
    EditText  getImageNameFromEditText, getImageName;
    DimensionClass dimensionClass = new DimensionClass();
    SpecificationClass specificationClass = new SpecificationClass();
    private String price;
    ArrayList<OffersData> offersList = new ArrayList<>();
    ImageView showSelectedImage;
    ByteArrayOutputStream byteArrayOutputStream ;
    LinearLayout additionaFieldLL;

    byte[] byteArray ;
    HttpURLConnection httpURLConnection ;

    URL url;

    OutputStream outputStream;

    BufferedWriter bufferedWriter ;

    int RC ;

    BufferedReader bufferedReader ;

    StringBuilder stringBuilder;
Bitmap fixBitMap;

    boolean check = true;
    private ProgressDialog progressDialog;
    String convertImage ;
    String getImageNameFromEditTextString;
    public ProductDetailsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            consumerId = getArguments().containsKey("selectedId") ? getArguments().getString("selectedId") : "";
            productId = getArguments().containsKey("productId") ? getArguments().getString("productId") : "";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.added_product_details, container, false);
        initViews();
        return view;
    }


    // Initiate Views
    private void initViews() {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        addOrderBtn = (Button) view.findViewById(R.id.addOrderBtn);
        viewOffersBtn = (Button) view.findViewById(R.id.viewOffersBtn);
        quantity = (TextView) view.findViewById(R.id.expectedQuantity);
        productName = (TextView) view.findViewById(R.id.productName);
        additionalComments = (TextView) view.findViewById(R.id.additionalComments);
        additionaFieldLL = (LinearLayout) view.findViewById(R.id.additionaFieldLL);
        dimensions = (TextView)view.findViewById(R.id.dimensions);
        addOrderBtn.setVisibility(View.VISIBLE);
        addOrderBtn.setOnClickListener(this);
        viewOffersBtn.setOnClickListener(this);
        getImageFromGalleryButton = (Button)view.findViewById(R.id.button);

        uploadImageOnServerButton = (Button)view.findViewById(R.id.button2);

        showSelectedImage = (ImageView)view.findViewById(R.id.imageView);

        getImageName = (EditText)view.findViewById(R.id.editText);

        byteArrayOutputStream = new ByteArrayOutputStream();

//        getImageFromGalleryButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent();
//
//                intent.setType("image/*");
//
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//
//                startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 1);
//
//            }
//        });
//
//
//        uploadImageOnServerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                getImageNameFromEditTextString = getImageName.getText().toString();
//
//                UploadImageToServer();
//
//            }
//        });
        new FetchDetailsTask().execute();

    }

    public class FetchDetailsTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... arg0) {

            try {
                SpannableStringBuilder string = new SpannableStringBuilder(WebServiceConstants.GET_SINGLE_PRODUCT);
                string.append(consumerId);
                string.append("/");
                string.append(productId);
                URL url = new URL(string.toString());


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
//                conn.setDoInput(true);
//                conn.setDoOutput(true);
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
            if (null != result && isVisible()) {
                try {
                    object = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (null != object) {
                    if (!object.optString("status").isEmpty() && (Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_BAD_REQUEST
                            || Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_UNAUTHORIZED)) {
                        Toast.makeText(getActivity(), "Something went wrong please try again",
                                Toast.LENGTH_LONG).show();
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        new FetchOffersTask().execute();
                        parseListingData(object);

                    }
                }
            } else {
                FragmentManager fragmentManager = MainActivity.fragmentManager;
                fragmentManager.popBackStackImmediate();
            }


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getActivity().setTitle("Product Details");
    }

    private void parseListingData(JSONObject result) {
        productNameString = result.optString("name");
        Utils.setDetailsTextField("Name", getActivity(), productName, result.optString("name"));
        Utils.setDetailsTextField("Quantity ", getActivity(), quantity, result.optString("quantity"));
        if (!TextUtils.isEmpty(String.valueOf(result.optString("additionalComments")))) {
            additionalComments.setVisibility(View.VISIBLE);
            Utils.setDetailsTextField("Additional Comments", getActivity(), additionalComments, String.valueOf(result.optString("additionalComments")));
        } else {
            additionalComments.setVisibility(View.GONE);
        }
        Gson gson = new Gson();
        dimensionClass = gson.fromJson(String.valueOf(result.optJSONObject("dimension")), DimensionClass.class);
        Utils.setDetailsTextField("Dimesnions", getActivity(), dimensions, dimensionClass.width + "\"" + " x " + dimensionClass.height + "\" x " + dimensionClass.length+"\"");
        Map<String, Object> mapData = new HashMap<String, Object>();

        try {
            mapData =   parseJSONObjectToMap(result.optJSONObject("specifications"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (null != mapData && mapData.size() > 0 ){
            for(Map.Entry entry: mapData.entrySet()){
                View v = vi.inflate(R.layout.additiona_textview, null);
                TextView textView = (TextView) v.findViewById(R.id.additionalFields);
                Utils.setDetailsTextField(String.valueOf(entry.getKey()), getActivity(), textView, String.valueOf(entry.getValue()));
                additionaFieldLL.addView(v);
            }
            additionaFieldLL.setVisibility(View.VISIBLE);

        }else{
            additionaFieldLL.setVisibility(View.GONE);
        }
    }

    public static Map<String,Object> parseJSONObjectToMap(JSONObject jsonObject) throws JSONException{
        Map<String, Object> mapData = new HashMap<String, Object>();
        Iterator<String> keysItr = jsonObject.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            String value = String.valueOf(jsonObject.get(key));
            mapData.put(key, value);
        }
        return mapData;
    }

    public class FetchOffersTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... arg0) {

            try {
                SpannableStringBuilder string = new SpannableStringBuilder(WebServiceConstants.GET_SINGLE_PRODUCT_OFFERS);

                string.append(productId);
                URL url = new URL(string.toString());


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
//                conn.setDoInput(true);
//                conn.setDoOutput(true);
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
            if (null != result && isVisible()) {
                try {
                    object = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                offersList = new ArrayList<>();
                if (null != object) {
                    if (!object.optString("status").isEmpty() && (Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_BAD_REQUEST
                            || Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_UNAUTHORIZED)) {
                        Toast.makeText(getActivity(), "Something went wrong please try again",
                                Toast.LENGTH_LONG).show();
                    } else {
                        // show offers strip
                        if (object.has("offers")) {
                            try {
                                Gson gson = new Gson();
                                for (int i = 0; i < object.getJSONArray("offers").length(); i++) {
                                    OffersData item = gson.fromJson(String.valueOf(object.getJSONArray("offers").getJSONObject(i)), OffersData.class);
                                    offersList.add(item);
                                }
                                if (offersList.size() > 0) {
                                    viewOffersBtn.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
            } else {
                FragmentManager fragmentManager = MainActivity.fragmentManager;
                fragmentManager.popBackStackImmediate();
            }


        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addOrderBtn) {
            showAddpriceDialog();
        } else if (view.getId() == R.id.viewOffersBtn) {
            AddedOffersListFragment fragment = new AddedOffersListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("productId", productId);
            fragment.setOfferList(offersList);
            fragment.setArguments(bundle);
            MainActivity.addActionFragment(fragment);
        }

    }

    private void showAddpriceDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        final EditText edittext = new EditText(getActivity());
        alert.setTitle("Enter Your Offer Price");

        alert.setView(edittext);

        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                Editable textValue = edittext.getText();
                if (textValue.toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please input price ", Toast.LENGTH_LONG);
                } else {
                    price = textValue.toString();
                    new AddPriceTask().execute();
                }

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
                dialog.dismiss();
            }
        });

        alert.show();
    }

    public class AddPriceTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... arg0) {

            try {
                SpannableStringBuilder string = new SpannableStringBuilder(WebServiceConstants.ADD_PRICE);
                string.append(productId);
                string.append("/");
                string.append(price);
                URL url = new URL(string.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("PUT");
                conn.setDoInput(true);
                conn.setDoOutput(true);
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

            progressBar.setVisibility(View.GONE);
            if (isVisible()) {
                if (null != result && result.equalsIgnoreCase("true")) {
                    Toast.makeText(getActivity(), "Price Added Successfully",
                            Toast.LENGTH_LONG).show();
                    FragmentManager fragmentManager = MainActivity.fragmentManager;
                    fragmentManager.popBackStackImmediate();
                } else {
                    Toast.makeText(getActivity(), "Something went wrong please try again",
                            Toast.LENGTH_LONG).show();
                }

            } else {

                FragmentManager fragmentManager = MainActivity.fragmentManager;
                fragmentManager.popBackStackImmediate();

            }
        }
    }
    public void UploadImageToServer(){

        fixBitMap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byteArray = byteArrayOutputStream.toByteArray();

        convertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(getActivity(),"Image is Uploading","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                progressDialog.dismiss();

                Toast.makeText(getActivity(),string1,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();

                HashMapParams.put("imageTag", getImageNameFromEditTextString);

                HashMapParams.put("imageName", convertImage);

                String urlImage = WebServiceConstants.UPLOAD_IMAGE+ productId ;
                String FinalData = imageProcessClass.ImageHttpRequest(urlImage, HashMapParams);

                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();
    }

    public class ImageProcessClass{

        public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {
                url = new URL(requestURL);

                httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(20000);

                httpURLConnection.setConnectTimeout(20000);

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Authorization", SharedPreferences.getString(getActivity(), SharedPreferences.KEY_AUTHTOKEN));

                httpURLConnection.setDoInput(true);

                httpURLConnection.setDoOutput(true);

                outputStream = httpURLConnection.getOutputStream();

                bufferedWriter = new BufferedWriter(

                        new OutputStreamWriter(outputStream, "UTF-8"));

                bufferedWriter.write(bufferedWriterDataFN(PData));

                bufferedWriter.flush();

                bufferedWriter.close();

                outputStream.close();

                RC = httpURLConnection.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReader.readLine()) != null){

                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            stringBuilder = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
                if (check)
                    check = false;
                else
                    stringBuilder.append("&");

                stringBuilder.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilder.append("=");

                stringBuilder.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilder.toString();
        }

    }
    @Override
    public void onActivityResult(int RC, int RQC, Intent I) {

        super.onActivityResult(RC, RQC, I);

        if (RC == 1 && RQC == RESULT_OK && I != null && I.getData() != null) {

            Uri uri = I.getData();

            try {

                fixBitMap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);

                showSelectedImage.setImageBitmap(fixBitMap);

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }
}
