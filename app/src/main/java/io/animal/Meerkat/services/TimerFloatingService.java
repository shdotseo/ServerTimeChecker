package io.animal.Meerkat.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import io.animal.Meerkat.ui.floating.TimerView;

public class TimerFloatingService extends Service {

    private final static String TAG = "TimerFloatingService";

    private TimerView timerView;

    @Override
    public void onCreate() {
        super.onCreate();

        timerView = new TimerView(getApplicationContext());
        timerView.updateParamsForLocation();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (timerView != null) {
            timerView.removeView();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
