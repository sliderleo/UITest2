package com.example.uitest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private CardView prof,vehicle,tow,location,logout,history;
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
        history=findViewById(R.id.history);

        prof.setOnClickListener(this);
        vehicle.setOnClickListener(this);
        history.setOnClickListener(this);
        location.setOnClickListener(this);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final String id = user.getUid();
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef= database.getReference().child("Users").child(id);

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String userType= dataSnapshot.child("type").getValue().toString();
                        if(userType.equals("Driver")){
                        }else if(userType.equals("Tow Car Driver")){
                            DatabaseReference towRef = database.getReference().child("Status").child(id);
                            String duty = "off";
                            towRef.child("duty").setValue(duty);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(MainActivity.this, Login.class);
                Toast.makeText(MainActivity.this, "Logged out ",Toast.LENGTH_SHORT).show();
                startActivity(i);
            }
        });

        tow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String id = user.getUid();
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef= database.getReference().child("Users").child(id);

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String userType= dataSnapshot.child("type").getValue().toString();
                        if(userType.equals("Driver")){
                            Intent i;
                            i = new Intent(MainActivity.this, Request.class);
                            startActivity(i);
                        }else if(userType.equals("Tow Car Driver")){
                            Intent i;
                            i = new Intent(MainActivity.this, RequestTow.class);
                            startActivity(i);
                        }else{
                            Toast.makeText(MainActivity.this, "Error!",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                        System.exit(0);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }


    @Override
        public void onClick(View v) {
            Intent i;

            switch(v.getId()) {
                case R.id.profCard: i = new Intent(this, User.class);startActivity(i);break;
                case R.id.vehicleCard: i = new Intent(this, Vehicle.class);startActivity(i);break;
                case R.id.towCard: i = new Intent(this, Request.class);startActivity(i);break;
                case R.id.locationCard: i = new Intent(this, Workshop.class);startActivity(i);break;
                case R.id.history: i = new Intent(this, HIstoryList.class);startActivity(i);break;
            }
        }
    }


