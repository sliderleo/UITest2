package com.example.uitest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ViewRequestList extends AppCompatActivity {
    private Button cancelButton,onGoingButton;
    private ListView rListView;
    private String userId;
    private ImageButton backBtn,refreshBtn;
    private ArrayList<String> mReqList = new ArrayList<>();
    private ArrayList<String> reqId = new ArrayList<>();
    private ArrayList<String> towIdList = new ArrayList<>();
    private ArrayList<String> statusList = new ArrayList<>();
    Info info,towDInfo,statusInfo;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference myRef,towDRef;
    String requestIdString,towIdString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request_list);
        rListView=findViewById(R.id.request_listView);
        final ArrayAdapter<String> rArrayAdapter=new ArrayAdapter<>(getApplicationContext(),R.layout.mytextview,mReqList);
        rListView.setAdapter(rArrayAdapter);
        info=new Info();
        towDInfo=new Info();
        statusInfo=new Info();
        user= FirebaseAuth.getInstance().getCurrentUser();
        userId=user.getUid();
        database = FirebaseDatabase.getInstance();
        myRef=database.getReference().child("Request");

        onGoingButton=findViewById(R.id.to_ongoing_button);
        onGoingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String str = info.getId();
                final String status = statusInfo.getId();
                final String towDId = towDInfo.getId();
                if(str == null){
                    Toast.makeText(ViewRequestList.this,"Please select a request!!",Toast.LENGTH_LONG).show();
                }else  if(status.equals("Accepted")){
                    Intent i = new Intent(ViewRequestList.this,OnGoingUser.class);
                    i.putExtra("requestId",str);
                    i.putExtra("towId",towDId);
                    startActivity(i);
                }else{
                    Toast.makeText(ViewRequestList.this, "The request is still pending!", Toast.LENGTH_SHORT).show();
                }
            }
        });



        backBtn= findViewById(R.id.backArrow);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        refreshBtn=findViewById(R.id.refreshButton);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });

        cancelButton =findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String str=info.getId();
                if(str == null){
                    Toast.makeText(ViewRequestList.this,"Please select a request!!",Toast.LENGTH_LONG).show();
                }else{
                    myRef=database.getReference("Request/"+str);
                    myRef.child("status").setValue("Canceled");
                    Toast.makeText(ViewRequestList.this,"Request Rejected",Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(getIntent());

                }
            }
        });

        rListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String requestId=reqId.get(position);
                String towDId = towIdList.get(position);
                String stat = statusList.get(position);
                requestIdString =reqId.get(position);
                towIdString= towIdList.get(position);
                info.setId(requestId);
                towDInfo.setId(towDId);
                statusInfo.setId(stat);

            }
        });

        rListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String callerCon =towIdList.get(position);
                ViewUser dialog = new ViewUser();
                Bundle bundle = new Bundle();
                bundle.putString("id",callerCon);
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(),"View User");
                return true;
            }
        });

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String towId=dataSnapshot.child("towDriverId").getValue().toString();
                String locName=dataSnapshot.child("workshopName").getValue().toString();
                String requestId=dataSnapshot.child("id").getValue().toString();
                String callerName = dataSnapshot.child("userName").getValue().toString();
                String contact = dataSnapshot.child("userContact").getValue().toString();
                String callerID = dataSnapshot.child("userId").getValue().toString();
                String towName = dataSnapshot.child("towDriverName").getValue().toString();
                String pay = dataSnapshot.child("payment").getValue().toString();
                String status=dataSnapshot.child("status").getValue().toString();
                String fareS = dataSnapshot.child("fare").getValue().toString();
                DecimalFormat d = new DecimalFormat("0.00");
                double fare = Double.parseDouble(fareS);
                String fareL=d.format(fare);
                if(rArrayAdapter.getCount()!=0){
                    mReqList.clear();
                    towIdList.clear();
                    reqId.clear();
                    statusList.clear();
                    if(userId.equals(callerID) && status.equals("Pending")){
                        String text = "Selected Driver: "+towName
                                +"\nDrop off Location: "+locName+"\nFare: RM"+fareL+"\nStatus: "+status+"\nPayment type: "+pay;
                        mReqList.add(text);
                        towIdList.add(towId);
                        reqId.add(requestId);
                        statusList.add(status);
                        rArrayAdapter.notifyDataSetChanged();
                    }else if(userId.equals(callerID) && status.equals("Accepted")){
                        String text = "Selected Driver: "+towName
                                +"\nDrop off Location: "+locName+"\nFare: RM"+fareL+"\nStatus: "+status+"\nPayment type: "+pay;
                        mReqList.add(text);
                        towIdList.add(towId);
                        reqId.add(requestId);
                        statusList.add(status);
                        rArrayAdapter.notifyDataSetChanged();
                    }

                }else{
                    if(userId.equals(callerID) && status.equals("Pending")){
                        String text = "Selected Driver: "+towName
                                +"\nDrop off Location: "+locName+"\nFare: RM"+fareL+"\nStatus: "+status+"\nPayment type: "+pay;
                        mReqList.add(text);
                        towIdList.add(towId);
                        reqId.add(requestId);
                        statusList.add(status);
                        rArrayAdapter.notifyDataSetChanged();
                    }else if(userId.equals(callerID) && status.equals("Accepted")){
                        String text = "Selected Driver: "+towName
                                +"\nDrop off Location: "+locName+"\nFare: RM"+fareL+"\nStatus: "+status+"\nPayment type: "+pay;
                        mReqList.add(text);
                        towIdList.add(towId);
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
    }
}
