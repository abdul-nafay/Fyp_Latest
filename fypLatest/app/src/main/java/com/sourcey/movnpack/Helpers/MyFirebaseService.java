package com.sourcey.movnpack.Helpers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sourcey.movnpack.BidPlacementActivities.SPBidRecievedActivity;
import com.sourcey.movnpack.DataBase.DatabaseManager;
import com.sourcey.movnpack.DrawerModule.DrawerActivity;
import com.sourcey.movnpack.Model.AcceptedBidsModel;
import com.sourcey.movnpack.Model.BidRecievedModel;
import com.sourcey.movnpack.Model.ServiceProvider;
import com.sourcey.movnpack.R;
import com.sourcey.movnpack.UserServiceProviderCommunication.SPBidActivity;
import com.sourcey.movnpack.Utility.MemorizerUtil;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.breakStrategy;
import static android.R.attr.data;
import static android.R.attr.defaultToDeviceProtectedStorage;
import static com.google.android.gms.measurement.AppMeasurement.getInstance;

/**
 * Created by abdul on 12/15/17.
 */

public class MyFirebaseService extends FirebaseMessagingService{

    private android.support.v4.app.NotificationCompat.Builder notificationBuilder;
    private Bitmap icon;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("TAG", "From: " + remoteMessage.getFrom());
        Log.d("Ali",remoteMessage.toString());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("TAG", "Message data payload: " + remoteMessage.getData());

            Map<String,String> data = remoteMessage.getData();
            String bidType = data.get("Bid_Type");
            switch (bidType)
            {
                case "Bid_Received":
                    // Handle Here Bide Received
                    ServiceProvider serviceProvider = Session.getInstance().getServiceProvider();
                    String spId = serviceProvider.getPhoneNumber();

                    BidRecievedModel bidRecievedModel = new BidRecievedModel();
                    bidRecievedModel.setMessage(data.get("message"));
                    bidRecievedModel.setBidId(data.get("bidId"));
                    bidRecievedModel.setDate(data.get("date"));
                    bidRecievedModel.setUserToken(data.get("userToken"));
                    bidRecievedModel.setUserId(data.get("userId"));
                    bidRecievedModel.setUserName(data.get("userName"));
                    bidRecievedModel.setAmount(data.get("amount"));
                    bidRecievedModel.setCategoryName(data.get("to"));

                    bidRecievedModel.setStatus("0");
                    bidRecievedModel.setSpId(spId);

                    boolean res = DatabaseManager.getInstance(this).addBidRecieved(bidRecievedModel);
                    if(res){
                        //MemorizerUtil.displayToast(getApplicationContext(),"Data
                        // Inserted");
                        // Check if message contains a notification payload.
                        if (remoteMessage.getNotification() != null) {
                            Log.d("TAG", "Message Notification Body: " + remoteMessage.getNotification().getBody());
                            sendNotification(remoteMessage.getNotification().getBody(),bidRecievedModel);
                        }
                    }
                    else {
                        //MemorizerUtil.displayToast(getApplicationContext(),"Error in Insert");
                    }



                    //
                    break;

                case "Bid_Accepted":

                    //ServiceProvider sp = Session.getInstance().getServiceProvider();
                    //String spID = sp.getPhoneNumber();
                   // String spName = sp.getName();
                    AcceptedBidsModel acceptedBidsModel = new AcceptedBidsModel();
                    acceptedBidsModel.setBidId(data.get("bidId"));
                    acceptedBidsModel.setSpId(data.get("spId"));
                    acceptedBidsModel.setSpName(data.get("spName"));
                    acceptedBidsModel.setDate(data.get("date"));

                    boolean result = DatabaseManager.getInstance(this).addAcceptedBids(acceptedBidsModel);
                    if (result){

                        if (remoteMessage.getNotification() != null) {
                            Log.d("TAG", "Message Notification Body: " + remoteMessage.getNotification().getBody());
                            sendNotificationForAcceptedBids(remoteMessage.getNotification().getBody(),acceptedBidsModel);
                        }
                    }
                    else {

                    }

                default:
                    break;
            }

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                //scheduleJob();
            } else {
                // Handle message within 10 seconds
                //handleNow();
            }

        }



        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    private void sendNotification(String messageBody, BidRecievedModel bidRecievedModel) {
        Intent intent = new Intent(this, SPBidRecievedActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("bidRecieved", (Parcelable) bidRecievedModel);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        android.support.v4.app.NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("FCM Message")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri).setContentIntent(pendingIntent).setPriority(Notification.PRIORITY_HIGH);
                ;

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void sendNotificationForAcceptedBids(String messageBody, AcceptedBidsModel bidRecievedModel) {
        Intent intent = new Intent(this, DrawerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
       // intent.putExtra("bidRecieved", (Parcelable) bidRecievedModel);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        android.support.v4.app.NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("FCM Message")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri).setContentIntent(pendingIntent).setPriority(Notification.PRIORITY_HIGH);
        ;

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
