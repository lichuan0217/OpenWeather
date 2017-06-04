package top.lemonsoda.openweather.view;

import top.lemonsoda.openweather.model.entry.Weather;
import top.lemonsoda.openweather.presenter.WeatherPresenterInterface;

/**
 * Created by Chuan on 01/06/2017.
 */

public interface WeatherViewInterface {

    void setWeatherInfo(Weather weather);

    void showLoading();

    void hideLoading();

    void showError();

    void setPresent(WeatherPresenterInterface presenter);
}
