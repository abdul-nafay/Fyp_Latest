package com.sourcey.movnpack.BidPlacementActivities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.sourcey.movnpack.DataBase.DatabaseManager;
import com.sourcey.movnpack.Helpers.Session;
import com.sourcey.movnpack.Model.BidModel;
import com.sourcey.movnpack.Model.BidRecievedModel;
import com.sourcey.movnpack.Network.HttpHandler;
import com.sourcey.movnpack.R;
import com.sourcey.movnpack.Utility.AppConstants;
import com.sourcey.movnpack.Utility.MemorizerUtil;

import org.json.JSONObject;

import java.util.HashMap;

import static android.R.id.message;
import static android.os.Build.VERSION_CODES.M;

public class SPBidRecievedActivity extends AppCompatActivity implements View.OnClickListener {

    TextView subjectTextView;
    TextView messageTextView;
    TextView amountTextView;

    Button acceptBidButton;
    Button rejectBidButton;
    Button counterBidButton;

    BidRecievedModel bidRecievedModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spbid_recieved);

        bidRecievedModel = getIntent().getParcelableExtra("bidRecieved");


        subjectTextView = (TextView) findViewById(R.id.subject_text_view);
        messageTextView = (TextView) findViewById(R.id.message_text_view);
        amountTextView  = (TextView) findViewById(R.id.bid_amount_text_view);

        acceptBidButton = (Button) findViewById(R.id.btn_accept_bid);
        rejectBidButton = (Button) findViewById(R.id.btn_reject_bid);
        counterBidButton = (Button) findViewById(R.id.btn_counter_bid);

        subjectTextView.setText("Work Offer Dummy Text Need to Change");
        messageTextView.setText(bidRecievedModel.getMessage());
        amountTextView.setText(bidRecievedModel.getAmount());

        if (!bidRecievedModel.getStatus().equals("0")){

            acceptBidButton.getBackground().setAlpha(100);
            acceptBidButton.setEnabled(false);
            rejectBidButton.getBackground().setAlpha(100);
            rejectBidButton.setEnabled(false);
            counterBidButton.getBackground().setAlpha(100);
            counterBidButton.setEnabled(false);

        }
        else {
            acceptBidButton.getBackground().setAlpha(255);
            acceptBidButton.setEnabled(true);
            rejectBidButton.getBackground().setAlpha(255);
            rejectBidButton.setEnabled(true);
            counterBidButton.getBackground().setAlpha(255);
            counterBidButton.setEnabled(true);
        }

        acceptBidButton.setOnClickListener((View.OnClickListener) this );


    }


    @Override
    public void onClick(View v) {

        if (acceptBidButton.getId() == v.getId()) {

            Session s = Session.getInstance();

            new SPBidRecievedTask(bidRecievedModel.getUserToken(), bidRecievedModel.getBidId(), "Bid_Accepted", s.getServiceProvider().getPhoneNumber(), s.getServiceProvider().getName(), "DATE").execute();
        }
        else if (rejectBidButton.getId() == v.getId()){
            bidRecievedModel.setStatus("2");
            boolean res = DatabaseManager.getInstance(this).editBidRecieved(bidRecievedModel);
            if (res){
                MemorizerUtil.displayToast(getApplicationContext(),"Update done");
            }
            else {
                MemorizerUtil.displayToast(getApplicationContext(),"Update nahi howa");
            }
        }
        else if (counterBidButton.getId() == v.getId()){
            bidRecievedModel.setStatus("3");
            boolean res = DatabaseManager.getInstance(this).editBidRecieved(bidRecievedModel);
            if (res){
               // MemorizerUtil.displayToast(getApplicationContext(),"Update done");

            }
            else {
                MemorizerUtil.displayToast(getApplicationContext(),"Update nahi howa");
            }
        }
    }


    public class SPBidRecievedTask extends AsyncTask<String, Void, String> {

        String userToken,bidId,bidType,date,spId,spName;

        public SPBidRecievedTask(String userToken,String bidId,String bidType,String spId, String spName,String date){

            //this.categoryName = categoryName;
            //this.message = message;
            this.bidId = bidId;
            this.date =date;
            this.userToken = userToken;
           // this.userId = userId;
           // this.userName = userName;
          //  this.amount = amount;
            this.spId = spId;
            this.spName = spName;
            this.bidType = bidType;
         //   this.status = status;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                response = userBidPlacementApi(userToken,bidId,bidType,spId,spName,date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //BidModel bidModel = JsonParser.getInstance().parseBidResponse(s);

            bidRecievedModel.setStatus("1");
            boolean res = DatabaseManager.getInstance(getApplicationContext()).editBidRecieved(bidRecievedModel);
            if (res){
                //emorizerUtil.displayToast(getApplicationContext(),"Update done");
                finish();
            }
            else {
                MemorizerUtil.displayToast(getApplicationContext(),"Update nahi howa");
            }
        }

        }

        public String userBidPlacementApi(String userToken,String bidId,String bidType,String spId,String spName,String date) {
            String response = "";
            try {

                HttpHandler httpHandler = new HttpHandler();
                HashMap params =   new HashMap<>();
                HashMap<String, String> data = new HashMap<>();
                HashMap<String, String> notification = new HashMap<>();
                //data.put("to",categoryName);
                //data.put("message", message);
                data.put("Bid_Type","Bid_Accepted");
                data.put("bidId", bidId);
                data.put("spId", spId);
                data.put("spName", spName);
                data.put("date",date);
                data.put("spToken", FirebaseInstanceId.getInstance().getToken());
                //data.put("userToken",userToken);
                //data.put("userId",userId);
                //data.put("userName",userName);
                //data.put("amount",amount);

                notification.put("body","Your bid has been accepted!");
                notification.put("title","Tap to view");

                params.put("to",userToken);

                Gson gson = new Gson();
                String dataStr = gson.toJson(data);
                params.put("data",new JSONObject(data));

                //Gson gson = new Gson();
                String notificationStr = gson.toJson(notification);

                params.put("notification", new JSONObject(notification));

                Log.i("ALi",params.toString());

                response = httpHandler.performPostCallWithHeader(AppConstants.API_BID_PLACEMENT, params);

            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
            }

            return response;

        }
    }

