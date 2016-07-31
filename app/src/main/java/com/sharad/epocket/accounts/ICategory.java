package com.sharad.epocket.accounts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.sharad.epocket.R;
import com.sharad.epocket.database.ContentConstant;
import com.sharad.epocket.utils.Constant;
import com.sharad.epocket.utils.Item;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

/**
 * Created by Sharad on 24-Jul-16.
 */

public class ICategory extends Item{
    private int imageIndex;
    private int color;
    private String title;
    private int usageCount;
    private int type;

    public static final int CATEGORY_TYPE_EXPENSE  = 0;
    public static final int CATEGORY_TYPE_INCOME   = 1;
    public static final int CATEGORY_TYPE_TRANSFER = 2;

    public ICategory(int imageIndex) {
        this(Constant.INVALID_ID, imageIndex, 0, "", 0, 0);
    }
    public ICategory(long id, int imageIndex, int color, String title, int type, int usageCount) {
        super(id);
        this.imageIndex = (imageIndex < CategoryImageList.imageResource.length) ? imageIndex : 0;
        this.color = color;
        this.title = title;
        this.type = type;
        this.usageCount = usageCount;
    }

    public ICategory(Cursor c) {
        super(Constant.INVALID_ID);

        long id 	   = c.getLong(c.getColumnIndex(ContentConstant.KEY_CATEGORY_ROWID));
        String title   = c.getString(c.getColumnIndex(ContentConstant.KEY_CATEGORY_TITLE));
        int imageIndex = c.getInt(c.getColumnIndex(ContentConstant.KEY_CATEGORY_IMAGE_IDX));
        int color      = c.getInt(c.getColumnIndex(ContentConstant.KEY_CATEGORY_COLOR));
        int type       = c.getInt(c.getColumnIndex(ContentConstant.KEY_CATEGORY_TYPE));
        int usageCount = c.getInt(c.getColumnIndex(ContentConstant.KEY_CATEGORY_COUNT));

        this.id = id;
        this.imageIndex = (imageIndex < CategoryImageList.imageResource.length) ? imageIndex : 0;
        this.color = color;
        this.title = title;
        this.type = type;
        this.usageCount = usageCount;
    }

    @Override
    public ContentValues getContentValues() {
        // Create row's data:
        ContentValues content = new ContentValues();
        content.put(ContentConstant.KEY_CATEGORY_TITLE,     this.getTitle());
        content.put(ContentConstant.KEY_CATEGORY_COUNT,     this.getUsageCount());
        content.put(ContentConstant.KEY_CATEGORY_IMAGE_IDX, this.getImageIndex());
        content.put(ContentConstant.KEY_CATEGORY_COLOR,     this.getColor());
        content.put(ContentConstant.KEY_CATEGORY_TYPE,      this.getType());

        return content;
    }

    public int getColor() {         return color;           }
    public int getImageIndex() {    return imageIndex;      }
    public String getTitle() {      return title;           }
    public int getType() {          return type;            }
    public int getUsageCount() {    return usageCount;      }
    public int getImageResource() { return CategoryImageList.imageResource[imageIndex]; }

    public void setColor(int color) {           this.color = color;            }
    public void setTitle(String title) {        this.title = title;            }
    public void setType(int type) {             this.type = type;              }
    public void setUsageCount(int usageCount) { this.usageCount = usageCount;  }
    public void setImageIndex(int imageIndex) {
        this.imageIndex = (imageIndex < CategoryImageList.imageResource.length) ? imageIndex : 0;
    }

    public static class iComparator implements Comparator<ICategory> {
        @Override
        public int compare(ICategory o, ICategory o1) {
            return (o.getUsageCount() == o1.getUsageCount())
                    ? 0
                    : ((o.getUsageCount() < o1.getUsageCount()) ? 1 : -1);
        }
    }

    public static void getDefaultIncomeCategories(Context c, ArrayList<ICategory> list) {
        list.clear();

        list.add(new ICategory(0,  83, color(c), "Salary", ICategory.CATEGORY_TYPE_INCOME, 0));
        list.add(new ICategory(0,  20, color(c), "Gifts", ICategory.CATEGORY_TYPE_INCOME, 0));
        list.add(new ICategory(0,  69, color(c), "Shares & Equity", ICategory.CATEGORY_TYPE_INCOME, 0));
        list.add(new ICategory(0,   3, color(c), "Incentives & Allowances", ICategory.CATEGORY_TYPE_INCOME, 0));
        list.add(new ICategory(0,   1, color(c), "Savings", ICategory.CATEGORY_TYPE_INCOME, 0));
        list.add(new ICategory(0,  84, color(c), "Business", ICategory.CATEGORY_TYPE_INCOME, 0));
        list.add(new ICategory(0,  79, color(c), "Others", ICategory.CATEGORY_TYPE_INCOME, 0));
    }

    public static void getDefaultExpenseCategories(Context c, ArrayList<ICategory> list) {
        list.clear();

        list.add(new ICategory(0,   0, color(c), "Home", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,   3, color(c), "Personal", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,   5, color(c), "Electricity", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,   6, color(c), "Water", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,   7, color(c), "Utility", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  11, color(c), "Television", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  50, color(c), "Wifi & Broadband", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,   8, color(c), "Laundry", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,   9, color(c), "Kitchen", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  10, color(c), "Tools & Repair", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  12, color(c), "Gadgets & Accessories", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  14, color(c), "Beverages", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  15, color(c), "Coffee", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  16, color(c), "Smoke", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  17, color(c), "Food & Restaurant", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  18, color(c), "Pizza", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  19, color(c), "Celebrations", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  20, color(c), "Gifts", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  21, color(c), "Flowers & Decoration", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  22, color(c), "Grocery", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  25, color(c), "Shopping", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  80, color(c), "Clothes", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  23, color(c), "Bills", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  26, color(c), "Hospital", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  27, color(c), "Medicine", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  28, color(c), "Swimming", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  29, color(c), "Gym & Fitness", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  30, color(c), "Outdoor Sports", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  74, color(c), "Indoor Games", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  31, color(c), "Holidays", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  32, color(c), "Hotels", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  33, color(c), "Phone", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  34, color(c), "Fuel", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  35, color(c), "Flight", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  36, color(c), "Ferry & Cruise", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  37, color(c), "Bus", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  43, color(c), "Railway", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  82, color(c), "Tram & Metro", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  38, color(c), "Car", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  39, color(c), "Transport Goods", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  40, color(c), "Taxi & Rentals", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  41, color(c), "Bike", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  42, color(c), "Bicycle", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  44, color(c), "Parking", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  45, color(c), "Traffic & Fines", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  46, color(c), "Casino", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  47, color(c), "Movie", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  49, color(c), "Music", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  51, color(c), "Hobby", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  53, color(c), "Charity & Offerings", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  57, color(c), "Child Care", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  58, color(c), "Pets", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  68, color(c), "Education", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  73, color(c), "Insurance & Security", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  75, color(c), "Waste & Sewage", ICategory.CATEGORY_TYPE_EXPENSE, 0));
        list.add(new ICategory(0,  79, color(c), "Others", ICategory.CATEGORY_TYPE_EXPENSE, 0));
    }

    private static int color(Context context) {
        int colorList[] = context.getResources().getIntArray(R.array.light_palette);
        Random r = new Random();
        int index = r.nextInt(colorList.length - 1);
        return colorList[index];
    }
}
