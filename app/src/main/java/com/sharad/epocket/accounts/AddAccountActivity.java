package com.sharad.epocket.accounts;

import android.content.DialogInterface;
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
import android.widget.ToggleButton;

import com.sharad.epocket.R;
import com.sharad.epocket.utils.Utils;

import java.util.Currency;

public class AddAccountActivity extends AppCompatActivity {
    EditText editTextCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

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

        View layoutCurrency = findViewById(R.id.layout_account_currency);
        layoutCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCurrencyPicker();
            }
        });
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
        if (item.getItemId() == R.id.action_save) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
