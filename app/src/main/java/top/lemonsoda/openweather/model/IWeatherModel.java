package top.lemonsoda.openweather.model;

import rx.Observable;
import top.lemonsoda.openweather.model.entry.Weather;
import top.lemonsoda.openweather.presenter.IOnWeatherListener;

/**
 * Created by chuanl on 7/16/16.
 */
public interface IWeatherModel {

    void loadWeather(String cityName, IOnWeatherListener listener);

    void loadWeatherById(int id, IOnWeatherListener listener);

    Observable<Weather> loadWeatherById(int id);
}
