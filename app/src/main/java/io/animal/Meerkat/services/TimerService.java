package io.animal.Meerkat.services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import io.animal.Meerkat.eventbus.TimerEvent;
import io.animal.Meerkat.notification.NotificationHelper;

public class TimerService extends Service {

    private Timer timer;

    private TimerTask timerTask;

    private NotificationHelper notificationHelper;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationHelper = new NotificationHelper(this);

        timerTask = new TimerTask() {
            @Override
            public void run() {
                EventBus.getDefault().post(new TimerEvent());
            }
        };

        timer = new Timer();
        timer.schedule(timerTask, Calendar.getInstance().getTime(),1000);

        startForegroundService();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (timer != null) {
            timer.cancel();
        }
    }

    private void startForegroundService() {
        final int CHANNEL_ID = 1001;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            NotificationCompat.Builder builder = notificationHelper.getForegroundNotification();

            startForeground(CHANNEL_ID, builder.build());
        } else {
            startForeground(CHANNEL_ID, new Notification());
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
