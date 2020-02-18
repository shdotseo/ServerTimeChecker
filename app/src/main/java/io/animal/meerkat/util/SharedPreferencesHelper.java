package io.animal.meerkat.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;

public class SharedPreferencesHelper extends ContextWrapper {

    public final static String KEY_FLOATING_SERVICE_STATE = "floating_service_state";

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
