package com.sharad.epocket;

import android.support.v4.app.Fragment;

/**
 * Created by Sharad on 16-Jun-16.
 */

public class NavigationDrawerItem {
    private String name;
    private int icon;
    private Fragment fragment;

    public NavigationDrawerItem(String name, int icon, Fragment fragment) {
        this.name = name;
        this.icon = icon;
        this.fragment = fragment;
    }

    public String getName() {
        return name;
    }

    public int getIcon() {
        return icon;
    }

    public Fragment getFragment() { return fragment; }
}
