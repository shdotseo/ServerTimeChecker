package io.animal.Meerkat.ui.main;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
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
import com.google.android.material.textview.MaterialTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.Calendar;

import io.animal.Meerkat.R;
import io.animal.Meerkat.eventbus.TimerEvent;
import io.animal.Meerkat.services.TimerService;
import io.animal.Meerkat.util.RequestHttpConnection;
import io.animal.Meerkat.util.TimeFormatHelper;

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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        tw = view.findViewById(R.id.test_timer);

        // default naver.com
        NetworkTask networkTask = new NetworkTask();
        networkTask.execute("http://naver.com");

        is24Hour = view.findViewById(R.id.hour);
        is24Hour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                clock.setText(toTime(getSystemTime()));
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

        getContext().startService(new Intent(getContext(), TimerService.class));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getContext().stopService(new Intent(getContext(), TimerService.class));

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

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdatingTimer(TimerEvent event) {
        currentTime += 1000;
        clock.setText(timeFormatHelper.toDate(currentTime));
    }

    private Typeface getLedFont() throws Resources.NotFoundException {
        return ResourcesCompat.getFont(getContext(), R.font.font);
    }

    class NetworkTask extends AsyncTask<String, Void, Long> {

        @Override
        protected Long doInBackground(String... voids) {
            RequestHttpConnection requestHttpConnection = new RequestHttpConnection("http://naver.com");

            long serverTime = 0;
            try {
                serverTime = requestHttpConnection.getServerDate();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }

            return serverTime;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);

            currentTime = aLong;

            tw.setText("naver");

            clock.setText(timeFormatHelper.toDate(currentTime));
        }
    }

}
