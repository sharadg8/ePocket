package com.sharad.epocket.accounts;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Sharad on 12-Sep-15.
 */
public class AccountItem {
    long id;
    String currency;
    String title;
    String note;
    String accountNumber;
    String loginId;
    String password;
    float  balanceCard;
    float  balanceCash;
    float  inflow;
    float  outflow;
    boolean splitAccount;
    Calendar lastUpdate;

    AccountItem(String title) {
        this.title = title;
    }

    AccountItem(long id, String currency, String title, String note, String accountNumber,
                String loginId, String password, float balanceCard, float  balanceCash,
                float  inflow, float  outflow, boolean splitAccount, long lastUpdateMSec) {
        this(id, currency, title, note, accountNumber, loginId, password, balanceCard, balanceCash,
                inflow, outflow, splitAccount, null);

        lastUpdate = Calendar.getInstance();
        lastUpdate.setTimeInMillis(lastUpdateMSec);
    }

    AccountItem(long id, String currency, String title, String note, String accountNumber,
                String loginId, String password, float balanceCard, float  balanceCash,
                float  inflow, float  outflow, boolean splitAccount, Calendar lastUpdate) {
        this.id = id;
        this.currency = currency;
        this.title = title;
        this.note = note;
        this.accountNumber = accountNumber;
        this.loginId = loginId;
        this.password = password;
        this.balanceCard = balanceCard;
        this.balanceCash = balanceCash;
        this.inflow = inflow;
        this.outflow = outflow;
        this.splitAccount = splitAccount;
        this.lastUpdate = lastUpdate;
    }

    public long getId() {        return id;    }
    public void setId(long id) {        this.id = id;    }

    public String getTitle() {         return title; }
    public String getNote() {          return note; }
    public String getCurrency() {      return currency; }
    public String getAccountNumber() { return accountNumber; }
    public String getLoginId() {       return loginId; }
    public String getPassword() {      return password; }
    public Calendar getLastUpdate() {  return lastUpdate; }
    public float getBalanceCard() {    return balanceCard; }
    public float getBalanceCash() {    return balanceCash; }
    public float getInflow() {         return inflow; }
    public float getOutflow() {        return outflow; }
    public boolean isSplitAccount() {  return splitAccount; }

    public String getLastUpdateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("Last Update: EEE, dd MMM yyyy");
        return sdf.format(lastUpdate.getTime());
    }
}