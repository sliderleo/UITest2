package com.example.uitest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RequestList extends AppCompatActivity {
private Button acceptBtn,rejectBtn,onGoingButton,viewMapButton;
private ListView rListView;
private String userId;
private ImageButton backBtn,refreshBtn;
private ArrayList<String> mReqList = new ArrayList<>();
private ArrayList<String> reqId = new ArrayList<>();
private ArrayList<String> callerId = new ArrayList<>();
private ArrayList<String> statusList = new ArrayList<>();
Info info,statusInfo,userInfo;
FirebaseUser user;
FirebaseDatabase database;
DatabaseReference myRef,towDRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);
        viewMapButton=findViewById(R.id.view_map_button);
        onGoingButton=findViewById(R.id.ongoing_button);
        acceptBtn=findViewById(R.id.accept_button);
        rejectBtn=findViewById(R.id.reject_button);
        backBtn= (ImageButton) findViewById(R.id.backArrow);
        refreshBtn=(ImageButton)findViewById(R.id.refreshbtn);
        rListView=findViewById(R.id.request_listView);
        final ArrayAdapter<String> rArrayAdapter=new ArrayAdapter<>(getApplicationContext(),R.layout.mytextview,mReqList);
        rListView.setAdapter(rArrayAdapter);
        info=new Info();
        statusInfo=new Info();
        userInfo=new Info();
        user= FirebaseAuth.getInstance().getCurrentUser();
        userId=user.getUid();
        database = FirebaseDatabase.getInstance();
        myRef=database.getReference().child("Request");
        towDRef=database.getReference().child("Status").child(userId);


        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String towId=dataSnapshot.child("towDriverId").getValue().toString();
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
                if(rArrayAdapter.getCount()!=0){
                    mReqList.clear();
                    callerId.clear();
                    reqId.clear();
                    statusList.clear();
                    if(userId.equals(towId) && status.equals("Pending")){
                        String text = "Caller Name: "+callerName
                                +"\nContact: "+contact+"\nDrop off Location: "+locName+"\nFare: RM"+fareL+"\nStatus: "+status+"\nPayment type: "+pay;
                        mReqList.add(text);
                        callerId.add(callerID);
                        reqId.add(requestId);
                        statusList.add(status);
                        rArrayAdapter.notifyDataSetChanged();
                    }else if(userId.equals(towId) && status.equals("Accepted")){
                        String text = "Caller Name: "+callerName
                                +"\nContact: "+contact+"\nDrop off Location: "+locName+"\nFare: RM"+fareL+"\nStatus: "+status+"\nPayment type: "+pay;
                        mReqList.add(text);
                        callerId.add(callerID);
                        reqId.add(requestId);
                        statusList.add(status);
                        rArrayAdapter.notifyDataSetChanged();
                    }
                }else{
                    if(userId.equals(towId) && status.equals("Pending")){
                        String text = "Caller Name: "+callerName
                                +"\nContact: "+contact+"\nDrop off Location: "+locName+"\nFare: RM"+fareL+"\nStatus: "+status+"\nPayment type: "+pay;
                        mReqList.add(text);
                        callerId.add(callerID);
                        reqId.add(requestId);
                        statusList.add(status);
                        rArrayAdapter.notifyDataSetChanged();
                    }else if(userId.equals(towId) && status.equals("Accepted")){
                        String text = "Caller Name: "+callerName
                                +"\nContact: "+contact+"\nDrop off Location: "+locName+"\nFare: RM"+fareL+"\nStatus: "+status+"\nPayment type: "+pay;
                        mReqList.add(text);
                        callerId.add(callerID);
                        reqId.add(requestId);
                        statusList.add(status);
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

        rListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String content=reqId.get(position);
                String status = statusList.get(position);
                String user=callerId.get(position);
                info.setId(content);
                statusInfo.setId(status);
                userInfo.setId(user);
            }
        });

        rListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String callerCon =callerId.get(position);
                ViewUser dialog = new ViewUser();
                Bundle bundle = new Bundle();
                bundle.putString("id",callerCon);
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(),"View User");
                return true;
            }
        });

        viewMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String requestId=info.getId();
                Intent i = new Intent(RequestList.this,ViewLocation.class);
                i.putExtra("requestId",requestId);
                startActivity(i);
            }
        });

        onGoingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String requestId=info.getId();
                final String userId=userInfo.getId();
                final String status=statusInfo.getId();

                if(requestId == null){
                    Toast.makeText(RequestList.this,"Please select a request!!",Toast.LENGTH_LONG).show();
                }else  if(status.equals("Accepted")){
                    Intent i = new Intent(RequestList.this,OnGoingTow.class);
                    i.putExtra("requestId",requestId);
                    i.putExtra("callerId",userId);
                    startActivity(i);
                }else{
                    Toast.makeText(RequestList.this, "The request is still pending!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String str=info.getId();
                if(str == null){
                    Toast.makeText(RequestList.this,"Please select a request!!",Toast.LENGTH_LONG).show();
                }else{
                    myRef=database.getReference("Request/"+str);
                    readData(new MyCallback() {
                        @Override
                        public void onCallback(String status) {
                            if(status.equals("on")){
                                myRef.child("status").setValue("Accepted");
                                Toast.makeText(RequestList.this,"Request Accepted!",Toast.LENGTH_LONG).show();
                                String duty = "busy";
                                towDRef.child("duty").setValue(duty);
                                finish();
                                startActivity(getIntent());
                            }else if(status.equals("off")){
                                Toast.makeText(RequestList.this,"Please get on duty!",Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(RequestList.this,"You can only accept one request at a time!",Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
            }
        });

        rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String str=info.getId();
                if(str == null){
                    Toast.makeText(RequestList.this,"Please select a request!!",Toast.LENGTH_LONG).show();
                }else{
                    myRef=database.getReference("Request/"+str);
                    myRef.child("status").setValue("Rejected");
                    Toast.makeText(RequestList.this,"Request Rejected",Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(getIntent());

                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });
    }

    public interface MyCallback {
        void onCallback(String status);
    }

    public void readData(final MyCallback myCallback) {
        database.getReference().child("Status").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String status = dataSnapshot.child("duty").getValue().toString();
                myCallback.onCallback(status);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

}
