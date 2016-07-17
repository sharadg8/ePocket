package com.sharad.epocket.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.MenuItem;

import java.math.RoundingMode;
import java.text.NumberFormat;
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
}
