package com.sourcey.movnpack.UserServiceProviderCommunication;



import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sourcey.movnpack.DataBase.DatabaseManager;
import com.sourcey.movnpack.LoginModule.SignupActivity;
import com.sourcey.movnpack.Model.AcceptedBidsModel;
import com.sourcey.movnpack.Model.BaseModel;
import com.sourcey.movnpack.Model.BidModel;
import com.sourcey.movnpack.Model.ConversationListViewModel;
import com.sourcey.movnpack.Model.ServiceProvider;
import com.sourcey.movnpack.Model.UserBidCounterModel;
import com.sourcey.movnpack.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.view.WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;


public class UserBidConversationActivity extends AppCompatActivity {


    TextView amountTextView;
    Button backBtn;

    Button viewBidDetailBtn;
    RelativeLayout mRelativeLayout;


    String message,date,amount,counterMessage;


    ArrayList<ConversationListViewModel> dataModels;
    ArrayList<BaseModel> bids;
    ListView listView;
    private static UserBidConversationAdapter adapter;
    AcceptedBidsModel selectedBidModel;

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
        date = bidModel.getDate();
        amount = bidModel.getAmount();




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
                    ConversationListViewModel c =  new ConversationListViewModel(a.getSpName(),"Accepted your offer",a.getDate(),"1",a.getSpToken(),"");
                    c.a = a;
                    c.setBidID(a.getBidId());
                    dataModels.add(c);
                }
                else {
                    UserBidCounterModel u = (UserBidCounterModel) bid;
                    dataModels.add(new ConversationListViewModel(u.getSpName(),"Countered Your Offer",u.getDate(),"2",u.getSpToken(),u.getAmount()));
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
                counterMessage=dataModel.getMessage();
                selectedBidModel = dataModel.getA();
                dialogBoxAccepted(view);


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





        viewBidDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialogBox(v);
            }
        });




    }



    /*private void initiatePopupWindow(View v) {


        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) UserBidConversationActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View customView = inflater.inflate(R.layout.view_bid_detail_popup,null);
            // create a 300px width and 470px height PopupWindow
            final PopupWindow mPopupWindow = new PopupWindow(customView, 600, 800, true);
            // display the popup in the center
            mPopupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
            mPopupWindow.setOutsideTouchable(true);

            mPopupWindow.setBackgroundDrawable(new ColorDrawable());

            // Set an elevation value for popup window
            // Call requires API level 21
            if(Build.VERSION.SDK_INT>=21){
                mPopupWindow.setElevation(5.0f);
            }


            TextView messageTextView = (TextView) customView.findViewById(R.id.message_text_view_popup);
            messageTextView.setText(message);
            TextView dateTextView = (TextView) customView.findViewById(R.id.date_text_view_popup);
            dateTextView.setText("Date: "+ date);
            TextView amountTextViewPopup = (TextView) customView.findViewById(R.id.amount_text_view_popup);
            amountTextViewPopup.setText("Amount: "+ amount);

           // Button cancelButton = (Button) layout.findViewById(R.id.end_data_send_button);
            //cancelButton.setOnClickListener(cancel_button_click_listener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
    private void dialogBox(View v){

        try {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.view_bid_detail_popup);

            dialog.setTitle("Bid Details");
            dialog.setCanceledOnTouchOutside(true);
            dialog.getWindow().setBackgroundDrawableResource(R.color.primary_dark);

            TextView messageTextView = (TextView) dialog.findViewById(R.id.message_text_view_popup);
            messageTextView.setText(message);
            TextView dateTextView = (TextView) dialog.findViewById(R.id.date_text_view_popup);
            dateTextView.setText("Date: "+ date);
            TextView amountTextViewPopup = (TextView) dialog.findViewById(R.id.amount_text_view_popup);
            amountTextViewPopup.setText("Amount: "+ amount);

            dialog.show();


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    ///

    private void dialogBoxAccepted(View v){

        try {

            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.sp_bid_response_user_counter);

            dialog.setTitle("Bid Details");
            dialog.setCanceledOnTouchOutside(true);
            dialog.getWindow().setBackgroundDrawableResource(R.color.primary_dark);

            TextView messageTextView = (TextView) dialog.findViewById(R.id.message_text_view);
            messageTextView.setText(counterMessage);
         /*   TextView dateTextView = (TextView) dialog.findViewById(R.id.date_text_view_popup);
            dateTextView.setText("Date: "+ date);
            TextView amountTextViewPopup = (TextView) dialog.findViewById(R.id.amount_text_view_popup);
            amountTextViewPopup.setText("Amount: "+ amount);

            */

          Button confirmServiceButton = (Button) dialog.findViewById(R.id.btn_confirm) ;

            confirmServiceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ServiceConfirmationActivity.class);

                    intent.putExtra("acceptedBid",(Parcelable) selectedBidModel);
                    intent.putExtra("bidId",selectedBidModel.getBidId());
                    startActivity(intent);
                }
            });
            dialog.show();


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    ////




}
