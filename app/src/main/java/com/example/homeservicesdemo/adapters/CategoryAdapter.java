package com.example.homeservicesdemo.adapters;

import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homeservicesdemo.R;
import com.example.homeservicesdemo.activites.ServicesActivity;
import com.example.homeservicesdemo.models.CategoryBean;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    public List<CategoryBean> categoryBeanList;
    public Context context;
    public CategoryAdapter(Context context, List<CategoryBean> categoryBeanList) {
        this.categoryBeanList =categoryBeanList;
        this.context=context;
    }
    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_item,parent,false);
        return new CategoryAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        CategoryBean categoryBean = categoryBeanList.get(position);

        holder.mtxtViewTitle.setText(categoryBean.getCategory_name());
        Picasso.get().load(categoryBean.getImage()).into(holder.mImageThumb);
        holder.mllayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mcatId = categoryBean.getId();
                String mcatName = categoryBean.getCategory_name();
                Intent intent = new Intent(context, ServicesActivity.class);
                intent.putExtra("cat_id",mcatId);
                intent.putExtra("cat_name",mcatName);
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return categoryBeanList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageThumb;
        TextView mtxtViewTitle;
        LinearLayout mllayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mtxtViewTitle = itemView.findViewById(R.id.item_category_item_textView_title);
            mImageThumb = itemView.findViewById(R.id.item_category_item_imageView);
            mllayout = itemView.findViewById(R.id.item_category_llayout);
        }
    }
}
