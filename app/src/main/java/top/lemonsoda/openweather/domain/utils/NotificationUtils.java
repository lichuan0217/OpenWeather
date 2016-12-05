package top.lemonsoda.openweather.domain.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import top.lemonsoda.openweather.R;
import top.lemonsoda.openweather.model.entry.Weather;
import top.lemonsoda.openweather.view.ui.activity.WeatherActivity;

/**
 * Created by Chuan on 16/11/2016.
 */

public class NotificationUtils {

    private int NOTIFICATION_ID = 0;
    private int city_id;

    private static class NotificationUtilsHolder {
        private static final NotificationUtils INSTANCE = new NotificationUtils();
    }

    private NotificationUtils() {
    }

    public static NotificationUtils getInstance() {
        return NotificationUtilsHolder.INSTANCE;
    }

    public int getCityId() {
        return city_id;
    }

    public void createNotification(Context context, int id) {
        city_id = id;
        Intent intent = new Intent(context, WeatherActivity.class);
        intent.putExtra(Constants.INTENT_ARG_CITY_ID, id);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);

        String title = "N/A";
        String content = "N/A";
        int iconId = R.mipmap.ic_weather_rain;

        Weather weather = WeatherSharedPreference.getWeatherInfo(context, id);
        if (weather != null) {
            String desc = weather.getCurrentWeather().getWeather().get(0).getDescription();
            String temp = Utils.formatTemperature(context, weather.getCurrentWeather().getMain().getTemp());

            title = Utils.upperFirstLetter(desc) + " " + temp;
            content = weather.getCurrentWeather().getName();
            iconId = Utils.getResourceForWeatherCondition(weather.getCurrentWeather().getWeather().get(0).getId());
        }

        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(iconId)
                .setOngoing(true)
                .setContentIntent(pendingIntent).build();

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, notification);

    }

    public void cancelNotification(Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        notificationManager.cancel(NOTIFICATION_ID);
    }

    public void updateNotification(Context context, int id) {
        createNotification(context, id);
    }

}

