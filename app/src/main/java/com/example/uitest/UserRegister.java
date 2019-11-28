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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserRegister extends AppCompatActivity {
    private EditText etDob,etName,etContact;
    private DatePickerDialog picker;
    private Spinner sGender,sUser;
    private Button submit_button,select_button;
    ImageView imgv;
    String uri2;
    StorageReference mStorageRef;
    DatabaseReference mDatabase;
    FirebaseStorage storage;
    FirebaseUser userS;
    public Uri imguri;
    String name,spinnerGender,spinnerUser,contactString,dob,id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        etName=findViewById(R.id.et_name);
        etContact=findViewById(R.id.et_contact);
        imgv=findViewById(R.id.image_view);
        storage=FirebaseStorage.getInstance();
        userS = FirebaseAuth.getInstance().getCurrentUser();
        mStorageRef= storage.getReference("Images");
        mDatabase = FirebaseDatabase.getInstance().getReference("Upload");
        //spinner
        sUser = findViewById(R.id.spinner_user);
        sGender =findViewById(R.id.spinner_gender);

        //dob settings
        etDob= findViewById(R.id.et_dob);
        etDob.setInputType(InputType.TYPE_NULL);
        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr= Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                picker = new DatePickerDialog(UserRegister.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        etDob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                },year,month,day);
                picker.show();
            }
        });

        select_button=findViewById(R.id.select_button);
        select_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser();
            }
        });




        submit_button=findViewById(R.id.submit_button);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int contactNum=0;
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                id = user.getUid();

                spinnerGender = sGender.getSelectedItem().toString();
                spinnerUser=sUser.getSelectedItem().toString();
                name=etName.getText().toString();
                contactString=etContact.getText().toString();
                dob=etDob.getText().toString();

                if(contactString.isEmpty()){
                    Toast.makeText(UserRegister.this,"Contact number is empty!",Toast.LENGTH_SHORT).show();
                }else if(!contactString.isEmpty()){
                    contactNum=Integer.parseInt(contactString);
                }


                if(name.isEmpty()){
                    Toast.makeText(UserRegister.this,"Name is empty!",Toast.LENGTH_SHORT).show();
                }else if(dob.isEmpty()){
                    Toast.makeText(UserRegister.this,"DOB is empty!",Toast.LENGTH_SHORT).show();
                }else if (!(name.isEmpty() && dob.isEmpty())){
                    FileUploader();
                    UserInfo userInfo=new UserInfo(name,spinnerGender,spinnerUser,dob,contactString);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference().child("Users").child(id);
                    myRef.setValue(userInfo);

                    if(spinnerUser.equals("Tow Car Driver")){
                       addStatus();
                    }
                    Toast.makeText(UserRegister.this, "Added",Toast.LENGTH_SHORT).show();
                    toLogin();
                }else{
                    Toast.makeText(UserRegister.this,"Error Occurred!",Toast.LENGTH_SHORT).show();
                }



            }
        });

}
public void toLogin(){
    FirebaseAuth.getInstance().signOut();
    Intent i = new Intent(UserRegister.this, Login.class);
    startActivity(i);
}

public void addStatus(){
    Status stat=new Status(name,id,"off");
    FirebaseDatabase databaseTow = FirebaseDatabase.getInstance();
    DatabaseReference towRef = databaseTow.getReference().child("Status").child(id);
    towRef.setValue(stat);
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
                                    Toast.makeText(UserRegister.this,"Upload Successful",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(UserRegister.this,"No Image Selected",Toast.LENGTH_SHORT).show();
        }


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imguri=data.getData();
            imgv.setImageURI(imguri);

        }

    }
}
