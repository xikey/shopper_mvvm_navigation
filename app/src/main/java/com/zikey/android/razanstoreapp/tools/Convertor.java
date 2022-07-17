//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zikey.android.razanstoreapp.tools;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Convertor {
    public Convertor() {
    }

    public static int toPixcel(float dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / 160.0F));
        return px;
    }

    public static float toDp(int px, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round((float)px / (displayMetrics.xdpi / 160.0F));
        return (float)dp;
    }

    public static Date toDate(String dateStr) {
        if (TextUtils.isEmpty(dateStr)) {
            return null;
        } else {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                return dateFormat.parse(dateStr);
            } catch (Throwable var2) {
                return null;
            }
        }
    }

    public static String toString(Date date) {
        if (date == null) {
            return null;
        } else {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                return dateFormat.format(date);
            } catch (Throwable var2) {
                return null;
            }
        }
    }
}
