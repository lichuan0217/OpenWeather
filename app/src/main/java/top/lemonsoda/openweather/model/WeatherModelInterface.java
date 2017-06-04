package top.lemonsoda.openweather.model;

import rx.Observable;
import top.lemonsoda.openweather.model.entry.Weather;

/**
 * Created by Chuan on 01/06/2017.
 */

public interface WeatherModelInterface {

    Observable<Weather> loadWeatherById(int id);
}
