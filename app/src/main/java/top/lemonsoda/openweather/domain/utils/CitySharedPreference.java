package top.lemonsoda.openweather.domain.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.lemonsoda.openweather.model.entry.City;

/**
 * Created by Chuan on 7/26/16.
 */
public class CitySharedPreference {

    public static void saveManagedCityList(Context context, List<City> cities) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String jsonCities = gson.toJson(cities);

        editor.putString(Constants.PREF_MANAGE_CITY_KEY, jsonCities);
        editor.commit();
    }

    public static List<City> getManagedCityList(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        List<City> cityList;

        if (preferences.contains(Constants.PREF_MANAGE_CITY_KEY)) {
            String jsonCities = preferences.getString(Constants.PREF_MANAGE_CITY_KEY, null);
            Gson gson = new Gson();
            City[] cityItems = gson.fromJson(jsonCities, City[].class);

            cityList = Arrays.asList(cityItems);
            cityList = new ArrayList<>(cityList);
        } else {
            return null;
        }

        return cityList;
    }

    public static Map<Integer, City> getManagedCityListMap(Context context) {
        List<City> list = getManagedCityList(context);
        Map<Integer, City> map = new HashMap<>();
        for (City city : list) {
            map.put(city.get_id(), city);
        }
        return map;
    }

    public static void addManagedCity(Context context, City city) {
        List<City> cityList = getManagedCityList(context);
        if (cityList == null) {
            cityList = new ArrayList<>();
        }
        cityList.add(city);
        Log.d("chuanl ", "CityList : " + cityList.toString());
        saveManagedCityList(context, cityList);
    }
}
