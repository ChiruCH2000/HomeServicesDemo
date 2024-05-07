package com.example.homeservicesdemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homeservicesdemo.R;
import com.example.homeservicesdemo.models.ServiceExcluded;

import java.util.ArrayList;

public class ServiceExcludeAdapter extends RecyclerView.Adapter<ServiceExcludeAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<ServiceExcluded> mServiceExcludelist;
    public ServiceExcludeAdapter(Context mContext, ArrayList<ServiceExcluded> mServiceExcludelist) {
        this.mServiceExcludelist=mServiceExcludelist;
        this.mContext=mContext;
    }

    @NonNull
    @Override
    public ServiceExcludeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_exclude,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceExcludeAdapter.ViewHolder holder, int position) {
        ServiceExcluded serviceExcluded = mServiceExcludelist.get(position);
        holder.mtxtView.setText(serviceExcluded.getText());
    }

    @Override
    public int getItemCount() {
        return mServiceExcludelist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mtxtView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mtxtView=itemView.findViewById(R.id.item_exclude_textView);
        }
    }
}
