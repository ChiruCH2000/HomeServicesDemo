package com.example.homeservicesdemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homeservicesdemo.R;
import com.example.homeservicesdemo.activites.OrderSummery;
import com.example.homeservicesdemo.helperclass.DataBaseHelper;
import com.example.homeservicesdemo.helperclass.SelectedServiceDetails;

import java.util.List;

public class SelectedServiceAdapter extends RecyclerView.Adapter<SelectedServiceAdapter.ViewHolder> {
    private Context mcontext;
    private  List<SelectedServiceDetails> mSelectedServicesList;
    private DataBaseHelper mDatabaseHelper;
    private AdapterCallback adapterCallback;

    public SelectedServiceAdapter(Context mcontext, List<SelectedServiceDetails> mSelectedServicesList, OrderSummery adapterCallback) {
        this.mcontext=mcontext;
        this.mSelectedServicesList=mSelectedServicesList;
        this.mDatabaseHelper = new DataBaseHelper(mcontext);
        this.adapterCallback = adapterCallback;
    }
    public interface AdapterCallback {
        void onAdapterItemChanged();
    }

    @NonNull
    @Override
    public SelectedServiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.item_order_summery,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedServiceAdapter.ViewHolder holder, int position) {

        SelectedServiceDetails selectedServiceDetails=mSelectedServicesList.get(position);

        holder.mtextViewtitle.setText(selectedServiceDetails.getServiceName());

        // Calculate total price based on quantity and price of the service
        double totalPrice = selectedServiceDetails.getPrice() * selectedServiceDetails.getQuantity();

        // Convert total price to string before setting it to the TextView
        holder.mtextViewPrice.setText(String.valueOf(totalPrice));
        /*mDatabaseHelper.updateCategoryTotalPrice(String.valueOf(selectedServiceDetails.getCategoryId()));*/
        holder.mtextViewCount.setText(String.valueOf(selectedServiceDetails.getQuantity()));

        holder.mtextViewDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQuantity = selectedServiceDetails.getQuantity() - 1;
                if (newQuantity > 0) {
                    selectedServiceDetails.setQuantity(newQuantity);
                    holder.mtextViewCount.setText(String.valueOf(newQuantity));

                    // Recalculate total price after decrementing quantity
                    double newTotalPrice = selectedServiceDetails.getPrice() * newQuantity;
                    holder.mtextViewPrice.setText(String.valueOf(newTotalPrice));

                    updateQuantityInDatabase(selectedServiceDetails, newQuantity);
                    adapterCallback.onAdapterItemChanged();
                }
                else mDatabaseHelper.removeServiceFromCart(String.valueOf(selectedServiceDetails.getServiceId()));
            }
        });
        holder.mtextViewIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQuantity = selectedServiceDetails.getQuantity() + 1;
                selectedServiceDetails.setQuantity(newQuantity);
                holder.mtextViewCount.setText(String.valueOf(newQuantity));

                // Recalculate total price after incrementing quantity
                double newTotalPrice = selectedServiceDetails.getPrice() * newQuantity;
                holder.mtextViewPrice.setText(String.valueOf(newTotalPrice));

                updateQuantityInDatabase(selectedServiceDetails, newQuantity);
                adapterCallback.onAdapterItemChanged();
            }
        });
    }
    private void updateQuantityInDatabase(SelectedServiceDetails selectedServiceDetails, int newQuantity) {
        mDatabaseHelper.updateQuantity(selectedServiceDetails.getServiceId(), newQuantity);
    }
    @Override
    public int getItemCount() {
        if (mSelectedServicesList != null) {
            return mSelectedServicesList.size();
        } else {
            return 0; // or any default value you want to return if the list is null
        }
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mtextViewtitle,mtextViewDecrement,mtextViewIncrement,mtextViewCount,mtextViewPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mtextViewtitle=itemView.findViewById(R.id.item_order_summery_textView_item_name);
            mtextViewDecrement= itemView.findViewById(R.id.item_order_summery_textView_minus);
            mtextViewIncrement=itemView.findViewById(R.id.item_order_summery_textView_plus);
            mtextViewCount=itemView.findViewById(R.id.item_order_summery_textView_count);
            mtextViewPrice=itemView.findViewById(R.id.item_order_summery_textView_price);

        }
    }
}
