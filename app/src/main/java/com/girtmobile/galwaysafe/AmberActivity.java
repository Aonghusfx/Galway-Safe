package com.girtmobile.galwaysafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AmberActivity extends AppCompatActivity {

    Button yes;
    Button no;

    //GeofenceBroadcastReceiver geofenceBroadcastReceiver = new GeofenceBroadcastReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amber);

      //  geofenceBroadcastReceiver.timers.get(0).cancel();

        yes = (Button) findViewById(R.id.btnYes);
        no = (Button) findViewById(R.id.btnNo);

        no.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), SafeActivity.class);
            startActivity(intent);}});

        yes.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), HelplineActivity.class);
            startActivity(intent);}});
    }
}