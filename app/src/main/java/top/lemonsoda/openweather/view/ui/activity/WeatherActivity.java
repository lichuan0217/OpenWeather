package top.lemonsoda.openweather.view.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import butterknife.BindView;
import top.lemonsoda.openweather.R;
import top.lemonsoda.openweather.domain.utils.CitySharedPreference;
import top.lemonsoda.openweather.domain.utils.Constants;
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

        if (CitySharedPreference.getCityList(this) == null) {
            CitySharedPreference.addCity(this, "Beijing");
        }
        cityList = CitySharedPreference.getCityList(this);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new DotChangerListener());

        setupViewPageWithDot(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult called..");
        if (requestCode == Constants.REQUEST_ADD_CITY) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "result OK");
                boolean dateChange = data.getBooleanExtra(Constants.ARG_CITY_MANAGE_CHANGED, false);
                if (dateChange) {
                    Log.d(TAG, "Data Changed ...");
                    cityList = CitySharedPreference.getCityList(this);
                    Log.d(TAG, "city list: " + cityList.toString());
                    mSectionsPagerAdapter.notifyDataSetChanged();
//                    setupViewPageWithDot(cityList.size() - 1);
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
