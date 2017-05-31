package top.lemonsoda.openweather.domain.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Created by Chuan on 22/11/2016.
 */

public class LocationTracker implements LocationListener {

    private final Context context;
    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;

    boolean canGetLocation = false;
    Location location;
    double lat;
    double lon;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60;

    public static final String TAG = LocationTracker.class.getCanonicalName();

    protected LocationManager locationManager;
    private String provider;

    public LocationTracker(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "--- Permission not allowed");
            return;
        }

        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnable && !isNetworkEnable) {
            Log.d(TAG, "--- No Location Provider is enable");
        } else {
            this.canGetLocation = true;
            if (isGPSEnable) {
                Log.d(TAG, "--- GPS Enable");
                provider = LocationManager.GPS_PROVIDER;
            } else if (isNetworkEnable) {
                Log.d(TAG, "--- Network Enable");
                provider = LocationManager.NETWORK_PROVIDER;
            }
//            location = locationManager.getLastKnownLocation(provider);
//            if (location != null) {
//                Log.d(TAG, "--- Location is not NULL");
//                lat = location.getLatitude();
//                lon = location.getLongitude();
//            }
//            locationManager.requestLocationUpdates(
//                    provider,
//                    MIN_TIME_BW_UPDATES,
//                    MIN_DISTANCE_CHANGE_FOR_UPDATES,
//                    this);
        }
    }

    public boolean canGetLocation() {
        return canGetLocation;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "--- getLocation() permission not allowed");
            return;
        }
        location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            Log.d(TAG, "--- Location is not NULL");
            lat = location.getLatitude();
            lon = location.getLongitude();
        }
        locationManager.requestLocationUpdates(
                    provider,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES,
                    this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "--- onLocationChanged");
        lat = location.getLatitude();
        lon = location.getLongitude();
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
