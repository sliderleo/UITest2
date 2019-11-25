package com.example.uitest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.ArrayList;


public class RequestTow extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private Button onButton,offButton,acceptButton,rejectButton;
    private ChildEventListener mChildEventListener;
    private Location lastLocation;
    private TextView tv_status;
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private ListView rListView;
    private LocationRequest locationRequest;
    private Marker currentUserLocationMarker;
    private Info info,callerInfo;
    private static final int REQUEST_USER_LOCATION_CODE = 99;
    private LocationManager locationManager;
    private ArrayList<String> mReqList = new ArrayList<>();
    private ArrayList<String> reqId = new ArrayList<>();
    private ArrayList<String> callerId = new ArrayList<>();
    private ArrayList<LatLng> userLocation = new ArrayList<>();
    private ArrayList<LatLng> workshopLocation = new ArrayList<>();
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference myRef,mWorkshop,mRequest,requestRef;
    private String userId,callerName,text;
    LocationRequest locReq;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_tow);
        rejectButton=findViewById(R.id.reject_button);
        acceptButton=findViewById(R.id.accept_button);
        tv_status=findViewById(R.id.status_text);
        onButton=findViewById(R.id.on_button);
        offButton=findViewById(R.id.off_button);
        info=new Info();
        rListView=findViewById(R.id.request_list);
        final ArrayAdapter<String> rArrayAdapter=new ArrayAdapter<>(getApplicationContext(),R.layout.mytextview,mReqList);
        rListView.setAdapter(rArrayAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkUserLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        user= FirebaseAuth.getInstance().getCurrentUser();
        userId=user.getUid();
        database = FirebaseDatabase.getInstance();
        mRequest=database.getReference().child("Request");

        mRequest.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String towId=dataSnapshot.child("towDriverId").getValue().toString();
                if(userId.equals(towId)){
                    String locName=dataSnapshot.child("workshopName").getValue().toString();
                    String requestId=dataSnapshot.child("id").getValue().toString();
                    String callerName = dataSnapshot.child("userName").getValue().toString();
                    String contact = dataSnapshot.child("userContact").getValue().toString();
                    String callerID = dataSnapshot.child("userId").getValue().toString();
                    String status=dataSnapshot.child("status").getValue().toString();

                    String userSLong = dataSnapshot.child("userLong").getValue().toString();
                    String userSLat = dataSnapshot.child("userLat").getValue().toString();
                    double userLat=Double.parseDouble(userSLat);
                    double userLong=Double.parseDouble(userSLong);
                    LatLng userL=new LatLng(userLat,userLong);
                    userLocation.add(userL);

                    String workshopSLong = dataSnapshot.child("workshopLong").getValue().toString();
                    String workshopSLat = dataSnapshot.child("workshopLat").getValue().toString();
                    double workshopLat=Double.parseDouble(workshopSLat);
                    double workshopLong=Double.parseDouble(workshopSLong);
                    LatLng workshopL=new LatLng(workshopLat,workshopLong);
                    workshopLocation.add(workshopL);


                    String text = "Caller Name: "+callerName
                            +"\nContact: "+contact+"\nDrop off Location: "+locName+"\nStatus: "+status;
                    mReqList.add(text);
                    callerId.add(callerID);
                    reqId.add(requestId);

                    rArrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String towId=dataSnapshot.child("towDriverId").getValue().toString();
                if(userId.equals(towId)){
                    String locName=dataSnapshot.child("workshopName").getValue().toString();
                    String requestId=dataSnapshot.child("id").getValue().toString();
                    String callerName = dataSnapshot.child("userName").getValue().toString();
                    String contact = dataSnapshot.child("userContact").getValue().toString();
                    String callerID = dataSnapshot.child("userId").getValue().toString();
                    String status=dataSnapshot.child("status").getValue().toString();

                    String userSLong = dataSnapshot.child("userLong").getValue().toString();
                    String userSLat = dataSnapshot.child("userLat").getValue().toString();
                    double userLat=Double.parseDouble(userSLat);
                    double userLong=Double.parseDouble(userSLong);
                    LatLng userL=new LatLng(userLat,userLong);
                    userLocation.add(userL);

                    String workshopSLong = dataSnapshot.child("workshopLong").getValue().toString();
                    String workshopSLat = dataSnapshot.child("workshopLat").getValue().toString();
                    double workshopLat=Double.parseDouble(workshopSLat);
                    double workshopLong=Double.parseDouble(workshopSLong);
                    LatLng workshopL=new LatLng(workshopLat,workshopLong);
                    workshopLocation.add(workshopL);


                    String text = "Caller Name: "+callerName
                            +"\nContact: "+contact+"\nDrop off Location: "+locName+"\nStatus: "+status;
                    mReqList.add(text);
                    callerId.add(callerID);
                    reqId.add(requestId);

                    rArrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.getMessage();
            }
        });

        rListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mMap.clear();
                String content=reqId.get(position);
                LatLng workshoploc = workshopLocation.get(position);
                LatLng userloc = userLocation.get(position);
                mMap.addMarker(new MarkerOptions().position(workshoploc).title("Drop Off Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                mMap.addMarker(new MarkerOptions().position(userloc).title("User Current Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                info.setId(content);

            }
        });

        rListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String callerCon =callerId.get(position);
                Toast.makeText(RequestTow.this,callerCon,Toast.LENGTH_LONG).show();
                ViewUser dialog = new ViewUser();
                Bundle bundle = new Bundle();
                bundle.putString("id",callerCon);
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(),"View User");
                return true;
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String str=info.getId();
                if(str == null){
                    Toast.makeText(RequestTow.this,"Please select a request!!",Toast.LENGTH_LONG).show();
                }else{
                    mReqList.clear();
                    requestRef=database.getReference("Request/"+str);
                    requestRef.child("status").setValue("Accepted");
                    Toast.makeText(RequestTow.this,"Request Accepted!",Toast.LENGTH_LONG).show();

                }
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String str=info.getId();
                if(str == null){
                    Toast.makeText(RequestTow.this,"Please select a request!!",Toast.LENGTH_LONG).show();
                }else{
                    mReqList.clear();
                    requestRef=database.getReference("Request/"+str);
                    requestRef.child("status").setValue("Rejected");
                    Toast.makeText(RequestTow.this,"Request Rejected",Toast.LENGTH_LONG).show();

                }
            }
        });
        myRef = database.getReference().child("Status").child(userId);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String duty=dataSnapshot.child("duty").getValue().toString();

                if(duty.equals("on")){
                    tv_status.setText("Status: On Duty");
                }else if(duty.equals("off")){
                    tv_status.setText("Status: Off Duty");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.getMessage();
            }
        });

        onButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String duty = "on";
                myRef.child("duty").setValue(duty);
                tv_status.setText("Status: On Duty");
                Toast.makeText(RequestTow.this, "Status Updated",Toast.LENGTH_SHORT).show();
                callPermission();
            }
        });

        offButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String duty = "off";
                myRef.child("duty").setValue(duty);
                tv_status.setText("Status: Off Duty");
                Toast.makeText(RequestTow.this, "Status Updated",Toast.LENGTH_SHORT).show();
                onPause();
            }
        });

        database=FirebaseDatabase.getInstance();
        mWorkshop=database.getReference("Workshop");
        mWorkshop.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                String name = dataSnapshot.child("name").getValue().toString();
