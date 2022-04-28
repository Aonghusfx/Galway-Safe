package com.girtmobile.galwaysafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SafeActivity extends AppCompatActivity {

    Button home;
    Button back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe);

        home = (Button) findViewById(R.id.btnSafe1);
        back = (Button) findViewById(R.id.btnSafe2);

        home.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View view) {Intent intent = new Intent(getApplicationContext(), MainActivity.class);startActivity(intent);}});
        back.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View view) {Intent intent = new Intent(getApplicationContext(), HelplineActivity.class);startActivity(intent);;}});
    }
}