package com.sharad.epocket.accounts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sharad.epocket.database.ContentConstant;
import com.sharad.epocket.database.DatabaseAdapter;

import java.util.ArrayList;

/**
 * Created by Sharad on 24-Jul-16.
 */

public class DataSourceCategory extends DatabaseAdapter {
    public DataSourceCategory(Context context) {
        super(context);
    }

    public long insertCategory(ICategory iCategory) {
        ICategory.updateCategory(iCategory);
        SQLiteDatabase db = openDb();

        ContentValues content = iCategory.getContentValues();

        // Insert it into the database.
        long id = db.insert(DATABASE_TABLE_CATEGORY, null, content);
        closeDb();
        return id;
    }

    public boolean updateCategory(ICategory iCategory) {
        ICategory.updateCategory(iCategory);
        SQLiteDatabase db = openDb();
        String where = ContentConstant.KEY_CATEGORY_ROWID + "=" + iCategory.getId();

        // Create row's data:
        ContentValues content = iCategory.getContentValues();

        // Update it into the database.
        boolean status = db.update(DATABASE_TABLE_CATEGORY, content, where, null) != 0;
        closeDb();
        return status;
    }

    public boolean deleteCategory(long rowId) {
        ICategory.removeCategory(rowId);
        SQLiteDatabase db = openDb();
        String where = ContentConstant.KEY_CATEGORY_ROWID + "=" + rowId;
        boolean status = db.delete(DATABASE_TABLE_CATEGORY, where, null) != 0;
        closeDb();
        return status;
    }

    public void deleteAllCategories() {
        SQLiteDatabase db = openDb();
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_CATEGORY);
        db.execSQL(DATABASE_CREATE_SQL_CATEGORY);
        closeDb();
    }

    public ICategory getCategory(long rowId) {
        SQLiteDatabase db = openDb();
        ICategory category = null;
        String where = ContentConstant.KEY_CATEGORY_ROWID + "=" + rowId;
        Cursor c = 	db.query(true, DATABASE_TABLE_CATEGORY, ContentConstant.ALL_KEYS_CATEGORY,
                where, null, null, null, null, null);
        if(c.moveToFirst()) {
            category = new ICategory(c);
        }

        closeDb();
        return category;
    }

    public void getCategories(ArrayList<ICategory> categories) {
        getCategories(categories, null);
    }

    public void getCategories(ArrayList<ICategory> categories, String where) {
        SQLiteDatabase db = openDb();
        categories.clear();
        Cursor c = 	db.query(true, DATABASE_TABLE_CATEGORY, ContentConstant.ALL_KEYS_CATEGORY,
                where, null, null, null, null, null);
        if (c != null) {
            if(c.moveToFirst()) {
                do {
                    categories.add(new ICategory(c));
                } while (c.moveToNext());
            }
        }
        closeDb();
    }
}
