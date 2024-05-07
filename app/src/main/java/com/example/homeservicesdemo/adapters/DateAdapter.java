package com.example.homeservicesdemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homeservicesdemo.R;
import com.example.homeservicesdemo.models.DayDateModel;

import java.util.Calendar;
import java.util.List;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateViewHolder> {

    private Context mContext;
    private List<DayDateModel> mDayDateList;
    private OnDateItemClickListener mListener;
    public DateAdapter(Context context, List<DayDateModel> dayDateList, OnDateItemClickListener listener) {
        mContext = context;
        mDayDateList = dayDateList;
        mListener = listener;
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_calender, parent, false);
        return new DateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        DayDateModel dayDateModel = mDayDateList.get(position);
        holder.textViewDay.setText(dayDateModel.getDay());
        String[] dateParts = dayDateModel.getDate().split(" ");
        if (dateParts.length == 3) {
            String day = dateParts[1];
            String month = dateParts[0];
            // Set textMonth and textDate
            holder.textViewdate.setText(day);
            holder.textViewMonth.setText(month);
        } else {
            // If the date format is not as expected, set the full date
            holder.textViewdate.setText(dayDateModel.getDate());
        }

        System.out.println("day " + dayDateModel.getDay() + "date" + dayDateModel.getDate());
        if (dayDateModel.isSelected()) {
            // Set imgCheck visibility to visible if the item is selected
            holder.imageViewCheck.setVisibility(View.VISIBLE);
        } else {
            // Set imgCheck visibility to gone if the item is not selected
            holder.imageViewCheck.setVisibility(View.GONE);
        }

        // Set an OnClickListener to handle date selection
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset all selected flags to false
                for (DayDateModel model : mDayDateList) {
                    model.setSelected(false);
                }
                // Set the selected flag to true for the clicked item
                dayDateModel.setSelected(true);
                // Notify the adapter that the data set has changed to update the UI
                notifyDataSetChanged();
                if (mListener != null) {
                    mListener.onDateItemClick(dayDateModel);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDayDateList.size();
    }

    public class DateViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMonth;
        TextView textViewDay;
        TextView textViewdate;
        ImageView imageViewCheck;
        public DateViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewMonth = itemView.findViewById(R.id.item_calender_textView_month);
            textViewdate = itemView.findViewById(R.id.item_calender_textView_date);
            textViewDay = itemView.findViewById(R.id.item_calender_textView_day);

            imageViewCheck = itemView.findViewById(R.id.item_calender_imgView_check);

        }
    }


    public interface OnDateItemClickListener {
        void onDateItemClick(DayDateModel day);
    }
}

