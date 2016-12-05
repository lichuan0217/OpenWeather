package top.lemonsoda.openweather.model.entry;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chuanl on 7/16/16.
 */
public class Weather implements Parcelable {

    private CurrentWeather currentWeather;
    private ForecastWeather forecastWeather;
    private String update;

    public CurrentWeather getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(CurrentWeather currentWeather) {
        this.currentWeather = currentWeather;
    }

    public ForecastWeather getForecastWeather() {
        return forecastWeather;
    }

    public void setForecastWeather(ForecastWeather forecastWeather) {
        this.forecastWeather = forecastWeather;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.currentWeather, flags);
        dest.writeParcelable(this.forecastWeather, flags);
        dest.writeString(this.update);
    }

    public Weather() {
    }

    protected Weather(Parcel in) {
        this.currentWeather = in.readParcelable(CurrentWeather.class.getClassLoader());
        this.forecastWeather = in.readParcelable(ForecastWeather.class.getClassLoader());
        this.update = in.readString();
    }

    public static final Parcelable.Creator<Weather> CREATOR = new Parcelable.Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel source) {
            return new Weather(source);
        }

        @Override
        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };
}
