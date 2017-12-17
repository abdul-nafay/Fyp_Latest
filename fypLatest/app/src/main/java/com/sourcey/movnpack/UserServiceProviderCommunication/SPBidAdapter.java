package com.sourcey.movnpack.UserServiceProviderCommunication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sourcey.movnpack.Model.BidModel;
import com.sourcey.movnpack.Model.BidRecievedModel;
import com.sourcey.movnpack.R;

import java.util.ArrayList;

/**
 * Created by Abdul Nafay Waseem on 12/17/2017.
 */

public class SPBidAdapter extends ArrayAdapter<BidRecievedModel> implements View.OnClickListener{

    private ArrayList<BidRecievedModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView titleTextView;
        TextView messageInitialsTextView;
        TextView dateTextView;

    }

    public SPBidAdapter(ArrayList<BidRecievedModel> data, Context context) {
        super(context, R.layout.user_bid_items, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        BidRecievedModel model =(BidRecievedModel)object;


       /* switch (v.getId())
        {
            case R.id.item_info:
                Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }*/
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        BidRecievedModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.user_bid_items, parent, false);
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.title_textview);
            viewHolder.messageInitialsTextView = (TextView) convertView.findViewById(R.id.message_initials_textview);
            viewHolder.dateTextView = (TextView) convertView.findViewById(R.id.date_textview);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.titleTextView.setText(dataModel.getCategoryName());
        viewHolder.messageInitialsTextView.setText(dataModel.getMessage());
        viewHolder.dateTextView.setText(dataModel.getDate());

        //viewHolder.info.setOnClickListener(this);
        //viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}
