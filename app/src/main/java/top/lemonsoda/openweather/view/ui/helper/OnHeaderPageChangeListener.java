package top.lemonsoda.openweather.view.ui.helper;

import android.support.v4.view.ViewPager;
import android.util.Log;

/**
 * Created by Chuan on 01/06/2017.
 */

public class OnHeaderPageChangeListener implements ViewPager.OnPageChangeListener {
    private static final String TAG = OnHeaderPageChangeListener.class.getCanonicalName();

    private OnCityChangeListener listener;

    public OnHeaderPageChangeListener(OnCityChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d(TAG, "onPageSelected " + position);
        listener.onCityChange(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
