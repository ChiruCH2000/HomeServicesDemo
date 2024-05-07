package com.example.homeservicesdemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homeservicesdemo.R;
import com.example.homeservicesdemo.models.RateCardBean;


import java.util.ArrayList;

public class RateCardAdapter extends RecyclerView.Adapter<RateCardAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<RateCardBean> mRateCardBean;
    public RateCardAdapter(Context context, ArrayList<RateCardBean> rateCardBeanslist) {
        this.mContext=context;
        this.mRateCardBean= rateCardBeanslist;
    }

    @NonNull
    @Override
    public RateCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_ratecard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RateCardAdapter.ViewHolder holder, int position) {

        RateCardBean rateCardBean =mRateCardBean.get(position);

        holder.mtxtDescription.setText(rateCardBean.getDescription());
        holder.mtxtPrice.setText("₹"+rateCardBean.getPrice());
    }


    @Override
    public int getItemCount() {
        return mRateCardBean.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mtxtDescription,mtxtPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mtxtDescription= itemView.findViewById(R.id.item_ratecard_description);
            mtxtPrice = itemView.findViewById(R.id.item_ratecard_price);
        }
    }
}
