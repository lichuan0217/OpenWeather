package top.lemonsoda.openweather.view.ui.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.List;

import top.lemonsoda.openweather.view.ui.fragment.WeatherFragment;

/**
 * Created by Chuan on 8/4/16.
 */
public class WeatherPagerAdapter extends FragmentPagerAdapter {
    private List<String> cityList;
    private long baseId = 0;
    private long countPre = 0;

    public WeatherPagerAdapter(FragmentManager fm, List<String> cities) {
        super(fm);
        this.cityList = cities;
        countPre = cityList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return WeatherFragment.newInstance(cityList.get(position), position);
    }

    @Override
    public int getCount() {
        return cityList.size();
    }

    public void updateCityList(List<String> cityList) {
        this.cityList = cityList;
        baseId += getCount() + countPre;
        countPre = cityList.size();
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
