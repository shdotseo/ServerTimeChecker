package io.animal.meerkat.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.provider.Settings;

public class PermissionHelper extends ContextWrapper {

    public PermissionHelper(Context c) {
        super(c);
    }

    public boolean hasSystemAlertWindows() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (!Settings.canDrawOverlays(getApplicationContext())) {
            return false;
        }
        return true;
    }
}
