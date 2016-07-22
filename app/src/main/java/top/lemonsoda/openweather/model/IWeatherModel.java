package top.lemonsoda.openweather.model;

import top.lemonsoda.openweather.presenter.IOnWeatherListener;

/**
 * Created by chuanl on 7/16/16.
 */
public interface IWeatherModel {

    void loadWeather(String cityName, IOnWeatherListener listener);
}
