package com.sharad.epocket.accounts;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.sharad.epocket.R;
import com.sharad.epocket.utils.Constant;
import com.sharad.epocket.utils.Utils;

import java.util.Calendar;
import java.util.Currency;
import java.util.Locale;

public class AddAccountActivity extends AppCompatActivity {
    EditText editTextCurrency;
    private long accountId = Constant.INVALID_ID;
    private Currency selectedCurrency = Currency.getInstance(Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        Bundle extras = getIntent().getExtras();
        accountId = extras.getLong(Constant.ARG_ACCOUNT_NUMBER_LONG, Constant.INVALID_ID);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextCurrency = (EditText) findViewById(R.id.account_currency);

        final ToggleButton btnCash = (ToggleButton) findViewById(R.id.account_button_cash);
        final ToggleButton btnCard = (ToggleButton) findViewById(R.id.account_button_card);

        final View layoutCash = findViewById(R.id.layout_account_cash);
        final View layoutCard = findViewById(R.id.layout_account_card);

        btnCash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                if(!isChecked && !btnCard.isChecked()) {
                    buttonView.setChecked(true);
                } else {
                    if (isChecked) {
                        layoutCash.setVisibility(View.VISIBLE);
                    } else {
                        layoutCash.setVisibility(View.GONE);
                    }
                }
            }
        });

        btnCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked && !btnCash.isChecked()) {
                    buttonView.setChecked(true);
                } else {
                    if (isChecked) {
                        layoutCard.setVisibility(View.VISIBLE);
                    } else {
                        layoutCard.setVisibility(View.GONE);
                    }
                }
            }
        });

        final Button addField = (Button) findViewById(R.id.account_add_field);
        final View layoutDescription = findViewById(R.id.layout_account_description);
        final View layoutContact = findViewById(R.id.layout_account_contact);
        final View layoutLogin = findViewById(R.id.layout_account_login);
        final View layoutPassword = findViewById(R.id.layout_account_password);
        final View layoutAccountNumber = findViewById(R.id.layout_account_number);
        layoutDescription.setVisibility(View.GONE);
        layoutContact.setVisibility(View.GONE);
        layoutLogin.setVisibility(View.GONE);
        layoutPassword.setVisibility(View.GONE);
        layoutAccountNumber.setVisibility(View.GONE);
        addField.setVisibility(View.VISIBLE);

        final CharSequence[] fields = {
                "Description", "Account Number", "Login Id", "Password", "Contact Number" };
        final boolean[] checkedFields = {
                false, false, false, false, false };
        final View[] layoutFields = {
                layoutDescription, layoutAccountNumber, layoutLogin, layoutPassword, layoutContact };

        addField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AddAccountActivity.this)
                        .setTitle("Select Fields")
                        .setMultiChoiceItems(fields, checkedFields, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                checkedFields[which] = isChecked;
                            }
                        })
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                for(int i=0; i<layoutFields.length; i++) {
                                    layoutFields[i].setVisibility((checkedFields[i] ? View.VISIBLE : View.GONE));
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            }
        });

        editTextCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCurrencyPicker();
            }
        });

        EditText accountName = (EditText)findViewById(R.id.account_name);
        EditText accountCashBal = (EditText)findViewById(R.id.account_cash_balance);
        EditText accountCardBal = (EditText)findViewById(R.id.account_card_balance);
        EditText accountNote = (EditText)findViewById(R.id.account_note);
        EditText accountLogin = (EditText)findViewById(R.id.account_login);
        EditText accountPassword = (EditText)findViewById(R.id.account_password);
        EditText accountNumber = (EditText)findViewById(R.id.account_number);
        EditText accountContact = (EditText)findViewById(R.id.account_contact);
        if(accountId != Constant.INVALID_ID) {
            DataSourceAccount source = new DataSourceAccount(this);
            IAccount account = source.getAccount(accountId);
            if(account != null) {
                btnCash.setChecked(account.hasCashAccount());
                layoutCash.setVisibility(account.hasCashAccount() ? View.VISIBLE : View.GONE);
                btnCard.setChecked(account.hasCardAccount());
                layoutCard.setVisibility(account.hasCardAccount() ? View.VISIBLE : View.GONE);

                accountName.setText(account.getTitle());
                accountCashBal.setText("" + account.getBalanceCash());
                accountCardBal.setText("" + account.getBalanceCard());
                if(account.getNote().length() > 0) {
                    accountNote.setText(account.getNote());
                    layoutDescription.setVisibility(View.VISIBLE);
                    checkedFields[0] = true;
                }
                if(account.getAccountNumber().length() > 0) {
                    accountNumber.setText(account.getAccountNumber());
                    layoutAccountNumber.setVisibility(View.VISIBLE);
                    checkedFields[1] = true;
                }
                if(account.getLoginId().length() > 0) {
                    accountLogin.setText(account.getLoginId());
                    layoutLogin.setVisibility(View.VISIBLE);
                    checkedFields[2] = true;
                }
                if(account.getPassword().length() > 0) {
                    accountPassword.setText(account.getPassword());
                    layoutPassword.setVisibility(View.VISIBLE);
                    checkedFields[3] = true;
                }
                if(account.getContact().length() > 0) {
                    accountContact.setText(account.getContact());
                    layoutContact.setVisibility(View.VISIBLE);
                    checkedFields[4] = true;
                }
                selectedCurrency = Currency.getInstance(account.getIsoCurrency());
            }
        } else {
            accountName.setText("");
            accountCashBal.setText("");
            accountCardBal.setText("");
            accountNote.setText("");
            accountLogin.setText("");
            accountPassword.setText("");
            accountNumber.setText("");
            accountContact.setText("");
        }
        editTextCurrency.setText(selectedCurrency.getSymbol() + " - " + selectedCurrency.getDisplayName());
    }

    private void showCurrencyPicker(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("CurrencyPickerDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        CurrencyPickerDialog newFragment = CurrencyPickerDialog.newInstance();
        newFragment.show(ft, "CurrencyPickerDialog");
        newFragment.setOnCurrencySelectedListener(new CurrencyPickerDialog.OnCurrencySelectedListener() {
            @Override
            public void onCurrencySelected(Currency currency) {
                selectedCurrency = currency;
                editTextCurrency.setText(currency.getSymbol() + " - " + currency.getDisplayName());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_account, menu);
        Utils.tintMenuIcon(this, menu.findItem(R.id.action_save), android.R.color.white);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_save) {
            save();
            finish();
            return true;
        } else if(id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void save() {
        EditText accountName = (EditText)findViewById(R.id.account_name);
        EditText accountCashBal = (EditText)findViewById(R.id.account_cash_balance);
        EditText accountCardBal = (EditText)findViewById(R.id.account_card_balance);
        EditText accountNote = (EditText)findViewById(R.id.account_note);
        EditText accountLogin = (EditText)findViewById(R.id.account_login);
        EditText accountPassword = (EditText)findViewById(R.id.account_password);
        EditText accountNumber = (EditText)findViewById(R.id.account_number);
        EditText accountContact = (EditText)findViewById(R.id.account_contact);
        ToggleButton btnCash = (ToggleButton) findViewById(R.id.account_button_cash);
        ToggleButton btnCard = (ToggleButton) findViewById(R.id.account_button_card);

        if(accountName.getText().length() > 0) {
            int accountType = IAccount.ACCOUNT_TYPE_CASH_CARD;

            if((btnCard.isChecked()) && (btnCash.isChecked())) {
                accountType = IAccount.ACCOUNT_TYPE_CASH_CARD;
            } else if(btnCard.isChecked()) {
                accountType = IAccount.ACCOUNT_TYPE_CARD_ONLY;
            } else if(btnCash.isChecked()) {
                accountType = IAccount.ACCOUNT_TYPE_CASH_ONLY;
            }

            float cardBal = 0;
            if(accountCardBal.getText().length() > 0) {
                cardBal = Float.parseFloat(accountCardBal.getText().toString());
            }
            float cashBal = 0;
            if(accountCashBal.getText().length() > 0) {
                cashBal = Float.parseFloat(accountCashBal.getText().toString());
            }

            IAccount account = new IAccount(accountId, selectedCurrency.getCurrencyCode(),
                    accountName.getText().toString(), accountNote.getText().toString(),
                    accountNumber.getText().toString(), accountLogin.getText().toString(),
                    accountPassword.getText().toString(), accountContact.getText().toString(),
                    cardBal, cashBal, 0, 0, accountType, Calendar.getInstance().getTimeInMillis());

            DataSourceAccount source = new DataSourceAccount(this);
            if(accountId == Constant.INVALID_ID) {
                accountId = source.insertAccount(account);
                Toast.makeText(getApplicationContext(), "New Account Added", Toast.LENGTH_SHORT).show();
            } else {
                source.updateAccount(account);
                Toast.makeText(getApplicationContext(), "Account Updated", Toast.LENGTH_SHORT).show();
            }
            Bundle activityResult = new Bundle();
            activityResult.putLong("AddAccountActivityKeyAccountId", accountId);
            Intent intent = new Intent();
            intent.putExtras(activityResult);
            setResult(RESULT_OK, intent);
        }
    }
}
