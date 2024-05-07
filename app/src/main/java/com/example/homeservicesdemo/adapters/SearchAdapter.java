package com.example.homeservicesdemo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homeservicesdemo.R;
import com.example.homeservicesdemo.models.SearchedSearvicesItems;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private List<SearchedSearvicesItems> searchItemList;

    public SearchAdapter(List<SearchedSearvicesItems> searchItemList) {
        this.searchItemList = searchItemList;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_service, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        SearchedSearvicesItems searchItem = searchItemList.get(position);

        // Bind data to views in the item layout
        holder.mtxtViewName.setText(searchItem.getService_name());
        Picasso.get().load(searchItem.getImage()).fit()
                .into(holder.mImg);

    }

    @Override
    public int getItemCount() {
        return searchItemList.size();
    }

    static class SearchViewHolder extends RecyclerView.ViewHolder {

        private TextView mtxtViewName;
        private ImageView mImg;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            mtxtViewName = itemView.findViewById(R.id.item_search_service_textView_name);
            mImg = itemView.findViewById(R.id.item_search_service_img);
        }
    }
}


