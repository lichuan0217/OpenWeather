package top.lemonsoda.openweather.presenter.impl;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import top.lemonsoda.openweather.domain.utils.RxUtils;
import top.lemonsoda.openweather.model.WeatherModelInterface;
import top.lemonsoda.openweather.model.entry.Weather;
import top.lemonsoda.openweather.presenter.WeatherPresenterInterface;
import top.lemonsoda.openweather.view.WeatherViewInterface;

/**
 * Created by Chuan on 01/06/2017.
 */

public class WeatherPresenter implements WeatherPresenterInterface {

    private boolean firstLoad = true;
    private WeatherViewInterface weatherView;
    private WeatherModelInterface weatherModel;

    private Subscription loadSubscription;

    public WeatherPresenter(WeatherModelInterface weatherModel) {
        this.weatherModel = weatherModel;
    }

    @Override
    public void setWeatherView(WeatherViewInterface weatherView) {
        this.weatherView = weatherView;
    }

    @Override
    public void start(int id) {
        getWeatherById(id, firstLoad);
        firstLoad = false;
    }

    @Override
    public void destroy() {
        RxUtils.unsubscribe(loadSubscription);
    }

    @Override
    public void reset() {
        firstLoad = true;
    }

    @Override
    public void getWeatherById(int id, boolean forceUpdate) {
        if (!forceUpdate)
            return;
        weatherView.showLoading();
        loadSubscription = weatherModel.loadWeatherById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<Weather>() {
                            @Override
                            public void call(Weather weather) {
                                weatherView.hideLoading();
                                weatherView.setWeatherInfo(weather);
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                weatherView.showError();
                            }
                        }
                );
    }
}
