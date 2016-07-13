package com.sharad.epocket.utils;

import java.util.Locale;

/**
 * Created by Sharad on 13-Jul-16.
 */

public class CurrencyUtils {
    public static final int CURRENCY_INDIAN_RUPEE       = 0;
    public static final int CURRENCY_AMERICAN_DOLLER    = 1;
    public static final int CURRENCY_EURO               = 2;
    public static final int CURRENCY_JAPANESE_YEN       = 3;
    public static final int CURRENCY_BRITISH_POUND      = 4;
    public static final int CURRENCY_AUSTRAILIAN_DOLLER = 5;
    public static final int CURRENCY_SWISS_FRANC        = 6;
    public static final int CURRENCY_CANADIAN_DOLLER    = 7;
    public static final int CURRENCY_MEXICAN_PESO       = 8;
    public static final int CURRENCY_CHINESE_YUAN       = 9;
    public static final int CURRENCY_NEW_ZEALAND_DOLLER = 10;
    public static final int CURRENCY_SWEDISH_KRONA      = 11;
    public static final int CURRENCY_RUSSIAN_RUBLE      = 12;
    public static final int CURRENCY_HONGKONG_DOLLER    = 13;
    public static final int CURRENCY_NORWEGIAN_KRONE    = 14;
    public static final int CURRENCY_SINGAPORE_DOLLER   = 15;
    public static final int CURRENCY_TURKISH_LIRA       = 16;
    public static final int CURRENCY_SOUTH_KOREAN_WON   = 17;
    public static final int CURRENCY_SOUTH_AFRICAN_RAND = 18;
    public static final int CURRENCY_BRAZILIAN_REAL     = 19;
    public static final int CURRENCY_EMIRATI_DIRHAM     = 20;

    public static Locale getLocale(int currencyIndex) {
        switch (currencyIndex) {
            case CURRENCY_INDIAN_RUPEE:
                return new Locale("en", "IN");
            case CURRENCY_AMERICAN_DOLLER:
                return Locale.US;
            case CURRENCY_EURO:
                return Locale.ITALY;
            case CURRENCY_JAPANESE_YEN:
                return Locale.JAPAN;
            case CURRENCY_BRITISH_POUND:
                return Locale.UK;
            case CURRENCY_AUSTRAILIAN_DOLLER:

            case CURRENCY_SWISS_FRANC:

            case CURRENCY_CANADIAN_DOLLER:
                return Locale.CANADA;
            case CURRENCY_MEXICAN_PESO:

            case CURRENCY_CHINESE_YUAN:
                return Locale.CHINA;
            case CURRENCY_NEW_ZEALAND_DOLLER:

            case CURRENCY_SWEDISH_KRONA:

            case CURRENCY_RUSSIAN_RUBLE:

            case CURRENCY_HONGKONG_DOLLER:

            case CURRENCY_NORWEGIAN_KRONE:

            case CURRENCY_SINGAPORE_DOLLER:

            case CURRENCY_TURKISH_LIRA:

            case CURRENCY_SOUTH_KOREAN_WON:

            case CURRENCY_SOUTH_AFRICAN_RAND:

            case CURRENCY_BRAZILIAN_REAL:

            case CURRENCY_EMIRATI_DIRHAM:
                return Locale.getDefault();
        }
        return Locale.getDefault();
    }
}
