package com.sharad.utils.viewpager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sharad on 12-Jun-16.
 */

public class HollyViewPagerBus {
    public static Map<Context, HollyViewPager> map = new HashMap<>();

    public static void register(Context context, HollyViewPager hollyViewPager) {
        map.put(context, hollyViewPager);
    }

    public static void unregister(Context context){
        map.remove(context);
    }

    public static void registerRecyclerView(Context context, RecyclerView recyclerView) {
        HollyViewPager hollyViewPager = map.get(context);
        if(hollyViewPager != null)
            hollyViewPager.registerRecyclerView(recyclerView);
    }

    public static HollyViewPager get(Context context){
        return map.get(context);
    }
}
