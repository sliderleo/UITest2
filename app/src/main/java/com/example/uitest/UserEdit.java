package com.example.uitest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class UserEdit extends AppCompatActivity {
    private EditText etName,etdob,etContact;
    private Spinner spinnerGender;
    private Button editButton;
    DatePickerDialog picker;
    String id;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);
        etName=findViewById(R.id.edit_name);
        etdob=findViewById(R.id.edit_dob);
        etContact=findViewById(R.id.edit_contact);
        spinnerGender=findViewById(R.id.edit_spinner_gender);
        editButton=findViewById(R.id.edit_button);
        etdob.setInputType(InputType.TYPE_NULL);

        etdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr= Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                picker = new DatePickerDialog(UserEdit.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        etdob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                },year,month,day);
                picker.show();
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        id = user.getUid();

        database= FirebaseDatabase.getInstance();
        myRef=database.getReference().child("Users").child(id);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                etName.setText(dataSnapshot.child("name").getValue().toString());
                etdob.setText(dataSnapshot.child("dob").getValue().toString());
                etContact.setText(dataSnapshot.child("contact").getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.getMessage();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name,spinnerGenderString,contactString,dob;
                int contactNum=0;

                spinnerGenderString = spinnerGender.getSelectedItem().toString();
                name=etName.getText().toString();
                contactString=etContact.getText().toString();
                dob=etdob.getText().toString();

                if(name.isEmpty()){
                    Toast.makeText(UserEdit.this,"Name is empty!",Toast.LENGTH_SHORT).show();
                }else if(dob.isEmpty()){
                    Toast.makeText(UserEdit.this,"DOB is empty!",Toast.LENGTH_SHORT).show();
                }else if(contactString.isEmpty()){
                    Toast.makeText(UserEdit.this,"Contact is empty!",Toast.LENGTH_SHORT).show();

                }else if (!(name.isEmpty() && dob.isEmpty() && contactString.isEmpty())){
                     myRef = database.getReference().child("Users").child(id);
                    myRef.child("name").setValue(name);
                    myRef.child("contact").setValue(contactString);
                    myRef.child("dob").setValue(dob);
                    myRef.child("gender").setValue(spinnerGenderString);
                    Toast.makeText(UserEdit.this, "Edited",Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(UserEdit.this,"Error Occurred!",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
