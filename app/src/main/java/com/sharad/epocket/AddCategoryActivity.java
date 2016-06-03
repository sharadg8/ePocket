package com.sharad.epocket;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.sharad.widgets.CircleButton;


public class AddCategoryActivity extends ActionBarActivity {
    private Toolbar mToolbar;
    private RelativeLayout mColorPicker;
    private RelativeLayout mIconPicker;

    private CircleButton mColorButtons[];
    private ImageButton mIconButtons[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        initToolbar();
        setupColorPicker();
        setupIconPicker();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton myFab = (FloatingActionButton) this.findViewById(R.id.fabButton);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_delete:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupColorPicker() {
        mColorPicker = (RelativeLayout) findViewById(R.id.colorPicker);

        RelativeLayout relativeLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        relativeLayout.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        mColorPicker.removeAllViews();
        mColorPicker.addView(relativeLayout, params);

        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        linearLayout.setOrientation(LinearLayout.VERTICAL);
        relativeLayout.addView(linearLayout, params1);

        int[] palette = getResources().getIntArray(R.array.light_palette);
        mColorButtons = new CircleButton[palette.length];
        for (int i = 0; i < (palette.length/6); i++) {
            LinearLayout linearLayout1 = new LinearLayout(this);
            linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.addView(linearLayout1, params1);
            for (int j = 0; j < 6; j++) {
                CircleButton cb = newPaletteButton(palette[i * 6 + j]);
                mColorButtons[i * 6 + j] = cb;
                linearLayout1.addView(cb);
            }
        }
    }

    private CircleButton newPaletteButton(final int color) {
        final CircleButton cb = new CircleButton(this);
        int size = getResources().getDimensionPixelSize(R.dimen.color_circle_diameter);
        cb.setLayoutParams(new LinearLayout.LayoutParams(size, size));
        cb.setColor(color);
        cb.setChecked(false);
        cb.setImageResource(R.drawable.ic_check_white_24dp);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int[] attrs = new int[]{android.R.attr.selectableItemBackgroundBorderless};
            cb.setBackground(obtainStyledAttributes(attrs).getDrawable(0));
        }
        cb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                int[] location = new int[2];
                cb.getLocationOnScreen(location);
                /*mColorId = color;
                mEditorView.setBackgroundColor(mColorId);
                mMaskView.setBackgroundColor(mColorId);
                hidePopupView(mColorPicker, new Point(location[0] + 60, location[1] - 100));*/
            }
        });
        return cb;
    }

    private void setupIconPicker() {
        mIconPicker = (RelativeLayout) findViewById(R.id.iconPicker);

        RelativeLayout relativeLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        relativeLayout.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        mIconPicker.removeAllViews();
        mIconPicker.addView(relativeLayout, params);

        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        linearLayout.setOrientation(LinearLayout.VERTICAL);
        relativeLayout.addView(linearLayout, params1);

        TypedArray ids = getResources().obtainTypedArray(R.array.icon_resource);

        mIconButtons = new ImageButton[ids.length()];
        for (int i = 0; i < (ids.length()/6); i++) {
            LinearLayout linearLayout1 = new LinearLayout(this);
            linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.addView(linearLayout1, params1);
            for (int j = 0; j < 6; j++) {
                int index = ids.getResourceId(i * 6 + j, R.mipmap.ic_card_giftcard_black_24dp);
                ImageButton ib = newLabelButton(index);
                mIconButtons[i * 6 + j] = ib;
                linearLayout1.addView(ib);
            }
        }
        ids.recycle();
    }

    private ImageButton newLabelButton(final int label) {
        final ImageButton ib = new ImageButton(this);
        ib.setImageResource(label);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int[] attrs = new int[]{android.R.attr.selectableItemBackgroundBorderless};
            ib.setBackground(obtainStyledAttributes(attrs).getDrawable(0));
        } else {
            int[] attrs = new int[]{android.R.attr.selectableItemBackground};
            ib.setBackground(obtainStyledAttributes(attrs).getDrawable(0));
        }

        ib.setColorFilter(getResources().getColor(R.color.icons_dark));
        ib.setOnClickListener(new View.OnClickListener() {
            public void onClick(View button) {
                /*int[] location = new int[2];
                ib.getLocationOnScreen(location);
                mLabelId = Event.getLabelId(label);
                mFavorite.setImageResource(label);
                hidePopupView(mLabelPicker, new Point(location[0] + 60, location[1] - 100));*/
            }
        });
        return ib;
    }
}
