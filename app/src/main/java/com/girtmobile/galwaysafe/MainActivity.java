package com.girtmobile.galwaysafe;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;


public class MainActivity extends AppCompatActivity{

    Button button1;
    Button button2;
    Button button3;
    Button saveICEBtn;
    Button selectedButton;
    Button termsICEBtn;

    private ImageView imgVideo;
    private ImageView imgLocation;
    private TextView textView;

    public static String phoneNo1 = "";
    public static String phoneNo2 = "";
    public static String phoneNo3 = "";
    private static final String TAG = "MainActivity";

    public int CONTACT_PERMISSION_CODE = 1;
    private static final int PERMISSIONS_REQUEST_SEND_SMS = 123;
    private final int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private final int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;

    public int CONTACT_PICK_CODE = 2;
    public static final String SHARED_PREFS = "sharedPrefs";

    public String loadText1;
    public String loadText2;
    public String loadText3;



    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    String provider;
    protected String latitude,longitude;
    protected boolean gps_enabled,network_enabled;


    public Boolean GeofencingActivated;




    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        View view = this.getWindow().getDecorView();
        int myColor = getResources().getColor(R.color.colorPrimary);
        view.setBackgroundColor(myColor);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= 29)
            {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    if (!foregroundServiceRunning()) {  //start foreground service
                        Intent serviceIntent = new Intent(this, ForegroundService.class);
                        startForegroundService(serviceIntent);
                    }
                }
            }
            else
            {
                if (!foregroundServiceRunning()) {  //start foreground service
                            Intent serviceIntent = new Intent(this, ForegroundService.class);
                            startForegroundService(serviceIntent);
                        }
            }
        }
        else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            }
        }




        button1 = (Button) findViewById(R.id.btnICE1);
        button2 = (Button) findViewById(R.id.btnICE2);
        button3 = (Button) findViewById(R.id.btnICE3);
        textView = (TextView) findViewById(R.id.textView);
        saveICEBtn = (Button) findViewById(R.id.saveICEBtn);
        termsICEBtn = (Button) findViewById(R.id.termsICEBtn);
        imgVideo = (ImageView) findViewById(R.id.imageViewMovie);
        imgLocation = (ImageView) findViewById(R.id.imageViewLocation);


        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        GeofencingActivated = sharedPreferences.getBoolean("geofencingactivated", false);




        imgVideo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openVideo(getApplicationContext());
            }
        });

        imgLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openMap();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedButton = button1;
                actionButton();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedButton = button2;
                actionButton();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedButton = button3;
                actionButton();
            }
        });

        saveICEBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveICE();
            }
        });

        termsICEBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TermsActivity.class);
                startActivity(intent);
            }
        });


        loadData();
        updateViews();

    }



    public boolean foregroundServiceRunning(){
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service: activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if(ForegroundService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }






    private void saveICE() {
        if (phoneNo1 == ""  &&  phoneNo2 == ""  &&  phoneNo3 == "")
        {
            Toast.makeText(this, "ICE Contacts not set", Toast.LENGTH_SHORT).show();
            return;
        }

        final Context contextForBuilder = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(contextForBuilder);
        builder.setMessage("When you save the ICE Contacts the app will send an SMS informing them that they have been selected as your ICE Contact. ICE Contacts do not need to have the app installed.")
                .setCancelable(false)
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sendSMS(phoneNo1, phoneNo2, phoneNo3);
                        saveData();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Context contextForBuilder = null;
                        //Toast.makeText(contextForBuilder, "ICE Contacts saved.", Toast.LENGTH_LONG).show();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void openVideo(Context applicationContext) {
        //String url = "https://app.emaze.com/@AOFLWZQCR/galway-safe-app";
        String url = "https://www.youtube.com/watch?v=z_K1YAsfldw";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }


    public void openMap() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }


    public void actionButton() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            pickContact();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, CONTACT_PERMISSION_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, CONTACT_PERMISSION_CODE);
            }
        }
    }






    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CONTACT_PERMISSION_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted...", Toast.LENGTH_SHORT).show();
                //return;
                pickContact();
            } else {
                Toast.makeText(this, "Permission Denied...", Toast.LENGTH_SHORT).show();
                //reqPermission();
            }
        }


        if (requestCode == PERMISSIONS_REQUEST_SEND_SMS)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) sendSMS(phoneNo1, phoneNo2, phoneNo3);
            else Toast.makeText(this, "Permission Denied...", Toast.LENGTH_SHORT).show();
        }



        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Toast.makeText(this, "location disabled", Toast.LENGTH_LONG).show();
                    //buildAlertMessageNoGps();
                    // return;
                }

                if (Build.VERSION.SDK_INT >= 29) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION))
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
                    else
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
                }

                else {  //if android version is below 10 we won't need to ask for background permission
                    if (!GeofencingActivated) {
                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("geofencingactivated", true);
                        editor.apply();


                        openMap();

                        if (!foregroundServiceRunning()) {  //start foreground service
                            Intent serviceIntent = new Intent(this, ForegroundService.class);
                            startForegroundService(serviceIntent);
                        }
                    }
                }
            }
            else {
                Toast.makeText(this, "The App Won't Be Working With Permisssions Denied.", Toast.LENGTH_LONG).show();
            }
            return;
        }



        if (requestCode == BACKGROUND_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {  //all permissions granted

                if (!GeofencingActivated) {
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("geofencingactivated", true);
                    editor.apply();



                    openMap();

                    if(!foregroundServiceRunning()) {  //start foreground service
                        Intent serviceIntent = new Intent(this, ForegroundService.class);
                        startForegroundService(serviceIntent);
                    }

                }
                else {
                    Toast.makeText(this, "Geofencing Succeffully Activated", Toast.LENGTH_SHORT).show();


                    //getLastLocation();
                    //checkSettingsAndStartLocationUpdates();
                    return;
                }
            }
            else
            {
                Toast.makeText(this, "Background Geolocation Won't Be Working With Permisssions Denied. Please select the 'allow all the time' option", Toast.LENGTH_LONG).show();


                //getLastLocation();
                //checkSettingsAndStartLocationUpdates();
                return;
            }
        }
    }


    public void pickContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        //startActivityForResult(intent, CONTACT_PICK_CODE);
        contactPickerResult.launch(intent);
    }


    private void sendSMS(final String phone1, final String phone2, final String phone3) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {

            Intent intentSend1 = new Intent("SMS_SENT1");
            intentSend1.putExtra("phone", phone1);
            PendingIntent piSend1;
            if (Build.VERSION.SDK_INT >= 31) piSend1 = PendingIntent.getBroadcast(this, 0, intentSend1, PendingIntent.FLAG_MUTABLE);
            else piSend1 = PendingIntent.getBroadcast(this, 0, intentSend1, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent intentSend2 = new Intent("SMS_SENT2");
            intentSend2.putExtra("phone", phone2);
            PendingIntent piSend2;
            if (Build.VERSION.SDK_INT >= 31) piSend2 = PendingIntent.getBroadcast(this, 0, intentSend2, PendingIntent.FLAG_MUTABLE);
            else piSend2 = PendingIntent.getBroadcast(this, 0, intentSend2, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent intentSend3 = new Intent("SMS_SENT3");
            intentSend3.putExtra("phone", phone3);
            PendingIntent piSend3;
            if (Build.VERSION.SDK_INT >= 31) piSend3 = PendingIntent.getBroadcast(this, 0, intentSend3, PendingIntent.FLAG_MUTABLE);
            else piSend3 = PendingIntent.getBroadcast(this, 0, intentSend3, PendingIntent.FLAG_UPDATE_CURRENT);

            String message = "Message sent by the Galway safe app:\n\nHi, I have requested you to be one of my ICE contacts.";
            String message1 = "If I feel that I really need to talk to someone after a night out in Galway I have asked if you would be willing to help";

            try {

                SmsManager manager = SmsManager.getDefault();
                if (phoneNo1 != "")
                {
                    manager.sendTextMessage(phone1, null, message, piSend1, null);
                    manager.sendTextMessage(phone1, null, message1, piSend1, null);
                }
                if (phoneNo2 != "")
                {
                    manager.sendTextMessage(phone2, null, message, piSend2, null);
                    manager.sendTextMessage(phone2, null, message1, piSend2, null);
                }
                if (phoneNo3 != "")
                {
                    manager.sendTextMessage(phone3, null, message, piSend3, null);
                    manager.sendTextMessage(phone3, null, message1, piSend3, null);
                }

                Toast.makeText(this, "ICE Contacts saved.", Toast.LENGTH_LONG).show();
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), ex.getMessage().toString(), Toast.LENGTH_LONG).show();
            }

        }
        else //ask for sms permission
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PERMISSIONS_REQUEST_SEND_SMS);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PERMISSIONS_REQUEST_SEND_SMS);
            }
        }

    }


    private ActivityResultLauncher<Intent> contactPickerResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        System.out.println("picked up");

                        Cursor cursor1, cursor2;
                        Intent data = result.getData();
                        Uri uri = data.getData();

                        cursor1 = getContentResolver().query(uri, null, null, null, null, null);
                        if (cursor1.moveToFirst()) {
                            @SuppressLint("Range") String contactName = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            @SuppressLint("Range") String contactID = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));
                            @SuppressLint("Range") String IDResults = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                            int IDResultHold = Integer.parseInt(IDResults);
                            //@SuppressLint("Range") String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            //String phoneNo = cursor.getString(Integer.parseInt(contactID));


                            if (IDResultHold == 1) {
                                //cursor2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null );
                                cursor2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactID,
                                        null,
                                        null
                                );
                                while (cursor2.moveToNext()) {
                                    @SuppressLint("Range") String contactNumber = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                    if (selectedButton == button1) {
                                        phoneNo1 = contactNumber.toString();
                                        button1.setText(contactName.toString());
                                    }
                                    if (selectedButton == button2) {
                                        phoneNo2 = contactNumber.toString();
                                        button2.setText(contactName.toString());
                                    }
                                    if (selectedButton == button3) {
                                        phoneNo3 = contactNumber.toString();
                                        button3.setText(contactName.toString());
                                    }

                                }
                                cursor2.close();
                            }
                            cursor1.close();

                        }
                    } else {
                        System.out.println("nope");
                    }
                }
            });


    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("ICEPhone1", this.phoneNo1);
        editor.putString("ICEPhone2", this.phoneNo2);
        editor.putString("ICEPhone3", this.phoneNo3);

        editor.putString("1", button1.getText().toString());
        editor.putString("2", button2.getText().toString());
        editor.putString("3", button3.getText().toString());


        //editor.apply();
        editor.commit();
    }


    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        loadText1 = sharedPreferences.getString("1", "ICE CONTACT 1");
        loadText2 = sharedPreferences.getString("2", "ICE CONTACT 2");
        loadText3 = sharedPreferences.getString("3", "ICE CONTACT 3");

        phoneNo1 = sharedPreferences.getString("ICEPhone1", "");
        phoneNo2 = sharedPreferences.getString("ICEPhone2", "");
        phoneNo3 = sharedPreferences.getString("ICEPhone3", "");
    }


    public void updateViews() {
        button1.setText(loadText1);
        button2.setText(loadText2);
        button3.setText(loadText3);
    }

}


