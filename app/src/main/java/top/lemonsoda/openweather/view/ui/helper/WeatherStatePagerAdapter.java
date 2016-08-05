package top.lemonsoda.openweather.view.ui.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import top.lemonsoda.openweather.view.ui.fragment.WeatherFragment;

/**
 * Created by Chuan on 8/4/16.
 */
public class WeatherStatePagerAdapter extends FragmentStatePagerAdapter {
    private List<String> cityList;

    public WeatherStatePagerAdapter(FragmentManager fm, List<String> cities) {
        super(fm);
        cityList = cities;
    }

    @Override
    public Fragment getItem(int position) {
        return WeatherFragment.newInstance(cityList.get(position), position);
    }

    @Override
    public int getCount() {
        return cityList.size();
    }
}
