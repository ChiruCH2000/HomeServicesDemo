package com.example.homeservicesdemo.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homeservicesdemo.R;
import com.example.homeservicesdemo.activites.RateCardActivity;
import com.example.homeservicesdemo.activites.ServicesActivity;
import com.example.homeservicesdemo.helperclass.DataBaseHelper;
import com.example.homeservicesdemo.models.CustomerReviewsBean;
import com.example.homeservicesdemo.models.HowItWork;
import com.example.homeservicesdemo.models.ServiceExcluded;
import com.example.homeservicesdemo.models.ServiceIncluded;
import com.example.homeservicesdemo.models.ServicesBean;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ServicesViewHolder> {

    private Context mContext;
    private ArrayList<ServicesBean> mServiceItems;
    private ArrayList<HowItWork> mHowItWorkslist;
    private ArrayList<ServiceExcluded> mServiceExcludelist;
    private ArrayList<ServiceIncluded> mServiceIncludelist;
    private String mCatId,mSubCatId,mCategoryName,mServiceName,mPrice,mRating;
    private DataBaseHelper mDatabaseHelper;
    private AdapterCallback adapterCallback;


    public ServicesAdapter(Context context, ArrayList<ServicesBean> serviceItems, DataBaseHelper databaseHelper, ServicesActivity adapterCallback) {
        this.mContext = context;
        this.mServiceItems = serviceItems;
        this.mDatabaseHelper=databaseHelper;
        this.adapterCallback = adapterCallback;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_services, parent, false);
        return new ServicesViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ServicesViewHolder holder, int position) {
        ServicesBean serviceItem = mServiceItems.get(position);

        // Bind data to views
        holder.textViewsubCategoryName.setText(serviceItem.getSubcategory_name());
        holder.textViewDays.setText(serviceItem.getWarranty());
        holder.textViewServiceName.setText(serviceItem.getName());
        holder.textViewRating.setText(String.valueOf(serviceItem.getService_rating()));
        holder.textViewPrice.setText("₹"+serviceItem.getPrice());

        System.out.println("Service Excluded Item: " + serviceItem.getService_excluded());
        System.out.println("Service included Item: " + serviceItem.getService_included());


        String note1 = serviceItem.getNote1();
        if (!TextUtils.isEmpty(note1)) {
            holder.textViewNote1.setText("\u2022 " + note1);
        }
        String note2 = serviceItem.getNote2();
        if (!TextUtils.isEmpty(note2)) {
            holder.textViewNote2.setText("\u2022 " + note2);
        }
        String duration = serviceItem.getDuration();
        if (!TextUtils.isEmpty(duration)) {
            holder.textViewTime.setText("\u2022 " + duration);
        }
        String warrenty = serviceItem.getWarranty();
        if (!TextUtils.isEmpty(warrenty)) {
            holder.textViewDays.setText("\u2022 " + warrenty);
        }
// Check if the service is already in the cart
        boolean isInCart = mDatabaseHelper.isServiceInCart(serviceItem.getService_id());

        if (isInCart) {
            // If the service is already in the cart, show countLL and hide txtAdd
            holder.llCount.setVisibility(View.VISIBLE);
            holder.txtViewAdd.setVisibility(View.GONE);
            int quantity = mDatabaseHelper.getServiceQuantity(serviceItem.getService_id());
            holder.txtCount.setText(String.valueOf(quantity));
            adapterCallback.onAdapterItemChanged();
        } else {
            // If the service is not in the cart, hide countLL and show txtAdd
            holder.llCount.setVisibility(View.GONE);
            holder.txtViewAdd.setVisibility(View.VISIBLE);
            adapterCallback.onAdapterItemChanged();
        }
        // Set image resource
        Picasso.get().load(serviceItem.getImage()).fit().centerCrop().into(holder.imageView);

        /*holder.txtViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected service details
                String serviceName = serviceItem.getName();
                double servicePrice = Double.parseDouble(serviceItem.getPrice());
                String categoryId = (serviceItem.getCategory_id());
                String categoryName = serviceItem.getCategory_name();
                String serviceId = (serviceItem.getService_id());
                String subServiceId = (serviceItem.getSubcat_id());
                String subServiceName = (serviceItem.getSubcategory_name());
                // Check if the service is already in the cart
                boolean isInCart = mDatabaseHelper.isServiceInCart(serviceId);
                int quantity = mDatabaseHelper.getServiceQuantity(serviceId);


                if (isInCart) {
                    if (quantity > 0) {
                        // If the service is already in the cart, show llCount and hide txtViewAdd
                        holder.llCount.setVisibility(View.VISIBLE);
                        holder.txtViewAdd.setVisibility(View.GONE);
                        holder.txtCount.setText(String.valueOf(quantity));
                        adapterCallback.onAdapterItemChanged();
                    }

                } else {
                    // If the service is not in the cart, add it to the cart and update UI
                    quantity = 1; // Default quantity
                    mDatabaseHelper.addToCart(serviceId, categoryId, categoryName, serviceName, subServiceId, subServiceName, servicePrice, quantity);
                    // Hide llCount and show txtViewAdd
                    holder.llCount.setVisibility(View.GONE);
                    holder.txtViewAdd.setVisibility(View.VISIBLE);
                    holder.txtCount.setText(String.valueOf(quantity));
                    *//*adapterCallback.onAdapterItemChanged();*//*
                }
            }
        });

        holder.txtIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentCount = Integer.parseInt(holder.txtCount.getText().toString());
                currentCount++;
                holder.txtCount.setText(String.valueOf(currentCount));
                updateQuantityInDatabase(serviceItem.getService_id(), currentCount);
                adapterCallback.onAdapterItemChanged();
            }
        });

        holder.txtDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentCount = Integer.parseInt(holder.txtCount.getText().toString());
                if (currentCount > 0) {
                    currentCount--;
                    holder.txtCount.setText(String.valueOf(currentCount));
                    updateQuantityInDatabase(serviceItem.getService_id(), currentCount);
                }
                // Check if the count is below 1
                if (currentCount < 1) {
                    // Remove the service from the table
                    mDatabaseHelper.removeServiceFromCart(serviceItem.getService_id());
                    // Update the UI dynamically
                    holder.llCount.setVisibility(View.GONE);
                    holder.txtViewAdd.setVisibility(View.VISIBLE);
                }
                adapterCallback.onAdapterItemChanged();
            }
        });*/
        holder.txtViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected service details
                String serviceName = serviceItem.getName();
                double servicePrice = Double.parseDouble(serviceItem.getPrice());
                String categoryId = serviceItem.getCategory_id();
                String categoryName = serviceItem.getCategory_name();
                String serviceId = serviceItem.getService_id();
                String subServiceId = serviceItem.getSubcat_id();
                String subServiceName = serviceItem.getSubcategory_name();
                String type = serviceItem.getType();
                ArrayList varient = serviceItem.getVariants();

                // Fetch the quantity of the service from the database
                int quantity = mDatabaseHelper.getServiceQuantity(serviceId);

                if (quantity > 0) {
                    // If the service is already in the cart, show llCount and hide txtViewAdd
                    holder.llCount.setVisibility(View.VISIBLE);
                    holder.txtViewAdd.setVisibility(View.GONE);
                    holder.txtCount.setText(String.valueOf(quantity));
                } else {
                    // If the service is not in the cart, add it to the cart and update UI
                    quantity = 1; // Default quantity
                    mDatabaseHelper.addToCart(serviceId, categoryId, categoryName, serviceName, subServiceId, subServiceName, servicePrice, quantity,type,varient);
                    // Hide llCount and show txtViewAdd
                    holder.llCount.setVisibility(View.GONE);
                    holder.txtViewAdd.setVisibility(View.VISIBLE);
                    holder.txtCount.setText(String.valueOf(quantity));
                }
                adapterCallback.onAdapterItemChanged();
            }
        });

