package top.lemonsoda.openweather.view.ui.activity;

import android.app.SearchManager;
import android.content.AsyncQueryHandler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import top.lemonsoda.openweather.R;
import top.lemonsoda.openweather.domain.utils.CitySharedPreference;
import top.lemonsoda.openweather.domain.utils.Constants;
import top.lemonsoda.openweather.model.entry.City;
import top.lemonsoda.openweather.view.ui.custom.EmptyRecyclerView;
import top.lemonsoda.openweather.view.ui.helper.CityAdapter;
import top.lemonsoda.openweather.view.ui.helper.CityItemTouchHelperCallback;

public class LocationManageActivity extends BaseActivity {
    private static final String TAG = LocationManageActivity.class.getCanonicalName();


    @BindView(R.id.rv_manage_city_list)
    EmptyRecyclerView rvCityList;

    @BindView(R.id.ll_empty_view)
    View llEmptyView;

    @BindView(R.id.btn_empty_add)
    ImageButton btnEmptyAdd;

    SearchView searchView;

    private QueryHandler queryHandler;
    private List<City> cityList;
    private CityAdapter adapter;

    private Map<Integer, City> cityMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResID = R.layout.activity_location_manage;
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        queryHandler = new QueryHandler(this);

        cityList = CitySharedPreference.getManagedCityList(this);
        cityMap = CitySharedPreference.getManagedCityListMap(this);

        btnEmptyAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setIconified(false);
            }
        });

        rvCityList.setEmptyView(llEmptyView);
        rvCityList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CityAdapter(this, cityList);
        rvCityList.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new CityItemTouchHelperCallback(adapter, cityList);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rvCityList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_location_manage, menu);

        MenuItem searchMenu = menu.findItem(R.id.action_city_search);
        Drawable wrapped = DrawableCompat.wrap(searchMenu.getIcon());
        DrawableCompat.setTint(wrapped, ContextCompat.getColor(this, R.color.white));
        searchMenu.setIcon(wrapped);

        searchView = (SearchView) MenuItemCompat.getActionView(searchMenu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(new ComponentName(this, LocationManageActivity.class)));

        return true;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d(TAG, "Action Search : " + query);
            Toast.makeText(this, "Searching by: " + query, Toast.LENGTH_SHORT).show();

        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Log.d(TAG, "intent.getData() : " + intent.getDataString());
            queryHandler.startQuery(0, null, intent.getData(), null, null, null, null);
            Log.d(TAG, "Action View: " + intent.getData().getLastPathSegment());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LocationManageActivity.this, WeatherActivity.class);
        intent.putExtra(Constants.ARG_CITY_MANAGE_CHANGED, adapter.isDataChanged());
        setResult(RESULT_OK, intent);
        Log.d(TAG, "onBackPressed");
        super.onBackPressed();
    }

    public void updateView() {
        cityList.clear();
        cityList.addAll(CitySharedPreference.getManagedCityList(this));
        Log.d(TAG, "update view: cityList: " + cityList);
        adapter.notifyDataSetChanged();
        adapter.setDataChanged(true);
    }

    public class QueryHandler extends AsyncQueryHandler {

        WeakReference<LocationManageActivity> activityRef;

        public QueryHandler(LocationManageActivity locationManageActivity) {
            super(locationManageActivity.getContentResolver());
            activityRef = new WeakReference<>(locationManageActivity);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            super.onQueryComplete(token, cookie, cursor);
            if (cursor == null || cursor.getCount() == 0) {
                Log.d(TAG, "null");
                return;
            }
            cursor.moveToFirst();
            String cityName = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
            String cityId = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID));
            Log.d(TAG, "onQueryComplete: " + cityName + ", " + cityId);
            cursor.close();

            if (activityRef.get() != null) {
                activityRef.get().updateView();
            }
        }
    }
}
