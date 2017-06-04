package top.lemonsoda.openweather.presenter;

import top.lemonsoda.openweather.view.WeatherViewInterface;

/**
 * Created by Chuan on 01/06/2017.
 */

public interface WeatherPresenterInterface extends BasePresenter{

    void getWeatherById(int id, boolean forceUpdate);

    void setWeatherView(WeatherViewInterface view);
}
