package com.example.clifners6171.mapapp2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.location.LocationManager.*;
import static android.location.LocationManager.NETWORK_PROVIDER;
import static com.example.clifners6171.mapapp2.R.styleable.View;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private boolean canGetLocation = false;
    private static final long MIN_TIME_BN_UPDATES = 1000 * 15 * 1;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 5;
    private Location myLocation;
    private static final int MY_LOC_ZOOM_FACTOR = 17;
    int color = Color.BLUE;
    List<Circle> markerList = new ArrayList<Circle>();
    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in place born and move the camera
        LatLng born = new LatLng(32.7157, -117.1611);
        mMap.addMarker(new MarkerOptions().position(born).title("Born here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(born));

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(false); //used to be true
        markerList = new ArrayList<Circle>();
        getLocation();

    }

    public void mapView(View v) {
        if (mMap.getMapType() == GoogleMap.MAP_TYPE_SATELLITE)
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        else
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    public void findPOI(View v) throws IOException {
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        if (message.length() > 0) {

            //finds location of the search, adds circle to that place on map
            geocoder = new Geocoder(this, Locale.getDefault());

            //get own location
            if (isNetworkEnabled) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                myLocation = locationManager.getLastKnownLocation(NETWORK_PROVIDER);
            }
            else if (isGPSEnabled) {
                myLocation = locationManager.getLastKnownLocation(GPS_PROVIDER);
            }
            List<Address> adresses = geocoder.getFromLocationName(message, 11, myLocation.getLatitude() -.0361, myLocation.getLongitude() -.036, myLocation.getLatitude() +.0361, myLocation.getLongitude() +.036);
            if(myLocation != null)
                for (Address address : adresses) {
                    color = Color.DKGRAY;
                    LatLng ad = new LatLng(address.getLatitude(), address.getLongitude());
                    Circle circle = mMap.addCircle(new CircleOptions().center(ad).radius(100).strokeColor(Color.BLACK).fillColor(color));
                    markerList.add(circle);
                }
            else Toast.makeText(this, "No location found", Toast.LENGTH_SHORT);  //add .show() to the end?

        }
        else Toast.makeText(this, "No text entered", Toast.LENGTH_SHORT);
    }

    public void clearMarkers(View v) {
        mMap.clear();
        markerList.clear();

    }

    public void trackerOnOff () {
        /*Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
        if(!isGPSEnabled) {
            //intent.putExtra("enabled", true);
            //sendBroadcast(intent);
            isGPSEnabled = true;
        } else {
            //intent.putExtra("enabled", false);
            //sendBroadcast(intent);
            //locationManager.removeUpdates(locationListenerGps);
            isGPSEnabled = false;
        }*/
        //locationManager.removeUpdates (locationListenerGps);


    }


    public void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            isGPSEnabled = locationManager.isProviderEnabled(GPS_PROVIDER);
            if (isGPSEnabled) Log.d("MyMaps", "getLocation: GPS is enabled");

            //get network status
            isNetworkEnabled = locationManager.isProviderEnabled(NETWORK_PROVIDER);
            if (isNetworkEnabled) Log.d("MyMaps", "getLocation: NETWORK is enabled");

            if (!isGPSEnabled && !isNetworkEnabled) {
                Log.d("MyMaps", "getLocation: no provider is enabled");
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    Log.d("MyMaps", "getLocation:Network enabled - requesting location updates");
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationManager.requestLocationUpdates(NETWORK_PROVIDER, MIN_TIME_BN_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListenerNetwork);

                    Log.d("MyMaps", "getLocation:NetworkLoc update request successful");
                    Toast.makeText(this, "Using Network", Toast.LENGTH_SHORT);
                }
            }


            if (isGPSEnabled) { // alt enter this and add permission check
                Log.d("MyMaps", "getLocation: GPS enabled - requesting location updates");
                locationManager.requestLocationUpdates(GPS_PROVIDER, MIN_TIME_BN_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListenerGps);

                Log.d("MyMaps", "getLocation: GPSLoc update request successful");
                Toast.makeText(this, "Using Network", Toast.LENGTH_SHORT);
            }

        } catch (Exception e) {
            Log.d("MyMaps", "Caught exception in getLocation");
            e.printStackTrace();
        }
    }

    Context context = this;


    android.location.LocationListener locationListenerGps = new android.location.LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //output in Log.d and Toast that GPS is enabled and working
            Log.d("MyMaps", "GPS enabled and working");
            Toast.makeText(context, "GPS enabled and working", Toast.LENGTH_SHORT);

            //drop a marker on map - create a method called dropAmarker (not a blue dot)
            color = Color.RED;
            dropMarker(locationManager.GPS_PROVIDER);

            //remove the network locatoin updates - see the locatoinManager for update removal method
            locationManager.removeUpdates(locationListenerNetwork);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //output in Log.d and Toast that GPS is enabled and working
            Log.d("MyMaps", "GPS enabled and working");

            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("MyMaps", "location provider available");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("MyMaps", "location provider not available");

                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationManager.requestLocationUpdates(NETWORK_PROVIDER, MIN_TIME_BN_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListenerNetwork);
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("MyMaps", "location provide not currently available");
                    locationManager.requestLocationUpdates(NETWORK_PROVIDER, MIN_TIME_BN_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListenerNetwork);
                    break;
                default:
                    Log.d("MyMaps", "location provide not currently available");
                    locationManager.requestLocationUpdates(NETWORK_PROVIDER, MIN_TIME_BN_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListenerNetwork);
                    break;
            }

            //setup a switch statement to check the status input parameter
            //case LocationProvider.AVAIALABLE → output message to Log.d and Toast
            //case LocatoinProvider.OUT_OF_SERVICE -- output message, request updates from NETWORK_PROVIDER (copy and paste the MINTIME code thing)
            //case LocationProvider.TEMPORARILY_UNAVAILABLE -- messgae, request updates from NETWORK_PROVIDER
            // default case -- same ^^
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

    };


    android.location.LocationListener locationListenerNetwork = new android.location.LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //output in Log.d and Toast that GPS is enabled and working
            Log.d("MyMaps", "Network enabled and working");
            Toast.makeText(context, "Network enabled and working", Toast.LENGTH_SHORT);

            //drop a marker on map - create a method called dropAmarker (not a blue dot)
            color = Color.BLACK;
            dropMarker(NETWORK_PROVIDER);


            //remove the network locatoin updates - see the locatoinManager for update removal method
            //I don't think I have to do that on this one
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //output in Log.d and Toast that GPS is enabled and working
            Log.d("MyMaps", "Network enabled and working");
        }

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}


        //get another LocationListener, but with Network. Do same type of thing for each method, except for onLocationChanged, also add relaunch the network provider request (requestLocatoinUpdates (NETWORK_PROVIDER) )
        //for statusChange, just output a message and ignore the switch


    };


    public void dropMarker(String provider) {
        LatLng userLocation = new LatLng(0,0);

        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            myLocation = locationManager.getLastKnownLocation(provider);
            if (myLocation == null) {
                //display message via Log.d and/or Toast
                Log.d("MyMaps", "no location");
            } else {
                Log.d("MyMaps", "location found");

                Log.d("MyMaps", String.valueOf(myLocation.getLatitude()));
                userLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());

                //display message with lat/long
                Log.d("MyMaps", userLocation.toString());
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(userLocation, MY_LOC_ZOOM_FACTOR);

                //drop the actual marker
                Circle circle = mMap.addCircle(new CircleOptions().center(userLocation).radius(10).strokeColor(Color.GREEN).fillColor(color));
                markerList.add(circle);
                mMap.animateCamera(update);

            }
        } else
            Log.d("MyMaps", "no location manager found");
    }

}



