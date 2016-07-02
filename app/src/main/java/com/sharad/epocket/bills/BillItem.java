package com.sharad.epocket.bills;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Sharad on 01-Jul-16.
 */

public class BillItem {
    private long id;
    private String title;
    private String account;
    private String currency;
    private float amount;
    private Calendar startDate;
    private Calendar nextDate;
    private Calendar endDate;
    private int repeat;
    private int daysRemaining;

    public BillItem(long id, String title, String account, String currency,
                    float amount, long startDate, long endDate, int repeat) {
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

    public long getId() {        return id;    }
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
