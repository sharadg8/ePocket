package com.sharad.epocket.bills;


import android.animation.Animator;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.sharad.epocket.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link DialogFragment} subclass.
 * Use the {@link AddBillDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddBillDialogFragment extends DialogFragment {
    private static final String ARG_BILL_ID = "bill_id";
    public static final long    INVALID_ID = -1;

    private int mYear;
    private int mMonth;
    private int mDay;
    private BillDataSource _db;
    private BillItem _bill;
    private long _id = INVALID_ID;

    public AddBillDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id Database id for editing, -1 for adding new.
     * @return A new instance of fragment AddBillDialogFragment.
     */
    public static AddBillDialogFragment newInstance(long id) {
        AddBillDialogFragment fragment = new AddBillDialogFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_BILL_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _id = INVALID_ID;
        if (getArguments() != null) {
            _id = getArguments().getLong(ARG_BILL_ID);
        }

        _db = new BillDataSource(getContext());
        if(_id != INVALID_ID) {
            _bill = _db.getBill(_id);
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

        final TextInputLayout textLayoutTitle = (TextInputLayout) rootView.findViewById(R.id.ab_til_title);
        final EditText editTextTitle = (EditText) rootView.findViewById(R.id.ab_title);
        final TextInputLayout textLayoutAmount = (TextInputLayout) rootView.findViewById(R.id.ab_til_amount);
        final EditText editTextAmount = (EditText) rootView.findViewById(R.id.ab_amount);
        final Button date = (Button) rootView.findViewById(R.id.ab_date);

        ImageButton close = (ImageButton) rootView.findViewById(R.id.ab_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        final Button account = (Button) rootView.findViewById(R.id.ab_account);
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO update the list of accounts
                final CharSequence[] items = { "Default", "Default" };
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Select Account");
                builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO update the account selected
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });

        final SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        Calendar currentDate = Calendar.getInstance();

        /*********** Update data for edit ***********/
        if(_id != INVALID_ID) {
            editTextTitle.setText(_bill.getTitle());
            editTextAmount.setText("" + _bill.getAmount());
            currentDate.setTime(_bill.getNextDate().getTime());
            if (DateUtils.isToday(currentDate.getTimeInMillis())) {
                date.setText("Today");
            } else {
                date.setText(df.format(currentDate.getTime()));
            }
        }
        /********************************************/

        mYear = currentDate.get(Calendar.YEAR);
        mMonth = currentDate.get(Calendar.MONTH);
        mDay = currentDate.get(Calendar.DAY_OF_MONTH);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int year, int month, int day) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(year, month, day);
                        if(DateUtils.isToday(cal.getTimeInMillis())) {
                            date.setText("Today");
                        } else {
                            date.setText(df.format(cal.getTime()));
                        }
                        mYear = year;
                        mMonth = month;
                        mDay = day;
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.show();
            }
        });

        final Button save = (Button) rootView.findViewById(R.id.ab_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate() == false) {
                    return;
                }
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                store();

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
                AlphaAnimation anim1 = new AlphaAnimation(0.1f, 1.0f);
                anim1.setDuration(300);
                anim1.setStartOffset(500);
                anim1.setFillAfter(true);
                doneIcon.startAnimation(anim1);

                doneLabel.setVisibility(View.VISIBLE);
                AlphaAnimation anim2 = new AlphaAnimation(0, 1.0f);
                anim2.setDuration(300);
                anim2.setFillAfter(true);
                doneLabel.startAnimation(anim2);

                anim1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        dismiss();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });
            }

            private void store() {
                if(_id == INVALID_ID) {
                    Calendar date = Calendar.getInstance();
                    date.set(mYear, mMonth, mDay);
                    String title = editTextTitle.getText().toString();
                    float amount = Float.parseFloat(editTextAmount.getText().toString());
                    _bill = new BillItem(0, title, "Default", "$", amount, date.getTimeInMillis(), 0, 0);
                    _db.insertBill(_bill);
                } else {
                    _db.updateBill(_id, _bill);
                }
            }

            private boolean validate() {
                if(editTextTitle.getText().length() == 0) {
                    textLayoutTitle.setError("Title can't be empty!");
                    textLayoutTitle.setErrorEnabled(true);
                    return false;
                } else {
                    textLayoutTitle.setErrorEnabled(false);
                }

                if(editTextAmount.getText().length() == 0) {
                    textLayoutAmount.setError("Amount can't be empty!");
                    textLayoutAmount.setErrorEnabled(true);
                    return false;
                } else {
                    float amount = Float.parseFloat(editTextAmount.getText().toString());
                    if(amount < 0.1f) {
                        textLayoutAmount.setError("Invalid Amount!");
                        textLayoutAmount.setErrorEnabled(true);
                        return false;
                    } else {
                        textLayoutAmount.setErrorEnabled(false);
                    }
                }

                return true;
            }
        });

        return rootView;
    }
}
