package com.sharad.epocket;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.sharad.home.AccountsFragment;
import com.sharad.utils.toolbar.NavigationItem;
import com.sharad.utils.toolbar.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AddTransactionFragment.OnFragmentInteractionListener {
    ViewPager mAddTransactionView;
    NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initFragment();

        final FloatingActionButton myFab = (FloatingActionButton) this.findViewById(R.id.fabButton);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Intent myIntent = new Intent(MainActivity.this, BudgetActivity.class);
                //MainActivity.this.startActivity(myIntent);
                mAddTransactionView.setVisibility(View.VISIBLE);
                myFab.setVisibility(View.GONE);
            }
        });

        mNavigationView = (NavigationView) findViewById(R.id.bottomNavigation);
        mNavigationView.isWithText(false);
        mNavigationView.isColoredBackground(true);
        mNavigationView.setItemActiveColorWithoutColoredBackground(ContextCompat.getColor(this, R.color.dark_palette00));
        //mNavigationView.setFont(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Noh_normal.ttf"));

        mNavigationView.addTab(new NavigationItem("Home", ContextCompat.getColor(this, R.color.light_palette01), R.mipmap.ic_home_black_24dp));
        mNavigationView.addTab(new NavigationItem("Accounts", ContextCompat.getColor(this, R.color.light_palette04), R.mipmap.ic_account_box_black_24dp));
        mNavigationView.addTab(new NavigationItem("Cards", ContextCompat.getColor(this, R.color.light_palette07), R.mipmap.ic_credit_card_black_24dp));
        mNavigationView.addTab(new NavigationItem("Goals", ContextCompat.getColor(this, R.color.light_palette09), R.mipmap.ic_goal_black_24px));
        mNavigationView.addTab(new NavigationItem("Budget", ContextCompat.getColor(this, R.color.light_palette13), R.mipmap.ic_budget_black_24px));
        mNavigationView.addTab(new NavigationItem("Bills", ContextCompat.getColor(this, R.color.light_palette17), R.mipmap.ic_receipt_black_24dp));
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initFragment() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(AccountsFragment.createInstance(1), "Accounts");
        viewPager.setAdapter(pagerAdapter);

        mAddTransactionView = (ViewPager) findViewById(R.id.addTransactionView);
        PagerAdapter addPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        addPagerAdapter.addFragment(AddTransactionFragment.newInstance(), "AddTransaction");
        mAddTransactionView.setAdapter(addPagerAdapter);
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

    public void onFragmentInteraction(Uri uri) {
        FloatingActionButton myFab = (FloatingActionButton) this.findViewById(R.id.fabButton);
        myFab.setVisibility(View.VISIBLE);
        mAddTransactionView.setVisibility(View.GONE);
        Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        mAddTransactionView.startAnimation(slide_down);
    }

    static class PagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }
}
