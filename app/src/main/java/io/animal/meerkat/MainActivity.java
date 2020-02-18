package io.animal.meerkat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.animal.meerkat.eventbus.FloatingServiceEvent;
import io.animal.meerkat.eventbus.FloatingServiceStatus;
import io.animal.meerkat.services.TimerFloatingService;
import io.animal.meerkat.services.TimerService;
import io.animal.meerkat.ui.bottom.ServerSheetsFragment;
import io.animal.meerkat.ui.main.MainFragment;
import io.animal.meerkat.util.PermissionHelper;
import io.animal.meerkat.util.SharedPreferencesHelper;

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
                    startClockViewService();
                } else {
                    // show admob
                    showInterstitial();
                }
            }
        });
        BottomAppBar bottomAppBar = findViewById(R.id.bottomAppBar);
        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerSheetsFragment bottomSheet = new ServerSheetsFragment();
                bottomSheet.show(getSupportFragmentManager(), "S");
            }
        });

        initializeAdmob();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!isFloatingLaunchingService(this)) {
            startClockService();
        }

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

        if (isFloatingLaunchingService(this)) {
            stopClockService();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
        }
        return super.onOptionsItemSelected(item);
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

    private void stopClockService() {
        stopService(getClockViewServiceIntent());

        stopService(getClockServiceIntent());
    }

    ///-------------------------------------------------------------------------------- TimerService

    private void startClockService() {
        if (!isTimerServiceLaunching(this)) {
            startService(getClockServiceIntent());
        }
    }

    private Intent getClockServiceIntent() {
        Intent i = new Intent(getApplicationContext(), TimerService.class);
        i.putExtra(TimerService.TIME_SERVER_URL, "http://naver.com");
        return i;
    }

    ///---------------------------------------------------------------------------- TimerService end

    ///----------------------------------------------------------------------------- FloatingService

    private void startClockViewService() {
        startService(getClockViewServiceIntent());
    }

    private Intent getClockViewServiceIntent() {
        return new Intent(getApplicationContext(), TimerFloatingService.class);
    }

    ///------------------------------------------------------------------------- FloatingService end

    ///--------------------------------------------------------------------------------------- admob

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

    ///----------------------------------------------------------------------------------- admob end

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

    ///------------------------------------------------------------------------- floating button end

    ///----------------------------------------------------------------------------------- SharedPref

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

    ///------------------------------------------------------------------------------ SharedPref end

    ///--------------------------------------------------------------------------- EventBus Listener

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

    ///----------------------------------------------------------------------- EventBus Listener end

    ///------------------------------------------------------------------------------ private method

    private boolean isFloatingLaunchingService(Context mContext) {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (TimerFloatingService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return  false;
    }

    private boolean isTimerServiceLaunching(Context c) {
        ActivityManager manager = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (TimerService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return  false;
    }
}
