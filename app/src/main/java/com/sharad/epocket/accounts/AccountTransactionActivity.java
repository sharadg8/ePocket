package com.sharad.epocket.accounts;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.sharad.epocket.R;
import com.sharad.epocket.utils.Constant;
import com.sharad.epocket.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AccountTransactionActivity extends AppCompatActivity {
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
    private long accountId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_transaction);
        Utils.setTaskDescription(this);

        Bundle extras = getIntent().getExtras();
        accountId = extras.getLong(Constant.ARG_ACCOUNT_NUMBER_LONG, Constant.INVALID_ID);

        DataSourceAccount dataSourceAccount = new DataSourceAccount(this);
        IAccount iAccount = dataSourceAccount.getAccount(accountId);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(iAccount != null) {
            toolbar.setTitle(iAccount.getTitle());
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home) {
            setResult(RESULT_CANCELED);
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
            return AccountTransactionFragment.newInstance(accountId, position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 6;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM yy");
            Calendar cal = Calendar.getInstance();
            switch (position) {
                case Constant.TAB_ACCOUNT_TRANSACTION_THIS_MONTH:
                    return "THIS MONTH";
                case Constant.TAB_ACCOUNT_TRANSACTION_MONTH_M1:
                    cal.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
                    cal.add(Calendar.MONTH, -position);
                    return dateFormat.format(cal.getTime());
                case Constant.TAB_ACCOUNT_TRANSACTION_MONTH_M2:
                    cal.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
                    cal.add(Calendar.MONTH, -position);
                    return dateFormat.format(cal.getTime());
                case Constant.TAB_ACCOUNT_TRANSACTION_MONTH_M3:
                    cal.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
                    cal.add(Calendar.MONTH, -position);
                    return dateFormat.format(cal.getTime());
                case Constant.TAB_ACCOUNT_TRANSACTION_MONTH_M4:
                    cal.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
                    cal.add(Calendar.MONTH, -position);
                    return dateFormat.format(cal.getTime());
                case Constant.TAB_ACCOUNT_TRANSACTION_OLDER:
                    return "OLDER";
            }
            return null;
        }
    }
}
