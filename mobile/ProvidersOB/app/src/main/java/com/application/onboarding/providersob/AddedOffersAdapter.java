package com.application.onboarding.providersob;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by aggarwal.swati on 5/11/19.
 */

public class AddedOffersAdapter extends RecyclerView.Adapter<AddedOffersAdapter.CustomViewHolder>
       {

    ArrayList<OffersData> offersDataArrayList = new ArrayList<>();
    Context context;

    public AddedOffersAdapter (Context mContext, ArrayList<OffersData> data){
        context = mContext;
        offersDataArrayList = data;

    }
    @Override
    public AddedOffersAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.offers_item, null);

        AddedOffersAdapter.CustomViewHolder viewHolder = new AddedOffersAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AddedOffersAdapter.CustomViewHolder customViewHolder, int i) {
        String providerName = "";
        String price = null;
        if (null!=offersDataArrayList && offersDataArrayList.size() > 0) {
            OffersData testObjtem = offersDataArrayList.get(i);
        providerName=    testObjtem.providerName;
        price = String.valueOf(testObjtem.offerPrice);
        }

        if (null == providerName || providerName.isEmpty() ){
            customViewHolder.providerName.setText("N/A");
        }else{
            customViewHolder.providerName.setText(providerName);
        }
        if (null == price || price.isEmpty()){
            customViewHolder.price.setVisibility(View.GONE);
        }else
        {
            customViewHolder.price.setVisibility(View.VISIBLE);
        }
        customViewHolder.price.setText("Price : " + price);

    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (null!= offersDataArrayList && offersDataArrayList.size() > 0){
            size = offersDataArrayList.size();
        }
        return size;
    }



    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView providerName, price;
        RelativeLayout textLL;
        AppCompatCheckBox checkbox ;


        public CustomViewHolder(View view) {
            super(view);
            this.providerName = (TextView) view.findViewById(R.id.providerName);
            price= (TextView)view.findViewById(R.id.price);
            textLL = (RelativeLayout)view.findViewById(R.id.textLL);
        }

    }


}
