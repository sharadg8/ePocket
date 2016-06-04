package com.sharad.epocket;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sharad.widgets.ProgressGraph;


public class BudgetActivity extends ActionBarActivity {
    Toolbar mToolbar;
    RelativeLayout mGraphView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        initToolbar();
        initGraphView();
    }

    private void initGraphView() {
        mGraphView = (RelativeLayout) findViewById(R.id.graph_view);
        ProgressGraph progress = new ProgressGraph(this);

        progress.addProgressItem("Household", R.mipmap.ic_home_black_24dp, getResources().getColor(R.color.dark_palette00), 100, 40);
        progress.addProgressItem("Savings", R.mipmap.ic_account_balance_wallet_black_24dp, getResources().getColor(R.color.dark_palette12), 100, 80);
        progress.addProgressItem("Childcare", R.mipmap.ic_child_friendly_black_24dp, getResources().getColor(R.color.dark_palette07), 100, 20);
        progress.addProgressItem("Household", R.mipmap.ic_beach_access_black_24dp, getResources().getColor(R.color.dark_palette01), 100, 10);
        progress.addProgressItem("Savings", R.mipmap.ic_card_giftcard_black_24dp, getResources().getColor(R.color.dark_palette02), 100, 100);
        progress.addProgressItem("Childcare", R.mipmap.ic_directions_bus_black_24dp, getResources().getColor(R.color.dark_palette06), 100, 50);
        progress.addProgressItem("Household", R.mipmap.ic_hotel_black_24dp, getResources().getColor(R.color.dark_palette09), 100, 90);
        progress.addProgressItem("Savings", R.mipmap.ic_lightbulb_outline_black_24dp, getResources().getColor(R.color.dark_palette16), 100, 80);
        progress.addProgressItem("Childcare", R.mipmap.ic_local_dining_black_24dp, getResources().getColor(R.color.dark_palette05), 100, 20);

        progress.setPadding(8,8,8,8);
        progress.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, progress.getGraphHeight()));
        mGraphView.addView(progress);
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_budget, menu);
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
}
