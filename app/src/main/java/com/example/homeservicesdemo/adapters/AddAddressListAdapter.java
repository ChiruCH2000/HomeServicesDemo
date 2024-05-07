package com.example.homeservicesdemo.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.example.homeservicesdemo.R;
import com.example.homeservicesdemo.activites.ManageAddressActivity;
import com.example.homeservicesdemo.models.AddressesListBean;
import com.example.homeservicesdemo.utils.ApiRequest;
import com.example.homeservicesdemo.utils.CheckNetworkConnection;
import com.example.homeservicesdemo.utils.Singleton;
import com.example.homeservicesdemo.utils.VolleyResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
public class AddAddressListAdapter extends RecyclerView.Adapter<AddAddressListAdapter.ViewHolder> {
    private Context context;
    private ApiRequest mApiRequest;
    private CheckNetworkConnection mCheckNetworkConnection;
    private List<AddressesListBean> addressesList;
    private String userId;
    private SharedPreferences sharedPreferences;
    private boolean isOpenedByOrderSummary; // Add this field

    public interface OnSetAddressClickListener {
        void onSetAddressClicked(AddressesListBean addressesListBean);
    }

    private OnSetAddressClickListener mListener; // Listener reference

    public AddAddressListAdapter(Context context, List<AddressesListBean> addressesList, boolean isOpenedByOrderSummary, OnSetAddressClickListener listener) {
        this.context = context;
        this.addressesList = addressesList;
        this.isOpenedByOrderSummary = isOpenedByOrderSummary;
        mCheckNetworkConnection = new CheckNetworkConnection();
        mApiRequest = new ApiRequest(context);
        sharedPreferences = context.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id", "");
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AddressesListBean addressesListBean = addressesList.get(position);

        holder.mtxtLabel.setText(addressesListBean.getLabel());
        holder.mtxtLandMark.setText(addressesListBean.getLandmark());
        holder.mtxtAddress.setText(addressesListBean.getAddress());
        holder.mtxtHouseNo.setText(addressesListBean.getHouse());

        String addressId = addressesListBean.getAddress_id();

        holder.mimgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(addressId);
            }
        });

        // Check if the button should be visible
        if (isOpenedByOrderSummary) {
            holder.mbtnSettheaddress.setVisibility(View.VISIBLE);
            // Set click listener for the button
            holder.mbtnSettheaddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Notify the listener about button click
                    if (mListener != null) {
                        mListener.onSetAddressClicked(addressesListBean);
                    }
                }
            });
        } else {
            holder.mbtnSettheaddress.setVisibility(View.GONE);
        }
    }

    private void showDeleteConfirmationDialog(String addressId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to delete?");

        // Add "Yes" button
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mCheckNetworkConnection.isNetworkAvailable(context)) {
                    ((ManageAddressActivity)context).CallApiDelete(addressId);
                } else {
                    Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Add "No" button
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog, do nothing
                dialog.dismiss();
            }
        });

        // Show the dialog
        builder.show();
    }



    @Override
    public int getItemCount() {
        return addressesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mtxtLabel, mtxtAddress, mtxtHouseNo, mtxtLandMark;
        private ImageView mimgDelete;
        private Button mbtnSettheaddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mtxtLabel = itemView.findViewById(R.id.item_address_label);
            mtxtAddress = itemView.findViewById(R.id.item_address_address);
            mtxtHouseNo = itemView.findViewById(R.id.item_address_houseNo);
            mtxtLandMark = itemView.findViewById(R.id.item_address_landMark);

            mimgDelete = itemView.findViewById(R.id.item_address_img_delete);
            mbtnSettheaddress= itemView.findViewById(R.id.item_address_button_setAddress);
        }
    }
}
