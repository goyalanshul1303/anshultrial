package com.application.onboarding.providersob;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by aggarwal.swati on 2/11/19.
 */

public class ProductsItemAdapter extends RecyclerView.Adapter<ProductsItemAdapter.CustomViewHolder>
        implements View.OnClickListener {
    OnItemClickListener mItemClickListener;

    private ArrayList<ProductsDetailsItem> data = new ArrayList();
    Context context;
    private boolean isFromOfferListing;
    private ArrayList<String> selectedIds = new ArrayList<>();

    public ProductsItemAdapter(Context mContext, ArrayList<ProductsDetailsItem> data){
        context = mContext;
        this.data = data;

}

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.listview_item_row, null);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        ProductsDetailsItem testObjtem = data.get(i);
        if (null == testObjtem.name || testObjtem.name.isEmpty() ){
            customViewHolder.textView.setText("N/A");
        }else{
            customViewHolder.textView.setText(testObjtem.name);
        }
//        Utils.setDetailsTextField("Carton Type", getActivity(), cartonType, cartonTypeString);
//        customViewHolder.customerEmail.setText("Email : " + testObjtem.email);
        customViewHolder.textLL.setTag(i);
        customViewHolder.textLL.setOnClickListener(this);
        customViewHolder.checkBox.setTag(i);
        if (isFromOfferListing)
                customViewHolder.checkBox.setVisibility(View.VISIBLE);
        else{
            customViewHolder.checkBox.setVisibility(View.GONE);
        }
        customViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductsDetailsItem testObjtem =   data.get((Integer) view.getTag());
                if(((AppCompatCheckBox) view).isChecked()){
                    System.out.println("Checked");
                    selectedIds.add(testObjtem.id);

                } else {
                    System.out.println("Un-Checked");
                    selectedIds.remove(testObjtem.id);
                }
                if (null!=mItemClickListener){
                    mItemClickListener.getSelectedProductIdsforOffer(selectedIds);
                }
            }
        });




    }

    @Override
    public int getItemCount() {
        return (null != data ? data.size() : 0);
    }

    public void setIsFromOfferListing(boolean isFromOfferListing) 
    {
        this.isFromOfferListing = isFromOfferListing;
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView textView, customerEmail;
        RelativeLayout textLL;
        AppCompatCheckBox checkBox;


        public CustomViewHolder(View view) {
            super(view);
            this.textView = (TextView) view.findViewById(R.id.customerName);
            customerEmail= (TextView)view.findViewById(R.id.customerEmail);
            textLL = (RelativeLayout) view.findViewById(R.id.textLL);
            checkBox = (AppCompatCheckBox) view.findViewById(R.id.checkbox);

        }

    }

    public interface OnItemClickListener {

        public void onItemClick(View view, int position);
        public void getSelectedProductIdsforOffer(ArrayList<String> ids);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public void onClick(View view) {
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick(view, (Integer) view.getTag());
        }
    }
}
