package com.sharad.epocket;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddTransactionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddTransactionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddTransactionFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private static final String ARG_PARAM1 = "type";
    private int mType;

    public static int TRANSACTION_TYPE_EXPENSE  = 0;
    public static int TRANSACTION_TYPE_INCOME   = 1;
    public static int TRANSACTION_TYPE_TRANSFER = 2;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddTransactionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddTransactionFragment newInstance() {
        int type = TRANSACTION_TYPE_EXPENSE;
        AddTransactionFragment fragment = new AddTransactionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, type);
        fragment.setArguments(args);
        return fragment;
    }

    public AddTransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt(ARG_PARAM1, TRANSACTION_TYPE_EXPENSE);
        } else {
            mType = TRANSACTION_TYPE_EXPENSE;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_add_transaction, container, false);

        View view4 = rootView.findViewById(R.id.at_accent_box);
        TextView view5 = (TextView) rootView.findViewById(R.id.at_title_text);
        ImageButton view6 = (ImageButton) rootView.findViewById(R.id.at_close);
        if(mType == TRANSACTION_TYPE_EXPENSE) {
            view4.setBackgroundColor(getResources().getColor(R.color.transaction_expense));
            view5.setTextColor(getResources().getColor(R.color.transaction_expense));
            view5.setText("EXPENSE");
            view6.setColorFilter(getResources().getColor(R.color.transaction_expense));
        } else if(mType == TRANSACTION_TYPE_INCOME) {
            view4.setBackgroundColor(getResources().getColor(R.color.transaction_income));
            view5.setTextColor(getResources().getColor(R.color.transaction_income));
            view5.setText("INCOME");
            view6.setColorFilter(getResources().getColor(R.color.transaction_income));
        } else if(mType == TRANSACTION_TYPE_TRANSFER) {
            view4.setBackgroundColor(getResources().getColor(R.color.transaction_transfer));
            view5.setTextColor(getResources().getColor(R.color.transaction_transfer));
            view5.setText("TRANSFER");
            view6.setColorFilter(getResources().getColor(R.color.transaction_transfer));
        }

        ImageButton bClose = (ImageButton) rootView.findViewById(R.id.at_close);
        bClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onFragmentInteraction(null);
                }
            }
        });

        final Button bSave = (Button) rootView.findViewById(R.id.at_save);
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
