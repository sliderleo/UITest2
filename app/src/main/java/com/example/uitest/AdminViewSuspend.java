package com.example.uitest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdminViewSuspend extends AppCompatActivity {
    ListView listView;
    ImageButton backbtn;
    Button activeBtn;
    Info info,nameInfo;
    DatabaseReference mDatabaseReference;
    DatabaseReference myRef,statusRef;
    private ArrayList<String> uList = new ArrayList<>();
    private ArrayList<String> nameList = new ArrayList<>();
    private ArrayList<String> userIdList = new ArrayList<>();
    private FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_suspend);
        database = FirebaseDatabase.getInstance();
        info=new Info();
        nameInfo=new Info();
        backbtn=findViewById(R.id.img_back);
        listView=findViewById(R.id.user_listview);
        activeBtn=findViewById(R.id.active_button);
        myRef=database.getReference().child("Users");

        final ArrayAdapter<String> rArrayAdapter=new ArrayAdapter<>(getApplicationContext(),R.layout.mytextview,uList);
        listView.setAdapter(rArrayAdapter);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        activeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name=nameInfo.getId();
                final String id=info.getId();
                statusRef=database.getReference("Users/"+id);
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminViewSuspend.this);
                builder.setTitle("Tow Car App");
                builder.setMessage("Are you sure you wan to active user "+name+" ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        statusRef.child("status").setValue("1");
                        Toast.makeText(AdminViewSuspend.this,"Activate!!",Toast.LENGTH_LONG).show();
                        finish();
                        startActivity(getIntent());
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String name=dataSnapshot.child("name").getValue().toString();
                String type=dataSnapshot.child("type").getValue().toString();
                String status=dataSnapshot.child("status").getValue().toString();
                String id = dataSnapshot.child("userid").getValue().toString();
                if(status.equals("0")){
                    String text="Name: "+name+"\nType: "+type+"\nStatus: Suspended/Inactive";
                    nameList.add(name);
                    userIdList.add(id);
                    uList.add(text);
                }
                rArrayAdapter.notifyDataSetChanged();
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String content=userIdList.get(position);
                String name=nameList.get(position);
                nameInfo.setId(name);
                info.setId(content);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String driverId =userIdList.get(position);
                ViewUser dialog = new ViewUser();
                Bundle bundle = new Bundle();
                bundle.putString("id",driverId);
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(),"View User");
                return true;
            }
        });
    }
}
