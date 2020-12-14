package com.example.thescannerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        //this is the bottom navigation binding
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //this is the navigation for the about us activity
        bottomNavigationView.setSelectedItemId(R.id.aboutus);
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
                    //new intent is activated to the tutorial
                    case R.id.tutorial:
                        startActivity(new Intent(getApplicationContext(),Tutorial.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    //no action is needed for about us
                    case R.id.aboutus:
                        return true;
                }
                return false;
            }
        });
    }
}