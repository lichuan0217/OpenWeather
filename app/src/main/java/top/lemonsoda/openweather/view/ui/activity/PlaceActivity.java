package top.lemonsoda.openweather.view.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.lemonsoda.openweather.R;
import top.lemonsoda.openweather.domain.utils.CitySharedPreference;
import top.lemonsoda.openweather.model.entry.City;
import top.lemonsoda.openweather.view.adapter.PlaceAdapter;

public class PlaceActivity extends BaseActivity {

    private List<City> cityList;
    private PlaceAdapter adapter;

    @BindView(R.id.rv_place)
    RecyclerView rvPlaceList;

    @OnClick(R.id.fab_add_place)
    void onAddPlaceClick(View view) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResID = R.layout.activity_place;
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);

        loadCityInfo();
        rvPlaceList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PlaceAdapter(this, cityList);
        rvPlaceList.setAdapter(adapter);
    }

    private void loadCityInfo() {
        if (CitySharedPreference.getManagedCityList(this) == null) {
            City beijing = new City();
            /*
            {
              "lat": 39.907501,
              "country": "CN",
              "_id": 1816670,
              "lon": 116.397232,
              "name": "Beijing"
            }
            */
            beijing.setName("Beijing");
            beijing.set_id(1816670);
            beijing.setCountry("CN");
            beijing.setLat(39.907501);
            beijing.setLon(116.397232);
            CitySharedPreference.addManagedCity(this, beijing);
        }

        cityList = CitySharedPreference.getManagedCityList(this);

    }


}
