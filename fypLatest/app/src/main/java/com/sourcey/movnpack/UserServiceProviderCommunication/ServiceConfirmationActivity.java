package com.sourcey.movnpack.UserServiceProviderCommunication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.sourcey.movnpack.DrawerModule.DrawerActivity;
import com.sourcey.movnpack.Helpers.API_TYPE;
import com.sourcey.movnpack.Helpers.Session;
import com.sourcey.movnpack.Helpers.TaskScheduler;
import com.sourcey.movnpack.Model.AcceptedBidsModel;
import com.sourcey.movnpack.Model.BaseModel;
import com.sourcey.movnpack.Model.BidModel;
import com.sourcey.movnpack.Model.ConfirmBidModel;
import com.sourcey.movnpack.Model.UserBidCounterModel;
import com.sourcey.movnpack.Network.MessageAsyncInterface;
import com.sourcey.movnpack.Network.MessageAsyncTask;
import com.sourcey.movnpack.R;
import com.sourcey.movnpack.Utility.AppConstants;
import com.sourcey.movnpack.Utility.MemorizerUtil;
import com.sourcey.movnpack.Utility.Utility;

//
import com.google.android.gms.location.places.*;
//

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import static android.R.attr.format;


public class ServiceConfirmationActivity extends AppCompatActivity implements MessageAsyncInterface , View.OnClickListener {

    Button doneButton;
    Button cancelServiceButton;
    TextView setTimeTextView;
    TextView setDateTextView;
    TextView amountTextView;
    TextView locationTextView;
    String spToken;
    EditText serviceTimeInput;

    BidModel bid ;
    BaseModel bidResponseToConfirm;
    ConfirmBidModel localBid;
    ProgressDialog progressDialog;
    int PLACE_PICKER_REQUEST = 1;
    Place selectedPlace;

