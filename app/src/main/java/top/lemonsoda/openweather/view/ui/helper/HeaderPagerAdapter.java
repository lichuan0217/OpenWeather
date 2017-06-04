package top.lemonsoda.openweather.view.ui.helper;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import top.lemonsoda.openweather.R;
import top.lemonsoda.openweather.model.entry.City;


/**
 * Created by Chuan on 01/06/2017.
 */

public class HeaderPagerAdapter extends PagerAdapter {
    private static final String TAG = HeaderPagerAdapter.class.getCanonicalName();

    private Context context;
    private List<City> cityList;

    public HeaderPagerAdapter(Context context, List<City> list) {
        this.context = context;
        this.cityList = list;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d(TAG, "instantiateItem " + position);
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.drawer_header_item, container, false);
        TextView title = ButterKnife.findById(layout, R.id.tv_drawer_header_item);
        title.setText(cityList.get(position).getName() + ", " + cityList.get(position).getCountry());
        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.d(TAG, "destroy Item " + position);
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return cityList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
