package com.sharad.epocket;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.sharad.cards.CardsFragment;
import com.sharad.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements CardsFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initNavigationDrawer(toolbar);
    }

    private void initNavigationDrawer(Toolbar toolbar) {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final List<NavigationDrawerItem> rowListItem = new ArrayList<>();
        rowListItem.add(new NavigationDrawerItem("Home", R.drawable.ic_home_black_24dp, null));
        rowListItem.add(new NavigationDrawerItem("Accounts", R.drawable.ic_account_box_black_24dp, null));
        rowListItem.add(new NavigationDrawerItem("Budget", R.drawable.ic_budget_black_24px, null));
        rowListItem.add(new NavigationDrawerItem("Goals", R.drawable.ic_goal_black_24px, null));
        rowListItem.add(new NavigationDrawerItem("Cards", R.drawable.ic_credit_card_black_24dp, CardsFragment.newInstance("","")));
        rowListItem.add(new NavigationDrawerItem("Bills", R.drawable.ic_receipt_black_24dp, null));

        GridLayoutManager gridLayout = new GridLayoutManager(MainActivity.this, 2);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.lst_drawer_items);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayout);

        DrawerGridRecycler rcAdapter = new DrawerGridRecycler(MainActivity.this, rowListItem);
        recyclerView.setAdapter(rcAdapter);

        recyclerView.addOnItemTouchListener(
            new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Fragment nextFragment = rowListItem.get(position).getFragment();
                    if(nextFragment != null) {
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        // Replace whatever is in the fragment_container view with this fragment,
                        // and add the transaction to the back stack so the user can navigate back
                        transaction.replace(R.id.content_main, nextFragment);
                        //transaction.addToBackStack(null);
                        // Commit the transaction
                        transaction.commit();
                        drawer.closeDrawer(GravityCompat.START);
                    }
                }
            })
        );
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

    public void onFragmentInteraction(Uri uri) {

    }
}
