package com.sharad.epocket.utils;

import java.text.NumberFormat;

/**
 * Created by Sharad on 12-Jul-16.
 */

public class Utils {

    public static String formatCurrency(String currency, float value) {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        return currency + " " + nf.format(value);
    }

    public static String formatCurrencyDec(String currency, float value) {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        return currency + " " + nf.format(value);
    }
}
