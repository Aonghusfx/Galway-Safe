package com.girtmobile.galwaysafe;


import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Calendar;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.gson.Gson;

import java.util.Date;
import java.util.List;


public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "GeofenceBroadcastReceiv";
    public int counter;
    public static final String SHARED_PREFS = "sharedPrefs";



    // public Boolean counting;


    public int entered;
    public int quit;

    public double latitude = 0;
    public double longitude = 0;

    public String geofence_ID;
    public LocationManager locationManager;

    public static List<CountDownTimer> timers = new ArrayList<>();



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {




        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        //String te = mService.test;
       // Log.i("TAG", te);


        NotificationHelper notificationHelper = new NotificationHelper(context);

        if (timers.isEmpty())
        {
            CountDownTimer timer = new CountDownTimer(600000, 1000) {
                @Override
                public void onTick(long l) {
                    float t = (float) (l/1000);
                    Log.d(TAG, String.valueOf(t));
                    if (t == 300.0) notificationHelper.sendHighPriorityNotification("You activated the app.", "Tap to continue...", HelplineActivity.class);
                }

                @Override
                public void onFinish() {
                    SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, context.MODE_PRIVATE);

                    String phoneNo1 = sharedPreferences.getString("ICEPhone1", "");//loadString(context, "ICEPhone1");
                    String phoneNo2 = sharedPreferences.getString("ICEPhone2", "");//loadString(context, "ICEPhone2");
                    String phoneNo3 = sharedPreferences.getString("ICEPhone3", "");//loadString(context, "ICEPhone3");

                    Log.d(TAG, phoneNo1);
                    Log.d(TAG, phoneNo2);
                    Log.d(TAG, phoneNo3);

                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {

                        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                        String url = "https://www.google.com/maps/search/?api=1&query=" + String.valueOf(latitude) + "%2C" + String.valueOf(longitude);

                        Intent intentSend1 = new Intent("SMS_SENT1");
                        intentSend1.putExtra("phone", phoneNo1);
                        PendingIntent piSend1;
                        if (Build.VERSION.SDK_INT >= 31) piSend1 = PendingIntent.getBroadcast(context, 0, intentSend1, PendingIntent.FLAG_MUTABLE);
                        else piSend1 = PendingIntent.getBroadcast(context, 0, intentSend1, PendingIntent.FLAG_UPDATE_CURRENT);

                        Intent intentSend2 = new Intent("SMS_SENT2");
                        intentSend2.putExtra("phone", phoneNo2);
                        PendingIntent piSend2;
                        if (Build.VERSION.SDK_INT >= 31) piSend2 = PendingIntent.getBroadcast(context, 0, intentSend2, PendingIntent.FLAG_MUTABLE);
                        else piSend2 = PendingIntent.getBroadcast(context, 0, intentSend2, PendingIntent.FLAG_UPDATE_CURRENT);

                        Intent intentSend3 = new Intent("SMS_SENT3");
                        intentSend3.putExtra("phone", phoneNo3);
                        PendingIntent piSend3;
                        if (Build.VERSION.SDK_INT >= 31) piSend3 = PendingIntent.getBroadcast(context, 0, intentSend3, PendingIntent.FLAG_MUTABLE);
                        else piSend3 = PendingIntent.getBroadcast(context, 0, intentSend3, PendingIntent.FLAG_UPDATE_CURRENT);

                        String message = "Message sent by the Galway Safe app:\n\nI'm in a dangerous area right now: " + url;
                       // String message1 = "I'm right here: " + url;

                        try {

                            SmsManager manager = SmsManager.getDefault();
                            if (phoneNo1 != "") {
                                manager.sendTextMessage(phoneNo1, null, message, piSend1, null);
                                //manager.sendTextMessage(phoneNo1, null, message1, piSend1, null);
                            }
                            if (phoneNo2 != "") {
                                manager.sendTextMessage(phoneNo2, null, message, piSend2, null);
                               // manager.sendTextMessage(phoneNo2, null, message1, piSend2, null);
                            }
                            if (phoneNo3 != "") {
                                manager.sendTextMessage(phoneNo3, null, message, piSend3, null);
                                //manager.sendTextMessage(phoneNo3, null, message1, piSend3, null);
                            }
                            if (phoneNo1 == "" && phoneNo2 == "" && phoneNo3 == "") {
                                return;
                            }
                            notificationHelper.sendHighPriorityNotification("ICE CONTACTS WARNED!", "The app has sent a message to your ICE contacts informing them that you're in an unsafe area.", HelplineActivity.class);

                        } catch (Exception ex) {

                        }
                    }



                    //Intent intent1 = new Intent(context, AmberActivity.class);
                    //intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //context.startActivity(intent1);



                   // test(context);
                }
            };

            timers.add(timer);
        }



          //in order to use the save timer we store it in an array




        //saveInt(context.getApplicationContext(), "test", 0);
        //NotificationHelper notificationHelper = new NotificationHelper(context);
        //public String TAG = "GeofenceBroadcastReceiv";
        //NotificationHelper notificationHelper = new NotificationHelper(context);
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        /*if (geofencingEvent.hasError()) {
            Log.d(TAG, "onReceive: Error receiving geofence event...");
            return;
        }*/

        List<Geofence> geofenceList = geofencingEvent.getTriggeringGeofences();
        for (Geofence geofence : geofenceList) {
            geofence_ID = geofence.getRequestId();
        }
        Log.d(TAG, "Geofence ID: " + geofence_ID);
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentTime = (c.get(11) * 100) + c.get(12);

        int transitionType = geofencingEvent.getGeofenceTransition();

        if (currentTime >= 0  &&  currentTime <= 600) {


            switch (transitionType) {
                case Geofence.GEOFENCE_TRANSITION_ENTER://whenever the user enters a new geofence
                    Log.d(TAG, "entering...");
                    //if (t >= 0 && t <= 600) //if the user enters a geofence during nighttime(between midnight and 6 am)
                    //  {
                    // ;

                    break;
                //else
                //{
                //Log.d(TAG, String.valueOf(t));
                //Log.d(TAG, "not into time");
                //break;
                // }


                case Geofence.GEOFENCE_TRANSITION_DWELL:  //if the user is moving in a geofence
                    Log.d(TAG, "dwelling...");
                    timers.get(0).cancel();
                    timers.get(0).start();

                    break;
                case Geofence.GEOFENCE_TRANSITION_EXIT:  //if the user quits a geofence
                    Log.d(TAG, "quitting...");
                    timers.get(0).cancel();

                    break;
            }
        }
        else Log.d(TAG, "NOPE");
    }



    public void saveInt(Context context, String key, int value) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public String loadString(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);


        // return sharedPref.getInt(key, value);
        // obj.start();
        return sharedPref.getString(key, "");
    }

}

