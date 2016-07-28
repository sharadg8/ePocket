package com.sharad.epocket;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.sharad.epocket.widget.recyclerview.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        HomeFragment.OnFragmentInteractionListener,
        AccountsFragment.OnFragmentInteractionListener,
        BillsFragment.OnFragmentInteractionListener,
        BudgetFragment.OnFragmentInteractionListener,
        CardsFragment.OnFragmentInteractionListener,
        GoalsFragment.OnFragmentInteractionListener {

    private FloatingActionButton mFab;
    private BaseFragment mSelectedFragment;
    List<NavigationDrawerItem> featureItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        initNavigationDrawer(toolbar);
    }

    private void initNavigationDrawer(Toolbar toolbar) {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        featureItem = new ArrayList<>();
        //rowListItem.add(new NavigationDrawerItem("Timeline", R.drawable.ic_list_black_24dp, HomeFragment.newInstance("","")));
        featureItem.add(new NavigationDrawerItem("Accounts", R.drawable.ic_account_box_black_24dp, AccountsFragment.newInstance("","")));
        //rowListItem.add(new NavigationDrawerItem("Budget", R.drawable.ic_budget_black_24px, BudgetFragment.newInstance("","")));
        //rowListItem.add(new NavigationDrawerItem("Goals", R.drawable.ic_goal_black_24px, GoalsFragment.newInstance("","")));
        //rowListItem.add(new NavigationDrawerItem("Cards", R.drawable.ic_credit_card_black_24dp, CardsFragment.newInstance("","")));
        featureItem.add(new NavigationDrawerItem("Bills", R.drawable.ic_receipt_black_24dp, BillsFragment.newInstance("","")));
        switchFeature(0);


        GridLayoutManager gridLayout = new GridLayoutManager(MainActivity.this, 2);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.lst_drawer_items);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayout);

        DrawerGridRecycler rcAdapter = new DrawerGridRecycler(MainActivity.this, featureItem);
        recyclerView.setAdapter(rcAdapter);

        recyclerView.addOnItemTouchListener(
            new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    switchFeature(position);
                    drawer.closeDrawer(GravityCompat.START);
                }
            })
        );
    }

    private void switchFeature(int position) {
        if(position < featureItem.size()) {
            BaseFragment nextFragment = featureItem.get(position).getFragment();
            if (nextFragment != null) {
                mSelectedFragment = nextFragment;
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                mSelectedFragment.setFabAppearance();
                transaction.replace(R.id.content_main, mSelectedFragment);
                transaction.commit();
            }
        }
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
