package io.animal.meerkat.services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import io.animal.meerkat.eventbus.TimerEvent;
import io.animal.meerkat.notification.NotificationHelper;
import io.animal.meerkat.util.RequestHttpConnection;

public class TimerService extends Service {

    public final static int EVENT_PERIOD = 1000;
    public final static String TIME_SERVER_URL = "TIME_SERVER_URL";

    private final static String TAG = "TimeService";

    private Timer timer;

    private TimerTask timerTask;

    private NotificationHelper notificationHelper;

    private long time;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationHelper = new NotificationHelper(this);

        // start foreground
        startForegroundService();

        // get server time
        String url = intent.getStringExtra(TIME_SERVER_URL);
        if (url != null) {
            NetworkTask networkTask = new NetworkTask();
            networkTask.execute(url);
        }

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

    // ----------------------------------------------------------------------- AsyncTask for Network

    class NetworkTask extends AsyncTask<String, Void, Long> {

        private String server;

        @Override
        protected Long doInBackground(String... voids) {
            long serverTime = 0;

            if (voids == null || voids.length == 0) {
                return serverTime;
            }

            // set server url
            server = voids[0];

            RequestHttpConnection requestHttpConnection = new RequestHttpConnection(server);

            try {
                serverTime = requestHttpConnection.getServerDate();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }

            return serverTime;
        }

        @Override
        protected void onPostExecute(Long date) {
            super.onPostExecute(date);

            time = date;

            timerTask = new TimerTask() {
                @Override
                public void run() {
                    time += EVENT_PERIOD;
                    EventBus.getDefault().post(new TimerEvent(time));
                }
            };

            timer = new Timer();
            timer.schedule(timerTask, Calendar.getInstance().getTime(), EVENT_PERIOD);
        }
    }

    // ----------------------------------------------------------------------- AsyncTask for Network
}
