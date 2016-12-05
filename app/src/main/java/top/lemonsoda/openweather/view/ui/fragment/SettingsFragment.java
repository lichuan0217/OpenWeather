package top.lemonsoda.openweather.view.ui.fragment;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.List;

import top.lemonsoda.openweather.R;
import top.lemonsoda.openweather.domain.application.App;
import top.lemonsoda.openweather.domain.utils.CitySharedPreference;
import top.lemonsoda.openweather.domain.utils.NotificationUtils;
import top.lemonsoda.openweather.domain.utils.Utils;
import top.lemonsoda.openweather.model.entry.City;

/**
 * Created by Chuan on 7/25/16.
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    private static final String TAG = SettingsFragment.class.getCanonicalName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_weather);

        setCityListPreferenceData();

        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_units_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_notification_choose_city_key)));
        findPreference(getString(R.string.pref_show_notification_key)).setOnPreferenceChangeListener(this);
    }


    private void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(this);

        setPreferenceSummary(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    private void setPreferenceSummary(Preference preference, Object value) {
        String stringValue = value.toString();
        String key = preference.getKey();
        Log.d(TAG, "..." + key + ", " + value);
        if (key.equals(getString(R.string.pref_units_key)) || key.equals(getString(R.string.pref_notification_choose_city_key))) {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }
    }

    private void setCityListPreferenceData() {

        ListPreference lp = (ListPreference) findPreference(getString(R.string.pref_notification_choose_city_key));
        List<City> cityList = CitySharedPreference.getManagedCityList(getActivity());
        Log.d(TAG, "set city list: " + cityList.size());
        if (cityList == null || cityList.size() < 1)
            return;

        CharSequence[] entries = new CharSequence[cityList.size()];
        CharSequence[] entryValues = new CharSequence[cityList.size()];

        for (int i = 0; i < cityList.size(); i++) {
            entries[i] = cityList.get(i).getName();
            entryValues[i] = String.valueOf(cityList.get(i).get_id());
            Log.d(TAG, "value: " + entries[i] + " , " + entryValues[i]);
        }
        lp.setEntries(entries);
        lp.setEntryValues(entryValues);
        lp.setDefaultValue(String.valueOf(cityList.get(0).get_id()));
//        lp.setSummary(entries[0]);

        if (lp.getValue() == null) {
            Log.d(TAG, "test set value");
            Utils.setStatusbarCity(getActivity(), entryValues[0].toString());
        }

//        Log.d(TAG, "test : " + Utils.getStatusbarCity(getActivity()));
        Log.d(TAG, "test : value is " + lp.getValue());
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        Log.d(TAG, "Preference Changed: " + preference.getKey());
        String key = preference.getKey();
        if (key.equals(getString(R.string.pref_units_key))) {
            setPreferenceSummary(preference, value);
        } else if (key.equals(getString(R.string.pref_notification_choose_city_key))) {
            setPreferenceSummary(preference, value);
            NotificationUtils.getInstance().updateNotification(
                    getActivity(), Integer.parseInt(value.toString()));
        } else if (key.equals(getString(R.string.pref_show_notification_key))) {
            Boolean isShowNotification = (Boolean) value;
            if (isShowNotification) {
                Log.d(TAG, "Show notification");
                ListPreference listPreference = (ListPreference) findPreference(getString(R.string.pref_notification_choose_city_key));
                Log.d(TAG, "Get listPre value: " + listPreference.getValue());
                int cityId = Integer.parseInt(listPreference.getValue());
                NotificationUtils.getInstance().createNotification(getActivity(), cityId);
            } else {
                Log.d(TAG, "Do not show notification");
                NotificationUtils.getInstance().cancelNotification(getActivity());
            }
        }
        return true;
    }
}
