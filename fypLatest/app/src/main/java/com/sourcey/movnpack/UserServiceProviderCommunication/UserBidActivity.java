package com.sourcey.movnpack.UserServiceProviderCommunication;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sourcey.movnpack.DataBase.DatabaseManager;
import com.sourcey.movnpack.Model.BaseModel;
import com.sourcey.movnpack.Model.BidModel;
import com.sourcey.movnpack.R;

import java.util.ArrayList;

import static android.R.id.closeButton;


public class UserBidActivity extends AppCompatActivity {

    Button backBtn;


    ArrayList<BidModel> dataModels;
    ArrayList<BaseModel> bids;
    ListView listView;
    private static UserBidAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bid);

        backBtn = (Button) findViewById(R.id.btn_back_activity);



        listView = (ListView)findViewById(R.id.list);
        bids = DatabaseManager.getInstance(this).getBidsForUserId();

        dataModels = new ArrayList<>();
        if(bids!=null) {
            for (BaseModel bid : bids) {
                dataModels.add((BidModel) bid);
            }
        }
       // dataModels= new ArrayList<>();


        adapter= new UserBidAdapter(dataModels,getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                BidModel dataModel= dataModels.get(position);

                Intent intent = new Intent(UserBidActivity.this,UserBidConversationActivity.class);
                intent.putExtra("bidId",dataModel.getBidId());
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






}
