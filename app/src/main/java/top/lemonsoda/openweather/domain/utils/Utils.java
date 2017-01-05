package top.lemonsoda.openweather.domain.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import top.lemonsoda.openweather.R;

/**
 * Created by chuanl on 7/19/16.
 */
public class Utils {


    public static String getStatusbarCity(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(context.getString(R.string.pref_notification_choose_city_key), null);
    }

    public static void setStatusbarCity(Context context, String id) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.pref_notification_choose_city_key), id);
        editor.commit();
    }

    public static boolean isAutoLocationEnable(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(context.getString(R.string.pref_auto_location_key), false);
    }

    public static boolean isMetric(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_units_key),
                context.getString(R.string.pref_units_metric))
                .equals(context.getString(R.string.pref_units_metric));
    }

    public static String formatTemperature(Context context, double temperature) {
        if (!isMetric(context)) {
            temperature = (temperature * 1.8) + 32;
        }

        // For presentation, assume the user doesn't care about tenths of a degree.
        return String.format(context.getString(R.string.format_temperature), temperature);
    }

    public static boolean isShowNotification(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(context.getString(R.string.pref_show_notification_key), true);
    }

    public static int getNotificationCityId(Context context, int id) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String t = prefs.getString(context.getString(R.string.pref_notification_choose_city_key), String.valueOf(id));
        return Integer.parseInt(t);
    }

    public static void updateNotificationLocation(Context context, int id) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(context.getString(R.string.pref_notification_choose_city_key),
                String.valueOf(id));
        editor.commit();
    }

    public static int getArtResourceForWeatherCondition(int weatherId) {
        if (weatherId >= 200 && weatherId <= 232) {
            return R.mipmap.art_storm;
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.mipmap.art_light_rain;
        } else if (weatherId >= 500 && weatherId <= 504) {
            return R.mipmap.art_rain;
        } else if (weatherId == 511) {
            return R.mipmap.art_snow;
        } else if (weatherId >= 520 && weatherId <= 531) {
            return R.mipmap.art_rain;
        } else if (weatherId >= 600 && weatherId <= 622) {
            return R.mipmap.art_snow;
        } else if (weatherId >= 701 && weatherId <= 761) {
            return R.mipmap.art_fog;
        } else if (weatherId == 761 || weatherId == 781) {
            return R.mipmap.art_storm;
        } else if (weatherId == 800) {
            return R.mipmap.art_clear;
        } else if (weatherId == 801) {
            return R.mipmap.art_light_clouds;
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.mipmap.art_clouds;
        }
        return -1;
    }


    public static int getResourceForWeatherCondition(int weatherId) {
        if (weatherId >= 200 && weatherId <= 232) {
            return R.mipmap.ic_storm;
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.mipmap.ic_light_rain;
        } else if (weatherId >= 500 && weatherId <= 504) {
            return R.mipmap.ic_rain;
        } else if (weatherId == 511) {
            return R.mipmap.ic_snow;
        } else if (weatherId >= 520 && weatherId <= 531) {
            return R.mipmap.ic_rain;
        } else if (weatherId >= 600 && weatherId <= 622) {
            return R.mipmap.ic_snow;
        } else if (weatherId >= 701 && weatherId <= 761) {
            return R.mipmap.ic_fog;
        } else if (weatherId == 761 || weatherId == 781) {
            return R.mipmap.ic_storm;
        } else if (weatherId == 800) {
            return R.mipmap.ic_clear;
        } else if (weatherId == 801) {
            return R.mipmap.ic_light_clouds;
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.mipmap.ic_cloudy;
        }
        return R.mipmap.ic_weather_rain;
    }


    public static String getFormatDate(Context context, int millis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
        return simpleDateFormat.format(new Date(millis * 1000l));
//        return DateUtils.formatDateTime(context, millis, DateUtils.FORMAT_SHOW_TIME);
    }

    public static String getFormatLastUpdate(Context context, String date) {
        long minDiff = 30;
        String formatLastUpdate;
        Calendar now = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleSdf = new SimpleDateFormat("MM-dd HH:mm");
        SimpleDateFormat todaySdf = new SimpleDateFormat("HH:mm");
        Calendar update = new GregorianCalendar();
        try {
            update.setTime(sdf.parse(date));
            minDiff = now.getTimeInMillis() - update.getTimeInMillis();
            minDiff = minDiff / (60 * 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (minDiff < 1) {
            formatLastUpdate = String.format(context.getString(R.string.format_last_update_just_now));
        } else if (minDiff < 30) {
            formatLastUpdate = String.format(context.getString(R.string.format_last_update_in_half), minDiff);
        } else if (minDiff < 24 * 60) {
            formatLastUpdate = String.format(context.getString(R.string.format_last_update), todaySdf.format(update.getTime()));
        } else {
            formatLastUpdate = String.format(context.getString(R.string.format_last_update), simpleSdf.format(update.getTime()));
        }
        return formatLastUpdate;
    }

    public static String getCurrentDate() {
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(calendar.getTime());
    }


    public static String getMonthDay(Context context, int timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("dd", Locale.ENGLISH);

        Date today = new Date(System.currentTimeMillis());
        String todayMonthDay = format.format(today);

        Date date = new Date(timestamp * 1000l);
        String monthDay = format.format(date);

        return todayMonthDay.equals(monthDay) ? context.getString(R.string.info_now) : monthDay;
    }


    public static String getFormatDayString(int timestamp) {
        Date date = new Date(timestamp * 1000l);
        SimpleDateFormat format = new SimpleDateFormat("MMMM dd", Locale.ENGLISH);
        return format.format(date);
    }

    public static String getFriendlyCurrentDayString(Context context) {

        Calendar cal = new GregorianCalendar();

        String today = context.getString(R.string.today);
        int formatId = R.string.format_full_friendly_date;

        SimpleDateFormat monthDayFormat = new SimpleDateFormat("MMMM dd", Locale.ENGLISH);
        String monthDayString = monthDayFormat.format(cal.getTime());

        return String.format(context.getString(formatId), today, monthDayString);
    }


    public static String getFormattedWind(Context context, float windSpeed, float degrees) {
        int windFormat;
        if (isMetric(context)) {
            windFormat = R.string.format_wind_kmh;
        } else {
            windFormat = R.string.format_wind_mph;
            windSpeed = .621371192237334f * windSpeed;
        }

        String direction = "Unknown";
        if (degrees >= 337.5 || degrees < 22.5) {
            direction = "N";
        } else if (degrees >= 22.5 && degrees < 67.5) {
            direction = "NE";
        } else if (degrees >= 67.5 && degrees < 112.5) {
            direction = "E";
        } else if (degrees >= 112.5 && degrees < 157.5) {
            direction = "SE";
        } else if (degrees >= 157.5 && degrees < 202.5) {
            direction = "S";
        } else if (degrees >= 202.5 && degrees < 247.5) {
            direction = "SW";
        } else if (degrees >= 247.5 && degrees < 292.5) {
            direction = "W";
        } else if (degrees >= 292.5 && degrees < 337.5) {
            direction = "NW";
        }
        return String.format(context.getString(windFormat), windSpeed, direction);
    }


    public static int getScreenWidth(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.widthPixels;
    }

    public static int getScreenHeight(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.heightPixels;
    }

    public static String upperFirstLetter(String string) {
        return Character.toUpperCase(string.charAt(0)) + string.substring(1);
    }

    public static String createShareContent(Context context,
                                            String city,
                                            String desc,
                                            String temp) {
        return String.format(context.getString(R.string.share_content), city, desc, temp);
    }
}
