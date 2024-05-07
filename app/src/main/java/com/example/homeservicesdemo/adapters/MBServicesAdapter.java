package com.example.homeservicesdemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homeservicesdemo.R;
import com.example.homeservicesdemo.models.MostBookedService;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MBServicesAdapter extends RecyclerView.Adapter<MBServicesAdapter.ViewHolder> {
    public List<MostBookedService> mostBookedServiceList;
    public Context context;
    public MBServicesAdapter(Context context, List<MostBookedService> mostBookedServiceList) {
        this.mostBookedServiceList =mostBookedServiceList;
        this.context=context;
    }

    @NonNull
    @Override
    public MBServicesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mbservices,parent,false);
        return new MBServicesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MBServicesAdapter.ViewHolder holder, int position) {
        MostBookedService mostBookedService = mostBookedServiceList.get(position);

        holder.mtxtViewTitle.setText(mostBookedService.getTitle());
        Picasso.get().load(mostBookedService.getImage()).into(holder.mImageThumb);
        holder.mtxtViewPrice.setText(mostBookedService.getMrp());
    }

    @Override
    public int getItemCount() {
        return mostBookedServiceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageThumb;
        TextView mtxtViewTitle,mtxtViewPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mtxtViewTitle = itemView.findViewById(R.id.item_mbservices_item_textView_title);
            mImageThumb = itemView.findViewById(R.id.item_mbservices_item_imageView);
            mtxtViewPrice = itemView.findViewById(R.id.item_mbservices_item_textView_mrp);
        }
    }
}

