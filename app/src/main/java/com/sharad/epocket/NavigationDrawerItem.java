package com.sharad.epocket;

/**
 * Created by Sharad on 16-Jun-16.
 */

public class NavigationDrawerItem {
    private String name;
    private int icon;

    public NavigationDrawerItem(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
