package com.sourcey.movnpack.DrawerModule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sourcey.movnpack.AppFragments.HomeMapFragment;
import com.sourcey.movnpack.AppFragments.SPHomeMapFragment;
import com.sourcey.movnpack.AppFragments.SettingsFragment;
import com.sourcey.movnpack.Helpers.Session;
import com.sourcey.movnpack.LoginModule.LoginActivity;
import com.sourcey.movnpack.Model.ServiceProvider;
import com.sourcey.movnpack.Model.User;
import com.sourcey.movnpack.R;
import com.sourcey.movnpack.Utility.Utility;

public class SPDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SPHomeMapFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener {
    private TextView userNameTextView;
    private TextView userNumberTextView;
    private GeoFire geoFire;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawersp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        if (savedInstanceState == null) {
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = SPHomeMapFragment.class;

            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        }

      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;

        if (id == R.id.nav_home) {
            fragmentClass = SPHomeMapFragment.class;
        } else if (id == R.id.nav_settings) {
            fragmentClass = SettingsFragment.class;
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_TimeIn) {

            String path = Utility.getPathForServiceNamed("Cargo");
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
            geoFire = new GeoFire(ref);
            User user = Session.getInstance().getUser();
            String number = "";
            if (user != null){
                number = user.getPhoneNumber();
            }
            else {
                number = "923362600692";
            }
            geoFire.setLocation(number, new GeoLocation(32.0908, 68.9652), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, DatabaseError error) {
                    if (error != null) {
                        System.err.println("There was an error saving the location to GeoFire: " + error);
                    } else {
                        System.out.println("Location saved on server successfully!");
                    }
                }
            });
            return true ;
        } else if (id == R.id.nav_TimeOut) {

            String path = Utility.getPathForServiceNamed("Cargo");
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
            geoFire = new GeoFire(ref);
            User user = Session.getInstance().getUser();
            String number = "";
            if (user != null){
                number = user.getPhoneNumber();
            }
            else {
                number = "923362600692";
            }
            geoFire.setLocation(number, new GeoLocation(0, 0), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, DatabaseError error) {
                    if (error != null) {
                        System.err.println("There was an error saving the location to GeoFire: " + error);
                    } else {
                        System.out.println("Location saved on server successfully!");
                    }
                }
            });
            return true;
            //Logout COmmented
            /*
            SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref",MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("email",null);
            editor.commit();
            ServiceProvider serviceProvider = new ServiceProvider();
            serviceProvider.setEmail("");
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
            */

        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void setupUIForDrawer() {
        User user = Session.getInstance().getUser();
        if (user != null) { // User cannot be null
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View header=navigationView.getHeaderView(0);
/*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
            ///
            userNameTextView = (TextView)header.findViewById(R.id.drawer_username);
            userNumberTextView = (TextView)header.findViewById(R.id.drawer_usernumber);

            userNameTextView.setText(user.getName());
            userNumberTextView.setText(user.getPhoneNumber());

        }
    }
}
