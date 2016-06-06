/*
 * Copyright 2014, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sharad.budget;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.sharad.epocket.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * This fragment inflates a layout with two Floating Action Buttons and acts as a listener to
 * changes on them.
 */
public class DetailsFragment extends Fragment {
    private final static String TAG = "DetailsFragment";
    public final static String ITEM_ID_KEY = "DetailsFragment$idKey";

    public static DetailsFragment createInstance(long id) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(ITEM_ID_KEY, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.feed_list, container, false);

        setupRecyclerView(recyclerView);
        return recyclerView;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DetailsRecycler recyclerAdapter = new DetailsRecycler(createItemList());
        recyclerView.setAdapter(recyclerAdapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        /*Intent intent = new Intent(getActivity(), DetailsActivity.class);
                        intent.putExtra(DetailsActivity.ID_KEY, _adapter.getItemList().get(position).get_id());

                        View movingView = getActivity().findViewById(R.id.appBarLayout);
                        Pair<View, String> pair1 = Pair.create(movingView, movingView.getTransitionName());
                        movingView = view.findViewById(R.id.dc_progress);
                        Pair<View, String> pair2 = Pair.create(movingView, movingView.getTransitionName());

                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                getActivity(), pair1, pair2
                        );
                        ActivityCompat.startActivity(getActivity(), intent, options.toBundle());*/
                        Toast.makeText(getActivity().getApplicationContext(), "Item clicked at "+position, Toast.LENGTH_SHORT);
                    }
                })
        );
    }

    private List<DetailItem> createItemList() {
        List<DetailItem> itemList = new ArrayList<>();
        DecimalFormat nf = new DecimalFormat("##,##,###");
        SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy");

        itemList.add(new DetailItem("Title01", "Title", R.mipmap.ic_beach_access_black_24dp, getResources().getColor(R.color.dark_palette00)));
        itemList.add(new DetailItem("Title02", "Bank Name", R.mipmap.ic_child_friendly_black_24dp, getResources().getColor(R.color.dark_palette04)));
        itemList.add(new DetailItem("Title03", "Account Number", R.mipmap.ic_flight_black_24dp, getResources().getColor(R.color.dark_palette08)));
        itemList.add(new DetailItem("Title04", "Title", R.mipmap.ic_beach_access_black_24dp, getResources().getColor(R.color.dark_palette01)));
        itemList.add(new DetailItem("Title05", "Bank Name", R.mipmap.ic_child_friendly_black_24dp, getResources().getColor(R.color.dark_palette09)));
        itemList.add(new DetailItem("Title06", "Account Number", R.mipmap.ic_flight_black_24dp, getResources().getColor(R.color.dark_palette03)));
        itemList.add(new DetailItem("Title07", "Title", R.mipmap.ic_beach_access_black_24dp, getResources().getColor(R.color.dark_palette07)));
        itemList.add(new DetailItem("Title08", "Bank Name", R.mipmap.ic_child_friendly_black_24dp, getResources().getColor(R.color.dark_palette12)));
        itemList.add(new DetailItem("Title09", "Account Number", R.mipmap.ic_flight_black_24dp, getResources().getColor(R.color.dark_palette02)));
        itemList.add(new DetailItem("Title10", "Title", R.mipmap.ic_beach_access_black_24dp, getResources().getColor(R.color.dark_palette05)));
        itemList.add(new DetailItem("Title11", "Bank Name", R.mipmap.ic_child_friendly_black_24dp, getResources().getColor(R.color.dark_palette10)));
        itemList.add(new DetailItem("Title12", "Account Number", R.mipmap.ic_flight_black_24dp, getResources().getColor(R.color.dark_palette16)));
        itemList.add(new DetailItem("Title13", "Title", R.mipmap.ic_beach_access_black_24dp, getResources().getColor(R.color.dark_palette06)));
        itemList.add(new DetailItem("Title14", "Bank Name", R.mipmap.ic_child_friendly_black_24dp, getResources().getColor(R.color.dark_palette17)));
        itemList.add(new DetailItem("Title15", "Account Number", R.mipmap.ic_flight_black_24dp, getResources().getColor(R.color.dark_palette14)));

        return itemList;
    }
}
