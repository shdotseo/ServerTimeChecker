package io.animal.Meerkat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.animal.Meerkat.ui.main.MainFragment;
import io.animal.Meerkat.util.PermissionHelper;

public class MainActivity extends AppCompatActivity {

    private final static int REQ_CODE_OVERLAY_PERMISSION = 101;

    private FloatingActionButton floatingActionButton;

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

            }
        });
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

    private void enableClockFloatingView()  {

    }
}
