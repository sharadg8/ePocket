package com.sharad.epocket.accounts;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharad.epocket.R;
import com.sharad.epocket.utils.recurrencepicker.EventRecurrence;
import com.sharad.epocket.utils.recurrencepicker.RecurrencePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddTransactionActivity extends AppCompatActivity implements
        RecurrencePickerDialog.OnRecurrenceSetListener {
    private static final String TAG = "AddTransactionActivity";

    private static final String FRAG_TAG_RECUR_PICKER = "recurrencePickerDialogFragment";

    private final int TAB_EXPENSE = 0;
    private final int TAB_INCOME = 1;
    private final int TAB_TRANSFER = 2;

    private int mYear;
    private int mMonth;
    private int mDay;

    private String mRrule;
    private EventRecurrence mEventRecurrence = new EventRecurrence();
    private Time mStartTime;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private ImageView mCategoryImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mStartTime = new Time();

        final TextView fromCurrency = (TextView) findViewById(R.id.from_currency);
        final EditText fromAmount = (EditText) findViewById(R.id.from_amount);
        final TextView toCurrency = (TextView) findViewById(R.id.transfer_to_currency);
        final EditText toAmount = (EditText) findViewById(R.id.transfer_to_amount);
        final View toLayout = findViewById(R.id.layout_transfer_amount);
        final View toIcon = findViewById(R.id.transfer_icon);
        final View fromSwitch = findViewById(R.id.from_switch);
        final Button date = (Button) findViewById(R.id.date);
        final Button repeat = (Button) findViewById(R.id.repeat);
        final Button toAccount = (Button) findViewById(R.id.transfer_account);

        final SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        Calendar currentDate = Calendar.getInstance();
        mYear = currentDate.get(Calendar.YEAR);
        mMonth = currentDate.get(Calendar.MONTH);
        mDay = currentDate.get(Calendar.DAY_OF_MONTH);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog mDatePicker = new DatePickerDialog(AddTransactionActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int year, int month, int day) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(year, month, day);
                        if(DateUtils.isToday(cal.getTimeInMillis())) {
                            date.setText("TODAY");
                        } else {
                            date.setText(df.format(cal.getTime()));
                        }
                        mYear = year;
                        mMonth = month;
                        mDay = day;
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.show();
            }
        });

        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putLong(RecurrencePickerDialog.BUNDLE_START_TIME_MILLIS, mStartTime.toMillis(false));
                b.putString(RecurrencePickerDialog.BUNDLE_TIME_ZONE, mStartTime.timezone);

                // TODO may be more efficient to serialize and pass in EventRecurrence
                b.putString(RecurrencePickerDialog.BUNDLE_RRULE, mRrule);

                FragmentManager fm = getSupportFragmentManager();
                RecurrencePickerDialog rpd = (RecurrencePickerDialog) fm.findFragmentByTag(FRAG_TAG_RECUR_PICKER);
                if (rpd != null) {
                    rpd.dismiss();
                }
                rpd = new RecurrencePickerDialog();
                rpd.setArguments(b);
                rpd.setOnRecurrenceSetListener(AddTransactionActivity.this);
                rpd.show(fm, FRAG_TAG_RECUR_PICKER);
            }
        });

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
                                               @Override
                                               public void onTabSelected(TabLayout.Tab tab) {
                                                   super.onTabSelected(tab);
                                                   switch (tab.getPosition()) {
                                                       case TAB_EXPENSE:
                                                           toLayout.setVisibility(View.GONE);
                                                           toIcon.setVisibility(View.GONE);
                                                           fromSwitch.setVisibility(View.VISIBLE);
                                                           repeat.setVisibility(View.VISIBLE);
                                                           toAccount.setVisibility(View.GONE);
                                                           break;
                                                       case TAB_INCOME:
                                                           toLayout.setVisibility(View.GONE);
                                                           toIcon.setVisibility(View.GONE);
                                                           fromSwitch.setVisibility(View.VISIBLE);
                                                           repeat.setVisibility(View.VISIBLE);
                                                           toAccount.setVisibility(View.GONE);
                                                           break;
                                                       case TAB_TRANSFER:
                                                           toLayout.setVisibility(View.VISIBLE);
                                                           toIcon.setVisibility(View.VISIBLE);
                                                           fromSwitch.setVisibility(View.GONE);
                                                           repeat.setVisibility(View.GONE);
                                                           toAccount.setVisibility(View.VISIBLE);
                                                           break;
                                                   }
                                               }
                                           }
        );

        mCategoryImage = (ImageView) findViewById(R.id.selected_category);
    }

    @Override
    public void onRecurrenceSet(String rrule) {
        Log.d(TAG, "Old rrule:" + mRrule);
        Log.d(TAG, "New rrule:" + rrule);
        mRrule = rrule;
        if (mRrule != null) {
            mEventRecurrence.parse(mRrule);
        }
        //populateRepeats();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_transaction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return AddTransactionFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case TAB_EXPENSE:
                    return "EXPENSE";
                case TAB_INCOME:
                    return "INCOME";
                case TAB_TRANSFER:
                    return "TRANSFER";
            }
            return null;
        }
    }
}
