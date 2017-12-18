package com.sourcey.movnpack.BidPlacementActivities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.sourcey.movnpack.DataBase.DatabaseManager;
import com.sourcey.movnpack.Helpers.Session;
import com.sourcey.movnpack.Model.BidRecievedModel;
import com.sourcey.movnpack.Model.SPBidCounterModel;
import com.sourcey.movnpack.Model.ServiceProvider;
import com.sourcey.movnpack.Network.HttpHandler;
import com.sourcey.movnpack.R;
import com.sourcey.movnpack.Utility.AppConstants;
import com.sourcey.movnpack.Utility.MemorizerUtil;

import org.json.JSONObject;

import java.util.HashMap;


public class SPCounterBidActivity extends AppCompatActivity {

    EditText messageEditText;
    EditText amountEditText;

    Button counterBidButton;
    Button backBtn;

    BidRecievedModel bidRecievedModel;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spcounter_bid);
        bidRecievedModel=  getIntent().getParcelableExtra("bidReceived");

        messageEditText = (EditText) findViewById(R.id.input_message);
        amountEditText = (EditText) findViewById(R.id.input_propose_amount);
        backBtn = (Button) findViewById(R.id.btn_back_activity);
        counterBidButton = (Button) findViewById(R.id.btn_counter_bid);

        counterBidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceProvider s = Session.getInstance().getServiceProvider();
                new SPCounterBidTask(bidRecievedModel.getUserToken(),bidRecievedModel.getBidId(),"",s.getPhoneNumber(),s.getName(),"DATE",messageEditText.getText().toString(),amountEditText.getText().toString()).execute();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public class SPCounterBidTask extends AsyncTask<String, Void, String> {

        String userToken,bidId,bidType,date,spId,spName,message,amount;

        public SPCounterBidTask(String userToken,String bidId,String bidType,String spId, String spName,String date , String message , String amount){

            //this.categoryName = categoryName;
            this.message = message;
            this.bidId = bidId;
            this.date =date;
            this.userToken = userToken;
            this.amount = amount;
            this.spId = spId;
            this.spName = spName;
            this.bidType = bidType;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SPCounterBidActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading...");
            progressDialog.show();


        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                response = userCounterBidPlacementAPI(userToken,bidId,bidType,spId,spName,date,message,amount);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //BidModel bidModel = JsonParser.getInstance().parseBidResponse(s);

            if (progressDialog != null){
                progressDialog.dismiss();
            }

            bidRecievedModel.setStatus("2");
            boolean res = DatabaseManager.getInstance(getApplicationContext()).editBidRecieved(bidRecievedModel);
            if (res){
                //emorizerUtil.displayToast(getApplicationContext(),"Update done");

                SPBidCounterModel spBidCounterModel = new SPBidCounterModel();
                spBidCounterModel.setBidId(bidId);
                spBidCounterModel.setMessage(amount);
                spBidCounterModel.setMessage(message);
                spBidCounterModel.setDate(date);

                boolean resu = DatabaseManager.getInstance(getApplicationContext()).addSPBidCounter(spBidCounterModel);

                if (resu){
                    MemorizerUtil.displayToast(getApplicationContext(),"Inserted");
                }
                else {
                    MemorizerUtil.displayToast(getApplicationContext(),"Insert nahi howa");
                }

                finish();
            }
            else {
                MemorizerUtil.displayToast(getApplicationContext(),"Update nahi howa");
            }


        }

    }

    public String userCounterBidPlacementAPI(String userToken,String bidId,String bidType,String spId,String spName,String date , String message , String amount) {
        String response = "";
        try {

            HttpHandler httpHandler = new HttpHandler();
            HashMap params =   new HashMap<>();
            HashMap<String, String> data = new HashMap<>();
            HashMap<String, String> notification = new HashMap<>();
            //data.put("to",categoryName);
            //data.put("message", message);
            data.put("Bid_Type","Bid_Counter");
            data.put("bidId", bidId);
            data.put("spId", spId);
            data.put("spName", spName);
            data.put("date",date);
            data.put("spToken", FirebaseInstanceId.getInstance().getToken());
            data.put("message",message);
            data.put("amount",amount);
            //data.put("userToken",userToken);
            //data.put("userId",userId);
            //data.put("userName",userName);
            //data.put("amount",amount);

            notification.put("body","You have received a counter bid request");
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
