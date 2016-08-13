package com.sharad.epocket.utils;

/**
 * Created by Sharad on 30-Jul-16.
 */

public interface Constant {
    int INVALID_ID = -1;

    String ARG_ACCOUNT_NUMBER_LONG      = "account_number";
    String ARG_TRANSACTION_NUMBER_LONG  = "transaction_number";
    String ARG_TIME_IN_MS_LONG          = "time_in_ms";

    String DLG_CURRENCY_PICKER          = "currency_picker";
    String DLG_ACCOUNT_WITHDRAW         = "account_withdraw";
    String DLG_CATEGORY_EDITOR          = "category_editor";

    int REQ_ADD_ACCOUNT                 = 1;
    int REQ_EDIT_ACCOUNT                = 2;
    int REQ_ADD_TRANSACTION             = 3;
    int REQ_EDIT_TRANSACTION            = 4;
    int REQ_LIST_TRANSACTION            = 5;
    int REQ_LIST_TRANSACTION_YEAR       = 6;


    int TAB_ACCOUNT_TRANSACTION_THIS_MONTH  = 0;
    int TAB_ACCOUNT_TRANSACTION_MONTH_M1    = 1;
    int TAB_ACCOUNT_TRANSACTION_MONTH_M2    = 2;
    int TAB_ACCOUNT_TRANSACTION_MONTH_M3    = 3;
    int TAB_ACCOUNT_TRANSACTION_MONTH_M4    = 4;
    int TAB_ACCOUNT_TRANSACTION_OLDER       = 5;
}
