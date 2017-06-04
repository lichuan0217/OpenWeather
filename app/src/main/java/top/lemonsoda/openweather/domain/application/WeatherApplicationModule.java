package top.lemonsoda.openweather.domain.application;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import top.lemonsoda.openweather.domain.utils.di.WeatherApplicationScope;

/**
 * Created by Chuan on 31/05/2017.
 */

@Module
public class WeatherApplicationModule {

    private final Context context;

    public WeatherApplicationModule(Context context) {
        this.context = context.getApplicationContext();
    }

    @Provides
    @WeatherApplicationScope
    public Context provideContext() {
        return context;
    }
}
