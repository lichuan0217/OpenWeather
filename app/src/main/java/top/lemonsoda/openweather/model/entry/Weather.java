package top.lemonsoda.openweather.model.entry;

/**
 * Created by chuanl on 7/16/16.
 */
public class Weather {

    private CurrentWeather currentWeather;
    private ForecastWeather forecastWeather;

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
}
