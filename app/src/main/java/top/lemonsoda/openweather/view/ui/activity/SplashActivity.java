package top.lemonsoda.openweather.view.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import top.lemonsoda.openweather.domain.utils.CitySharedPreference;
import top.lemonsoda.openweather.domain.utils.Constants;

/**
 * Created by Chuan on 8/1/16.
 */
public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getCanonicalName();
    private static final long SPLASH_DISPLAY_LENGTH = 1500;
    private int REQUEST_GRANTED_LOCATION = 0;

    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        runnable = new Runnable() {
            @Override
            public void run() {
//                requestLocationPermission();
//                goToWeather();
                goToMain();
            }
        };

        handler.postDelayed(runnable, SPLASH_DISPLAY_LENGTH);
    }

    private void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "--- Checking permissions!");
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    REQUEST_GRANTED_LOCATION);
        } else {
            Log.d(TAG, "--- Permissions are checked!");
            goToMain();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_GRANTED_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "--- Allowed to use Location!");
            } else {
                Log.d(TAG, "--- Not allowed to use Location!");
            }
            goToMain();
        }
    }

    private void goToMain() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToWeather() {
        Intent intent = new Intent(SplashActivity.this, WeatherActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(runnable);
    }
}
