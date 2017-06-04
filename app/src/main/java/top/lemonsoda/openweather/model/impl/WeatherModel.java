package top.lemonsoda.openweather.model.impl;

import rx.Observable;
import rx.functions.Func2;
import top.lemonsoda.openweather.domain.net.WeatherService;
import top.lemonsoda.openweather.domain.utils.Utils;
import top.lemonsoda.openweather.model.WeatherModelInterface;
import top.lemonsoda.openweather.model.entry.CurrentWeather;
import top.lemonsoda.openweather.model.entry.ForecastWeather;
import top.lemonsoda.openweather.model.entry.Weather;

/**
 * Created by Chuan on 01/06/2017.
 */

public class WeatherModel implements WeatherModelInterface {

    private final static String UNITS_METRIC = "metric";
    private final static int CNT = 7;
    private WeatherService weatherService;

    public WeatherModel(WeatherService service) {
        this.weatherService = service;
    }

    @Override
    public Observable<Weather> loadWeatherById(int id) {
        return Observable.zip(
                weatherService.getForecastWeatherById(id, UNITS_METRIC, CNT),
                weatherService.getCurrentWeatherById(id, UNITS_METRIC),

                new Func2<ForecastWeather, CurrentWeather, Weather>() {
                    @Override
                    public Weather call(ForecastWeather forecastWeather, CurrentWeather currentWeather) {
                        Weather weather = new Weather();
                        weather.setCurrentWeather(currentWeather);
                        weather.setForecastWeather(forecastWeather);
                        weather.setUpdate(Utils.getCurrentDate());
                        return weather;
                    }
                });
    }
}
