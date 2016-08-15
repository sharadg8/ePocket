package com.sharad.epocket.bills;

import android.content.ContentValues;
import android.database.Cursor;

import com.sharad.epocket.database.BillTable;
import com.sharad.epocket.utils.Constant;
import com.sharad.epocket.utils.Item;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Sharad on 01-Jul-16.
 */

public class IBill extends Item{
    private String title;
    private String account;
    private String currency;
    private float amount;
    private Calendar startDate;
    private Calendar nextDate;
    private Calendar endDate;
    private int repeat;
    private int daysRemaining;

    public IBill(long id, String title, String account, String currency,
                 float amount, long startDate, long endDate, int repeat) {
        super(id);

        this.title = title;
        this.account = account;
        this.currency = currency;
        this.amount = amount;
        this.repeat = repeat;
        this.startDate = Calendar.getInstance();
        this.startDate.setTimeInMillis(startDate);
        this.endDate = Calendar.getInstance();
        this.endDate.setTimeInMillis(endDate);

        this.nextDate = Calendar.getInstance();
        this.nextDate.setTimeInMillis(startDate);

        Calendar today = Calendar.getInstance();
        long diff = this.nextDate.getTimeInMillis() - today.getTimeInMillis();
        this.daysRemaining = (int)(diff / (24 * 60 * 60 * 1000));
    }

    public IBill(Cursor c) {
        super(Constant.INVALID_ID);

        long id 		= c.getLong(c.getColumnIndex(BillTable.COLUMN_ID));
        String title 	= c.getString(c.getColumnIndex(BillTable.COLUMN_TITLE));
        String account  = c.getString(c.getColumnIndex(BillTable.COLUMN_ACCOUNT));
        String currency	= c.getString(c.getColumnIndex(BillTable.COLUMN_CURRENCY));
        float amount	= c.getFloat(c.getColumnIndex(BillTable.COLUMN_AMOUNT));
        long startDate  = c.getLong(c.getColumnIndex(BillTable.COLUMN_DATE));
        long endDate	= c.getLong(c.getColumnIndex(BillTable.COLUMN_END_DATE));
        int repeat		= c.getInt(c.getColumnIndex(BillTable.COLUMN_REPEAT));

        this.id = id;
        this.title = title;
        this.account = account;
        this.currency = currency;
        this.amount = amount;
        this.repeat = repeat;
        this.startDate = Calendar.getInstance();
        this.startDate.setTimeInMillis(startDate);
        this.endDate = Calendar.getInstance();
        this.endDate.setTimeInMillis(endDate);

        this.nextDate = Calendar.getInstance();
        this.nextDate.setTimeInMillis(startDate);

        Calendar today = Calendar.getInstance();
        long diff = this.nextDate.getTimeInMillis() - today.getTimeInMillis();
        this.daysRemaining = (int)(diff / (24 * 60 * 60 * 1000));
    }

    @Override
    public ContentValues getContentValues() {
        // Create row's data:
        ContentValues content = new ContentValues();
        content.put(BillTable.COLUMN_TITLE     , this.getTitle());
        content.put(BillTable.COLUMN_ACCOUNT   , this.getAccount());
        content.put(BillTable.COLUMN_CURRENCY  , this.getCurrency());
        content.put(BillTable.COLUMN_AMOUNT    , this.getAmount());
        content.put(BillTable.COLUMN_DATE      , this.getStartDateLong());
        content.put(BillTable.COLUMN_END_DATE  , this.getEndDateLong());
        content.put(BillTable.COLUMN_REPEAT    , this.getRepeat());

        return content;
    }
    public String getTitle() {        return title;    }
    public float getAmount() {        return amount;    }
    public String getCurrency() {        return currency;    }
    public int getRepeat() {        return repeat;    }
    public String getAccount() {        return account;    }
    public int getDaysRemaining() {        return daysRemaining;    }
    public Calendar getEndDate() {        return endDate;    }
    public Calendar getStartDate() {        return startDate;    }
    public long getEndDateLong() {        return endDate.getTimeInMillis();    }
    public long getStartDateLong() {        return startDate.getTimeInMillis();    }

    public String getAmountString() {
        DecimalFormat df = new DecimalFormat("##,###.##");
        if(currency == "₹") {
            df.applyPattern("##,##,###.##");
        }
        return currency + df.format(amount);
    }

    public String getNextDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy");
        return sdf.format(nextDate.getTime());
    }

    public String getDaysString() {
        String string;
        if(daysRemaining < 0) {
            string = "" + Math.abs(daysRemaining) + " days ago";
        } else {
            string = "" + daysRemaining + " days remaining";
        }
        return string;
    }

    public Calendar getNextDate() {
        return nextDate;
    }
}
