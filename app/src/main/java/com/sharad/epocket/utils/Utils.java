package com.sharad.epocket.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.MenuItem;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Sharad on 12-Jul-16.
 */

public class Utils {

    public static String formatCurrency(Locale locale, float value) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        nf.setMaximumFractionDigits(0);
        return nf.format(value);
    }

    public static String formatCurrencyDec(Locale locale, float value) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        return nf.format(value);
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
