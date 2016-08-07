package com.sharad.epocket;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.sharad.epocket.accounts.AccountsFragment;
import com.sharad.epocket.bills.BillsFragment;
import com.sharad.epocket.budget.BudgetFragment;
import com.sharad.epocket.cards.CardsFragment;
import com.sharad.epocket.goals.GoalsFragment;
import com.sharad.epocket.home.HomeFragment;
import com.sharad.epocket.utils.BaseFragment;
import com.sharad.epocket.utils.Utils;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        HomeFragment.OnFragmentInteractionListener,
        AccountsFragment.OnFragmentInteractionListener,
        BillsFragment.OnFragmentInteractionListener,
        BudgetFragment.OnFragmentInteractionListener,
        CardsFragment.OnFragmentInteractionListener,
        GoalsFragment.OnFragmentInteractionListener {

    private FloatingActionButton mFab;
    private BaseFragment mSelectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.setTaskDescription(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSelectedFragment != null) {
                    mSelectedFragment.onFabClick(view);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mSelectedFragment = AccountsFragment.newInstance("","");
        FragmentManager fragmentManager = getSupportFragmentManager();
        mSelectedFragment.setFabAppearance();
        fragmentManager.beginTransaction().replace(R.id.content_main, mSelectedFragment).commit();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Class fragmentClass = null;
        switch(id) {
            case R.id.nav_accounts:
                fragmentClass = AccountsFragment.class;
                break;
            case R.id.nav_budgets:
                fragmentClass = BudgetFragment.class;
                break;
            case R.id.nav_goals:
                fragmentClass = GoalsFragment.class;
                break;
            case R.id.nav_cards:
                fragmentClass = CardsFragment.class;
                break;
            case R.id.nav_bills:
                fragmentClass = BillsFragment.class;
                break;
            case R.id.nav_categories:
                break;
        }

        if (fragmentClass != null) {
            try {
                mSelectedFragment = (BaseFragment) fragmentClass.newInstance();
                FragmentManager fragmentManager = getSupportFragmentManager();
                mSelectedFragment.setFabAppearance();
                fragmentManager.beginTransaction().replace(R.id.content_main, mSelectedFragment).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
    public void onFragmentInteraction(Uri uri) {

    }

    public FloatingActionButton getFab() {
        return mFab;
    }
}
