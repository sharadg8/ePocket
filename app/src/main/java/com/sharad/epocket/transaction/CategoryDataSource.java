package com.sharad.epocket.transaction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sharad.epocket.database.DatabaseAdapter;

import java.util.ArrayList;

/**
 * Created by Sharad on 24-Jul-16.
 */

public class CategoryDataSource extends DatabaseAdapter {
    public CategoryDataSource(Context context) {
        super(context);
    }

    private ContentValues getContentValues(Category category) {
        // Create row's data:
        ContentValues content = new ContentValues();
        content.put(KEY_CATEGORY_TITLE,       category.getTitle());
        content.put(KEY_CATEGORY_COUNT,       category.getUsageCount());
        content.put(KEY_CATEGORY_IMAGE_IDX,   category.getImageIndex());
        content.put(KEY_CATEGORY_COLOR,       category.getColor());
        content.put(KEY_CATEGORY_TYPE,        category.getType());

        return content;
    }

    public long insertCategory(Category category) {
        SQLiteDatabase db = openDb();

        ContentValues content = getContentValues(category);

        // Insert it into the database.
        long id = db.insert(DATABASE_TABLE_CATEGORY, null, content);
        closeDb();
        return id;
    }

    public boolean updateCategory(long rowId, Category category) {
        SQLiteDatabase db = openDb();
        String where = KEY_CATEGORY_ROWID + "=" + rowId;

        // Create row's data:
        ContentValues content = getContentValues(category);

        // Update it into the database.
        boolean status = db.update(DATABASE_TABLE_CATEGORY, content, where, null) != 0;
        closeDb();
        return status;
    }

    public boolean deleteCategory(long rowId) {
        SQLiteDatabase db = openDb();
        String where = KEY_CATEGORY_ROWID + "=" + rowId;
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

    private Category parseCategory(Cursor c) {
        long id 	   = c.getLong(c.getColumnIndex(KEY_CATEGORY_ROWID));
        String title   = c.getString(c.getColumnIndex(KEY_CATEGORY_TITLE));
        int imageIndex = c.getInt(c.getColumnIndex(KEY_CATEGORY_IMAGE_IDX));
        int color      = c.getInt(c.getColumnIndex(KEY_CATEGORY_COLOR));
        int type       = c.getInt(c.getColumnIndex(KEY_CATEGORY_TYPE));
        int usageCount = c.getInt(c.getColumnIndex(KEY_CATEGORY_COUNT));

        Category category = new Category(id, imageIndex, color, title, type, usageCount);
        return category;
    }

    public Category getCategory(long rowId) {
        SQLiteDatabase db = openDb();
        Category category = null;
        String where = KEY_CATEGORY_ROWID + "=" + rowId;
        Cursor c = 	db.query(true, DATABASE_TABLE_CATEGORY, ALL_KEYS_CATEGORY,
                where, null, null, null, null, null);
        if(c.moveToFirst()) {
            category = parseCategory(c);
        }

        closeDb();
        return category;
    }

    public void getCategories(ArrayList<Category> categories) {
        getCategories(categories, null);
    }

    public void getCategories(ArrayList<Category> categories, String where) {
        SQLiteDatabase db = openDb();
        categories.clear();
        Cursor c = 	db.query(true, DATABASE_TABLE_CATEGORY, ALL_KEYS_CATEGORY,
                where, null, null, null, null, null);
        if (c != null) {
            if(c.moveToFirst()) {
                do {
                    categories.add(parseCategory(c));
                } while (c.moveToNext());
            }
        }
        closeDb();
    }
}
