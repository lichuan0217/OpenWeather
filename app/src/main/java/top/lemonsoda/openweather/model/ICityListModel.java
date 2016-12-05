package top.lemonsoda.openweather.model;

import top.lemonsoda.openweather.presenter.IOnCityListListener;

/**
 * Created by Chuan on 04/11/2016.
 */

public interface ICityListModel {

    void loadCityList(String pattern, IOnCityListListener listener);
}
