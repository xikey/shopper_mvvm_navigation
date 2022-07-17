package com.zikey.android.razanstoreapp.tools

import android.os.SystemClock
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.Date;

class CalendarWrapper {


    fun getCurrentPersianDate(): String? {
        val calendar = PersianCalendar()
        val year: Int = calendar.getPersianYear()
        val monthOfYear: Int = calendar.getPersianMonth()
        val dayOfMonth: Int = calendar.getPersianDay()
        return toPersianFormat(year, monthOfYear, dayOfMonth)
    }

    fun getFirstDayOfThisYear(): String? {
        val calendar = PersianCalendar()
        val year: Int = calendar.getPersianYear()
        val monthOfYear: Int = calendar.getPersianMonth()
        val dayOfMonth: Int = calendar.getPersianDay()
        var temp = toPersianFormat(year, monthOfYear, dayOfMonth)
        temp = temp?.substring(0, 5)
        temp += "01/01"
        return temp
    }


    fun getFirstDayOfThisMonth(): String? {
        val calendar = PersianCalendar()
        val year: Int = calendar.getPersianYear()
        val monthOfYear: Int = calendar.getPersianMonth()
        val month: String
        month = if (monthOfYear < 9) "0" + (monthOfYear + 1) else "" + (monthOfYear + 1)
        return "$year/$month/01"
    }

    fun getCurrentTime(): String? {
        return try {
            val c: Calendar = Calendar.getInstance()
            val minutes: Int = c.get(Calendar.MINUTE)
            val hour: Int = c.get(Calendar.HOUR_OF_DAY)
            var min = minutes.toString()
            var h = hour.toString()
            if (minutes < 10) {
                min = "0$min"
            }
            if (hour < 10) {
                h = "0$h"
            }
            "$h:$min"
        } catch (e: Exception) {
            ""
        }
    }

    fun getCurrentTime_Second(): String? {
        return try {
            val c: Calendar = Calendar.getInstance()
            val minutes: Int = c.get(Calendar.MINUTE)
            val hour: Int = c.get(Calendar.HOUR_OF_DAY)
            val secound: Int = c.get(Calendar.SECOND)
            var min = minutes.toString()
            var h = hour.toString()
            var s = secound.toString()
            if (minutes < 10) {
                min = "0$min"
            }
            if (hour < 10) {
                h = "0$h"
            }
            if (secound < 10) {
                s = "0$s"
            }
            "$h:$min:$s"
        } catch (e: Exception) {
            ""
        }
    }


    fun getCurrentLongTime(): Long {
        return try {
            val c: Calendar = Calendar.getInstance()
            c.getTimeInMillis()
        } catch (e: Exception) {
            0
        }
    }

    fun getTimeFromBoot(): Long {
        return SystemClock.elapsedRealtime()
    }

    fun millisecondsToMinutes(miliSecounds: Long): Long {
        return TimeUnit.MILLISECONDS.toMinutes(miliSecounds)
    }


    fun millisecondsToDate(millis: Long): Date? {
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis(millis)
        return calendar.getTime()
    }

    companion object {

        /**
         * convert input Date from PersianCalendar Library to wellFormed date like '1396/04/02'
         * @param year
         * @param monthOfYear
         * @param dayOfMonth
         * @return
         */
        fun toPersianFormat(year: Int, monthOfYear: Int, dayOfMonth: Int): String? {
            val month: String
            val day: String
            month = if (monthOfYear < 9) "0" + (monthOfYear + 1) else "" + (monthOfYear + 1)
            day = if (dayOfMonth <= 9) "0$dayOfMonth" else "" + dayOfMonth
            return "$year/$month/$day"
        }

        fun getNameOfDayPersianType(year: Int, monthOfYear: Int, dayOfMonth: Int): String? {

            val persianCalendar = PersianCalendar()
            persianCalendar.setPersianDate(year, monthOfYear, dayOfMonth)
            return  persianCalendar.persianWeekDayName


        }
    }

}