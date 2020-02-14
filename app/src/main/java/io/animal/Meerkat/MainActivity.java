package io.animal.Meerkat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.animal.Meerkat.eventbus.FloatingServiceEvent;
import io.animal.Meerkat.eventbus.FloatingServiceStatus;
import io.animal.Meerkat.services.TimerFloatingService;
import io.animal.Meerkat.ui.main.MainFragment;
import io.animal.Meerkat.util.PermissionHelper;
import io.animal.Meerkat.util.SharedPreferencesHelper;

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

                if (!isClockFloating) {
                    // show floating view
                    startClockService();
                } else {
                    // show admob
                    showInterstitial();
                }
            }
        });

        initializeAdmob();
    }

    @Override
    protected void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        onLoadPrefFloatingStatus();
        updateFloatingIcon();
    }

    @Override
    protected void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    //------------------------------------------------------------------------------ FloatingService

    private void startClockService() {
        startService(getClockServiceIntent());
    }

    private void stopClockService() {
        stopService(getClockServiceIntent());
    }

    private Intent getClockServiceIntent() {
        return new Intent(getApplicationContext(), TimerFloatingService.class);
    }

    //------------------------------------------------------------------------------ FloatingService

    //---------------------------------------------------------------------------------------- admob

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

    //---------------------------------------------------------------------------------------- admob

    //------------------------------------------------------------------------------ floating button

    private void updateFloatingIcon() {
        if (isClockFloating) {
            // now floating status. so floating icon is close
            floatingActionButton.setImageResource(R.drawable.ic_close_white_24dp);
        } else {
            // not floating status. so floating icon is add
            floatingActionButton.setImageResource(R.drawable.ic_add_to_queue_white_24dp);
        }
    }

    //------------------------------------------------------------------------------ floating button

    //----------------------------------------------------------------------------------- SharedPref

    private void onSavePrefFloatingStatus(boolean status) {
        SharedPreferencesHelper pref = new SharedPreferencesHelper(this);
        if (pref != null) {
            pref.setFloatingState(status);
        }
    }

    private void onLoadPrefFloatingStatus() {
        SharedPreferencesHelper pref = new SharedPreferencesHelper(this);
        if (pref != null) {
            isClockFloating = pref.getFloatingState();
        } else {
            isClockFloating = false;
        }
    }

    //----------------------------------------------------------------------------------- SharedPref

    // --------------------------------------------------------------------------- EventBus Listener

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFloatingServiceEvent(FloatingServiceEvent event) {
        Log.d(TAG, "onChangeFloatingStatus: " + event.toString());
        if (event.getStatus() == FloatingServiceStatus.RUNNING) {
            isClockFloating = true;
        } else {
            isClockFloating = false;
        }

        updateFloatingIcon();
    }

    // --------------------------------------------------------------------------- EventBus Listener
}
