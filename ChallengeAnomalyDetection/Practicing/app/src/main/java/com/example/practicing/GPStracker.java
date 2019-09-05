package com.example.practicing;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.health.PackageHealthStats;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class GPStracker extends AppCompatActivity implements LocationListener {

    Context context;

    public  GPStracker(Context c){
        context = c;
    }

    public Location getLocation() {
        // check whether permission is given to use GPS data
        if  (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(context, "Permission not granted", Toast.LENGTH_SHORT);
            return null;
        }

        // attempt to access GPS data
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // check whether GPS is enabled
        if(isGPSEnabled) {
            // get and return location
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500,0,this);
            Location l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return l;
        } else {
            // show that GPS is disabled
            Toast.makeText(context, "please enable gps", Toast.LENGTH_LONG);
        }
        return null;
    }



    // couple of functions needed for implementing LocationListener

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
