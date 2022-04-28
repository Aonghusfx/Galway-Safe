package com.girtmobile.galwaysafe;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AlertActivity extends AppCompatActivity {



    Button yes;
    Button notSure;
    Button no;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);




        yes = (Button) findViewById(R.id.btnYes);
        notSure = (Button) findViewById(R.id.btnUnsure);
        no = (Button) findViewById(R.id.btnNo);

        yes.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View view) {Intent intent = new Intent(getApplicationContext(), SafeActivity.class);
            startActivity(intent);}});
        notSure.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View view) {Intent intent = new Intent(getApplicationContext(), AmberActivity.class);
            startActivity(intent);}});
        no.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View view) {
        }});


    }


}