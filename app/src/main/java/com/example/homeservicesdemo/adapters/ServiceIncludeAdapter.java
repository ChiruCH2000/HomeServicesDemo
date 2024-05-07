package com.example.homeservicesdemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homeservicesdemo.R;
import com.example.homeservicesdemo.models.ServiceIncluded;

import java.util.ArrayList;

public class ServiceIncludeAdapter extends RecyclerView.Adapter<ServiceIncludeAdapter.VieHolder> {
    private Context mContext;
    private ArrayList<ServiceIncluded> mServiceIncludelist;
    public ServiceIncludeAdapter(Context mContext, ArrayList<ServiceIncluded> mServiceIncludelist) {
        this.mServiceIncludelist =mServiceIncludelist;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ServiceIncludeAdapter.VieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_include,parent,false);
        return new VieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceIncludeAdapter.VieHolder holder, int position) {
        ServiceIncluded serviceIncluded = mServiceIncludelist.get(position);
        holder.mtxtView.setText(serviceIncluded.getText());
    }

    @Override
    public int getItemCount() {
        return mServiceIncludelist.size();
    }

    public class VieHolder extends RecyclerView.ViewHolder {
        private TextView mtxtView;
        public VieHolder(@NonNull View itemView) {
            super(itemView);
            mtxtView = itemView.findViewById(R.id.item_include_textView);
        }
    }
}
