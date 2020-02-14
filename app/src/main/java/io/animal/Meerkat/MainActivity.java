package io.animal.Meerkat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.animal.Meerkat.services.TimerFloatingService;
import io.animal.Meerkat.ui.main.MainFragment;
import io.animal.Meerkat.util.PermissionHelper;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    private final static int REQ_CODE_OVERLAY_PERMISSION = 101;

    private FloatingActionButton floatingActionButton;

    private boolean isClockFloating;

    // admob
    private InterstitialAd mInterstitialAd;

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

        // bind UI resource
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check floating permission
                PermissionHelper permissionHelper = new PermissionHelper(getApplicationContext());
                if (!permissionHelper.hasSystemAlertWindows()) {
                    showAlertPermission();
                    return;
                }

                // show floating view
                if (!isClockFloating) {
                    startClockService();
                } else {
                    // show admob view.
                    showInterstitial();
                }
            }
        });

        // init admob.
        initializeAdmob();
    }


    private void startClockService() {
        isClockFloating = !isClockFloating;

        // show floating view.
//        if (Build.VERSION.SDK_INT >= 26) {
//            startForegroundService(getClockServiceIntent());
//        } else {
            startService(getClockServiceIntent());
//        }
    }

    private void stopClockService() {
        stopService(getClockServiceIntent());
        isClockFloating = !isClockFloating;
    }

    private Intent getClockServiceIntent() {
        return new Intent(getApplicationContext(), TimerFloatingService.class);
    }

    private void showAlertPermission() {
        new MaterialAlertDialogBuilder(this)
                .setMessage("서버시계를 화면에 띄우기 위해서는 '플로팅' 권한이 필요합니다.")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + getApplicationContext().getPackageName()));
                        startActivityForResult(intent, REQ_CODE_OVERLAY_PERMISSION);
                    }
                })
                .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // admob

    private void showInterstitial() {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    private void initializeAdmob() {
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

        // Create the InterstitialAd and set the adUnitId.
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.banner_ad_unit_id_for_full_test));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.d(TAG, "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.d(TAG, "onAdFailedToLoad");
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                Log.d(TAG, "onAdOpended");
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                Log.d(TAG, "onAdClicked");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.d(TAG, "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                Log.d(TAG, "onAdClosed");

                // load next ad.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());

                // stopClock
                stopClockService();
            }
        });
    }
}
