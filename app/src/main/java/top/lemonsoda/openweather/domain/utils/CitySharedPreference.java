package top.lemonsoda.openweather.domain.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Chuan on 7/26/16.
 */
public class CitySharedPreference {

    public static void saveCityList(Context context, List<String> cities) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String jsonCities = gson.toJson(cities);

        editor.putString(Constants.PREF_CITY_KEY, jsonCities);
        editor.commit();
    }

    public static ArrayList<String> getCityList(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        List<String> cityList;

        if (preferences.contains(Constants.PREF_CITY_KEY)) {
            String jsonCities = preferences.getString(Constants.PREF_CITY_KEY, null);
            Gson gson = new Gson();
            String[] cityItems = gson.fromJson(jsonCities, String[].class);

            cityList = Arrays.asList(cityItems);
            cityList = new ArrayList<>(cityList);
        } else {
            return null;
        }

        return (ArrayList<String>) cityList;
    }

    public static void addCity(Context context, String city) {
        List<String> cityList = getCityList(context);
        if (cityList == null) {
            cityList = new ArrayList<>();
        }
        cityList.add(city);
        saveCityList(context, cityList);
    }

    public static void removeCity(Context context, String city) {
        List<String> cityList = getCityList(context);
        if (cityList != null) {
            cityList.remove(city);
            saveCityList(context, cityList);
        }
    }
}
