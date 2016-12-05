package top.lemonsoda.openweather.view;

import java.util.List;

import top.lemonsoda.openweather.model.entry.City;

/**
 * Created by Chuan on 04/11/2016.
 */

public interface ICityListView {

    void setCityInfo(List<City> cities);

    void showLoading();

    void hideLoading();

    void showError();
}
