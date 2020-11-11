package com.yanyi.fire.util;

import android.annotation.SuppressLint;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import java.lang.ref.WeakReference;


/**
 * Toast 辅助类
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public final class ToastUtils {

    private static WeakReference<Toast> sToastRef;

    private ToastUtils() {
    }

    /**
     * Show the sToast for a short period of time.
     *
     * @param text The text.
     */
    public static void showShort(@NonNull final CharSequence text) {
        show(text, Toast.LENGTH_SHORT);
    }

    /**
     * Show the sToast for a short period of time.
     *
     * @param resId The resource id for text.
     */
    public static void showShort(@StringRes final int resId) {
        show(resId, Toast.LENGTH_SHORT);
    }

    /**
     * Show the sToast for a short period of time.
     *
     * @param resId The resource id for text.
     * @param args  The args.
     */
    public static void showShort(@StringRes final int resId, final Object... args) {
        if (args != null && args.length == 0) {
            show(resId, Toast.LENGTH_SHORT);
        } else {
            show(resId, Toast.LENGTH_SHORT, args);
        }
    }

    /**
     * Show the sToast for a short period of time.
     *
     * @param format The format.
     * @param args   The args.
     */
    public static void showShort(final String format, final Object... args) {
        if (args != null && args.length == 0) {
            show(format, Toast.LENGTH_SHORT);
        } else {
            show(format, Toast.LENGTH_SHORT, args);
        }
    }

    /**
     * Show the sToast for a long period of time.
     *
     * @param text The text.
     */
    public static void showLong(@NonNull final CharSequence text) {
        show(text, Toast.LENGTH_LONG);
    }

    /**
     * Show the sToast for a long period of time.
     *
     * @param resId The resource id for text.
     */
    public static void showLong(@StringRes final int resId) {
        show(resId, Toast.LENGTH_LONG);
    }

    /**
     * Show the sToast for a long period of time.
     *
     * @param resId The resource id for text.
     * @param args  The args.
     */
    public static void showLong(@StringRes final int resId, final Object... args) {
        if (args != null && args.length == 0) {
            show(resId, Toast.LENGTH_SHORT);
        } else {
            show(resId, Toast.LENGTH_LONG, args);
        }
    }

    /**
     * Show the sToast for a long period of time.
     *
     * @param format The format.
     * @param args   The args.
     */
    public static void showLong(final String format, final Object... args) {
        if (args != null && args.length == 0) {
            show(format, Toast.LENGTH_SHORT);
        } else {
            show(format, Toast.LENGTH_LONG, args);
        }
    }

    /**
     * Cancel the sToast.
     */
    public static void cancel() {
        if (sToastRef != null && sToastRef.get() != null) {
            sToastRef.get().cancel();
            sToastRef.clear();
        }
    }

    private static void show(@StringRes final int resId, final int duration) {
        show(MyApplication.getAppContext().getResources().getText(resId).toString(), duration);
    }

    private static void show(@StringRes final int resId, final int duration, final Object... args) {
        show(String.format(MyApplication.getAppContext().getResources().getString(resId), args), duration);
    }

    private static void show(final String format, final int duration, final Object... args) {
        show(String.format(format, args), duration);
    }

    @SuppressLint("ShowToast")
    private static void show(final CharSequence text, final int duration) {
        cancel();
        if (sToastRef == null || sToastRef.get() == null) {
            sToastRef = new WeakReference<>(Toast.makeText(MyApplication.getAppContext(), text, duration));
        }
        Toast toast = sToastRef.get();
        toast.setText(text);
        toast.setDuration(duration);
        toast.show();
    }

}
