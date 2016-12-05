package top.lemonsoda.openweather.presenter;

import java.util.List;

import top.lemonsoda.openweather.model.entry.City;

/**
 * Created by Chuan on 04/11/2016.
 */

public interface IOnCityListListener {

    void onSuccess(List<City> city);

    void onError();
}
