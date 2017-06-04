package top.lemonsoda.openweather.domain.application;

import android.app.Activity;
import android.app.Application;

import timber.log.Timber;

/**
 * Created by Chuan on 31/05/2017.
 */

public class WeatherApplication extends Application {

    WeatherApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());

        component = DaggerWeatherApplicationComponent.builder()
                .weatherApplicationModule(new WeatherApplicationModule(this))
                .build();
    }

    public WeatherApplicationComponent getComponent() {
        return component;
    }

    public static WeatherApplication getWeatherApplication(Activity activity) {
        return (WeatherApplication) activity.getApplicationContext();
    }
}
