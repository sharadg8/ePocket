package com.sharad.epocket.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateUtils;
import android.text.style.RelativeSizeSpan;
import android.view.MenuItem;

import com.sharad.epocket.R;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Sharad on 12-Jul-16.
 */

public class Utils {
    private static HashMap<String, Locale> localeMap = new HashMap<>();
    private static HashMap<String, Float> exchangeMap = new HashMap<>();

    public static void updateExchangeRate(String isoCurrency, float rate) {
        exchangeMap.put(isoCurrency, rate);
    }

    public static float getExchangeRate(String isoCurrency) {
        return exchangeMap.get(isoCurrency);
    }

    public static void loadLocaleMap(ArrayList<String> isoCurrencies) {
        for (Locale locale : NumberFormat.getAvailableLocales()) {
            String code = NumberFormat.getCurrencyInstance(locale).getCurrency().getCurrencyCode();
            if (isoCurrencies.indexOf(code) != Constant.INVALID_ID) {
                localeMap.put(code, new Locale.Builder().setLocale(locale).setLanguage("en").build());
                isoCurrencies.remove(code);
                if(isoCurrencies.size() == 0) { break; }
            }
        }
    }

    public static String formatCurrency(String isoCurrency, float value) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(getLocale(isoCurrency));
        nf.setRoundingMode(RoundingMode.HALF_DOWN);
        nf.setMaximumFractionDigits(0);
        return nf.format(value);
    }

    public static SpannableString formatCurrencyDec(String isoCurrency, float value) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(getLocale(isoCurrency));
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);

        float decValue = value - (float)Math.floor(value);
        DecimalFormat df = new DecimalFormat(".##");
        df.setMaximumIntegerDigits(0);
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);
        String decString = df.format(decValue);

        SpannableString span = new SpannableString(nf.format(value));
        int decPos = span.toString().lastIndexOf(decString);
        span.setSpan(new RelativeSizeSpan(0.6f), decPos, decPos+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
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

    public static void setTaskDescription(Context context) {
        if(context instanceof Activity) {
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_splash);
            ((Activity)context).setTaskDescription(new ActivityManager.TaskDescription(
                    context.getResources().getString(R.string.app_name), bm, Color.WHITE));
        }
    }

    public static void tintMenuIcon(Context context, MenuItem item, @ColorRes int color) {
        if(item != null) {
            Drawable normalDrawable = item.getIcon();
            Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
            DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(context, color));

            item.setIcon(wrapDrawable);
        }
    }

    public static int dp2pix(int dp) {
        return Math.round(dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pix2dp(int pix) {
        return Math.round(pix / Resources.getSystem().getDisplayMetrics().density);
    }

    public static String getLongDateString(long timeInMsec) {
        String text;
        if(DateUtils.isToday(timeInMsec)) {
            text = "Today";
        } else if(isYesterday(timeInMsec)) {
            text = "Yesterday";
        } else {
            SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy");
            text = df.format(timeInMsec);
        }
        return text;
    }

    public static long getMonthStart_ms(long time_ms){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time_ms);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static long getMonthEnd_ms(long time_ms){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time_ms);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTimeInMillis();
    }

    public static long getYearStart_ms(long time_ms){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time_ms);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static long getYearEnd_ms(long time_ms){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time_ms);
        cal.set(Calendar.MONTH, 11);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTimeInMillis();
    }

    public static long getDayStart_ms(long time_ms){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time_ms);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static long getDayEnd_ms(long time_ms){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time_ms);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
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

    public static boolean isThisMonth(long date) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(date);
        boolean isThisMonth = (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR))
                && (cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH));
        return isThisMonth;
    }

    public static int getDayOfMonth(long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }
}
