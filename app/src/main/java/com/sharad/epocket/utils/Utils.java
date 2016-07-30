package com.sharad.epocket.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.MenuItem;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Sharad on 12-Jul-16.
 */

public class Utils {
    private static HashMap<String, Locale> localeMap = new HashMap<>();

    public static String formatCurrency(String isoCurrency, float value) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(getLocale(isoCurrency));
        nf.setRoundingMode(RoundingMode.HALF_DOWN);
        nf.setMaximumFractionDigits(0);
        return nf.format(value);
    }

    public static String formatCurrencyDec(String isoCurrency, float value) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(getLocale(isoCurrency));
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        return nf.format(value);
    }

    private static Locale getLocale(String isoCurrency) {
        Locale selectedLocale = localeMap.get(isoCurrency);
        if(selectedLocale == null) {
            for (Locale locale : NumberFormat.getAvailableLocales()) {
                String code = NumberFormat.getCurrencyInstance(locale).getCurrency().getCurrencyCode();
                if (isoCurrency.equals(code)) {
                    selectedLocale = new Locale.Builder().setLocale(locale).setLanguage("en").build();
                    localeMap.put(isoCurrency, selectedLocale);
                    break;
                }
            }
        }

        if(selectedLocale == null) {
            selectedLocale = Locale.getDefault();
            localeMap.put(isoCurrency, selectedLocale);
        }
        return selectedLocale;
    }

    public static void tintMenuIcon(Context context, MenuItem item, @ColorRes int color) {
        if(item != null) {
            Drawable normalDrawable = item.getIcon();
            Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
            DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(context, color));

            item.setIcon(wrapDrawable);
        }
    }

    public static long getMonthStart_ms(long time_ms){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time_ms);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTimeInMillis();
    }

    public static long getMonthEnd_ms(long time_ms){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time_ms);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTimeInMillis();
    }

    public static boolean isSameDay(long ms1, long ms2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(ms1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(ms2);
        boolean isSameDay = (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR))
                && (cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH))
                && (cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH));
        return isSameDay;
    }

    public static boolean isYesterday(long date) {
        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.DAY_OF_MONTH, -1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(date);
        boolean isYesterday = (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR))
                && (cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH))
                && (cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH));
        return isYesterday;
    }
}
