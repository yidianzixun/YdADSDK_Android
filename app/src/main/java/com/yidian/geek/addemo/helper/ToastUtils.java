package com.yidian.geek.addemo.helper;

import android.content.Context;
import android.widget.Toast;
import com.yidian.newssdk.utils.ThreadUtils;
import java.lang.ref.WeakReference;

/**
 * Created by chenyichang on 2017/11/24.
 */

public final class ToastUtils {

    private static WeakReference<Toast> sToastRef;

    private ToastUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void showShort(Context context, CharSequence message) {
        showToast(context, message, Toast.LENGTH_SHORT);
    }

    public static void showShort(Context context, int message) {
        showToast(context, context.getString(message), Toast.LENGTH_SHORT);
    }

    public static void showLong(Context context, CharSequence message) {
        showToast(context, message, Toast.LENGTH_LONG);
    }

    public static void showLong(Context context, int message) {
        showToast(context, context.getString(message), Toast.LENGTH_LONG);
    }

    public static void show(Context context, CharSequence message, int duration) {
        showToast(context, message, duration);
    }

    public static void show(Context context, int message, int duration) {
        showToast(context, context.getString(message), duration);
    }

    private static void hideToast() {
        if (sToastRef != null) {
            Toast previousToast = sToastRef.get();
            if (previousToast != null) {
                previousToast.cancel();
            }
        }
    }

    private static void showToast(final Context context, final CharSequence msg, final int duration) {
        ThreadUtils.post2UI(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(context.getApplicationContext(), msg, duration);
                hideToast();
                toast.show();
                sToastRef = new WeakReference<>(toast);
            }
        });
    }

    public static void showToast(final Toast toast) {
        ThreadUtils.post2UI(new Runnable() {
            @Override
            public void run() {
                hideToast();
                toast.show();
                sToastRef = new WeakReference<>(toast);
            }
        });
    }
}