// Increment button click listener
        holder.txtIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentCount = Integer.parseInt(holder.txtCount.getText().toString());
                currentCount++;
                holder.txtCount.setText(String.valueOf(currentCount));
                updateQuantityInDatabase(serviceItem.getService_id(), currentCount);
                adapterCallback.onAdapterItemChanged();
            }
        });

        // Decrement button click listener
        holder.txtDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentCount = Integer.parseInt(holder.txtCount.getText().toString());
                if (currentCount > 0) {
                    currentCount--;
                    holder.txtCount.setText(String.valueOf(currentCount));
                    updateQuantityInDatabase(serviceItem.getService_id(), currentCount);
                }
                // If the count is below 1, remove the service from the table
                if (currentCount < 1) {
                    mDatabaseHelper.removeServiceFromCart(serviceItem.getService_id());
                    holder.llCount.setVisibility(View.GONE);
                    holder.txtViewAdd.setVisibility(View.VISIBLE);
                }
                adapterCallback.onAdapterItemChanged();
            }
        });

        holder.textViewViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSubCatId=serviceItem.getSubcat_id();
                mCatId =serviceItem.getCategory_id();
                mCategoryName=serviceItem.getCategory_name()+"-"+serviceItem.getSubcategory_name();
                mServiceName= serviceItem.getName();
                mPrice=serviceItem.getPrice();
                mRating= String.valueOf(serviceItem.getService_rating());
                mHowItWorkslist= serviceItem.getHow_it_works();
                mServiceIncludelist =serviceItem.getService_included();
                mServiceExcludelist =serviceItem.getService_excluded();

                Toast.makeText(mContext, "onclick VD"+mServiceExcludelist.size(), Toast.LENGTH_SHORT).show();
                showDialogViewDetails(mCatId,mSubCatId,mCategoryName,mServiceName,mPrice,mRating ,mServiceIncludelist,mServiceExcludelist,mHowItWorkslist);

            }
        });
    }
    private void updateQuantityInDatabase(String serviceId, int newQuantity) {
        // Update quantity in the database
        mDatabaseHelper.updateQuantity(Integer.parseInt(serviceId), newQuantity);
    }
    private void showDialogViewDetails(String mCatId, String mSubCatId,String mCategoryName,String servicename,String price,String rating,ArrayList<ServiceIncluded> mServiceIncludelist,ArrayList<ServiceExcluded> mServiceExcludelist,ArrayList<HowItWork> mHowItWorkslist) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_viewdetails);

        LinearLayout mllClose,mllSRCard;
        TextView mtxtTitle,mtxtRating,mtxtPrice,txtViewHIW,txtViewSI,txtViewSE;

        RecyclerView mRvHowItWorks,mRvInclude,mRvExclude,mRvCustomerReviews;



        mllClose = dialog.findViewById(R.id.bottom_sheet_viewdetails_linearLayoutclose);
        mllSRCard = dialog.findViewById(R.id.bottom_sheet_viewdetails_linearLayout_standardRateCard);

        txtViewHIW=dialog.findViewById(R.id.bottom_sheet_viewdetails_texView_howitworks);
        txtViewSI=dialog.findViewById(R.id.bottom_sheet_viewdetails_texView_include);
        txtViewSE=dialog.findViewById(R.id.bottom_sheet_viewdetails_texView_exclude);

        mtxtTitle = dialog.findViewById(R.id.bottom_sheet_viewdetails_textView_name);
        mtxtPrice = dialog.findViewById(R.id.bottom_sheet_viewdetails_textview_price);
        mtxtRating = dialog.findViewById(R.id.bottom_sheet_viewdetails_textview_rating);

        mRvHowItWorks = dialog.findViewById(R.id.bottom_sheet_viewdetails_recyclerView_howItWorks);
        mRvInclude = dialog.findViewById(R.id.bottom_sheet_viewdetails_recyclerView_included);
        mRvExclude = dialog.findViewById(R.id.bottom_sheet_viewdetails_recyclerView_excluded);
        mRvCustomerReviews = dialog.findViewById(R.id.bottom_sheet_viewdetails_recyclerView_reviews);


        if (!mHowItWorkslist.isEmpty()) {
            HowItWorksAdapter HIWAdapter = new HowItWorksAdapter(mContext, mHowItWorkslist);
            mRvHowItWorks.setLayoutManager(new LinearLayoutManager(mContext));
            mRvHowItWorks.setAdapter(HIWAdapter);
        }
        else txtViewHIW.setVisibility(View.GONE);
        if (!mServiceIncludelist.isEmpty()) {
            ServiceIncludeAdapter SIadapter = new ServiceIncludeAdapter(mContext, mServiceIncludelist);
            mRvInclude.setLayoutManager(new LinearLayoutManager(mContext));
            mRvInclude.setAdapter(SIadapter);
        }
        else txtViewSI.setVisibility(View.GONE);

        if (!mServiceExcludelist.isEmpty()) {
            ServiceExcludeAdapter SEadapter = new ServiceExcludeAdapter(mContext, mServiceExcludelist);
            mRvExclude.setLayoutManager(new LinearLayoutManager(mContext));
            mRvExclude.setAdapter(SEadapter);
        }
        else txtViewSE.setVisibility(View.GONE);

        mtxtTitle.setText(servicename);
        mtxtPrice.setText("₹"+price);
        mtxtRating.setText(rating);

        mllClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        mllSRCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RateCardActivity.class);
                intent.putExtra("cat_id",mCatId);
                intent.putExtra("subCatId",mSubCatId);
                intent.putExtra("categoryName",mCategoryName);
                dialog.getContext().startActivity(intent);
            }
        });

        ArrayList<CustomerReviewsBean> staticReviewsList = new ArrayList<>();
        staticReviewsList.add(new CustomerReviewsBean("Santhosh Kashyap", "explained the problem in detail and fixed the issue", "4.0", "Nov 2023"));
        staticReviewsList.add(new CustomerReviewsBean("John Doe", "Great service, highly recommended!", "5.0", "Dec 2023"));
        staticReviewsList.add(new CustomerReviewsBean("Santhosh Kashyap", "explained the problem in detail and fixed the issue", "4.0", "Nov 2023"));
        staticReviewsList.add(new CustomerReviewsBean("John Doe", "Great service, highly recommended!", "5.0", "Dec 2023"));
        staticReviewsList.add(new CustomerReviewsBean("Santhosh Kashyap", "explained the problem in detail and fixed the issue", "4.0", "Nov 2023"));
        staticReviewsList.add(new CustomerReviewsBean("John Doe", "Great service, highly recommended!", "5.0", "Dec 2023"));


        // Set up RecyclerView adapter for customer reviews
        CustomerReviewsAdapter adapter = new CustomerReviewsAdapter(mContext, staticReviewsList);
        mRvCustomerReviews.setLayoutManager(new LinearLayoutManager(mContext));
        mRvCustomerReviews.setAdapter(adapter);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }
    @Override
    public int getItemCount() {
        return mServiceItems.size();
    }

    public interface AdapterCallback {
        void onAdapterItemChanged();
    }

    public static class ServicesViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        LinearLayout llCount;
        TextView textViewsubCategoryName,textViewDays,textViewServiceName,textViewRating,textViewPrice,textViewTime,textViewNote1,textViewNote2,textViewViewDetails,txtViewAdd,txtDecrement,txtIncrement,txtCount;

        public ServicesViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.item_search_services_imgView_img);

            textViewsubCategoryName = itemView.findViewById(R.id.item_search_services_textView_categoryName);
            textViewDays = itemView.findViewById(R.id.item_search_services_textView_warrenty);
            textViewServiceName = itemView.findViewById(R.id.item_services_textview_serviceName);
            textViewRating = itemView.findViewById(R.id.item_services_textview_rating);
            textViewPrice = itemView.findViewById(R.id.item_services_textview_price);
            textViewTime = itemView.findViewById(R.id.item_services_textview_duration);
            textViewNote1 = itemView.findViewById(R.id.item_services_textview_note1);
            textViewNote2 = itemView.findViewById(R.id.item_services_textview_note2);
            textViewViewDetails = itemView.findViewById(R.id.item_services_textview_Viewdetails);
            txtViewAdd=itemView.findViewById(R.id.item_services_textView_add);
            txtIncrement=itemView.findViewById(R.id.item_services_textView_plus);
            txtDecrement=itemView.findViewById(R.id.item_services_textView_minus);
            txtCount=itemView.findViewById(R.id.item_services_textView_count);

            llCount =itemView.findViewById(R.id.item_services_linerLayout_count);

        }
    }
}

