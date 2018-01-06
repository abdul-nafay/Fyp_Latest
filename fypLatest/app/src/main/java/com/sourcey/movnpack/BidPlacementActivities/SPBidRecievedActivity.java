package com.sourcey.movnpack.BidPlacementActivities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.sourcey.movnpack.DataBase.DatabaseManager;
import com.sourcey.movnpack.Helpers.Session;
import com.sourcey.movnpack.LoginModule.SignupActivity;
import com.sourcey.movnpack.Model.BaseModel;
import com.sourcey.movnpack.Model.BidModel;
import com.sourcey.movnpack.Model.BidRecievedModel;
import com.sourcey.movnpack.Network.HttpHandler;
import com.sourcey.movnpack.R;
import com.sourcey.movnpack.UserServiceProviderCommunication.SPAssignedTaskActivity;
import com.sourcey.movnpack.Utility.AppConstants;
import com.sourcey.movnpack.Utility.MemorizerUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.R.id.message;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static android.os.Build.VERSION_CODES.M;

public class SPBidRecievedActivity extends AppCompatActivity implements View.OnClickListener {

    TextView subjectTextView;
    TextView messageTextView;
    TextView amountTextView;
    TextView bidStatusTextView;

    Button acceptBidButton;
    Button rejectBidButton;
    Button counterBidButton;
    Button backBtn;

    BidRecievedModel bidRecievedModel;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spbid_recieved);

        bidRecievedModel = getIntent().getParcelableExtra("bidRecieved");
        String bidID = getIntent().getStringExtra("bidID");
        ArrayList<BaseModel> model = DatabaseManager.getInstance(this).getBidReceivedById(bidID);
        if (model != null && model.size() > 0){
            bidRecievedModel = (BidRecievedModel) model.get(0);
        }
        subjectTextView = (TextView) findViewById(R.id.subject_text_view);
        messageTextView = (TextView) findViewById(R.id.message_text_view);
        amountTextView  = (TextView) findViewById(R.id.bid_amount_text_view);
        bidStatusTextView  = (TextView) findViewById(R.id.task_status_text_view);

        acceptBidButton = (Button) findViewById(R.id.btn_accept_bid);
        rejectBidButton = (Button) findViewById(R.id.btn_reject_bid);
        counterBidButton = (Button) findViewById(R.id.btn_counter_bid);
        backBtn = (Button) findViewById(R.id.btn_back_activity);

        subjectTextView.setText(bidRecievedModel.getSubject());
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
        counterBidButton.setOnClickListener((View.OnClickListener)this);
        backBtn.setOnClickListener((View.OnClickListener) this);
        rejectBidButton.setOnClickListener((View.OnClickListener)this);

        if(bidRecievedModel.getLock()==0){
            bidStatusTextView.setVisibility(View.GONE);
        }

        if(bidRecievedModel.getLock()==1){
            String mystring=new String("Task has been assigned to you click her to view details");
            SpannableString content = new SpannableString(mystring);
            content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
            bidStatusTextView.setText(content);

            bidStatusTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), SPAssignedTaskActivity.class);
                    intent.putExtra("bidId",bidRecievedModel.getBidId());
                    startActivity(intent);
                }
            });


        }

        if(bidRecievedModel.getLock()==2){
            bidStatusTextView.setText("Task has been already assigned");
        }


    }


    @Override
    public void onClick(View v) {

        if (acceptBidButton.getId() == v.getId()) {

            Session s = Session.getInstance();

            new SPBidRecievedTask(bidRecievedModel.getUserToken(), bidRecievedModel.getBidId(), "Bid_Accepted", s.getServiceProvider().getPhoneNumber(), s.getServiceProvider().getName(), "DATE").execute();
        }
        else if (rejectBidButton.getId() == v.getId()){
            bidRecievedModel.setStatus("3");
            boolean res = DatabaseManager.getInstance(this).editBidRecieved(bidRecievedModel);
            if (res){
                MemorizerUtil.displayToast(getApplicationContext(),"Update done");
                finish();
            }
            else {
                MemorizerUtil.displayToast(getApplicationContext(),"Update nahi howa");
            }
        }
        else if (counterBidButton.getId() == v.getId()){
            /*
            bidRecievedModel.setStatus("3");
            boolean res = DatabaseManager.getInstance(this).editBidRecieved(bidRecievedModel);
            if (res){
               // MemorizerUtil.displayToast(getApplicationContext(),"Update done");

            }
            else {
                MemorizerUtil.displayToast(getApplicationContext(),"Update nahi howa");
            }
            */
            Intent intent = new Intent(SPBidRecievedActivity.this,SPCounterBidActivity.class);

            intent.putExtra("bidReceived", (Parcelable) bidRecievedModel);
            //startActivity(intent);
            startActivityForResult(intent,1);
        }
        else if (backBtn.getId() == v.getId()){

            finish();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                finish();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
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

            progressDialog = new ProgressDialog(SPBidRecievedActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
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
            if (progressDialog != null){
                progressDialog.dismiss();
            }

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


