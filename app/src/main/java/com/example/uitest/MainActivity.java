package com.example.uitest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private CardView prof,vehicle,tow,location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prof =findViewById(R.id.profCard);
        vehicle= findViewById(R.id.vehicleCard);
        tow=findViewById(R.id.towCard);
        location=findViewById(R.id.locationCard);

        prof.setOnClickListener(this);
        vehicle.setOnClickListener(this);
        tow.setOnClickListener(this);
        location.setOnClickListener(this);
    }

        @Override
        public void onClick(View v) {
            Intent i;

            switch(v.getId()) {
                case R.id.profCard: i = new Intent(this, User.class);startActivity(i);break;
                case R.id.vehicleCard: i = new Intent(this, Vehicle.class);startActivity(i);break;
                case R.id.towCard: i = new Intent(this, Login.class);startActivity(i);break;
                case R.id.locationCard: i = new Intent(this, Workshop.class);startActivity(i);break;
            }
        }
    }


