package com.openinstitute.nuru;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

public class GPS_Service extends Service {
    private LocationListener locationListener;
    private LocationManager locationManager;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /*@SuppressLint("MissingPermission")*/
    @Override
    public void onCreate() {
        super.onCreate();
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Intent i= new Intent("location_update");
                i.putExtra("gps_longitude",location.getLongitude());
                i.putExtra("gps_latitude",location.getLatitude() );
                sendBroadcast(i);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent= new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        };
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,50000,0,locationListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null){
            locationManager.removeUpdates(locationListener);
        }
    }
}
