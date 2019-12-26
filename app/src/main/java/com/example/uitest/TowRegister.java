package com.example.uitest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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

import java.util.ArrayList;
import java.util.Calendar;

public class TowRegister extends AppCompatActivity implements InsuranceDialog.onInsuranceDialog{
    private EditText etDob,etName,etContact,etDesc,etCompanyname,etInsurance;
    private DatePickerDialog picker;
    private Spinner sGender;
    private Button submit_button,select_button,pdf_button;
    ImageView imgv;
    String uri2;
    StorageReference mStorageRef,mCompanyRef;
    DatabaseReference mDatabase,mComDatabase;
    FirebaseStorage storage;
    FirebaseUser userS;
    public Uri imguri;
    String name,spinnerGender,contactString,dob,id,desc,companyname,insurancecover;
    final static int PICK_PDF_CODE = 2342;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tow_register);
        etDesc=findViewById(R.id.et_desc);
        etName=findViewById(R.id.et_name);
        etContact=findViewById(R.id.et_contact);
        etCompanyname=findViewById(R.id.et_company_name);
        etInsurance=findViewById(R.id.et_insurance);
        imgv=findViewById(R.id.image_view);
        storage=FirebaseStorage.getInstance();
        userS = FirebaseAuth.getInstance().getCurrentUser();
        mStorageRef= storage.getReference("Images");
        mCompanyRef= storage.getReference("Files");
        mDatabase = FirebaseDatabase.getInstance().getReference("Upload");
        mComDatabase = FirebaseDatabase.getInstance().getReference("Company");
        sGender =findViewById(R.id.spinner_gender);

        etDob= findViewById(R.id.et_dob);
        etDob.setInputType(InputType.TYPE_NULL);
        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr= Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                picker = new DatePickerDialog(TowRegister.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        etDob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                },year,month,day);
                picker.show();
            }
        });

        select_button=findViewById(R.id.select_button);
        pdf_button=findViewById(R.id.pdf_button);
        select_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser();
            }
        });
        etInsurance.setInputType(InputType.TYPE_NULL);

        submit_button=findViewById(R.id.submit_button);

        etInsurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment insurancedialog = new InsuranceDialog();
                insurancedialog.setCancelable(false);
                insurancedialog.show(getSupportFragmentManager(),"Insurance");
            }
        });

        pdf_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPDF();
            }
        });

        select_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser();
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int contactNum=0;
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                id = user.getUid();
                desc=etDesc.getText().toString();
                spinnerGender = sGender.getSelectedItem().toString();
                name=etName.getText().toString();
                contactString=etContact.getText().toString();
                dob=etDob.getText().toString();


                if(contactString.isEmpty()){
                    Toast.makeText(TowRegister.this,"Contact number is empty!",Toast.LENGTH_SHORT).show();
                }else if(desc.isEmpty()){
                    Toast.makeText(TowRegister.this,"The description is empty!",Toast.LENGTH_SHORT).show();
                }else  if(name.isEmpty()){
                    Toast.makeText(TowRegister.this,"Name is empty!",Toast.LENGTH_SHORT).show();
                }else if(dob.isEmpty()) {
                    Toast.makeText(TowRegister.this, "DOB is empty!", Toast.LENGTH_SHORT).show();
                }else if (companyname.isEmpty()){
                    Toast.makeText(TowRegister.this, "Company name is empty!", Toast.LENGTH_SHORT).show();
                }else if(insurancecover.isEmpty()){
                    Toast.makeText(TowRegister.this, "Insurance coverage is empty!", Toast.LENGTH_SHORT).show();
                }else{
                    FileUploader();
                    UserInfo userInfo=new UserInfo(name,spinnerGender,"Tow Car Driver",dob,contactString,desc,"0");
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference().child("Users").child(id);
                    myRef.setValue(userInfo);
                    Toast.makeText(TowRegister.this, "Registered, please wait for admin to approve.",Toast.LENGTH_SHORT).show();
                    toLogin();
                }



            }
        });
    }




    public void toLogin(){
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(TowRegister.this, Login.class);
        finishAffinity();
        startActivity(i);
    }

    public void addStatus(){
        Status stat=new Status(name,id,"off");
        FirebaseDatabase databaseTow = FirebaseDatabase.getInstance();
        DatabaseReference myRef = databaseTow.getReference().child("Rating").child(id);
        DatabaseReference towRef = databaseTow.getReference().child("Status").child(id);
        towRef.setValue(stat);
        myRef.push().setValue(5);
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
                                    Toast.makeText(TowRegister.this,"Upload Successful",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(TowRegister.this,"No Image Selected",Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TowRegister.this);
        builder.setTitle("Return to the previous page");
        builder.setMessage("Are you sure ?")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                        Intent i = new Intent(TowRegister.this,RegisterType.class);
                        startActivity(i);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imguri=data.getData();
            imgv.setImageURI(imguri);
        }

        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                uploadFile(data.getData());
            }else{
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }

    }


    @Override
    public void onPositiveButtonClicked(String[] list, ArrayList<String> selectedItemList) {
        StringBuilder sb = new StringBuilder();
        for (String str:selectedItemList){
            sb.append(str+",");
        }
        etInsurance.setText(sb);
    }

    @Override
    public void onNegativeButtonClicked() {
        String text = "None";
        etInsurance.setText(text);
    }

    private void getPDF() {
        //for greater than lolipop versions we need the permissions asked on runtime
        //so if the permission is not available user will go to the screen to allow storage permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            return;
        }

        //creating an intent for file chooser
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_CODE);
    }


    private void uploadFile(Uri data) {
        StorageReference sRef = mCompanyRef.child(userS.getUid() + ".pdf");
        sRef.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        companyname=etCompanyname.getText().toString();
                        insurancecover=etInsurance.getText().toString();

                        if(companyname.isEmpty()){
                            Toast.makeText(TowRegister.this, "Company name is empty", Toast.LENGTH_SHORT).show();
                        }else if(insurancecover.isEmpty()){
                            Toast.makeText(TowRegister.this, "Insurance coverage is empty!", Toast.LENGTH_SHORT).show();
                        }else if(!companyname.isEmpty() && !insurancecover.isEmpty()){

                        }else{

                        }
                        Toast.makeText(TowRegister.this, "File Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        CompanyInfo upload = new CompanyInfo(companyname,id,insurancecover,taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                        mComDatabase.child(userS.getUid()).setValue(upload);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


    }
}
