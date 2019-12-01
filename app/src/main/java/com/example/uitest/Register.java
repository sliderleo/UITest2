package com.example.uitest;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Register extends AppCompatActivity {
    private EditText etPassword,etEmail,etPassword2;

    private Button submitBtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth=FirebaseAuth.getInstance();



        //editText
        etPassword=findViewById(R.id.et_password);
        etEmail=findViewById(R.id.et_email);
        etPassword2=findViewById(R.id.et_password2);


        //button
        submitBtn =findViewById(R.id.submit_button);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String password,email,password2;
                password2 = etPassword2.getText().toString();
                password = etPassword.getText().toString();
                email=etEmail.getText().toString();

                if(email.isEmpty() && password.isEmpty() && password2.isEmpty()){
                    Toast.makeText(Register.this, "The field is empty",Toast.LENGTH_SHORT).show();
                }else if(email.isEmpty()){
                    Toast.makeText(Register.this, "Email is empty",Toast.LENGTH_SHORT).show();
                }else if(password.isEmpty()){
                    Toast.makeText(Register.this, "Password is empty",Toast.LENGTH_SHORT).show();
                }else if(password2.isEmpty()){
                    Toast.makeText(Register.this, "Confirm Password is empty",Toast.LENGTH_SHORT).show();
                }else if(!password.equals(password2)){
                    Toast.makeText(Register.this, "Password is empty",Toast.LENGTH_SHORT).show();
                }else{
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(Register.this, "Register Unsuccessful",Toast.LENGTH_SHORT).show();
                            }else{

                                Toast.makeText(Register.this, "Register Successfully",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Register.this, UserRegister.class));
                            }
                        }
                    });
                }//end of if else
            }
        });//end of submit btn

    }




}
