package com.sharad.accounts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.sharad.epocket.R;
import com.sharad.utils.viewpager.HollyViewPager;
import com.sharad.utils.viewpager.HollyViewPagerConfigurator;

public class AccountTransactionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_transactions);

        initToolbar();
        initViewPager();
    }

    private void initViewPager() {
        HollyViewPager hollyViewPager = (HollyViewPager) findViewById(R.id.hollyViewPager);
        hollyViewPager.getViewPager().setPageMargin(getResources().getDimensionPixelOffset(R.dimen.viewpager_margin));
        hollyViewPager.setConfigurator(new HollyViewPagerConfigurator() {
            @Override
            public float getHeightPercentForPage(int page) {
                return ((page+4)%10)/10f;
            }
        });

        hollyViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new RecyclerViewFragment();
            }

            @Override
            public int getCount() {
                return 12;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return "TITLE " + position;
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Transactions");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
