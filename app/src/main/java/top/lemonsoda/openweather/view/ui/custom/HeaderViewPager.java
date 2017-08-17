package top.lemonsoda.openweather.view.ui.custom;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import top.lemonsoda.openweather.R;
import top.lemonsoda.openweather.domain.utils.Utils;

/**
 * Created by Chuan on 01/06/2017.
 */

public class HeaderViewPager extends ViewPager {
    private static final String TAG = HeaderViewPager.class.getCanonicalName();

    private float navHeaderHeight;

    public HeaderViewPager(Context context) {
        this(context, null);
    }

    public HeaderViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        navHeaderHeight = Utils.convertDpToPixel(
                getResources().getDimension(R.dimen.nav_header_height), context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (ev.getY() < navHeaderHeight) {
                    Log.d(TAG, "Action Down and Top " + navHeaderHeight);
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
