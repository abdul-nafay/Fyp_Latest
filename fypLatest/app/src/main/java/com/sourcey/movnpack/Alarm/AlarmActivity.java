package com.sourcey.movnpack.Alarm;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sourcey.movnpack.DrawerModule.DrawerActivity;
import com.sourcey.movnpack.R;
import com.sourcey.movnpack.UserServiceProviderCommunication.UserTaskDetailActivity;

public class AlarmActivity extends Activity {

    String taskID;
    String bidID;
    MediaPlayer mp;
    Button viewDetailsButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alarm);
        //viewDetailsButton = (Button) findViewById(R.id.button_alarm);
        taskID = getIntent().getStringExtra("taskID");
        bidID = getIntent().getStringExtra("bidID");
        viewDetailsButton = (Button) findViewById(R.id.button_alarm_details);
        viewDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserTaskDetailActivity.class);
                intent.putExtra("cbmID",taskID);
                intent.putExtra("bidID",bidID);
                startActivity(intent);
            }
        });
        if (taskID == null) {
            finish();
        }
        AudioPlayer.getInstance().play(R.raw.pirates);
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent( getApplicationContext(), DrawerActivity.class );
        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
        this.startActivity( intent );
    }

    @Override
    protected void onPause() {
        super.onPause();
        AudioPlayer.getInstance().stop();
    }
}
