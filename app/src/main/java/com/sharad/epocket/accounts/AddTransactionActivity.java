package com.sharad.epocket.accounts;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharad.epocket.R;

public class AddTransactionActivity extends AppCompatActivity {
    private final int TAB_EXPENSE = 0;
    private final int TAB_INCOME = 1;
    private final int TAB_TRANSFER = 2;

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

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

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
