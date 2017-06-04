package top.lemonsoda.openweather.view.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import top.lemonsoda.openweather.R;

public class TestMainActivity extends BaseActivity {

    public static final String TAG = TestMainActivity.class.getCanonicalName();
//    LocationTracker tracker;

    LocationManager locationManager;
    String provider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResID = R.layout.activity_test_main;
        super.onCreate(savedInstanceState);

//        tracker = new LocationTracker(this);
//        if (tracker.canGetLocation()) {
//            tracker.getLocation();
//            Log.d(TAG, "--- Get Location : " + tracker.getLat() + ", " + tracker.getLon());
//        }

        requestLocation();
    }


    private void requestLocation() {
        //location
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            Log.d(TAG, "GPS Location Manager");
            provider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            Log.d(TAG, "Network provider");
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            Log.d(TAG, "No Location Provider");
            Toast.makeText(this, "No Location Manager", Toast.LENGTH_LONG);
            return;
        }


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location lo = locationManager.getLastKnownLocation(provider);

        if (lo != null) {
            Log.d(TAG, "Location is not null!");
            showLocation(lo);
        }
        locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
    }


    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
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
    };

    private void showLocation(Location location) {
        Log.d(TAG, "Location ---> " + location.getLatitude() + ", " + location.getLongitude());
    }


}
