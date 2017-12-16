package com.sourcey.movnpack.UserServiceProviderCommunication;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sourcey.movnpack.R;

import java.util.ArrayList;


public class UserBidActivity extends AppCompatActivity {


    ArrayList<BidViewModel> dataModels;
    ListView listView;
    private static UserBidAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bid);

        listView = (ListView)findViewById(R.id.list);

        dataModels= new ArrayList<>();
        dataModels.add(new BidViewModel("Apple Pie", "Android 1.0","September 23, 2008"));
        dataModels.add(new BidViewModel("Apple Pie", "Android 1.0","September 23, 2008"));
        dataModels.add(new BidViewModel("Apple Pie", "Android 1.0","September 23, 2008"));
        dataModels.add(new BidViewModel("Apple Pie", "Android 1.0","September 23, 2008"));
        dataModels.add(new BidViewModel("Apple Pie", "Android 1.0","September 23, 2008"));

        adapter= new UserBidAdapter(dataModels,getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                BidViewModel dataModel= dataModels.get(position);


                Snackbar.make(view, dataModel.getCatTitle()+"\n"+dataModel.getMessageInitials()+" date: "+dataModel.getDate(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
            }
        });

    }



}
