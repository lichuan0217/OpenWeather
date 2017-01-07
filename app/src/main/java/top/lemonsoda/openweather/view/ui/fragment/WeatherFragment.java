package top.lemonsoda.openweather.view.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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
import butterknife.OnClick;
import top.lemonsoda.openweather.R;
import top.lemonsoda.openweather.domain.utils.Constants;
import top.lemonsoda.openweather.domain.utils.NotificationUtils;
import top.lemonsoda.openweather.domain.utils.Utils;
import top.lemonsoda.openweather.domain.utils.WeatherSharedPreference;
import top.lemonsoda.openweather.model.entry.City;
import top.lemonsoda.openweather.model.entry.CurrentWeather;
import top.lemonsoda.openweather.model.entry.ForecastWeather;
import top.lemonsoda.openweather.model.entry.Weather;
import top.lemonsoda.openweather.presenter.IWeatherPresenter;
import top.lemonsoda.openweather.presenter.impl.WeatherPresenterImpl;
import top.lemonsoda.openweather.view.IWeatherView;
import top.lemonsoda.openweather.view.ui.activity.WeatherActivity;

/**
 * Created by chuanl on 7/19/16.
 */
public class WeatherFragment extends BaseFragment
        implements IWeatherView, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = WeatherFragment.class.getCanonicalName();

    @BindView(R.id.ll_weather_container)
    LinearLayout llWeatherContainer;

    @BindView(R.id.tv_temp)
    TextView tvTemp;

    @BindView(R.id.tv_weather_rain)
    TextView tvWeatherRain;

    @BindView(R.id.tv_weather_wind)
    TextView tvWeatherWind;

    @BindView(R.id.img_weather_icon)
    ImageView imgWeatherIcon;

    @BindView(R.id.tv_last_update)
    TextView tvLastUpdate;

    @BindView(R.id.srl_weather)
    SwipeRefreshLayout srlWeather;

    @BindView(R.id.rv_forecast)
    RecyclerView rvForecast;

    private IWeatherPresenter weatherPresenter;
    private City city;
    private int index;
    private Weather weatherInfo = null;
    private ForecastAdapter forecastAdapter;
    private OnForecastItemClickListener forecastItemClickListener;

    public WeatherFragment() {
    }

    public static WeatherFragment newInstance(City city, int pos) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.ARG_CITY, city);
        args.putInt(Constants.ARG_CITY_INDEX, pos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherPresenter = new WeatherPresenterImpl(this);
        if (getArguments() != null) {
            city = getArguments().getParcelable(Constants.ARG_CITY);
            index = getArguments().getInt(Constants.ARG_CITY_INDEX, -1);
        }
        Log.d(TAG, "OnCreate: " + city);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "OnCreateView: " + city);
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        ButterKnife.bind(this, view);

        srlWeather.setOnRefreshListener(this);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvForecast.setLayoutManager(llm);
        forecastAdapter = new ForecastAdapter();
        rvForecast.setAdapter(forecastAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "OnResume: " + city);

        weatherInfo = ((WeatherActivity) getActivity()).getWeatherInfo(city);
        if (weatherInfo == null) {
            weatherInfo = WeatherSharedPreference.getWeatherInfo(getActivity(), city.get_id());
            if (weatherInfo != null) {
                Log.d(TAG, "Load weather from sharedPreference");
                Log.d(TAG, "WeatherSharedPreference: " + weatherInfo.getCurrentWeather().getName()
                        + ", " + weatherInfo.getCurrentWeather().getMain().getTemp());
                showWeather();
            }
        } else {
            isDataInitiated = true;
            showWeather();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnForecastItemClickListener) {
            forecastItemClickListener = (OnForecastItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void fetchData() {
        weatherPresenter.getWeatherById(city.get_id());
    }

    @Override
    public void setWeatherInfo(Weather weather) {
        isDataInitiated = true;
        weatherInfo = weather;
        WeatherSharedPreference.saveWeatherInfo(getActivity(), weather);
        ((WeatherActivity) getActivity()).addWeatherInfo(city, weather);
        showWeather();
        if (NotificationUtils.getInstance().getCityId() == city.get_id()) {
            NotificationUtils.getInstance().updateNotification(getActivity(), city.get_id());
        }
    }

    @Override
    public void showLoading() {
        Log.d(TAG, "showLoading...");
        if (!srlWeather.isRefreshing()) {
            srlWeather.post(new Runnable() {
                @Override
                public void run() {
                    srlWeather.setRefreshing(true);
                }
            });
        }
    }

    @Override
    public void hideLoading() {
        srlWeather.setRefreshing(false);
        Log.d(TAG, "hideLoading...");
    }

    @Override
    public void showError() {
        srlWeather.setRefreshing(false);
        Toast.makeText(getActivity(), getString(R.string.error_message), Toast.LENGTH_SHORT).show();
        Log.d(TAG, "showError...");
    }

    private void showWeather() {
        Log.d(TAG, "ShowWeather: " + city);
        CurrentWeather currentWeather = weatherInfo.getCurrentWeather();
        String temp = Utils.formatTemperature(getActivity(), currentWeather.getMain().getTemp());
        tvTemp.setText(temp);
        tvWeatherRain.setText(getString(R.string.format_humidity, currentWeather.getMain().getHumidity()));
        tvWeatherWind.setText(
                Utils.getFormattedWind(
                        getActivity(),
                        (float) currentWeather.getWind().getSpeed(),
                        (float) currentWeather.getWind().getDeg()));
        int weatherId = currentWeather.getWeather().get(0).getId();
        imgWeatherIcon.setImageResource(Utils.getArtResourceForWeatherCondition(weatherId));
        tvLastUpdate.setText(Utils.getFormatLastUpdate(getActivity(), weatherInfo.getUpdate()));
        forecastAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        prepareFetchData(true);
    }

    class ForecastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_forecast_item, parent, false);
            return new ForecastViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ForecastViewHolder viewHolder = (ForecastViewHolder) holder;

            ForecastWeather forecastWeather = weatherInfo.getForecastWeather();
            int dt = forecastWeather.getList().get(position).getDt();
            double d_temp = forecastWeather.getList().get(position).getTemp().getDay();
            int weather_id = forecastWeather.getList().get(position).getWeather().get(0).getId();

            viewHolder.tvDate.setText(Utils.getMonthDay(getActivity(), dt));
            viewHolder.tvTemp.setText(Utils.formatTemperature(getActivity(), d_temp));
            viewHolder.imgIcon.setImageResource(Utils.getArtResourceForWeatherCondition(weather_id));
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
        }
    }

    public class ForecastViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_date)
        TextView tvDate;

        @BindView(R.id.tv_item_temp)
        TextView tvTemp;

        @BindView(R.id.img_item_icon)
        public ImageView imgIcon;

        @BindView(R.id.ll_item_container)
        LinearLayout llContainer;

        @OnClick(R.id.ll_item_container)
        void onClick() {
            if (weatherInfo == null) {
                return;
            }
            forecastItemClickListener.onItemClick(
                    this, getLayoutPosition(), weatherInfo);
        }

        public ForecastViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnForecastItemClickListener {
        void onItemClick(ForecastViewHolder holder, int pos, Weather weather);
    }

}
