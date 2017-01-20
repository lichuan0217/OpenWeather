package top.lemonsoda.openweather.view.ui.helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import top.lemonsoda.openweather.R;
import top.lemonsoda.openweather.model.entry.CurrentWeather;
import top.lemonsoda.openweather.model.entry.ForecastWeather;
import top.lemonsoda.openweather.model.entry.Weather;

/**
 * Created by Chuan on 08/01/2017.
 */

public class WeatherInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_TODAY = 0;
    private static final int TYPE_NEXT_DAYS = 1;

    private Context context;
    private CurrentWeather currentWeather;
    private ForecastWeather forecastWeather;


    public WeatherInfoAdapter(Context cxt, Weather weather) {
        context = cxt;
        currentWeather = weather.getCurrentWeather();
        forecastWeather = weather.getForecastWeather();
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case TYPE_TODAY:
                return TYPE_TODAY;
            case TYPE_NEXT_DAYS:
                return TYPE_NEXT_DAYS;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_TODAY:
                View view = LayoutInflater.from(context).inflate(R.layout.layout_weather_today_1, parent, false);
                return new CurrentWeatherViewHolder(view);
            case TYPE_NEXT_DAYS:


        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class CurrentWeatherViewHolder extends RecyclerView.ViewHolder {

        public CurrentWeatherViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class NextDaysWeatherViewHolder extends RecyclerView.ViewHolder {

        public NextDaysWeatherViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
