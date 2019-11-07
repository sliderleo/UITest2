package com.example.uitest;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity{
    private EditText etUsername,etPassword;
    String username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //editText
        etUsername=findViewById(R.id.login_username);
        etPassword=findViewById(R.id.login_password);


    }

    public void toRegister(View v){
        Intent i;
        i = new Intent(this, Register.class);startActivity(i);
    }

    public void onLogin(View v) {
        String username= etUsername.getText().toString();
        String password= etPassword.getText().toString();
        String type = "login";
        LoginBackground backgroundWorker = new LoginBackground(this);
        backgroundWorker.execute(type,username,password);
    }


}
