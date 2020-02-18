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

import com.google.android.material.textview.MaterialTextView;

import io.animal.meerkat.R;

public class TimerView extends ContextWrapper {

    private final static String TAG = "TimerView";

    private View arenaView;

    private WindowManager.LayoutParams params;

    public TimerView(Context c) {
        super(c);

        LayoutInflater inflate = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        arenaView = inflate.inflate(R.layout.timer_view_land, null);

        MaterialTextView tw = arenaView.findViewById(R.id.clock);
        tw.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "onTouch");
                return false;
            }
        });
        params = createTouchViewParams();
    }

    public void updateParamsForLocation() {
        getWindowManager().addView(arenaView, params);
    }

    public void removeView() {
        getWindowManager().removeView(arenaView);
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
}
