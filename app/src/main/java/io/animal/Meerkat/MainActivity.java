package io.animal.Meerkat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import io.animal.Meerkat.ui.main.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, new MainFragment())
                    .commitNow();
        }
    }
}