//                String latitudeString = dataSnapshot.child("latitude").getValue().toString();
//                String longitudeString = dataSnapshot.child("longitude").getValue().toString();
//                double latitude = Double.parseDouble(latitudeString);
//                double longitude=Double.parseDouble(longitudeString);
//                LatLng location=new LatLng(latitude,longitude);
//                mMap.addMarker(new MarkerOptions().position(location).title(name));
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


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }


    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest=new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest,this);
        }
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation=location;
        mMap.animateCamera(CameraUpdateFactory.zoomBy(12));

        if(googleApiClient != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
        }
    }

    public boolean checkUserLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_USER_LOCATION_CODE);
            }else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_USER_LOCATION_CODE);

            }
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case REQUEST_USER_LOCATION_CODE:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        if (googleApiClient == null){
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }else{
                    Toast.makeText(this,"Permission D9", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }




    protected synchronized  void buildGoogleApiClient(){
        googleApiClient=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).
                        addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    public void callPermission(){

        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        Permissions.check(this/*context*/, permissions, "Location permission are required", new Permissions.Options(), new PermissionHandler() {
            @Override
            public void onGranted() {
                requestLocation();
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                super.onDenied(context, deniedPermissions);
                callPermission();
            }
        });
    }

    public void requestLocation(){
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_GRANTED &&  ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PermissionChecker.PERMISSION_GRANTED) {
            fusedLocationProviderClient=new FusedLocationProviderClient(this);
            locReq=new LocationRequest();
            locReq.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locReq.setFastestInterval(2000);
            locReq.setInterval(4000);
            fusedLocationProviderClient.requestLocationUpdates(locReq,mLocationCallback,getMainLooper());

        }else callPermission();
    }

    LocationCallback mLocationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                updateLocation(location.getLatitude(),location.getLongitude());
            }
        }

    };
    @Override
    public void onPause() {
        super.onPause();
        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
        }
    }

    public void updateLocation(double driverLat,double driverLong){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("CurrentLocation").child(userId);
        LocationUpdate current = new LocationUpdate(driverLat,driverLong);
        myRef.setValue(current);
    }

    public void openDialog(){
        ViewUser dialog = new ViewUser();
        dialog.show(getSupportFragmentManager(),"View User");
    }

}