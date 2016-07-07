package com.sharad.epocket.accounts;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sharad.epocket.R;
import com.sharad.epocket.utils.AutofitRecyclerView;
import com.sharad.epocket.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class AddTransactionActivity extends AppCompatActivity {

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

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

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
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_add_transaction, container, false);
            AutofitRecyclerView recyclerView = (AutofitRecyclerView)rootView.findViewById(R.id.recyclerView);

            final List<Integer> itemList = new ArrayList<>();
            itemList.add(R.drawable.c_home_black_24dp);
            itemList.add(R.drawable.c_account_balance_black_24dp);
            itemList.add(R.drawable.c_account_balance_wallet_black_24dp);
            itemList.add(R.drawable.c_airplanemode_active_black_24dp);
            itemList.add(R.drawable.c_directions_boat_black_24dp);
            itemList.add(R.drawable.c_directions_bus_black_24dp);
            itemList.add(R.drawable.c_directions_car_black_24dp);
            itemList.add(R.drawable.c_favorite_black_24dp);
            itemList.add(R.drawable.c_extension_black_24dp);
            itemList.add(R.drawable.c_camera_roll_black_24dp);
            itemList.add(R.drawable.c_flash_on_black_24dp);
            itemList.add(R.drawable.c_headset_black_24dp);
            itemList.add(R.drawable.c_important_devices_black_24dp);
            itemList.add(R.drawable.c_insert_emoticon_black_24dp);
            itemList.add(R.drawable.c_language_black_24dp);
            itemList.add(R.drawable.c_lightbulb_outline_black_24dp);
            itemList.add(R.drawable.c_live_tv_black_24dp);
            itemList.add(R.drawable.c_local_activity_black_24dp);
            itemList.add(R.drawable.c_local_bar_black_24dp);
            itemList.add(R.drawable.c_local_cafe_black_24dp);
            itemList.add(R.drawable.c_local_dining_black_24dp);
            itemList.add(R.drawable.c_local_florist_black_24dp);
            itemList.add(R.drawable.c_local_gas_station_black_24dp);
            itemList.add(R.drawable.c_local_grocery_store_black_24dp);
            itemList.add(R.drawable.c_local_hospital_black_24dp);
            itemList.add(R.drawable.c_hotel_black_24dp);
            itemList.add(R.drawable.c_local_mall_black_24dp);
            itemList.add(R.drawable.c_local_offer_black_24dp);
            itemList.add(R.drawable.c_local_parking_black_24dp);
            itemList.add(R.drawable.c_local_phone_black_24dp);
            itemList.add(R.drawable.c_group_black_24dp);
            itemList.add(R.drawable.c_local_pizza_black_24dp);
            itemList.add(R.drawable.c_local_shipping_black_24dp);
            itemList.add(R.drawable.c_local_taxi_black_24dp);
            itemList.add(R.drawable.c_movie_black_24dp);
            itemList.add(R.drawable.c_music_note_black_24dp);
            itemList.add(R.drawable.c_network_wifi_black_24dp);
            itemList.add(R.drawable.c_palette_black_24dp);
            itemList.add(R.drawable.c_payment_black_24dp);
            itemList.add(R.drawable.c_person_black_24dp);
            itemList.add(R.drawable.c_pets_black_24dp);
            itemList.add(R.drawable.c_phone_android_black_24dp);
            itemList.add(R.drawable.c_photo_camera_black_24dp);
            itemList.add(R.drawable.c_pregnant_woman_black_24dp);
            itemList.add(R.drawable.c_receipt_black_24dp);
            itemList.add(R.drawable.c_shop_black_24dp);
            itemList.add(R.drawable.c_shopping_basket_black_24dp);
            itemList.add(R.drawable.c_style_black_24dp);
            itemList.add(R.drawable.c_motorcycle_black_24dp);
            itemList.add(R.drawable.c_content_cut_black_24dp);
            itemList.add(R.drawable.c_traffic_black_24dp);
            itemList.add(R.drawable.c_weekend_black_24dp);
            itemList.add(R.drawable.c_thumb_up_black_24dp);
            itemList.add(R.drawable.c_thumb_down_black_24dp);
            itemList.add(R.drawable.c_school_black_24dp);
            itemList.add(R.drawable.c_build_black_24dp);
            itemList.add(R.drawable.c_card_giftcard_black_24dp);
            itemList.add(R.drawable.c_trending_up_black_24dp);
            itemList.add(R.drawable.c_trending_flat_black_24dp);
            itemList.add(R.drawable.c_trending_down_black_24dp);
            itemList.add(R.drawable.c_face_black_24dp);
            itemList.add(R.drawable.c_security_black_24dp);
            itemList.add(R.drawable.c_child_friendly_black_24px);
            itemList.add(R.drawable.c_videogame_asset_black_24px);
            itemList.add(R.drawable.c_delete_black_24dp);

            CategoryRecyclerAdapter rcAdapter = new CategoryRecyclerAdapter(itemList);
            recyclerView.setAdapter(rcAdapter);
            recyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            //mCategoryImage.setImageResource(itemList.get(position));
                        }
                    })
            );

            return rootView;
        }
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
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "EXPENSE";
                case 1:
                    return "INCOME";
                case 2:
                    return "TRANSFER";
            }
            return null;
        }
    }
}
