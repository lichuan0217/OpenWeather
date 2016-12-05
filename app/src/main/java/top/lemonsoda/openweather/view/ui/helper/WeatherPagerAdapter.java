package top.lemonsoda.openweather.view.ui.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.List;

import top.lemonsoda.openweather.model.entry.City;
import top.lemonsoda.openweather.view.ui.fragment.WeatherFragment;

/**
 * Created by Chuan on 8/4/16.
 */
public class WeatherPagerAdapter extends FragmentPagerAdapter {
    private List<City> locationList;
    private long baseId = 0;
    private long countPre = 0;

    public WeatherPagerAdapter(FragmentManager fm, List<City> locations) {
        super(fm);
        locationList = locations;
        countPre = locationList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return WeatherFragment.newInstance(locationList.get(position), position);
    }

    @Override
    public int getCount() {
        return locationList.size();
    }

    public void updateLocationList(List<City> locations) {
        locationList = locations;
        baseId += getCount() + countPre;
        countPre = locationList.size();
    }


    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public long getItemId(int position) {
        return baseId + position;
    }

    public void notifyChangeInPosition(int n) {
        // shift the ID returned by getItemId outside the range of all previous fragments
        baseId += getCount() + n;
    }


}
