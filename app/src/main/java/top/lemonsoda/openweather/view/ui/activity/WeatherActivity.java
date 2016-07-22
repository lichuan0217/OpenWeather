package top.lemonsoda.openweather.view.ui.activity;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import top.lemonsoda.openweather.R;
import top.lemonsoda.openweather.model.entry.Weather;
import top.lemonsoda.openweather.presenter.IWeatherPresenter;
import top.lemonsoda.openweather.presenter.impl.WeatherPresenterImpl;
import top.lemonsoda.openweather.view.IWeatherView;
import top.lemonsoda.openweather.view.ui.fragment.WeatherFragment;

public class WeatherActivity extends BaseActivity {
    private static final String TAG = WeatherActivity.class.getCanonicalName();

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private List<String> cityList;

    private int dotPre = 0;

    @BindView(R.id.container)
    ViewPager mViewPager;

    @BindView(R.id.ll_dot_group)
    LinearLayout llDotGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResID = R.layout.activity_weather;
        super.onCreate(savedInstanceState);

//        toolbar.getBackground().setAlpha(255);
        cityList = new ArrayList<>();

        cityList.add("Beijing");
        cityList.add("Guangzhou");
        cityList.add("Shijiazhuang");
        cityList.add("London,uk");

        initDot();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new DotChangerListener());

        llDotGroup.getChildAt(0).setEnabled(true);
        mViewPager.setCurrentItem(0);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return WeatherFragment.newInstance(cityList.get(position));
        }

        @Override
        public int getCount() {
            return cityList.size();
        }


    }

    private void initDot() {
        View dot;
        LinearLayout.LayoutParams params;

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

    private class DotChangerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Log.d(TAG, "Page select: " + position);
            llDotGroup.getChildAt(dotPre).setEnabled(false);
            llDotGroup.getChildAt(position).setEnabled(true);
            dotPre = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
