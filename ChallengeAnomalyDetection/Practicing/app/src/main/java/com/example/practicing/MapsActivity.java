package com.example.practicing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, SensorEventListener {

    private static final String TAG = "MapsActivity";

    private GoogleMap mMap;
    private SensorManager sensorManager;
    private Sensor accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Created mapsactivity instance");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Log.d(TAG, "onCreate: Reached end of onCreate");

        Log.d(TAG, "onCreate: Initializing Sensor services");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(MapsActivity.this, accelerometer, sensorManager.SENSOR_DELAY_NORMAL);
        Log.d(TAG, "onCreate: Registered gyroscope and accelerometer");
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

//        // Add a marker in Sydney and move the camera
//        LatLng uTwente = new LatLng(52.2398, 6.8500);
//        addCustomMarker(googleMap, uTwente, "severe");
//
        LatLng OnO = new LatLng(52.2387, 6.8561);
//        addCustomMarker(googleMap, OnO, "bad");
        mMap.moveCamera(CameraUpdateFactory.newLatLng(OnO));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
    }

    public void addCustomMarker(GoogleMap googleMap, LatLng position, int severity) {
        BitmapDescriptor image = null;
        String title = null;

        Log.d(TAG, "addCustomMarker: severity: "+ severity);

        switch (severity) {
            case 4:
                image = BitmapDescriptorFactory.fromResource(R.drawable.red);
                title = "Severe anomaly";
                break;
            case 3:
                image = BitmapDescriptorFactory.fromResource(R.drawable.orange);
                title = "Anomaly";
                break;
            case 2:
                image = BitmapDescriptorFactory.fromResource(R.drawable.yellow);
                title = "Speed bump";
                break;
            case 1:
                image = BitmapDescriptorFactory.fromResource(R.drawable.blue);
                title = "Hill";
                break;
            default:
                image = BitmapDescriptorFactory.fromResource(R.drawable.green);
                title = "Nothing wrong";
                break;
        }

        if (googleMap != null) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title(title)
                    .icon(image)
            );
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d(TAG, "onSensorChanged: Sensor has changed: " + sensorEvent.sensor.getType());

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER && anomalyCode(sensorEvent.values[2]) > -1){
            int anomalyCode = anomalyCode(sensorEvent.values[2]);
            Log.d(TAG, "Anomaly spotted");
            GPStracker g = new GPStracker(getApplicationContext());
            Location l = g.getLocation();

            if (l != null) {
                Log.d(TAG, "Retrieving GPS coordinates" );
                double lat = l.getLatitude();
                double lon = l.getLongitude();
//                Toast.makeText(getApplicationContext(), "lat: " + lat + "lon: " + lon, Toast.LENGTH_LONG).show();
                LatLng position = new LatLng(lat, lon);
                addCustomMarker(mMap, position, anomalyCode);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private int anomalyCode(float value) {
        Log.d(TAG, "Checking if anomaly ...");
        float treshold = 4;

        if((value - 9.81) < treshold || (value - 9.81) > -treshold){
            Log.d(TAG, "anomalyCode: anomaly found");
            return 4;
        } else {
            return 0;
        }
    }
}
