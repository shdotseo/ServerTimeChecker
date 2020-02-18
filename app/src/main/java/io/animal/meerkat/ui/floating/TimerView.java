package io.animal.meerkat.ui.floating;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.google.android.material.textview.MaterialTextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.animal.meerkat.R;
import io.animal.meerkat.eventbus.TimerEvent;

public class TimerView extends ContextWrapper {

    private final static String TAG = "TimerView";

    private ClockView clockView;

    private View view;

    private WindowManager.LayoutParams params;

    private MaterialTextView clock;

    public TimerView(Context c) {
        super(c);

        LayoutInflater inflate = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        view = inflate.inflate(R.layout.timer_view_land, null);

        clockView = new ClockView(view);

        clock = view.findViewById(R.id.clock);
        clock.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "onTouch");
                clockView.changeAlpha();
                return false;
            }
        });
        params = createTouchViewParams();
    }

    public void updateParamsForLocation() {
        getWindowManager().addView(view, params);
    }

    public void removeView() {
        getWindowManager().removeView(view);
    }

    public void updateClock(@NonNull String time) {
        clock.setText(time);
    }

    private WindowManager _windowManager;

    private WindowManager getWindowManager() {
        if (_windowManager == null) {
            _windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
        }
        return _windowManager;
    }

    private WindowManager.LayoutParams createTouchViewParams() {
        WindowManager.LayoutParams params;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                            | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                            | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        } else {
            params = new WindowManager.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                            | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                            | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 0;
        return params;
    }


    // ------------------------------------------------------------------------------------ EventBus

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdatingTimer(TimerEvent event) {
//        currentTime += TimerService.EVENT_PERIOD;
//        timerView.updateClock(timeFormatHelper.toDate(currentTime));
        clock.setText("");
    }

    // ------------------------------------------------------------------------------------ EventBus
}
