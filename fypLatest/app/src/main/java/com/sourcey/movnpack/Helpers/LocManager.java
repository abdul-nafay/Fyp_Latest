package com.sourcey.movnpack.Helpers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by abdul on 12/10/17.
 */

public class LocManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static LocManager sharedInstance;
    LocChangeListener delegate;

    private LocManager() {
    }

    GoogleApiClient mGoogleApiClient;
    Context appContext;
    Location currentLocation;

    protected synchronized void buildGoogleApiClient(Context c) {
        mGoogleApiClient = new GoogleApiClient.Builder(c)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        System.out.println("ABC buildGoogleApiClient map was invoked: ");
    }

    public static void createInstance(Context c, LocChangeListener delegate) {
        sharedInstance = new LocManager();
        sharedInstance.appContext = c;
        sharedInstance.delegate = delegate;
        sharedInstance.buildGoogleApiClient(c);
        sharedInstance.mGoogleApiClient.connect();
    }

    public Location getLastLcoation() {
        if (ActivityCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        return LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            delegate.failedToGetLocationWithError("Enable your location First");
            return;
        }
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (currentLocation != null) {
            sharedInstance.delegate.locationDidChanged(currentLocation);
        }
        else {
            delegate.failedToGetLocationWithError("Unable to get Location");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        delegate.failedToGetLocationWithError("Unable to get Location");
    }
}
