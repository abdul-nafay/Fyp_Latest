package com.sourcey.movnpack.UserServiceProviderCommunication;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sourcey.movnpack.DataBase.DatabaseManager;
import com.sourcey.movnpack.Model.AcceptedBidsModel;
import com.sourcey.movnpack.Model.BaseModel;
import com.sourcey.movnpack.Model.BidModel;
import com.sourcey.movnpack.Model.ConversationListViewModel;
import com.sourcey.movnpack.Model.UserBidCounterModel;
import com.sourcey.movnpack.R;

import java.util.ArrayList;


public class UserBidConversationActivity extends AppCompatActivity{


    TextView amountTextView;
    Button backBtn;

    Button viewBidDetailBtn;
    RelativeLayout mRelativeLayout;
    PopupWindow mPopupWindow;
    View customView;
    LayoutInflater inflater;

    String message,date,amount;


    ArrayList<ConversationListViewModel> dataModels;
    ArrayList<BaseModel> bids;
    ListView listView;
    private static UserBidConversationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bid_conversation);

        backBtn = (Button) findViewById(R.id.btn_back_activity);

        viewBidDetailBtn = (Button) findViewById(R.id.btn_view_bid_detail);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl_custom_layout);


        String bidId = getIntent().getStringExtra("bidId");




        amountTextView  = (TextView) findViewById(R.id.bid_amount_text_view);

        BidModel bidModel = (BidModel) DatabaseManager.getInstance(this).getBidById(bidId).get(0);
        amountTextView.setText(bidModel.getAmount());

        message = bidModel.getMessage();
       // date = bidModel.getDate();
        //amount = bidModel.getAmount();

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


        /////////


        viewBidDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                initiatePopupWindow(v);
            }
        });

        ////////


    }


    ////////////////




    private void initiatePopupWindow(View v) {



       // mRelativeLayout = (LinearLayout) findViewById(R.id.linearLayoutPopUp);
        //mRelativeLayout.setVisibility(View.GONE);

        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
           inflater = (LayoutInflater) UserBidConversationActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            // Inflate the custom layout/view
            customView = inflater.inflate(R.layout.view_bid_detail_popup,null);



            // create a 300px width and 470px height PopupWindow
            mPopupWindow = new PopupWindow(customView,600,800, true);
            // display the popup in the center
            mPopupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

           TextView dateTextView = (TextView) findViewById(R.id.date_text_view_popup);
            TextView messageTextView = (TextView) findViewById(R.id.message_text_view_popup);
            TextView scrollerAmountTextView = (TextView) findViewById(R.id.amount_text_view_popup);


            messageTextView.setText(message);
            //dateTextView.setText("Date: "+ date);
            //scrollerAmountTextView.setText("Amount: "+ amount);
            //Button cancelButton = (Button) layout.findViewById(R.id.end_data_send_button);
            //cancelButton.setOnClickListener(cancel_button_click_listener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    //////////////////////
}
