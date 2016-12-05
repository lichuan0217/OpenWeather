package top.lemonsoda.openweather.domain.contentprovider;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import top.lemonsoda.openweather.domain.net.OpenWeatherMapManager;
import top.lemonsoda.openweather.domain.utils.CitySharedPreference;
import top.lemonsoda.openweather.domain.utils.Constants;
import top.lemonsoda.openweather.model.entry.City;

public class CitySuggestionProvider extends ContentProvider {

    private static final String TAG = CitySuggestionProvider.class.getCanonicalName();

    private static final int TYPE_ALL_SUGGESTION = 0;
    private static final int TYPE_SINGLE_SUGGESTION = 1;

    private UriMatcher uriMatcher;
    private List<City> cityData;

    public CitySuggestionProvider() {
    }

    @Override
    public boolean onCreate() {
        cityData = new ArrayList<>();
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Constants.AUTHORITY_CITY_SUGGESTION, "/#", TYPE_SINGLE_SUGGESTION);
        uriMatcher.addURI(Constants.AUTHORITY_CITY_SUGGESTION, "search_suggest_query/*", TYPE_ALL_SUGGESTION);
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Log.d(TAG, "Query uri : " + uri);
        final MatrixCursor cursor = new MatrixCursor(
                new String[]{
                        BaseColumns._ID,
                        SearchManager.SUGGEST_COLUMN_TEXT_1,
                        SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID
                }
        );

        if (uriMatcher.match(uri) == TYPE_ALL_SUGGESTION) {
            Log.d(TAG, "TYPE ALL");
            String query = uri.getLastPathSegment().toLowerCase();
            Log.d(TAG, "Query String : " + query);
            Observer<List<City>> observer = new Observer<List<City>>() {

                List<City> cityList;

                @Override
                public void onCompleted() {
                    Log.d(TAG, "onComplete : cityData size : " + cityData.size());
                    for (int i = 0; i < cityList.size(); ++i) {
                        City city = cityList.get(i);
                        cursor.addRow(new Object[]{i, city.getName() + ", " + city.getCountry(), city.get_id()});
                    }
                }

                @Override
                public void onError(Throwable e) {
                    Log.d(TAG, "Error:  " + e.getMessage());
                }

                @Override
                public void onNext(List<City> cities) {
                    Log.d(TAG, "onNext : " + cities);
                    cityList = cities;
                    cityData.clear();
                    cityData.addAll(cities);
                }
            };
            OpenWeatherMapManager.getInstance().getCityList(observer, query);
        } else if (uriMatcher.match(uri) == TYPE_SINGLE_SUGGESTION) {
            Log.d(TAG, "TYPE SINGLE : " + cityData.size());
            int cityId = Integer.parseInt(uri.getLastPathSegment());
            for (int i = 0; i < cityData.size(); i++) {
                if (cityId == cityData.get(i).get_id()) {
                    City city = cityData.get(i);
                    CitySharedPreference.addManagedCity(getContext(), city);
                    cursor.addRow(new Object[]{city.get_id(), city.getName(), city.get_id()});
                }
            }
        }
        return cursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
