package top.lemonsoda.openweather.view.ui.custom;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Chuan on 01/06/2017.
 */

public class HeaderViewPager extends ViewPager {
    private static final String TAG = HeaderViewPager.class.getCanonicalName();

    public HeaderViewPager(Context context) {
        super(context);
    }

    public HeaderViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (ev.getY() < 450) {
                    Log.d(TAG, "Action Down and Top 450");
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
