package com.application.onboarding.providersob;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by aggarwal.swati on 2/11/19.
 */

public class ConsumersItemAdapter extends RecyclerView.Adapter<ConsumersItemAdapter.CustomViewHolder>
        implements View.OnClickListener {


    private ArrayList<ConsumerDetailsItem> data = new ArrayList();
    Context context;

    public ConsumersItemAdapter (Context mContext, ArrayList<ConsumerDetailsItem> data){
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
        ConsumerDetailsItem testObjtem = data.get(i);
        if (null == testObjtem.consumerName || testObjtem.consumerName.isEmpty() ){
            customViewHolder.textView.setText("N/A");
        }else{
            customViewHolder.textView.setText(testObjtem.consumerName);
        }
        if (null == testObjtem.email || testObjtem.email.isEmpty()){
            customViewHolder.customerEmail.setVisibility(View.GONE);
        }else
        {
            customViewHolder.customerEmail.setVisibility(View.VISIBLE);
        }
        customViewHolder.customerEmail.setText("Email : " + testObjtem.email);

    }

    @Override
    public int getItemCount() {
        return (null != data ? data.size() : 0);
    }

    @Override
    public void onClick(View view) {

    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView textView, customerEmail;


        public CustomViewHolder(View view) {
            super(view);
            this.textView = (TextView) view.findViewById(R.id.customerName);
            customerEmail= (TextView)view.findViewById(R.id.customerEmail);

        }

    }

    public interface OnItemClickListener {

        public void onItemClick(View view, int position);
    }
}
