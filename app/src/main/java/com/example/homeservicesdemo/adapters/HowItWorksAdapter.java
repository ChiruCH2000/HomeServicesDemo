package com.example.homeservicesdemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homeservicesdemo.R;
import com.example.homeservicesdemo.models.HowItWork;

import java.util.ArrayList;


public class HowItWorksAdapter extends RecyclerView.Adapter<HowItWorksAdapter.ViewHolder> {
    private Context mcontext;
    private ArrayList<HowItWork> howItWorkslist;
    public HowItWorksAdapter(Context mcontext, ArrayList<HowItWork> howItWorkslist) {
        this.mcontext = mcontext;
        this.howItWorkslist=howItWorkslist;
    }

    @NonNull
    @Override
    public HowItWorksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_howitworks,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HowItWorksAdapter.ViewHolder holder, int position) {

        HowItWork howItWork=howItWorkslist.get(position);
        int count = position + 1;

        holder.mtxtCount.setText(String.valueOf(count));
        holder.mtxtName.setText(howItWork.getTitle());
        holder.mtxtDescription.setText(howItWork.getDescription());
    }

    @Override
    public int getItemCount() {
        return howItWorkslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mtxtCount,mtxtName,mtxtDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mtxtCount = itemView.findViewById(R.id.item_howitworks_txtView_count);
            mtxtName = itemView.findViewById(R.id.item_howitworks_txtView_name);
            mtxtDescription = itemView.findViewById(R.id.item_howitworks_txtView_description);
        }
    }
}
