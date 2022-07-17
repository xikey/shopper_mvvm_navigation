package com.zikey.android.razanstoreapp.tools;

/**
 * Created by Zikey on 09/08/2017.
 */

public class PersianDateConverter {

    /**
     *convert input Date from PersianCalendar Library to wellFormed date like '1396/04/02'
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
     * @return
     */
    public static String toPersianFormat(int year, int monthOfYear, int dayOfMonth) {

        String month;
        String day;
        if (monthOfYear < 9) month = "0" + (monthOfYear + 1);
        else month = "" + (monthOfYear + 1);
        if (dayOfMonth <= 9) day = "0" + (dayOfMonth);
        else day = "" + (dayOfMonth);
        return (year + "/" + month + "/" + day);

    }
}
