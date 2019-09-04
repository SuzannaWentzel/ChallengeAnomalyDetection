package com.example.practicing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "MainActivity";

    private SensorManager sensorManager;
    Sensor gyroscope, accelerometer;

    TextView xGyroValue, yGyroValue, zGyroValue;
    TextView xAccValue, yAccValue, zAccValue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xGyroValue = (TextView) findViewById(R.id.xGyroValue);
        yGyroValue = (TextView) findViewById(R.id.yGyroValue);
        zGyroValue = (TextView) findViewById(R.id.zGyroValue);

        xAccValue = (TextView) findViewById(R.id.xAccValue);
        yAccValue = (TextView) findViewById(R.id.yAccValue);
        zAccValue = (TextView) findViewById(R.id.zAccValue);

        Log.d(TAG, "onCreate: Initializing Sensor services");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(MainActivity.this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(MainActivity.this, accelerometer, sensorManager.SENSOR_DELAY_NORMAL);
        Log.d(TAG, "onCreate: Registered gyroscope and accelerometer");

        Button btn = (Button) findViewById(R.id.openMaps);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        });
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;

        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Log.d(TAG, "onAcceleroSensorChanged: X: " + sensorEvent.values[0] + ", Y: " + sensorEvent.values[1] + ", Z:  " + sensorEvent.values[2]);

            xAccValue.setText("X: " + sensorEvent.values[0]);
            yAccValue.setText("Y: " + sensorEvent.values[1]);
            zAccValue.setText("Z: " + sensorEvent.values[2]);
        } else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            Log.d(TAG, "onGyroSensorChanged: X: " + sensorEvent.values[0] + ", Y: " + sensorEvent.values[1] + ", Z:  " + sensorEvent.values[2]);

            xGyroValue.setText("X: " + sensorEvent.values[0]);
            yGyroValue.setText("Y: " + sensorEvent.values[1]);
            zGyroValue.setText("Z: " + sensorEvent.values[2]);
        }


    }
}