    private TimePicker timePicker1;

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    String formattedDate = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_confirmation);

        String bidId = getIntent().getStringExtra("bidId").toString();
        bidResponseToConfirm = getIntent().getParcelableExtra("bidResponse");
        ArrayList<BaseModel> b =  DatabaseManager.getInstance(this).getBidById(bidId);
        if (b!=null && b.size() > 0) {
            bid =(BidModel) b.get(0);
        }

        doneButton = (Button) findViewById(R.id.btn_confirm);
        cancelServiceButton = (Button) findViewById(R.id.btn_reject_bid);

        amountTextView = (TextView) findViewById(R.id.confirmed_amount_text_view);
        setTimeTextView = (TextView) findViewById(R.id.time_set);
        setDateTextView = (TextView) findViewById(R.id.date_set_text_view);

        setTimeTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ServiceConfirmationActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        setTimeTextView.setText(getTimeString(selectedHour,selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        amountTextView.setText(getAmount()+" RS");
        locationTextView =  (TextView) findViewById(R.id.location_text_view);
        locationTextView.setOnClickListener(new View.OnClickListener() {
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
        //serviceTimeInput = (EditText) findViewById(R.id.servivce_time_text_view);



       // showDate(year, month+1, day);




        doneButton.setOnClickListener(this);




    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {

            DatePickerDialog dp = new DatePickerDialog(this, myDateListener, year, month, day);
            Calendar c = Calendar.getInstance();
            int yy = c.get(Calendar.YEAR);
            int mm = c.get(Calendar.MONTH);
            int dd = c.get(Calendar.DAY_OF_MONTH);
            c.set(Calendar.MONTH, mm);
            c.set(Calendar.DAY_OF_MONTH, dd);
            c.set(Calendar.YEAR, yy);
            dp.getDatePicker().setMinDate(c.getTimeInMillis());

            return  dp;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    //showDate(arg1, arg2+1, arg3);
                    int   day  = arg0.getDayOfMonth();
                    int   month= arg0.getMonth();
                    int   year = arg0.getYear();
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    formattedDate = sdf.format(calendar.getTime());
                    setDateTextView.setText(formattedDate);
                    Log.d("ALI",formattedDate);
                }
            };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {

                selectedPlace = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("%s", selectedPlace.getName());
               // Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                locationTextView.setText(toastMsg);
            }
        }
    }

    @Override
    public void didCompleteWithTask(HashMap<String, String> data, API_TYPE apiType) {

        if (apiType == API_TYPE.confirmBidBroadcast) {
            if (progressDialog != null) {
                /// Schedule Task :D
                TaskScheduler.getInstance().scheduleTaskForUser(localBid , getApplicationContext());

                progressDialog.dismiss();
                //finish();
                Intent intent = new Intent( getApplicationContext(), DrawerActivity.class );
                intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                this.startActivity( intent );
            }
        }
        else if (apiType == API_TYPE.confirmBidSIngle) {
                if (DatabaseManager.getInstance(this).addConfirmBidUser(localBid)) {
                    Log.d("ALI","Added");

                    new MessageAsyncTask(prepareParamsForBroadcastOfConfirmation(),AppConstants.API_BID_PLACEMENT,this,API_TYPE.confirmBidBroadcast).execute();
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
            if (!validate()) {
                MemorizerUtil.displayToast(this,"Please Enter all the Information");
                return;
            }
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
       // String timeString = getTimeString(timePicker1.getCurrentHour(),timePicker1.getCurrentMinute());
        String ID = UUID.randomUUID().toString();
        String timeString  =  setTimeTextView.getText().toString();
        localBid = new ConfirmBidModel();
        localBid.setIsDeleted("0");
        localBid.setID(ID);
        localBid.setMessage("Dummy Message");
        localBid.setBidId(bid.getBidId());
        localBid.setUserId(Session.sharedInstance.getUser().getPhoneNumber());
        localBid.setSpId(getSpID());
        localBid.setSpToken(getSpToken());
        localBid.setLat(selectedPlace.getLatLng().latitude +"");
        localBid.setLongi(selectedPlace.getLatLng().longitude +"");
        localBid.setDate(formattedDate);
        localBid.setTime(timeString);
        localBid.setAmount(getAmount());
        HashMap params =   new HashMap<>();
        HashMap<String, String> data = new HashMap<>();
        HashMap<String, String> notification = new HashMap<>();
        //data.put("to",categoryName);
        data.put("ID", ID);
        data.put("message", "Dummy Message");
        data.put("bidId", bid.getBidId());
        data.put("date",formattedDate);
        data.put("amount",getAmount());
        data.put("Bid_Type","Bid_Confirm_Single");
        data.put("serviceTime",timeString);
        data.put("lat",selectedPlace.getLatLng().latitude +"");
        data.put("long",selectedPlace.getLatLng().longitude +"");
        notification.put("body","You have just assigned a new task.");
        notification.put("title","Congratulations!!");

        params.put("to",getSpToken());

        Gson gson = new Gson();
        String dataStr = gson.toJson(data);
        params.put("data",new JSONObject(data));

        //Gson gson = new Gson();
        String notificationStr = gson.toJson(notification);

        params.put("notification", new JSONObject(notification));

       return params;
    }
    private HashMap<String,String> prepareParamsForBroadcastOfConfirmation() {
        HashMap params =   new HashMap<>();
        HashMap<String, String> data = new HashMap<>();
        //data.put("to",categoryName);
        data.put("Bid_Type","Bid_Lock");
        data.put("bidId", bid.getBidId());
        data.put("assignedTo",getSpID());
//("/topics/"+catName
        params.put("to","/topics/"+bid.getCategoryName());

        Gson gson = new Gson();
        String dataStr = gson.toJson(data);
        params.put("data",new JSONObject(data));

        return params;
    }

    public String getTimeString(int hour , int minute)
    {
        String format = "";
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }
        String timeString =  hour + ":" + minute + " " + format;
        return timeString;
    }

    public boolean validate()
    {
        if (!(locationTextView.getText().toString().equals("") || setTimeTextView.getText().toString().equals("")
            || setDateTextView.getText().equals(""))) {
            return true;
        }
        return false;
    }

    // HELPER FUNCTIONS
    String getSpID() {
        if (bidResponseToConfirm instanceof AcceptedBidsModel) {
           return ((AcceptedBidsModel) bidResponseToConfirm).getSpId();
        }
        else {
            return ((UserBidCounterModel) bidResponseToConfirm).getSpId();
        }
    }

    String getSpToken() {
        if (bidResponseToConfirm instanceof AcceptedBidsModel) {
            return ((AcceptedBidsModel) bidResponseToConfirm).getSpToken();
        }
        else {
            return ((UserBidCounterModel) bidResponseToConfirm).getSpToken();
        }
    }

    String getAmount() {
        if (bidResponseToConfirm instanceof AcceptedBidsModel) {
            return bid.getAmount();
        }
        else {
            return ((UserBidCounterModel) bidResponseToConfirm).getAmount();
        }
    }

}
