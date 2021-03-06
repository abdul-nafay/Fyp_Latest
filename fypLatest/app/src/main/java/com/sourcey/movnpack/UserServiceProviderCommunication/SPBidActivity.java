package com.sourcey.movnpack.UserServiceProviderCommunication;

import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.sourcey.movnpack.BidPlacementActivities.SPBidRecievedActivity;
import com.sourcey.movnpack.DataBase.DatabaseManager;
import com.sourcey.movnpack.Model.BaseModel;
import com.sourcey.movnpack.Model.BidModel;
import com.sourcey.movnpack.Model.BidRecievedModel;
import com.sourcey.movnpack.R;

import java.util.ArrayList;


public class SPBidActivity extends AppCompatActivity {

    ArrayList<BidRecievedModel> dataModels;
    ArrayList<BaseModel> bids;
    ListView listView;
    Button backBtn;
    AppCompatImageView emptySPImageView;
    AppCompatTextView emptySPTextView;
    private static SPBidAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spbid);

        backBtn = (Button) findViewById(R.id.btn_back_activity);
        listView = (ListView) findViewById(R.id.list);
        emptySPImageView = (AppCompatImageView) findViewById(R.id.empty_sp_bid);
        emptySPTextView = (AppCompatTextView) findViewById(R.id.empty_sp_bid_textView);
        bids = DatabaseManager.getInstance(this).getBidsRecieved();

        dataModels = new ArrayList<>();
        if(bids!=null) {
            emptySPImageView.setVisibility(View.GONE);
            emptySPTextView.setVisibility(View.GONE);
            for (BaseModel bid : bids) {
                dataModels.add((BidRecievedModel) bid);
            }
        }

        adapter= new SPBidAdapter(dataModels,getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                BidRecievedModel dataModel= dataModels.get(position);

                Intent intent = new Intent(SPBidActivity.this,SPBidRecievedActivity.class);
                intent.putExtra("bidRecieved", (Parcelable) dataModel);
                intent.putExtra("bidID",dataModel.getBidId());
                startActivity(intent);

                Snackbar.make(view, dataModel.getCategoryName()+"\n"+dataModel.getMessage()+" date: "+dataModel.getDate(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();

        bids = DatabaseManager.getInstance(this).getBidsRecieved();

        dataModels = new ArrayList<>();
        if(bids!=null) {
            for (BaseModel bid : bids) {
                dataModels.add((BidRecievedModel) bid);
            }
        }
        // dataModels= new ArrayList<>();


        adapter= new SPBidAdapter(dataModels,getApplicationContext());

        listView.setAdapter(adapter);

    }
}
