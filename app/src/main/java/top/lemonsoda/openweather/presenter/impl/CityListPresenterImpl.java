package top.lemonsoda.openweather.presenter.impl;

import java.util.List;

import top.lemonsoda.openweather.model.ICityListModel;
import top.lemonsoda.openweather.model.entry.City;
import top.lemonsoda.openweather.model.impl.CityListModelImpl;
import top.lemonsoda.openweather.presenter.ICityListPresenter;
import top.lemonsoda.openweather.presenter.IOnCityListListener;
import top.lemonsoda.openweather.view.ICityListView;

/**
 * Created by Chuan on 04/11/2016.
 */

public class CityListPresenterImpl implements ICityListPresenter, IOnCityListListener {

    ICityListView cityListView;
    ICityListModel cityListModel;

    public CityListPresenterImpl(ICityListView cityListView) {
        this.cityListView = cityListView;
        this.cityListModel = new CityListModelImpl();
    }

    @Override
    public void getCityList(String pattern) {
        cityListView.showLoading();
        cityListModel.loadCityList(pattern, this);
    }

    @Override
    public void onSuccess(List<City> cities) {
        cityListView.hideLoading();
        cityListView.setCityInfo(cities);
    }

    @Override
    public void onError() {
        cityListView.hideLoading();
        cityListView.showError();
    }
}
