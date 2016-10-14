package top.lemonsoda.openweather.view.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import top.lemonsoda.openweather.R;
import top.lemonsoda.openweather.domain.utils.CitySharedPreference;
import top.lemonsoda.openweather.domain.utils.Constants;
import top.lemonsoda.openweather.model.entry.ForecastWeather;
import top.lemonsoda.openweather.model.entry.Weather;
import top.lemonsoda.openweather.view.ui.fragment.WeatherFragment;
import top.lemonsoda.openweather.view.ui.helper.WeatherPagerAdapter;

public class WeatherActivity extends BaseActivity implements WeatherFragment.OnForecastItemClickListener {
    private static final String TAG = WeatherActivity.class.getCanonicalName();

    private WeatherPagerAdapter weatherPagerAdapter;
    private List<String> cityList;
    private HashMap<String, Weather> weatherMap;

    private int dotPre = 0;

    @BindView(R.id.container)
    ViewPager mViewPager;

    @BindView(R.id.ll_dot_group)
    LinearLayout llDotGroup;

    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResID = R.layout.activity_weather;
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tvTitle = (TextView)toolbar.findViewById(R.id.tv_toolbar_title);


        if (CitySharedPreference.getCityList(this) == null) {
            CitySharedPreference.addCity(this, "Beijing");
        }
        cityList = CitySharedPreference.getCityList(this);
        weatherMap = new HashMap<>();
        for (String city : cityList) {
            weatherMap.put(city, null);
        }
        tvTitle.setText(cityList.get(0));

        weatherPagerAdapter = new WeatherPagerAdapter(getSupportFragmentManager(), cityList);
        mViewPager.setAdapter(weatherPagerAdapter);
        mViewPager.addOnPageChangeListener(new DotChangerListener());

        setupViewPageWithDot(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult called..");
        if (requestCode == Constants.REQUEST_ADD_CITY) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "result OK");
                boolean dataChange = data.getBooleanExtra(Constants.ARG_CITY_MANAGE_CHANGED, false);
                if (dataChange) {
                    Log.d(TAG, "Data Changed ...");
                    cityList = CitySharedPreference.getCityList(this);
                    Log.d(TAG, "city list: " + cityList.toString());
                    updateWeatherMap();
                    weatherPagerAdapter.updateCityList(cityList);
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(WeatherActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_city_manage) {
            Intent intent = new Intent(WeatherActivity.this, CityManageActivity.class);
            startActivityForResult(intent, Constants.REQUEST_ADD_CITY);
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

    public void updateWeatherMap() {
        for (String city : cityList) {
            if (!weatherMap.containsKey(city)) {
                weatherMap.put(city, null);
            }
        }
        Iterator it = weatherMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = entry.getKey().toString();
            if (!cityList.contains(key)) {
                it.remove();
            }
        }

        Log.d(TAG, "update--cityList: " + cityList);
        Log.d(TAG, "update--weatherMap: " + weatherMap.toString());
    }

    public void addWeather(String key, Weather weather) {
        weatherMap.put(key, weather);
    }

    public Weather getWeather(String key) {
        return weatherMap.get(key);
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
            tvTitle.setText(cityList.get(position));
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void initDot() {
        View dot;
        LinearLayout.LayoutParams params;

        llDotGroup.removeAllViews();

        for (int i = 0; i < cityList.size(); ++i) {
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
}
