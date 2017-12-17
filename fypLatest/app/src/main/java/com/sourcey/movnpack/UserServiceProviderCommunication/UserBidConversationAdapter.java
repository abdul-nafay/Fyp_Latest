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
import com.sourcey.movnpack.Model.ConversationListViewModel;
import com.sourcey.movnpack.R;

import java.util.ArrayList;

/**
 * Created by Abdul Nafay Waseem on 12/17/2017.
 */


public class UserBidConversationAdapter extends ArrayAdapter<ConversationListViewModel> implements View.OnClickListener{

    private ArrayList<ConversationListViewModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView nameTextView;
        TextView messageInitialsTextView;
        TextView dateTextView;
        TextView amountTextView;
    }

    public UserBidConversationAdapter(ArrayList<ConversationListViewModel> data, Context context) {
        super(context, R.layout.user_bid_conversation_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        ConversationListViewModel model =(ConversationListViewModel)object;


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
        ConversationListViewModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.user_bid_conversation_item, parent, false);
            viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.name_text_view);
            viewHolder.messageInitialsTextView = (TextView) convertView.findViewById(R.id.message_initials_textview);
            viewHolder.dateTextView= (TextView) convertView.findViewById(R.id.date_text_view);
            viewHolder.amountTextView = (TextView) convertView.findViewById(R.id.amount_text_view);
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.nameTextView.setText(dataModel.getName());
        viewHolder.messageInitialsTextView.setText(dataModel.getMessage());
        viewHolder.dateTextView.setText(dataModel.getDate());
        if (dataModel.getStatus().equals("1")) {
            viewHolder.amountTextView.setVisibility(View.GONE);
        }
        else {
            viewHolder.amountTextView.setVisibility(View.VISIBLE);
            viewHolder.amountTextView.setText(dataModel.getAmount());
        }

        //viewHolder.info.setOnClickListener(this);
        //viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}

