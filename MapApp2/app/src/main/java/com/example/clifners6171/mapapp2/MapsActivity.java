package com.example.clifners6171.mapapp2;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.example.clifners6171.mapapp2.R.styleable.View;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    //private LocationManager locationManager;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private boolean canGetLocation = false;
    private static final long MIN_TIME_BN_UPDATES = 1000*15*1;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 5;

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
        mMap.setMyLocationEnabled(true);

        }
    }

    public void mapView(View v) {
        if (mMap.getMapType() == GoogleMap.MAP_TYPE_SATELLITE)
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        else
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }


    /*
    public void getLocation() {
		try{
			LocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

			isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			if(isGPSEnabled) Log.d(“MyMaps”, “getLocation: GPS is enabled”);

			//get network status
			isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			if(isNetworkEnabled) Log.d(“MyMaps”, “getLocation: NETWORK is enabled”);

			if(!isGPSEnabled && !isNetworkEnabled) {
				Log.d(“MyMaps”, “getLocation: no provider is enabled”);
}
			else {
				this.canGetLocation = true;
if(isNetworkEnabled) {
	Log.d(“MyMaps”, “getLocation: Network enabled - requesting location updates”);
	locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BN_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListenerNetwork);

Log.d(“MyMaps”, “getLocation: NetworkLoc update request successful”);
Toast.makeText(this, “Using Network”, Toast.LENGTH_SHORT);
}


if(isGPSEnabled) { // alt enter this and add permission check
	Log.d(“MyMaps”, “getLocation: GPS enabled - requesting location updates”);
	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BN_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListenerGPS);

Log.d(“MyMaps”, “getLocation: GPSLoc update request successful”);
Toast.makeText(this, “Using Network”, Toast.LENGTH_SHORT);
}

		} catch (Exception e){
			Log.d(“MyMaps”, “Caught exception in getLocation”);
			e.printStackTrace();
}


android.location.LocationListener locationListenerGps = new android.location.LocationListener() {
@Override
public void onLocationChanged (Location location) {
	//output in Log.d and Toast that GPS is enabled and working

	//drop a marker on map - create a method called dropAmarker (not a blue dot)

	//remove the network locatoin updates - see the locatoinManager for update removal method
}

@Override
public void onStatusChanged (String provider, int status, Bundle extras) {
	//output in Log.d and Toast that GPS is enabled and working

	//setup a switch statement to check the status input parameter
//case LocationProvider.AVAIALABLE → output message to Log.d and Toast
	//case LocatoinProvider.OUT_OF_SERVICE -- output message, request updates from NETWORK_PROVIDER (copy and paste the MINTIME code thing)
	//case LocationProvider.TEMPORARILY_UNAVAILABLE -- messgae, request updates from NETWORK_PROVIDER
	// default case -- same ^^
}

@Override
Public void onProviderEnabled (String provider (0 {}

@Override
Public void onProviderDisabled (String provider) {}

//get another LocationListener, but with Network. Do same type of thing for each method, except for onLocationChanged, also add relaunch the network provider request (requestLocatoinUpdates (NETWORK_PROVIDER) )
//for statusChange, just output a message and ignore the switch



     */
}
