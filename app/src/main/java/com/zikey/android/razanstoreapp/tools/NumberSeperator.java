package com.zikey.android.razanstoreapp.tools;

import java.text.DecimalFormat;

/**
 * Created by Zikey on 15/07/2017.
 */

public class NumberSeperator {


    public static String separate(double num) {

        DecimalFormat formatter = new DecimalFormat("###,###,###.###");
        return formatter.format(num);
    }

    public static String separate(float num) {

        DecimalFormat formatter = new DecimalFormat("###,###,###.###");
        return formatter.format(num);
    }

    public static String separate(long num) {

        DecimalFormat formatter = new DecimalFormat("###,###,###.###");
        return formatter.format(num);
    }

    public static String removeDotZero(double num) {

        DecimalFormat formatter = new DecimalFormat("###.#");
        return formatter.format(num);
    }

    public static double just2decimal(double num) {

        DecimalFormat formatter = new DecimalFormat(".##");
        return Double.parseDouble(formatter.format(num));
    }

    public static String separateAndRound(double num) {

        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(num);
    }

}
