package com.example.homeservicesdemo.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homeservicesdemo.R;
import com.example.homeservicesdemo.helperclass.DataBaseHelper;
import com.example.homeservicesdemo.models.ServicesBean;
import com.example.homeservicesdemo.models.VarientBean;

import java.util.ArrayList;

public class VarientAdapter extends RecyclerView.Adapter<VarientAdapter.ViewHolder> {
    private Context mcontext;
    private ArrayList<VarientBean> mVarient;
    private DataBaseHelper mDatabaseHelper;
    private ServicesAdapter.AdapterCallback adapterCallback;
    private ServicesBean serviceBean;
    public VarientAdapter(Context mContext, ArrayList<VarientBean> mVarient, ServicesBean serviceBean, DataBaseHelper databaseHelper, Context adapterCallback) {
        this.mVarient =mVarient;
        this.mcontext =mContext;
        this.mDatabaseHelper=databaseHelper;
        this.serviceBean = serviceBean;
        this.adapterCallback = (ServicesAdapter.AdapterCallback) adapterCallback;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VarientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_varient,parent,false);
        return new VarientAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VarientAdapter.ViewHolder holder, int position) {
        VarientBean varientBean = mVarient.get(position);

        holder.mtxtViewTitle.setText(varientBean.getVariant_name());
        holder.mtxtViewPrice.setText("₹"+varientBean.getVariant_price());
        holder.mtxtViewRating.setText(varientBean.getVariant_rating());
        String note1 = varientBean.getVarient_note1();
        if (!TextUtils.isEmpty(note1)) {
            holder.mtxtViewNote1.setText("\u2022 " + note1);
        }
        String note2 = varientBean.getVarient_note2();
        if (!TextUtils.isEmpty(note2)) {
            holder.mtxtViewNote2.setText("\u2022 " + note2);
        }

        boolean isInCart = mDatabaseHelper.isVarientInCart(serviceBean.getService_id());
        String varientId = varientBean.getVariant_id();
        if (isInCart) {
            // If the service is already in the cart, show countLL and hide txtAdd
            holder.mllcount.setVisibility(View.VISIBLE);
            holder.mtxtAdd.setVisibility(View.GONE);
            int quantity = mDatabaseHelper.getVarientQuantity(varientId);
            holder.mtxtcount.setText(String.valueOf(quantity));
            adapterCallback.onAdapterItemChanged();
        } else {
            // If the service is not in the cart, hide countLL and show txtAdd
            holder.mllcount.setVisibility(View.GONE);
            holder.mtxtAdd.setVisibility(View.VISIBLE);
            adapterCallback.onAdapterItemChanged();
        }
        holder.mtxtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceName = serviceBean.getName();
                double servicePrice = Double.parseDouble(serviceBean.getPrice());
                String categoryId = serviceBean.getCategory_id();
                String categoryName = serviceBean.getCategory_name();
                String serviceId = serviceBean.getService_id();
                String subServiceId = serviceBean.getSubcat_id();
                String subServiceName = serviceBean.getSubcategory_name();
                String type = serviceBean.getType();
                String varientId = varientBean.getVariant_id();
                String varientName = varientBean.getVariant_name();
                Double varientPrice = Double.valueOf(varientBean.getVariant_price());

                // Fetch the quantity of the varient from the database
                int quantity = mDatabaseHelper.getVarientQuantity(varientId);

                if (quantity > 0) {
                    // If the varient is already in the cart, show llCount and hide txtViewAdd
                    holder.mllcount.setVisibility(View.VISIBLE);
                    holder.mtxtAdd.setVisibility(View.GONE);
                    holder.mtxtcount.setText(String.valueOf(quantity));
                } else {
                    // If the varient is not in the cart, add it to the cart and update UI
                    quantity = 1; // Default quantity
                    mDatabaseHelper.addToCartWithVariant(serviceId, categoryId, categoryName, serviceName, subServiceId, subServiceName, servicePrice, quantity, type,varientId,varientName,varientPrice);
                    // Hide llCount and show txtViewAdd
                    holder.mllcount.setVisibility(View.GONE);
                    holder.mtxtAdd.setVisibility(View.VISIBLE);
                    holder.mtxtcount.setText(String.valueOf(quantity));
                }
                adapterCallback.onAdapterItemChanged();
            }
        });
        holder.mtxtplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentCount = Integer.parseInt(holder.mtxtcount.getText().toString());
                currentCount++;
                holder.mtxtcount.setText(String.valueOf(currentCount));
                updateQuantityInDatabase(varientBean.getVariant_id(), currentCount);
                adapterCallback.onAdapterItemChanged();
            }
        });

        // Decrement button click listener
        holder.mtxtminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentCount = Integer.parseInt(holder.mtxtcount.getText().toString());
                if (currentCount > 0) {
                    currentCount--;
                    holder.mtxtcount.setText(String.valueOf(currentCount));
                    updateQuantityInDatabase(varientBean.getVariant_id(), currentCount);
                }
                // If the count is below 1, remove the service from the table
                if (currentCount < 1) {
                    mDatabaseHelper.removeServiceFromCart(varientBean.getVariant_id());
                    holder.mllcount.setVisibility(View.GONE);
                    holder.mtxtAdd.setVisibility(View.VISIBLE);
                }
                adapterCallback.onAdapterItemChanged();
            }
        });

    }
    private void updateQuantityInDatabase(String varientId, int newQuantity) {
        // Update quantity in the database
        mDatabaseHelper.updateQuantity(Integer.parseInt(varientId), newQuantity);
    }
    @Override
    public int getItemCount() {
        return mVarient.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView  mtxtViewTitle,mtxtViewPrice,mtxtViewRating,mtxtViewNote1,mtxtViewNote2,mtxtAdd,mtxtcount,mtxtplus,mtxtminus;
        public LinearLayout mllcount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mllcount = itemView.findViewById(R.id.item_varient_linerLayout_count);

            mtxtAdd = itemView.findViewById(R.id.item_varient_textView_add);
            mtxtViewTitle = itemView.findViewById(R.id.item_varient_textview_varientName);
            mtxtViewPrice = itemView.findViewById(R.id.item_varient_textview_price);
            mtxtViewRating = itemView.findViewById(R.id.item_varient_textview_rating);
            mtxtViewNote1 = itemView.findViewById(R.id.item_varient_textview_note1);
            mtxtViewNote2 = itemView.findViewById(R.id.item_varient_textview_note2);
            mtxtcount = itemView.findViewById(R.id.item_varient_textView_count);
            mtxtplus = itemView.findViewById(R.id.item_varient_textView_plus);
            mtxtminus = itemView.findViewById(R.id.item_varient_textView_minus);
        }
    }
}
