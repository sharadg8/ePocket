package com.sharad.epocket.accounts;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.format.DateUtils;

import com.sharad.epocket.database.ContentConstant;
import com.sharad.epocket.utils.Constant;
import com.sharad.epocket.utils.Item;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Currency;
import java.util.Locale;

/**
 * Created by Sharad on 12-Sep-15.
 */
public class IAccount extends Item{
    public final static int ACCOUNT_TYPE_CASH_CARD = 0;
    public final static int ACCOUNT_TYPE_CARD_ONLY = 1;
    public final static int ACCOUNT_TYPE_CASH_ONLY = 2;

    String isoCurrency;
    String title;
    String note;
    String accountNumber;
    String loginId;
    String password;
    String contact;
    int accountType;
    int listIndex;
    int imageIndex;
    float inflow;
    float outflow;
    float  balanceCard;
    float  balanceCash;
    long lastUpdate;

    public IAccount() {
        this(Constant.INVALID_ID, Currency.getInstance(Locale.getDefault()).getCurrencyCode(),
                "", "", "", "", "", "", ACCOUNT_TYPE_CASH_CARD, -1, 0, 0, 0,
                Calendar.getInstance().getTimeInMillis());
    }

    public IAccount(long id, String isoCurrency, String title, String note, String accountNumber,
                    String loginId, String password, String contact, int imageIndex, int accountType,
                    int listIndex, float balanceCard, float balanceCash, long lastUpdateMSec) {

        super(id);

        this.isoCurrency = isoCurrency;
        this.title = title;
        this.note = note;
        this.accountNumber = accountNumber;
        this.loginId = loginId;
        this.password = password;
        this.contact = contact;
        this.accountType = accountType;
        this.balanceCard = balanceCard;
        this.balanceCash = balanceCash;
        this.inflow = 0;
        this.outflow = 0;
        this.listIndex = listIndex;
        this.imageIndex = imageIndex;
        lastUpdate = lastUpdateMSec;
    }

    public IAccount(Cursor c) {
        super(Constant.INVALID_ID);

        long id 		     = c.getLong(c.getColumnIndex(ContentConstant.KEY_ACCOUNT_ROWID));
        String title         = c.getString(c.getColumnIndex(ContentConstant.KEY_ACCOUNT_TITLE));
        String isoCurrency   = c.getString(c.getColumnIndex(ContentConstant.KEY_ACCOUNT_CURRENCY));
        String note          = c.getString(c.getColumnIndex(ContentConstant.KEY_ACCOUNT_NOTE));
        String accountNumber = c.getString(c.getColumnIndex(ContentConstant.KEY_ACCOUNT_NUMBER));
        String loginId       = c.getString(c.getColumnIndex(ContentConstant.KEY_ACCOUNT_LOGIN));
        String password      = c.getString(c.getColumnIndex(ContentConstant.KEY_ACCOUNT_PASSWORD));
        String contact       = c.getString(c.getColumnIndex(ContentConstant.KEY_ACCOUNT_CONTACT));
        int imageIndex       = c.getInt(c.getColumnIndex(ContentConstant.KEY_ACCOUNT_LOGO));
        int accountType      = c.getInt(c.getColumnIndex(ContentConstant.KEY_ACCOUNT_TYPE));
        int listIndex        = c.getInt(c.getColumnIndex(ContentConstant.KEY_ACCOUNT_LIST_INDEX));
        float balanceCard    = c.getFloat(c.getColumnIndex(ContentConstant.KEY_ACCOUNT_BAL_CARD));
        float balanceCash    = c.getFloat(c.getColumnIndex(ContentConstant.KEY_ACCOUNT_BAL_CASH));
        long lastUpdateMSec  = c.getLong(c.getColumnIndex(ContentConstant.KEY_ACCOUNT_LAST_UPDATE));

        this.id = id;
        this.isoCurrency = isoCurrency;
        this.title = title;
        this.note = note;
        this.accountNumber = accountNumber;
        this.loginId = loginId;
        this.password = password;
        this.contact = contact;
        this.balanceCard = balanceCard;
        this.balanceCash = balanceCash;
        this.accountType = accountType;
        this.balanceCard = balanceCard;
        this.balanceCash = balanceCash;
        this.inflow = 0;
        this.outflow = 0;
        this.listIndex = listIndex;
        this.imageIndex = imageIndex;
        this.lastUpdate = lastUpdateMSec;
    }

