package com.example.uitest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class HIstoryList extends AppCompatActivity {
    private ListView rListView;
    private ArrayList<String> mReqList = new ArrayList<>();
    ImageButton backBtn;
    String  userId;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_list);

        user= FirebaseAuth.getInstance().getCurrentUser();
        userId=user.getUid();
        rListView=findViewById(R.id.history_listView);
        backBtn=findViewById(R.id.backArrow);
        final ArrayAdapter<String> rArrayAdapter=new ArrayAdapter<>(getApplicationContext(),R.layout.mytextview,mReqList);
        rListView.setAdapter(rArrayAdapter);
        database = FirebaseDatabase.getInstance();
        myRef=database.getReference().child("Request");

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String towId=dataSnapshot.child("towDriverId").getValue().toString();
                String towDriver=dataSnapshot.child("towDriverName").getValue().toString();
                String locName=dataSnapshot.child("workshopName").getValue().toString();
                String requestId=dataSnapshot.child("id").getValue().toString();
                String callerName = dataSnapshot.child("userName").getValue().toString();
                String contact = dataSnapshot.child("userContact").getValue().toString();
                String callerID = dataSnapshot.child("userId").getValue().toString();
                String status=dataSnapshot.child("status").getValue().toString();
                String pay = dataSnapshot.child("payment").getValue().toString();
                String fareS = dataSnapshot.child("fare").getValue().toString();
                DecimalFormat d = new DecimalFormat("0.00");
                double fare = Double.parseDouble(fareS);
                String fareL=d.format(fare);

                if(userId.equals(towId) || userId.equals(callerID)){

                    if(status.equals("Ended")||status.equals("Cancelled")||status.equals("Rejected")){
                        String text = "Request by: "+callerName+"\nTow Driver: "+towDriver+"\nDestination: "+locName+"\nFare: RM"+fareL+"\nStatus: "+status+"\nPayment type: "+pay;
                        mReqList.add(text);
                        rArrayAdapter.notifyDataSetChanged();
                    }
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
}
