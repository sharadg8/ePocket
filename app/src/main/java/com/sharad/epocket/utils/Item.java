package com.sharad.epocket.utils;

import android.content.ContentValues;

/**
 * Created by Sharad on 24-Jul-16.
 */

public class Item {
    protected long id;
    public Item(long id) {        this.id = id;    }
    public void setId(long id) {  this.id = id;    }
    public long getId() {         return id;       }

    public ContentValues getContentValues() { return new ContentValues(); }
}