    @Override
    public ContentValues getContentValues() {
        // Create row's data:
        ContentValues content = new ContentValues();
        content.put(ContentConstant.KEY_ACCOUNT_TITLE, this.getTitle());
        content.put(ContentConstant.KEY_ACCOUNT_CURRENCY, this.getIsoCurrency());
        content.put(ContentConstant.KEY_ACCOUNT_NOTE, this.getNote());
        content.put(ContentConstant.KEY_ACCOUNT_NUMBER, this.getAccountNumber());
        content.put(ContentConstant.KEY_ACCOUNT_LOGIN, this.getLoginId());
        content.put(ContentConstant.KEY_ACCOUNT_PASSWORD, this.getPassword());
        content.put(ContentConstant.KEY_ACCOUNT_CONTACT, this.getContact());
        content.put(ContentConstant.KEY_ACCOUNT_TYPE, this.getAccountType());
        content.put(ContentConstant.KEY_ACCOUNT_LIST_INDEX, this.getListIndex());
        content.put(ContentConstant.KEY_ACCOUNT_BAL_CARD, this.getBalanceCard());
        content.put(ContentConstant.KEY_ACCOUNT_BAL_CASH, this.getBalanceCash());
        content.put(ContentConstant.KEY_ACCOUNT_LAST_UPDATE, this.getLastUpdate());

        return content;
    }

    public String getTitle() {         return title; }
    public String getNote() {          return note; }
    public String getAccountNumber() { return accountNumber; }
    public String getLoginId() {       return loginId; }
    public String getPassword() {      return password; }
    public String getContact() {       return contact; }
    public long getLastUpdate() {      return lastUpdate; }
    public float getBalanceCard() {    return balanceCard; }
    public float getBalanceCash() {    return balanceCash; }
    public float getBalance() {        return balanceCash + balanceCard;  }
    public float getInflow() {         return inflow; }
    public float getOutflow() {        return outflow; }
    public int getAccountType() {      return accountType; }
    public String getIsoCurrency() {   return isoCurrency; }
    public int getListIndex() {        return listIndex;   }
    public int getImageIndex() {       return imageIndex;  }

    public void setInflow(float inflow) {        this.inflow = inflow;    }
    public void setOutflow(float outflow) {      this.outflow = outflow;  }
    public void setListIndex(int listIndex) {    this.listIndex = listIndex; }
    public void setAccountNumber(String accountNumber) {    this.accountNumber = accountNumber;    }
    public void setAccountType(int accountType) {        this.accountType = accountType;    }
    public void setBalanceCard(float balanceCard) {        this.balanceCard = balanceCard;    }
    public void setBalanceCash(float balanceCash) {        this.balanceCash = balanceCash;    }
    public void setContact(String contact) {        this.contact = contact;    }
    public void setIsoCurrency(String isoCurrency) {        this.isoCurrency = isoCurrency;    }
    public void setLastUpdate(long lastUpdate) {        this.lastUpdate = lastUpdate;    }
    public void setLoginId(String loginId) {        this.loginId = loginId;    }
    public void setNote(String note) {        this.note = note;    }
    public void setPassword(String password) {        this.password = password;    }
    public void setTitle(String title) {        this.title = title;    }
    public void setImageIndex(int imageIndex) { this.imageIndex = imageIndex;   }

    public boolean hasCardAccount() { return ((accountType == ACCOUNT_TYPE_CASH_CARD) || (accountType == ACCOUNT_TYPE_CARD_ONLY));  }
    public boolean hasCashAccount() { return ((accountType == ACCOUNT_TYPE_CASH_CARD) || (accountType == ACCOUNT_TYPE_CASH_ONLY));  }
    public boolean hasBothAccount() { return (accountType == ACCOUNT_TYPE_CASH_CARD);  }

    public String getLastUpdateString() {
        String string;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(lastUpdate);
        if(DateUtils.isToday(lastUpdate)) {
            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
            string = "Last Update: Today " + sdf.format(cal.getTime());
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy");
            string = "Last Update: " + sdf.format(cal.getTime());
        }

        return string;
    }

    public static class iComparator implements Comparator<IAccount> {
        @Override
        public int compare(IAccount o, IAccount o1) {
            return (o.getListIndex() == o1.getListIndex())
                    ? 0
                    : ((o.getListIndex() < o1.getListIndex()) ? -1 : 1);
        }
    }
}