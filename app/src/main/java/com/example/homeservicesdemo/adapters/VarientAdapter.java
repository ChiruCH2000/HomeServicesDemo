package com.example.homeservicesdemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homeservicesdemo.R;
import com.example.homeservicesdemo.helperclass.DataBaseHelper;
import com.example.homeservicesdemo.models.VarientBean;

import java.util.ArrayList;

public class VarientAdapter extends RecyclerView.Adapter<VarientAdapter.ViewHolder> {
    private Context mcontext;
    private ArrayList<VarientBean> mVarient;
    private DataBaseHelper mDatabaseHelper;
    private ServicesAdapter.AdapterCallback adapterCallback;
    public VarientAdapter(Context mContext, ArrayList<VarientBean> mVarient, DataBaseHelper databaseHelper, Context adapterCallback) {
        this.mVarient =mVarient;
        this.mcontext =mContext;
        this.mDatabaseHelper=databaseHelper;
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
        holder.mtxtViewPrice.setText(varientBean.getVariant_price());
        holder.mtxtViewRating.setText(varientBean.getVariant_rating());
        holder.mtxtViewNote1.setText(varientBean.getVarient_note1());
        holder.mtxtViewNote2.setText(varientBean.getVarient_note2());

        boolean isInCart = mDatabaseHelper.isVarientInCart(varientBean.getVariant_id());

        if (isInCart) {
            // If the service is already in the cart, show countLL and hide txtAdd
            holder.mllcount.setVisibility(View.VISIBLE);
            holder.mtxtAdd.setVisibility(View.GONE);
            int quantity = mDatabaseHelper.getServiceQuantity(serviceItem.getService_id());
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

            }
        });
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
