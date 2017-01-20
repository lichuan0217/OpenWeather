package top.lemonsoda.openweather.view.ui.activity;

import android.os.Bundle;

import java.util.HashMap;
import java.util.List;

import top.lemonsoda.openweather.R;
import top.lemonsoda.openweather.domain.utils.ActivityUtils;
import top.lemonsoda.openweather.domain.utils.CitySharedPreference;
import top.lemonsoda.openweather.model.entry.City;
import top.lemonsoda.openweather.model.entry.Weather;
import top.lemonsoda.openweather.view.ui.fragment.MainFragment;
import top.lemonsoda.openweather.view.ui.fragment.WeatherFragment;

public class MainActivity extends BaseActivity implements MainFragment.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        layoutResID = R.layout.activity_main;
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        MainFragment fragment =
                (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fl_main);
        if (fragment == null) {
            fragment = MainFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), fragment, R.id.fl_main);
        }

        toolbar.setTitle("Overview");

    }

    @Override
    public void onItemClickInteraction() {

    }
}
