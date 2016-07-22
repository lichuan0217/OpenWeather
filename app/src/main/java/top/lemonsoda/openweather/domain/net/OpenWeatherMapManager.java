package top.lemonsoda.openweather.domain.net;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import top.lemonsoda.openweather.domain.utils.Constants;
import top.lemonsoda.openweather.model.entry.CurrentWeather;
import top.lemonsoda.openweather.model.entry.ForecastWeather;
import top.lemonsoda.openweather.model.entry.Weather;

/**
 * Created by chuanl on 7/16/16.
 */
public class OpenWeatherMapManager {

    private final static String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private final static String UNITS_METRIC = "metric";

    private Retrofit retrofit;
    private IWeatherService weatherService;

    private OpenWeatherMapManager() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        weatherService = retrofit.create(IWeatherService.class);
    }

    private static class SingletonHolder {
        private static final OpenWeatherMapManager INSTANCE = new OpenWeatherMapManager();
    }

    public static OpenWeatherMapManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public interface IWeatherService {

        @GET("weather?appid=" + Constants.OPEN_WEATHER_MAP_API_KEY)
        Observable<CurrentWeather> getCurrentWeatherByName(
                @Query("q") String cityName, @Query("units") String units);

        @GET("forecast/daily?appid=" + Constants.OPEN_WEATHER_MAP_API_KEY)
        Observable<ForecastWeather> getForecastWeatherByName(
                @Query("q") String cityName,
                @Query("units") String units,
                @Query("cnt") int cnt
        );

    }

    public void getWeatherByName(Observer<Weather> observer, String cityName, int cnt) {
        Observable.zip(
                weatherService.getCurrentWeatherByName(cityName, UNITS_METRIC),
                weatherService.getForecastWeatherByName(cityName, UNITS_METRIC, cnt),
                new Func2<CurrentWeather, ForecastWeather, Weather>() {
                    @Override
                    public Weather call(CurrentWeather currentWeather, ForecastWeather forecastWeather) {
                        Weather weather = new Weather();
                        weather.setCurrentWeather(currentWeather);
                        weather.setForecastWeather(forecastWeather);
                        return weather;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
