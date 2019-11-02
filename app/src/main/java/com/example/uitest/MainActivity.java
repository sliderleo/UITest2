package com.example.uitest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private CardView prof,vehicle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prof =findViewById(R.id.profCard);
        vehicle= findViewById(R.id.vehicleCard);



        prof.setOnClickListener(this);
        vehicle.setOnClickListener(this);
    }

        @Override
        public void onClick(View v) {
            Intent i;

            switch(v.getId()) {
                case R.id.profCard: i = new Intent(this, User.class);startActivity(i);break;
                case R.id.vehicleCard: i = new Intent(this, Login.class);startActivity(i);break;
            }
        }
    }


