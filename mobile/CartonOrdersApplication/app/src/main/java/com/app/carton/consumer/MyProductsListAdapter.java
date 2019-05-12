package com.app.carton.consumer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.carton.orders.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by aggarwal.swati on 5/11/19.
 */

public class MyProductsListAdapter extends RecyclerView.Adapter<MyProductsListAdapter.CustomViewHolder>
    implements View.OnClickListener
{
    ProductsItemAdapter.OnItemClickListener mItemClickListener;

    private ArrayList<ProductsDetailsItem> data = new ArrayList();
    Context context;

    public MyProductsListAdapter(Context mContext, ArrayList<ProductsDetailsItem> data){
        context = mContext;
        this.data = data;
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.my_product_item, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        ProductsDetailsItem testObjtem = data.get(i);
        if (null == testObjtem.name || testObjtem.name.isEmpty()) {
            customViewHolder.productName.setText("N/A");
        } else {
            customViewHolder.productName.setText(testObjtem.name);
        }
        if (null != testObjtem.dimension) {
        StringBuilder builder = new StringBuilder();
        builder.append( testObjtem.dimension.getWidth());
        builder.append(" x ");
        builder.append( testObjtem.dimension.getWidth());
        builder.append(" x ");
        builder.append( testObjtem.dimension.getLength());
        customViewHolder.dimensions.setText(builder.toString());
        if (testObjtem.isPreviousOrderPresent) {
            customViewHolder.noOrderll.setVisibility(View.GONE);
            customViewHolder.orderPlacedLL.setVisibility(View.VISIBLE);
            customViewHolder.orderStatusLL.setVisibility(View.VISIBLE);
        } else {
            customViewHolder.noOrderll.setVisibility(View.VISIBLE);
            customViewHolder.orderPlacedLL.setVisibility(View.GONE);
            customViewHolder.orderStatusLL.setVisibility(View.GONE);

        }
            customViewHolder.dimensions.setVisibility(View.VISIBLE);

        }else{
            customViewHolder.dimensions.setVisibility(View.GONE);
        }
//        Utils.setDetailsTextField("Carton Type", getActivity(), cartonType, cartonTypeString);
//        customViewHolder.customerEmail.setText("Email : " + testObjtem.email);

        customViewHolder.textLL.setTag(i);
        customViewHolder.noOrderll.setTag(i);


    }

    @Override
    public int getItemCount() {
        return (null != data ? data.size() : 0);
    }

    @Override
    public void onClick(View view) {

    }


    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView productName, dimensions, noOfBoxes, price,detailsLink, orderStatus,date;
        LinearLayout textLL,noOrderll,orderPlacedLL,boxDetails, orderStatusLL;
        private SparseBooleanArray selectedItems = new SparseBooleanArray();


        public CustomViewHolder(View view) {
            super(view);
            this.productName = (TextView) view.findViewById(R.id.productName);
            dimensions= (TextView)view.findViewById(R.id.dimensions);
            textLL = (LinearLayout)view.findViewById(R.id.textLL);
            orderPlacedLL = (LinearLayout) view.findViewById(R.id.orderPlacedLL);
            noOrderll = (LinearLayout) view.findViewById(R.id.noOrderll);
            boxDetails = (LinearLayout)view.findViewById(R.id.boxDetails);
            detailsLink = (TextView)view.findViewById(R.id.detailsLink);
            orderStatusLL = (LinearLayout)view.findViewById(R.id.orderStatusLL);
            orderStatus = (TextView)view.findViewById(R.id.orderStatus);
            date = (TextView)view.findViewById(R.id.date);
            price = (TextView)view.findViewById(R.id.price);
            noOfBoxes = (TextView)view.findViewById(R.id.noOfBoxes);
            textLL.setOnClickListener(this);
            noOrderll.setOnClickListener(this);
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

    public void SetOnItemClickListener(final ProductsItemAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

}
