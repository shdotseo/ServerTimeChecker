package io.animal.meerkat.ui.floating;

import android.content.Context;
import android.view.View;

public class ClockView {

    private View view;

    public ClockView(View view) {
        this.view = view;
    }

    public void changeAlpha() {
        if (view.getAlpha() == 1.0) {
            view.setAlpha(0.5f);
        } else {
            view.setAlpha(1.0f);
        }
    }
}
