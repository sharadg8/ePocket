package com.sharad.epocket.accounts;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.sharad.epocket.R;
import com.sharad.epocket.database.ContentConstant;
import com.sharad.epocket.utils.Constant;
import com.sharad.epocket.utils.ScrollHandler;
import com.sharad.epocket.utils.Utils;
import com.sharad.epocket.widget.recyclerview.StickyRecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AccountOverviewActivity extends AppCompatActivity implements ScrollHandler {
    private IAccount iAccount = null;
    private ArrayList<ITransaction> iTransactionArrayList = null;
    private StickyRecyclerView recyclerView = null;
    private OverviewRecyclerAdapter recyclerAdapter = null;
    private Calendar selectedMonth = Calendar.getInstance();
    private Button bMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_overview);
        Utils.setTaskDescription(this);

        Bundle extras = getIntent().getExtras();
        long accountId = extras.getLong(Constant.ARG_ACCOUNT_NUMBER_LONG, Constant.INVALID_ID);

        DataSourceAccount dataSourceAccount = new DataSourceAccount(this);
        iAccount = dataSourceAccount.getAccount(accountId);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bMonth = (Button) findViewById(R.id.month);
        recyclerView = (StickyRecyclerView) findViewById(R.id.recyclerView);

        if(iAccount != null) {
            setTitle(iAccount.getTitle());
            iTransactionArrayList = new ArrayList<>();
            recyclerAdapter = new OverviewRecyclerAdapter(this, this);
            recyclerAdapter.setIsoCurrency(iAccount.getIsoCurrency());
            recyclerView.setIndexer(recyclerAdapter);
            recyclerView.setAdapter(recyclerAdapter);

            switchToMonth(selectedMonth.getTimeInMillis());

            recyclerAdapter.setOnItemClickListener(new OverviewRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onEditClicked(int position, ITransaction iTransaction) {
                    if((iTransaction.getType() == ITransaction.TRANSACTION_TYPE_ACCOUNT_WITHDRAW)
                            || (iTransaction.getType() == ITransaction.TRANSACTION_TYPE_ACCOUNT_DEPOSIT)) {
                        showWithdrawDialog(iTransaction);
                    } else {
                        Intent intent = new Intent(getBaseContext(), AddTransactionActivity.class);
                        intent.putExtra(Constant.ARG_ACCOUNT_NUMBER_LONG, iTransaction.getAccount());
                        intent.putExtra(Constant.ARG_TRANSACTION_NUMBER_LONG, iTransaction.getId());
                        startActivityForResult(intent, Constant.REQ_EDIT_TRANSACTION);
                    }
                }

                @Override
                public void onDeleteClicked(final int position, final ITransaction iTransaction) {
                    new AlertDialog.Builder(AccountOverviewActivity.this)
                            .setMessage("Delete this transaction?")
                            .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    DataSourceTransaction source = new DataSourceTransaction(
                                            getBaseContext());
                                    source.deleteTransaction(iTransaction.getId());
                                    recyclerAdapter.removeAt(position);
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, null)
                            .show();
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }

        switch (requestCode) {
            case Constant.REQ_EDIT_TRANSACTION:
                long transactionId = data.getExtras().getLong(Constant.ARG_TRANSACTION_NUMBER_LONG, Constant.INVALID_ID);
                if (transactionId != Constant.INVALID_ID) {
                    // TODO update this to retain current position.
                    switchToMonth(selectedMonth.getTimeInMillis());
                }
                break;
        }
    }

    private void showWithdrawDialog(ITransaction iTransaction) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(Constant.DLG_ACCOUNT_WITHDRAW);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        WithdrawDialogFragment newFragment = WithdrawDialogFragment.newInstance(iTransaction.getAccount(), iTransaction.getId());
        newFragment.setOnWithdrawDialogListener(new WithdrawDialogFragment.OnWithdrawDialogListener() {
            @Override
            public void onTransactionUpdated(ITransaction iTransaction) {
                // TODO update this to retain current position.
                switchToMonth(selectedMonth.getTimeInMillis());
            }
        });
        newFragment.show(ft, Constant.DLG_ACCOUNT_WITHDRAW);
    }

    public void onButtonClick(View view) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(selectedMonth.getTimeInMillis());
        switch (view.getId()) {
            case R.id.previous:
                calendar.add(Calendar.MONTH, -1);
                switchToMonth(calendar.getTimeInMillis());
                break;
            case R.id.next:
                calendar.add(Calendar.MONTH, 1);
                switchToMonth(calendar.getTimeInMillis());
                break;
            case R.id.month:
                break;
        }
    }

    private void switchToMonth(final long timeInMillis) {
        AccountManager manager = AccountManager.getInstance();
        if(manager.hasAnyTransactionBeforeMonth(this, iAccount, timeInMillis)) {
            findViewById(R.id.previous).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.previous).setVisibility(View.INVISIBLE);
        }
        if(manager.hasAnyTransactionAfterMonth(this, iAccount, timeInMillis)
                || Utils.getMonthEnd_ms(timeInMillis) < Calendar.getInstance().getTimeInMillis()) {
            findViewById(R.id.next).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.next).setVisibility(View.INVISIBLE);
        }

        iTransactionArrayList.clear();
        DataSourceTransaction dataSourceTransaction = new DataSourceTransaction(getBaseContext());

        selectedMonth.setTimeInMillis(timeInMillis);
        long start_ms = Utils.getMonthStart_ms(timeInMillis);
        long end_ms = Utils.getMonthEnd_ms(timeInMillis);
        if (Utils.isThisMonth(timeInMillis)) {
            end_ms = Utils.getDayEnd_ms(timeInMillis);
        }

        String where = ContentConstant.KEY_TRANSACTION_ACCOUNT + "=" + iAccount.getId()
                + " AND " + ContentConstant.KEY_TRANSACTION_DATE + ">=" + start_ms
                + " AND " + ContentConstant.KEY_TRANSACTION_DATE + "<=" + end_ms;

        dataSourceTransaction.getTransactions(iTransactionArrayList, where);
        if(iTransactionArrayList.size() > 0) {
            smoothScrollTo(0);
            recyclerView.invalidateHeaderView();
        }
        recyclerAdapter.setItemList(iTransactionArrayList, selectedMonth.getTimeInMillis());

        SimpleDateFormat df = new SimpleDateFormat("MMM yyyy");
        bMonth.setText(df.format(selectedMonth.getTime()));
    }

    @Override
    public void setSmoothScrollStableId(long stableId) {

    }

    @Override
    public void smoothScrollTo(int position) {
        recyclerView.getLayoutManager().scrollToPosition(position);
    }
}
