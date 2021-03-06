package com.sourcey.movnpack.BidPlacementActivities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.sourcey.movnpack.DataBase.DatabaseManager;
import com.sourcey.movnpack.Helpers.JsonParser;
import com.sourcey.movnpack.Helpers.Session;
import com.sourcey.movnpack.Model.BidModel;
import com.sourcey.movnpack.Model.User;
import com.sourcey.movnpack.Network.HttpHandler;
import com.sourcey.movnpack.R;
import com.sourcey.movnpack.SP.spProfileInfo;
import com.sourcey.movnpack.Utility.AppConstants;
import com.sourcey.movnpack.Utility.MemorizerUtil;
import com.sourcey.movnpack.Utility.Utility;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import butterknife.ButterKnife;

import static android.R.attr.password;
import static android.R.id.input;
import static com.sourcey.movnpack.R.id.map;

public class UserBidPlacementActivity extends Activity {

    public TextView categoryName;
    public EditText inputMessage,inputBid,inputSubject;
    public Button submit, backButton;
    ProgressDialog progressDialog;
    public String catName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bid_placement);

        catName = getIntent().getStringExtra("categoryName");
        initUI();



    }

    public void initUI(){

        categoryName = (TextView) findViewById(R.id.category_name);
        inputMessage = (EditText) findViewById(R.id.input_name);
        inputBid = (EditText) findViewById(R.id.input_min_bid);
        inputSubject = (EditText) findViewById(R.id.input_subject);

        backButton = (Button) findViewById(R.id.btn_back_activity);
        submit = (Button) findViewById(R.id.btn_create_bid);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    User u = Session.getInstance().getUser();
                    String dateString = Utility.getCurrentDateString();
                    new userBidPlacementTask("/topics/"+catName,inputMessage.getText().toString(), UUID.randomUUID().toString(),dateString, FirebaseInstanceId.getInstance().getToken(),u.getPhoneNumber(),u.getName(),inputBid.getText().toString(),inputSubject.getText().toString()).execute();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        categoryName.setText(catName + " Service");
    }

    public boolean validate(){

        boolean valid = true;

        String subject = inputSubject.getText().toString();
        String message = inputMessage.getText().toString();
        String bidAmount = inputBid.getText().toString();

        if (subject.isEmpty()){
            inputSubject.setError("Please Enter Subject");
            valid = false;
        }
        else {
            inputSubject.setError(null);
        }
        if (message.isEmpty()){
            inputMessage.setError("Please Enter message");
            valid = false;
        }
        else {
            inputMessage.setError(null);
        }
        if (bidAmount.isEmpty()){
            inputBid.setError("Please Enter amount");
            valid = false;
        }
        else {
            inputBid.setError(null);
        }

        return valid;


    }


    public class userBidPlacementTask extends AsyncTask<String, Void, String>{

        String message,bidId,date,userToken,userId,userName,amount,categoryName ,subject;

       public userBidPlacementTask(String categoryName,String message,String bidId,String date,String userToken,String userId,String userName,String amount , String subject){

            this.categoryName = categoryName;
            this.message = message;
            this.bidId = bidId;
            this.date =date;
            this.userToken = userToken;
            this.userId = userId;
            this.userName = userName;
            this.amount = amount;
            this.subject = subject;
       }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(UserBidPlacementActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                response = userBidPlacementApi(categoryName,message,bidId,date,userToken,userId,userName,amount,subject);
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

            BidModel bidModel = new BidModel();
            bidModel.setMessage(message);
            bidModel.setBidId(bidId);
            bidModel.setDate(date);
            bidModel.setUserToken(userToken);
            bidModel.setUserId(userId);
            bidModel.setUserName(userName);
            bidModel.setAmount(amount);
            bidModel.setCategoryName(categoryName);
            bidModel.setSubject(subject);
            bidModel.setCategoryName(catName);
            boolean result;
            result = DatabaseManager.getInstance(getApplicationContext()).addUserBid(bidModel);

            if (result){
                finish();
            }
            else {
                MemorizerUtil.displayToast(getApplicationContext(),"Error Inserting data");
            }



        }

        public String userBidPlacementApi(String categoryName,String message,String bidId,String date,String userToken,String userId,String userName,String amount , String subject) {
            String response = "";
            try {

                HttpHandler httpHandler = new HttpHandler();
                HashMap params =   new HashMap<>();
                HashMap<String, String> data = new HashMap<>();
                HashMap<String, String> notification = new HashMap<>();
                //data.put("to",categoryName);
                data.put("message", message);
                data.put("bidId", bidId);
                data.put("date",date);
                data.put("userToken",userToken);
                data.put("userId",userId);
                data.put("userName",userName);
                data.put("amount",amount);
                data.put("Bid_Type","Bid_Received");
                data.put("subject",subject);
                notification.put("body","YOu have just Received a new work offer");
                notification.put("title","New Work Offer");

                params.put("to",categoryName);

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

}
