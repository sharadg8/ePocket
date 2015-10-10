package com.sharad.epocket;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sharad.deckview.views.DeckChildView;
import com.sharad.deckview.views.DeckView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    // View that stacks its children like a deck of cards
    DeckView<Datum> mDeckView;

    Drawable mDefaultHeaderIcon;
    ArrayList<Datum> mEntries;

    // Placeholder for when the image is being downloaded
    Bitmap mDefaultThumbnail;

    // Retain position on configuration change
    // imageSize to pass to http://lorempixel.com
    int scrollToChildIndex = -1, imageSize = 500;

    // SavedInstance bundle keys
    final String CURRENT_SCROLL = "current.scroll", CURRENT_LIST = "current.list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initFab();

        mDeckView = (DeckView) findViewById(R.id.deckview);
        mDefaultThumbnail = BitmapFactory.decodeResource(getResources(),
                R.drawable.default_thumbnail);
        mDefaultHeaderIcon = getResources().getDrawable(R.drawable.default_header_icon);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(CURRENT_LIST)) {
                mEntries = savedInstanceState.getParcelableArrayList(CURRENT_LIST);
            }

            if (savedInstanceState.containsKey(CURRENT_SCROLL)) {
                scrollToChildIndex = savedInstanceState.getInt(CURRENT_SCROLL);
            }
        }

        if (mEntries == null) {
            mEntries = new ArrayList<>();

            int[] palette = getResources().getIntArray(R.array.palette);
            Random r = new Random();
            for (int i = 0; i < 10; i++) {
                Datum datum = new Datum();
                datum.id = generateUniqueKey();
                datum.color = palette[r.nextInt(palette.length)];
                mEntries.add(datum);
            }
        }

        // Callback implementation
        DeckView.Callback<Datum> deckViewCallback = new DeckView.Callback<Datum>() {
            @Override
            public ArrayList<Datum> getData() {
                return mEntries;
            }

            @Override
            public void loadViewData(WeakReference<DeckChildView<Datum>> dcv, Datum item) {
                loadViewDataInternal(item, dcv);
            }

            @Override
            public void unloadViewData(Datum item) {
                //Picasso.with(MainActivity.this).cancelRequest(item.target);
            }

            @Override
            public void onViewDismissed(Datum item) {
                mEntries.remove(item);
                mEntries.add(0, item);
                mDeckView.notifyDataSetChanged();
            }

            @Override
            public void onItemClick(Datum item) {
                Toast.makeText(MainActivity.this,
                        "Item with id: '" + item.id + "' clicked",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNoViewsToDeck() {
                Toast.makeText(MainActivity.this,
                        "No views to show",
                        Toast.LENGTH_SHORT).show();
            }
        };

        mDeckView.initialize(deckViewCallback);

        if (scrollToChildIndex != -1) {
            mDeckView.post(new Runnable() {
                @Override
                public void run() {
                    // Restore scroll position
                    mDeckView.scrollToChild(scrollToChildIndex);
                }
            });
        }
    }

    private void initFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabButton);
        fab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Intent intent = new Intent(MainActivity.this, EditorActivity.class);

                    View movingView = findViewById(R.id.appBarLayout);
                    Pair<View, String> pair1 = Pair.create(movingView, movingView.getTransitionName());

                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            MainActivity.this, pair1
                    );
                    ActivityCompat.startActivity(MainActivity.this, intent, options.toBundle());
                }
                return true;
            }
        });
    }

    private void showColorPicker() {
        final AlertDialog.Builder colorPicker = new AlertDialog.Builder(this);
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View viewLayout = inflater.inflate(R.layout.color_picker_dialog,
                (ViewGroup) findViewById(R.id.color_picker_dialog));
        final View colorView = viewLayout.findViewById(R.id.color_view);
        int[] palette = getResources().getIntArray(R.array.palette);
        Random r = new Random();
        colorView.setBackgroundColor(palette[r.nextInt(palette.length)]);

        colorPicker.setView(viewLayout);

        //  SeekBar red
        final SeekBar seekRed = (SeekBar) viewLayout.findViewById(R.id.color_bar_red);
        final SeekBar seekGreen = (SeekBar) viewLayout.findViewById(R.id.color_bar_green);
        final SeekBar seekBlue = (SeekBar) viewLayout.findViewById(R.id.color_bar_blue);

        seekRed.setProgress(Color.red(((ColorDrawable) colorView.getBackground()).getColor()));
        seekRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView valueRed = (TextView) viewLayout.findViewById(R.id.color_value_red);
                int color = ((ColorDrawable)colorView.getBackground()).getColor();
                colorView.setBackgroundColor(Color.rgb(progress, Color.green(color), Color.blue(color)));

                updateColorBarValue(seekRed, valueRed);
                updateColorBars(seekRed, seekGreen, seekBlue, color);
            }

            public void onStartTrackingTouch(SeekBar arg0) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //  SeekBar green
        seekGreen.setProgress(Color.green(((ColorDrawable) colorView.getBackground()).getColor()));
        seekGreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView valueGreen = (TextView) viewLayout.findViewById(R.id.color_value_green);
                int color = ((ColorDrawable)colorView.getBackground()).getColor();
                colorView.setBackgroundColor(Color.rgb(Color.red(color), progress, Color.blue(color)));

                updateColorBarValue(seekGreen, valueGreen);
                updateColorBars(seekRed, seekGreen, seekBlue, color);
            }
            public void onStartTrackingTouch(SeekBar arg0) { }
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        //  SeekBar blue
        seekBlue.setProgress(Color.blue(((ColorDrawable) colorView.getBackground()).getColor()));
        seekBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView valueBlue = (TextView) viewLayout.findViewById(R.id.color_value_blue);
                int color = ((ColorDrawable) colorView.getBackground()).getColor();
                colorView.setBackgroundColor(Color.rgb(Color.red(color), Color.green(color), progress));

                updateColorBarValue(seekBlue, valueBlue);
                updateColorBars(seekRed, seekGreen, seekBlue, color);
            }

            public void onStartTrackingTouch(SeekBar arg0) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // Button OK
        colorPicker.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Datum datum = new Datum();
                        datum.id = generateUniqueKey();
                        datum.color = ((ColorDrawable) colorView.getBackground()).getColor();
                        mEntries.add(datum);
                        mDeckView.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
        // Button CANCEL
        colorPicker.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        colorPicker.create();
        colorPicker.show();
    }

    private void updateColorBars(SeekBar seekRed, SeekBar seekGreen, SeekBar seekBlue, int color) {
        /*
        float seekHeight = (float)(seekRed.getMeasuredHeight() - seekRed.getPaddingTop() - seekRed.getPaddingBottom());
        int padding = (int)(seekHeight * 0.34);

        int[] colorsRed = new int[2];
        colorsRed[0] = Color.rgb(0, Color.green(color), Color.blue(color));
        colorsRed[1] = Color.rgb(255, Color.green(color), Color.blue(color));
        GradientDrawable backColorRed = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colorsRed);
        InsetDrawable insetRed = new InsetDrawable(backColorRed, 0, padding, 0, padding);
        LayerDrawable progressDrawableRed = (LayerDrawable)seekRed.getProgressDrawable();
        progressDrawableRed.setDrawableByLayerId(android.R.id.secondaryProgress, insetRed);
        seekRed.postInvalidate();

        int[] colorsGreen = new int[2];
        colorsGreen[0] = Color.rgb(Color.red(color), 0, Color.blue(color));
        colorsGreen[1] = Color.rgb(Color.red(color), 255, Color.blue(color));
        GradientDrawable backColorGreen = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colorsGreen);
        InsetDrawable insetGreen = new InsetDrawable(backColorGreen, 0, padding, 0, padding);
        LayerDrawable progressDrawableGreen = (LayerDrawable)seekGreen.getProgressDrawable();
        progressDrawableGreen.setDrawableByLayerId(android.R.id.secondaryProgress, insetGreen);
        seekGreen.postInvalidate();

        int[] colorsBlue = new int[2];
        colorsBlue[0] = Color.rgb(Color.red(color), Color.green(color), 0);
        colorsBlue[1] = Color.rgb(Color.red(color), Color.green(color), 255);
        GradientDrawable backColorBlue = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colorsBlue);
        InsetDrawable insetBlue = new InsetDrawable(backColorBlue, 0, padding, 0, padding);
        LayerDrawable progressDrawableBlue = (LayerDrawable)seekBlue.getProgressDrawable();
        progressDrawableBlue.setDrawableByLayerId(android.R.id.secondaryProgress, insetBlue);
        seekBlue.postInvalidate();
        */
    }

    private void updateColorBarValue(SeekBar seek, TextView text) {
        int progress = seek.getProgress();
        float seekWidth = (float)(seek.getMeasuredWidth() - seek.getPaddingLeft() - seek.getPaddingRight());
        Paint paint = new Paint();
        Rect bounds = new Rect();
        text.setText(String.valueOf(progress));
        paint.setTextSize(text.getTextSize());
        paint.getTextBounds(text.getText().toString(), 0, text.getText().toString().length(), bounds);
        int seek_label_pos = (int)(seekWidth * ((float)progress / seek.getMax())) + seek.getPaddingLeft() - (bounds.width()/2);
        text.setX(seek_label_pos);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    void loadViewDataInternal(final Datum datum,
                              final WeakReference<DeckChildView<Datum>> weakView) {
        if (weakView.get() != null) {
            weakView.get().onDataLoaded(datum, null, datum.color);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save current scroll and the list
        int currentChildIndex = mDeckView.getCurrentChildIndex();
        outState.putInt(CURRENT_SCROLL, currentChildIndex);
        outState.putParcelableArrayList(CURRENT_LIST, mEntries);

        super.onSaveInstanceState(outState);
    }

    // Generates a key that will remain unique
    // during the application's lifecycle
    private static int generateUniqueKey() {
        return ++KEY;
    }

    private static int KEY = 0;
}
