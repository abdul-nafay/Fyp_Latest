package com.sourcey.movnpack.UserServiceProviderCommunication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.sourcey.movnpack.R;


public class SPAssignedTaskActivity extends AppCompatActivity {

    Button locationBtn;
    Button viewOrignalBidBtn;
    Button confirmServiceBtn;
    Button rejectButton;

    TextView timeTextView;
    TextView dateTextView;
    TextView amountTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spassigned_task);

        locationBtn = (Button) findViewById(R.id.btn_location);
        viewOrignalBidBtn = (Button) findViewById(R.id.btn_view_orignal_bid);
        //confirmServiceBtn = (Button) findViewById(R.id.btn_confirm_service);
        //rejectButton = (Button) findViewById(R.id.btn_reject_service);

        timeTextView = (TextView) findViewById(R.id.time_text_view);
        dateTextView = (TextView) findViewById(R.id.date_text_view);
        amountTextView = (TextView) findViewById(R.id.amount_text_view);



    }
}
