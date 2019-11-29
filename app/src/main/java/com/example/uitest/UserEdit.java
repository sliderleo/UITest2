package com.example.uitest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserEdit extends AppCompatActivity {
    private EditText etName,etdob,etContact,etDesc;
    private Spinner spinnerGender;
    private Button editButton,selectButton,uploadButton;
    CircleImageView circleImageView;
    DatePickerDialog picker;
    String id,uri2;
    FirebaseDatabase database;
    DatabaseReference myRef,mDatabase;
    StorageReference mStorageRef;
    FirebaseStorage storage;
    FirebaseUser user,userS;
    public Uri imguri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);
        userS = FirebaseAuth.getInstance().getCurrentUser();
        circleImageView=findViewById(R.id.circle_imageview);
        selectButton= (Button) findViewById(R.id.ue_select_image_button);
        uploadButton= (Button) findViewById(R.id.ue_upload_image_button);
        etDesc=findViewById(R.id.edit_desc);
        etName=findViewById(R.id.edit_name);
        etdob=findViewById(R.id.edit_dob);
        etContact=findViewById(R.id.edit_contact);
        spinnerGender=findViewById(R.id.edit_spinner_gender);
        editButton=findViewById(R.id.edit_button);
        etdob.setInputType(InputType.TYPE_NULL);
        storage=FirebaseStorage.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Upload");
        mStorageRef= storage.getReference("Images");

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUploader();
            }
        });

        etdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr= Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                picker = new DatePickerDialog(UserEdit.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        etdob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                },year,month,day);
                picker.show();
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        id = user.getUid();

        database= FirebaseDatabase.getInstance();
        myRef=database.getReference().child("Users").child(id);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                etName.setText(dataSnapshot.child("name").getValue().toString());
                etdob.setText(dataSnapshot.child("dob").getValue().toString());
                etContact.setText(dataSnapshot.child("contact").getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.getMessage();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name,spinnerGenderString,contactString,dob;
                int contactNum=0;

                spinnerGenderString = spinnerGender.getSelectedItem().toString();
                name=etName.getText().toString();
                contactString=etContact.getText().toString();
                dob=etdob.getText().toString();

                if(name.isEmpty()){
                    Toast.makeText(UserEdit.this,"Name is empty!",Toast.LENGTH_SHORT).show();
                }else if(dob.isEmpty()){
                    Toast.makeText(UserEdit.this,"DOB is empty!",Toast.LENGTH_SHORT).show();
                }else if(contactString.isEmpty()){
                    Toast.makeText(UserEdit.this,"Contact is empty!",Toast.LENGTH_SHORT).show();

                }else if (!(name.isEmpty() && dob.isEmpty() && contactString.isEmpty())){
                     myRef = database.getReference().child("Users").child(id);
                    myRef.child("name").setValue(name);
                    myRef.child("contact").setValue(contactString);
                    myRef.child("dob").setValue(dob);
                    myRef.child("gender").setValue(spinnerGenderString);
                    Toast.makeText(UserEdit.this, "Edited",Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(UserEdit.this,"Error Occurred!",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap =MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void FileChooser(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,1);
    }



    private void FileUploader(){
        if(imguri !=null){
            final StorageReference ref = mStorageRef.child(userS.getUid()+"."+getExtension(imguri));

            ref.putFile(imguri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadlink=uri.toString();
                                    Toast.makeText(UserEdit.this,"Upload Successful",Toast.LENGTH_SHORT).show();
                                    String name = userS.getUid();
                                    UploadInfo upload = new UploadInfo(name,downloadlink);
                                    mDatabase.child(userS.getUid()).setValue(upload);
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                }
            });
        }else{
            Toast.makeText(UserEdit.this,"No Image Selected",Toast.LENGTH_SHORT).show();
        }


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imguri=data.getData();
            circleImageView.setImageURI(imguri);

        }

    }
}
