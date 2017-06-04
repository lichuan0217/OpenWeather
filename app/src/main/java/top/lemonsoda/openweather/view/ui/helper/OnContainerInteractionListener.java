package top.lemonsoda.openweather.view.ui.helper;

import top.lemonsoda.openweather.model.entry.Weather;

/**
 * Created by Chuan on 31/05/2017.
 */

public interface OnContainerInteractionListener {

    public void onDetail(String cityId, String date);

    public void onSetTitle(String title, int type);

    public void onSaveWeather(int id, Weather weather);

    public Weather onGetWeather(int id);
}
