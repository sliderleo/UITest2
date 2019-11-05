package com.example.uitest;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity{
    private Button registerBtn,loginBtn;
    private EditText etUsername,etPassword;
    String username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EditText UsernameET,PasswordET;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //buttons
        registerBtn=(Button) findViewById(R.id.register_button);
        loginBtn=(Button)findViewById(R.id.login_button);
        //editText
        etUsername=(EditText)findViewById(R.id.login_username);
        etPassword=(EditText)findViewById(R.id.login_password);
        //onClickListener
        //registerBtn.setOnClickListener(this);
    }


    public void onLogin(View v) {
        String username= etUsername.getText().toString();
        String password= etPassword.getText().toString();
        String type = "login";
        LoginBackground backgroundWorker = new LoginBackground(this);
        backgroundWorker.execute(type,username,password);
    }

}
