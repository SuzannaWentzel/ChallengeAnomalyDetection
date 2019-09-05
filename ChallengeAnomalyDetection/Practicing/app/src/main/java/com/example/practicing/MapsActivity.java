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
import android.util.Pair;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.lang.Object;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, SensorEventListener {

    private static final String TAG = "MapsActivity";

    private GoogleMap mMap;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private List<Float> temp;               // temp is the previous three measurements of the zvalue of the accelerometer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        temp = new ArrayList<>();

        // initialize accelerometer and set listener on the sensor
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

        // move camera to O&O
        LatLng OnO = new LatLng(52.2387, 6.8561);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(OnO));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
    }

    // function for adding custom markers (colored pointers and green dot)
    public void addCustomMarker(GoogleMap googleMap, LatLng position, int severity, float smoothedAccZ) {
        BitmapDescriptor image;
        String title;

        switch (severity) {
            case 4:
                image = BitmapDescriptorFactory.fromResource(R.drawable.red);
                title = "Severe anomaly, z: " + smoothedAccZ;
                break;
            case 3:
                image = BitmapDescriptorFactory.fromResource(R.drawable.orange);
                title = "Anomaly, z: " + smoothedAccZ;
                break;
            case 2:
                image = BitmapDescriptorFactory.fromResource(R.drawable.yellow);
                title = "Mild anomaly, z: " + smoothedAccZ;
                break;
            default:
                image = BitmapDescriptorFactory.fromResource(R.drawable.green2);
                title = "Route"; // (for showing route)
                break;
        }

        // add marker to maps
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
//        Log.d(TAG, "onSensorChanged: Accelerometer values: " + "X: " + sensorEvent.values[0] + ", Y: " + sensorEvent.values[1] + ", Z: " + sensorEvent.values[2]);

        // check if sensortype is accelerometer, and if marker needs to be set
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){

            // get GPS location
            List<Float> anomalyCode = anomalyCode(sensorEvent.values[2]);
            GPStracker g = new GPStracker(getApplicationContext());
            Location l = g.getLocation();

            // check if location could be retrieved
            if (l != null) {
                // add marker
                double lat = l.getLatitude();
                double lon = l.getLongitude();
                LatLng position = new LatLng(lat, lon);
                addCustomMarker(mMap, position, Math.round(anomalyCode.get(0)), anomalyCode.get(1));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    // function that returns severity of anomaly (0, 2, 3, 4, where 0 is no anomaly and 4 is
    // severe anomaly)
    private List<Float> anomalyCode(float zvalue) {
        List<Float> codeResult = new ArrayList<>();
        float res;

        // check if temp has enough measurements
        if (temp.size() < 3) {          // not enough, adding this to temp and returning this value
            temp.add(zvalue);
            res = zvalue;
        } else {
            // has enough measurements, apply smoothing by copying temp, sorting the copy and taking
            // the median
            temp.remove(0);             // remove oldest value
            temp.add(zvalue);           // add newest value

            // copy the temporary measurements
            List<Float> copy = new ArrayList<>();
            copy.add(temp.get(0));
            copy.add(temp.get(1));
            copy.add(temp.get(2));

            // sort the copy
            Collections.sort(copy);

            // find median
            res = copy.get(1);
        }

        // defined tresholds:
        float tresholdSevere = (float) 4;
        float tresholdBad = (float) 2.6;
        float tresholdSpeedbump = (float) 1.6;
        float tresholdHill = (float) 0.8;



        // check for anomalies, classify anomaly in severity
        if((res - 9.81) > tresholdSevere || (res - 9.81) < -tresholdSevere){    // severe
            Log.d(TAG, "anomalyCode: anomaly found");
            codeResult.add((float) 4);
        } else if ((res - 9.81) > tresholdBad || (res - 9.81) < -tresholdBad){  // bad
            codeResult.add((float) 3);
        } else if ((res - 9.81) > tresholdSpeedbump || (res - 9.81) < -tresholdSpeedbump) { // mild
            codeResult.add((float) 2);
        } else {
            codeResult.add((float) 0);
        }

        codeResult.add(res);
        return codeResult;
    }
}
