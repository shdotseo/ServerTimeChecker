package io.animal.Meerkat.ui.main;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
