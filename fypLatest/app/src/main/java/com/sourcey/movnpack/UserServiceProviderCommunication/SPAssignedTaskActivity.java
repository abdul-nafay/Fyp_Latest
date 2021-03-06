package com.sourcey.movnpack.UserServiceProviderCommunication;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sourcey.movnpack.DataBase.DatabaseManager;
import com.sourcey.movnpack.DrawerModule.DrawerActivity;
import com.sourcey.movnpack.DrawerModule.SPDrawerActivity;
import com.sourcey.movnpack.Helpers.API_TYPE;
import com.sourcey.movnpack.Helpers.Session;
import com.sourcey.movnpack.Model.AcceptedBidsModel;
import com.sourcey.movnpack.Model.AssignedTasksModel;
import com.sourcey.movnpack.Model.BaseModel;
import com.sourcey.movnpack.Model.BidRecievedModel;
import com.sourcey.movnpack.Model.ConfirmBidModel;
import com.sourcey.movnpack.Network.MessageAsyncInterface;
import com.sourcey.movnpack.Network.MessageAsyncTask;
import com.sourcey.movnpack.R;
import com.sourcey.movnpack.Utility.AppConstants;
import com.sourcey.movnpack.Utility.MemorizerUtil;
import com.sourcey.movnpack.Utility.Utility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;


public class SPAssignedTaskActivity extends AppCompatActivity implements MessageAsyncInterface {

    Button locationBtn;
    AppCompatButton viewOrignalBidBtn;
    AppCompatButton cancelBid;
    Button confirmServiceBtn;
    Button rejectButton;
    ProgressDialog progressDialog;
    TextView timeTextView;
    TextView dateTextView;
    TextView amountTextView;
    AssignedTasksModel assignedTasksModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spassigned_task);

        locationBtn = (Button) findViewById(R.id.btn_location);
        viewOrignalBidBtn = (AppCompatButton) findViewById(R.id.btn_view_orignal_bid);
        cancelBid  = (AppCompatButton) findViewById(R.id.cancel_Bid);
        //confirmServiceBtn = (Button) findViewById(R.id.btn_confirm_service);
        //rejectButton = (Button) findViewById(R.id.btn_reject_service);

        timeTextView = (TextView) findViewById(R.id.time_text_view);
        dateTextView = (TextView) findViewById(R.id.date_text_view);
        amountTextView = (TextView) findViewById(R.id.amount_text_view);

        //
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", Float.parseFloat(assignedTasksModel.getLat()), Float.parseFloat(assignedTasksModel.getLongi()), "");
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

        cancelBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertForCancelBid();
            }
        });

        String bidId = getIntent().getStringExtra("bidId");
        assignedTasksModel = (AssignedTasksModel) DatabaseManager.getInstance(this).getAssignedTaskByBidId(bidId);

        timeTextView.setText(assignedTasksModel.getTime());
        dateTextView.setText(assignedTasksModel.getDate());
        amountTextView.setText(assignedTasksModel.getAmount());
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
                progressDialog = new ProgressDialog(SPAssignedTaskActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Loading...");
                progressDialog.show();

                new MessageAsyncTask(prepareParamsForBroadcastOfUnlockBid(),AppConstants.API_BID_PLACEMENT,SPAssignedTaskActivity.this,API_TYPE.unLockBid).execute();
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

    void deleteAssignedTask() {
        BidRecievedModel bidRecievedModel;
        ArrayList<BaseModel> model = DatabaseManager.getInstance(this).getBidReceivedById(assignedTasksModel.getBidId());
        if (model != null && model.size() > 0){
//            bidRecievedModel = (BidRecievedModel) model.get(0);
//            bidRecievedModel.setLock(0);
//            DatabaseManager.getInstance(this).updateBidRecievedStatus(bidRecievedModel);
            boolean deleted =  DatabaseManager.getInstance(this).deleteAssignedTaskModel(assignedTasksModel);
            if (deleted) {
                MemorizerUtil.displayToast(getApplicationContext(),"Successfully Deleted");
            }
            else {
                MemorizerUtil.displayToast(getApplicationContext(), "Failed To Delete");
            }
        }
    }

    private HashMap<String,String> prepareParamsForSPCancelTask() {
        HashMap params =   new HashMap<>();
        HashMap<String, String> data = new HashMap<>();
        HashMap<String, String> notification = new HashMap<>();
        //data.put("to",categoryName);
        data.put("ID", assignedTasksModel.getID());
        data.put("message", "Dummy Message");
        data.put("bidId", assignedTasksModel.getBidId());
        data.put("date",Utility.getCurrentDateString());
        data.put("Bid_Type","SP_Cancel-Task");
        notification.put("body",Session.getInstance().getServiceProvider().getName()+" has cancelled the task you assigned to him");
        notification.put("title","Important Alert");
        BidRecievedModel bid =(BidRecievedModel) DatabaseManager.getInstance(this).getBidReceivedById(assignedTasksModel.getBidId()).get(0);
        params.put("to",bid.getUserToken());

        Gson gson = new Gson();
        String dataStr = gson.toJson(data);
        params.put("data",new JSONObject(data));

        //Gson gson = new Gson();
        String notificationStr = gson.toJson(notification);

        params.put("notification", new JSONObject(notification));

        return params;
    }
    private HashMap<String,String> prepareParamsForBroadcastOfUnlockBid() {
        HashMap params =   new HashMap<>();
        HashMap<String, String> data = new HashMap<>();
        //data.put("to",categoryName);
        data.put("Bid_Type","Bid_Unlock");
        data.put("bidId", assignedTasksModel.getBidId());
//("/topics/"+catName
        params.put("to","/topics/"+Session.getInstance().getServiceProvider().getCategoryName());

        Gson gson = new Gson();
        String dataStr = gson.toJson(data);
        params.put("data",new JSONObject(data));

        return params;
    }

    @Override
    public void didCompleteWithTask(HashMap<String, String> data, API_TYPE apiType) {

        if (apiType == API_TYPE.unLockBid) {
            new MessageAsyncTask(prepareParamsForSPCancelTask(),AppConstants.API_BID_PLACEMENT,this,API_TYPE.spCancelBid).execute();
        }

        else if (progressDialog != null) {
            progressDialog.dismiss();
            //finish();
            deleteAssignedTask();
            Intent intent = new Intent( getApplicationContext(), SPDrawerActivity.class );
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
}
