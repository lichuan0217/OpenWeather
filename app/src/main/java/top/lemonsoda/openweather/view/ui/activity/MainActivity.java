package top.lemonsoda.openweather.view.ui.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.lemonsoda.openweather.R;
import top.lemonsoda.openweather.domain.application.WeatherApplication;
import top.lemonsoda.openweather.domain.net.WeatherService;
import top.lemonsoda.openweather.domain.utils.ActivityUtils;
import top.lemonsoda.openweather.domain.utils.CitySharedPreference;
import top.lemonsoda.openweather.domain.utils.Constants;
import top.lemonsoda.openweather.domain.utils.Utils;
import top.lemonsoda.openweather.model.WeatherModelInterface;
import top.lemonsoda.openweather.model.entry.City;
import top.lemonsoda.openweather.model.entry.Weather;
import top.lemonsoda.openweather.model.impl.WeatherModel;
import top.lemonsoda.openweather.presenter.WeatherPresenterInterface;
import top.lemonsoda.openweather.presenter.impl.WeatherPresenter;
import top.lemonsoda.openweather.view.ui.custom.HeaderViewPager;
import top.lemonsoda.openweather.view.ui.fragment.DailyFragment;
import top.lemonsoda.openweather.view.ui.fragment.OverviewFragment;
import top.lemonsoda.openweather.view.ui.helper.HeaderPagerAdapter;
import top.lemonsoda.openweather.view.ui.helper.OnCityChangeListener;
import top.lemonsoda.openweather.view.ui.helper.OnContainerInteractionListener;
import top.lemonsoda.openweather.view.ui.helper.OnHeaderPageChangeListener;

public class MainActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        OnContainerInteractionListener,
        OnCityChangeListener {

    private static final String TAG = MainActivity.class.getCanonicalName();

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    HeaderViewPager headerViewPager;

    private List<City> cityList;
    private HashMap<Integer, Weather> weatherHashMap;
    private int currentPos = 0;
    private WeatherPresenterInterface weatherPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResID = R.layout.activity_main;
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);

        loadCityList();
        setupNavigation();

        weatherHashMap = new HashMap<>();
        WeatherService service = WeatherApplication.getWeatherApplication(this).getComponent().getWeatherService();
        WeatherModelInterface weatherModel = new WeatherModel(service);
        weatherPresenter = new WeatherPresenter(weatherModel);

        OverviewFragment overviewFragment =
                (OverviewFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.fl_weather_container);
        if (overviewFragment == null) {
            overviewFragment = OverviewFragment.newInstance(cityList.get(0));
            overviewFragment.setPresent(weatherPresenter);
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    overviewFragment,
                    R.id.fl_weather_container);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_overview) {
            OverviewFragment fragment = OverviewFragment.newInstance(cityList.get(currentPos));
            fragment.setPresent(weatherPresenter);
            ActivityUtils.replaceFragment(
                    getSupportFragmentManager(),
                    fragment,
                    R.id.fl_weather_container);
        } else if (id == R.id.nav_daily) {
            DailyFragment fragment = DailyFragment.newInstance(cityList.get(currentPos));
            fragment.setPresent(weatherPresenter);
            ActivityUtils.replaceFragment(
                    getSupportFragmentManager(),
                    fragment,
                    R.id.fl_weather_container);
        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_about) {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDetail(String cityId, String date) {

    }

    @Override
    public void onSetTitle(String title, int type) {
        toolbar.setTitle(title);
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) toolbar.getLayoutParams();
//        if (type == 0) {
//            params.height = (int) Utils.convertDpToPixel(56, this);
//        }
//        if (type == 1) {
//            params.height = (int) Utils.convertDpToPixel(128, this);
//            params.topMargin = (int)Utils.convertDpToPixel(56, this);
//        }
//        toolbar.setLayoutParams(params);
    }

    @Override
    public void onSaveWeather(int id, Weather weather) {
        weatherHashMap.put(id, weather);
    }

    @Override
    public Weather onGetWeather(int id) {
        if (weatherHashMap.containsKey(id))
            return weatherHashMap.get(id);
        return null;
    }

    @Override
    public void onCityChange(int pos) {
        Log.d(TAG, "onCityChange " + pos);
        currentPos = pos;
        OverviewFragment fragment = OverviewFragment.newInstance(cityList.get(currentPos));
        weatherPresenter.reset();
        fragment.setPresent(weatherPresenter);
        ActivityUtils.replaceFragment(
                getSupportFragmentManager(),
                fragment,
                R.id.fl_weather_container);
        MenuItem item = navigationView.getMenu().getItem(0);
        item.setChecked(true);
    }

    /**
     * Setup DrawerLayout and NavigationView
     */
    private void setupNavigation() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        setupNavigationHeader();
    }

    /**
     * Setup Navigation header view
     */
    private void setupNavigationHeader() {
        // Bind Header View
        View headView = navigationView.getHeaderView(0);
        headerViewPager = ButterKnife.findById(headView, R.id.vp_header);
        headerViewPager.setAdapter(new HeaderPagerAdapter(this, cityList));
        headerViewPager.addOnPageChangeListener(new OnHeaderPageChangeListener(this));
    }


    /**
     * Load city list from SharedPreference
     */
    private void loadCityList() {
        cityList = CitySharedPreference.getManagedCityList(this);
        if (cityList == null) {
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
            cityList = new ArrayList<>();
            cityList.add(beijing);
        }
    }
}
