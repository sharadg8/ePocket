package com.sharad.cards;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.sharad.epocket.R;

public class AddCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add Card");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        HorizontalScrollView scrollView = (HorizontalScrollView)findViewById(R.id.horizontalScrollView);
        EditText name = (EditText)findViewById(R.id.c_add_name);
        EditText number = (EditText)findViewById(R.id.c_add_number);
        EditText expiry = (EditText)findViewById(R.id.c_add_expiry);
        EditText cvv = (EditText)findViewById(R.id.c_add_cvv);
        EditText pin = (EditText)findViewById(R.id.c_add_pin);
        EditText contact = (EditText)findViewById(R.id.c_add_contact);

        setScrollTo(name, scrollView);
        setScrollTo(number, scrollView);
        setScrollTo(expiry, scrollView);
        setScrollTo(cvv, scrollView);
        setScrollTo(pin, scrollView);
        setScrollTo(contact, scrollView);
    }

    private void setScrollTo(final EditText view, final HorizontalScrollView scrollView) {
        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    int scrollTo = 0;//((getWindowManager().getDefaultDisplay().getWidth() - view.getWidth())  / 2);
                    final int count = ((LinearLayout) scrollView.getChildAt(0))
                            .getChildCount();
                    for (int i = 0; i < count; i++) {
                        final View child = ((TextInputLayout)((LinearLayout) scrollView.getChildAt(0)).getChildAt(i)).getEditText();
                        if (child != view) {
                            scrollTo += child.getMeasuredWidth();
                        } else {
                            break;
                        }
                    }
                    scrollView.smoothScrollTo(scrollTo, 0);
                }
            }
        });
    }
}
