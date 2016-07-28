package com.sharad.epocket.accounts;

import android.text.format.DateUtils;

import com.sharad.epocket.utils.Item;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Sharad on 12-Sep-15.
 */
public class IAccount extends Item{
    public final static int ACCOUNT_TYPE_CASH_CARD = 0;
    public final static int ACCOUNT_TYPE_CARD_ONLY = 1;
    public final static int ACCOUNT_TYPE_CASH_ONLY = 2;

    public static final long INVALID_ID = -1;

    String isoCurrency;
    String title;
    String note;
    String accountNumber;
    String loginId;
    String password;
    String contact;
    float  balanceCard;
    float  balanceCash;
    float  inflow;
    float  outflow;
    int accountType;
    Calendar lastUpdate;

    public IAccount(long id, String isoCurrency, String title, String note, String accountNumber,
                    String loginId, String password, String contact, float balanceCard, float balanceCash,
                    float inflow, float outflow, int accountType, long lastUpdateMSec) {
        this(id, isoCurrency, title, note, accountNumber, loginId, password, contact,
                balanceCard, balanceCash, inflow, outflow, accountType, null);

        lastUpdate = Calendar.getInstance();
        lastUpdate.setTimeInMillis(lastUpdateMSec);
    }

    IAccount(long id, String isoCurrency, String title, String note, String accountNumber,
             String loginId, String password, String contact, float balanceCard, float  balanceCash,
             float  inflow, float  outflow, int accountType, Calendar lastUpdate) {
        super(id);

        this.isoCurrency = isoCurrency;
        this.title = title;
        this.note = note;
        this.accountNumber = accountNumber;
        this.loginId = loginId;
        this.password = password;
        this.contact = contact;
        this.balanceCard = balanceCard;
        this.balanceCash = balanceCash;
        this.inflow = inflow;
        this.outflow = outflow;
        this.accountType = accountType;
        this.lastUpdate = lastUpdate;
    }

    public String getTitle() {         return title; }
    public String getNote() {          return note; }
    public String getAccountNumber() { return accountNumber; }
    public String getLoginId() {       return loginId; }
    public String getPassword() {      return password; }
    public String getContact() {      return contact; }
    public Calendar getLastUpdate() {  return lastUpdate; }
    public long getLastUpdateMSec() {  return lastUpdate.getTimeInMillis(); }
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