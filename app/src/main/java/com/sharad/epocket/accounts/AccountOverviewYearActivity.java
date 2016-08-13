package com.sharad.epocket.accounts;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.sharad.epocket.R;
import com.sharad.epocket.utils.Constant;
import com.sharad.epocket.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AccountOverviewYearActivity extends AppCompatActivity {
    private IAccount iAccount = null;
    private ViewPager viewPager = null;
    private YearPagerAdapter yearPagerAdapter = null;
    private ArrayList<Long> yearList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_overview);
        Utils.setTaskDescription(this);

        Bundle extras = getIntent().getExtras();
        long accountId = extras.getLong(Constant.ARG_ACCOUNT_NUMBER_LONG, Constant.INVALID_ID);
        Calendar calendar = Calendar.getInstance();
        long timeInMillis = extras.getLong(Constant.ARG_TIME_IN_MS_LONG, calendar.getTimeInMillis());

        yearList = new ArrayList<>();

        DataSourceAccount dataSourceAccount = new DataSourceAccount(this);
        iAccount = dataSourceAccount.getAccount(accountId);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(iAccount != null) {
            setTitle(iAccount.getTitle());
            viewPager = (ViewPager) findViewById(R.id.viewPager);

            yearPagerAdapter = new YearPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(yearPagerAdapter);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset,
                                           int positionOffsetPixels) { }

                @Override
                public void onPageSelected(int position) {
                    updateYear();
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });

            yearList.add(timeInMillis);

            AccountManager manager = AccountManager.getInstance();
            if(manager.hasAnyTransactionBeforeYear(this, iAccount, timeInMillis)) {
                calendar.setTimeInMillis(timeInMillis);
                calendar.add(Calendar.YEAR, -1);
                yearList.add(0, calendar.getTimeInMillis());
            }
            if(manager.hasAnyTransactionAfterYear(this, iAccount, timeInMillis)) {
                calendar.setTimeInMillis(timeInMillis);
                calendar.add(Calendar.YEAR, +1);
                yearList.add(calendar.getTimeInMillis());
            }

            yearPagerAdapter.notifyDataSetChanged();

            updateYear();
            viewPager.setCurrentItem(yearList.indexOf(timeInMillis), true);

            ImageButton previous = (ImageButton) findViewById(R.id.previous);
            previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem((viewPager.getCurrentItem() - 1), true);
                }
            });

            Button date = (Button) findViewById(R.id.date);
            date.setClickable(false);

            ImageButton next = (ImageButton) findViewById(R.id.next);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem((viewPager.getCurrentItem() + 1), true);
                }
            });
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            AccountManager manager = AccountManager.getInstance();
            if(viewPager.getCurrentItem() < 2) {
                if (manager.hasAnyTransactionBeforeYear(getBaseContext(), iAccount, yearList.get(0))) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(yearList.get(0));
                    calendar.add(Calendar.YEAR, -1);
                    if (Constant.INVALID_ID == yearList.indexOf(calendar.getTimeInMillis())) {
                        yearList.add(0, calendar.getTimeInMillis());
                        yearPagerAdapter.notifyDataSetChanged();
                        viewPager.setCurrentItem((viewPager.getCurrentItem() + 1), false);
                    }
                }
            }

            if(viewPager.getCurrentItem() > (yearList.size() - 3)) {
                if (manager.hasAnyTransactionAfterYear(getBaseContext(), iAccount, yearList.get(yearList.size() - 1))) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(yearList.get(yearList.size() - 1));
                    calendar.add(Calendar.YEAR, +1);
                    if (Constant.INVALID_ID == yearList.indexOf(calendar.getTimeInMillis())) {
                        yearList.add(calendar.getTimeInMillis());
                        yearPagerAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class YearPagerAdapter extends FragmentStatePagerAdapter {

        public YearPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return yearList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return AccountOverviewYearFragment.newInstance(iAccount.getId(), yearList.get(position));
        }

        @Override
        public int getItemPosition(Object item) {
            return POSITION_NONE;
        }
    }

    private void updateYear() {
        findViewById(R.id.previous).setVisibility((viewPager.getCurrentItem() > 0) ? View.VISIBLE : View.INVISIBLE);
        findViewById(R.id.next).setVisibility((viewPager.getCurrentItem() < yearList.size()-1) ? View.VISIBLE : View.INVISIBLE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(yearList.get(viewPager.getCurrentItem()));
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        Button date = (Button) findViewById(R.id.date);
        date.setText(df.format(calendar.getTime()));

        /* Load more data */
        new Handler().postDelayed(runnable, 500);
    }
}
