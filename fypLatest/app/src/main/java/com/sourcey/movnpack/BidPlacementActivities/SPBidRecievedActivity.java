package com.sourcey.movnpack.BidPlacementActivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.sourcey.movnpack.R;

public class SPBidRecievedActivity extends AppCompatActivity {

    TextView subjectTextView;
    TextView messageTextView;
    TextView amountTextView;

    Button acceptBidButton;
    Button rejectBidButton;
    Button counterBidButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spbid_recieved);

        subjectTextView = (TextView) findViewById(R.id.subject_text_view);
        messageTextView = (TextView) findViewById(R.id.message_text_view);
        amountTextView  = (TextView) findViewById(R.id.bid_amount_text_view);

        acceptBidButton = (Button) findViewById(R.id.btn_accept_bid);
        rejectBidButton = (Button) findViewById(R.id.btn_reject_bid);
        counterBidButton = (Button) findViewById(R.id.btn_counter_bid);
    }
}
