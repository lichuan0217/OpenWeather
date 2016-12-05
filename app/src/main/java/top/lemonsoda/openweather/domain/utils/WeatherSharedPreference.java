package top.lemonsoda.openweather.domain.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

import top.lemonsoda.openweather.model.entry.Weather;

/**
 * Created by Chuan on 14/11/2016.
 */

public class WeatherSharedPreference {

    private static final String TAG = WeatherSharedPreference.class.getCanonicalName();

    public static void saveWeatherInfo(Context context, Weather weather) {
        Log.d(TAG, "Save Weather");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String jsonWeather = gson.toJson(weather);

        String key = Constants.PREF_WEATHER_KEY + weather.getCurrentWeather().getId();
        Log.d(TAG, "Save Key: " + key);
        editor.putString(key, jsonWeather);
        editor.commit();
    }

    public static Weather getWeatherInfo(Context context, int id) {
        Log.d(TAG, "Get Weather");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Weather weather;

        String key = Constants.PREF_WEATHER_KEY + id;
        Log.d(TAG, "Get Key: " + key);

        if (preferences.contains(key)) {
            String jsonWeather = preferences.getString(key, null);
            Gson gson = new Gson();
            weather = gson.fromJson(jsonWeather, Weather.class);
        } else {
            return null;
        }

        return weather;
    }
}
