package com.sharad.epocket.database;

/**
 * Created by Sharad on 29-Jul-16.
 */

public interface ContentConstant {
    /**
     * Data table for bills
     */
    String KEY_BILL_ROWID      = "_id";
    String KEY_BILL_TITLE      = "title";
    String KEY_BILL_ACCOUNT    = "account";
    String KEY_BILL_CURRENCY   = "currency";
    String KEY_BILL_AMOUNT     = "amount";
    String KEY_BILL_DATE       = "date";
    String KEY_BILL_END_DATE   = "end_date";
    String KEY_BILL_REPEAT     = "repeat";

    String[] ALL_KEYS_BILL = new String[] {KEY_BILL_ROWID, KEY_BILL_TITLE,
            KEY_BILL_ACCOUNT, KEY_BILL_CURRENCY, KEY_BILL_AMOUNT, KEY_BILL_DATE, KEY_BILL_END_DATE,
            KEY_BILL_REPEAT};
    
    /**
     * Data table for accounts
     */
    String KEY_ACCOUNT_ROWID      = "_id";
    String KEY_ACCOUNT_TITLE      = "title";
    String KEY_ACCOUNT_CURRENCY   = "currency";
    String KEY_ACCOUNT_NOTE       = "note";
    String KEY_ACCOUNT_NUMBER     = "acc_num";
    String KEY_ACCOUNT_LOGIN      = "login";
    String KEY_ACCOUNT_PASSWORD   = "password";
    String KEY_ACCOUNT_CONTACT    = "contact";
    String KEY_ACCOUNT_BAL_CARD   = "bal_card";
    String KEY_ACCOUNT_BAL_CASH   = "bal_cash";
    String KEY_ACCOUNT_INFLOW     = "inflow";
    String KEY_ACCOUNT_OUTFLOW    = "outflow";
    String KEY_ACCOUNT_TYPE       = "type";
    String KEY_ACCOUNT_LAST_UPDATE= "last_update";

    String[] ALL_KEYS_ACCOUNT = new String[] {KEY_ACCOUNT_ROWID,
            KEY_ACCOUNT_TITLE, KEY_ACCOUNT_CURRENCY, KEY_ACCOUNT_NOTE, KEY_ACCOUNT_NUMBER,
            KEY_ACCOUNT_LOGIN, KEY_ACCOUNT_PASSWORD, KEY_ACCOUNT_CONTACT, KEY_ACCOUNT_BAL_CARD,
            KEY_ACCOUNT_BAL_CASH, KEY_ACCOUNT_INFLOW, KEY_ACCOUNT_OUTFLOW, KEY_ACCOUNT_TYPE,
            KEY_ACCOUNT_LAST_UPDATE };

    /**
     * Data table for categories
     */
    String KEY_CATEGORY_ROWID      = "_id";
    String KEY_CATEGORY_TITLE      = "title";
    String KEY_CATEGORY_COUNT      = "count";
    String KEY_CATEGORY_IMAGE_IDX  = "image_idx";
    String KEY_CATEGORY_COLOR      = "color";
    String KEY_CATEGORY_TYPE       = "type";

    String[] ALL_KEYS_CATEGORY = new String[] {KEY_CATEGORY_ROWID, KEY_CATEGORY_TITLE,
            KEY_CATEGORY_COUNT, KEY_CATEGORY_IMAGE_IDX, KEY_CATEGORY_COLOR, KEY_CATEGORY_TYPE };

    /**
     * Data table for transactions
     */
    String KEY_TRANSACTION_ROWID        = "_id";
    String KEY_TRANSACTION_DATE         = "date";
    String KEY_TRANSACTION_COMMENT      = "comment";
    String KEY_TRANSACTION_REPEAT       = "repeat";
    String KEY_TRANSACTION_TYPE         = "type";
    String KEY_TRANSACTION_SUB_TYPE     = "sub_type";
    String KEY_TRANSACTION_ACCOUNT      = "account";
    String KEY_TRANSACTION_CATEGORY     = "category";
    String KEY_TRANSACTION_AMOUNT       = "amount";

    String[] ALL_KEYS_TRANSACTION = new String[] {KEY_TRANSACTION_ROWID,
            KEY_TRANSACTION_DATE, KEY_TRANSACTION_COMMENT, KEY_TRANSACTION_REPEAT,
            KEY_TRANSACTION_TYPE, KEY_TRANSACTION_SUB_TYPE, KEY_TRANSACTION_ACCOUNT,
            KEY_TRANSACTION_CATEGORY, KEY_TRANSACTION_AMOUNT  };
}
