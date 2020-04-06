package com.openinstitute.nuru;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.Dialog;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    String map_title = "Post Location";
    String cLatitude = "-1.258636";
    String cLongitude = "36.80578439999999";


    SupportMapFragment mapFragment;
    GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);*/


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            String post_coordinates = bundle.getString("post_coordinates");
            String[] coordinates = post_coordinates.split(",");
            cLatitude = coordinates[0];
            cLongitude = coordinates[1];

        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getApplicationContext());
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

        // Add a marker in Sydney, Australia, and move the camera.
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Default"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        LatLng rage = new LatLng(Double.parseDouble(cLatitude), Double.parseDouble(cLongitude));
        mMap.addMarker(new MarkerOptions().position(rage).title("Marker at " + map_title));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(rage));*/

    }


    /*
    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getApplicationContext());
        mGoogleMap = googleMap;
        mGoogleMap.setMyLocationEnabled(true);


        LatLng point = new LatLng(Double.parseDouble(cLatitude), Double.parseDouble(cLongitude));
        drawMarker(point);

    }
     */

    private void drawMarker(LatLng point){
        mGoogleMap.clear();
        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(point);

        markerOptions.title("Post Taken from here");


        markerOptions.snippet("Latitude:" + point.latitude + ",Longitude:" + point.longitude);

        mGoogleMap.addMarker(markerOptions);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(point));

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
   /* @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        LatLng rage = new LatLng(Double.parseDouble(cLatitude), Double.parseDouble(cLongitude));
        mMap.addMarker(new MarkerOptions().position(rage).title("Marker at " + map_title));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(rage));
    }*/
}