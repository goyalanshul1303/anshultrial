package com.app.carton.consumer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.carton.orders.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);

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
        customViewHolder.orderId.setText(testObjtem.id);
//        Utils.setDetailsTextField("Carton Type", getActivity(), cartonType, cartonTypeString);
        customViewHolder.quantity.setText("Quantity : " + testObjtem.quantity);
        if (testObjtem.orderStatus == 2){
            // order alread MANUFACTURER_ASSIGNED
            customViewHolder.awardStatus.setText("Already Awarded Manufature Assigned");
        }else{
            customViewHolder.awardStatus.setText("Order placed");

        }
        customViewHolder.year.setText(""+Utils.getCalenderFromTime(testObjtem.orderDate).get(Calendar.YEAR));
        customViewHolder.month.setText(""+Utils.getCalenderFromTime(testObjtem.orderDate).getDisplayName(Calendar.MONTH,Calendar.LONG, Locale.getDefault()));
        customViewHolder.date.setText(Utils.getDateofOdrerPlaced(Utils.getCalenderFromTime(testObjtem.orderDate).get(Calendar.DATE)));
        customViewHolder.mainLayout.setTag(i);

    }

    @Override
    public int getItemCount() {
        return (null != data ? data.size() : 0);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SparseBooleanArray selectedItems = new SparseBooleanArray();

        TextView textView, quantity, awardStatus,orderId, year, month, date;
        LinearLayout textLL;
        RelativeLayout mainLayout;


        public CustomViewHolder(View view) {
            super(view);
            this.textView = (TextView) view.findViewById(R.id.productName);
            this.quantity = (TextView) view.findViewById(R.id.quantity);
            textLL = (LinearLayout)view.findViewById(R.id.textLL);
            awardStatus = (TextView)view.findViewById(R.id.awardedStatus);
            orderId = (TextView) view.findViewById(R.id.orderId);
            mainLayout =(RelativeLayout)view.findViewById(R.id.mainLayout);
            mainLayout.setOnClickListener(this);
            date = (TextView)view.findViewById(R.id.date);
            year = (TextView)view.findViewById(R.id.yearText);
            month = (TextView)view.findViewById(R.id.month);

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
