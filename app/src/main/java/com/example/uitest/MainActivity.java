package com.example.uitest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private CardView prof,vehicle,tow,location,logout;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prof =findViewById(R.id.profCard);
        vehicle= findViewById(R.id.vehicleCard);
        tow=findViewById(R.id.towCard);
        location=findViewById(R.id.locationCard);
        logout=findViewById(R.id.logout);

        prof.setOnClickListener(this);
        vehicle.setOnClickListener(this);
        tow.setOnClickListener(this);
        location.setOnClickListener(this);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(MainActivity.this, Login.class);
                Toast.makeText(MainActivity.this, "Logged out ",Toast.LENGTH_SHORT).show();
                startActivity(i);
            }
        });
    }

        @Override
        public void onClick(View v) {
            Intent i;

            switch(v.getId()) {
                case R.id.profCard: i = new Intent(this, User.class);startActivity(i);break;
                case R.id.vehicleCard: i = new Intent(this, Vehicle.class);startActivity(i);break;
                case R.id.towCard: i = new Intent(this, Request.class);startActivity(i);break;
                case R.id.locationCard: i = new Intent(this, Workshop.class);startActivity(i);break;
            }
        }
    }


