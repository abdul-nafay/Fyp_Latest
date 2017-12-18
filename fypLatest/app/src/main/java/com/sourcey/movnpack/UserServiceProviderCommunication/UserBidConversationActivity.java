package com.sourcey.movnpack.UserServiceProviderCommunication;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.sourcey.movnpack.DataBase.AcceptedBids;
import com.sourcey.movnpack.DataBase.DatabaseManager;
import com.sourcey.movnpack.Model.AcceptedBidsModel;
import com.sourcey.movnpack.Model.BaseModel;
import com.sourcey.movnpack.Model.BidModel;
import com.sourcey.movnpack.Model.ConversationListViewModel;
import com.sourcey.movnpack.Model.UserBidCounterModel;
import com.sourcey.movnpack.R;

import java.util.ArrayList;


public class UserBidConversationActivity extends AppCompatActivity {

   // TextView subjectTextView;
   // TextView messageTextView;
    TextView amountTextView;
    Button backBtn;

    ArrayList<ConversationListViewModel> dataModels;
    ArrayList<BaseModel> bids;
    ListView listView;
    private static UserBidConversationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bid_conversation);

        backBtn = (Button) findViewById(R.id.btn_back_activity);

        String bidId = getIntent().getStringExtra("bidId");

       // subjectTextView = (TextView) findViewById(R.id.subject_text_view);
       // messageTextView = (TextView) findViewById(R.id.message_text_view);
        amountTextView  = (TextView) findViewById(R.id.bid_amount_text_view);
        BidModel bidModel = (BidModel) DatabaseManager.getInstance(this).getBidById(bidId).get(0);
        amountTextView.setText(bidModel.getAmount());
        listView = (ListView)findViewById(R.id.list);
        bids = DatabaseManager.getInstance(this).getAcceptedBidId(bidId);
        ArrayList<BaseModel> counterBids = DatabaseManager.getInstance(this).getUserBidCounterById(bidId);
        if (counterBids != null){
            if (bids == null) {
                bids = new ArrayList<>();
            }
            bids.addAll(counterBids);
        }

        dataModels = new ArrayList<>();
        if(bids!=null) {
            for (BaseModel bid : bids) {
                if (bid instanceof AcceptedBidsModel) {
                    AcceptedBidsModel a = (AcceptedBidsModel) bid;
                    dataModels.add( new ConversationListViewModel(a.getSpName(),"Accepted your offer",a.getDate(),"1",a.getSpToken(),""));
                }
                else {
                    UserBidCounterModel u = (UserBidCounterModel) bid;
                    dataModels.add(new ConversationListViewModel(u.getSpName(),"Countered YOur Offer",u.getDate(),"2",u.getSpToken(),u.getAmount()));
                }

            }
        }
        // dataModels= new ArrayList<>();


        adapter= new UserBidConversationAdapter(dataModels,getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ConversationListViewModel dataModel= dataModels.get(position);


                Snackbar.make(view, dataModel.getName()+"\n"+dataModel.getMessage()+" date: "+dataModel.getDate(), Snackbar.LENGTH_LONG)
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
