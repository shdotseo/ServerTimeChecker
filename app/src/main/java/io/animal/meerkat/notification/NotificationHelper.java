package io.animal.meerkat.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import io.animal.meerkat.R;

/**
 * Notification Helper Class.
 */
public class NotificationHelper extends ContextWrapper {

    public static final String ROOT_CHANNEL_ID = "io.animal.meerkat.root";
    public static final String ROOT_CHANNEL = "io.animal.meerkat.root";

    private NotificationManager manager;

    public NotificationHelper(Context ctx) {
        super(ctx);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chan0 = new NotificationChannel(ROOT_CHANNEL_ID,
                    ROOT_CHANNEL, NotificationManager.IMPORTANCE_LOW);
            getManager().createNotificationChannel(chan0);
        }
    }

    /**
     * Get a notification for foreground
     *
     * @return the builder
     */
    public NotificationCompat.Builder getForegroundNotification() {
        return new NotificationCompat.Builder(getApplicationContext(), ROOT_CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("");
    }

    /**
     * Send a notification.
     *
     * @param id The ID of the notification
     * @param notification The notification object
     */
    public void notify(int id, NotificationCompat.Builder notification) {
        getManager().notify(id, notification.build());
    }

    /**
     * Request Canceling notification.
     *
     * @param id Notification Id for cancel
     */
    public void cancel(int id) {
        getManager().cancel(id);
    }

    /**
     * Get the small icon for this app
     *
     * @return The small icon resource id
     */
    private int getSmallIcon() {
        return R.drawable.ic_schedule_gray_24dp;
    }

    /**
     * Get the notification manager.
     *
     * Utility method as this helper works with it a lot.
     *
     * @return The system service NotificationManager
     */
    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }
}
