package top.lemonsoda.openweather.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.lemonsoda.openweather.R;
import top.lemonsoda.openweather.domain.utils.Utils;
import top.lemonsoda.openweather.domain.utils.WeatherSharedPreference;
import top.lemonsoda.openweather.model.entry.City;
import top.lemonsoda.openweather.model.entry.ForecastWeather;
import top.lemonsoda.openweather.model.entry.Weather;

/**
 * Created by chuanl on 12/23/16.
 */

public class PlaceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<City> cityList;

    public PlaceAdapter(Context cxt, List<City> list) {
        context = cxt;
        cityList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_place_item, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PlaceViewHolder viewHolder = (PlaceViewHolder)holder;
        City city = cityList.get(position);
        Weather weather = WeatherSharedPreference.getWeatherInfo(context, city.get_id());
        ForecastWeather forecastWeather = weather.getForecastWeather();
        int weatherId = forecastWeather.getList().get(position).getWeather().get(0).getId();
        double temp = forecastWeather.getList().get(position).getTemp().getDay();
        String time = Utils.getFormatDate(context, weather.getCurrentWeather().getDt());

        viewHolder.tvPlaceName.setText(city.getName());
        viewHolder.imgPlaceIcon.setImageResource(Utils.getArtResourceForWeatherCondition(weatherId));
        viewHolder.tvPlaceTemp.setText(Utils.formatTemperature(context, temp));
        viewHolder.tvPlaceTime.setText(time);
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.img_place_icon)
        ImageView imgPlaceIcon;

        @BindView(R.id.tv_place_name)
        TextView tvPlaceName;

        @BindView(R.id.tv_place_temp)
        TextView tvPlaceTemp;

        @BindView(R.id.tv_place_time)
        TextView tvPlaceTime;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
