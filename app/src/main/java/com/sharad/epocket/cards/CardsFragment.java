package com.sharad.epocket.cards;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharad.epocket.R;
import com.sharad.epocket.utils.BaseFragment;
import com.sharad.epocket.widget.recyclerview.RecyclerItemClickListener;
import com.sharad.epocket.widget.recyclerview.SnappyRecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CardsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CardsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardsFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CardsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CardsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CardsFragment newInstance(String param1, String param2) {
        CardsFragment fragment = new CardsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_cards, container, false);
        setupRecyclerView(rootView);

        FloatingActionButton fabAdd = (FloatingActionButton) rootView.findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity().getApplicationContext(), AddCardActivity.class);
                getActivity().startActivityForResult(myIntent, 2);
            }
        });

        FloatingActionButton fabEdit = (FloatingActionButton) rootView.findViewById(R.id.fab_edit);
        fabEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity().getApplicationContext(), AddCardActivity.class);
                getActivity().startActivityForResult(myIntent, 3);
            }
        });

        return rootView;
    }

    private void setupRecyclerView(View rootView) {
        SnappyRecyclerView recyclerView = (SnappyRecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setSnapEnabled(true, true);
        recyclerView.showIndicator(true);
        CardsRecycler recyclerAdapter = new CardsRecycler(createItemList());
        recyclerView.setAdapter(recyclerAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnItemTouchListener(
            new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                }
            })
        );
    }

    private List<CardItem> createItemList() {
        List<CardItem> itemList = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("MM/yy");

        itemList.add(new CardItem("Title01", "Title", R.drawable.ic_home_black_24dp, getResources().getColor(R.color.dark_palette00)));
        itemList.add(new CardItem("Title02", "Bank Name", R.drawable.ic_home_black_24dp, getResources().getColor(R.color.dark_palette04)));
        itemList.add(new CardItem("Title03", "Account Number", R.drawable.ic_home_black_24dp, getResources().getColor(R.color.dark_palette08)));
        itemList.add(new CardItem("Title04", "Title", R.drawable.ic_home_black_24dp, getResources().getColor(R.color.dark_palette01)));
        itemList.add(new CardItem("Title05", "Bank Name", R.drawable.ic_home_black_24dp, getResources().getColor(R.color.dark_palette09)));

        return itemList;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
