package top.lemonsoda.openweather.view.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.lemonsoda.openweather.R;
import top.lemonsoda.openweather.domain.utils.CitySharedPreference;
import top.lemonsoda.openweather.domain.utils.Constants;
import top.lemonsoda.openweather.model.entry.Weather;
import top.lemonsoda.openweather.view.ui.helper.CityItemTouchHelperCallback;
import top.lemonsoda.openweather.view.ui.helper.ItemTouchHelperAdapter;

public class CityManageActivity extends BaseActivity {

    private static final String TAG = CityManageActivity.class.getCanonicalName();

    @BindView(R.id.rv_city_list)
    RecyclerView rvCityList;

    @BindView(R.id.actv_search)
    AutoCompleteTextView actvSearch;

    private List<String> cityList;
    private CityListAdapter cityListAdapter;

    private boolean dataChanged = false;
    private int dataChangeId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResID = R.layout.activity_city_manage;
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cityList = CitySharedPreference.getCityList(this);
        Log.d(TAG, "CityList: " + cityList == null ? "null" : cityList.toString());

        if (cityList == null) {
            CitySharedPreference.addCity(this, "Beijing");
            cityList = CitySharedPreference.getCityList(this);
        }

        String[] cities = getResources().getStringArray(R.array.city_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, cities);
        actvSearch.setAdapter(adapter);
        actvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String city = (String) parent.getItemAtPosition(position);
                if (!cityList.contains(city)) {
                    CitySharedPreference.addCity(CityManageActivity.this, city);
                    cityList.add(city);
                    cityListAdapter.notifyDataSetChanged();
                    dataChanged = true;
                }
                actvSearch.setText("");
            }
        });

        rvCityList.setLayoutManager(new LinearLayoutManager(this));
        cityListAdapter = new CityListAdapter(this, cityList);
        rvCityList.setAdapter(cityListAdapter);
        ItemTouchHelper.Callback callback = new CityItemTouchHelperCallback(cityListAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rvCityList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CityManageActivity.this, WeatherActivity.class);
        intent.putExtra(Constants.ARG_CITY_MANAGE_CHANGED, dataChanged);
        intent.putExtra(Constants.ARG_CITY_MANAGE_CHANGE_ID, dataChangeId);
        setResult(RESULT_OK, intent);
        Log.d(TAG, "onBackPressed");
        super.onBackPressed();
    }

    public class CityListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter {

        private LayoutInflater inflater;
        private Context context;
        private List<String> cityList;

        public CityListAdapter(Context cxt, List<String> cities) {
            this.cityList = cities;
            this.context = cxt;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.layout_city_manage_item, parent, false);
            return new CityItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            CityItemViewHolder viewHolder = (CityItemViewHolder) holder;
            viewHolder.tvCityName.setText(this.cityList.get(position));
        }

        @Override
        public int getItemCount() {
            return this.cityList.size();
        }

        @Override
        public void onItemMove(int fromPosition, int toPosition) {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(this.cityList, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(this.cityList, i, i - 1);
                }
            }
            notifyItemMoved(fromPosition, toPosition);
            CitySharedPreference.saveCityList(CityManageActivity.this, cityList);
            dataChanged = true;
        }

        @Override
        public void onItemDismiss(int position) {
            this.cityList.remove(position);
            notifyItemRemoved(position);
            CitySharedPreference.saveCityList(CityManageActivity.this, cityList);
            dataChanged = true;
            dataChangeId = position;
        }
    }

    public class CityItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_layout_city_name)
        TextView tvCityName;

        public CityItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
