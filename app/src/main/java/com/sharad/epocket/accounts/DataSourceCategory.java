package com.sharad.epocket.accounts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sharad.epocket.database.CategoryTable;
import com.sharad.epocket.database.DatabaseHelper;

import java.util.ArrayList;

/**
 * Created by Sharad on 24-Jul-16.
 */

public class DataSourceCategory {
    DatabaseHelper helper;

    public DataSourceCategory(Context context) {
        helper = new DatabaseHelper(context);
    }

    public long insertCategory(ICategory iCategory) {
        ICategory.updateCategory(iCategory);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues content = iCategory.getContentValues();

        // Insert it into the database.
        long id = db.insert(CategoryTable.TABLE_CATEGORY, null, content);
        helper.close();
        return id;
    }

    public boolean updateCategory(ICategory iCategory) {
        ICategory.updateCategory(iCategory);
        SQLiteDatabase db = helper.getWritableDatabase();
        String where = CategoryTable.COLUMN_ID + "=" + iCategory.getId();

        // Create row's data:
        ContentValues content = iCategory.getContentValues();

        // Update it into the database.
        boolean status = db.update(CategoryTable.TABLE_CATEGORY, content, where, null) != 0;
        helper.close();
        return status;
    }

    public boolean deleteCategory(long rowId) {
        ICategory.removeCategory(rowId);
        SQLiteDatabase db = helper.getWritableDatabase();
        String where = CategoryTable.COLUMN_ID + "=" + rowId;
        boolean status = db.delete(CategoryTable.TABLE_CATEGORY, where, null) != 0;
        helper.close();
        return status;
    }

    public ICategory getCategory(long rowId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        ICategory category = null;
        String where = CategoryTable.COLUMN_ID + "=" + rowId;
        Cursor c = 	db.query(true, CategoryTable.TABLE_CATEGORY, null,
                where, null, null, null, null, null);
        if(c.moveToFirst()) {
            category = new ICategory(c);
        }

        helper.close();
        return category;
    }

    public void getCategories(ArrayList<ICategory> categories) {
        getCategories(categories, null);
    }

    public void getCategories(ArrayList<ICategory> categories, String where) {
    SQLiteDatabase db = helper.getReadableDatabase();
        categories.clear();
        Cursor c = 	db.query(true, CategoryTable.TABLE_CATEGORY, null,
                where, null, null, null, null, null);
        if (c != null) {
            if(c.moveToFirst()) {
                do {
                    categories.add(new ICategory(c));
                } while (c.moveToNext());
            }
        }
        helper.close();
    }
}
