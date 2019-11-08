package com.example.uitest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class User extends AppCompatActivity {
    private ImageButton backBtn,editBtn;
    private TextView tv_name,tv_gender,tv_dob,tv_contact,tv_email;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        backBtn=  (ImageButton) findViewById(R.id.backArrow);
        editBtn=(ImageButton)findViewById(R.id.editButton);

        tv_name=findViewById(R.id.name_tv);
        tv_gender=findViewById(R.id.gender_tv);
        tv_dob=findViewById(R.id.dob_tv);
        tv_contact=findViewById(R.id.contact_tv);
        tv_email=findViewById(R.id.email_tv);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        myRef= database.getReference().child("Users").child(id);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String name = dataSnapshot.child("name").getValue().toString();
                String gender= dataSnapshot.child("gender").getValue().toString();
                String dob = dataSnapshot.child("dob").getValue().toString();
                String contact=dataSnapshot.child("contact").getValue().toString();
                String email = user.getEmail();
                tv_name.setText("Name: "+name);
                tv_gender.setText("Gender: "+gender);
                tv_dob.setText("DOB: "+dob);
                tv_contact.setText("Contact: "+contact);
                tv_email.setText("Email: "+email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });









        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
