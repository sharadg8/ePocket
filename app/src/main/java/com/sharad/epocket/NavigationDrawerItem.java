package com.sharad.epocket;

import com.sharad.epocket.utils.BaseFragment;

/**
 * Created by Sharad on 16-Jun-16.
 */

public class NavigationDrawerItem {
    private String name;
    private int icon;
    private BaseFragment fragment;

    public NavigationDrawerItem(String name, int icon, BaseFragment fragment) {
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

    public BaseFragment getFragment() { return fragment; }
}
