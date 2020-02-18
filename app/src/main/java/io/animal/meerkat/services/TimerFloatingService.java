package io.animal.meerkat.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.IBinder;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import io.animal.meerkat.eventbus.FloatingServiceEvent;
import io.animal.meerkat.eventbus.FloatingServiceStatus;
import io.animal.meerkat.ui.floating.TimerView;
import io.animal.meerkat.util.SharedPreferencesHelper;

public class TimerFloatingService extends Service {

    private final static String TAG = "TimerFloatingService";

    private static final String BCAST_CONFIGCHANGED = "android.intent.action.CONFIGURATION_CHANGED";

    private TimerView timerView;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        timerView = new TimerView(getApplicationContext());
        timerView.updateParamsForLocation();

        onUpdateFloatingStatusonPref(true);

//        IntentFilter filter = new IntentFilter();
//        filter.addAction(BCAST_CONFIGCHANGED);
//        registerReceiver(mBroadcastReceiver, filter);

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

        onUpdateFloatingStatusonPref(false);

        // post event bus
        EventBus.getDefault().post(new FloatingServiceEvent(FloatingServiceStatus.STOP));

//        if (mBroadcastReceiver != null) {
//            unregisterReceiver(mBroadcastReceiver);
//        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

//    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent myIntent) {
//
//            if (myIntent.getAction().equals( BCAST_CONFIGCHANGED ) ) {
//
//                Log.d(TAG, "received->" + BCAST_CONFIGCHANGED);
//
//
//                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
//                    // it's Landscape
//                    Log.d(TAG, "LANDSCAPE");
//                }
//                else {
//                    Log.d(TAG, "PORTRAIT");
//                }
//            }
//        }
//    };

    private void onUpdateFloatingStatusonPref(boolean status) {
        SharedPreferencesHelper pref = new SharedPreferencesHelper(getApplicationContext());
        if (pref != null) {
            pref.setFloatingState(status);
        }
    }

}
