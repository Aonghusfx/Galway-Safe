package com.girtmobile.galwaysafe;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Random;

public class ForegroundService extends Service implements LocationListener {

    private final IBinder binder = new LocalBinder();


    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    String provider;
    protected boolean gps_enabled, network_enabled;

    public int test = 0;

    private GeofencingClient geofencingClient;
    private GeofenceHelper geofenceHelper;
    public ArrayList<Geofence> geofenceList = new ArrayList<>();


    public LatLng Galway = new LatLng(53.269122246684084, -9.051582322744828);
    public LatLng Galway1 = new LatLng(53.27110399498937, -9.055877879188833);
    public LatLng Galway2 = new LatLng(53.272861986591536, -9.056156828921345);
    public LatLng Galway3 = new LatLng(53.274858, -9.05532);
    public LatLng Galway4 = new LatLng(53.27716927176104, -9.055142985982023);
    public LatLng Galway5 = new LatLng(53.269679803239256, -9.055457204652408);


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Intent intent1 = new Intent(this, AmberActivity.class);
        //intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //startActivity(intent1);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);



         //geofences won't be working if geolocation is off so check if it's enable





        final String CHANNELID = "Foreground Service ID";
        NotificationChannel channel = new NotificationChannel(
                CHANNELID,
                CHANNELID,
                NotificationManager.IMPORTANCE_LOW
        );

        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notification = new Notification.Builder(this, CHANNELID)
                .setContentText("geolocation listening...")
                .setContentTitle("your safe is our priority")
                .setSmallIcon(R.drawable.notificationicon);

        startForeground(1001, notification.build());




        return super.onStartCommand(intent, flags, startId);
    }

    //@Nullable
    //@Override
    //public IBinder onBind(Intent intent) {
       // return null;
    //}

    public class LocalBinder extends Binder {
        ForegroundService getService() {
            // Return this instance of LocalService so clients can call public methods
            return ForegroundService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void addGeofence(LatLng latLng, float radius, String id) {
        Geofence geofence = geofenceHelper.getGeofence(id, latLng, radius, Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT);
        geofenceList.add(geofence);
        GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofence);
        /*GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        // Add the geofences to be monitored by geofencing service using the list we created.
        builder.addGeofences(geofenceList);

        return builder.build();*/
       // Log.d("foreground", "qua...");
        PendingIntent pendingIntent = geofenceHelper.getPendingIntent();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("foreground", "onSuccess: Geofence Added...");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String errorMessage = geofenceHelper.getErrorString(e);
                        Log.d("foreground", "onFailure: " + errorMessage);
                    }
                });


    }

    public void onLocationChanged(@NonNull Location location) {
        Log.d("foreground", "updating location...");

        if (test == 0)
        {
            geofencingClient = LocationServices.getGeofencingClient(this);
            geofenceHelper = new GeofenceHelper(this);


            addGeofence(Galway, 170, "0");
            addGeofence(Galway1, 70, "1");
            addGeofence(Galway2, 130, "2");
            addGeofence(Galway3, 130, "3");
            addGeofence(Galway4, 130, "4");
            addGeofence(Galway5, 100, "5");
            test = 1;
        }

    }


    public void activateApp() {
        Intent intent1 = new Intent(this, AmberActivity.class);
        //intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent1.setAction(Intent.ACTION_MAIN);
        //intent1.addCategory(Intent.CATEGORY_LAUNCHER);
        startActivity(intent1);
    }


}
