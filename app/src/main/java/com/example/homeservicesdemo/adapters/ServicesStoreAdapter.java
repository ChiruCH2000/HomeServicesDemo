package com.example.homeservicesdemo.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homeservicesdemo.R;
import com.example.homeservicesdemo.models.ServicesBean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ServicesStoreAdapter extends RecyclerView.Adapter<ServicesStoreAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<ServicesBean> mServiceItems;
    public ServicesStoreAdapter(Context context, ArrayList<ServicesBean> serviceItems) {
        mContext = context;
        mServiceItems = serviceItems;
    }

    @NonNull
    @Override
    public ServicesStoreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_store_services, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesStoreAdapter.ViewHolder holder, int position) {
        ServicesBean serviceItem = mServiceItems.get(position);

        // Bind data to views
        holder.textViewServiceName.setText(serviceItem.getName());
        holder.textViewPrice.setText("₹"+serviceItem.getPrice());
        Picasso.get().load(serviceItem.getImage()).fit().centerCrop().into(holder.imageView);

        String note1 = serviceItem.getNote1();
        if (!TextUtils.isEmpty(note1)) {
            holder.textViewNote1.setText("\u2022 " + note1);
        }
        String note2 = serviceItem.getNote2();
        if (!TextUtils.isEmpty(note2)) {
            holder.textViewNote2.setText("\u2022 " + note2);
        }
    }


    @Override
    public int getItemCount() {
        return mServiceItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewServiceName,textViewPrice,textViewNote1,textViewNote2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_store_search_services_imgView_img);
            textViewServiceName = itemView.findViewById(R.id.item_store_services_textview_serviceName);
            textViewPrice = itemView.findViewById(R.id.item_store_services_textview_price);
            textViewNote1 = itemView.findViewById(R.id.item_store_services_textview_note1);
            textViewNote2 = itemView.findViewById(R.id.item_store_services_textview_note2);
        }
    }
}

