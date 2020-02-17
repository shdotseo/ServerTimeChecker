package io.animal.Meerkat.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

import io.animal.Meerkat.eventbus.TimerEvent;

public class TimerService extends Service {

    private Timer timer;

    private TimerTask timerTask;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                EventBus.getDefault().post(new TimerEvent());
            }
        };

        timer = new Timer();
        timer.schedule(timerTask, 1000);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
