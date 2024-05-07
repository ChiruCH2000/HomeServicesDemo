package com.example.homeservicesdemo.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homeservicesdemo.R;
import com.example.homeservicesdemo.activites.OrderSummery;
import com.example.homeservicesdemo.activites.ServicesActivity;
import com.example.homeservicesdemo.helperclass.CartItems;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private final Context mcontext;
    private List<CartItems> cartItemsList;

    public CartAdapter(Context mcontext, List<CartItems> cartItemsList) {
        this.mcontext = mcontext;
        this.cartItemsList=cartItemsList;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_cart,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {

        CartItems cartItems =cartItemsList.get(position);
        holder.mtxtTitle.setText(cartItems.getCategoryName());
        holder.mtxtprice.setText("\u2022 " +"₹"+cartItems.getTotalPrice());

        // Set up ListView with the list of service names
        /*ArrayAdapter<String> adapter = new ArrayAdapter<>(mcontext, R.layout.item_cart_list, cartItems.getServiceNames());*/
        MyExpandableListAdapter adapter = new MyExpandableListAdapter(mcontext, cartItems.getServiceNames());

        System.out.println("cart items Names :"+cartItems.getServiceNames());
        holder.mExpendableListView.setAdapter(adapter);

        // Display the count of services
        int serviceCount = cartItems.getServiceNames().size();
        holder.mtxtServicesCount.setText(String.valueOf(serviceCount));

        holder.mtxtAddServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, ServicesActivity.class);
                intent.putExtra("cat_id",cartItems.getCategoryId());
                mcontext.startActivity(intent);
            }
        });
        holder.mtxtCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, OrderSummery.class);
                intent.putExtra("cat_id",cartItems.getCategoryId());
                intent.putExtra("cat_name",cartItems.getCategoryName());
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItemsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mtxtTitle,mtxtprice,mtxtServicesCount,mtxtAddServices,mtxtCheckout;
        ExpandableListView mExpendableListView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mtxtTitle = itemView.findViewById(R.id.item_cart_textView_name);
            mtxtprice = itemView.findViewById(R.id.item_cart_textView_servicescosts);
            mtxtServicesCount = itemView.findViewById(R.id.item_cart_textView_servicescount);
            mtxtAddServices = itemView.findViewById(R.id.item_cart_textView_addServices);
            mtxtCheckout = itemView.findViewById(R.id.item_cart_textView_checkOut);
            mExpendableListView = itemView.findViewById(R.id.item_cart_listView_services);
        }
    }
}
