package com.sharad.epocket.accounts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharad.epocket.R;
import com.sharad.epocket.utils.AutofitRecyclerView;
import com.sharad.epocket.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sharad on 09-Jul-16.
 */

public class AddTransactionFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public AddTransactionFragment() { }
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AddTransactionFragment newInstance(int sectionNumber) {
        AddTransactionFragment fragment = new AddTransactionFragment();
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
        itemList.add(R.drawable.c_water_drop_black_24px);
        itemList.add(R.drawable.c_live_tv_black_24dp);
        itemList.add(R.drawable.c_local_activity_black_24dp);
        itemList.add(R.drawable.c_local_bar_black_24dp);
        itemList.add(R.drawable.c_local_cafe_black_24dp);
        itemList.add(R.drawable.c_local_dining_black_24dp);
        itemList.add(R.drawable.c_local_florist_black_24dp);
        itemList.add(R.drawable.c_local_gas_station_black_24dp);
        itemList.add(R.drawable.c_local_grocery_store_black_24dp);
        itemList.add(R.drawable.c_local_hospital_black_24dp);
        itemList.add(R.drawable.c_medicine_black_24px);
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
