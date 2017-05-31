package top.lemonsoda.openweather.view.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import top.lemonsoda.openweather.R;
import top.lemonsoda.openweather.domain.utils.CitySharedPreference;
import top.lemonsoda.openweather.domain.utils.Constants;
import top.lemonsoda.openweather.domain.utils.NotificationUtils;
import top.lemonsoda.openweather.domain.utils.Utils;
import top.lemonsoda.openweather.model.entry.City;
import top.lemonsoda.openweather.model.entry.ForecastWeather;
import top.lemonsoda.openweather.model.entry.Weather;
import top.lemonsoda.openweather.view.ui.fragment.WeatherFragment;
import top.lemonsoda.openweather.view.ui.helper.WeatherPagerAdapter;

public class WeatherActivity extends BaseActivity
        implements WeatherFragment.OnForecastItemClickListener {
    private static final String TAG = WeatherActivity.class.getCanonicalName();

    private WeatherPagerAdapter weatherPagerAdapter;

    private List<City> locationList;
    private HashMap<Integer, Weather> weatherInfoMap;

    private int dotPre = 0;

    @BindView(R.id.container)
    ViewPager mViewPager;

    @BindView(R.id.ll_dot_group)
    LinearLayout llDotGroup;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    TextView tvTitle;

    private LocationManager locationManager;
    private String provider;
    private int REQUEST_GRANTED_LOCATION = 0;
    private boolean isAutoLocationEnable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResID = R.layout.activity_weather;
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvTitle = (TextView) toolbar.findViewById(R.id.tv_toolbar_title);

        isAutoLocationEnable = Utils.isAutoLocationEnable(this);
        if (isAutoLocationEnable) {
            Log.d(TAG, "location is enable!");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_GRANTED_LOCATION);
            } else {
                requestLocation();
            }
        }


        if (CitySharedPreference.getManagedCityList(this) == null) {
            City beijing = new City();
            /*
            {
              "lat": 39.907501,
              "country": "CN",
              "_id": 1816670,
              "lon": 116.397232,
              "name": "Beijing"
            }
            */
            beijing.setName("Beijing");
            beijing.set_id(1816670);
            beijing.setCountry("CN");
            beijing.setLat(39.907501);
            beijing.setLon(116.397232);
            CitySharedPreference.addManagedCity(this, beijing);
        }
        locationList = CitySharedPreference.getManagedCityList(this);
        weatherInfoMap = new HashMap<>();
        for (City location : locationList) {
            weatherInfoMap.put(location.get_id(), null);
        }
        tvTitle.setText(locationList.get(0).getName());
        setupToggle();
        weatherPagerAdapter = new WeatherPagerAdapter(getSupportFragmentManager(), locationList);
        mViewPager.setAdapter(weatherPagerAdapter);
        mViewPager.addOnPageChangeListener(new DotChangerListener());
        setupNotification();

        if (getIntent() != null) {
            int showId = getIntent().getIntExtra(Constants.INTENT_ARG_CITY_ID, 0);
            for (int i = 0; i < locationList.size(); i++) {
                if (locationList.get(i).get_id() == showId) {
                    setupViewPageWithDot(i);
                    return;
                }
            }
        }
        setupViewPageWithDot(0);
    }

    private void showLocation(Location location) {
        Log.d(TAG, "Location ---> " + location.getLatitude() + ", " + location.getLongitude());
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

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "----onNewIntent");
        super.onNewIntent(intent);
        int currentItem = mViewPager.getCurrentItem();
        int currentId = locationList.get(currentItem).get_id();
        int id = intent.getIntExtra(Constants.INTENT_ARG_CITY_ID, currentId);
        if (id != currentId) {
            for (int i = 0; i < locationList.size(); i++) {
                if (locationList.get(i).get_id() == id) {
                    mViewPager.setCurrentItem(i);
                    break;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_ADD_CITY) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "result OK");
                boolean dataChange = data.getBooleanExtra(Constants.ARG_CITY_MANAGE_CHANGED, false);
                if (dataChange) {
                    Log.d(TAG, "Data Changed ...");

                    locationList = CitySharedPreference.getManagedCityList(this);
                    Log.d(TAG, "CityList: " + locationList.toString());
                    updateWeatherInfoMap();
                    weatherPagerAdapter.updateLocationList(locationList);

                    weatherPagerAdapter.notifyDataSetChanged();
                    setupViewPageWithDot(0);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weather, menu);

        MenuItem shareItem = menu.findItem(R.id.action_share);
        Drawable wrapped = DrawableCompat.wrap(shareItem.getIcon());
        DrawableCompat.setTint(wrapped, ContextCompat.getColor(this, R.color.white));
        shareItem.setIcon(wrapped);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            int currentItem = mViewPager.getCurrentItem();
            Weather weather = getWeatherInfo(locationList.get(currentItem));
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "OpenWeather Share");
            String name = locationList.get(currentItem).getName();
            String desc = weather.getCurrentWeather().getWeather().get(0).getDescription();
            String temp = Utils.formatTemperature(this,
                    weather.getCurrentWeather().getMain().getTemp());
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    Utils.createShareContent(this, name, desc, temp));
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Intent.createChooser(shareIntent, "Share"));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(WeatherFragment.ForecastViewHolder holder, int pos, Weather weather) {
        Log.d(TAG, "Pos: " + pos);
        Intent intent = new Intent(WeatherActivity.this, DetailActivity.class);
        ForecastWeather forecastWeather = weather.getForecastWeather();
        intent.putExtra(Constants.ARG_DETAIL_KEY, forecastWeather);
        intent.putExtra(Constants.ARG_DETAIL_DAY, pos);

        ActivityOptionsCompat activityOptions =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                        new Pair<View, String>(holder.imgIcon, getString(R.string.detail_icon_transition_name)));
        ActivityCompat.startActivity(this, intent, activityOptions.toBundle());
    }

    public void updateWeatherInfoMap() {
        List<Integer> ids = new ArrayList<>();

        for (City city : locationList) {
            ids.add(city.get_id());
            if (!weatherInfoMap.containsKey(city.get_id())) {
                weatherInfoMap.put(city.get_id(), null);
            }
        }

        Iterator it = weatherInfoMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Integer key = (Integer) entry.getKey();
            if (!ids.contains(key)) {
                it.remove();
            }
        }
    }

    public void addWeatherInfo(City city, Weather weather) {
        weatherInfoMap.put(city.get_id(), weather);
    }

    public Weather getWeatherInfo(City city) {
        return weatherInfoMap.get(city.get_id());
    }

    private void setupNotification() {
        if (Utils.isShowNotification(this)) {
            int id = Utils.getNotificationCityId(this, locationList.get(0).get_id());
            NotificationUtils.getInstance().createNotification(this, id);
        }
    }

    public class DotChangerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Log.d(TAG, "Page select: " + position);
            llDotGroup.getChildAt(dotPre).setEnabled(false);
            llDotGroup.getChildAt(position).setEnabled(true);
            dotPre = position;
            tvTitle.setText(locationList.get(position).getName());
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void initDot() {
        View dot;
        LinearLayout.LayoutParams params;

        llDotGroup.removeAllViews();

        for (int i = 0; i < locationList.size(); ++i) {
            dot = new View(this);
            dot.setBackgroundResource(R.drawable.dot_bg_selector);
            params = new LinearLayout.LayoutParams(20, 20);
            params.leftMargin = 10;
            dot.setEnabled(false);
            dot.setLayoutParams(params);
            llDotGroup.addView(dot);
        }
    }

    private void setupViewPageWithDot(int position) {
        initDot();
        llDotGroup.getChildAt(position).setEnabled(true);
        mViewPager.setCurrentItem(position);
    }

    private void setupToggle() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_manage_location:
                        Intent intent = new Intent(WeatherActivity.this,
                                LocationManageActivity.class);
                        startActivityForResult(intent, Constants.REQUEST_ADD_CITY);
                        break;
                    case R.id.nav_settings:
                        startActivity(new Intent(WeatherActivity.this, SettingsActivity.class));
                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (locationManager != null)
            locationManager.removeUpdates(locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_GRANTED_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocation();
            } else {
                Log.d(TAG, "Not allowed to use location");
            }
        }
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
            Toast.makeText(this, "No Location Manager", Toast.LENGTH_LONG);
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
        locationManager.requestLocationUpdates(provider, 5000, 1, locationListener);
    }
}
