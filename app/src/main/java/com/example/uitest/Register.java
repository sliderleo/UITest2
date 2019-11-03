package com.example.uitest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private EditText etDob,etUsername,etPassword1,etPassword2,etEmail,etName,etContact;
    private DatePickerDialog picker;
    private Spinner sGender,sUser;
    private Button submitBtn;
    private AlertDialog ab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //dob settings
        etDob= (EditText)findViewById(R.id.et_dob);
        etDob.setInputType(InputType.TYPE_NULL);
        etDob.setOnClickListener(this);

        //editText
        etUsername=(EditText)findViewById(R.id.et_username);
        etPassword1=(EditText)findViewById(R.id.et_password1);
        etPassword2=(EditText)findViewById(R.id.et_password2);
        etEmail=(EditText)findViewById(R.id.et_email);
        etName=(EditText)findViewById(R.id.et_name);
        etContact=(EditText)findViewById(R.id.et_contact);

        //spinner
        sUser = (Spinner) findViewById(R.id.spinner_gender);
        sGender = (Spinner)findViewById(R.id.spinner_user);

        //button
        submitBtn =(Button)findViewById(R.id.submit_button);
        submitBtn.setOnClickListener(this);

    }
    public void getVal(){
        //getting all the values
        String username,password1,password2,email,name,spinnerGender,spinnerUser,contactString,dob;
        int contactNum;

        //getting values from spinner
        spinnerGender = sGender.getSelectedItem().toString();
        spinnerUser=sUser.getSelectedItem().toString();

        //values from edit text
        username = etUsername.getText().toString();
        password1 = etPassword1.getText().toString();
        password2=etPassword2.getText().toString();
        email=etEmail.getText().toString();
        name=etName.getText().toString();
        contactString=etContact.getText().toString();
        dob=etDob.getText().toString();

        //parse contact num to int
        contactNum=Integer.parseInt(contactString);

    //commit check
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.et_dob:final Calendar cldr= Calendar.getInstance();
                            int day = cldr.get(Calendar.DAY_OF_MONTH);
                            int month = cldr.get(Calendar.MONTH);
                            int year = cldr.get(Calendar.YEAR);

                            picker = new DatePickerDialog(Register.this, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    etDob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                }
                            },year,month,day);
                            picker.show();break;

            case R.id.submit_button:

        }
    }


}
