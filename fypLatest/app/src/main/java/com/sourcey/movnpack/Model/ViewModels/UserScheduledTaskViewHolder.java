package com.sourcey.movnpack.Model.ViewModels;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.sourcey.movnpack.R;
/**
 * Created by ali.haider on 2/9/2018.
 */

public class UserScheduledTaskViewHolder extends RecyclerView.ViewHolder {
    public TextView subjectTW;
    public TextView timeTW;
    public TextView dateTW;
    public TextView amountTW;
    public TextView assignedToTW;
    public AppCompatButton detailsBtn;
    public  AppCompatButton trackBtn;
    public UserScheduledTaskViewHolder(View itemView) {
        super(itemView);
        subjectTW =  (TextView) itemView.findViewById(R.id.card_subject_tw);
        timeTW =  (TextView) itemView.findViewById(R.id.card_time_tw);
        dateTW =  (TextView) itemView.findViewById(R.id.card_date_tw);
        amountTW =  (TextView) itemView.findViewById(R.id.card_amount_tw);
        assignedToTW =  (TextView) itemView.findViewById(R.id.card_assigned_to_sp);
        detailsBtn = (AppCompatButton)  itemView.findViewById(R.id.card_view_details);
        trackBtn = (AppCompatButton) itemView.findViewById(R.id.card_track_location);
    }
}
