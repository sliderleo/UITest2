package com.example.uitest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class RequestTow extends AppCompatActivity {
    private Button onButton,offButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_tow);
        onButton=findViewById(R.id.on_button);
        offButton=findViewById(R.id.off_button);

    }
}
