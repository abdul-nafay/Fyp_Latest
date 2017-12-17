package com.sourcey.movnpack.BidPlacementActivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sourcey.movnpack.Model.BidRecievedModel;
import com.sourcey.movnpack.R;


public class SPCounterBidActivity extends AppCompatActivity {

    EditText messageEditText;
    EditText amountEditText;

    Button counterBidButton;
    BidRecievedModel bidRecievedModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spcounter_bid);
        bidRecievedModel=  getIntent().getParcelableExtra("bidReceived");

        messageEditText = (EditText) findViewById(R.id.input_message);
        amountEditText = (EditText) findViewById(R.id.input_propose_amount);

        counterBidButton = (Button) findViewById(R.id.btn_counter_bid);

        counterBidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
    }
}
