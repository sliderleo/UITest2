package com.example.uitest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private Button registerBtn,loginBtn;
    private EditText etUsername,etPassword;
    String username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //buttons
        registerBtn=(Button) findViewById(R.id.register_button);
        loginBtn=(Button)findViewById(R.id.login_button);
        //editText
        etUsername=(EditText)findViewById(R.id.login_username);
        etPassword=(EditText)findViewById(R.id.login_password);
        //onClickListener
        registerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()){
            case R.id.register_button: i = new Intent(this,Register.class);startActivity(i);break; // redirect to register activity
            case R.id.login_button: login(); break;
        }
    }

    public void login(){ //login function
        username=etUsername.getText().toString();
        password=etPassword.getText().toString();
    }
}
