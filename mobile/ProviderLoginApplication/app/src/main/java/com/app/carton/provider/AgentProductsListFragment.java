package com.app.carton.provider;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
 * Created by aggarwal.swati on 2/12/19.
 */

public class AgentProductsListFragment extends Fragment implements View.OnClickListener {

    private static View view;

    private RecyclerView productsRecyclerView;

    private ProgressBar progressBar;
    private String urlType;
    DataView data = new DataView();
    private MyProductsListAdapter adapter;
    View viewNoProductAdded;
    ArrayList<ProductsDetailsItem> productDetailsItems = new ArrayList<>();
    private Button addProductBtn;
    String productName;

    public AgentProductsListFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_products_list, container, false);
        setHasOptionsMenu(true);
        initViews();
        return view;
    }


    // Initiate Views
    private void initViews() {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        productsRecyclerView = (RecyclerView) view.findViewById(R.id.productsRecyclerView);
        viewNoProductAdded = (View) view.findViewById(R.id.viewNoProductAdded);
        addProductBtn = (Button) view.findViewById(R.id.addProductBtn);
        addProductBtn.setOnClickListener(this);


    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Product List");
        new GetAllProductsAsyncTask().execute();
    }

    public class GetAllProductsAsyncTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... arg0) {

            try {

                SpannableStringBuilder string = new SpannableStringBuilder(WebServiceConstants.GET_ALL_PRODUCTS);

                string.append("provider/");

                string.append(SharedPreferences.getString(getActivity(), "entityId"));
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
            if (isVisible()) {
                if (null != result) {
                    if (result.trim().charAt(0) == '[') {
                        Log.e("Response is : ", "JSONArray");
                        parseListingData(result);
                    } else if (result.trim().charAt(0) == '{') {
                        try {
                            object = new JSONObject(result);
                            if (null != object && !object.optString("status").isEmpty() && (Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_BAD_REQUEST
                                    || Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_UNAUTHORIZED)) {
                                Toast.makeText(getActivity(), "Something went wrong please try again",
                                        Toast.LENGTH_LONG).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Something went wrong please try again",
                                Toast.LENGTH_LONG).show();
                    }


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

    private void parseListingData(String result) {
        try {
            JSONArray list = new JSONArray(result);
            if (null != list && list.length() > 0) {
                Gson gson = new Gson();
                productDetailsItems = new ArrayList<>();
                for (int i = 0; i < list.length(); i++) {
                    ProductsDetailsItem item = gson.fromJson(String.valueOf(list.getJSONObject(i)), ProductsDetailsItem.class);
                    productDetailsItems.add(item);
                }
                adapter = new MyProductsListAdapter(getActivity(), productDetailsItems);
                productsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                productsRecyclerView.setAdapter(adapter);
                viewNoProductAdded.setVisibility(View.GONE);
                productsRecyclerView.setVisibility(View.VISIBLE);
                adapter.SetOnItemClickListener(new ProductsItemAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Fragment newFragment = new ProductDetailsFragment();
                        Bundle bundle = new Bundle();
                        ProductsDetailsItem item = productDetailsItems.get(position);

                        bundle.putString("selectedId", SharedPreferences.getString(getActivity(), "entityId"));


                        bundle.putString("productId", item.id);
                        productName = item.name;
                        newFragment.setArguments(bundle);
                        MainActivity.addActionFragment(newFragment);
                    }

                });

            } else {
                // no consumers added . please add consumer first
                viewNoProductAdded.setVisibility(View.VISIBLE);
                productsRecyclerView.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addProductBtn) {
            CreateRawProductFragment fragment = new CreateRawProductFragment();
            MainActivity.addActionFragment(fragment);
        }
    }
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.over_flow_item);
        if(item!=null)
            item.setVisible(false);
    }
}
