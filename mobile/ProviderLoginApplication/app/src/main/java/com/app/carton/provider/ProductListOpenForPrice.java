package com.app.carton.provider;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
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
 * Created by aggarwal.swati on 4/25/19.
 */

public class ProductListOpenForPrice extends Fragment implements View.OnClickListener  {

    private static View view;

    private RecyclerView productsRecyclerView;

    private ProgressBar progressBar;
    DataView data = new DataView();
    Button createOrderBtn;
    private ProductsItemAdapter adapter;
    View viewNoOrdersAdded;
    private ArrayList<ProductsDetailsItem> productListItem;

    public ProductListOpenForPrice() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.products_list, container, false);
        initViews();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) { ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true); }

        getActivity().setTitle("Products For Offers");
    }

    // Initiate Views
    private void initViews() {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        productsRecyclerView = (RecyclerView) view.findViewById(R.id.productsRecyclerView);

        viewNoOrdersAdded = (View)view.findViewById(R.id.viewNoProductAdded);

        new GetAllProductsAsyncTask().execute();
    }

    public class GetAllProductsAsyncTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... arg0) {

            try {

                SpannableStringBuilder string = new SpannableStringBuilder(WebServiceConstants.OPEN_PRODUCT_ACCEPTING_OFFERS);
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
            if (isVisible()){
                if (null != result) {
                    if(result.trim().charAt(0) == '[') {
                        Log.e("Response is : " , "JSONArray");
                        parseListingData(result);
                    } else if(result.trim().charAt(0) == '{') {
                        try {
                            object = new JSONObject(result);
                            if (null != object && !object.optString("status").isEmpty() && ( Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_BAD_REQUEST
                                    || Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_UNAUTHORIZED)) {
                                Toast.makeText(getActivity(), "Something went wrong please try again",
                                        Toast.LENGTH_LONG).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else  {
                        Toast.makeText(getActivity(), "Something went wrong please try again",
                                Toast.LENGTH_LONG).show();
                    }


                }else  {
                    Toast.makeText(getActivity(), "Something went wrong please try again",
                            Toast.LENGTH_LONG).show();
                }
            }else{
                FragmentManager fragmentManager = MainActivity.fragmentManager;
                fragmentManager.popBackStackImmediate();
            }



        }
    }

    private void parseListingData(String result) {
        try {
            JSONArray list = new JSONArray(result);
            if (null!= list && list.length() > 0 ){
                Gson gson=new Gson();
                productListItem = new ArrayList<>();
                for (int i = 0 ; i < list.length() ;i ++){
                    ProductsDetailsItem item=gson.fromJson(String.valueOf((list.optJSONObject(i))),ProductsDetailsItem.class);
                    productListItem.add(item);
                }
                adapter = new ProductsItemAdapter(getActivity(), productListItem);
                productsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                productsRecyclerView.setAdapter(adapter);
                viewNoOrdersAdded.setVisibility(View.GONE);
                productsRecyclerView.setVisibility(View.VISIBLE);
                adapter.SetOnItemClickListener(new ProductsItemAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Fragment newFragment = new ProductDetailsFragment();
                        Bundle bundle = new Bundle();
                        ProductsDetailsItem item = productListItem.get(position);
                        bundle.putString("selectedId", item.consumerId);
                        bundle.putString("productId", item.id);
                        newFragment.setArguments(bundle);
                        MainActivity.addActionFragment(newFragment);

                    }
                });

            }else{
                // no orders added . please add consumer first
                viewNoOrdersAdded.setVisibility(View.VISIBLE);
                productsRecyclerView.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        MenuItem item=menu.findItem(R.id.placed_order);
        if(item!=null)
            item.setVisible(false);
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.createOrderBtn){
//            OrderDetailFragment fragment = new OrderDetailFragment();
//            Bundle bundle = new Bundle();
//            bundle.putString("consumerId", selectedId);
//            fragment.setArguments(bundle);
//            MainActivity.addActionFragment(fragment);
        }
    }

}
