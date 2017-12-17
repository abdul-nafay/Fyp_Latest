package com.sourcey.movnpack.UserServiceProviderCommunication;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.sourcey.movnpack.DataBase.DatabaseManager;
import com.sourcey.movnpack.Model.BaseModel;
import com.sourcey.movnpack.Model.BidModel;
import com.sourcey.movnpack.R;

import java.util.ArrayList;


public class UserBidConversationActivity extends AppCompatActivity {

   // TextView subjectTextView;
   // TextView messageTextView;
    TextView amountTextView;

    ArrayList<BidModel> dataModels;
    ArrayList<BaseModel> bids;
    ListView listView;
    private static UserBidConversationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bid_conversation);


       // subjectTextView = (TextView) findViewById(R.id.subject_text_view);
       // messageTextView = (TextView) findViewById(R.id.message_text_view);
        amountTextView  = (TextView) findViewById(R.id.bid_amount_text_view);

        listView = (ListView)findViewById(R.id.list);
        bids = DatabaseManager.getInstance(this).getBidsForUserId();

        dataModels = new ArrayList<>();
        if(bids!=null) {
            for (BaseModel bid : bids) {
                dataModels.add((BidModel) bid);
            }
        }
        // dataModels= new ArrayList<>();


        adapter= new UserBidConversationAdapter(dataModels,getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                BidModel dataModel= dataModels.get(position);


                Snackbar.make(view, dataModel.getCategoryName()+"\n"+dataModel.getMessage()+" date: "+dataModel.getDate(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
            }
        });




    }
}
