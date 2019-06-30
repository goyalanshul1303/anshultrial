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

public class OngoingOrderItemAdapter extends RecyclerView.Adapter<OngoingOrderItemAdapter.CustomViewHolder>
        implements View.OnClickListener {
    OrderItemAdapter.OnItemClickListener mItemClickListener;

    private ArrayList<OrdersListDetailsItem> data = new ArrayList();
    Context context;

    public OngoingOrderItemAdapter(Context mContext, ArrayList<OrdersListDetailsItem> data){
        context = mContext;
        this.data = data;

    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.ongoing_list_item, null);

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
//        Utils.setDetailsTextField("Carton Type", getActivity(), cartonType, cartonTypeString);
        customViewHolder.quantity.setText( testObjtem.quantity + " Nos");
        customViewHolder.priceProduct.setText("\u20B9 " + testObjtem.orderAmount);

        if (null!=testObjtem.statuses){
            for (int j = 0; j < testObjtem.statuses.size(); j++){
                OrderStatus orderStatus = testObjtem.statuses.get(j);
                if (orderStatus.status == 2){
                    customViewHolder.dateAssigned.setText(Utils.getDate(orderStatus.statusDate));
                }else {
                    customViewHolder.targetDate.setText("--");
                    customViewHolder.dateAssigned.setText("N/A");

                } if (orderStatus.status >= 3){
                    customViewHolder.initiatedDate.setText(Utils.getDate(orderStatus.statusDate));
                    if (null != testObjtem.awardedQuote){
                        customViewHolder.targetDate.setText(Utils.addDaysToDate(orderStatus.statusDate,testObjtem.awardedQuote.noOfDays));
                    }else{
                        customViewHolder.targetDate.setText("--");

                    }
                }
            }
        }
        customViewHolder.topRl.setTag(i);

    }

    @Override
    public int getItemCount() {
        return (null != data ? data.size() : 0);
    }

    @Override
    public void onClick(View view) {

    }


    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView, quantity,priceProduct,dateAssigned,initiatedDate,targetDate;
        RelativeLayout topRl;
        private SparseBooleanArray selectedItems = new SparseBooleanArray();


        public CustomViewHolder(View view) {
            super(view);
            this.textView = (TextView) view.findViewById(R.id.productName);
            this.quantity = (TextView) view.findViewById(R.id.quantity);
            topRl =(RelativeLayout) view.findViewById(R.id.topRl);
            priceProduct = (TextView)view.findViewById(R.id.priceProduct);
            dateAssigned= (TextView)view.findViewById(R.id.dateAssigned);
            initiatedDate = (TextView)view.findViewById(R.id.initiatedDate);
            targetDate = (TextView)view.findViewById(R.id.targetDate);
            topRl.setOnClickListener(this);

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

    public void SetOnItemClickListener(final OrderItemAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


}
