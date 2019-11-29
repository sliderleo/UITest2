package com.example.uitest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
    private Button cancelButton;
    private ListView rListView;
    private String userId;
    private ImageButton backBtn;
    private ArrayList<String> mReqList = new ArrayList<>();
    private ArrayList<String> reqId = new ArrayList<>();
    private ArrayList<String> towIdList = new ArrayList<>();
    Info info;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference myRef,towDRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request_list);
        rListView=findViewById(R.id.request_listView);
        final ArrayAdapter<String> rArrayAdapter=new ArrayAdapter<>(getApplicationContext(),R.layout.mytextview,mReqList);
        rListView.setAdapter(rArrayAdapter);
        info=new Info();
        user= FirebaseAuth.getInstance().getCurrentUser();
        userId=user.getUid();
        database = FirebaseDatabase.getInstance();
        myRef=database.getReference().child("Request");


        backBtn= findViewById(R.id.backArrow);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                String content=reqId.get(position);
                info.setId(content);
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
                String status=dataSnapshot.child("status").getValue().toString();
                String fareS = dataSnapshot.child("fare").getValue().toString();
                DecimalFormat d = new DecimalFormat("0.00");
                double fare = Double.parseDouble(fareS);
                String fareL=d.format(fare);

                if(userId.equals(callerID) && status.equals("Pending")){
                    String text = "Selected Driver: "+towName
                            +"\nDrop off Location: "+locName+"\nFare: RM"+fareL+"\nStatus: "+status;
                    mReqList.add(text);
                    towIdList.add(towId);
                    reqId.add(requestId);

                    rArrayAdapter.notifyDataSetChanged();
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
