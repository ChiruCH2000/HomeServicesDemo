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
import com.example.homeservicesdemo.activites.MyOrderDetailsActivity;
import com.example.homeservicesdemo.models.Orderbean;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    private Context context;
    private List<Orderbean> ordersList;
    public OrdersAdapter(Context context, List<Orderbean> ordersList) {
        this.context = context;
        this.ordersList = ordersList;
    }

    @NonNull
    @Override
    public OrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrdersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.ViewHolder holder, int position) {

        Orderbean orderbean =ordersList.get(position);

        holder.mtxtViewOrderId.setText(orderbean.getOrder_id());
        holder.mtxtViewName.setText(orderbean.getCategory_name());
        holder.mtxtViewDate.setText(orderbean.getOrder_date());
        holder.mtxtViewTime.setText(orderbean.getOrder_time());
        holder.mtxtViewPrice.setText(orderbean.getPay_amount());
        holder.mtxtViewStatus.setText(orderbean.getStatus());

        holder.mllayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MyOrderDetailsActivity.class);
                intent.putExtra("id",orderbean.getOid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mllayout;
        TextView mtxtViewName,mtxtViewPrice,mtxtViewDate,mtxtViewTime,mtxtViewStatus,mtxtViewOrderId;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mtxtViewOrderId = itemView.findViewById(R.id.item_order_txtView_id);
            mtxtViewName = itemView.findViewById(R.id.item_order_txtView_title);
            mtxtViewDate = itemView.findViewById(R.id.item_order_txtView_date);
            mtxtViewTime = itemView.findViewById(R.id.item_order_txtView_time);
            mtxtViewPrice = itemView.findViewById(R.id.item_order_txtView_price);
            mtxtViewStatus =itemView.findViewById(R.id.item_order_txtView_status);
            mllayout = itemView.findViewById(R.id.item_order_llayout);
        }
    }
}
