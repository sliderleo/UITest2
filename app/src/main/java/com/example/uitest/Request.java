package com.example.uitest;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Request extends FragmentActivity implements OnMapReadyCallback , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private TextView  tv_selected_workshop,tv_fare;
    ImageButton backBtn,refreshBtn;
    private Spinner car_spinner1,paymentSpinner;
    private GoogleMap mMap;
    private DatabaseReference myRef, carRef, towRef, userRef;
    private FirebaseDatabase database;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private LatLng destinationLatLng;
    private Marker currentUserLocationMarker, markerWorkshop;
    private Button requestBtn,requestListBtn;
    private static final int REQUEST_USER_LOCATION_CODE = 99;
    private double userLat, userLong, locationLat, locationLong;
    private android.location.LocationListener locationListener;
    private List<String> carList = new ArrayList<String>();
    private ArrayList<String> mDriver = new ArrayList<>();
    private List<String> insuranceList = new ArrayList<>();
    String id, towid, locationName,myName,Dname,carplate,carinsurance;
    String driverName=null;
    ListView driverList;
    double price;
    LocationManager locationManager;
    List<String> towList,towIdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        car_spinner1 = findViewById(R.id.car_spinner);
        paymentSpinner=findViewById(R.id.payment_spinner);
        tv_selected_workshop = findViewById(R.id.selected_workshop);
        tv_fare=findViewById(R.id.fare_price);
        requestBtn = findViewById(R.id.request_button);
        driverList=findViewById(R.id.driver_list);
        requestListBtn=findViewById(R.id.request_list_button);
        backBtn=findViewById(R.id.back_button);
        refreshBtn=findViewById(R.id.refresh_button);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        id = user.getUid();

        final ArrayAdapter<String> rArrayAdapter=new ArrayAdapter<>(getApplicationContext(),R.layout.textview,mDriver);
        driverList.setAdapter(rArrayAdapter);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Workshop");
        carRef = database.getReference("Car").child(id);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkUserLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapR);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        requestListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Request.this,ViewRequestList.class);
                startActivity(i);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String name = dataSnapshot.child("name").getValue().toString();
                String latitudeString = dataSnapshot.child("latitude").getValue().toString();
                String longitudeString = dataSnapshot.child("longitude").getValue().toString();
                double latitude = Double.parseDouble(latitudeString);
                double longitude = Double.parseDouble(longitudeString);
                LatLng location = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(location).title(name));
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


        Query query = carRef;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String car = dataSnapshot1.child("plate").getValue(String.class);
                    String cinsurance = dataSnapshot1.child("insurance").getValue(String.class);
                    insuranceList.add(cinsurance);
                    carList.add(car);
                }
                if(carList.isEmpty()){
                    String text="No Vehicle Registered";
                    carList.add(text);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Request.this, android.R.layout.simple_spinner_item, carList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                car_spinner1.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        driverList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String driverId =towIdList.get(position);
                ViewUser dialog = new ViewUser();
                Bundle bundle = new Bundle();
                bundle.putString("id",driverId);
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(),"View User");
                return true;
            }
        });

        driverList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                driverName=towIdList.get(position);
                Dname = mDriver.get(position);
                Toast.makeText(Request.this, Dname +" Selected!", Toast.LENGTH_SHORT).show();
            }
        });

        towRef = database.getReference("Status");
        Query towQuery = towRef;

        towQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                towIdList=new ArrayList<>();
                if(rArrayAdapter.getCount()!=0){
                    mDriver.clear();
                    towIdList.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        String dutyString = dataSnapshot1.child("duty").getValue().toString();

                        if (dutyString.equals("on")) {
                            String driver = dataSnapshot1.child("name").getValue().toString();
                            String towData=dataSnapshot1.child("userId").getValue().toString();
                            mDriver.add(driver);
                            towIdList.add(towData);
                        } else if (dutyString == "off") {}
                    }

                    rArrayAdapter.notifyDataSetChanged();
                }else{
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        String dutyString = dataSnapshot1.child("duty").getValue().toString();

                        if (dutyString.equals("on")) {
                            String driver = dataSnapshot1.child("name").getValue().toString();
                            String towData=dataSnapshot1.child("userId").getValue().toString();
                            mDriver.add(driver);
                            towIdList.add(towData);
                        } else if (dutyString == "off") {}
                    }

                    rArrayAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        myRef= database.getReference().child("Users").child(id);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String name = dataSnapshot.child("name").getValue().toString();
                String contact=dataSnapshot.child("contact").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        car_spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                carplate=carList.get(position);
                carinsurance=insuranceList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String car = car_spinner1.getSelectedItem().toString();
                final String payment =paymentSpinner.getSelectedItem().toString();
                if(locationLat==0 && locationLong==0){
                    Toast.makeText(Request.this,"Please select a workshop!" , Toast.LENGTH_SHORT).show();
                }else if(mDriver.isEmpty() && towIdList.isEmpty()){
                    Toast.makeText(Request.this,"There is no tow car driver around, please try again later!" , Toast.LENGTH_SHORT).show();
                }else if (car == "No Vehicle Registered"){
                    Toast.makeText(Request.this,"Please add your vehicle at the Vehicle page!" , Toast.LENGTH_SHORT).show();
                }else{
                    readData(new MyCallback() {
                        @Override
                        public void onCallback(String name,String contact) {
                            final String nameS = name;
                            final String contactS=contact;
                            AlertDialog.Builder builder = new AlertDialog.Builder(Request.this);
                            builder.setTitle("Requesting for tow ..");
                            builder.setMessage("Note: We only accept COD only. If you want to use your insurance coverage, please mention to the Tow Driver when meet and provide the require information for the claim.\n Proceed if you agree.");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DatabaseReference reqRef=database.getReference().child("Request");
                                    String reqId=reqRef.push().getKey();

                                    RequestInfo info = new RequestInfo(nameS,contactS,userLat,userLong,locationLat,locationLong,locationName,id,driverName,Dname,car,price,"Pending",reqId,carinsurance,payment);
                                    reqRef.child(reqId).setValue(info);
                                    Toast.makeText(Request.this,"Request Successfully" , Toast.LENGTH_SHORT).show();
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();


                        }
                    });

                }

            }
        });



    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng position = marker.getPosition();
                locationName = marker.getTitle();
                //workshop lat and long
                locationLat = position.latitude;
                locationLong = position.longitude;
                Toast.makeText(Request.this, locationName + " is selected.", Toast.LENGTH_SHORT).show();
                tv_selected_workshop.setText("Selected Workshop: " + locationName);
                destinationLatLng = position;
                Location mylocation = new Location("");
                Location dest_location = new Location("");
                mylocation.setLatitude(userLat);
                mylocation.setLongitude(userLong);
                dest_location.setLatitude(locationLat);
                dest_location.setLongitude(locationLong);
                double distance = (mylocation.distanceTo(dest_location))/1000;
                if(distance >20){
                    double newd  = distance-20;
                    price = (newd*2.5)+80;
                }else{
                    price=80;
                }

                DecimalFormat d = new DecimalFormat("0.00");
                tv_fare.setText("Price: RM"+d.format(price));
                return true;

            }
        });
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
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
        lastLocation = location;

        if (currentUserLocationMarker != null) {
            currentUserLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("User Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        //user location
        userLat = location.getLatitude();
        userLong = location.getLongitude();
        final String latitude = Double.toString(location.getLatitude());
        final String longitude = Double.toString(location.getLongitude());





        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(12));

        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }

    }

    public boolean checkUserLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_USER_LOCATION_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_USER_LOCATION_CODE);

            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_USER_LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (googleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }


    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).
                        addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    public interface MyCallback {
        void onCallback(String name,String contact);
    }

    public void readData(final MyCallback myCallback) {
        database.getReference().child("Users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String contact=dataSnapshot.child("contact").getValue().toString();
                myCallback.onCallback(name,contact);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
