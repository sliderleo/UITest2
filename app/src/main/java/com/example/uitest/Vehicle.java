package com.example.uitest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Vehicle extends AppCompatActivity{
    private ImageButton backBtn,addBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);

        backBtn = (ImageButton) findViewById(R.id.backArrow);
        addBtn=(ImageButton)findViewById(R.id.addButton);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    public void openDialog(){
        AddDialog dialog = new AddDialog();
        dialog.show(getSupportFragmentManager(),"Example");
    }
}
