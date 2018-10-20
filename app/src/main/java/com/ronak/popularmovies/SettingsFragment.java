package com.ronak.popularmovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if (null != preference) {
            String value = sharedPreferences.getString(preference.getKey(),"");
            setSummary(preference,value);
        }
    }

    private void setSummary(Preference preference, String value) {
        ListPreference listPreference = (ListPreference) preference;
        int preferenceIndex = listPreference.findIndexOfValue(value);
        if (preferenceIndex >= 0){
            listPreference.setSummary(listPreference.getEntries()[preferenceIndex]);
        }
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
    addPreferencesFromResource(R.xml.settingsoptions);
    SharedPreferences  sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        Preference preference = preferenceScreen.getPreference(0);
        String value = sharedPreferences.getString(preference.getKey(),"");
        setSummary(preference,value);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
