package com.example.homeservicesdemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homeservicesdemo.R;
import com.example.homeservicesdemo.models.SubCategoryBean;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryItemAdapter extends RecyclerView.Adapter<CategoryItemAdapter.CategoryViewHolder> {

    private Context mContext;
    private List<SubCategoryBean> msubCategoryItems;
    private ApiCallback mApiCallback;

    public CategoryItemAdapter(Context context, List<SubCategoryBean> subcategoryItems,ApiCallback apiCallback) {
        mContext = context;
        msubCategoryItems = subcategoryItems;
        mApiCallback = apiCallback;
    }


    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_category_list, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        SubCategoryBean subCategoryBean = msubCategoryItems.get(position);

        // Bind data to views
        Picasso.get().load(subCategoryBean.getImage()).fit().into(holder.imageView);
        holder.textViewTitle.setText(subCategoryBean.getSubcategory_name());

        holder.linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mApiCallback.onSubCategorySelected(subCategoryBean.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return msubCategoryItems.size();
    }
    public interface ApiCallback {
        void onSubCategorySelected(String subCatId);
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewTitle;
        LinearLayout linearlayout;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_category_list_imgView_img);
            textViewTitle = itemView.findViewById(R.id.item_category_list_textView_title);
            linearlayout = itemView.findViewById(R.id.item_category_list_linearLayout);
        }
    }
}
