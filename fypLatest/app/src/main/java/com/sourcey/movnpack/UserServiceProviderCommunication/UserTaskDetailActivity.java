package com.sourcey.movnpack.UserServiceProviderCommunication;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sourcey.movnpack.DataBase.DatabaseManager;
import com.sourcey.movnpack.DrawerModule.DrawerActivity;
import com.sourcey.movnpack.Helpers.API_TYPE;
import com.sourcey.movnpack.Helpers.Session;
import com.sourcey.movnpack.Model.AssignedTasksModel;
import com.sourcey.movnpack.Model.BidModel;
import com.sourcey.movnpack.Model.ConfirmBidModel;
import com.sourcey.movnpack.Network.MessageAsyncInterface;
import com.sourcey.movnpack.Network.MessageAsyncTask;
import  com.sourcey.movnpack.R;
import com.sourcey.movnpack.SP.spProfileInfo;
import com.sourcey.movnpack.Utility.AppConstants;
import com.sourcey.movnpack.Utility.MemorizerUtil;
import com.sourcey.movnpack.Utility.Utility;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

public class UserTaskDetailActivity extends AppCompatActivity implements View.OnClickListener, MessageAsyncInterface {

    Button locationBtn;
    AppCompatButton viewOrignalBidBtn;
    AppCompatButton cancelBid;
    AppCompatButton viewSpDetailsBtn;
    Button confirmServiceBtn;
    Button rejectButton;
    ProgressDialog progressDialog;
    TextView timeTextView;
    TextView dateTextView;
    TextView amountTextView;
    TextView spNameTextView;
    ConfirmBidModel confirmBidModel;
    BidModel originalBidModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_task_detail);

        //
        locationBtn = (Button) findViewById(R.id.user_task_btn_location);
        viewOrignalBidBtn = (AppCompatButton) findViewById(R.id.user_task_btn_view_orignal_bid);
        viewSpDetailsBtn = (AppCompatButton) findViewById(R.id.user_task_view_sp_detail);
        cancelBid  = (AppCompatButton) findViewById(R.id.user_task_cancel_Bid);
        viewSpDetailsBtn.setOnClickListener(this);
        viewOrignalBidBtn.setOnClickListener(this);
        cancelBid.setOnClickListener(this);
        //confirmServiceBtn = (Button) findViewById(R.id.btn_confirm_service);
        //rejectButton = (Button) findViewById(R.id.btn_reject_service);

        timeTextView = (TextView) findViewById(R.id.user_task_time_text_view);
        dateTextView = (TextView) findViewById(R.id.user_task_date_text_view);
        amountTextView = (TextView) findViewById(R.id.user_task_amount_text_view);
        spNameTextView = (TextView) findViewById(R.id.user_task_sp_name_value);
        //

        //
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", Float.parseFloat(confirmBidModel.getLat()), Float.parseFloat(confirmBidModel.getLongi()), "");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                try
                {
                    startActivity(intent);
                }
                catch(ActivityNotFoundException ex)
                {
                    try
                    {
                        Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(unrestrictedIntent);
                    }
                    catch(ActivityNotFoundException innerEx)
                    {
                        MemorizerUtil.displayToast(getApplicationContext(),"Please Install Google Map app ");
                    }
                }
            }
        });
        //

        /*cancelBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertForCancelBid();
            }
        });*/

        String cbmID = getIntent().getStringExtra("cbmID");
        String bidID = getIntent().getStringExtra("bidID");
        confirmBidModel = DatabaseManager.getInstance(this).getConfirmedBidByID(cbmID);
        originalBidModel =  DatabaseManager.getInstance(this).getBidWithID(bidID);

        timeTextView.setText(confirmBidModel.getTime());
        dateTextView.setText(confirmBidModel.getDate());
        amountTextView.setText(confirmBidModel.getAmount());
        spNameTextView.setText(confirmBidModel.getSpId());
    }

    @Override
    public void onClick(View view) {
       if (view == viewOrignalBidBtn) {
           Intent intent = new Intent(UserTaskDetailActivity.this,UserBidConversationActivity.class);
           intent.putExtra("bidId",originalBidModel.getBidId());
           startActivity(intent);
       }
       else if (view == viewSpDetailsBtn) {
           Intent intent = new Intent(getApplicationContext(), spProfileInfo.class);
           intent.putExtra("number",confirmBidModel.getSpId());
           startActivity(intent);
       }
       else if (view == cancelBid) {
            showAlertForCancelBid();
       }
       else {

       }
    }
    public void showAlertForCancelBid() {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Cancel Task");
        builder.setMessage("Are you sure you want to Cancel Task??");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // continue with delete
                //MemorizerUtil.displayToast(getApplicationContext(),"Done");
                //deleteAssignedTask();
                progressDialog = new ProgressDialog(UserTaskDetailActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Loading...");
                progressDialog.show();

                new MessageAsyncTask(prepareParamsForBroadcastOfUnlockBid(), AppConstants.API_BID_PLACEMENT,UserTaskDetailActivity.this, API_TYPE.unLockBid).execute();
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // do nothing
            }
        });
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.show();
    }
    private HashMap<String,String> prepareParamsForBroadcastOfUnlockBid() {
        HashMap params =   new HashMap<>();
        HashMap<String, String> data = new HashMap<>();
        //data.put("to",categoryName);
        data.put("Bid_Type","Bid_Unlock");
        data.put("bidId", originalBidModel.getBidId());
//("/topics/"+catName
        params.put("to","/topics/"+originalBidModel.getCategoryName());

        Gson gson = new Gson();
        String dataStr = gson.toJson(data);
        params.put("data",new JSONObject(data));

        return params;
    }
    private HashMap<String,String> prepareParamsForSPCancelTask() {
        HashMap params =   new HashMap<>();
        HashMap<String, String> data = new HashMap<>();
        HashMap<String, String> notification = new HashMap<>();
        //data.put("to",categoryName);
        data.put("ID", confirmBidModel.getID());
        data.put("message", "Dummy Message");
        data.put("bidId", confirmBidModel.getBidId());
        data.put("date", Utility.getCurrentDateString());
        data.put("Bid_Type","User_Cancel-Task");
        notification.put("body", Session.getInstance().getUser().getName()+" has cancelled the task it assigned to you");
        notification.put("title","Important Alert");
       // BidRecievedModel bid =(BidRecievedModel) DatabaseManager.getInstance(this).getBidReceivedById(assignedTasksModel.getBidId()).get(0);
        //BidModel bidModel = (BidModel) DatabaseManager.getInstance(this).getBidWithID(confirmBidModel.getBidId());
        params.put("to",confirmBidModel.getSpToken());

        Gson gson = new Gson();
        String dataStr = gson.toJson(data);
        params.put("data",new JSONObject(data));

        //Gson gson = new Gson();
        String notificationStr = gson.toJson(notification);

        params.put("notification", new JSONObject(notification));

        return params;
    }
    @Override
    public void didCompleteWithTask(HashMap<String, String> data, API_TYPE apiType) {

        if (apiType == API_TYPE.unLockBid) {
            new MessageAsyncTask(prepareParamsForSPCancelTask(),AppConstants.API_BID_PLACEMENT,this,API_TYPE.userCancelBid).execute();
        }
        else if (progressDialog != null) {
            progressDialog.dismiss();
            //finish();
            deleteConfirmBid();
            Intent intent = new Intent( getApplicationContext(), DrawerActivity.class );
            intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
            this.startActivity( intent );
        }
    }

    @Override
    public void didFailToCompleteTask() {
        if (progressDialog != null) {
            progressDialog.dismiss();

        }
        MemorizerUtil.displayToast(getApplicationContext(),"Something went wrong, lease Try Again");
    }
    void deleteConfirmBid() {
        boolean isDel = DatabaseManager.getInstance(this).deleteConfirmBid(confirmBidModel);
        if (isDel) {

        }
        else {

        }
    }

}
