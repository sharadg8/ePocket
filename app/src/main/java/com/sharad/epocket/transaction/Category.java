package com.sharad.epocket.transaction;

/**
 * Created by Sharad on 24-Jul-16.
 */

public class Category {
    private long id;
    private int imageIndex;
    private int color;
    private String title;
    private int usageCount;
    private int type;

    public static final int CATEGORY_TYPE_EXPENSE  = 0;
    public static final int CATEGORY_TYPE_INCOME   = 1;
    public static final int CATEGORY_TYPE_TRANSFER = 2;

    public Category(long id, int imageIndex, int color, String title, int type, int usageCount) {
        this.id = id;
        this.imageIndex = (imageIndex < CategoryImageList.imageResource.length) ? imageIndex : 0;
        this.color = color;
        this.title = title;
        this.type = type;
        this.usageCount = usageCount;
    }

    public long getId() {           return id;              }
    public int getColor() {         return color;           }
    public int getImageIndex() {    return imageIndex;      }
    public String getTitle() {      return title;           }
    public int getType() {          return type;            }
    public int getUsageCount() {    return usageCount;      }
    public int getImageResource() { return CategoryImageList.imageResource[imageIndex]; }

    public void setId(long id) {                this.id = id;                  }
    public void setColor(int color) {           this.color = color;            }
    public void setTitle(String title) {        this.title = title;            }
    public void setType(int type) {             this.type = type;              }
    public void setUsageCount(int usageCount) { this.usageCount = usageCount;  }
    public void setImageIndex(int imageIndex) {
        this.imageIndex = (imageIndex < CategoryImageList.imageResource.length) ? imageIndex : 0;
    }
}
