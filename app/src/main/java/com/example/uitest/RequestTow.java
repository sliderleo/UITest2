package com.example.uitest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RequestTow extends AppCompatActivity {
    private Button onButton,offButton;
    private TextView tv_status;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_tow);
        tv_status=findViewById(R.id.status_text);
        onButton=findViewById(R.id.on_button);
        offButton=findViewById(R.id.off_button);
        user= FirebaseAuth.getInstance().getCurrentUser();
        userId=user.getUid();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Status").child(userId);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String duty=dataSnapshot.child("duty").getValue().toString();

                if(duty.equals("on")){
                    tv_status.setText("Status: On Duty");
                }else if(duty.equals("off")){
                    tv_status.setText("Status: Off Duty");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        onButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String duty = "on";
                myRef.child("duty").setValue(duty);
                tv_status.setText("Status: On Duty");
                Toast.makeText(RequestTow.this, "Status Updated",Toast.LENGTH_SHORT).show();
            }
        });

        offButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String duty = "off";
                myRef.child("duty").setValue(duty);
                tv_status.setText("Status: Off Duty");
                Toast.makeText(RequestTow.this, "Status Updated",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
