package com.example.practicing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.nio.charset.MalformedInputException;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        // Add a marker in Sydney and move the camera
        LatLng uTwente = new LatLng(52.2398, 6.8500);
        addCustomMarker(googleMap, uTwente, "severe");

        LatLng OnO = new LatLng(52.2387, 6.8561);
        addCustomMarker(googleMap, OnO, "bad");
        mMap.moveCamera(CameraUpdateFactory.newLatLng(uTwente));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
    }

    public void addCustomMarker(GoogleMap googleMap, LatLng position, String severity) {
        BitmapDescriptor image;
        String title;

        if (severity.equals("severe")) {
            image = BitmapDescriptorFactory.fromResource(R.drawable.red);
            title = "Severe anomaly";
        } else if (severity.equals("bad")) {
            image = BitmapDescriptorFactory.fromResource(R.drawable.orange);
            title = "Anomaly";
        } else if (severity.equals("bump")) {
            image = BitmapDescriptorFactory.fromResource(R.drawable.yellow);
            title = "Speed bump";
        } else if (severity.equals("hill")) {
            image = BitmapDescriptorFactory.fromResource(R.drawable.blue);
            title = "Hill";
        } else {
            image = BitmapDescriptorFactory.fromResource(R.drawable.green);
            title = "Nothing wrong";
        }

        Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(position)
                .title(title)
                .icon(image)
        );
    }
}
