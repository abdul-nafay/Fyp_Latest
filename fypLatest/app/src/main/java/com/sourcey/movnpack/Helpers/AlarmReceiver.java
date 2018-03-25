package com.sourcey.movnpack.Helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sourcey.movnpack.Alarm.AlarmActivity;

/**
 * Created by ali.haider on 2/13/2018.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ALI","Received Intent");

        String taskID = intent.getStringExtra("taskID") ;
        String bidID = intent.getStringExtra("bidID");
        Intent scheduledIntent = new Intent(context, AlarmActivity.class);
        scheduledIntent.putExtra("taskID",taskID);
        scheduledIntent.putExtra("bidID",bidID);
        scheduledIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(scheduledIntent);

    }
}
