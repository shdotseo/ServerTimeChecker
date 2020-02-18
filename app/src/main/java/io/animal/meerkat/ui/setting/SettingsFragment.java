package io.animal.meerkat.ui.setting;

import android.os.Bundle;
import android.util.Log;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SeekBarPreference;

import io.animal.meerkat.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    private final static String TAG = "SettingsFragment";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.main_preferences, rootKey);

        SeekBarPreference fontSize = findPreference("font_size");
        if (fontSize != null) {
            fontSize.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Log.d(TAG, newValue.toString());
                    return false;
                }
            });
        }
    }
}
