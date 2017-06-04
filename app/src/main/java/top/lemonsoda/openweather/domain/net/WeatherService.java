package top.lemonsoda.openweather.domain.net;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import top.lemonsoda.openweather.domain.utils.Constants;
import top.lemonsoda.openweather.model.entry.CurrentWeather;
import top.lemonsoda.openweather.model.entry.ForecastWeather;

/**
 * Created by Chuan on 31/05/2017.
 */

public interface WeatherService {

    @GET("weather?appid=" + Constants.OPEN_WEATHER_MAP_API_KEY)
    Observable<CurrentWeather> getCurrentWeatherByName(
            @Query("q") String cityName, @Query("units") String units);

    @GET("forecast/daily?appid=" + Constants.OPEN_WEATHER_MAP_API_KEY)
    Observable<ForecastWeather> getForecastWeatherByName(
            @Query("q") String cityName,
            @Query("units") String units,
            @Query("cnt") int cnt
    );

    @GET("weather?appid=" + Constants.OPEN_WEATHER_MAP_API_KEY)
    Observable<CurrentWeather> getCurrentWeatherById(
            @Query("id") int id, @Query("units") String units);

    @GET("forecast/daily?appid=" + Constants.OPEN_WEATHER_MAP_API_KEY)
    Observable<ForecastWeather> getForecastWeatherById(
            @Query("id") int id,
            @Query("units") String units,
            @Query("cnt") int cnt
    );
}
