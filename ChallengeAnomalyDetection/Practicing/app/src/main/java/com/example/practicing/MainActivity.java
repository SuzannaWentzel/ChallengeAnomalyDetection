package com.example.practicing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
//
//    private SensorManager sensorManager;
//    Sensor accelerometer;
//
//    TextView zAccValue;
//
//    private boolean toMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        toMap = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        zAccValue = (TextView) findViewById(R.id.zAccValue);

//        Log.d(TAG, "onCreate: Initializing Sensor services");
//        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//
//        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        sensorManager.registerListener(MainActivity.this, accelerometer, sensorManager.SENSOR_DELAY_NORMAL);
//        Log.d(TAG, "onCreate: Registered gyroscope and accelerometer");

        Button btn = (Button) findViewById(R.id.openMaps);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
//                toMap = false;
                finish();
            }
        });
    }

//    @Override
//    public void onAccuracyChanged(Sensor sensor, int i) {
//
//    }

//    @Override
//    public void onSensorChanged(SensorEvent sensorEvent) {
//        Sensor sensor = sensorEvent.sensor;
//
//        if (!toMap) {
//            finish();
//        }
//
//        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//            Log.d(TAG, "onAcceleroSensorChanged: X: " + sensorEvent.values[0] + ", Y: " + sensorEvent.values[1] + ", Z:  " + sensorEvent.values[2]);
//
//            zAccValue.setText("Z: " + sensorEvent.values[2]);
//        }
//    }
}
