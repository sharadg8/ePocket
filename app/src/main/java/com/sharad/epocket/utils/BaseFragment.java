package com.sharad.epocket.utils;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.View;

import com.sharad.epocket.MainActivity;

/**
 * Created by Sharad on 27-Jul-16.
 */

public class BaseFragment extends Fragment {
    protected FloatingActionButton mFab;

    public void onFabClick(View view){
        // Do nothing here , only in derived classes
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Activity activity = getActivity();
        if (activity instanceof MainActivity) {
            final MainActivity mainActivity = (MainActivity) activity;
            mFab = mainActivity.getFab();
        }
    }

    public void setFabAppearance() {
        // Do nothing here , only in derived classes
    }
}
