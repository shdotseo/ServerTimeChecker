package io.animal.meerkat.ui.main;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.Calendar;

import io.animal.meerkat.R;
import io.animal.meerkat.eventbus.TimerEvent;
import io.animal.meerkat.services.TimerService;
import io.animal.meerkat.util.RequestHttpConnection;
import io.animal.meerkat.util.SharedPreferencesHelper;
import io.animal.meerkat.util.TimeFormatHelper;

public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment";

    private TextView tw;
    private TextView clock;
    private SwitchMaterial is24Hour;

    private FrameLayout clockContainer;

    private int clockWight;
    private int clockHeight;

    private TimeFormatHelper timeFormatHelper;

    private long currentTime;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        timeFormatHelper = new TimeFormatHelper();

        // update current time.
        currentTime = getSystemTime();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        tw = view.findViewById(R.id.test_timer);

        // default naver.com
//        NetworkTask networkTask = new NetworkTask();
//        networkTask.execute("http://naver.com");

        is24Hour = view.findViewById(R.id.hour);
        onSavePref24Hour(is24Hour.isChecked());

        is24Hour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onSavePref24Hour(isChecked);

                clock.setText(toTime(currentTime));
            }
        });


//        final FrameLayout clockContainer = view.findViewById(R.id.clock_container);
//        clockWight = clockContainer.getLayoutParams().width;
//        clockHeight = clockContainer.getLayoutParams().height;

        clock = view.findViewById(R.id.clock);

        // TODO font 적용이 되지 않음.
//        clock.setText(toTime(getSystemTime()));
//        clock.setTypeface(getLedFont());

        AppCompatSeekBar fontSizeSeekBar = view.findViewById(R.id.font_size);
        fontSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "onProgressChanged: " + progress);
                clock.setTextSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

//        AppCompatSeekBar fontMarginSeekBar = view.findViewById(R.id.margin_size);
//        fontMarginSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
////                ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) clock.getLayoutParams();
////                params.width = clockWight - progress - 70;
////                params.height= clockHeight - progress - 70;
//                clock.setPadding(progress, progress, progress, progress);
////                clock.setLayoutParams(params);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//            }
//        });

        // register EventBus
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        EventBus.getDefault().unregister(this);
    }

    private long getSystemTime() {
        return Calendar.getInstance().getTime().getTime();
    }

    private String toTime(long time) {
        if (is24Hour.isChecked()) {
            return timeFormatHelper.toDate(time);
        } else {
            return timeFormatHelper.toDate12(time);
        }
    }

    // -------------------------------------------------------------------------- Event Bus Listener

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdatingTimer(TimerEvent event) {
        currentTime = event.getTime();

        clock.setText(timeFormatHelper.toDate(currentTime));
    }

    // -------------------------------------------------------------------------- Event Bus Listener

    private Typeface getLedFont() throws Resources.NotFoundException {
        return ResourcesCompat.getFont(getContext(), R.font.font);
    }


    /// --------------------------------------------------------------------------- sharedpreference

    private void onSavePref24Hour(boolean b24Hour) {
        SharedPreferencesHelper pref = new SharedPreferencesHelper(getContext());
        if (pref != null) {
            pref.setEnable24Hour(b24Hour);
        }
    }

    /// ----------------------------------------------------------------------- sharedpreference end


//    class NetworkTask extends AsyncTask<String, Void, Long> {
//
//        private String server;
//
//        @Override
//        protected Long doInBackground(String... voids) {
//            long serverTime = 0;
//
//            if (voids == null || voids.length == 0) {
//                return serverTime;
//            }
//
//            // set server url
//            server = voids[0];
//
//            RequestHttpConnection requestHttpConnection = new RequestHttpConnection(server);
//
//            try {
//                serverTime = requestHttpConnection.getServerDate();
//            } catch (IOException e) {
//                Log.e(TAG, e.getMessage());
//            }
//
//            return serverTime;
//        }
//
//        @Override
//        protected void onPostExecute(Long date) {
//            super.onPostExecute(date);
//
//            currentTime = date;
//
//            tw.setText(server);
//
//            clock.setText(timeFormatHelper.toDate(currentTime));
//        }
//    }

}
