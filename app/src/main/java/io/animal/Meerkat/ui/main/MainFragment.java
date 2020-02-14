package io.animal.Meerkat.ui.main;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.fragment.app.Fragment;

import com.google.android.material.textview.MaterialTextView;

import java.io.IOException;

import io.animal.Meerkat.R;
import io.animal.Meerkat.util.RequestHttpConnection;

public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment";

    private TextView tw;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        tw = view.findViewById(R.id.test_timer);

        NetworkTask networkTask = new NetworkTask();
        networkTask.execute("http://naver.com");

        final MaterialTextView clock = view.findViewById(R.id.clock);

        AppCompatSeekBar fontSizeSeekBar = view.findViewById(R.id.font_size);
        fontSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                clock.setTextSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        AppCompatSeekBar fontMarginSeekBar = view.findViewById(R.id.margin_size);
//        fontMarginSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
////                 clock.setPadding(progress, progress, progress, progress);
//                 clock.setWidth(progress);
//                 clock.setHeight(progress);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
        return view;
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

            tw.setText("" + aLong);
        }
    }

}
