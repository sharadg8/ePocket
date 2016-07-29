package com.sharad.epocket.accounts;

import android.content.ContentValues;
import android.database.Cursor;

import com.sharad.epocket.database.ContentConstant;
import com.sharad.epocket.utils.Item;

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

    public static final int TRANSACTION_SUB_TYPE_ACCOUNT_CARD = 1;
    public static final int TRANSACTION_SUB_TYPE_ACCOUNT_CASH = 2;

    public ITransaction() {
            this(0, 0, "", "", 0, 0, 0, 0, 0);
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
        super(0);

        long id 	    = c.getLong(c.getColumnIndex(ContentConstant.KEY_TRANSACTION_ROWID));
        long date       = c.getLong(c.getColumnIndex(ContentConstant.KEY_TRANSACTION_DATE));
        String comment  = c.getString(c.getColumnIndex(ContentConstant.KEY_TRANSACTION_COMMENT));
        String repeat   = c.getString(c.getColumnIndex(ContentConstant.KEY_TRANSACTION_REPEAT));
        int type        = c.getInt(c.getColumnIndex(ContentConstant.KEY_TRANSACTION_TYPE));
        int subType     = c.getInt(c.getColumnIndex(ContentConstant.KEY_TRANSACTION_SUB_TYPE));
        long account    = c.getLong(c.getColumnIndex(ContentConstant.KEY_TRANSACTION_ACCOUNT));
        long category   = c.getLong(c.getColumnIndex(ContentConstant.KEY_TRANSACTION_CATEGORY));
        float amount    = c.getFloat(c.getColumnIndex(ContentConstant.KEY_TRANSACTION_AMOUNT));

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

    public ContentValues getContentValues() {
        // Create row's data:
        ContentValues content = new ContentValues();
        content.put(ContentConstant.KEY_TRANSACTION_DATE,     this.getDate());
        content.put(ContentConstant.KEY_TRANSACTION_COMMENT,  this.getComment());
        content.put(ContentConstant.KEY_TRANSACTION_REPEAT,   this.getRepeat());
        content.put(ContentConstant.KEY_TRANSACTION_TYPE,     this.getType());
        content.put(ContentConstant.KEY_TRANSACTION_SUB_TYPE, this.getSubType());
        content.put(ContentConstant.KEY_TRANSACTION_ACCOUNT,  this.getAccount());
        content.put(ContentConstant.KEY_TRANSACTION_CATEGORY, this.getCategory());
        content.put(ContentConstant.KEY_TRANSACTION_AMOUNT,   this.getAmount());

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
}
