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

public class AdminMain extends AppCompatActivity implements View.OnClickListener{
    CardView userCard,logoutCard,inactiveCard,approveCard;
    FirebaseAuth mFirebaseAuth;
    CircleImageView profilePic;
    String id;
    private FirebaseDatabase database;
    private DatabaseReference mDatabaseRef,loginref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        userCard=findViewById(R.id.auserCard);
        logoutCard=findViewById(R.id.logoutCard);
        inactiveCard=findViewById(R.id.suspendCard);
        approveCard=findViewById(R.id.approvalCard);
        profilePic=findViewById(R.id.profile_pic);
        FirebaseUser current  =FirebaseAuth.getInstance().getCurrentUser();
        id = current.getUid();
        userCard.setOnClickListener(this);
        inactiveCard.setOnClickListener(this);
        approveCard.setOnClickListener(this);

        mFirebaseAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        loginref= database.getReference("Users");
        mDatabaseRef= database.getReference().child("Upload");

        logoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminMain.this);
                builder.setTitle(R.string.app_name);
                builder.setMessage("Do you want to logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseAuth.getInstance().signOut();
                                Intent i = new Intent(AdminMain.this, Login.class);
                                Toast.makeText(AdminMain.this, "Logged out ",Toast.LENGTH_SHORT).show();
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
    }


    @Override
    public void onClick(View v) {
        Intent i;

        switch(v.getId()) {
            case R.id.auserCard: i = new Intent(this, AdminViewActive.class);startActivity(i);break;
            case R.id.suspendCard: i = new Intent(this, AdminViewSuspend.class);startActivity(i);break;
            case R.id.approvalCard: i = new Intent(this, AdminViewTow.class);startActivity(i);break;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminMain.this);
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
}
