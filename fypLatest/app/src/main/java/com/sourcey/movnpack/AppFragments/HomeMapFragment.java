package com.sourcey.movnpack.AppFragments;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Interpolator;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.sourcey.movnpack.BidPlacementActivities.UserBidPlacementActivity;
import com.sourcey.movnpack.Helpers.Session;
import com.sourcey.movnpack.LoginModule.SignupActivity;
import com.sourcey.movnpack.Model.User;
import com.sourcey.movnpack.R;
import com.sourcey.movnpack.SP.spProfileInfo;
import com.sourcey.movnpack.Utility.MemorizerUtil;
import com.sourcey.movnpack.Utility.Utility;

import java.util.ArrayList;

import static com.google.firebase.database.Transaction.*;

//import com.sourcey.materiallogindemo.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeMapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

class HomeFragmentButtonTags extends Object {

     public static final String Cargo = "Cargo";
    public static final String Mandi = "Mandi";
    public static final String Labour = "Labour";

}
class CategoryButtonsUI extends Object {
    public static Button previousButton;
    public static Button currentButton;
    public static void toggleSelection() {

        if (previousButton != null) {
            //CategoryButtonsUI.previousButton.setBackgroundColor(Color.parseColor("#6ec6ff"));
            CategoryButtonsUI.previousButton.setTextColor(Color.WHITE);
            CategoryButtonsUI.previousButton.setSelected(true);
        }
        //CategoryButtonsUI.currentButton.setBackgroundColor(Color.RED);
        CategoryButtonsUI.currentButton.setTextColor(Color.parseColor("#0069c0"));
        CategoryButtonsUI.currentButton.setHighlightColor(Color.parseColor("#0069c0"));
    }

}


