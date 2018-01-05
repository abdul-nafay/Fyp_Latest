package com.sourcey.movnpack.UserServiceProviderCommunication;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sourcey.movnpack.DataBase.DatabaseManager;
import com.sourcey.movnpack.Model.AssignedTasksModel;
import com.sourcey.movnpack.R;
import com.sourcey.movnpack.Utility.MemorizerUtil;

import java.util.Locale;


public class SPAssignedTaskActivity extends AppCompatActivity {

    Button locationBtn;
    Button viewOrignalBidBtn;
    Button confirmServiceBtn;
    Button rejectButton;

    TextView timeTextView;
    TextView dateTextView;
    TextView amountTextView;
    AssignedTasksModel assignedTasksModel;

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
                        MemorizerUtil.displayToast(this,"Please Install Google Map app ");
                    }
                }
            }
        });
        //
        String bidId = getIntent().getStringExtra("bidId");
        assignedTasksModel = (AssignedTasksModel) DatabaseManager.getInstance(this).getAssignedTaskByBidId(bidId);

        timeTextView.setText(assignedTasksModel.getTime());
        dateTextView.setText(assignedTasksModel.getDate());
        amountTextView.setText(assignedTasksModel.getAmount());
    }
}
