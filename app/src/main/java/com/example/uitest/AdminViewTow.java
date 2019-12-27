package com.example.uitest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminViewTow extends AppCompatActivity {
    ListView listView;
    ImageButton backbtn;
    Button approveBtn;
    Info info;
    DatabaseReference mDatabaseReference;
    List<String> uploadlist;
    DatabaseReference statusRef,towDRef;
    private ArrayList<String> towidList = new ArrayList<>();
    private FirebaseDatabase database;
    private ArrayList<String> mReqList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_tow);
        database = FirebaseDatabase.getInstance();
        uploadlist=new ArrayList<>();
        info=new Info();
        listView=findViewById(R.id.towdriver_listview);
        final ArrayAdapter<String> rArrayAdapter=new ArrayAdapter<>(getApplicationContext(),R.layout.mytextview,mReqList);
        listView.setAdapter(rArrayAdapter);
        backbtn=findViewById(R.id.img_back);
        mDatabaseReference = database.getReference().child("Company");
        statusRef=database.getReference().child("Users");
        towDRef=database.getReference().child("Company");
        approveBtn=findViewById(R.id.submit_button);


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        approveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String towid=info.getId();
                if(towid == null){
                    Toast.makeText(AdminViewTow.this,"Please select a tow car driver!!",Toast.LENGTH_LONG).show();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminViewTow.this);
                    builder.setTitle("Tow Car App");
                    builder.setMessage("Approve the tow car driver ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            statusRef=database.getReference("Company/"+towid);
                            towDRef=database.getReference("Users/"+towid);
                            statusRef.child("status").setValue("1");
                            towDRef.child("status").setValue("1");
                            Toast.makeText(AdminViewTow.this,"Approved!!",Toast.LENGTH_LONG).show();
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
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String content=towidList.get(position);
                info.setId(content);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(uploadlist.get(position)));
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminViewTow.this);
                builder.setTitle("Tow Car App");
                builder.setMessage("Download the pdf file ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });

        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                 String name = dataSnapshot.child("name").getValue().toString();
                 String companyname = dataSnapshot.child("companyname").getValue().toString();
                 String insurance = dataSnapshot.child("insurance").getValue().toString();
                 String pdf = dataSnapshot.child("pdfurl").getValue().toString();
                String stat = dataSnapshot.child("status").getValue().toString();
                String towid = dataSnapshot.child("towdriverid").getValue().toString();
                 if(stat.equals("0")){
                     String text = "Name: "+name+"\nCompany Name: "+companyname+"\nStatus: Waiting for Approval";
                     towidList.add(towid);
                     uploadlist.add(pdf);
                     mReqList.add(text);
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
    }

    public interface MyCallback {
        void onCallback(String name,String id);
    }

    public void readData(final MyCallback myCallback) {
        database.getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String id=dataSnapshot.child("id").getValue().toString();
                myCallback.onCallback(name,id);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void readName(final MyCallback myCallback){
        database.getReference().child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String name = dataSnapshot.child("name").getValue().toString();
                String id=dataSnapshot.child("id").getValue().toString();
                myCallback.onCallback(name,id);
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
