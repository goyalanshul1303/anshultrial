package com.app.carton.consumer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.carton.orders.R;

import java.util.ArrayList;

/**
 * Created by aggarwal.swati on 5/11/19.
 */

public class MyCartListAdapter extends RecyclerView.Adapter<MyCartListAdapter.CustomViewHolder>

{
    ProductsItemAdapter.OnItemClickListener mItemClickListener;

    private ArrayList<ProductsDetailsItem> data = new ArrayList();
    Context context;

    public MyCartListAdapter(Context mContext, ArrayList<ProductsDetailsItem> data){
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
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        ProductsDetailsItem testObjtem = data.get(i);
        if (null == testObjtem.name || testObjtem.name.isEmpty()) {
            customViewHolder.productName.setText("N/A");
        } else {
            customViewHolder.productName.setText(testObjtem.name);
        }
        if (null!=testObjtem.price ){
            customViewHolder.price.setText("\u20B9 " + testObjtem.price);
            customViewHolder.price.setVisibility(View.VISIBLE);

        }


    }

    @Override
    public int getItemCount() {
        return (null != data ? data.size() : 0);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView productName, quantity,  price;

        ImageButton increase, decrease;
        Button removeItem;


        public CustomViewHolder(View view) {
            super(view);
            this.productName = (TextView) view.findViewById(R.id.productName);

            price = (TextView)view.findViewById(R.id.price);

            increase = (ImageButton) view.findViewById(R.id.increase);
            increase.setOnClickListener(this);
            decrease = (ImageButton)view.findViewById(R.id.decrease);
            decrease.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
//            if (selectedItems.get(getAdapterPosition(), false)) {
//                selectedItems.delete(getAdapterPosition());
//                view.setSelected(false);
//            }
//            else {
//                selectedItems.put(getAdapterPosition(), true);
//                view.setSelected(true);
//            }
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
