package io.animal.Meerkat.ui.setting;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import io.animal.Meerkat.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.main_preferences, rootKey);
    }
}
