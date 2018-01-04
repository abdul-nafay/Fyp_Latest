package com.sourcey.movnpack.UserServiceProviderCommunication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;
import com.sourcey.movnpack.BidPlacementActivities.UserBidPlacementActivity;
import com.sourcey.movnpack.DataBase.AcceptedBids;
import com.sourcey.movnpack.DataBase.Bid;
import com.sourcey.movnpack.DataBase.Confirmed_Bids;
import com.sourcey.movnpack.DataBase.DatabaseManager;
import com.sourcey.movnpack.Helpers.API_TYPE;
import com.sourcey.movnpack.Helpers.Session;
import com.sourcey.movnpack.Model.AcceptedBidsModel;
import com.sourcey.movnpack.Model.BaseModel;
import com.sourcey.movnpack.Model.BidModel;
import com.sourcey.movnpack.Model.ConfirmBidModel;
import com.sourcey.movnpack.Network.MessageAsyncInterface;
import com.sourcey.movnpack.Network.MessageAsyncTask;
import com.sourcey.movnpack.R;
import com.sourcey.movnpack.Utility.AppConstants;
import com.sourcey.movnpack.Utility.Utility;

//
import com.google.android.gms.location.places.*;
//

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class ServiceConfirmationActivity extends AppCompatActivity implements MessageAsyncInterface , View.OnClickListener {

    Button locationButton;
    Button doneButton;
    Button cancelServiceButton;
    BidModel bid ;
    TextView amountTextView;
    String spToken;
    EditText serviceTimeInput;
    AcceptedBidsModel bidResponseToConfirm;
    ConfirmBidModel localBid;
    ProgressDialog progressDialog;
    int PLACE_PICKER_REQUEST = 1;
    Place selectedPlace;
    TextView locationTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_confirmation);

        String bidId = getIntent().getStringExtra("bidId").toString();
        bidResponseToConfirm = getIntent().getParcelableExtra("acceptedBid");
        ArrayList<BaseModel> b =  DatabaseManager.getInstance(this).getBidById(bidId);
        if (b!=null && b.size() > 0) {
            bid =(BidModel) b.get(0);
        }

        locationButton = (Button) findViewById(R.id.btn_location);
        doneButton = (Button) findViewById(R.id.btn_confirm);
        cancelServiceButton = (Button) findViewById(R.id.btn_reject_bid);

        amountTextView = (TextView) findViewById(R.id.confirmed_amount_text_view);
        locationTextView =  (TextView) findViewById(R.id.location_text_view);
        serviceTimeInput = (EditText) findViewById(R.id.servivce_time_text_view);

        doneButton.setOnClickListener(this);

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(ServiceConfirmationActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                selectedPlace = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", selectedPlace.getName());
               // Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                locationTextView.setText(toastMsg);
            }
        }
    }

    @Override
    public void didCompleteWithTask(HashMap<String, String> data, API_TYPE apiType) {

        if (apiType == API_TYPE.confirmBidBroadcast) {

        }
        else if (apiType == API_TYPE.confirmBidSIngle) {
                if (DatabaseManager.getInstance(this).addConfirmBidUser(localBid)) {
                    Log.d("ALI","Added");
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                }
                else {
                    Log.d("ALI","Failed To ADD");
                }
        }
    }

    @Override
    public void didFailToCompleteTask() {

    }

    @Override
    public void onClick(View v) {

        if (v == doneButton) {

            progressDialog = new ProgressDialog(ServiceConfirmationActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            new MessageAsyncTask(prepareParamsForConfirmationSingle(), AppConstants.API_BID_PLACEMENT,this,API_TYPE.confirmBidSIngle).execute();
        }

    }

    private HashMap<String,String> prepareParamsForConfirmationSingle() {
        String token = "";

        localBid = new ConfirmBidModel();
        localBid.setMessage("Dummy Message");
        localBid.setBidId(bid.getBidId());
        localBid.setUserId(Session.sharedInstance.getUser().getPhoneNumber());
        localBid.setSpId(((AcceptedBidsModel) bidResponseToConfirm).getSpId());
        localBid.setSpToken(((AcceptedBidsModel) bidResponseToConfirm).getSpToken());
        localBid.setLat("10.7");
        localBid.setLongi("10.7");
        localBid.setDate(serviceTimeInput.getText().toString());

        HashMap params =   new HashMap<>();
        HashMap<String, String> data = new HashMap<>();
        HashMap<String, String> notification = new HashMap<>();
        //data.put("to",categoryName);
        data.put("message", "Dummy Message");
        data.put("bidId", bid.getBidId());
        data.put("date", serviceTimeInput.getText().toString());
        data.put("amount",amountTextView.getText().toString());
        data.put("Bid_Type","Bid_Confirm_Single");
        data.put("serviceTime",serviceTimeInput.getText().toString());
        data.put("lat","10.7");
        data.put("long","10.7");
        notification.put("body","You have just assigned a new task.");
        notification.put("title","Congratulations!!");

        params.put("to",((AcceptedBidsModel) bidResponseToConfirm).getSpToken());

        Gson gson = new Gson();
        String dataStr = gson.toJson(data);
        params.put("data",new JSONObject(data));

        //Gson gson = new Gson();
        String notificationStr = gson.toJson(notification);

        params.put("notification", new JSONObject(notification));

       return params;
    }
}
