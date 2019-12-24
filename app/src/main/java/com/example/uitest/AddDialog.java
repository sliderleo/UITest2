package com.example.uitest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddDialog extends AppCompatDialogFragment {
    private EditText etPlate,etModel;
    private Spinner spinnerBrand,spinnerColor,spinnerInsurance;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_add_vehicle, null);
        etPlate=view.findViewById(R.id.et_car_plate);
        etModel=view.findViewById(R.id.et_car_model);
        spinnerBrand=view.findViewById(R.id.spinner_brand);
        spinnerColor=view.findViewById(R.id.spinner_color);
        spinnerInsurance=view.findViewById(R.id.spinner_insurance);

        builder.setView(view).setTitle("Add New Vehicle").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

            }
        }).setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                String carPlate = etPlate.getText().toString();
                String model=etModel.getText().toString();
                String brand=spinnerBrand.getSelectedItem().toString();
                String color=spinnerColor.getSelectedItem().toString();
                String insurance = spinnerInsurance.getSelectedItem().toString();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String id = user.getUid();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference().child("Car").child(id);
                String carId=myRef.push().getKey();

                CarInfo car = new CarInfo(carPlate,brand,model,color,carId,insurance);
                myRef.child(carId).setValue(car);

            }
        });

        return builder.create();
    }
}
