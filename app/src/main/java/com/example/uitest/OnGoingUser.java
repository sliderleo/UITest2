package com.example.uitest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class OnGoingUser extends FragmentActivity implements OnMapReadyCallback , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    TextView tv_name,tv_distance;
    CircleImageView circleImg;
    private Button rateBtn;
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    LocationManager locationManager;
    private Location lastLocation,userLocation,towDriverLocation;
    private LatLng destinationLatLng;
    private Marker currentUserLocationMarker, markerWorkshop,towDriverMarker;
    private static final int REQUEST_USER_LOCATION_CODE = 99;
    private double userLat, userLong, locationLat, locationLong;
    private android.location.LocationListener locationListener;
    DatabaseReference mDatabaseRef,myRef,towDLocRef,requestRef;
    private final String CHANNEL_ID = "personal_noti";
    private final int NOTIFICATION_ID = 001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_going_user);
        final String towId=getIntent().getStringExtra("towId");
        final String reqId = getIntent().getStringExtra("requestId");
        rateBtn=findViewById(R.id.rating_button);
        circleImg=findViewById(R.id.imgview_circle);
        tv_name=findViewById(R.id.name_tv);
        tv_distance=findViewById(R.id.distance_tv);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkUserLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabaseRef= database.getReference().child("Upload");
        myRef= database.getReference().child("Users").child(towId);
        towDLocRef=database.getReference().child("CurrentLocation").child(towId);
        requestRef=database.getReference().child("Request").child(reqId);
        userLocation = new Location("");
        towDriverLocation = new Location("");

        rateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OnGoingUser.this,RatingUI.class);
                i.putExtra("towId",towId);
                startActivity(i);
                rateBtn.setEnabled(false);
            }
        });


        mDatabaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String name =dataSnapshot.child("mName").getValue().toString();
                if(name.equals(towId)){
                    String links = dataSnapshot.child("mImageUrl").getValue().toString();
                    Picasso.get().load(links).into(circleImg);
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

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                tv_name.setText("Name: "+name);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        towDLocRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(towDriverMarker !=null){
                    towDriverMarker.remove();
                }

                String towLat = dataSnapshot.child("userlat").getValue().toString();
                String towLong = dataSnapshot.child("userlong").getValue().toString();
                double latitude = Double.parseDouble(towLat);
                double longitude = Double.parseDouble(towLong);
                LatLng towDLocation = new LatLng(latitude,longitude);
                towDriverLocation.setLatitude(latitude);
                towDriverLocation.setLongitude(longitude);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(towDLocation);
                markerOptions.title("Tow Driver Location");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                towDriverMarker=mMap.addMarker(markerOptions);

                DecimalFormat d = new DecimalFormat("0.00");
                double distance = (userLocation.distanceTo(towDriverLocation))/1000;
                tv_distance.setText("Distance: "+d.format(distance)+"km");

                if(distance<0.5){
                    NotificationCompat.Builder builder= new NotificationCompat.Builder(OnGoingUser.this,CHANNEL_ID);
                    builder.setSmallIcon(R.drawable.person_black);
                    builder.setContentTitle("Tow driver is near you ...");
                    builder.setContentText("Tow driver is reaching soon");
                    builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(OnGoingUser.this);
                    notificationManagerCompat.notify(NOTIFICATION_ID,builder.build());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        requestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String workLatS= dataSnapshot.child("workshopLat").getValue().toString();
                String workLongS = dataSnapshot.child("workshopLong").getValue().toString();
                double workLat = Double.parseDouble(workLatS);
                double workLong = Double.parseDouble(workLongS);
                LatLng workloc = new LatLng(workLat,workLong);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(workloc);
                markerOptions.title("Destination");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                markerWorkshop=mMap.addMarker(markerOptions);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        //user location
        userLat = location.getLatitude();
        userLong = location.getLongitude();
        final String latitude = Double.toString(location.getLatitude());
        final String longitude = Double.toString(location.getLongitude());
        double userlat=Double.parseDouble(latitude);
        double userlong=Double.parseDouble(longitude);
        userLocation.setLatitude(userlat);
        userLocation.setLongitude(userlong);
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
}
