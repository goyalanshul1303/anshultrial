package com.app.carton.provider;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by aggarwal.swati on 5/11/19.
 */

public class MyConsumersListAdapter extends RecyclerView.Adapter<MyConsumersListAdapter.CustomViewHolder>

{
    ProductsItemAdapter.OnItemClickListener mItemClickListener;

    private ArrayList<String> data = new ArrayList();
    Context context;

    public MyConsumersListAdapter(Context mContext, ArrayList<String> data){
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
        String testObjtem = data.get(i);
        if (null == testObjtem) {
            customViewHolder.productName.setText("N/A");
        } else {
            customViewHolder.productName.setText(testObjtem);
        }

//        if (null != testObjtem.dimension) {
//        StringBuilder builder = new StringBuilder();
//        builder.append( testObjtem.dimension.getWidth());
//        builder.append(" x ");
//        builder.append( testObjtem.dimension.getWidth());
//        builder.append(" x ");
//        builder.append( testObjtem.dimension.getLength());
//        customViewHolder.dimensions.setText(builder.toString());
//
//            customViewHolder.dimensions.setVisibility(View.VISIBLE);
//
//        }else{
            customViewHolder.dimensions.setVisibility(View.GONE);
//        }
//        Utils.setDetailsTextField("Carton Type", getActivity(), cartonType, cartonTypeString);
//        customViewHolder.customerEmail.setText("Email : " + testObjtem.email);

        customViewHolder.textLL.setTag(i);

    }

    @Override
    public int getItemCount() {
        return (null != data ? data.size() : 0);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView productName, dimensions;
        LinearLayout noOrderll,orderPlacedLL;
        private SparseBooleanArray selectedItems = new SparseBooleanArray();
        RelativeLayout  textLL;
        ImageView reorder;


        public CustomViewHolder(View view) {
            super(view);
            this.productName = (TextView) view.findViewById(R.id.productName);
            dimensions= (TextView)view.findViewById(R.id.dimensions);
            textLL = (RelativeLayout)view.findViewById(R.id.topRl);
            textLL.setOnClickListener(this);

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
