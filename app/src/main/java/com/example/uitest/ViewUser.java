package com.example.uitest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewUser extends AppCompatDialogFragment {
    private EditText etName,etGender,etDate,etContact,etDesc;
    private String viewId;
    CircleImageView imgv;
    DatabaseReference myRef,mDatabaseRef;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_view_user, null);
        viewId=getArguments().getString("id");
        imgv=view.findViewById(R.id.circle_imgageview);
        etName= view.findViewById(R.id.view_name);
        etGender=view.findViewById(R.id.view_gender);
        etContact=view.findViewById(R.id.view_contact);
        etDate=view.findViewById(R.id.view_dob);
        etDesc=view.findViewById(R.id.view_desc);

        final FirebaseDatabase mdatabase = FirebaseDatabase.getInstance();
        mDatabaseRef= mdatabase.getReference().child("Upload");
        //etName.setText(viewId);

        mDatabaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String name =dataSnapshot.child("mName").getValue().toString();
                if(name.equals(viewId)){
                    String links = dataSnapshot.child("mImageUrl").getValue().toString();
                    Picasso.get().load(links).into(imgv);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef= database.getReference().child("Users").child(viewId);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String gender= dataSnapshot.child("gender").getValue().toString();
                String dob = dataSnapshot.child("dob").getValue().toString();
                String contact=dataSnapshot.child("contact").getValue().toString();
                String desc = dataSnapshot.child("desc").getValue().toString();
                etName.setText(name);
                etGender.setText(gender);
                etDate.setText(dob);
                etContact.setText(contact);
                etDesc.setText(desc);

                etName.setEnabled(false);
                etGender.setEnabled(false);
                etDate.setEnabled(false);
                etContact.setEnabled(false);
                etDesc.setEnabled(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        builder.setView(view).setTitle("User Profile").setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }
}
