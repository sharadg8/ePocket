package com.sharad.epocket.utils;

/**
 * Created by Sharad on 30-Jul-16.
 */

public interface Constant {
    int INVALID_ID = -1;

    String ARG_ACCOUNT_NUMBER_LONG      = "account_number";
    String ARG_TRANSACTION_NUMBER_LONG  = "transaction_number";
    String ARG_TAB_NUMBER_INT           = "tab_number";

    int REQ_ADD_ACCOUNT                 = 1;
    int REQ_EDIT_ACCOUNT                = 2;
    int REQ_ADD_TRANSACTION             = 3;
    int REQ_EDIT_TRANSACTION            = 4;
    int REQ_LIST_TRANSACTION            = 5;
}
