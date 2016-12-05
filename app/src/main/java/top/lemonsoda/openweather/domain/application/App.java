package top.lemonsoda.openweather.domain.application;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import top.lemonsoda.openweather.R;

/**
 * Created by Chuan on 10/17/16.
 */

public class App extends Application {
    private static final String TAG = App.class.getCanonicalName();

    private static App application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static App getApplication() {
        return application;
    }

}
