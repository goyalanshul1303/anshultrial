package com.app.carton.consumer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.carton.orders.R;

import java.util.ArrayList;

/**
 * Created by aggarwal.swati on 3/29/19.
 */

public class QuotationItemsAdapter extends RecyclerView.Adapter<QuotationItemsAdapter.CustomViewHolder>
        implements View.OnClickListener {
    QuotationItemsAdapter.OnItemClickListener mItemClickListener;

    private ArrayList<QuotationData> data = new ArrayList();
    Context context;

    public QuotationItemsAdapter(Context mContext, ArrayList<QuotationData> data){
        context = mContext;
        this.data = data;

    }
    @Override
    public QuotationItemsAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.added_quotation_details, null);

        QuotationItemsAdapter.CustomViewHolder viewHolder = new QuotationItemsAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(QuotationItemsAdapter.CustomViewHolder customViewHolder, int i) {
        QuotationData testObjtem = data.get(i);
        Utils.setDetailsTextField("Quotation Amount", context, customViewHolder.quotationAmount,String.valueOf(testObjtem.quoteAmount));
        Utils.setDetailsTextField("Quotation Start Date", context, customViewHolder.quotationStartDate, Utils.getDate(testObjtem.orderStartDate));
        Utils.setDetailsTextField("Quotation End Date", context, customViewHolder.quotationEndDate, Utils.getDate(testObjtem.orderFulfillmentDate));
        Utils.setDetailsTextField("Quotation Placed Date", context, customViewHolder.quotationPlacedDate, Utils.getDate(testObjtem.orderPlacedDate));
        customViewHolder.awardQuotationBtn.setTag(i);
        customViewHolder.awardQuotationBtn.setOnClickListener(this);
        if (testObjtem.isAwarded()){
            customViewHolder.awardQuotationBtn.setText("Awarded Quotation");
            customViewHolder.awardQuotationBtn.setEnabled(false);
        }else{
            customViewHolder.awardQuotationBtn.setText("Award Quotation");
            customViewHolder.awardQuotationBtn.setEnabled(true);
        }

    }

    @Override
    public int getItemCount() {
        return (null != data ? data.size() : 0);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView quotationPlacedDate, quotationEndDate,quotationStartDate, quotationAmount;

        Button awardQuotationBtn;


        public CustomViewHolder(View view) {
            super(view);
            quotationPlacedDate = (TextView)view.findViewById(R.id.quotationPlacedDate);
            quotationStartDate = (TextView)view.findViewById(R.id.quotationStartDate);
            quotationEndDate = (TextView)view.findViewById(R.id.quotationEndDate);
            quotationAmount = (TextView)view.findViewById(R.id.quotationAmount);
            awardQuotationBtn = (Button) view.findViewById(R.id.awardQuotationBtn);

        }

    }

    public interface OnItemClickListener {

        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final QuotationItemsAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.awardQuotationBtn){
            QuotationData testObjtem = data.get((Integer) view.getTag());
            //
            testObjtem.setAwarded(true);
            notifyDataSetChanged();
        }
    }
}
