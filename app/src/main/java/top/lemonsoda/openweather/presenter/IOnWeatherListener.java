package top.lemonsoda.openweather.presenter;

import top.lemonsoda.openweather.model.entry.Weather;

/**
 * Created by chuanl on 7/16/16.
 */
public interface IOnWeatherListener {

    void onSuccess(Weather weather);

    void onError();
}
