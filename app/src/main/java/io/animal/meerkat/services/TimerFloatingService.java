package io.animal.meerkat.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.android.material.textview.MaterialTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Optional;

import io.animal.meerkat.eventbus.FloatingServiceEvent;
import io.animal.meerkat.eventbus.FloatingServiceStatus;
import io.animal.meerkat.eventbus.TimerEvent;
import io.animal.meerkat.ui.floating.TimerView;
import io.animal.meerkat.util.SharedPreferencesHelper;

public class TimerFloatingService extends Service {

    private final static String TAG = "TimerFloatingService";

    private TimerView timerView;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        timerView = new TimerView(getApplicationContext());
        timerView.updateParamsForLocation();

        onUpdateFloatingStautsonPref(true);

        // post event bus
        EventBus.getDefault().post(new FloatingServiceEvent(FloatingServiceStatus.RUNNING));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

        if (timerView != null) {
            timerView.removeView();
        }

        onUpdateFloatingStautsonPref(false);

        // post event bus
        EventBus.getDefault().post(new FloatingServiceEvent(FloatingServiceStatus.STOP));
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void onUpdateFloatingStautsonPref(boolean status) {
        SharedPreferencesHelper pref = new SharedPreferencesHelper(getApplicationContext());
        if (pref != null) {
            pref.setFloatingState(status);
        }
    }

}
