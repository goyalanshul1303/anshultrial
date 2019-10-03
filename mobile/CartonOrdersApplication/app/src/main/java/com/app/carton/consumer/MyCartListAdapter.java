package com.app.carton.consumer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.carton.orders.R;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by aggarwal.swati on 5/11/19.
 */

public class MyCartListAdapter extends RecyclerView.Adapter<MyCartListAdapter.CustomViewHolder> {
    ProductsItemAdapter.OnItemClickListener mItemClickListener;

    private ArrayList<InsertItemRequest> data = new ArrayList();
    Context context;
    private int productCount = 0;
    ProgressDialog pd;

    public MyCartListAdapter(Context mContext, ArrayList<InsertItemRequest> data) {
        context = mContext;
        this.data = data;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_cart, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, int i) {
        final InsertItemRequest testObjtem = data.get(i);
        if (null == testObjtem.getProductId() || testObjtem.getProductId().isEmpty()) {
            customViewHolder.productName.setText("N/A");
        } else {
            customViewHolder.productName.setText(testObjtem.getProductId());
        }
        customViewHolder.ediQuantityButton.setTag(i);
        customViewHolder.ediQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final InsertItemRequest testObjtem = data.get((Integer) view.getTag());
                showALertDialog(testObjtem.productId);
            }
        });
//        if (null != testObjtem.price) {
//            customViewHolder.price.setText("\u20B9 " + testObjtem.price);
//            customViewHolder.price.setVisibility(View.VISIBLE);
//
//        }
        customViewHolder.removeItem.setTag(i);
        customViewHolder.removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final InsertItemRequest testObjtem = data.get((Integer) view.getTag());
//                    testObjtem.setQuantityCount(Integer.valueOf(testObjtem.getQuantityCount()) - 1);
                    notifyDataSetChanged();
                    callRemoveAPI(testObjtem.productId, 0);

            }
        });

        customViewHolder.quantityNumber.setText("" + testObjtem.getQuantity());
    }

    private void callRemoveAPI(String id, int productCount) {
        new InsertItemToCartAsyncTask().execute(id, String.valueOf(productCount), "true");
    }

    private void callInsertAPI(String id, int productCount) {
        new InsertItemToCartAsyncTask().execute(id, String.valueOf(productCount), "false");

    }

    @Override
    public int getItemCount() {
        return (null != data ? data.size() : 0);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView productName, quantity, price, quantityNumber;
        ImageView removeItem;
        ImageButton ediQuantityButton;


        public CustomViewHolder(View view) {
            super(view);
            this.productName = (TextView) view.findViewById(R.id.productNameTextView);

            price = (TextView) view.findViewById(R.id.price);
            removeItem = (ImageView) view.findViewById(R.id.delete_btn);
            ediQuantityButton = (ImageButton)view.findViewById(R.id.ediQuantityButton);
            quantityNumber = (TextView) view.findViewById(R.id.integer_number);
        }

        @Override
        public void onClick(View view) {

            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(view, (Integer) view.getTag());
            }
        }
    }

    public interface OnItemClickListener {

        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final ProductsItemAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public class InsertItemToCartAsyncTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            pd = ProgressDialog.show(context, "", "Please Wait", false);

        }

        protected String doInBackground(String... arg0) {

            try {
                String url = "";
                if (arg0[2].toString().equalsIgnoreCase("false")) {
                    url = WebServiceConstants.INSERT_ITEM_TO_CART;
                } else {
                    url = WebServiceConstants.REMOVE_ITEM_FROM_CART;

                }
                URL stringUrl = new URL(url);

                HttpURLConnection conn = (HttpURLConnection) stringUrl.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("PUT");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", SharedPreferences.getString(context, SharedPreferences.KEY_AUTHTOKEN));

                JSONObject object = new JSONObject();
                InsertItemRequest request = new InsertItemRequest();
                request.productId = arg0[0].toString();
                request.quantity = Integer.valueOf(arg0[1]);


                Gson gson = new Gson();
//                new GsonBuilder().create().toJson(this, Producto.class);
                String json = gson.toJson(request);

//                selectedIdString = selectedIdString.replace("[","").replace("]","").replaceAll("\\s","").trim();
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(String.valueOf(json));

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
            pd.dismiss();


        }
    }

    private void showALertDialog(final String id) {
        final EditText taskEditText = new EditText(context);
        taskEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Add quantity")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Editable task = (taskEditText.getText());
                        callInsertAPI(id, Integer.valueOf(task.toString()));
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }
}
