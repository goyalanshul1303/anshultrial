package com.app.carton.consumer;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
 * Created by aggarwal.swati on 2/8/19.
 */

public class ConsumerOrderListFragment extends Fragment implements View.OnClickListener  {

    private static View view;

    private RecyclerView orderListView;

    CreateOrderRequest request = new CreateOrderRequest();
    private ProgressBar progressBar;
    DataView data = new DataView();
    Button goToProductBtn;
    private OrderItemAdapter adapter;
    View viewNoOrdersAdded;
    private ArrayList<OrdersListDetailsItem> orderListDetailsItems;

    public ConsumerOrderListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.order_list, container, false);
        initViews();

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) { ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true); }

        getActivity().setTitle("Open Orders");
    }

    // Initiate Views
    private void initViews() {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        orderListView = (RecyclerView) view.findViewById(R.id.ordersRecyclerView);

        viewNoOrdersAdded = (View)view.findViewById(R.id.viewNoOrdersAdded);
        goToProductBtn = (Button)view.findViewById(R.id.goToProductBtn);
        goToProductBtn.setOnClickListener(this);

        new GetAllOpenOrdersAsyncTask().execute();
    }

    public class GetAllOpenOrdersAsyncTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... arg0) {

            try {

                SpannableStringBuilder string = new SpannableStringBuilder(WebServiceConstants.GET_ORDERS);
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

//
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
                            MainActivity.replaceLoginFragment(new ConsumerLoginFragment());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else  {
                    if (null!= getActivity()){
                        Toast.makeText(getActivity(), "Something went wrong please try again",
                                Toast.LENGTH_LONG).show();
                        MainActivity.replaceLoginFragment(new ConsumerLoginFragment());
                    }

                }


            }else  {
                if (null!= getActivity()) {
                    Toast.makeText(getActivity(), "Something went wrong please try again",
                            Toast.LENGTH_LONG).show();
                }
            }


        }
    }

    private void parseListingData(String result) {
        try {
            JSONArray list = new JSONArray(result);
            if (null!= list && list.length() > 0 ){
                Gson gson=new Gson();
                orderListDetailsItems = new ArrayList<>();
                for (int i = 0 ; i < list.length() ;i ++){
                    OrdersListDetailsItem item=gson.fromJson(String.valueOf((list.optJSONObject(i))),OrdersListDetailsItem.class);
                    orderListDetailsItems.add(item);
                }
                adapter = new OrderItemAdapter(getActivity(), orderListDetailsItems);
                orderListView.setLayoutManager(new LinearLayoutManager(getActivity()));
                orderListView.setAdapter(adapter);
                viewNoOrdersAdded.setVisibility(View.GONE);
                orderListView.setVisibility(View.VISIBLE);
                adapter.SetOnItemClickListener(new OrderItemAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        Fragment newFragment = new OpenOrdersDetailFragment();
                        Bundle bundle = new Bundle();
                        OrdersListDetailsItem item = orderListDetailsItems.get(position);
                        bundle.putString("orderId", item.id);
                        bundle.putBoolean("isFromOpenOrders", true);
                        newFragment.setArguments(bundle);
                        MainActivity.addActionFragment(newFragment);

                    }
                });


            }else{
                // take user to open products  screen
              MainActivity.replaceLoginFragment(new ConsumerProductsListFragment());
//                viewNoOrdersAdded.setVisibility(View.VISIBLE);
//                orderListView.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.goToProductBtn){
            ConsumerOrderListFragment fragment = new ConsumerOrderListFragment();
            Bundle bundle = new Bundle();
//            bundle.putString("consumerId", selectedId);
            fragment.setArguments(bundle);
            MainActivity.addActionFragment(fragment);
        }
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.open_orders_list);
        if(item!=null)
            item.setVisible(false);

    }

}
