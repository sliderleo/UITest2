package com.example.uitest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class UserRegister extends AppCompatActivity {
    private EditText etDob,etName,etContact;
    private DatePickerDialog picker;
    private Spinner sGender,sUser;
    private Button submit_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        etName=findViewById(R.id.et_name);
        etContact=findViewById(R.id.et_contact);

        //spinner
        sUser = findViewById(R.id.spinner_user);
        sGender =findViewById(R.id.spinner_gender);

        //dob settings
        etDob= findViewById(R.id.et_dob);
        etDob.setInputType(InputType.TYPE_NULL);
        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr= Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                picker = new DatePickerDialog(UserRegister.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        etDob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                },year,month,day);
                picker.show();
            }
        });

        submit_button=findViewById(R.id.submit_button);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name,spinnerGender,spinnerUser,contactString,dob;
                int contactNum=0;
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String id = user.getUid();

                spinnerGender = sGender.getSelectedItem().toString();
                spinnerUser=sUser.getSelectedItem().toString();
                name=etName.getText().toString();
                contactString=etContact.getText().toString();
                dob=etDob.getText().toString();

                if(contactString.isEmpty()){
                    Toast.makeText(UserRegister.this,"Contact number is empty!",Toast.LENGTH_SHORT).show();
                }else if(!contactString.isEmpty()){
                    contactNum=Integer.parseInt(contactString);
                }


                if(name.isEmpty()){
                    Toast.makeText(UserRegister.this,"Name is empty!",Toast.LENGTH_SHORT).show();
                }else if(dob.isEmpty()){
                    Toast.makeText(UserRegister.this,"DOB is empty!",Toast.LENGTH_SHORT).show();
                }else if (!(name.isEmpty() && dob.isEmpty())){
                    UserInfo userInfo=new UserInfo(name,spinnerGender,spinnerUser,dob,contactNum);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference().child("Users").child(id);
                    myRef.setValue(userInfo);
                    Toast.makeText(UserRegister.this, "Added",Toast.LENGTH_SHORT).show();
                    toLogin();
                }else{
                    Toast.makeText(UserRegister.this,"Error Occurred!",Toast.LENGTH_SHORT).show();
                }

            }
        });

}
public void toLogin(){
    FirebaseAuth.getInstance().signOut();
    Intent i = new Intent(UserRegister.this, Login.class);
    startActivity(i);
}

}
