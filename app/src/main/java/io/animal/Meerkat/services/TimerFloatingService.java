package io.animal.Meerkat.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import io.animal.Meerkat.eventbus.FloatingServiceEvent;
import io.animal.Meerkat.eventbus.FloatingServiceStatus;
import io.animal.Meerkat.ui.floating.TimerView;
import io.animal.Meerkat.util.SharedPreferencesHelper;

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

        // post eventbus
        EventBus.getDefault().post(new FloatingServiceEvent(FloatingServiceStatus.RUNNING));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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

        // post eventbus
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
