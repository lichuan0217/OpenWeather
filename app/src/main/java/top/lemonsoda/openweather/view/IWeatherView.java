package top.lemonsoda.openweather.view;

import top.lemonsoda.openweather.model.entry.Weather;

/**
 * Created by chuanl on 7/16/16.
 */
public interface IWeatherView {

    void setWeatherInfo(Weather weather);

    void showLoading();

    void hideLoading();

    void showError();
}
