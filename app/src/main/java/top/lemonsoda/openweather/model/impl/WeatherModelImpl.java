package top.lemonsoda.openweather.model.impl;

import rx.Observer;
import top.lemonsoda.openweather.domain.net.OpenWeatherMapManager;
import top.lemonsoda.openweather.model.IWeatherModel;
import top.lemonsoda.openweather.model.entry.Weather;
import top.lemonsoda.openweather.presenter.IOnWeatherListener;

/**
 * Created by chuanl on 7/16/16.
 */
public class WeatherModelImpl implements IWeatherModel {
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
                listener.onError();
            }

            @Override
            public void onNext(Weather w) {
                weather = w;
            }
        };

        OpenWeatherMapManager.getInstance().getWeatherByName(weatherObserver, cityName, 3);
    }
}
