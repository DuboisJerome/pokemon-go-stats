package com.pokemongostats.view.utils;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.FragmentActivity;

/**
 * @author Zapagon
 */
public final class KeyboardUtils {

    private final static String KEYBOARD_TAG = "KEYBOARD";

    private KeyboardUtils() {
    }

    public static void initKeyboard(final Activity a) {
        try {
            if (a == null) {
                return;
            }
            Window w = a.getWindow();
            if (w == null) {
                return;
            }
            w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } catch (Exception e) {
            Log.e(KEYBOARD_TAG, "Problem when setting softinputmode for keyboard", e);
        }
    }

    public static void hideKeyboard(final Activity a) {
        try {
            if (a == null) {
                return;
            }
            View focus = a.getCurrentFocus();
            if (focus == null) {
                return;
            }
            InputMethodManager in = (InputMethodManager) a.getSystemService(FragmentActivity.INPUT_METHOD_SERVICE);
            assert in != null;
            in.hideSoftInputFromWindow(focus.getWindowToken(), 0);
        } catch (Exception e) {
            Log.e(KEYBOARD_TAG, "Problem when hidding keyboard", e);
        }
    }
}
