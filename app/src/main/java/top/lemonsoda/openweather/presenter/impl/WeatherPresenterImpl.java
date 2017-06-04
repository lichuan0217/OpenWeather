package top.lemonsoda.openweather.presenter.impl;

import top.lemonsoda.openweather.model.IWeatherModel;
import top.lemonsoda.openweather.model.entry.Weather;
import top.lemonsoda.openweather.model.impl.WeatherModelImpl;
import top.lemonsoda.openweather.presenter.IOnWeatherListener;
import top.lemonsoda.openweather.presenter.IWeatherPresenter;
import top.lemonsoda.openweather.view.IWeatherView;

/**
 * Created by chuanl on 7/16/16.
 */
public class WeatherPresenterImpl implements IWeatherPresenter, IOnWeatherListener {

    private IWeatherModel weatherModel;
    private IWeatherView weatherView;

    public WeatherPresenterImpl(IWeatherView weatherView) {
        this.weatherView = weatherView;
        this.weatherModel = new WeatherModelImpl(null);
    }

    @Override
    public void onSuccess(Weather weather) {
        weatherView.hideLoading();
        weatherView.setWeatherInfo(weather);
    }

    @Override
    public void onError() {
        weatherView.hideLoading();
        weatherView.showError();
    }

    @Override
    public void getWeather(String cityName) {
        weatherView.showLoading();
        weatherModel.loadWeather(cityName, this);
    }

    @Override
    public void getWeatherById(int id) {
        weatherView.showLoading();
        weatherModel.loadWeatherById(id, this);
    }
}
