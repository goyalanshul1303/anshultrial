package com.application.onboarding.providersob;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by aggarwal.swati on 2/11/19.
 */

public class OnboardedCostumersListFragment extends Fragment implements View.OnClickListener {

    private static View view;

    private RecyclerView orderListView;

    private ProgressBar progressBar;
    private String urlType;
    DataView data = new DataView();
    private ConsumersItemAdapter adapter;
    View viewNoAgentAdded;
    ArrayList<ConsumerDetailsItem> consumerDetailsItems = new ArrayList<>();
    private Button addAgentBtn;


    public OnboardedCostumersListFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String customerType = "";
        if (getArguments() != null){
            customerType = getArguments().containsKey("urlType") ? getArguments().getString("urlType") : "";
        }
        if (customerType.equalsIgnoreCase("consumers")){
            urlType = WebServiceConstants.CREATE_CONSUMER;
        }else{
            urlType = WebServiceConstants.CREATE_PROVIDER;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.consumer_list, container, false);
        initViews();
        return view;
    }

    public void setCustomerBoardedType(String customerType)
    {

    }

    // Initiate Views
    private void initViews() {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        orderListView = (RecyclerView) view.findViewById(R.id.consumersRecyclerView);
        viewNoAgentAdded = (View)view.findViewById(R.id.viewNoAgentAdded);
        addAgentBtn = (Button)view.findViewById(R.id.addAgent);
        addAgentBtn.setOnClickListener(this);

        new GetConsumerListTask().execute();
    }

    public class GetConsumerListTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL(urlType);


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

//
            if (null != result) {
                try {
                    object = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (null != object && !object.optString("status").isEmpty() && ( Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_BAD_REQUEST
                        || Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_UNAUTHORIZED)) {
                    Toast.makeText(getActivity(), "Something went wrong please try again",
                            Toast.LENGTH_LONG).show();
//                if (getFragmentManager().getBackStackEntryCount() > 0) {
//                    getFragmentManager().popBackStackImmediate();
//                }
                }else{
                    parseListingData(result);

                }
            }else  {
                Toast.makeText(getActivity(), "Something went wrong please try again",
                        Toast.LENGTH_LONG).show();
            }


        }
    }

    private void parseListingData(String result) {
        try {
            JSONArray list = new JSONArray(result);
            if (null!= list && list.length() > 0 ){
                Gson gson=new Gson();

                for (int i = 0 ; i < list.length() ;i ++){
                    ConsumerDetailsItem item=gson.fromJson(String.valueOf(list.getJSONObject(i)),ConsumerDetailsItem.class);
                    consumerDetailsItems.add(item);
                }
                adapter = new ConsumersItemAdapter(getActivity(), consumerDetailsItems);
                orderListView.setLayoutManager(new LinearLayoutManager(getActivity()));
                orderListView.setAdapter(adapter);
                viewNoAgentAdded.setVisibility(View.GONE);
                orderListView.setVisibility(View.VISIBLE);
                adapter.SetOnItemClickListener(new ConsumersItemAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        Fragment newFragment = new AgentDetailPagerFragment();
                        Bundle bundle = new Bundle();
                        ConsumerDetailsItem item = consumerDetailsItems.get(position);
                        bundle.putString("selectedId", item.id);
                        newFragment.setArguments(bundle);
                        MainActivity.addActionFragment(newFragment);

                    }
                });

            }else{
                // no consumers added . please add consumer first
                viewNoAgentAdded.setVisibility(View.VISIBLE);
                orderListView.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addAgent){
            MainActivity.addActionFragment(new ChooseActivityFragment());
        }
    }

}
