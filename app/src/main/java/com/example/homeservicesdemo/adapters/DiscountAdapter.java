package com.example.homeservicesdemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homeservicesdemo.R;
import com.example.homeservicesdemo.models.DiscountBean;

import java.util.ArrayList;

public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.DiscountViewHolder> {

    private Context mContext;
    private ArrayList<DiscountBean> mDiscountItems;

    public DiscountAdapter(Context context, ArrayList<DiscountBean> discountItems) {
        mContext = context;
        mDiscountItems = discountItems;
    }

    @NonNull
    @Override
    public DiscountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.discount_items, parent, false);
        return new DiscountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscountViewHolder holder, int position) {
        DiscountBean discountItem = mDiscountItems.get(position);
        holder.textViewText1.setText(discountItem.getText1());
        holder.textViewText2.setText(discountItem.getText2());
    }

    @Override
    public int getItemCount() {
        return mDiscountItems.size();
    }

    public static class DiscountViewHolder extends RecyclerView.ViewHolder {
        TextView textViewText1,textViewText2;

        public DiscountViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewText1 = itemView.findViewById(R.id.discount_items_textView_text1);
            textViewText2 = itemView.findViewById(R.id.discount_items_textView_text2);
        }
    }
}

