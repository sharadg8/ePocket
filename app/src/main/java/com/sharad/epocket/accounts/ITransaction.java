package com.sharad.epocket.accounts;

import android.content.ContentValues;
import android.database.Cursor;

import com.sharad.epocket.database.TransactionTable;
import com.sharad.epocket.utils.Constant;
import com.sharad.epocket.utils.Item;

import java.util.Calendar;
import java.util.Comparator;

/**
 * Created by Sharad on 23-Jul-16.
 */

public class ITransaction extends Item {
    private long date;
    private String comment;
    private String repeat;
    private int type;
    private int subType;
    private long account;
    private long category;
    private float amount;

    public static final int TRANSACTION_TYPE_ACCOUNT_EXPENSE  = 0;
    public static final int TRANSACTION_TYPE_ACCOUNT_INCOME   = 1;
    public static final int TRANSACTION_TYPE_ACCOUNT_TRANSFER = 2;
    public static final int TRANSACTION_TYPE_ACCOUNT_WITHDRAW = 3;
    public static final int TRANSACTION_TYPE_ACCOUNT_DEPOSIT  = 4;

    public static final int META_DATA_START  = 100;
    public static final int META_DATA_MONTH_OPENING_BALANCE_CARD  = 101;
    public static final int META_DATA_MONTH_OPENING_BALANCE_CASH  = 102;
    public static final int META_DATA_MONTH_INCOME           = 103;
    public static final int META_DATA_MONTH_EXPENSE          = 104;
    public static final int META_DATA_MONTH_TRANSFER         = 105;

    public static final int TRANSACTION_SUB_TYPE_ACCOUNT_CARD = 1;
    public static final int TRANSACTION_SUB_TYPE_ACCOUNT_CASH = 2;
    public static final int TRANSACTION_SUB_TYPE_ACCOUNT_BOTH = 3;

    public ITransaction() {
        this(Constant.INVALID_ID, 0, "", "", TRANSACTION_TYPE_ACCOUNT_EXPENSE,
                TRANSACTION_SUB_TYPE_ACCOUNT_CASH, Constant.INVALID_ID, Constant.INVALID_ID, 0);
        Calendar cal = Calendar.getInstance();
        this.date = cal.getTimeInMillis();
    }

    public ITransaction(long id, long date, String comment, String repeat, int type, int subType,
                        long account, long category, float amount) {
        super(id);

        this.date = date;
        this.comment = comment;
        this.repeat = repeat;
        this.type = type;
        this.subType = subType;
        this.account = account;
        this.category = category;
        this.amount = amount;
    }

    public ITransaction(Cursor c) {
        super(Constant.INVALID_ID);

        long id 	    = c.getLong(c.getColumnIndex(TransactionTable.COLUMN_ID));
        long date       = c.getLong(c.getColumnIndex(TransactionTable.COLUMN_DATE));
        String comment  = c.getString(c.getColumnIndex(TransactionTable.COLUMN_COMMENT));
        String repeat   = c.getString(c.getColumnIndex(TransactionTable.COLUMN_REPEAT));
        int type        = c.getInt(c.getColumnIndex(TransactionTable.COLUMN_TYPE));
        int subType     = c.getInt(c.getColumnIndex(TransactionTable.COLUMN_SUB_TYPE));
        long account    = c.getLong(c.getColumnIndex(TransactionTable.COLUMN_ACCOUNT));
        long category   = c.getLong(c.getColumnIndex(TransactionTable.COLUMN_CATEGORY));
        float amount    = c.getFloat(c.getColumnIndex(TransactionTable.COLUMN_AMOUNT));

        this.id = id;
        this.date = date;
        this.comment = comment;
        this.repeat = repeat;
        this.type = type;
        this.subType = subType;
        this.account = account;
        this.category = category;
        this.amount = amount;
    }

    @Override
    public ContentValues getContentValues() {
        // Create row's data:
        ContentValues content = new ContentValues();
        content.put(TransactionTable.COLUMN_DATE,     this.getDate());
        content.put(TransactionTable.COLUMN_COMMENT,  this.getComment());
        content.put(TransactionTable.COLUMN_REPEAT,   this.getRepeat());
        content.put(TransactionTable.COLUMN_TYPE,     this.getType());
        content.put(TransactionTable.COLUMN_SUB_TYPE, this.getSubType());
        content.put(TransactionTable.COLUMN_ACCOUNT,  this.getAccount());
        content.put(TransactionTable.COLUMN_CATEGORY, this.getCategory());
        content.put(TransactionTable.COLUMN_AMOUNT,   this.getAmount());

        return content;
    }

    public float getAmount() {   return amount;   }
    public int getType() {       return type;     }
    public int getSubType() {    return subType;  }
    public long getCategory() {  return category; }
    public long getDate() {      return date;     }
    public long getAccount() {   return account;  }
    public String getComment() { return comment;  }
    public String getRepeat() {  return repeat;   }

    public void setDate(long date) {         this.date = date;         }
    public void setComment(String comment) { this.comment = comment;   }
    public void setAccount(long account) {   this.account = account;   }
    public void setRepeat(String repeat) {   this.repeat = repeat;     }
    public void setType(int type) {          this.type = type;         }
    public void setSubType(int subType) {    this.subType = subType;   }
    public void setAmount(float amount) {    this.amount = amount;     }
    public void setCategory(long category) { this.category = category; }

    public static class iComparator implements Comparator<ITransaction> {
        @Override
        public int compare(ITransaction o, ITransaction o1) {
            return (o.getDate() == o1.getDate())
                    ? 0
                    : ((o.getDate() < o1.getDate()) ? 1 : -1);
        }
    }
}
