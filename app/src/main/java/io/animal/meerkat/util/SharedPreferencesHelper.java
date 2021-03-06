package io.animal.meerkat.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;

public class SharedPreferencesHelper extends ContextWrapper {

    public final static String KEY_FLOATING_SERVICE_STATE = "floating_service_state";
    public final static String KEY_FLOATING_CLOCK_ENABLE = "floating_clock_enable";

    private final static String KEY_24_HOUR = "24_hour";

    private final static String PREF_NAME = "pref";

    private SharedPreferences pref;

    /**
     * Construction.
     *
     * @param context
     */
    public SharedPreferencesHelper(Context context) {
        super(context);
    }

    public void setFloatingState(boolean state) {
        getPreferences().edit().putBoolean(KEY_FLOATING_SERVICE_STATE, state).apply();
    }

    public boolean getFloatingState() {
        return getPreferences().getBoolean(KEY_FLOATING_SERVICE_STATE, false);
    }

    public void setFloatingEnable(boolean state) {
        getPreferences().edit().putBoolean(KEY_FLOATING_CLOCK_ENABLE, state).apply();
    }

    public boolean isFloatingClock() {
        return getPreferences().getBoolean(KEY_FLOATING_CLOCK_ENABLE, false);
    }

    public void setEnable24Hour(boolean is24) {
        getPreferences().edit().putBoolean(KEY_24_HOUR, is24).apply();
    }

    public boolean checked24Hour() {
        return getPreferences().getBoolean(KEY_24_HOUR, false);
    }

    public void registerChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        getPreferences().registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        getPreferences().unregisterOnSharedPreferenceChangeListener(listener);
    }

    private SharedPreferences getPreferences() {
        if (pref == null) {
            pref = getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        }
        return pref;
    }

}
