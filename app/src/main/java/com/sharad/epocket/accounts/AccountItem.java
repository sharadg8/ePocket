package com.sharad.epocket.accounts;

import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Sharad on 12-Sep-15.
 */
public class AccountItem {
    public final static int ACCOUNT_TYPE_CASH_CARD = 0;
    public final static int ACCOUNT_TYPE_CARD_ONLY = 1;
    public final static int ACCOUNT_TYPE_CASH_ONLY = 2;

    long id;
    String isoCurrency;
    String title;
    String note;
    String accountNumber;
    String loginId;
    String password;
    float  balanceCard;
    float  balanceCash;
    float  inflow;
    float  outflow;
    int accountType;
    Calendar lastUpdate;

    AccountItem(String title) {
        this.title = title;
    }

    AccountItem(long id, String isoCurrency, String title, String note, String accountNumber,
                String loginId, String password, float balanceCard, float  balanceCash,
                float  inflow, float  outflow, int accountType, long lastUpdateMSec) {
        this(id, isoCurrency, title, note, accountNumber, loginId, password, balanceCard, balanceCash,
                inflow, outflow, accountType, null);

        lastUpdate = Calendar.getInstance();
        lastUpdate.setTimeInMillis(lastUpdateMSec);
    }

    AccountItem(long id, String isoCurrency, String title, String note, String accountNumber,
                String loginId, String password, float balanceCard, float  balanceCash,
                float  inflow, float  outflow, int accountType, Calendar lastUpdate) {
        this.id = id;
        this.isoCurrency = isoCurrency;
        this.title = title;
        this.note = note;
        this.accountNumber = accountNumber;
        this.loginId = loginId;
        this.password = password;
        this.balanceCard = balanceCard;
        this.balanceCash = balanceCash;
        this.inflow = inflow;
        this.outflow = outflow;
        this.accountType = accountType;
        this.lastUpdate = lastUpdate;
    }

    public long getId() {        return id;    }
    public void setId(long id) {        this.id = id;    }

    public String getTitle() {         return title; }
    public String getNote() {          return note; }
    public String getAccountNumber() { return accountNumber; }
    public String getLoginId() {       return loginId; }
    public String getPassword() {      return password; }
    public Calendar getLastUpdate() {  return lastUpdate; }
    public float getBalanceCard() {    return balanceCard; }
    public float getBalanceCash() {    return balanceCash; }
    public float getBalance() {        return balanceCash + balanceCard;  }
    public float getInflow() {         return inflow; }
    public float getOutflow() {        return outflow; }
    public int getAccountType() {      return accountType; }
    public String getIsoCurrency() {   return isoCurrency; }

    public boolean hasCardAccount() { return ((accountType == ACCOUNT_TYPE_CASH_CARD) || (accountType == ACCOUNT_TYPE_CARD_ONLY));  }
    public boolean hasCashAccount() { return ((accountType == ACCOUNT_TYPE_CASH_CARD) || (accountType == ACCOUNT_TYPE_CASH_ONLY));  }

    public String getLastUpdateString() {
        String string;
        if(DateUtils.isToday(lastUpdate.getTimeInMillis())) {
            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
            string = "Last Update: Today " + sdf.format(lastUpdate.getTime());
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy");
            string = "Last Update: " + sdf.format(lastUpdate.getTime());
        }

        return string;
    }
}