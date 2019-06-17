package com.app.carton.consumer;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.carton.orders.R;

import java.util.ArrayList;
import java.util.List;

public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ViewHolder> implements View.OnClickListener {
    private List<String> labels;
    private OnItemClickListener mItemClickListener;
    private int openOrderCount;
    private int requiremntsCount;
    private int inprogressCount;

    public ActionAdapter(int count) {
        labels = new ArrayList<>(count);
        for (int i = 0; i < count; ++i) {
            labels.add(String.valueOf(i));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.action_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        final String label = labels.get(position);


        if (position == 0){
            holder.textView.setText("My Products  ");
            holder.dashboardImv.setImageResource(R.drawable.waiting);
            holder.badgeRL.setVisibility(View.GONE);
        }else if (position == 1){
            holder.textView.setText("Requirements");
            holder.dashboardImv.setImageResource(R.drawable.progress);
            holder.badgeRL.setVisibility(View.VISIBLE);
            holder.badgeTextView.setText(""+requiremntsCount);
        }else if (position == 2){
            holder.textView.setText("Open Orders");
            holder.badgeRL.setVisibility(View.VISIBLE);
            holder.badgeTextView.setText(""+openOrderCount);
            holder.dashboardImv.setImageResource(R.drawable.pending);
        }else if (position == 3){
            holder.textView.setText("Completed Orders");
            holder.dashboardImv.setImageResource(R.drawable.completed);
            holder.badgeRL.setVisibility(View.GONE);
        }

holder.mainItem.setTag(position);
        holder.mainItem.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return labels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView ,badgeTextView;
        RelativeLayout badgeRL;
        ImageView dashboardImv;
        CardView mainItem;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.progressTextView);
            mainItem = (CardView)itemView.findViewById(R.id.mainItem);
            badgeRL = (RelativeLayout) itemView.findViewById(R.id.badgeRL);
            badgeTextView = (TextView) itemView.findViewById(R.id.badgeTextView);
            dashboardImv = (ImageView) itemView.findViewById(R.id.dashboardImv);
        }
    }
    public interface OnItemClickListener {

        public void onItemClick(View view, int position);
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
    public void setCardCount(int requiremntsCount, int openOrderCount) {
        this.openOrderCount = openOrderCount;
        this.requiremntsCount = requiremntsCount;


    }
}
