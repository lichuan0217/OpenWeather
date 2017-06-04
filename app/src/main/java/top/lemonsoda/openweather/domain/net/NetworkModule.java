package top.lemonsoda.openweather.domain.net;

import android.content.Context;

import java.io.File;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;
import top.lemonsoda.openweather.domain.application.WeatherApplicationModule;
import top.lemonsoda.openweather.domain.utils.Constants;
import top.lemonsoda.openweather.domain.utils.di.WeatherApplicationScope;

/**
 * Created by Chuan on 31/05/2017.
 */

@Module(includes = WeatherApplicationModule.class)
public class NetworkModule {

    @Provides
    @WeatherApplicationScope
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(
                new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Timber.i(message);
                    }
                }
        );
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    @Provides
    @WeatherApplicationScope
    public Cache provideCache(Context context) {
        File cacheFile = new File(context.getCacheDir(), Constants.CACHE_FILE_NAME);
        cacheFile.mkdirs();
        Cache cache = new Cache(cacheFile, 10 * 1000 * 1000); //10MB
        return cache;
    }


    @Provides
    @WeatherApplicationScope
    public OkHttpClient provideOkHttpClient(HttpLoggingInterceptor interceptor, Cache cache) {
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .cache(cache)
                .build();
    }
}
