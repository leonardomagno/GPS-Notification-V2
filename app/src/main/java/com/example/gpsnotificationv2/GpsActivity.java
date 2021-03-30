package com.example.gpsnotificationv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class GpsActivity extends Activity implements LocationListener {
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    private static Client client;
    private double distance;
    Location location; // Location


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 10, this);
        }

        location = new Location(LocationManager.GPS_PROVIDER);
        onLocationChanged(location);
    }
    @Override
    public void onLocationChanged(Location location) {
        locationManager.removeUpdates(this);

        client = new Client(-22.8816618, -43.0995832);
        double clientLatitude = client.getLatitude();
        double clientLongitude = client.getLongitude();

        double userLatitude = location.getLatitude();
        double userLongitude = location.getLongitude();

        distance = calculateDistanceBetweenPoints(userLatitude, userLongitude, clientLatitude, clientLongitude);

        setupProximityBehavior(distance);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    public double calculateDistanceBetweenPoints(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);

        return dist;
    }

    public void setupProximityBehavior(Double distance) {
        if (distance < 1000){
            Toast.makeText(getApplicationContext(), "Você está a menos de 1 km de distância", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Você está a mais de 1 km de distância", Toast.LENGTH_LONG).show();
        }
    }
}