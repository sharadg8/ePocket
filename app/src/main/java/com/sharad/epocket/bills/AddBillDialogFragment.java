package com.sharad.epocket.bills;


import android.animation.Animator;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.widget.Button;

import com.sharad.epocket.R;

/**
 * A simple {@link DialogFragment} subclass.
 * Use the {@link AddBillDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddBillDialogFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public AddBillDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddBillDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddBillDialogFragment newInstance(String param1, String param2) {
        AddBillDialogFragment fragment = new AddBillDialogFragment();
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

        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Material_Light_Dialog_MinWidth);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.dialog_add_bill, container, false);

        final View closingFrame = rootView.findViewById(R.id.ab_closing_frame);
        closingFrame.setVisibility(View.INVISIBLE);
        final View doneIcon = rootView.findViewById(R.id.ab_done_icon);
        doneIcon.setVisibility(View.INVISIBLE);
        final View doneLabel = rootView.findViewById(R.id.ab_done_label);
        doneLabel.setVisibility(View.INVISIBLE);

        final Button save = (Button) rootView.findViewById(R.id.ab_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the center for the clipping circle
                int[] pos = new int[2];
                save.getLocationOnScreen(pos);
                int cx = pos[0] + save.getWidth()/4;
                int cy = (int)(pos[1] - 2.85 * save.getHeight());

                // get the final radius for the clipping circle
                float finalRadius = (float) Math.hypot(cx, cy);
                Animator anim = ViewAnimationUtils.createCircularReveal(
                        closingFrame, cx, cy, 0, finalRadius);
                closingFrame.setVisibility(View.VISIBLE);
                anim.setDuration(300);
                anim.setInterpolator(new AccelerateInterpolator());
                anim.start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showFinishFrame();
                    }
                }, 400);
            }

            private void showFinishFrame() {
                doneIcon.setVisibility(View.VISIBLE);
                AlphaAnimation anim1 = new AlphaAnimation(0, 1.0f);
                anim1.setDuration(300);
                anim1.setFillAfter(true);
                doneIcon.startAnimation(anim1);

                doneLabel.setVisibility(View.VISIBLE);
                AlphaAnimation anim2 = new AlphaAnimation(0, 1.0f);
                anim2.setDuration(300);
                anim2.setStartOffset(500);
                anim2.setFillAfter(true);
                doneLabel.startAnimation(anim1);
            }
        });

        return rootView;
    }
}
