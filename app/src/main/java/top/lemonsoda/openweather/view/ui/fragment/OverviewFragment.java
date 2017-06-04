package top.lemonsoda.openweather.view.ui.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.lemonsoda.openweather.R;
import top.lemonsoda.openweather.domain.utils.Utils;
import top.lemonsoda.openweather.model.entry.City;
import top.lemonsoda.openweather.model.entry.CurrentWeather;
import top.lemonsoda.openweather.model.entry.ForecastWeather;
import top.lemonsoda.openweather.model.entry.Weather;
import top.lemonsoda.openweather.presenter.WeatherPresenterInterface;
import top.lemonsoda.openweather.view.WeatherViewInterface;
import top.lemonsoda.openweather.view.ui.helper.OnContainerInteractionListener;

public class OverviewFragment extends Fragment implements WeatherViewInterface, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = OverviewFragment.class.getCanonicalName();
    private static final String ARG_CITY_ID = "city_id";

    private City city;
    private Weather weather;

    private OnContainerInteractionListener listener;
    private WeatherPresenterInterface weatherPresenter;
    private FutureAdapter futureAdapter;

    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;

    @BindView(R.id.img_network_failed)
    ImageView imgError;

    @BindView(R.id.cv_overview_current)
    CardView cvOverviewCurrent;

    @BindView(R.id.cv_overview_future)
    CardView cvOverViewFuture;

    @BindView(R.id.cv_overview_current_detail)
    CardView cvOverviewDetail;

    @BindView(R.id.srl_weather_overview)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.ll_weather_overview)
    LinearLayout llWeatherOverViewContent;

    @BindView(R.id.tv_current_date)
    TextView tvCurrentDate;

    @BindView(R.id.tv_current_forecast)
    TextView tvCurrentForecast;

    @BindView(R.id.tv_current_now)
    TextView tvCurrentNow;

    @BindView(R.id.tv_current_high_low)
    TextView tvCurrentHighLow;

    @BindView(R.id.img_current_icon)
    ImageView imgCurrentIcon;

    @BindView(R.id.rv_overview_future)
    RecyclerView rvFuture;

    @BindView(R.id.tv_detail_clouds_value)
    TextView tvDetailClouds;

    @BindView(R.id.tv_detail_humidity_value)
    TextView tvDetailHumidity;

    @BindView(R.id.tv_detail_pressure_value)
    TextView tvDetailPressure;

    @BindView(R.id.tv_detail_wind_value)
    TextView tvDetailWind;


    public OverviewFragment() {
    }

    public static OverviewFragment newInstance(City city) {
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CITY_ID, city);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            city = getArguments().getParcelable(ARG_CITY_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        ButterKnife.bind(this, view);
        swipeRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvFuture.setLayoutManager(llm);
        futureAdapter = new FutureAdapter();
        rvFuture.setAdapter(futureAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        weather = listener.onGetWeather(city.get_id());
        if (this.weatherPresenter != null && weather == null) {
            weatherPresenter.start(city.get_id());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        listener.onSetTitle("Overview", 0);
        if (weather != null) {
            showWeather(weather);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnContainerInteractionListener) {
            listener = (OnContainerInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView" + city.getName());
        weatherPresenter.destroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void setWeatherInfo(Weather weather) {
        this.weather = weather;
        listener.onSaveWeather(city.get_id(), weather);
        showWeather(weather);
    }

    @Override
    public void showLoading() {
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setVisibility(View.GONE);
        pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        Log.d(TAG, "hide loading...");
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        llWeatherOverViewContent.setVisibility(View.VISIBLE);
        imgError.setVisibility(View.GONE);
        pbLoading.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        llWeatherOverViewContent.setVisibility(View.GONE);
        imgError.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.GONE);
        Toast.makeText(getActivity(), "Error...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresent(WeatherPresenterInterface presenter) {
        this.weatherPresenter = presenter;
        this.weatherPresenter.setWeatherView(this);
    }

    @Override
    public void onRefresh() {
        if (this.weatherPresenter != null) {
            this.weatherPresenter.reset();
            this.weatherPresenter.start(city.get_id());
        }
    }

    private void showWeather(Weather weather) {
        CurrentWeather currentWeather = weather.getCurrentWeather();
        renderCurrent(currentWeather);
        renderCurrentDetail(currentWeather);
        futureAdapter.notifyDataSetChanged();
        startAnimation();
    }

    private void renderCurrent(CurrentWeather currentWeather) {
//        tvCurrentDate.setText(Utils.getFriendlyCurrentDayString(getActivity()));
        tvCurrentDate.setText(city.getName() + ", " + city.getCountry());
        tvCurrentForecast.setText(currentWeather.getWeather().get(0).getMain());
        tvCurrentNow.setText(Utils.formatTemperature(getActivity(), currentWeather.getMain().getTemp()));
        tvCurrentHighLow.setText(Utils.formatTemperatureHighLow(
                getActivity(),
                currentWeather.getMain().getTemp_max(),
                currentWeather.getMain().getTemp_min()));
        imgCurrentIcon.setImageResource(Utils.getArtResourceForWeatherCondition(
                currentWeather.getWeather().get(0).getId()));
    }

    private void renderCurrentDetail(CurrentWeather currentWeather) {
        String wind = Utils.getFormattedWind(
                getActivity(),
                (float) currentWeather.getWind().getSpeed(),
                (float) currentWeather.getWind().getDeg());
        tvDetailWind.setText(wind);

        String pressure = getString(R.string.format_pressure, currentWeather.getMain().getPressure());
        tvDetailPressure.setText(pressure);

        String humidity = getString(R.string.format_humidity, currentWeather.getMain().getHumidity());
        tvDetailHumidity.setText(humidity);

        String cloud = getString(R.string.format_cloud, currentWeather.getClouds().getAll());
        tvDetailClouds.setText(cloud);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void startAnimation() {
        Log.d(TAG, "Screen Height: " + Utils.getScreenHeight(getActivity()));

        cvOverviewCurrent.setTranslationY(Utils.getScreenHeight(getActivity()));
        cvOverviewCurrent.animate().translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(700)
                .start();

        cvOverViewFuture.setTranslationY(Utils.getScreenHeight(getActivity()));
        cvOverViewFuture.animate().translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(1000)
                .start();

        cvOverviewDetail.setTranslationY(Utils.getScreenHeight(getActivity()));
        cvOverviewDetail.animate().translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(1300)
                .start();
    }

    class FutureAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_forecast_item, parent, false);
            return new FutureViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            FutureViewHolder viewHolder = (FutureViewHolder) holder;

            ForecastWeather forecastWeather = weather.getForecastWeather();
            int dt = forecastWeather.getList().get(position).getDt();
            double d_temp = forecastWeather.getList().get(position).getTemp().getDay();
            int weather_id = forecastWeather.getList().get(position).getWeather().get(0).getId();

            viewHolder.tvDate.setText(Utils.getWeekNameString(dt));
            viewHolder.tvTemp.setText(Utils.formatTemperature(getActivity(), d_temp));
            viewHolder.imgIcon.setImageResource(Utils.getArtResourceForWeatherCondition(weather_id));

            int screenWidth = Utils.getScreenWidth(getActivity());
            int cardMargin = (int) getResources().getDimension(R.dimen.overview_card_margin);
            int recyclerMargin = (int) getResources().getDimension(R.dimen.overview_future_recycler_margin);
            ViewGroup.LayoutParams params = viewHolder.llContainer.getLayoutParams();
            params.width = (screenWidth - 2 * cardMargin - 2 * recyclerMargin) / 5;
            viewHolder.llContainer.setLayoutParams(params);

        }

        @Override
        public int getItemCount() {
            if (weather == null)
                return 0;
            return weather.getForecastWeather().getCnt();
        }
    }

    class FutureViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_date)
        TextView tvDate;

        @BindView(R.id.tv_item_temp)
        TextView tvTemp;

        @BindView(R.id.img_item_icon)
        public ImageView imgIcon;

        @BindView(R.id.ll_item_container)
        LinearLayout llContainer;

        public FutureViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
