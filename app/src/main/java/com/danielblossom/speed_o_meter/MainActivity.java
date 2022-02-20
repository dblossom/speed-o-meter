package com.danielblossom.speed_o_meter;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.Formatter;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {



    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (GoogleApiAvailability
                .getInstance()
                .isGooglePlayServicesAvailable(this) != ConnectionResult.SUCCESS) {
            // do something -- error?
        }

        // wrapper method for checking permissions (still need to understand this better)
        requestLocationPermission();

/*        // I feel like this is redundant because of the above call ?
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
       } else {
            // Start program if permission granted.
            this.startProgram();
        }*/
        this.startProgram();
        this.updateSpeed(null);
        //this.updateLatitude(null);
        //this.updateLongitude(null);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void requestLocationPermission() {
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION,false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                // Precise location access granted.
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                // Only approximate location access granted.
                            } else {
                                // No location access granted.
                            }
                        }
                );
        // Before you perform the actual permission request, check whether your app
        // already has the permissions, and whether your app needs to show a permission
        // rationale dialog. For more details, see Request permissions.
        locationPermissionRequest.launch(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    @SuppressLint("MissingPermission")
    public void startProgram(/* String newSpeed */){
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if(locationManager != null){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
        }
    }

    private void updateSpeed(MyLocation myLocation) {
        float currentSpeed = 0;
        if (myLocation !=null){
            currentSpeed = myLocation.getSpeed();
        }
        Formatter fmt = new Formatter(new StringBuilder());
        fmt.format(Locale.US, "%5.1f",currentSpeed);
        String strCurrentSpeed = fmt.toString();
        strCurrentSpeed = strCurrentSpeed.replace(" ", "0");
        TextView speedView = (TextView) findViewById(R.id.textSpeedDisplay);
        speedView.setText(strCurrentSpeed);
    }

    private void updateLatitude(MyLocation myLocation){
        //TODO: add null check
        double currentLatitude = myLocation.getLatitude();
        String strCurrentLatitude = String.valueOf(currentLatitude);
        TextView latitudeView = (TextView) findViewById(R.id.textLatitude);
        latitudeView.setText("Latitude = " + strCurrentLatitude);
    }

    private void updateLongitude(MyLocation myLocation){
        //TODO: add null check
        TextView longitudeView = (TextView) findViewById(R.id.textLongitude);
        String strCurrentLongitude = String.valueOf(myLocation.getLongitude());
        longitudeView.setText("Longitude = " + strCurrentLongitude);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if(location != null){
            MyLocation myLocation = new MyLocation((location));
            this.updateSpeed(myLocation);
            this.updateLatitude(myLocation);
            this.updateLongitude(myLocation);
        }
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }
}