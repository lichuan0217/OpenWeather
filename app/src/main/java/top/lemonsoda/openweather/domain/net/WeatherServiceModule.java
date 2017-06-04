package top.lemonsoda.openweather.domain.net;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import top.lemonsoda.openweather.domain.utils.di.WeatherApplicationScope;

import static top.lemonsoda.openweather.domain.utils.Constants.BASE_URL;

/**
 * Created by Chuan on 31/05/2017.
 */

@Module(includes = {NetworkModule.class})
public class WeatherServiceModule {

    @Provides
    @WeatherApplicationScope
    public Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides
    @WeatherApplicationScope
    public WeatherService provideNewsService(Retrofit retrofit) {
        return retrofit.create(WeatherService.class);
    }
}
