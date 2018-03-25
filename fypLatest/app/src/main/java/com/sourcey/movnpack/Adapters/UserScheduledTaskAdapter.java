package com.sourcey.movnpack.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sourcey.movnpack.Helpers.UserScheduledTaskInterface;
import com.sourcey.movnpack.Model.ViewModels.UserScheduledTaskViewHolder;
import com.sourcey.movnpack.Model.ViewModels.UserScheduledTaskViewModel;
import com.sourcey.movnpack.R;

import java.util.ArrayList;

/**
 * Created by ali.haider on 2/9/2018.
 */

public class UserScheduledTaskAdapter extends RecyclerView.Adapter<UserScheduledTaskViewHolder> {

    private ArrayList<UserScheduledTaskViewModel> scheduledTasks;
    public UserScheduledTaskInterface intrface;
    public UserScheduledTaskAdapter(ArrayList<UserScheduledTaskViewModel> scheduledTasks) {
        this.scheduledTasks = scheduledTasks;
    }

    @Override
    public UserScheduledTaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.scheduledtask_cardview, parent, false);

        return new UserScheduledTaskViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(UserScheduledTaskViewHolder holder, int position) {
        final UserScheduledTaskViewModel ci = scheduledTasks.get(position);

        holder.subjectTW.setText(ci.subject);
        holder.timeTW.setText(ci.time);
        holder.dateTW.setText(ci.date);
        holder.amountTW.setText(ci.amount + " RS");
        holder.assignedToTW.setText(ci.spNumber);
        holder.detailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intrface.didPressedDetailButtonWithViewModel(ci);
            }
        });
        //Jugaar
        if (ci.assignedBidID != null) {
            holder.trackBtn.setVisibility(View.GONE);
        }
        else {
            holder.trackBtn.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return scheduledTasks.size();
    }
}
