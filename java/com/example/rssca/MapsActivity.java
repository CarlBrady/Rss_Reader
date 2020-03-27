package com.example.rssca;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import android.location.Location;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

//This class was implemented using the default Google Maps template provided by Android Studio upon creation of a new activity.

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {

    private Location lastLocation;
    private GoogleMap mMap;
    private Marker marker;
    private LatLng curr_location;
    private int i;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        FusedLocationProviderClient locationManager = LocationServices.getFusedLocationProviderClient(this); // creation of location provider
        Task<Location> task = locationManager.getLastLocation(); // use the location provider to get last known location
        task.addOnSuccessListener(new OnSuccessListener<Location>() { // add on success listener to finding last known location
            @Override
            public void onSuccess(Location location) {
                if (location != null) { //if location returned correctly
                    lastLocation = location;
                    String locale = "";
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    try {
                        List<Address> locality = geocoder.getFromLocation(lastLocation.getLatitude(), lastLocation.getLongitude(), 1); // store lat/long location of last known location
                        locale = locality.get(0).getCountryCode(); // store the country code of the last known location.
                    } catch (IOException e) {
                        e.printStackTrace(); // if location returned null or incorrect, throw exception.
                    }
                    Toast.makeText(getApplicationContext(), locale,
                            Toast.LENGTH_SHORT).show();

                    SupportMapFragment mf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map); // Use fragment manager to get fragment id
                    mf.getMapAsync(MapsActivity.this);
                }
            }

        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // auto generated code due to getBroadcast method requiring higher build version than project outlines.
            notificationChannel(); //create

            Intent intent = new Intent(MapsActivity.this,NotificationService.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(MapsActivity.this, 0,intent,0);
            long currentTime = System.currentTimeMillis();
            long alarmTime = 1000 * 2;
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, currentTime + alarmTime, alarmTime, pendingIntent);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setOnMapLoadedCallback(this);
        mMap = googleMap;
    }

    @Override
    public void onMapLoaded() {

        if (lastLocation != null) // if there was a previous known location found then put this location as a marker on the map.
        {
            curr_location = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
            marker = mMap.addMarker(new MarkerOptions().position(curr_location).title("Selected Location"));
            i++; // int i is later used to determine if there is a marker present on the map 1 = true, 0 = false.
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (i == 0) { // if there is no marker on the map and the user selects a location
                    curr_location = latLng;
                    marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));
                    i++;
                } else { // if there is a marker on the map
                    marker.remove();
                    curr_location = latLng;
                    marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));
                }
            }
        });
    }

    // This method allows the user to click the Current Location button and retrieve their current location.
    public void onClickCurrent(View v){

        if (lastLocation != null && i!=0)
        {
            marker.remove();
            curr_location = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
            marker = mMap.addMarker(new MarkerOptions().position(curr_location).title("Selected Location"));
        }
        else if(lastLocation != null)
        {
            curr_location = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
            marker = mMap.addMarker(new MarkerOptions().position(curr_location).title("Selected Location"));
            i++;
        }


    }
    public void onClickContinue(View v){

        Intent newsIntent = new Intent(getApplicationContext(),NewsActivity.class); // creation of an intent to the News Activity class
        String locale = "";
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> locality = geocoder.getFromLocation(curr_location.latitude, curr_location.longitude,1); //get the 'currlocation' location ( the marker location)
            locale = locality.get(0).getCountryCode(); // get the country code of the current marker placement.
        } catch (IOException e) {
            e.printStackTrace();
        }
        newsIntent.putExtra("locale",locale);
        this.startActivity(newsIntent);




    }

   @RequiresApi(api = Build.VERSION_CODES.N)
    public void notificationChannel(){ //creates a channel for the notification

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) // auto generated code due to build version restrictions
        {
            NotificationChannel channel = new NotificationChannel("news", "newsChannel", NotificationManager.IMPORTANCE_DEFAULT); // new notification channel for 'news' notification from Notification Service
            channel.setDescription("Channel");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel); // Use Notification manager system service to create notification channel
        }
    }

}

