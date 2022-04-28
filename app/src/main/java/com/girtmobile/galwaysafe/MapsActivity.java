package com.girtmobile.galwaysafe;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.girtmobile.galwaysafe.databinding.ActivityMapsBinding;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity{

    private static final String TAG = "MapsActivity";

    public GoogleMap mMap;
    private GeofencingClient geofencingClient;
    private ActivityMapsBinding binding;
    private GeofenceHelper geofenceHelper;

    private final int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private final int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;
    private float geofence_radius = 130;
    private String GEOFENCE_ID = "0";

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    String lat;
    String provider;
    protected String latitude,longitude;
    protected boolean gps_enabled,network_enabled;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this::onMapReady);


        //if(!foregroundServiceRunning()) {
          //  Intent serviceIntent = new Intent(this, ForegroundService.class);
            //startForegroundService(serviceIntent);
        //}
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





    public LatLng Galway = new LatLng(53.269122246684084, -9.051582322744828);
    public LatLng Galway1 = new LatLng(53.27110399498937, -9.055877879188833);
    public LatLng Galway2 = new LatLng(53.272861986591536, -9.056156828921345);
    public LatLng Galway3 = new LatLng(53.274858, -9.05532);
    public LatLng Galway4 = new LatLng(53.27716927176104, -9.055142985982023);
    public LatLng Galway5 = new LatLng(53.269679803239256, -9.055457204652408);

    //53.26055655090345, -9.075464705096199

    public ArrayList<Geofence> geofenceList = new ArrayList<>();


    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Galway and move the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Galway, 17));
        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setTrafficEnabled(true);


        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            //ask for geolocation permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
        }

    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (Build.VERSION.SDK_INT >= 29) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
                    }
                }
                else Setup();

            }

        }


        if (requestCode == BACKGROUND_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //We have  permission
                //mMap.setMyLocationEnabled(true);
                Setup();

                //Toast.makeText(this, "You can see geofences...", Toast.LENGTH_SHORT).show();
            }
        }


    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        Toast.makeText(getApplicationContext(), "THE APP WON'T BE WORKING WITH GEOLOCATION OFF", Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }



    public void Setup() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        mMap.setMyLocationEnabled(true);

        geofence_radius = 170;
        handleMapLongClick(Galway);
        GEOFENCE_ID = "1";

        geofence_radius = 70;
        handleMapLongClick(Galway1);
        GEOFENCE_ID = "2";

        geofence_radius = 130;
        handleMapLongClick(Galway2);
        GEOFENCE_ID = "3";
        handleMapLongClick(Galway3);
        GEOFENCE_ID = "4";
        handleMapLongClick(Galway4);

        geofence_radius = 100;
        GEOFENCE_ID = "5";
        handleMapLongClick(Galway5);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) Toast.makeText(this, "GEOFENCES ACTIVATED", Toast.LENGTH_SHORT).show();
        else
        {
            Toast.makeText(this, "APP WON'T BE WORKING WITH GEOLOCATION OFF", Toast.LENGTH_LONG).show();
            buildAlertMessageNoGps();
        }



    }


    private void handleMapLongClick(LatLng latLng) {
        addMarker(latLng);
        addCircle(latLng, geofence_radius);
        //getGeofencingRequest(latLng, geofence_radius);
       // addGeofence(latLng, geofence_radius, GEOFENCE_ID);
    }


    public void addMarker(@NonNull LatLng latLng) {
        MarkerOptions markeroptions = new MarkerOptions().position(latLng);
        mMap.addMarker(markeroptions);
    }


    public void addCircle(@NonNull LatLng latLng, float radius) {
        CircleOptions circleoptions = new CircleOptions();
        circleoptions.center(latLng);
        circleoptions.radius(radius);
        circleoptions.strokeColor(Color.argb(255, 255, 0, 0));
        circleoptions.fillColor(Color.argb(64, 255, 0, 0));
        circleoptions.strokeWidth(4);

        mMap.addCircle(circleoptions);
    }



}
