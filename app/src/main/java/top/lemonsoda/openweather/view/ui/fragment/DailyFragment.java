package top.lemonsoda.openweather.view.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.lemonsoda.openweather.R;
import top.lemonsoda.openweather.domain.utils.Utils;
import top.lemonsoda.openweather.model.entry.City;
import top.lemonsoda.openweather.model.entry.ForecastWeather;
import top.lemonsoda.openweather.model.entry.Weather;
import top.lemonsoda.openweather.presenter.WeatherPresenterInterface;
import top.lemonsoda.openweather.view.WeatherViewInterface;
import top.lemonsoda.openweather.view.ui.helper.OnContainerInteractionListener;

public class DailyFragment extends Fragment implements WeatherViewInterface,
        SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = DailyFragment.class.getCanonicalName();
    private static final String ARG_CITY_ID = "city_id";

    private City city;
    private Weather weather;
    private OnContainerInteractionListener listener;
    private WeatherPresenterInterface weatherPresenter;
    private DailyAdapter adapter;


    @BindView(R.id.rv_weather_daily)
    RecyclerView rvWeatherDaily;

    @BindView(R.id.srl_weather_daily)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;

    @BindView(R.id.img_network_failed)
    ImageView imgError;

    public DailyFragment() {
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
        View view = inflater.inflate(R.layout.fragment_daily, container, false);
        ButterKnife.bind(this, view);
        refreshLayout.setOnRefreshListener(this);
        rvWeatherDaily.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new DailyAdapter();
        rvWeatherDaily.setAdapter(adapter);
        rvWeatherDaily.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    adapter.setAnimationsLocked(true);
                }
            }
        });
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
        listener.onSetTitle(getString(R.string.title_daily_forecast));
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
        refreshLayout.setRefreshing(false);
        refreshLayout.setVisibility(View.GONE);
        pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        Log.d(TAG, "hide loading...");
        refreshLayout.setRefreshing(false);
        refreshLayout.setVisibility(View.VISIBLE);
        rvWeatherDaily.setVisibility(View.VISIBLE);
        imgError.setVisibility(View.GONE);
        pbLoading.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        refreshLayout.setRefreshing(false);
        refreshLayout.setVisibility(View.VISIBLE);
        rvWeatherDaily.setVisibility(View.GONE);
        imgError.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.GONE);
        Toast.makeText(getActivity(), "Error...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresent(WeatherPresenterInterface presenter) {
        this.weatherPresenter = presenter;
        this.weatherPresenter.setWeatherView(this);
    }

    private void showWeather(Weather weather) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        if (this.weatherPresenter != null) {
            this.weatherPresenter.reset();
            this.weatherPresenter.start(city.get_id());
        }
    }

    class DailyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;

        private int lastAnimatedPosition = 0;
        private boolean animationsLocked = false;
        private boolean delayEnterAnimation = true;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_HEADER) {
                View view = LayoutInflater.from(
                        getActivity()).inflate(R.layout.layout_daily_item_header, parent, false);
                return new DailyHeaderViewHolder(view);
            }
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_daily_item, parent, false);
            return new DailyViewHolder(view);
        }

        @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof DailyViewHolder) {
                DailyViewHolder viewHolder = (DailyViewHolder) holder;
                runEnterAnimation(viewHolder.itemView, position);
                ForecastWeather forecastWeather = weather.getForecastWeather();
                ForecastWeather.ListBean bean = forecastWeather.getList().get(position - 1);
                viewHolder.tvDailyDate.setText(Utils.getFullWeekNameString(bean.getDt()));
                String description = bean.getWeather().get(0).getDescription();
                description = Character.toUpperCase(description.charAt(0)) + description.substring(1);
                viewHolder.tvDailyDescription.setText(description);
                viewHolder.tvDailyTemperature.setText(
                        Utils.formatTemperatureHighLow(
                                getActivity(),
                                bean.getTemp().getMax(),
                                bean.getTemp().getMin()));
                viewHolder.tvDailyContent.setText(
                        Utils.getFormattedWindContent(
                                getActivity(),
                                (float) bean.getSpeed(),
                                (float) bean.getDeg()));
                viewHolder.imgDailyIcon.setImageResource(
                        Utils.getArtResourceForWeatherCondition(bean.getWeather().get(0).getId()));
            } else if (holder instanceof DailyHeaderViewHolder) {
                DailyHeaderViewHolder viewHolder = (DailyHeaderViewHolder) holder;
                viewHolder.tvCityName.setText(city.getName());
                viewHolder.tvWeatherDes.setText(
                        Utils.getDescriptionForNext7Days(weather.getForecastWeather(), getActivity()));
            }


        }

        @Override
        public int getItemCount() {
            if (weather == null)
                return 0;
            return weather.getForecastWeather().getCnt() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            return position == 0 ? TYPE_HEADER : TYPE_ITEM;
        }

        @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        private void runEnterAnimation(View view, int position) {
            if (animationsLocked) return;

            if (position > lastAnimatedPosition) {
                lastAnimatedPosition = position;
                view.setTranslationY(Utils.getScreenHeight(getActivity()));
                view.setAlpha(0.f);
                view.animate()
                        .translationY(0).alpha(1.f)
                        .setStartDelay(delayEnterAnimation ? 20 * (position) : 0)
                        .setInterpolator(new DecelerateInterpolator(2.f))
                        .setDuration(1000)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                animationsLocked = true;
                            }
                        })
                        .start();
            }
        }

        public void setAnimationsLocked(boolean animationsLocked) {
            this.animationsLocked = animationsLocked;
        }

        public void setDelayEnterAnimation(boolean delayEnterAnimation) {
            this.delayEnterAnimation = delayEnterAnimation;
        }
    }

    class DailyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_daily_current_date)
        TextView tvDailyDate;

        @BindView(R.id.tv_daily_weather_dec)
        TextView tvDailyDescription;

        @BindView(R.id.tv_daily_tmp)
        TextView tvDailyTemperature;

        @BindView(R.id.tv_daily_content)
        TextView tvDailyContent;

        @BindView(R.id.img_daily_icon)
        ImageView imgDailyIcon;

        public DailyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class DailyHeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_daily_city_name)
        TextView tvCityName;

        @BindView(R.id.tv_daily_weather_describe)
        TextView tvWeatherDes;


        public DailyHeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
