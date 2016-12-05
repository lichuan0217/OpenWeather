package top.lemonsoda.openweather.model.impl;

import android.util.Log;

import java.util.List;

import rx.Observer;
import top.lemonsoda.openweather.domain.net.OpenWeatherMapManager;
import top.lemonsoda.openweather.model.ICityListModel;
import top.lemonsoda.openweather.model.entry.City;
import top.lemonsoda.openweather.presenter.IOnCityListListener;

/**
 * Created by Chuan on 04/11/2016.
 */

public class CityListModelImpl implements ICityListModel {

    private static final String TAG = CityListModelImpl.class.getCanonicalName();


    @Override
    public void loadCityList(String pattern, final IOnCityListListener listener) {


        Observer<List<City>> cityListObserver = new Observer<List<City>>() {

            private List<City> cityList;

            @Override
            public void onCompleted() {
                Log.d(TAG, "Get cities complete");
                listener.onSuccess(cityList);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "Error: " + e.getMessage());
                listener.onError();
            }

            @Override
            public void onNext(List<City> cities) {
                Log.d(TAG, "Get cities : " + cities.toString());
                cityList = cities;
            }
        };

        OpenWeatherMapManager.getInstance().getCityList(cityListObserver, pattern);
    }
}
