package com.app.carton.consumer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

public class AllProductsListAdapter extends RecyclerView.Adapter<AllProductsListAdapter.CustomViewHolder> {
    ProductsItemAdapter.OnItemClickListener mItemClickListener;

    private ArrayList<ProductsDetailsItem> data = new ArrayList();
    Context context;
    private int productCount = 0;
    ProgressDialog pd;

    public AllProductsListAdapter(Context mContext, ArrayList<ProductsDetailsItem> data) {
        context = mContext;
        this.data = data;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.all_product_item, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, int i) {
        final ProductsDetailsItem testObjtem = data.get(i);
        if (null == testObjtem.name || testObjtem.name.isEmpty()) {
            customViewHolder.productName.setText("N/A");
        } else {
            customViewHolder.productName.setText(testObjtem.name);
        }
        if (null != testObjtem.price) {
            customViewHolder.priceProduct.setText("\u20B9 " + testObjtem.price);
            customViewHolder.priceProduct.setVisibility(View.VISIBLE);

        } else customViewHolder.priceProduct.setVisibility(View.GONE);
        if (null != testObjtem.dimension) {
            StringBuilder builder = new StringBuilder();
            builder.append(testObjtem.dimension.getWidth());
            builder.append("\" x ");
            builder.append(testObjtem.dimension.getWidth());
            builder.append("\" x ");
            builder.append(testObjtem.dimension.getLength());
            builder.append("\"");
            customViewHolder.dimensions.setText(builder.toString());

            customViewHolder.dimensions.setVisibility(View.VISIBLE);

        } else {
            customViewHolder.dimensions.setVisibility(View.GONE);
        }
        customViewHolder.addQuantityButton.setTag(i);
        customViewHolder.addQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProductsDetailsItem testObjtem = data.get((Integer) view.getTag());
                showALertDialog(testObjtem.id);
            }
        });
//        Utils.setDetailsTextField("Carton Type", getActivity(), cartonType, cartonTypeString);
//        customViewHolder.customerEmail.setText("Email : " + testObjtem.email);

//        customViewHolder.textLL.setTag(i);
//        customViewHolder.increase.setTag(i);
//        customViewHolder.decrease.setTag(i);
//        customViewHolder.increase.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                final ProductsDetailsItem testObjtem = data.get((Integer) view.getTag());
//
//                testObjtem.setQuantityCount(Integer.valueOf(testObjtem.getQuantityCount()) + 1);
//                callInsertAPI(testObjtem.getId(), testObjtem.getQuantityCount());
//                notifyDataSetChanged();
//            }
//        });
//        customViewHolder.decrease.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Integer.valueOf(customViewHolder.quantityNumber.getText().toString()) > 0) {
//                    final ProductsDetailsItem testObjtem = data.get((Integer) view.getTag());
//                    testObjtem.setQuantityCount(Integer.valueOf(testObjtem.getQuantityCount()) - 1);
//                    notifyDataSetChanged();
//                    callRemoveAPI(testObjtem.getId(), testObjtem.getQuantityCount());
//                }
//
//
//            }
//        });

//        customViewHolder.quantityNumber.setText("" + testObjtem.getQuantityCount());
//        if (customViewHolder.quantityNumber.getText() == "0") {
//            customViewHolder.decrease.setEnabled(false);
//            customViewHolder.decrease.setClickable(false);
//
//        } else {
//            customViewHolder.decrease.setEnabled(true);
//            customViewHolder.decrease.setClickable(true);
//
//        }
    }


    private void callInsertAPI(String id, int productCount) {
        new InsertItemToCartAsyncTask().execute(id, String.valueOf(productCount), "false");

    }

    @Override
    public int getItemCount() {
        return (null != data ? data.size() : 0);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView productName, dimensions, price, priceProduct;
        LinearLayout noOrderll;
        private SparseBooleanArray selectedItems = new SparseBooleanArray();
        RelativeLayout textLL;
        Button addQuantityButton;


        public CustomViewHolder(View view) {
            super(view);
            this.productName = (TextView) view.findViewById(R.id.productName);
            dimensions = (TextView) view.findViewById(R.id.dimensions);
            textLL = (RelativeLayout) view.findViewById(R.id.textLL);
            noOrderll = (LinearLayout) view.findViewById(R.id.noOrderll);
            price = (TextView) view.findViewById(R.id.price);
            priceProduct = (TextView) view.findViewById(R.id.priceProduct);
            addQuantityButton = (Button)view.findViewById(R.id.addQuantityButton);

        }

        @Override
        public void onClick(View view) {
            if (selectedItems.get(getAdapterPosition(), false)) {
                selectedItems.delete(getAdapterPosition());
                view.setSelected(false);
            } else {
                selectedItems.put(getAdapterPosition(), true);
                view.setSelected(true);
            }
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