public class HomeMapFragment extends Fragment  implements OnMapReadyCallback, LocationListener ,  GoogleApiClient.ConnectionCallbacks  , GoogleApiClient.OnConnectionFailedListener , View.OnClickListener, GoogleMap.OnInfoWindowClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;


    private Location lastKnownLocation;
    private boolean isLocationSet;
    private Marker currentMarker;
    private ArrayList<Marker> serviceMarkers;

    private LocationManager locationManager;
    private String provider;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private GeoFire geoFire;
    private OnFragmentInteractionListener mListener;

    private Button cargoButton;
    private Button mandiButton;
    private Button labourButton;
    private Button picnicButton;
    private Button packingButton;
    private Button plumberButton;
    private Button electricianButton;
    private FloatingActionButton messageButton;
    private TextView userNameTextView;
    private TextView userNumberTextView;
    public HomeMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeMapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeMapFragment newInstance(String param1, String param2) {
        HomeMapFragment fragment = new HomeMapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        serviceMarkers = new ArrayList<Marker>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_home_map, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
         cargoButton = (Button) getView().findViewById(R.id.cargoBtn);
         mandiButton = (Button) getView().findViewById(R.id.mandiBtn);
         labourButton = (Button) getView().findViewById(R.id.labourBtn);
        picnicButton = (Button) getView().findViewById(R.id.picnicBtn);
        packingButton = (Button) getView().findViewById(R.id.packingBtn);
        plumberButton = (Button) getView().findViewById(R.id.plumberBtn);
        electricianButton = (Button) getView().findViewById(R.id.electricianBtn);

        messageButton = (FloatingActionButton) getView().findViewById(R.id.message_button);

        cargoButton.setOnClickListener((View.OnClickListener) this);
        mandiButton.setOnClickListener((View.OnClickListener) this);
        labourButton.setOnClickListener((View.OnClickListener) this);
        picnicButton.setOnClickListener((View.OnClickListener) this);
        packingButton.setOnClickListener((View.OnClickListener) this);
        plumberButton.setOnClickListener((View.OnClickListener) this);
        electricianButton.setOnClickListener((View.OnClickListener) this);

        cargoButton.performClick();

        messageButton.setOnClickListener((View.OnClickListener) this);
        // mapFragment.getMapAsync(this);
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

        // Initialize the location fields

        if ( lastKnownLocation != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged( lastKnownLocation);
            locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 0, 0, this);
            //locationManager.requestLocationUpdates();
            if( provider.equals("gps")) {
                locationManager.getAllProviders();
            }

        } else {
           // locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 0, 0, this);
            buildGoogleApiClient();
            mGoogleApiClient.connect();
        }


      //  setupUIForDrawer();
    }

    public void setupUIForDrawer() {
        User user = Session.getInstance().getUser();
        if (user != null) { // User cannot be null
            NavigationView navigationView = (NavigationView) getView().findViewById(R.id.nav_view);
            View header=navigationView.getHeaderView(0);
/*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
            ///
            userNameTextView = (TextView)header.findViewById(R.id.drawer_username);
            userNumberTextView = (TextView)header.findViewById(R.id.drawer_usernumber);

            userNameTextView.setText(user.getName());
            userNumberTextView.setText(user.getPhoneNumber());

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
    public void onConnected(@Nullable Bundle bundle) {


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (lastKnownLocation != null) {
            LatLng sydney = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            if(currentMarker != null){
                currentMarker.remove();
            }
            MarkerOptions marker = new MarkerOptions().position(sydney).title("Hello Maps");
// Changing marker icon
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_user));
            currentMarker= mMap.addMarker(marker);
            currentMarker.setTag("Self");
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            setCamera(lastKnownLocation);
            isLocationSet=true;
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("","");
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this.getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        System.out.println("ABC buildGoogleApiClient map was invoked: ");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    Log.i("","");
    }

    @Override
    public void onClick(View v) {

        if (messageButton.getId() == v.getId()){
            Intent intent = new Intent(getContext(), UserBidPlacementActivity.class);
            intent.putExtra("categoryName",CategoryButtonsUI.currentButton.getText().toString());
            startActivity(intent);
        }

        if (v instanceof Button && v.getTag() != null && ((String)v.getTag()) != "") {
            String tag = (String) v.getTag();
            /*switch (tag) {
                case HomeFragmentButtonTags.Cargo:
                    break;
                case HomeFragmentButtonTags.Labour:
                    break;
                case HomeFragmentButtonTags.Mandi:
                    break;
                default:
                    break;
            }*/
            geoFireSetReferenceForServiceNamed(tag);

            //forUI
            updateUIForSelectedCategory((Button)v);
            //
        }

    }

    public void updateUIForSelectedCategory(Button currentButton) {
        CategoryButtonsUI.previousButton = CategoryButtonsUI.currentButton;
        CategoryButtonsUI.currentButton = currentButton;
        CategoryButtonsUI.toggleSelection();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        String number = marker.getTag().toString();
        MemorizerUtil.displayToast(getContext(),number);

        //spProfileInfo.SP_ProfileInfo.execute(number);
      //  new spProfileInfo(number).execute();

        Intent intent = new Intent(getContext(), spProfileInfo.class);
        intent.putExtra("number",number);
        startActivity(intent);
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
            currentMarker.setTag("Self");
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            setCamera(lastKnownLocation);
            isLocationSet=true;

        }
        mMap.setOnInfoWindowClickListener(this);
        // Add a marker in Sydney and move the camera
     /*  LatLng sydney = new LatLng(24.9349491, 67.0974638);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
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
            currentMarker.setTag("Self");
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
                .zoom(5)
                .build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    public void geoFireSetReferenceForServiceNamed(String service) {

        for (Marker smarker : serviceMarkers) {
            smarker.remove();
        }

        serviceMarkers  = new ArrayList<Marker>();

        String path = Utility.getPathForServiceNamed(service);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
        geoFire = new GeoFire(ref);


        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(24.9144, 67.0926), 1000);


        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                System.out.println(String.format("Key %s entered the search area at [%f,%f]", key, location.latitude, location.longitude));
                MarkerOptions markerOpt = new MarkerOptions().position(new LatLng(location.latitude,location.longitude)).title(key);
                markerOpt.icon(BitmapDescriptorFactory.fromResource(R.drawable.pink_car));
                Marker marker = mMap.addMarker(markerOpt);
                marker.setTag(key);
                serviceMarkers.add(marker);
            }

            @Override
            public void onKeyExited(String key) {
                System.out.println(String.format("Key %s is no longer in the search area", key));
                for (Marker smarker : serviceMarkers) {
                    if (smarker.getTag().equals(key)) {
                        smarker.remove();
                        serviceMarkers.remove(smarker);
                        break;
                    }
                }
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                System.out.println(String.format("Key %s moved within the search area to [%f,%f]", key, location.latitude, location.longitude));
                for (Marker marker : serviceMarkers) {
                    if (marker.getTag().equals(key)) {
                        animateMarkerNew(location,marker);
                        break;
                    }
                }
            }

            @Override
            public void onGeoQueryReady() {
                System.out.println("All initial data has been loaded and events have been fired!");
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                System.err.println("There was an error with this query: " + error);
            }
        });


    }

    public void getLocationOfSpecificKey(){
        geoFire.getLocation("firebase-hq", new LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation location) {
                if (location != null) {
                    System.out.println(String.format("The location for key %s is [%f,%f]", key, location.latitude, location.longitude));
                } else {
                    System.out.println(String.format("There is no location for key %s in GeoFire", key));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("There was an error getting the GeoFire location: " + databaseError);
            }
        });
    }

    public void updateCurrentLocationOnGeoFire()
    {
        //  if ( Session.getInstance().getUser() != null && Session.getInstance().getUser().isServiceProvider())
        // {
        geoFire.setLocation("Ali", new GeoLocation(37.7853889, -122.4056973), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if (error != null) {
                    System.err.println("There was an error saving the location to GeoFire: " + error);
                } else {
                    System.out.println("Location saved on server successfully!");
                }
            }
        });
        // }
    }

    public void addDummyLocations()
    {
        String path = Utility.getPathForServiceNamed(HomeFragmentButtonTags.Cargo);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
        geoFire = new GeoFire(ref);

        geoFire.setLocation("Cargo1", new GeoLocation(24.9044, 67.1136), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if (error != null) {
                    System.err.println("There was an error saving the location to GeoFire: " + error);
                } else {
                    System.out.println("Location saved on server successfully!");
                }
            }
        });

        geoFire.setLocation("Cargo2", new GeoLocation(24.9144, 67.0926), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if (error != null) {
                    System.err.println("There was an error saving the location to GeoFire: " + error);
                } else {
                    System.out.println("Location saved on server successfully!");
                }
            }
        });

        geoFire.setLocation("Cargo3", new GeoLocation(24.9338, 66.9561), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if (error != null) {
                    System.err.println("There was an error saving the location to GeoFire: " + error);
                } else {
                    System.out.println("Location saved on server successfully!");
                }
            }
        });



        String path1 = Utility.getPathForServiceNamed(HomeFragmentButtonTags.Mandi);

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference(path1);
        geoFire = new GeoFire(ref1);

        geoFire.setLocation("Mandi1", new GeoLocation(24.9438, 67.0845), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if (error != null) {
                    System.err.println("There was an error saving the location to GeoFire: " + error);
                } else {
                    System.out.println("Location saved on server successfully!");
                }
            }
        });

        geoFire.setLocation("Mandi2", new GeoLocation(24.95, 67.2167), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if (error != null) {
                    System.err.println("There was an error saving the location to GeoFire: " + error);
                } else {
                    System.out.println("Location saved on server successfully!");
                }
            }
        });

        geoFire.setLocation("Mandi3", new GeoLocation(24.8485, 67.0889), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if (error != null) {
                    System.err.println("There was an error saving the location to GeoFire: " + error);
                } else {
                    System.out.println("Location saved on server successfully!");
                }
            }
        });


        String path2 = Utility.getPathForServiceNamed(HomeFragmentButtonTags.Labour);
        DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference(path2);
        geoFire = new GeoFire(ref3);

        geoFire.setLocation("Labour1", new GeoLocation(24.8516, 67.0007), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if (error != null) {
                    System.err.println("There was an error saving the location to GeoFire: " + error);
                } else {
                    System.out.println("Location saved on server successfully!");
                }
            }
        });

        geoFire.setLocation("Labour2", new GeoLocation(24.9421, 67.0709), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if (error != null) {
                    System.err.println("There was an error saving the location to GeoFire: " + error);
                } else {
                    System.out.println("Location saved on server successfully!");
                }
            }
        });


        geoFire.setLocation("Labour3", new GeoLocation(24.8294, 67.0738), new GeoFire.CompletionListener() {
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

    private void animateMarkerNew(final GeoLocation destination, final Marker marker) {

        if (marker != null) {

            final LatLng startPosition = marker.getPosition();
            final LatLng endPosition = new LatLng(destination.latitude, destination.longitude);

            final float startRotation = marker.getRotation();
            final LatLngInterpolatorNew latLngInterpolator = new LatLngInterpolatorNew.LinearFixed();

            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(3000); // duration 3 second
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        float v = animation.getAnimatedFraction();
                        LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
                        marker.setPosition(newPosition);
                        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                                .target(newPosition)
                                .zoom(5.5f)
                                .build()));

                        marker.setRotation(getBearing(startPosition, new LatLng(destination.latitude, destination.longitude)));
                    } catch (Exception ex) {
                        //I don't care atm..
                    }
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    // if (mMarker != null) {
                    // mMarker.remove();
                    // }
                    // mMarker = googleMap.addMarker(new MarkerOptions().position(endPosition).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_car)));

                }
            });
            valueAnimator.start();
        }
    }


    private interface LatLngInterpolatorNew {
        LatLng interpolate(float fraction, LatLng a, LatLng b);

        class LinearFixed implements LatLngInterpolatorNew {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                double lngDelta = b.longitude - a.longitude;
                // Take the shortest path across the 180th meridian.
                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }
    }


    //Method for finding bearing between two points
    private float getBearing(LatLng begin, LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }

}


