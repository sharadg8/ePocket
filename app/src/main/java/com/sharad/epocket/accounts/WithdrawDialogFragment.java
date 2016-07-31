package com.sharad.epocket.accounts;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.sharad.epocket.R;
import com.sharad.epocket.utils.Constant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Sharad on 24-Jul-16.
 */

public class WithdrawDialogFragment extends DialogFragment {
    private static final String ARG_CATEGORY_ID   = "category_id";
    private static final String ARG_CATEGORY_TYPE = "category_type";
    private long id = Constant.INVALID_ID;
    private ITransaction iTransaction = null;
    private OnWithdrawDialogListener onWithdrawDialogListener = null;
    private EditText eAmount = null;

    public WithdrawDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param accountId Database id for editing, -1 for adding new.
     * @return A new instance of fragment AddBillDialogFragment.
     */
    public static WithdrawDialogFragment newInstance(long accountId, long transactionId) {
        WithdrawDialogFragment fragment = new WithdrawDialogFragment();
        Bundle args = new Bundle();
        args.putLong(Constant.ARG_ACCOUNT_NUMBER_LONG, accountId);
        args.putLong(Constant.ARG_TRANSACTION_NUMBER_LONG, transactionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = Constant.INVALID_ID;
        long accountId = Constant.INVALID_ID;
        if (getArguments() != null) {
            accountId = getArguments().getLong(Constant.ARG_ACCOUNT_NUMBER_LONG);
            id = getArguments().getLong(Constant.ARG_TRANSACTION_NUMBER_LONG);
        }

        DataSourceTransaction dataSourceTransaction = new DataSourceTransaction(getContext());
        if(id != Constant.INVALID_ID) {
            iTransaction = dataSourceTransaction.getTransaction(id);
        } else {
            iTransaction = new ITransaction();
            iTransaction.setType(ITransaction.TRANSACTION_TYPE_ACCOUNT_WITHDRAW);
            iTransaction.setSubType(ITransaction.TRANSACTION_SUB_TYPE_ACCOUNT_BOTH);
            iTransaction.setAccount(accountId);

            /* TODO Remove this */
            DataSourceCategory dataSourceCategory = new DataSourceCategory(getContext());
            ArrayList<ICategory> iCategories = new ArrayList<>();
            dataSourceCategory.getCategories(iCategories);
            iTransaction.setCategory(iCategories.get(0).getId());
        }

        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Material_Light_Dialog_MinWidth);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.dialog_account_withdraw, container, false);

        Button bSave = (Button) rootView.findViewById(R.id.save);
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { onSave(); }
        });

        ImageButton bClose = (ImageButton) rootView.findViewById(R.id.close);
        bClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { dismiss(); }
        });

        ImageButton bDelete = (ImageButton) rootView.findViewById(R.id.delete);
        bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { onDelete(); }
        });

        eAmount = (EditText) rootView.findViewById(R.id.amount);
        if(iTransaction.getAmount() > 0.01) {
            eAmount.setText("" + iTransaction.getAmount());
        }

        final Button date = (Button) rootView.findViewById(R.id.date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(iTransaction.getDate());
                DatePickerDialog datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int year, int month, int day) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(year, month, day);
                        if(DateUtils.isToday(cal.getTimeInMillis())) {
                            date.setText("TODAY");
                        } else {
                            date.setText(df.format(cal.getTime()));
                        }
                        iTransaction.setDate(cal.getTimeInMillis());
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                datePicker.show();
            }
        });

        return rootView;
    }

    private void onDelete() {
        DataSourceTransaction dataSourceTransaction = new DataSourceTransaction(getContext());
        dataSourceTransaction.deleteTransaction(id);
        if(onWithdrawDialogListener != null) {
            onWithdrawDialogListener.onTransactionDeleted(id);
        }
        dismiss();
    }

    private void onSave() {
        if(eAmount.getText().length() > 0) {
            float amount = Float.parseFloat(eAmount.getText().toString());
            if(amount > 0.01) {
                iTransaction.setAmount(amount);
                DataSourceTransaction dataSourceTransaction = new DataSourceTransaction(getContext());
                if (id == Constant.INVALID_ID) {
                    iTransaction.setId(dataSourceTransaction.insertTransaction(iTransaction));
                } else {
                    dataSourceTransaction.updateTransaction(iTransaction);
                }
                if (onWithdrawDialogListener != null) {
                    onWithdrawDialogListener.onTransactionUpdated(iTransaction);
                }
            }
        }
        dismiss();
    }

    public void setOnWithdrawDialogListener(OnWithdrawDialogListener listener ) {
        onWithdrawDialogListener = listener;
    }

    public interface OnWithdrawDialogListener {
        void onTransactionUpdated(ITransaction iTransaction);
        void onTransactionDeleted(long id);
    }
}
