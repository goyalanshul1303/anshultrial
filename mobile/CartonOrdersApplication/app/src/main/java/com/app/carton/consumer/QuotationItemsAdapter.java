package com.app.carton.consumer;

import android.content.Context;
import android.graphics.drawable.Drawable;
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

    public void setFromOpenOrders(boolean fromOpenOrders) {
        isFromOpenOrders = fromOpenOrders;
    }

    private boolean isFromOpenOrders;

    public QuotationItemsAdapter(Context mContext, ArrayList<QuotationData> data){
        context = mContext;
        this.data = data;

    }
    @Override
    public QuotationItemsAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.added_quotation_details, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        QuotationItemsAdapter.CustomViewHolder viewHolder = new QuotationItemsAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(QuotationItemsAdapter.CustomViewHolder customViewHolder, int i) {
        QuotationData testObjtem = data.get(i);
        customViewHolder.quotationAmount.setText("\u20B9 " +String.valueOf(testObjtem.quoteAmount));
       customViewHolder.quotationStartDate.setText(Utils.getDate(testObjtem.orderStartDate));
        customViewHolder.quotationEndDate.setText(Utils.getDate(testObjtem.orderFulfillmentDate));
        customViewHolder.quotationPlacedDate.setText(Utils.getDate(testObjtem.quoteDate));
        customViewHolder.awardQuotationBtn.setTag(i);

        customViewHolder.awardQuotationBtn.setOnClickListener(this);
        if (isFromOpenOrders){
            customViewHolder.awardQuotationBtn.setVisibility(View.GONE);
        }else{
            customViewHolder.awardQuotationBtn.setVisibility(View.VISIBLE);

        }
        if (testObjtem.isAwarded()){

            Drawable img = context.getResources().getDrawable( R.drawable.quotations_awarded );
//            customViewHolder.awardQuotationBtn.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
            customViewHolder.awardQuotationBtn.setBackground(img);
//            customViewHolder.awardQuotationBtn.setText("Awarded Quotation");
            customViewHolder.awardQuotationBtn.setEnabled(false);
        }else{
            Drawable img = context.getResources().getDrawable( R.drawable.quotations_award );
//            customViewHolder.awardQuotationBtn.setText("Award Quotation");
            customViewHolder.awardQuotationBtn.setBackground(img);
//            Drawable img = context.getResources().getDrawable( R.drawable.already_awarded );
//            customViewHolder.awardQuotationBtn.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
            customViewHolder.awardQuotationBtn.setEnabled(true);
        }

    }

    @Override
    public int getItemCount() {
        return (null != data ? data.size() : 0);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView quotationPlacedDate, quotationEndDate,quotationStartDate, quotationAmount,pricePerUnit;

        Button awardQuotationBtn;


        public CustomViewHolder(View view) {
            super(view);
            quotationPlacedDate = (TextView)view.findViewById(R.id.quotationPlacedDate);
            quotationStartDate = (TextView)view.findViewById(R.id.quotationStartDate);
            quotationEndDate = (TextView)view.findViewById(R.id.quotationEndDate);
            quotationAmount = (TextView)view.findViewById(R.id.quotationAmount);
            awardQuotationBtn = (Button) view.findViewById(R.id.awardQuotationBtn);
            pricePerUnit =(TextView)view.findViewById(R.id.priceperUnit);

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
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(view, (Integer) view.getTag());
            }
//            testObjtem.setAwarded(true);
//            notifyDataSetChanged();
        }
    }
}
