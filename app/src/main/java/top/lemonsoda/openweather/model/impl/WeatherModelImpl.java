package top.lemonsoda.openweather.model.impl;

import rx.Observable;
import rx.Observer;
import rx.functions.Func2;
import top.lemonsoda.openweather.domain.net.OpenWeatherMapManager;
import top.lemonsoda.openweather.domain.net.WeatherService;
import top.lemonsoda.openweather.domain.utils.Utils;
import top.lemonsoda.openweather.model.IWeatherModel;
import top.lemonsoda.openweather.model.entry.CurrentWeather;
import top.lemonsoda.openweather.model.entry.ForecastWeather;
import top.lemonsoda.openweather.model.entry.Weather;
import top.lemonsoda.openweather.presenter.IOnWeatherListener;

/**
 * Created by chuanl on 7/16/16.
 */
public class WeatherModelImpl implements IWeatherModel {
    private final static String UNITS_METRIC = "metric";
    private final static int CNT = 5;
    private WeatherService weatherService;

    public WeatherModelImpl(WeatherService service) {
        this.weatherService = service;
    }


    @Override
    public void loadWeather(String cityName, final IOnWeatherListener listener) {
        Observer<Weather> weatherObserver = new Observer<Weather>() {

            private Weather weather;

            @Override
            public void onCompleted() {
                listener.onSuccess(weather);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                listener.onError();
            }

            @Override
            public void onNext(Weather w) {
                weather = w;
            }
        };

        OpenWeatherMapManager.getInstance().getWeatherByName(weatherObserver, cityName, 4);
    }

    @Override
    public void loadWeatherById(int id, final IOnWeatherListener listener) {
        Observer<Weather> weatherObserver = new Observer<Weather>() {

            private Weather weather;

            @Override
            public void onCompleted() {
                listener.onSuccess(weather);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                listener.onError();
            }

            @Override
            public void onNext(Weather w) {
                weather = w;
            }
        };

        OpenWeatherMapManager.getInstance().getWeatherById(weatherObserver, id, 4);
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
