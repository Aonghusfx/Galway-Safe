package com.girtmobile.galwaysafe;


import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;

public class HelplineActivity extends AppCompatActivity{

    public static final String SHARED_PREFS = "sharedPrefs";


    public double latitude = 0;
    public double longitude = 0;


    Button msgICE;
    Button btnice4;
    Button emergency;
    Button imok;

    TextView hSEBtn1;
    TextView samBtn2;
    TextView samBtn;
    TextView hSEBtn2;

    public LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;


    private static final int PERMISSIONS_REQUEST_SEND_SMS = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpline);

        GeofenceBroadcastReceiver geofenceBroadcastReceiver = new GeofenceBroadcastReceiver();
        geofenceBroadcastReceiver.timers.get(0).cancel();

        //NotificationHelper notificationHelper = new NotificationHelper(this);
        //notificationHelper.sendHighPriorityNotification("skhf8owhobf", "", MapsActivity.class);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        String phoneNo1 = sharedPreferences.getString("ICEPhone1", "");
        String phoneNo2 = sharedPreferences.getString("ICEPhone2", "");
        String phoneNo3 = sharedPreferences.getString("ICEPhone3", "");
        Log.d("HelplineActivity", phoneNo1);
        Log.d("HelplineActivity", phoneNo2);
        Log.d("HelplineActivity", phoneNo3);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


        //Log.d("HelplineActivity", lat1.toString());
        //Log.d("HelplineActivity", longit.toString());


        //Location test = new Location();
        //Double aaa = test.getLongitude();


        //Log.d("HelplineActivity", aaa.toString());


        msgICE = (Button) findViewById(R.id.btnICE);
        btnice4 = (Button) findViewById(R.id.btnICE4);
        emergency = (Button) findViewById(R.id.btnEmer);
        imok = (Button) findViewById(R.id.imok);

        hSEBtn1 = (TextView) findViewById(R.id.hSEBtn1);
        hSEBtn2 = (TextView) findViewById(R.id.hSEBtn2);
        samBtn2 = (TextView) findViewById(R.id.samBtn2);
        samBtn = (TextView) findViewById(R.id.samBtn);


        samBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.DIAL");
                intent.setData(Uri.parse("tel:091-561222"));
                startActivity(intent);
            }
        });
        samBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.DIAL");
                intent.setData(Uri.parse("tel:1850-609090"));
                startActivity(intent);
            }
        });
        hSEBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.DIAL");
                intent.setData(Uri.parse("tel:091-561299"));
                startActivity(intent);
            }
        });
        hSEBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.DIAL");
                intent.setData(Uri.parse("tel:1800-459459"));
                startActivity(intent);
            }
        });
        imok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SafeActivity.class);
                startActivity(intent);
            }
        });


        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent("android.intent.action.DIAL");
                //intent.setData(Uri.parse("tel:999"));
                //startActivity(intent);



                    String phone = "999";
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    startActivity(intent);

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {

                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    String url = "https://www.google.com/maps/search/?api=1&query=" + String.valueOf(latitude) + "%2C" + String.valueOf(longitude);


                    Intent intentSend1 = new Intent("SMS_SENT1");
                    intentSend1.putExtra("phone", "0838650303");
                    PendingIntent piSend1;
                    if (Build.VERSION.SDK_INT >= 31) piSend1 = PendingIntent.getBroadcast(getApplicationContext(), 0, intentSend1, PendingIntent.FLAG_MUTABLE);
                    else piSend1 = PendingIntent.getBroadcast(getApplicationContext(), 0, intentSend1, PendingIntent.FLAG_UPDATE_CURRENT);

                    String message = "Message Sent By The Galway Safe App:\n\nI really need to talk to someone now. Can you please contact me immediately?";
                    String message1 = "I'm right here: " + url;

                    SmsManager manager = SmsManager.getDefault();

                    manager.sendTextMessage("0838650303", null, message, piSend1, null);
                    manager.sendTextMessage("0838650303", null, message1, piSend1, null);

                    Toast.makeText(getApplicationContext(), "message sent", Toast.LENGTH_LONG).show();
                }
                else reqPermission();

            }
        });


        btnice4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getlocation();
            }
        });


        msgICE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {

                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    String url = "https://www.google.com/maps/search/?api=1&query=" + String.valueOf(latitude) + "%2C" + String.valueOf(longitude);


                    Intent intentSend1 = new Intent("SMS_SENT1");
                    intentSend1.putExtra("phone", phoneNo1);
                    PendingIntent piSend1;
                    if (Build.VERSION.SDK_INT >= 31) piSend1 = PendingIntent.getBroadcast(getApplicationContext(), 0, intentSend1, PendingIntent.FLAG_MUTABLE);
                    else piSend1 = PendingIntent.getBroadcast(getApplicationContext(), 0, intentSend1, PendingIntent.FLAG_UPDATE_CURRENT);

                    Intent intentSend2 = new Intent("SMS_SENT2");
                    intentSend2.putExtra("phone", phoneNo2);
                    PendingIntent piSend2;
                    if (Build.VERSION.SDK_INT >= 31) piSend2 = PendingIntent.getBroadcast(getApplicationContext(), 0, intentSend2, PendingIntent.FLAG_MUTABLE);
                    else piSend2 = PendingIntent.getBroadcast(getApplicationContext(), 0, intentSend2, PendingIntent.FLAG_UPDATE_CURRENT);

                    Intent intentSend3 = new Intent("SMS_SENT3");
                    intentSend3.putExtra("phone", phoneNo3);
                    PendingIntent piSend3;
                    if (Build.VERSION.SDK_INT >= 31) piSend3 = PendingIntent.getBroadcast(getApplicationContext(), 0, intentSend3, PendingIntent.FLAG_MUTABLE);
                    else piSend3 = PendingIntent.getBroadcast(getApplicationContext(), 0, intentSend3, PendingIntent.FLAG_UPDATE_CURRENT);

                    String message = "Message Sent By The Galway Safe App:\n\nI really need to talk to someone now. Can you please contact me immediately?";
                    String message1 = "I'm right here: " + url;

                    try {

                        SmsManager manager = SmsManager.getDefault();
                        if (phoneNo1 != "") {
                            manager.sendTextMessage(phoneNo1, null, message, piSend1, null);
                            manager.sendTextMessage(phoneNo1, null, message1, piSend1, null);
                        }
                        if (phoneNo2 != "") {
                            manager.sendTextMessage(phoneNo2, null, message, piSend2, null);
                            manager.sendTextMessage(phoneNo2, null, message1, piSend2, null);
                        }
                        if (phoneNo3 != "") {
                            manager.sendTextMessage(phoneNo3, null, message, piSend3, null);
                            manager.sendTextMessage(phoneNo3, null, message1, piSend3, null);
                        }
                        if (phoneNo1 == "" && phoneNo2 == "" && phoneNo3 == "") {
                            Toast.makeText(getApplicationContext(), "ICE Contacts not set", Toast.LENGTH_LONG).show();
                            return;
                        }


                        Toast.makeText(getApplicationContext(), "message sent", Toast.LENGTH_LONG).show();

                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), ex.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }
                else reqPermission();
            }
        });
    }


    public void reqPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PERMISSIONS_REQUEST_SEND_SMS);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_SEND_SMS) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted...", Toast.LENGTH_SHORT).show();
                //return;
            } else {
                Toast.makeText(this, "Permission Denied...", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void getlocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        latitude = location.getLatitude();
        longitude = location.getLongitude();


        Log.d("HelplineActivity", String.valueOf(latitude));
        Log.d("HelplineActivity", String.valueOf(longitude));

        String url = "https://www.google.com/maps/search/?api=1&query=" + String.valueOf(latitude) + "%2C" + String.valueOf(longitude);

        Log.d("HelplineActivity", url);

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
        i.putExtra(Intent.EXTRA_TEXT, url);
        startActivity(Intent.createChooser(i, "Share URL"));
    }



   /* @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(this);
        Log.i("HelplineActivity", "onStop, done");
    }*/

}