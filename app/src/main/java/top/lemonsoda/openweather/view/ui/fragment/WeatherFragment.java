package top.lemonsoda.openweather.view.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.lemonsoda.openweather.R;
import top.lemonsoda.openweather.domain.utils.Constants;
import top.lemonsoda.openweather.domain.utils.Utils;
import top.lemonsoda.openweather.model.entry.CurrentWeather;
import top.lemonsoda.openweather.model.entry.ForecastWeather;
import top.lemonsoda.openweather.model.entry.Weather;
import top.lemonsoda.openweather.presenter.IWeatherPresenter;
import top.lemonsoda.openweather.presenter.impl.WeatherPresenterImpl;
import top.lemonsoda.openweather.view.IWeatherView;

/**
 * Created by chuanl on 7/19/16.
 */
public class WeatherFragment extends BaseFragment implements IWeatherView {
    private static final String TAG = WeatherFragment.class.getCanonicalName();

    @BindView(R.id.tv_city_name)
    TextView tvCityName;

    @BindView(R.id.tv_temp)
    TextView tvTemp;

    @BindView(R.id.tv_desc)
    TextView tvDesc;

//    @BindView(R.id.ll_forecast)
//    LinearLayout llForecast;

    @BindView(R.id.rv_forecast)
    RecyclerView rvForecast;

    private IWeatherPresenter weatherPresenter;
    private String cityName;
    private Weather weatherInfo = null;
    private ForecastAdapter forecastAdapter;

    public WeatherFragment() {
    }

    public static WeatherFragment newInstance(String cityName) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString(Constants.ARG_CITY_NAME, cityName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherPresenter = new WeatherPresenterImpl(this);
        if (getArguments() != null) {
            cityName = getArguments().getString(Constants.ARG_CITY_NAME, "Beijing");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvForecast.setLayoutManager(llm);
        forecastAdapter = new ForecastAdapter();
        rvForecast.setAdapter(forecastAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isDataInitiated && weatherInfo != null) {
            showWeather();
        }
    }

    @Override
    public void fetchData() {
        weatherPresenter.getWeather(cityName);
    }

    @Override
    public void setWeatherInfo(Weather weather) {
        isDataInitiated = true;
        Log.d(TAG, "setWeatherInfo");
        Log.d(TAG, "Temp: " + weather.getCurrentWeather().getMain().getTemp());
        Log.d(TAG, "Cnt: " + weather.getForecastWeather().getCnt());
        Log.d(TAG, "Forecast: " + weather.getForecastWeather().getList().get(0).getDt_txt());
        weatherInfo = weather;
        showWeather();
        forecastAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {
        Log.d(TAG, "showLoading...");
    }

    @Override
    public void hideLoading() {
        Log.d(TAG, "hideLoading...");
    }

    @Override
    public void showError() {
        Toast.makeText(getActivity(), "Error...", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "showError...");
    }

    private void showWeather() {
        CurrentWeather currentWeather = weatherInfo.getCurrentWeather();
        String temp = Utils.formatTemperature(getActivity(), currentWeather.getMain().getTemp());
        tvCityName.setText(currentWeather.getName());
        tvTemp.setText(temp);
        tvDesc.setText(currentWeather.getWeather().get(0).getDescription());
    }

    class ForecastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_forecast_item, parent, false);
            return new ForecastViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ForecastViewHolder viewHolder = (ForecastViewHolder)holder;

            ForecastWeather forecastWeather = weatherInfo.getForecastWeather();
//            double d_temp = forecastWeather.getList().get(position).getMain().getTemp();
//            int weather_id = forecastWeather.getList().get(position).getWeather().get(0).getId();
            viewHolder.tvTemp.setText(Utils.formatTemperature(getActivity(), 10));
            viewHolder.imgIcon.setImageResource(Utils.getArtResourceForWeatherCondition(800));
            ViewGroup.LayoutParams params = viewHolder.llContainer.getLayoutParams();
            params.width = Utils.getScreenWidth(getActivity()) / 4;
            viewHolder.llContainer.setLayoutParams(params);
        }

        @Override
        public int getItemCount() {
            if (weatherInfo == null)
              return 0;
            else {
                return 4;
            }
//            return 4;
        }
    }

    class ForecastViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_date)
        TextView tvDate;

        @BindView(R.id.tv_item_temp)
        TextView tvTemp;

        @BindView(R.id.img_item_icon)
        ImageView imgIcon;

        @BindView(R.id.ll_item_container)
        LinearLayout llContainer;


        public ForecastViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
