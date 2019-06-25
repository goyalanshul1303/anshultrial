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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by aggarwal.swati on 2/8/19.
 */

public class CompletedOrderListFragment extends Fragment implements View.OnClickListener  {

    private static View view;

    private RecyclerView orderListView;

    private ProgressBar progressBar;
    DataView data = new DataView();
    Button tryAgain;
    private OrderItemAdapter adapter;
    View viewNoOrdersAdded;
    TextView nothing_available;
    private ArrayList<OrdersListDetailsItem> orderListDetailsItems;

    public CompletedOrderListFragment() {

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

        getActivity().setTitle("Completed Orders");
    }

    // Initiate Views
    private void initViews() {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        orderListView = (RecyclerView) view.findViewById(R.id.ordersRecyclerView);
        nothing_available = (TextView) view.findViewById(R.id.nothing_available);
        viewNoOrdersAdded = (View)view.findViewById(R.id.viewNoOrdersAdded);
        tryAgain = (Button)view.findViewById(R.id.tryAgain);
        tryAgain.setOnClickListener(this);
        nothing_available = (TextView)  view.findViewById(R.id.nothing_available);

        new GetAllProductsAsyncTask().execute();
    }

    public class GetAllProductsAsyncTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... arg0) {

            try {

                SpannableStringBuilder string = new SpannableStringBuilder(WebServiceConstants.GET_COMPLETED_ORDERS);
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
                            if (null != object && !object.optString("status").isEmpty()) {
                                if (Integer.valueOf(object.optString("status")) == HttpURLConnection.HTTP_UNAUTHORIZED) {
                                    Toast.makeText(getActivity(), "You have been logged out",
                                            Toast.LENGTH_LONG).show();
                                    MainActivity.replaceLoginFragment(new ProviderLoginFragment());

                                } else
                                {
                                    viewNoOrdersAdded.setVisibility(View.VISIBLE);
                                    orderListView.setVisibility(View.GONE);
                                    tryAgain.setVisibility(View.VISIBLE);
                                    nothing_available.setText("Something went wrong, Please try again");

                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        viewNoOrdersAdded.setVisibility(View.VISIBLE);
                        orderListView.setVisibility(View.GONE);
                        tryAgain.setVisibility(View.VISIBLE);
                        nothing_available.setText("Something went wrong, Please try again");
                    }


                } else {
                    viewNoOrdersAdded.setVisibility(View.VISIBLE);
                    orderListView.setVisibility(View.GONE);
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
                        OrderDetailFragment newFragment = new OrderDetailFragment();
                        Bundle bundle = new Bundle();
                        OrdersListDetailsItem item = orderListDetailsItems.get(position);
                        bundle.putString("orderId", item.id);
                        bundle.putString("productId", item.productId);
                        newFragment.isFromCompleted(true);
                        newFragment.setArguments(bundle);
                        MainActivity.addActionFragment(newFragment);

                    }
                });

            }else{
                // no orders added . please add consumer first
                viewNoOrdersAdded.setVisibility(View.VISIBLE);
                nothing_available.setText("No Orders Completed by you");
                orderListView.setVisibility(View.GONE);
                tryAgain.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId()== R.id.tryAgain){
            new GetAllProductsAsyncTask().execute();
        }
    }

    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.over_flow_item);
        if(item!=null)
            item.setVisible(false);
    }
}
