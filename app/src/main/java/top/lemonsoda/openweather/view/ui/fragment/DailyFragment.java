package top.lemonsoda.openweather.view.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.lemonsoda.openweather.R;
import top.lemonsoda.openweather.model.entry.City;
import top.lemonsoda.openweather.model.entry.Weather;
import top.lemonsoda.openweather.presenter.WeatherPresenterInterface;
import top.lemonsoda.openweather.view.WeatherViewInterface;
import top.lemonsoda.openweather.view.ui.helper.OnContainerInteractionListener;

public class DailyFragment extends Fragment implements WeatherViewInterface {
    private static final String TAG = DailyFragment.class.getCanonicalName();
    private static final String ARG_CITY_ID = "city_id";

    private City city;
    private Weather weather;
    private OnContainerInteractionListener listener;
    private WeatherPresenterInterface weatherPresenter;

    @BindView(R.id.tv_daily)
    TextView textView;

    public DailyFragment() {
        // Required empty public constructor
    }

    public static DailyFragment newInstance(City city) {
        DailyFragment fragment = new DailyFragment();
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
        View view = inflater.inflate(R.layout.fragment_daily, container, false);
        ButterKnife.bind(this, view);
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
        listener.onSetTitle("Daily Forecasts", 1);
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
        Log.d(TAG, "onDestroyView");
        super.onDestroyView();
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
        Log.d(TAG, "loading...");
    }

    @Override
    public void hideLoading() {
        Log.d(TAG, "hide loading...");
    }

    @Override
    public void showError() {
        textView.setText("Error...");
    }

    @Override
    public void setPresent(WeatherPresenterInterface presenter) {
        this.weatherPresenter = presenter;
        this.weatherPresenter.setWeatherView(this);
    }

    private void showWeather(Weather weather) {
        textView.setText("Forecasts: " + weather.getForecastWeather().getList().size());
    }
}
