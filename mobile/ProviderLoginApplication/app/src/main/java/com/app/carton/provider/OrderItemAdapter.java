package com.app.carton.provider;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by aggarwal.swati on 2/8/19.
 */

public class OrderItemAdapter  extends RecyclerView.Adapter<OrderItemAdapter.CustomViewHolder>
         {
    OnItemClickListener mItemClickListener;

    private ArrayList<OrdersListDetailsItem> data = new ArrayList();
    Context context;

    public OrderItemAdapter(Context mContext, ArrayList<OrdersListDetailsItem> data){
        context = mContext;
        this.data = data;

    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.orders_listview_item_row, null);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        OrdersListDetailsItem testObjtem = data.get(i);
        if (null == testObjtem.productName || testObjtem.productName.isEmpty() ){
            customViewHolder.textView.setText("N/A");
        }else{
            customViewHolder.textView.setText(testObjtem.productName);
        }
        if ( testObjtem.orderAmount>0){
            customViewHolder.priceProduct.setText("\u20B9 " + testObjtem.orderAmount);
            customViewHolder.priceProduct.setVisibility(View.VISIBLE);
        }else {
            customViewHolder.priceProduct.setVisibility(View.GONE);
        }
        customViewHolder.date.setText(Utils.getDate(testObjtem.orderDate));


//        Utils.setDetailsTextField("Carton Type", getActivity(), cartonType, cartonTypeString);
        customViewHolder.quantity.setText( testObjtem.quantity + " Nos");
        customViewHolder.textLL.setTag(i);

    }

    @Override
    public int getItemCount() {
        return (null != data ? data.size() : 0);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView, quantity,priceProduct,orderStatus,date;
        RelativeLayout textLL;
        private SparseBooleanArray selectedItems = new SparseBooleanArray();


        public CustomViewHolder(View view) {
            super(view);
            this.textView = (TextView) view.findViewById(R.id.productName);
            this.quantity = (TextView) view.findViewById(R.id.quantity);
            textLL =(RelativeLayout) view.findViewById(R.id.textLL);
            textLL.setOnClickListener(this);
            priceProduct = (TextView)view.findViewById(R.id.priceProduct);
            orderStatus = (TextView)view.findViewById(R.id.orderStatus);
            date = (TextView)view.findViewById(R.id.date);

        }
        @Override
        public void onClick(View view) {
            if (selectedItems.get(getAdapterPosition(), false)) {
                selectedItems.delete(getAdapterPosition());
                view.setSelected(false);
            }
            else {
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

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

}
