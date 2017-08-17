package top.lemonsoda.openweather.view.ui.helper;

import top.lemonsoda.openweather.model.entry.Weather;

/**
 * Created by Chuan on 31/05/2017.
 */

public interface OnContainerInteractionListener {

    void onDetail(String cityId, String date);

    void onSetTitle(String title);

    void onSaveWeather(int id, Weather weather);

    Weather onGetWeather(int id);

}
