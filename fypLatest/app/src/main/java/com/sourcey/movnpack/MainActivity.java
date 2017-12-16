package com.sourcey.movnpack;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sourcey.movnpack.DataBase.DatabaseManager;
import com.sourcey.movnpack.DrawerModule.DrawerActivity;
import com.sourcey.movnpack.DrawerModule.SPDrawerActivity;
import com.sourcey.movnpack.Helpers.Session;
import com.sourcey.movnpack.LoginModule.LoginActivity;
import com.sourcey.movnpack.Model.ServiceProvider;
import com.sourcey.movnpack.Model.User;
import com.sourcey.movnpack.UserServiceProviderCommunication.UserBidActivity;

import java.util.HashMap;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref",MODE_PRIVATE);
        String hashString = preferences.getString("HashString",null);



        if (hashString != null){

            Gson gson = new Gson();
            java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>(){}.getType();
            HashMap<String, String> testHashMap = gson.fromJson(hashString, type);

            String typ = testHashMap.get("Type").toString();
            String email = testHashMap.get("Email").toString();

            if (typ.equals("0")) {

                User user = DatabaseManager.getInstance(getApplicationContext()).getUser(email);
                if (user != null) {
                    //Intent intent = new Intent(this, HomeMapActivity.class);
                    Session.getInstance().setUser(user);
                    Intent intent = new Intent(this, DrawerActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
            else {
                ServiceProvider serviceProvider = DatabaseManager.getInstance(getApplicationContext()).getServiceProvider(email);
                if (serviceProvider != null){

                    Session.getInstance().setServiceProvider(serviceProvider);
                    Intent intent = new Intent(this, SPDrawerActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
        else {
           Intent intent = new Intent(this, LoginActivity.class);
            //Intent intent = new Intent(this, UserBidActivity.class);

            startActivity(intent);
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
