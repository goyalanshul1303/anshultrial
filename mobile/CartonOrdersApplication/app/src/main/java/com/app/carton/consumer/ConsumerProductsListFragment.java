package com.app.carton.consumer;


import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.carton.orders.R;
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

public class ConsumerProductsListFragment extends Fragment implements View.OnClickListener {

    private static View view;

    private RecyclerView productsRecyclerView;

    private ProgressBar progressBar;
    TextView nothing_available;
    private String urlType;
    DataView data = new DataView();
    private MyProductsListAdapter adapter;
    View viewNoProductAdded;
    ArrayList<ProductsDetailsItem> productDetailsItems = new ArrayList<>();
    private Button tryAgain;
    String customerType = "";
    private String selectedId = "";
    private String consumerId;
    private String productName;

    public ConsumerProductsListFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null){
            customerType = getArguments().containsKey("urlType") ? getArguments().getString("urlType") : "";
            selectedId = getArguments().containsKey("selectedId") ? getArguments().getString("selectedId") : "";
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.products_list, container, false);
        initViews();
        setHasOptionsMenu(true);
        return view;
    }


    // Initiate Views
    private void initViews() {
        nothing_available = (TextView)  view.findViewById(R.id.nothing_available);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        productsRecyclerView = (RecyclerView) view.findViewById(R.id.productsRecyclerView);
        viewNoProductAdded = (View)view.findViewById(R.id.viewNoProductAdded);
        tryAgain = (Button)view.findViewById(R.id.tryAgain);
        tryAgain.setOnClickListener(this);

        new GetAllProductsAsyncTask().execute();

    }

    public class GetAllProductsAsyncTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... arg0) {

            try {

                SpannableStringBuilder string = new SpannableStringBuilder(WebServiceConstants.GET_ALL_PRODUCTS);
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
                if (result.trim().charAt(0) == '[') {
                    Log.e("Response is : ", "JSONArray");
                    parseListingData(result);
                } else if (result.trim().charAt(0) == '{') {
                    try {
                        object = new JSONObject(result);
                        if (null != object && !object.optString("status").isEmpty() ) {
                            if (Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_UNAUTHORIZED) {
                                Toast.makeText(getActivity(), "You have been logged out",
                                        Toast.LENGTH_LONG).show();
                                MainActivity.replaceLoginFragment(new ConsumerLoginFragment());

                            } else
                                {
                                    viewNoProductAdded.setVisibility(View.VISIBLE);
                                    productsRecyclerView.setVisibility(View.GONE);
                                    tryAgain.setVisibility(View.VISIBLE);
                                    nothing_available.setText("Something went wrong, Please try again");

                                }
                            }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    viewNoProductAdded.setVisibility(View.VISIBLE);
                    productsRecyclerView.setVisibility(View.GONE);
                    tryAgain.setVisibility(View.VISIBLE);
                    nothing_available.setText("Something went wrong, Please try again");
                }


            } else {
                viewNoProductAdded.setVisibility(View.VISIBLE);
                productsRecyclerView.setVisibility(View.GONE);
                tryAgain.setVisibility(View.VISIBLE);
                nothing_available.setText("Something went wrong, Please try again");


            }
        }else{
                FragmentManager fragmentManager = MainActivity.fragmentManager;
                fragmentManager.popBackStackImmediate();
            }

        }
    }

    private void parseListingData(String result) {
        try {
            JSONArray list = null;
            try {
                list = new JSONArray(result);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            if (null!= list && list.length() > 0 ){
                Gson gson=new Gson();
                productDetailsItems = new ArrayList<>();
                for (int i = 0 ; i < list.length() ;i ++){
                    ProductsDetailsItem item= null;
                    try {
                        item = gson.fromJson(String.valueOf(list.getJSONObject(i)),ProductsDetailsItem.class);
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    productDetailsItems.add(item);
                    consumerId = item.consumerId;
                }
                adapter = new MyProductsListAdapter(getActivity(), productDetailsItems);
                productsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                productsRecyclerView.setAdapter(adapter);
                viewNoProductAdded.setVisibility(View.GONE);
                productsRecyclerView.setVisibility(View.VISIBLE);
                adapter.SetOnItemClickListener(new ProductsItemAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        ProductsDetailsItem item = productDetailsItems.get(position);
                        if (view.getId() == R.id.textLL){
                            Bundle bundle = new Bundle();
                            Fragment newFragment = new ProductDetailsFragment();
                            bundle.putString("selectedId", item.consumerId);
                            bundle.putString("productId", item.id);
                            productName = item.name;
                            newFragment.setArguments(bundle);
                            MainActivity.addActionFragment(newFragment);
                        }else  if(view.getId()== R.id.noOrderll ){
                            CreateOrderFragment fragment = new CreateOrderFragment();
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("isFromProductDetail", true);
                            bundle.putString("productId", item.id);
                            fragment.setProductPrice(item.price);
                            fragment.setDimension(item.dimension);
                            fragment.setProductName(item.name);

                            fragment.setArguments(bundle);
                            MainActivity.addActionFragment(fragment);
                        }
                        else  if(view.getId()== R.id.detailsLink || view.getId() == R.id.reorder){
                         // create order details link

                              Bundle bundle = new Bundle();
                              bundle.putString("orderId", item.getLastOrder().getId());
                            if(item.getLastOrder().getOrderStatus() == 1) {
                                Fragment newFragment = new QuotationListingFragment();
                                bundle.putBoolean("isFromOpenOrders", false);
                                newFragment.setArguments(bundle);
                                MainActivity.addActionFragment(newFragment);
                            }else {
                                Fragment newFragment = new OpenOrdersDetailFragment();
                                bundle.putBoolean("isFromOpenOrders", true);
                                newFragment.setArguments(bundle);
                                MainActivity.addActionFragment(newFragment);
                            }

                            }

                    }
                });

            }else{
                // no consumers added . please add consumer first
                viewNoProductAdded.setVisibility(View.VISIBLE);
                tryAgain.setVisibility(View.GONE);
                productsRecyclerView.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Product List");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) { ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true); }

    }
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.over_flow_item);
        if(item!=null)
            item.setVisible(false);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO your code to hide item here
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onClick(View view) {
if (view.getId()== R.id.tryAgain){
    new GetAllProductsAsyncTask().execute();
}
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_create_order:
                // create order
                CreateOrderFragment fragment = new CreateOrderFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("isFromProductDetail", false);
                fragment.setProductList(productDetailsItems);
                fragment.setArguments(bundle);
                MainActivity.addActionFragment(fragment);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
