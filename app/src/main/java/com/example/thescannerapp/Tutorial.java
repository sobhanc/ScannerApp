package com.example.thescannerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Tutorial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        //this is the bottom navigation binding
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //this is the navigation for the tutorial activity
        bottomNavigationView.setSelectedItemId(R.id.tutorial);
        //upon selection the switch will be activated
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.home:
                        //new intent is activated to the home activity
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    //no action is needed for tutorial
                    case R.id.tutorial:
                        return true;

                    case R.id.aboutus:
                        //new intent is activated to the about us
                        startActivity(new Intent(getApplicationContext(),AboutUs.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}