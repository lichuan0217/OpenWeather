package top.lemonsoda.openweather.view.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import top.lemonsoda.openweather.R;
import top.lemonsoda.openweather.domain.utils.Constants;
import top.lemonsoda.openweather.domain.utils.Utils;
import top.lemonsoda.openweather.model.entry.CurrentWeather;
import top.lemonsoda.openweather.model.entry.ForecastWeather;

public class DetailActivity extends BaseActivity {

    @BindView(R.id.tv_detail_date)
    TextView tvDetailDate;

    @BindView(R.id.tv_detail_forecast)
    TextView tvDetailForecast;

    @BindView(R.id.tv_detail_high)
    TextView tvDetailHigh;

    @BindView(R.id.tv_detail_low)
    TextView tvDetailLow;

    @BindView(R.id.tv_detail_humidity)
    TextView tvDetailHumidity;

    @BindView(R.id.tv_detail_pressure)
    TextView tvDetailPressure;

    @BindView(R.id.tv_detail_wind)
    TextView tvDetailWind;

    @BindView(R.id.img_detail_icon)
    ImageView imgDetailIcon;


    ForecastWeather forecastWeather;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResID = R.layout.activity_detail;
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (getIntent() != null) {
            forecastWeather = getIntent().getParcelableExtra(Constants.ARG_DETAIL_KEY);
            index = getIntent().getIntExtra(Constants.ARG_DETAIL_DAY, 0);
        }

        if (forecastWeather == null)
            return;

        setDetailInfo();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setDetailInfo() {
        ForecastWeather.ListBean bean = forecastWeather.getList().get(index);
        tvDetailDate.setText(Utils.getFormatDayString(bean.getDt()));
        tvDetailForecast.setText(bean.getWeather().get(0).getMain());
        tvDetailHigh.setText(Utils.formatTemperature(this, bean.getTemp().getMax()));
        tvDetailLow.setText(Utils.formatTemperature(this, bean.getTemp().getMin()));
        tvDetailHumidity.setText(getString(R.string.format_humidity, bean.getHumidity()));
        tvDetailPressure.setText(getString(R.string.format_pressure, bean.getPressure()));
        tvDetailWind.setText(Utils.getFormattedWind(this, (float) bean.getSpeed(), (float) bean.getDeg()));
        int weatherId = bean.getWeather().get(0).getId();
        imgDetailIcon.setImageResource(Utils.getArtResourceForWeatherCondition(weatherId));
    }

}
