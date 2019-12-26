package com.example.uitest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegisterType extends AppCompatActivity implements View.OnClickListener {
    private CardView user,tow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_type);

        user=findViewById(R.id.card_user);
        tow=findViewById(R.id.card_tow);
        user.setOnClickListener(this);
        tow.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Intent i;

        switch(v.getId()) {
            case R.id.card_user: i = new Intent(this, UserRegister.class);startActivity(i);break;
            case R.id.card_tow: i = new Intent(this, TowRegister.class);startActivity(i);break;

        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterType.this);
        builder.setTitle("Return to the previous page");
        builder.setMessage("Are you sure ?")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
