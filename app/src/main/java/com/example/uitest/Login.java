package com.example.uitest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity{
    EditText et_email,et_password;
    Button login_btn;
    FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference loginref;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String id;
    private boolean check = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth=FirebaseAuth.getInstance();
        //editText
        et_email=findViewById(R.id.login_email);
        et_password=findViewById(R.id.login_password);
        login_btn=findViewById(R.id.login_button);
        database=FirebaseDatabase.getInstance();
        loginref= database.getReference("Users");


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=et_email.getText().toString().trim();
                String password=et_password.getText().toString().trim();

                if(email.isEmpty() && password.isEmpty()){
                    Toast.makeText(Login.this, "The field is empty",Toast.LENGTH_SHORT).show();
                }else if(email.isEmpty()){
                    Toast.makeText(Login.this, "Email is empty",Toast.LENGTH_SHORT).show();
                }else if(password.isEmpty()){
                    Toast.makeText(Login.this, "Password is empty",Toast.LENGTH_SHORT).show();
                }else{
                    firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(Login.this, "Error please try again",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(Login.this, "Logged in successfully",Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Login.this, MainActivity.class);
                                startActivity(i);
                            }
                        }
                    });
                }
            }
        });

        mAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFireBaseUSer = firebaseAuth.getCurrentUser();
                if(mFireBaseUSer !=null){
                   Toast.makeText(Login.this, "You are logged in",Toast.LENGTH_SHORT).show();
                   Intent i = new Intent(Login.this, MainActivity.class);
                   startActivity(i);
                }else{
                    Toast.makeText(Login.this, "Please login",Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    public void toRegister(View v){
        Intent i;
        i = new Intent(this, Register.class);startActivity(i);
    }


    @Override
    protected void onStart(){
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthStateListener);
    }

}
