package com.example.uitest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private CardView prof,vehicle,tow,location,logout,history;
    FirebaseAuth mFirebaseAuth;
    CircleImageView profilePic;
    private FirebaseDatabase database;
    private DatabaseReference loginref,mDatabaseRef;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String id ;
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
        profilePic=findViewById(R.id.profile_pic);
        FirebaseUser current  =FirebaseAuth.getInstance().getCurrentUser();
        id = current.getUid();


        mFirebaseAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        loginref= database.getReference("Users");
        mDatabaseRef= database.getReference().child("Upload");

        prof.setOnClickListener(this);
        vehicle.setOnClickListener(this);
        history.setOnClickListener(this);
        location.setOnClickListener(this);

        mDatabaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String name =dataSnapshot.child("mName").getValue().toString();
                if(name.equals(id)){
                    String links = dataSnapshot.child("mImageUrl").getValue().toString();
                    Picasso.get().load(links).into(profilePic);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.app_name);
                builder.setMessage("Do you want to logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                final String towid = user.getUid();
                                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef= database.getReference().child("Users").child(towid);

                                myRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String status = dataSnapshot.child("status").getValue().toString();
                                        String userType= dataSnapshot.child("type").getValue().toString();
                                        if(userType.equals("Driver") && status.equals("1")){
                                        }else if(userType.equals("Tow Car Driver") && status.equals("1")){
                                            DatabaseReference towRef = database.getReference().child("Status").child(towid);
                                            String duty = "off";
                                            towRef.child("duty").setValue(duty);
                                        }else{
                                            Toast.makeText(MainActivity.this, "Your account is inactive please contact the admin!", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                FirebaseAuth.getInstance().signOut();
                                Intent i = new Intent(MainActivity.this, Login.class);
                                Toast.makeText(MainActivity.this, "Logged out ",Toast.LENGTH_SHORT).show();
                                finishAffinity();
                                startActivity(i);
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
                        String status = dataSnapshot.child("status").getValue().toString();
                        String userType= dataSnapshot.child("type").getValue().toString();
                        if(userType.equals("Driver") && status.equals("1")){
                            Intent i;
                            i = new Intent(MainActivity.this, Request.class);
                            startActivity(i);
                        }else if(userType.equals("Tow Car Driver")&& status.equals("1")){
                            Intent i;
                            i = new Intent(MainActivity.this, RequestTow.class);
                            startActivity(i);
                        }else{
                            Toast.makeText(MainActivity.this, "You account is inactive! Please contact the admin for more information",Toast.LENGTH_SHORT).show();
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
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        final String uid = user.getUid();
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference towRef= database.getReference().child("Users").child(uid);
                        towRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String userType= dataSnapshot.child("type").getValue().toString();
                                if(userType.equals("Tow Car Driver")){
                                    DatabaseReference statusRef= database.getReference().child("Status").child(uid);
                                    statusRef.child("duty").setValue("off");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

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
                case R.id.locationCard: i = new Intent(this, AdminViewSuspend.class);startActivity(i);break;
                case R.id.history: i = new Intent(this, HIstoryList.class);startActivity(i);break;
            }
        }


    }


