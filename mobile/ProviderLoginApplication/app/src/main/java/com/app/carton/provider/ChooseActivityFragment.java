package com.app.carton.provider;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by aggarwal.swati on 12/26/18.
 */

public class ChooseActivityFragment extends Fragment implements View.OnClickListener {
    private static View view;
    private ProgressBar progressBar;
    private int inprogressCount, productPricingCount,quotationOrderCount;

    @Override
    public void onClick(View view) {



    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Welcome");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) { ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false); }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.choose_action, container, false);
        inflateViews();
        return view;
    }

    private void inflateViews() {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        ActionAdapter adapter = new ActionAdapter(4);
        recyclerView.setAdapter(adapter);
        adapter.SetOnItemClickListener(new ActionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if ((int)view.getTag() == 0){
                    // Get FragmentManager and FragmentTransaction object.
                    MainActivity.addActionFragment(new ProviderOngoingOrdersListFragment());

                }else if ((int)view.getTag() == 1){
                    MainActivity.addActionFragment(new CompletedOrderListFragment());
                }else if ((int)view.getTag() == 2){
                    MainActivity.addActionFragment(new PlacedOrderListFragment());
                }
                else if ((int)view.getTag() == 3){
                    MainActivity.addActionFragment(new ProductListOpenForPrice());
                }

            }
        });
        new FetchProviderDashboard().execute();
    }
    public class FetchProviderDashboard extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... arg0) {

            try {
                SpannableStringBuilder string = new SpannableStringBuilder(WebServiceConstants.GET_DASHBOARD_DETAILS);
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

            if (null != result) {
                try {
                    object = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (null!=object) {
                    if (!object.optString("status").isEmpty()) {
                        if ((Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_BAD_REQUEST)
                                || (Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_UNAUTHORIZED)
                                || ((Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_FORBIDDEN))) {
                            Toast.makeText(getActivity(), "Something went wrong please try again",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(), "Something went wrong please try again",
                                    Toast.LENGTH_LONG).show();
                        }
                    }else {

//                        new OrderDetailFragment.FetchDetailsTask().execute();
                        parseOrderListingData(object);


                    }
                }
            }else  {
                Toast.makeText(getActivity(), "Something went wrong please try again",
                        Toast.LENGTH_LONG).show();
            }


        }
    }

    private void parseOrderListingData(JSONObject object) {
        inprogressCount = object.optInt("inProgressCount");
        quotationOrderCount = object.optInt("quotationOrderCount");
        productPricingCount = object.optInt("productPricingCount");
    }
}
