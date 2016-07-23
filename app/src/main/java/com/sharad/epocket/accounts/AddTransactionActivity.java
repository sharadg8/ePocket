package com.sharad.epocket.accounts;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.ViewAnimationUtils;
import android.view.ViewGroupOverlay;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sharad.epocket.R;
import com.sharad.epocket.widget.recurrencepicker.EventRecurrence;
import com.sharad.epocket.widget.recurrencepicker.RecurrencePickerDialog;
import com.sharad.epocket.widget.transaction.CalculatorExpressionBuilder;
import com.sharad.epocket.widget.transaction.CalculatorExpressionEvaluator;
import com.sharad.epocket.widget.transaction.CalculatorExpressionEvaluator.EvaluateCallback;
import com.sharad.epocket.widget.transaction.CalculatorExpressionTokenizer;
import com.sharad.epocket.widget.transaction.TransactionEditText;
import com.sharad.epocket.widget.transaction.TransactionEditText.OnTextSizeChangeListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class AddTransactionActivity extends AppCompatActivity implements
        OnTextSizeChangeListener, EvaluateCallback, OnLongClickListener,
        RecurrencePickerDialog.OnRecurrenceSetListener {
    private static final String TAG = "AddTransactionActivity";

    private final int TAB_EXPENSE = 0;
    private final int TAB_INCOME = 1;
    private final int TAB_TRANSFER = 2;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private CategoryPagerAdapter mCategoryPagerAdapter;

    private static final String NAME = AddTransactionActivity.class.getName();

    // instance state keys
    private static final String KEY_CURRENT_STATE = NAME + "_currentState";
    private static final String KEY_CURRENT_EXPRESSION = NAME + "_currentExpression";

    /**
     * Constant for an invalid resource id.
     */
    public static final int INVALID_RES_ID = -1;

    private enum CalculatorState {
        INPUT, EVALUATE, RESULT, ERROR
    }

    private final TextWatcher mFormulaTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            setState(CalculatorState.INPUT);
            mEvaluator.evaluate(editable, AddTransactionActivity.this);
        }
    };

    private final OnKeyListener mFormulaOnKeyListener = new OnKeyListener() {
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_NUMPAD_ENTER:
                case KeyEvent.KEYCODE_ENTER:
                    if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                        onEquals();
                    }
                    // ignore all other actions
                    return true;
            }
            return false;
        }
    };

    private final Editable.Factory mFormulaEditableFactory = new Editable.Factory() {
        @Override
        public Editable newEditable(CharSequence source) {
            final boolean isEdited = mCurrentState == CalculatorState.INPUT
                    || mCurrentState == CalculatorState.ERROR;
            return new CalculatorExpressionBuilder(source, mTokenizer, isEdited);
        }
    };

    private CalculatorState mCurrentState;
    private CalculatorExpressionTokenizer mTokenizer;
    private CalculatorExpressionEvaluator mEvaluator;

    private View mDisplayView;
    private TransactionEditText mFormulaEditText;
    private TransactionEditText mResultEditText;
    private ViewPager mPadViewPager;
    private View mDeleteButton;
    private View mClearButton;
    private View mEqualButton;

    private Animator mCurrentAnimator;



    private static final String FRAG_TAG_RECUR_PICKER = "recurrencePickerDialogFragment";
    private int mYear;
    private int mMonth;
    private int mDay;

    private String mRrule;
    private EventRecurrence mEventRecurrence = new EventRecurrence();
    private Time mStartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDisplayView = findViewById(R.id.display);
        mFormulaEditText = (TransactionEditText) findViewById(R.id.formula);
        mResultEditText = (TransactionEditText) findViewById(R.id.result);
        mPadViewPager = (ViewPager) findViewById(R.id.pad_pager);
        mDeleteButton = findViewById(R.id.del);
        mClearButton = findViewById(R.id.clr);

        mEqualButton = findViewById(R.id.pad_numeric).findViewById(R.id.eq);
        if (mEqualButton == null || mEqualButton.getVisibility() != View.VISIBLE) {
            mEqualButton = findViewById(R.id.pad_operator).findViewById(R.id.eq);
        }

        mTokenizer = new CalculatorExpressionTokenizer(this);
        mEvaluator = new CalculatorExpressionEvaluator(mTokenizer);

        savedInstanceState = savedInstanceState == null ? Bundle.EMPTY : savedInstanceState;
        setState(CalculatorState.values()[
                savedInstanceState.getInt(KEY_CURRENT_STATE, CalculatorState.INPUT.ordinal())]);
        mFormulaEditText.setText(mTokenizer.getLocalizedExpression(
                savedInstanceState.getString(KEY_CURRENT_EXPRESSION, "")));
        mEvaluator.evaluate(mFormulaEditText.getText(), this);

        mFormulaEditText.setEditableFactory(mFormulaEditableFactory);
        mFormulaEditText.addTextChangedListener(mFormulaTextWatcher);
        mFormulaEditText.setOnKeyListener(mFormulaOnKeyListener);
        mFormulaEditText.setOnTextSizeChangeListener(this);
        mDeleteButton.setOnLongClickListener(this);

        // Set up the ViewPager with the sections adapter.
        mCategoryPagerAdapter = new CategoryPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(mCategoryPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.category_tabs);
        tabLayout.setupWithViewPager(viewPager);

        final SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        Calendar currentDate = Calendar.getInstance();
        mYear = currentDate.get(Calendar.YEAR);
        mMonth = currentDate.get(Calendar.MONTH);
        mDay = currentDate.get(Calendar.DAY_OF_MONTH);

        final Button date = (Button) findViewById(R.id.date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog mDatePicker = new DatePickerDialog(AddTransactionActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int year, int month, int day) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(year, month, day);
                        if(DateUtils.isToday(cal.getTimeInMillis())) {
                            date.setText("TODAY");
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

        mStartTime = new Time();
        final ImageButton repeat = (ImageButton) findViewById(R.id.repeat);
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putLong(RecurrencePickerDialog.BUNDLE_START_TIME_MILLIS, mStartTime.toMillis(false));
                b.putString(RecurrencePickerDialog.BUNDLE_TIME_ZONE, mStartTime.timezone);

                // TODO may be more efficient to serialize and pass in EventRecurrence
                b.putString(RecurrencePickerDialog.BUNDLE_RRULE, mRrule);

                FragmentManager fm = getSupportFragmentManager();
                RecurrencePickerDialog rpd = (RecurrencePickerDialog) fm.findFragmentByTag(FRAG_TAG_RECUR_PICKER);
                if (rpd != null) {
                    rpd.dismiss();
                }
                rpd = new RecurrencePickerDialog();
                rpd.setArguments(b);
                rpd.setOnRecurrenceSetListener(AddTransactionActivity.this);
                rpd.show(fm, FRAG_TAG_RECUR_PICKER);
            }
        });

        final Button account = (Button) findViewById(R.id.account);
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<AccountItem> accounts = new ArrayList<>();
                AccountDataSource source = new AccountDataSource(AddTransactionActivity.this);
                source.getAccounts(accounts);
                final CharSequence[] items = new CharSequence[accounts.size()];
                for(int i=0; i<accounts.size(); i++) {
                    items[i] = accounts.get(i).getTitle();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(AddTransactionActivity.this);
                builder.setTitle("Select Account");
                builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO update the account selected
                        account.setText(items[which]);
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });

        final ImageButton comment = (ImageButton) findViewById(R.id.comment);
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int marginPx = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 16,
                        AddTransactionActivity.this.getResources().getDisplayMetrics()
                );
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(marginPx, 0, marginPx, 0);

                final EditText commentField = new EditText(AddTransactionActivity.this);
                commentField.setLayoutParams(layoutParams);
                commentField.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                commentField.setInputType(InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                        | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                commentField.setHorizontalScrollBarEnabled(false);
                new AlertDialog.Builder(AddTransactionActivity.this)
                        .setTitle("Comment")
                        .setView(commentField)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(commentField.length() > 0) {
                                    comment.setColorFilter(ContextCompat.getColor(
                                            AddTransactionActivity.this,android.R.color.white));
                                } else {
                                    comment.setColorFilter(ContextCompat.getColor(
                                            AddTransactionActivity.this, R.color.primary_light));
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            }
        });
    }

    @Override
    public void onRecurrenceSet(String rrule) {
        Log.d(TAG, "Old rrule:" + mRrule);
        Log.d(TAG, "New rrule:" + rrule);
        mRrule = rrule;
        if (mRrule != null) {
            mEventRecurrence.parse(mRrule);
        }
        //populateRepeats();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_transaction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        // If there's an animation in progress, end it immediately to ensure the state is
        // up-to-date before it is serialized.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.end();
        }

        super.onSaveInstanceState(outState);

        outState.putInt(KEY_CURRENT_STATE, mCurrentState.ordinal());
        outState.putString(KEY_CURRENT_EXPRESSION,
                mTokenizer.getNormalizedExpression(mFormulaEditText.getText().toString()));
    }

    private void setState(CalculatorState state) {
        if (mCurrentState != state) {
            mCurrentState = state;

            if (state == CalculatorState.RESULT || state == CalculatorState.ERROR) {
                mDeleteButton.setVisibility(View.GONE);
                mClearButton.setVisibility(View.VISIBLE);
            } else {
                mDeleteButton.setVisibility(View.VISIBLE);
                mClearButton.setVisibility(View.GONE);
            }

            if (state == CalculatorState.ERROR) {
                final int errorColor = getResources().getColor(R.color.calculator_error_color);
                mFormulaEditText.setTextColor(errorColor);
                mResultEditText.setTextColor(errorColor);
                getWindow().setStatusBarColor(errorColor);
            } else {
                mFormulaEditText.setTextColor(
                        getResources().getColor(R.color.display_formula_text_color));
                mResultEditText.setTextColor(
                        getResources().getColor(R.color.display_result_text_color));
                getWindow().setStatusBarColor(
                        getResources().getColor(R.color.calculator_accent_color));
            }
        }
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();

        // If there's an animation in progress, end it immediately to ensure the state is
        // up-to-date before the pending user interaction is handled.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.end();
        }
    }

    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.eq:
                onEquals();
                break;
            case R.id.del:
                onDelete();
                break;
            case R.id.clr:
                onClear();
                break;
            default:
                mFormulaEditText.append(((Button) view).getText());
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (view.getId() == R.id.del) {
            onClear();
            return true;
        }
        return false;
    }

    @Override
    public void onEvaluate(String expr, String result, int errorResourceId) {
        if (mCurrentState == CalculatorState.INPUT) {
            mResultEditText.setText(result);
        } else if (errorResourceId != INVALID_RES_ID) {
            onError(errorResourceId);
        } else if (!TextUtils.isEmpty(result)) {
            onResult(result);
        } else if (mCurrentState == CalculatorState.EVALUATE) {
            // The current expression cannot be evaluated -> return to the input state.
            setState(CalculatorState.INPUT);
        }

        mFormulaEditText.requestFocus();
    }

    @Override
    public void onTextSizeChanged(final TextView textView, float oldSize) {
        if (mCurrentState != CalculatorState.INPUT) {
            // Only animate text changes that occur from user input.
            return;
        }

        // Calculate the values needed to perform the scale and translation animations,
        // maintaining the same apparent baseline for the displayed text.
        final float textScale = oldSize / textView.getTextSize();
        final float translationX = (1.0f - textScale) *
                (textView.getWidth() / 2.0f - textView.getPaddingEnd());
        final float translationY = (1.0f - textScale) *
                (textView.getHeight() / 2.0f - textView.getPaddingBottom());

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(textView, View.SCALE_X, textScale, 1.0f),
                ObjectAnimator.ofFloat(textView, View.SCALE_Y, textScale, 1.0f),
                ObjectAnimator.ofFloat(textView, View.TRANSLATION_X, translationX, 0.0f),
                ObjectAnimator.ofFloat(textView, View.TRANSLATION_Y, translationY, 0.0f));
        animatorSet.setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();
    }

    private void onEquals() {
        if (mCurrentState == CalculatorState.INPUT) {
            setState(CalculatorState.EVALUATE);
            mEvaluator.evaluate(mFormulaEditText.getText(), this);
        }
    }

    private void onDelete() {
        // Delete works like backspace; remove the last character from the expression.
        final Editable formulaText = mFormulaEditText.getEditableText();
        final int formulaLength = formulaText.length();
        if (formulaLength > 0) {
            formulaText.delete(formulaLength - 1, formulaLength);
        }
    }

    private void reveal(View sourceView, int colorRes, AnimatorListener listener) {
        final ViewGroupOverlay groupOverlay =
                (ViewGroupOverlay) getWindow().getDecorView().getOverlay();

        final Rect displayRect = new Rect();
        mDisplayView.getGlobalVisibleRect(displayRect);

        // Make reveal cover the display and status bar.
        final View revealView = new View(this);
        revealView.setBottom(displayRect.bottom);
        revealView.setLeft(displayRect.left);
        revealView.setRight(displayRect.right);
        revealView.setBackgroundColor(getResources().getColor(colorRes));
        groupOverlay.add(revealView);

        final int[] clearLocation = new int[2];
        sourceView.getLocationInWindow(clearLocation);
        clearLocation[0] += sourceView.getWidth() / 2;
        clearLocation[1] += sourceView.getHeight() / 2;

        final int revealCenterX = clearLocation[0] - revealView.getLeft();
        final int revealCenterY = clearLocation[1] - revealView.getTop();

        final double x1_2 = Math.pow(revealView.getLeft() - revealCenterX, 2);
        final double x2_2 = Math.pow(revealView.getRight() - revealCenterX, 2);
        final double y_2 = Math.pow(revealView.getTop() - revealCenterY, 2);
        final float revealRadius = (float) Math.max(Math.sqrt(x1_2 + y_2), Math.sqrt(x2_2 + y_2));

        final Animator revealAnimator =
                ViewAnimationUtils.createCircularReveal(revealView,
                        revealCenterX, revealCenterY, 0.0f, revealRadius);
        revealAnimator.setDuration(
                getResources().getInteger(android.R.integer.config_longAnimTime));

        final Animator alphaAnimator = ObjectAnimator.ofFloat(revealView, View.ALPHA, 0.0f);
        alphaAnimator.setDuration(
                getResources().getInteger(android.R.integer.config_mediumAnimTime));
        alphaAnimator.addListener(listener);

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(revealAnimator).before(alphaAnimator);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                groupOverlay.remove(revealView);
                mCurrentAnimator = null;
            }
        });

        mCurrentAnimator = animatorSet;
        animatorSet.start();
    }

    private void onClear() {
        if (TextUtils.isEmpty(mFormulaEditText.getText())) {
            return;
        }

        final View sourceView = mClearButton.getVisibility() == View.VISIBLE
                ? mClearButton : mDeleteButton;
        reveal(sourceView, R.color.calculator_accent_color, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mFormulaEditText.getEditableText().clear();
            }
        });
    }

    private void onError(final int errorResourceId) {
        if (mCurrentState != CalculatorState.EVALUATE) {
            // Only animate error on evaluate.
            mResultEditText.setText(errorResourceId);
            return;
        }

        reveal(mEqualButton, R.color.calculator_error_color, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                setState(CalculatorState.ERROR);
                mResultEditText.setText(errorResourceId);
            }
        });
    }

    private void onResult(final String result) {
        // Calculate the values needed to perform the scale and translation animations,
        // accounting for how the scale will affect the final position of the text.
        final float resultScale =
                mFormulaEditText.getVariableTextSize(result) / mResultEditText.getTextSize();
        final float resultTranslationX = (1.0f - resultScale) *
                (mResultEditText.getWidth() / 2.0f - mResultEditText.getPaddingEnd());
        final float resultTranslationY = (1.0f - resultScale) *
                (mResultEditText.getHeight() / 2.0f - mResultEditText.getPaddingBottom()) +
                (mFormulaEditText.getBottom() - mResultEditText.getBottom()) +
                (mResultEditText.getPaddingBottom() - mFormulaEditText.getPaddingBottom());
        final float formulaTranslationY = -mFormulaEditText.getBottom();

        // Use a value animator to fade to the final text color over the course of the animation.
        final int resultTextColor = mResultEditText.getCurrentTextColor();
        final int formulaTextColor = mFormulaEditText.getCurrentTextColor();
        final ValueAnimator textColorAnimator =
                ValueAnimator.ofObject(new ArgbEvaluator(), resultTextColor, formulaTextColor);
        textColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mResultEditText.setTextColor((int) valueAnimator.getAnimatedValue());
            }
        });

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                textColorAnimator,
                ObjectAnimator.ofFloat(mResultEditText, View.SCALE_X, resultScale),
                ObjectAnimator.ofFloat(mResultEditText, View.SCALE_Y, resultScale),
                ObjectAnimator.ofFloat(mResultEditText, View.TRANSLATION_X, resultTranslationX),
                ObjectAnimator.ofFloat(mResultEditText, View.TRANSLATION_Y, resultTranslationY),
                ObjectAnimator.ofFloat(mFormulaEditText, View.TRANSLATION_Y, formulaTranslationY));
        animatorSet.setDuration(getResources().getInteger(android.R.integer.config_longAnimTime));
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mResultEditText.setText(result);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // Reset all of the values modified during the animation.
                mResultEditText.setTextColor(resultTextColor);
                mResultEditText.setScaleX(1.0f);
                mResultEditText.setScaleY(1.0f);
                mResultEditText.setTranslationX(0.0f);
                mResultEditText.setTranslationY(0.0f);
                mFormulaEditText.setTranslationY(0.0f);

                // Finally update the formula to use the current result.
                mFormulaEditText.setText(result);
                setState(CalculatorState.RESULT);

                mCurrentAnimator = null;
            }
        });

        mCurrentAnimator = animatorSet;
        animatorSet.start();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class CategoryPagerAdapter extends FragmentPagerAdapter {

        public CategoryPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return AddTransactionFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case TAB_EXPENSE:
                    return "EXPENSE";
                case TAB_INCOME:
                    return "INCOME";
                case TAB_TRANSFER:
                    return "TRANSFER";
            }
            return null;
        }
    }
}