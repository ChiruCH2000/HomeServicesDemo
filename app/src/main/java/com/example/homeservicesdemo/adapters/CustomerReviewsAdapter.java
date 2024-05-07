package com.example.homeservicesdemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homeservicesdemo.R;
import com.example.homeservicesdemo.models.CustomerReviewsBean;

import java.util.ArrayList;

import javax.xml.namespace.QName;

public class CustomerReviewsAdapter extends RecyclerView.Adapter<CustomerReviewsAdapter.ViewHolder> {
    private Context mcontext;
    private ArrayList<CustomerReviewsBean> mcustomerReviewsBeansList;
    public CustomerReviewsAdapter(Context context, ArrayList<CustomerReviewsBean> mcustomerReviewsBeansList) {
        this.mcustomerReviewsBeansList = mcustomerReviewsBeansList;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public CustomerReviewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  view = LayoutInflater.from(mcontext).inflate(R.layout.item_customer_review,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerReviewsAdapter.ViewHolder holder, int position) {
        CustomerReviewsBean customerReviewsBean = mcustomerReviewsBeansList.get(position);
        holder.mtxtanme.setText(customerReviewsBean.getName());
        holder.mtxtdescription.setText(customerReviewsBean.getDescription());
        holder.mtxtrating.setText(customerReviewsBean.getRating());
        holder.mtxtdate.setText(customerReviewsBean.getDate());
    }

    @Override
    public int getItemCount() {
        return mcustomerReviewsBeansList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mtxtanme,mtxtdescription,mtxtrating,mtxtdate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mtxtanme = itemView.findViewById(R.id.item_customer_review_textView_reviewerName);
            mtxtdescription =itemView.findViewById(R.id.item_customer_review_textView_reviewMessage);
            mtxtrating = itemView.findViewById(R.id.item_customer_review_textview_rating);
            mtxtdate = itemView.findViewById(R.id.item_customer_review_textView_date);
        }
    }
}
