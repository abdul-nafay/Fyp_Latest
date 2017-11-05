package com.sourcey.movnpack.AppFragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sourcey.movnpack.Helpers.Session;
import com.sourcey.movnpack.R;
import com.sourcey.movnpack.Utility.Utility;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static com.sourcey.movnpack.R.id.map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SPHomeMapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SPHomeMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SPHomeMapFragment extends Fragment  implements LocationListener  ,OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private Location lastKnownLocation;
    private boolean isLocationSet;
    private Marker currentMarker;

    private LocationManager locationManager;
    private String provider;

    private GoogleMap mMap;

    private GeoFire geoFire;

    private OnFragmentInteractionListener mListener;

    public SPHomeMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SPHomeMapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SPHomeMapFragment newInstance(String param1, String param2) {
        SPHomeMapFragment fragment = new SPHomeMapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map1));

        ////
        mapFragment.getMapAsync(this);
        // Get the location manager
        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lastKnownLocation = locationManager.getLastKnownLocation(provider);

        if ( lastKnownLocation != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged( lastKnownLocation);
           // locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 0, 0, this);
            //locationManager.requestLocationUpdates();
            if( provider.equals("gps")) {
                locationManager.getAllProviders();
            }

        } else {
            lastKnownLocation = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
            if ( lastKnownLocation != null) {

                onLocationChanged( lastKnownLocation);
               // locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 0, 0, this);
                //locationManager.requestLocationUpdates();
                if( provider.equals("gps")) {
                    locationManager.getAllProviders();
                }

            }


        }

        ////


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_sp_home_map, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(lastKnownLocation != null){
            // Add a marker in Sydney and move the camera
            LatLng sydney = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());


            if(currentMarker != null){
                currentMarker.remove();
            }
            MarkerOptions marker = new MarkerOptions().position(sydney).title("Hello Maps");
// Changing marker icon
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_user));
            currentMarker= mMap.addMarker(marker);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            setCamera(lastKnownLocation);
            isLocationSet=true;
            currentMarker.setDraggable(true);

        }

        // Add a marker in Sydney and move the camera
     /*  LatLng sydney = new LatLng(24.9349491, 67.0974638);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Log.d(TAG, "latitude : "+ marker.getPosition().latitude);
                marker.setSnippet("");
                mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                updateLocationOnFireBase(marker.getPosition().latitude,marker.getPosition().longitude);
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

        });


    }


    @Override
    public void onLocationChanged(Location location) {

        lastKnownLocation = location;
        LatLng sydney = new LatLng(location.getLatitude()  , location.getLongitude());
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        if(isLocationSet){
            if(currentMarker != null){
                currentMarker.remove();

            }
            MarkerOptions marker = new MarkerOptions().position(sydney).title("Hello Maps");
// Changing marker icon
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_user));
            currentMarker= mMap.addMarker(marker);
            currentMarker.setDraggable(true);
            // setCamera(lastKnownLocation);
        }



////////////////////////////////// ********** changes by An *******///////////////////


        //  addOverlay(new LatLng(userLocation.getLatitude(), userLocation.getLongitude()));
        //mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

///////////////////////////////////////////////////////////////////////////////////////
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i("","Status Change");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i("","providerEnabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i("","Disabled");

        if(provider.equals("gps")){
            //Toast.makeText(getApplicationContext(), "GPS is off", Toast.LENGTH_LONG).show();

            // startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        Log.i("lm_disabled",provider);
    }



    public void setCamera(Location location){
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                .zoom(17)
                .build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    public void updateLocationOnFireBase(double lat , double longi)
    {
        //String path = Utility.getPathForServiceNamed(Session.getInstance().getServiceProvider())
        String path = Utility.getPathForServiceNamed(Utility.getCategoryNameFromServiceCategory(Session.getInstance().getServiceProvider().getCategory()));
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
        geoFire = new GeoFire(ref);

        //String number = Session.getInstance().getServiceProvider().getPhoneNumber();
        //String Cat = Utility.getCategoryNameFromServiceCategory(Session.getInstance().getServiceProvider().getCategory());

        geoFire.setLocation(Session.getInstance().getServiceProvider().getPhoneNumber(), new GeoLocation(lat, longi), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if (error != null) {
                    System.err.println("There was an error saving the location to GeoFire: " + error);
                } else {
                    System.out.println("Location saved on server successfully!");
                }
            }
        });
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
