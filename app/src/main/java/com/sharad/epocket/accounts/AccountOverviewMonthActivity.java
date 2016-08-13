package com.sharad.epocket.accounts;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.Pair;
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

public class AccountOverviewMonthActivity extends AppCompatActivity {
    private IAccount iAccount = null;
    private ViewPager viewPager = null;
    private MonthPagerAdapter monthPagerAdapter = null;
    private ArrayList<Long> monthList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_overview);
        Utils.setTaskDescription(this);

        Bundle extras = getIntent().getExtras();
        long accountId = extras.getLong(Constant.ARG_ACCOUNT_NUMBER_LONG, Constant.INVALID_ID);
        Calendar calendar = Calendar.getInstance();
        long timeInMillis = extras.getLong(Constant.ARG_TIME_IN_MS_LONG, calendar.getTimeInMillis());

        monthList = new ArrayList<>();

        DataSourceAccount dataSourceAccount = new DataSourceAccount(this);
        iAccount = dataSourceAccount.getAccount(accountId);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(iAccount != null) {
            setTitle(iAccount.getTitle());
            viewPager = (ViewPager) findViewById(R.id.viewPager);

            monthPagerAdapter = new MonthPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(monthPagerAdapter);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset,
                                           int positionOffsetPixels) { }

                @Override
                public void onPageSelected(int position) {
                    updateMonth();
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });

            monthList.add(timeInMillis);

            AccountManager manager = AccountManager.getInstance();
            if(manager.hasAnyTransactionBeforeMonth(this, iAccount, timeInMillis)) {
                calendar.setTimeInMillis(timeInMillis);
                calendar.add(Calendar.MONTH, -1);
                monthList.add(0, calendar.getTimeInMillis());
            }
            if(manager.hasAnyTransactionAfterMonth(this, iAccount, timeInMillis)) {
                calendar.setTimeInMillis(timeInMillis);
                calendar.add(Calendar.MONTH, +1);
                monthList.add(calendar.getTimeInMillis());
            }

            monthPagerAdapter.notifyDataSetChanged();

            updateMonth();
            viewPager.setCurrentItem(monthList.indexOf(timeInMillis), true);

            ImageButton previous = (ImageButton) findViewById(R.id.previous);
            previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem((viewPager.getCurrentItem() - 1), true);
                }
            });

            Button date = (Button) findViewById(R.id.date);
            date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AccountOverviewMonthActivity.this, AccountOverviewYearActivity.class);
                    intent.putExtra(Constant.ARG_ACCOUNT_NUMBER_LONG, iAccount.getId());
                    intent.putExtra(Constant.ARG_TIME_IN_MS_LONG, monthList.get(viewPager.getCurrentItem()));
                    startActivityForResult(intent, Constant.REQ_LIST_TRANSACTION_YEAR);
                    View movingView = findViewById(R.id.appBarLayout);
                    Pair<View, String> pair1 = Pair.create(movingView, movingView.getTransitionName());
                    //Pair<View, String> pair2 = Pair.create(view, view.getTransitionName());

                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            AccountOverviewMonthActivity.this, pair1/*, pair2*/
                    );
                    ActivityCompat.startActivity(AccountOverviewMonthActivity.this, intent, options.toBundle());
                }
            });

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
                if (manager.hasAnyTransactionBeforeMonth(getBaseContext(), iAccount, monthList.get(0))) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(monthList.get(0));
                    calendar.add(Calendar.MONTH, -1);
                    if (Constant.INVALID_ID == monthList.indexOf(calendar.getTimeInMillis())) {
                        monthList.add(0, calendar.getTimeInMillis());
                        monthPagerAdapter.notifyDataSetChanged();
                        viewPager.setCurrentItem((viewPager.getCurrentItem() + 1), false);
                    }
                }
            }

            if(viewPager.getCurrentItem() > (monthList.size() - 3)) {
                if (manager.hasAnyTransactionAfterMonth(getBaseContext(), iAccount, monthList.get(monthList.size() - 1))) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(monthList.get(monthList.size() - 1));
                    calendar.add(Calendar.MONTH, +1);
                    if (Constant.INVALID_ID == monthList.indexOf(calendar.getTimeInMillis())) {
                        monthList.add(calendar.getTimeInMillis());
                        monthPagerAdapter.notifyDataSetChanged();
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

    public class MonthPagerAdapter extends FragmentStatePagerAdapter {

        public MonthPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return monthList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return AccountOverviewMonthFragment.newInstance(iAccount.getId(), monthList.get(position));
        }

        @Override
        public int getItemPosition(Object item) {
            return POSITION_NONE;
        }
    }

    private void updateMonth() {
        findViewById(R.id.previous).setVisibility((viewPager.getCurrentItem() > 0) ? View.VISIBLE : View.INVISIBLE);
        findViewById(R.id.next).setVisibility((viewPager.getCurrentItem() < monthList.size()-1) ? View.VISIBLE : View.INVISIBLE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(monthList.get(viewPager.getCurrentItem()));
        SimpleDateFormat df = new SimpleDateFormat("MMM yyyy");
        Button date = (Button) findViewById(R.id.date);
        date.setText(df.format(calendar.getTime()));

        /* Load more data */
        new Handler().postDelayed(runnable, 500);
    }
}
