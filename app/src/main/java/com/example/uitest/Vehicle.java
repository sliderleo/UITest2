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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Vehicle extends AppCompatActivity{
    private ImageButton backBtn,addBtn;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ListView listView;
    private Button deleteBtn;
    private Info info;
    private ArrayList<String> mCarList = new ArrayList<>();
    private ArrayList<String> carId = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);
        info = new Info();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();
        database=FirebaseDatabase.getInstance();
        myRef= database.getReference("Car").child(id);
        deleteBtn=findViewById(R.id.delete_button);
        listView=findViewById(R.id.car_list_view);

        final ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>(this,R.layout.mytextview, mCarList);
        listView.setAdapter(arrayAdapter);

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String newCar=dataSnapshot.getValue(CarInfo.class).toString();
                String idCar=dataSnapshot.child("carId").getValue().toString();
                mCarList.add(newCar);
                carId.add(idCar);

                arrayAdapter.notifyDataSetChanged();
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



        backBtn = (ImageButton) findViewById(R.id.backArrow);
        addBtn=(ImageButton)findViewById(R.id.addButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String content= carId.get(position);
                info.setId(content);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String str=info.getId();

                if(str == null){
                    Toast.makeText(Vehicle.this,"Please a Vehicle",Toast.LENGTH_LONG).show();
                }else{
                    myRef.push().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            myRef.child(str).removeValue();
                            Toast.makeText(Vehicle.this,"Vehicle Deleted",Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(getIntent());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

    public void openDialog(){
        AddDialog dialog = new AddDialog();
        dialog.show(getSupportFragmentManager(),"Add Vehicle");
    }
}
