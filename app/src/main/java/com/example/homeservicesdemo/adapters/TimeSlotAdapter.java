package com.example.homeservicesdemo.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homeservicesdemo.R;
import com.example.homeservicesdemo.models.DayDateModel;
import com.example.homeservicesdemo.models.TimeSlotBean;

import java.util.Calendar;
import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder> {

    private Context mContext;
    private List<TimeSlotBean> mTimeSlots;
    private OnItemClickListener mListener;
    private String mType = "";


    public TimeSlotAdapter(Context context, List<TimeSlotBean> timeSlots,OnItemClickListener listener, String type) {
        mContext = context;
        mTimeSlots = timeSlots;
        mListener = listener;
        mType = type;
    }

    @NonNull
    @Override
    public TimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeslot, parent, false);
        return new TimeSlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotViewHolder holder, @SuppressLint("RecyclerView") int position) {
        TimeSlotBean timeSlot = mTimeSlots.get(position);
        holder.textViewTiming.setText(timeSlot.getTimings());

        // Extract the hour and minute parts from the time string
        String[] timeParts = timeSlot.getTimings().split(":");
        if (timeParts.length == 2) {
            try {
                int slotHour = Integer.parseInt(timeParts[0].trim());
                String[] minuteAndMeridiem = timeParts[1].trim().split(" ");
                if (minuteAndMeridiem.length == 2) {
                    int slotMinute = Integer.parseInt(minuteAndMeridiem[0]);
                    String meridiem = minuteAndMeridiem[1];

                    /*// Adjust the hour if it's in PM
                    if (meridiem.equalsIgnoreCase("PM")) {
                        slotHour += 12;
                    } else if (meridiem.equalsIgnoreCase("AM")) {
                        slotHour += 12;
                    }*/

                    // Get the current time
                    Calendar currentTime = Calendar.getInstance();
                    int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
                    int currentMinute = currentTime.get(Calendar.MINUTE);

                    // Calculate the difference between current time and time slot
                    int hourDiff = slotHour - currentHour;
                    int minuteDiff = slotMinute - currentMinute;

                    // If the time slot is within 1 hour from the current time
                    if (hourDiff < 1 || (hourDiff == 1 && minuteDiff <= 0)) {
                        holder.textViewTiming.setTextColor(Color.GRAY); // Set to gray for other time slots
                        holder.itemView.setEnabled(false); // Disable item selection
                    } else {

                        holder.textViewTiming.setTextColor(Color.BLACK);
                        holder.itemView.setEnabled(true); // Enable item selection
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                holder.textViewTiming.setTextColor(Color.GRAY); // Set to gray if parsing fails
                holder.itemView.setEnabled(false); // Disable item selection
            }
        } else {
            holder.textViewTiming.setTextColor(Color.GRAY); // Set to gray if time format is invalid
            holder.itemView.setEnabled(false); // Disable item selection
        }

        if (timeSlot.isSelected()) {
            // Set imgCheck visibility to visible if the item is selected
            holder.imgCheck.setVisibility(View.VISIBLE);
        } else {
            // Set imgCheck visibility to gone if the item is not selected
            holder.imgCheck.setVisibility(View.GONE);
        }

        // Set an OnClickListener to handle date selection
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(position, mType);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTimeSlots.size();
    }

    static class TimeSlotViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTiming;
        ImageView imgCheck;

        TimeSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTiming = itemView.findViewById(R.id.item_timeslot_textView_timing);
            imgCheck = itemView.findViewById(R.id.item_timeslot_imgView_Check);
        }

    }
    public interface OnItemClickListener {
        void onItemClick(int position, String type);

    }

}
