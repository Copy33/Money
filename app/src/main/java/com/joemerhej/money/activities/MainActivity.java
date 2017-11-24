package com.joemerhej.money.activities;

/**
 * Created by Joe Merhej on 11/2/17.
 */

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.data.PieEntry;
import com.joemerhej.money.dialogs.DateRangeDialogFragment;
import com.joemerhej.money.views.NonSwipeableViewPager;
import com.joemerhej.money.R;
import com.joemerhej.money.account.Account;
import com.joemerhej.money.adapters.MainChartsFragmentPagerAdapter;
import com.joemerhej.money.sms.Sms;
import com.joemerhej.money.sms.SmsObservable;
import com.joemerhej.money.sms.SmsUtils;
import com.joemerhej.money.transaction.Transaction;
import com.joemerhej.money.transaction.TransactionCategory;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements Observer, DateRangeDialogFragment.DateRangeDialogListener
{
    // log tag
    private static final String TAG = "MainActivity";

    // shared preferences keys
    private static final String PREF_USER_MOBILE_PHONE = "pref_user_mobile_phone";
    private static final String PREF_USER_DATE_RANGE = "pref_user_date_range";

    // general
    private static final int SMS_PERMISSION_CODE = 0;
    private static boolean MOCK = false;

    // =============================================================================================
    // PROTOTYPE SECTION
    // =============================================================================================
    // views
    private EditText mPhoneNumberEditText;
    private EditText mSmsToSendEditText;
    // properties
    private String mNumberToSendFrom;
    private SharedPreferences mSharedPreferences;
    // =============================================================================================

    // data: date range
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyy", Locale.US);
    private static final Long ONE_DAY = Long.valueOf(24 * 60 * 60 * 1000);

    private String mDateRangePreference;
    private Date mOriginalFromDate;
    private Date mOriginalToDate;
    private Date mFromDate;
    private Date mToDate;

    // views : charts header
    private TextView mDayTextView;
    private TextView mAccountBalanceTextView;
    private NonSwipeableViewPager mViewPager;
    private MainChartsFragmentPagerAdapter mPagerAdapter;
    private TabLayout mMainChartsTabs;

    // views : charts
    private ArrayList<PieEntry> mSpendingEntries = new ArrayList<>();
    private ArrayList<Integer> mSpendingColors = new ArrayList<>();
    private ArrayList<PieEntry> mIncomeEntries = new ArrayList<>();
    private ArrayList<Integer> mIncomeColors = new ArrayList<>();

    // views : categories
    private Button mRentButton;
    private Button mTransferOutButton;
    private Button mTransportButton;
    private Button mEntertainmentButton;
    private Button mFoodButton;
    private Button mBillsButton;
    private Button mTravelButton;
    private Button mShoppingButton;
    private Button mGroceriesButton;
    private Button mCareButton;
    private Button mSportsButton;
    private Button mHealthButton;
    private Button mPetsButton;
    private Button mOtherButton;
    private Button mNoneButton;
    private TextView mRentTextView;
    private TextView mTransferOutTextView;
    private TextView mTransportTextView;
    private TextView mEntertainmentTextView;
    private TextView mFoodTextView;
    private TextView mBillsTextView;
    private TextView mTravelTextView;
    private TextView mShoppingTextView;
    private TextView mGroceriesTextView;
    private TextView mCareTextView;
    private TextView mSportsTextView;
    private TextView mHealthTextView;
    private TextView mPetsTextView;
    private TextView mOtherTextView;
    private TextView mNoneTextView;

    // data : accounts
    private Account mMainAccount = new Account();

    private Account mAccountSalary = new Account();
    private Account mAccountTransferIn = new Account();
    private Account mAccountCash = new Account();

    private Account mAccountTransferOut = new Account();
    private Account mAccountRent = new Account();
    private Account mAccountBills = new Account();
    private Account mAccountGroceries = new Account();
    private Account mAccountFood = new Account();
    private Account mAccountEntertainment = new Account();
    private Account mAccountTransport = new Account();
    private Account mAccountSports = new Account();
    private Account mAccountShopping = new Account();
    private Account mAccountHealth = new Account();
    private Account mAccountTravel = new Account();
    private Account mAccountPets = new Account();
    private Account mAccountPersonalCare = new Account();
    private Account mAccountOther = new Account();
    private Account mAccountNone = new Account();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set up shared preferences
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // =========================================================================================
        // PROTOTYPE SECTION
        // =========================================================================================
        // define views
        mPhoneNumberEditText = findViewById(R.id.phone_number_edit_text);
        mSmsToSendEditText = findViewById(R.id.sms_to_send_edit_text);

        mPhoneNumberEditText.setVisibility(View.GONE);
        mSmsToSendEditText.setVisibility(View.GONE);

        // auto populate phone number form shared preferences
        mNumberToSendFrom = mSharedPreferences.getString(PREF_USER_MOBILE_PHONE, "");

        if(!TextUtils.isEmpty(mNumberToSendFrom))
            mPhoneNumberEditText.setText(mNumberToSendFrom);

        // add this activity as an observer for our SMS observable
        SmsObservable.getInstance().addObserver(this);
        // =========================================================================================

        // set up toolbar
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        // set up views
        mDayTextView = findViewById(R.id.day_text_view);
        mAccountBalanceTextView = findViewById(R.id.account_balance_text_view);
        mViewPager = findViewById(R.id.main_charts_view_pager);
        mMainChartsTabs = findViewById(R.id.main_charts_tabs);

        mRentButton = findViewById(R.id.category_rent_button_list);
        mTransferOutButton = findViewById(R.id.category_transfer_out_button_list);
        mTransportButton = findViewById(R.id.category_transport_button_list);
        mEntertainmentButton = findViewById(R.id.category_entertainment_button_list);
        mFoodButton = findViewById(R.id.category_food_button_list);
        mBillsButton = findViewById(R.id.category_bills_button_list);
        mTravelButton = findViewById(R.id.category_travel_button_list);
        mShoppingButton = findViewById(R.id.category_shopping_button_list);
        mGroceriesButton = findViewById(R.id.category_groceries_button_list);
        mCareButton = findViewById(R.id.category_care_button_list);
        mSportsButton = findViewById(R.id.category_sports_button_list);
        mHealthButton = findViewById(R.id.category_health_button_list);
        mPetsButton = findViewById(R.id.category_pets_button_list);
        mOtherButton = findViewById(R.id.category_other_button_list);
        mNoneButton = findViewById(R.id.category_none_button_list);

        mRentTextView = findViewById(R.id.category_rent_text_view);
        mTransferOutTextView = findViewById(R.id.category_transfer_out_text_view);
        mTransportTextView = findViewById(R.id.category_transport_text_view);
        mEntertainmentTextView = findViewById(R.id.category_entertainment_text_view);
        mFoodTextView = findViewById(R.id.category_food_text_view);
        mBillsTextView = findViewById(R.id.category_bills_text_view);
        mTravelTextView = findViewById(R.id.category_travel_text_view);
        mShoppingTextView = findViewById(R.id.category_shopping_text_view);
        mGroceriesTextView = findViewById(R.id.category_groceries_text_view);
        mCareTextView = findViewById(R.id.category_care_text_view);
        mSportsTextView = findViewById(R.id.category_sports_text_view);
        mHealthTextView = findViewById(R.id.category_health_text_view);
        mPetsTextView = findViewById(R.id.category_pets_text_view);
        mOtherTextView = findViewById(R.id.category_other_text_view);
        mNoneTextView = findViewById(R.id.category_none_text_view);

        // ask for permission to access sms
        if(!hasReadSmsPermission())
        {
            // should always call showRequestPermissionsInfoAlertDialog function and not requestReadSmsPermission directly to give the app dialog first
            showRequestPermissionsInfoAlertDialog();
        }

        mDateRangePreference = mSharedPreferences.getString(PREF_USER_DATE_RANGE, "Month");

        // will populate mFromDate and mToDate based on preference view in shared preferences
        setFromAndToDatesBasedOnDateRangePreference(mDateRangePreference);

        // populate page views based on date range in shared prefs
        populatePageViews(mFromDate, mToDate);
    }

    private void populatePageViews(Date fromDate, Date toDate)
    {
        // reset the accounts
        mMainAccount.clear();
        mAccountSalary.clear();
        mAccountTransferIn.clear();
        mAccountCash.clear();
        mAccountTransferOut.clear();
        mAccountRent.clear();
        mAccountBills.clear();
        mAccountGroceries.clear();
        mAccountFood.clear();
        mAccountEntertainment.clear();
        mAccountTransport.clear();
        mAccountSports.clear();
        mAccountShopping.clear();
        mAccountHealth.clear();
        mAccountTravel.clear();
        mAccountPets.clear();
        mAccountPersonalCare.clear();
        mAccountOther.clear();
        mAccountNone.clear();

        mSpendingColors.clear();
        mSpendingEntries.clear();
        mIncomeColors.clear();
        mIncomeEntries.clear();

        // reset the views
        mRentButton.setVisibility(View.GONE);
        mTransferOutButton.setVisibility(View.GONE);
        mTransportButton.setVisibility(View.GONE);
        mEntertainmentButton.setVisibility(View.GONE);
        mFoodButton.setVisibility(View.GONE);
        mBillsButton.setVisibility(View.GONE);
        mTravelButton.setVisibility(View.GONE);
        mShoppingButton.setVisibility(View.GONE);
        mGroceriesButton.setVisibility(View.GONE);
        mCareButton.setVisibility(View.GONE);
        mSportsButton.setVisibility(View.GONE);
        mHealthButton.setVisibility(View.GONE);
        mPetsButton.setVisibility(View.GONE);
        mOtherButton.setVisibility(View.GONE);
        mNoneButton.setVisibility(View.GONE);

        mRentTextView.setVisibility(View.GONE);
        mTransferOutTextView.setVisibility(View.GONE);
        mTransportTextView.setVisibility(View.GONE);
        mEntertainmentTextView.setVisibility(View.GONE);
        mFoodTextView.setVisibility(View.GONE);
        mBillsTextView.setVisibility(View.GONE);
        mTravelTextView.setVisibility(View.GONE);
        mShoppingTextView.setVisibility(View.GONE);
        mGroceriesTextView.setVisibility(View.GONE);
        mCareTextView.setVisibility(View.GONE);
        mSportsTextView.setVisibility(View.GONE);
        mHealthTextView.setVisibility(View.GONE);
        mPetsTextView.setVisibility(View.GONE);
        mOtherTextView.setVisibility(View.GONE);
        mNoneTextView.setVisibility(View.GONE);

        // set the views
        mDayTextView.setText("from " + DATE_FORMAT.format(fromDate) + " to " + DATE_FORMAT.format(toDate));

        // TODO: instead of fetching the SMSs between the 2 dates every time, we should fetch ALL the SMSs once and add the transactions (alongside manual transactions) to a database,
        // TODO:    then the app would fetch these transactions as needed depending on date. Every app resume or app launch should detect last sms fetched and fetch only new ones to add to the database.
        // get the list of sms between 2 dates
        List<Sms> smsList = new ArrayList<>();

        smsList = SmsUtils.getAllSms(this, "EmiratesNBD", fromDate, toDate);

        // go through every sms, retrieve transaction, and add it to the account
        Set<String> set = new HashSet<>();

        for(Sms sms : smsList)
        {
            Transaction transaction = Transaction.from(sms);
            mMainAccount.applyTransaction(transaction);

            if(transaction != null)
            {
                set.add(transaction.getIssuer());
            }
        }

        Log.d(TAG, "bla");

        // mock data if needed
        if(MOCK)
            mMainAccount.mock();

        // set up main account balance
        mAccountBalanceTextView.setText(NumberFormat.getNumberInstance(Locale.US).format(mMainAccount.getBalance().intValue()) + " " + mMainAccount.getCurrency().toString());

        // fill in the transactions by category
        populateCategoriesViews();

        // match transfers to eliminate transfers within account
        matchTransfers();

        // set up pie charts
        setupSpendingChart();
        setupIncomeChart();

        mPagerAdapter = new MainChartsFragmentPagerAdapter(getSupportFragmentManager(), mSpendingEntries, mSpendingColors, mIncomeEntries, mIncomeColors);
        mViewPager.setAdapter(mPagerAdapter);
        mMainChartsTabs.setupWithViewPager(mViewPager);
        mMainChartsTabs.getTabAt(0).setText(getResources().getString(R.string.spending_title));
        mMainChartsTabs.getTabAt(1).setText(getResources().getString(R.string.income_title));
    }

    private void setFromAndToDatesBasedOnDateRangePreference(String dateRangePreference)
    {
        try
        {
            Calendar cal = Calendar.getInstance();

            switch(dateRangePreference)
            {
                case "Day":
                    // from date : today's date
                    mFromDate = DATE_FORMAT.parse(DATE_FORMAT.format(cal.getTime()));

                    // to date : tomorrow's date
                    cal.add(Calendar.DATE, +1);
                    mToDate = DATE_FORMAT.parse(DATE_FORMAT.format(cal.getTime()));
                    break;

                case "Week":
                    // from date : first day of this week
                    cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                    mFromDate = DATE_FORMAT.parse(DATE_FORMAT.format(cal.getTime()));

                    // to date : first day of next week
                    cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                    cal.add(Calendar.WEEK_OF_YEAR, +1);
                    mToDate = DATE_FORMAT.parse(DATE_FORMAT.format(cal.getTime()));
                    break;

                case "Month":
                    // from date : first day of this month
                    cal.set(Calendar.DAY_OF_MONTH, 1);
                    mFromDate = DATE_FORMAT.parse(DATE_FORMAT.format(cal.getTime()));

                    // to date : first day of next month
                    cal.set(Calendar.DAY_OF_MONTH, 1);
                    cal.add(Calendar.MONTH, +1);
                    mToDate = DATE_FORMAT.parse(DATE_FORMAT.format(cal.getTime()));
                    break;

                case "Year":
                    // from date : first day of this year
                    cal.set(Calendar.DAY_OF_YEAR, 1);
                    mFromDate = DATE_FORMAT.parse(DATE_FORMAT.format(cal.getTime()));

                    // to date : first day of next year
                    cal.set(Calendar.DAY_OF_YEAR, 1);
                    cal.add(Calendar.YEAR, +1);
                    mToDate = DATE_FORMAT.parse(DATE_FORMAT.format(cal.getTime()));
                    break;

                case "All":
                    mFromDate = new Date(Long.MIN_VALUE);
                    mToDate = new Date(Long.MAX_VALUE);
                    break;

                case "Custom": // TODO: custom date behavior
                    break;
            }
        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }

        // this is where we set the original view dates
        mOriginalFromDate = new Date(mFromDate.getTime());
        mOriginalToDate = new Date(mToDate.getTime());
    }

    private void populateCategoriesViews()
    {
        String noneTextView = "";
        String salaryTextView = "";
        String transferInTextView = "";
        String cashTextView = "";
        String transferOutTextView = "";
        String rentTextView = "";
        String transportTextView = "";
        String entertainmentTextView = "";
        String foodTextView = "";
        String billsTextView = "";
        String travelTextView = "";
        String shoppingTextView = "";
        String groceriesTextView = "";
        String careTextView = "";
        String sportsTextView = "";
        String healthTextView = "";
        String petsTextView = "";
        String otherTextView = "";

        for(Transaction t : mMainAccount.getTransactions())
        {
            if(t.getCategory().compareTo(TransactionCategory.NONE.toString()) == 0)
            {
                mAccountNone.applyTransaction(t);
                noneTextView += t.toItemString();
                mNoneTextView.setVisibility(View.VISIBLE);
                mNoneButton.setVisibility(View.VISIBLE);
                continue;
            }
            // TODO: finish the next 3
            if(t.getCategory().compareTo(TransactionCategory.SALARY.toString()) == 0)
            {
                mAccountSalary.applyTransaction(t);
                salaryTextView += t.toItemString();
                continue;
            }
            if(t.getCategory().compareTo(TransactionCategory.TRANSFER_IN.toString()) == 0)
            {
                mAccountTransferIn.applyTransaction(t);
                transferInTextView += t.toItemString();
                continue;
            }
            if(t.getCategory().compareTo(TransactionCategory.CASH.toString()) == 0)
            {
                mAccountCash.applyTransaction(t);
                cashTextView += t.toItemString();
                continue;
            }
            if(t.getCategory().compareTo(TransactionCategory.TRANSFER_OUT.toString()) == 0)
            {
                mAccountTransferOut.applyTransaction(t);
                transferOutTextView += t.toItemString();
                mTransferOutTextView.setVisibility(View.VISIBLE);
                mTransferOutButton.setVisibility(View.VISIBLE);
                continue;
            }
            if(t.getCategory().compareTo(TransactionCategory.RENT.toString()) == 0)
            {
                mAccountRent.applyTransaction(t);
                rentTextView += t.toItemString();
                mRentTextView.setVisibility(View.VISIBLE);
                mRentButton.setVisibility(View.VISIBLE);
                continue;
            }
            if(t.getCategory().compareTo(TransactionCategory.BILLS.toString()) == 0)
            {
                mAccountBills.applyTransaction(t);
                billsTextView += t.toItemString();
                mBillsTextView.setVisibility(View.VISIBLE);
                mBillsButton.setVisibility(View.VISIBLE);
                continue;
            }
            if(t.getCategory().compareTo(TransactionCategory.GROCERIES.toString()) == 0)
            {
                mAccountGroceries.applyTransaction(t);
                groceriesTextView += t.toItemString();
                mGroceriesTextView.setVisibility(View.VISIBLE);
                mGroceriesButton.setVisibility(View.VISIBLE);
                continue;
            }
            if(t.getCategory().compareTo(TransactionCategory.FOOD.toString()) == 0)
            {
                mAccountFood.applyTransaction(t);
                foodTextView += t.toItemString();
                mFoodTextView.setVisibility(View.VISIBLE);
                mFoodButton.setVisibility(View.VISIBLE);
                continue;
            }
            if(t.getCategory().compareTo(TransactionCategory.ENTERTAINMENT.toString()) == 0)
            {
                mAccountEntertainment.applyTransaction(t);
                entertainmentTextView += t.toItemString();
                mEntertainmentTextView.setVisibility(View.VISIBLE);
                mEntertainmentButton.setVisibility(View.VISIBLE);
                continue;
            }
            if(t.getCategory().compareTo(TransactionCategory.TRANSPORT.toString()) == 0)
            {
                mAccountTransport.applyTransaction(t);
                transportTextView += t.toItemString();
                mTransportTextView.setVisibility(View.VISIBLE);
                mTransportButton.setVisibility(View.VISIBLE);
                continue;
            }
            if(t.getCategory().compareTo(TransactionCategory.SPORTS.toString()) == 0)
            {
                mAccountSports.applyTransaction(t);
                sportsTextView += t.toItemString();
                mSportsTextView.setVisibility(View.VISIBLE);
                continue;
            }
            if(t.getCategory().compareTo(TransactionCategory.SHOPPING.toString()) == 0)
            {
                mAccountShopping.applyTransaction(t);
                shoppingTextView += t.toItemString();
                mShoppingTextView.setVisibility(View.VISIBLE);
                mShoppingButton.setVisibility(View.VISIBLE);
                continue;
            }
            if(t.getCategory().compareTo(TransactionCategory.HEALTH.toString()) == 0)
            {
                mAccountHealth.applyTransaction(t);
                healthTextView += t.toItemString();
                mHealthTextView.setVisibility(View.VISIBLE);
                mHealthButton.setVisibility(View.VISIBLE);
                continue;
            }
            if(t.getCategory().compareTo(TransactionCategory.TRAVEL.toString()) == 0)
            {
                mAccountTravel.applyTransaction(t);
                travelTextView += t.toItemString();
                mTravelTextView.setVisibility(View.VISIBLE);
                mTravelButton.setVisibility(View.VISIBLE);
                continue;
            }
            if(t.getCategory().compareTo(TransactionCategory.PETS.toString()) == 0)
            {
                mAccountPets.applyTransaction(t);
                petsTextView += t.toItemString();
                mPetsTextView.setVisibility(View.VISIBLE);
                mPetsButton.setVisibility(View.VISIBLE);
                continue;
            }
            if(t.getCategory().compareTo(TransactionCategory.CARE.toString()) == 0)
            {
                mAccountPersonalCare.applyTransaction(t);
                careTextView += t.toItemString();
                mCareTextView.setVisibility(View.VISIBLE);
                mCareButton.setVisibility(View.VISIBLE);
                continue;
            }
            if(t.getCategory().compareTo(TransactionCategory.OTHER.toString()) == 0)
            {
                mAccountOther.applyTransaction(t);
                otherTextView += t.toItemString();
                mOtherTextView.setVisibility(View.VISIBLE);
                mOtherButton.setVisibility(View.VISIBLE);
                continue;
            }
        }

        mNoneTextView.setText(noneTextView);
        mTransferOutTextView.setText(transferOutTextView);
        mRentTextView.setText(rentTextView);
        mTransportTextView.setText(transportTextView);
        mEntertainmentTextView.setText(entertainmentTextView);
        mFoodTextView.setText(foodTextView);
        mBillsTextView.setText(billsTextView);
        mTravelTextView.setText(travelTextView);
        mShoppingTextView.setText(shoppingTextView);
        mGroceriesTextView.setText(groceriesTextView);
        mCareTextView.setText(careTextView);
        mSportsTextView.setText(sportsTextView);
        mHealthTextView.setText(healthTextView);
        mPetsTextView.setText(petsTextView);
        mOtherTextView.setText(otherTextView);
    }

    void matchTransfers()
    {
        Account smallerListAccount = new Account();
        Account largerListAccount = new Account();

        List<Transaction> toRemoveFromSmallerAccount = new ArrayList<>();
        List<Transaction> toRemoveFromLargerAccount = new ArrayList<>();

        if(mAccountTransferOut.getTransactions().size() < mAccountTransferIn.getTransactions().size())
        {
            smallerListAccount = mAccountTransferOut;
            largerListAccount = mAccountTransferIn;
        }
        else
        {
            largerListAccount = mAccountTransferOut;
            smallerListAccount = mAccountTransferIn;
        }

        for(Transaction ts : smallerListAccount.getTransactions())
        {
            for(Transaction tl : largerListAccount.getTransactions())
            {
                if(ts.negates(tl))
                {
                    toRemoveFromSmallerAccount.add(ts);
                    toRemoveFromLargerAccount.add(tl);
                    Log.d(TAG, "Found a match! - Transaction with amount: " + ts.getAmount().toString() + " matches transaction with amount " + tl.getAmount().toString());
                    break;
                }
            }
        }

        smallerListAccount.removeTransactions(toRemoveFromSmallerAccount);
        largerListAccount.removeTransactions(toRemoveFromLargerAccount);
    }

    private void setupSpendingChart()
    {
        float labelNumber = mAccountNone.getBalance().abs().floatValue();
        if(Float.compare(labelNumber, 0.0f) != 0)
        {
            mSpendingEntries.add(new PieEntry(labelNumber, TransactionCategory.NONE.toString()));
            mSpendingColors.add(ContextCompat.getColor(this, R.color.black));
        }

        labelNumber = mAccountTransferOut.getBalance().abs().floatValue();
        if(Float.compare(labelNumber, 0.0f) != 0)
        {
            mSpendingEntries.add(new PieEntry(labelNumber, TransactionCategory.TRANSFER_OUT.toString()));
            mSpendingColors.add(ContextCompat.getColor(this, R.color.blue_transfer));
        }

        labelNumber = mAccountBills.getBalance().abs().floatValue();
        if(Float.compare(labelNumber, 0.0f) != 0)
        {
            mSpendingEntries.add(new PieEntry(labelNumber, TransactionCategory.BILLS.toString()));
            mSpendingColors.add(ContextCompat.getColor(this, R.color.red));
        }

        labelNumber = mAccountRent.getBalance().abs().floatValue();
        if(Float.compare(labelNumber, 0.0f) != 0)
        {
            mSpendingEntries.add(new PieEntry(labelNumber, TransactionCategory.RENT.toString()));
            mSpendingColors.add(ContextCompat.getColor(this, R.color.blue));
        }

        labelNumber = mAccountFood.getBalance().abs().floatValue();
        if(Float.compare(labelNumber, 0.0f) != 0)
        {
            mSpendingEntries.add(new PieEntry(labelNumber, TransactionCategory.FOOD.toString()));
            mSpendingColors.add(ContextCompat.getColor(this, R.color.green));
        }

        labelNumber = mAccountEntertainment.getBalance().abs().floatValue();
        if(Float.compare(labelNumber, 0.0f) != 0)
        {
            mSpendingEntries.add(new PieEntry(labelNumber, TransactionCategory.ENTERTAINMENT.toString()));
            mSpendingColors.add(ContextCompat.getColor(this, R.color.pink));
        }

        labelNumber = mAccountTransport.getBalance().abs().floatValue();
        if(Float.compare(labelNumber, 0.0f) != 0)
        {
            mSpendingEntries.add(new PieEntry(labelNumber, TransactionCategory.TRANSPORT.toString()));
            mSpendingColors.add(ContextCompat.getColor(this, R.color.yellow));
        }

        labelNumber = mAccountGroceries.getBalance().abs().floatValue();
        if(Float.compare(labelNumber, 0.0f) != 0)
        {
            mSpendingEntries.add(new PieEntry(labelNumber, TransactionCategory.GROCERIES.toString()));
            mSpendingColors.add(ContextCompat.getColor(this, R.color.turquoise));
        }

        labelNumber = mAccountSports.getBalance().abs().floatValue();
        if(Float.compare(labelNumber, 0.0f) != 0)
        {
            mSpendingEntries.add(new PieEntry(labelNumber, TransactionCategory.SPORTS.toString()));
            mSpendingColors.add(ContextCompat.getColor(this, R.color.purple));
        }

        labelNumber = mAccountShopping.getBalance().abs().floatValue();
        if(Float.compare(labelNumber, 0.0f) != 0)
        {
            mSpendingEntries.add(new PieEntry(labelNumber, TransactionCategory.SHOPPING.toString()));
            mSpendingColors.add(ContextCompat.getColor(this, R.color.orange));
        }

        labelNumber = mAccountHealth.getBalance().abs().floatValue();
        if(Float.compare(labelNumber, 0.0f) != 0)
        {
            mSpendingEntries.add(new PieEntry(labelNumber, TransactionCategory.HEALTH.toString()));
            mSpendingColors.add(ContextCompat.getColor(this, R.color.navy));
        }

        labelNumber = mAccountTravel.getBalance().abs().floatValue();
        if(Float.compare(labelNumber, 0.0f) != 0)
        {
            mSpendingEntries.add(new PieEntry(labelNumber, TransactionCategory.TRAVEL.toString()));
            mSpendingColors.add(ContextCompat.getColor(this, R.color.brown));
        }

        labelNumber = mAccountPets.getBalance().abs().floatValue();
        if(Float.compare(labelNumber, 0.0f) != 0)
        {
            mSpendingEntries.add(new PieEntry(labelNumber, TransactionCategory.PETS.toString()));
            mSpendingColors.add(ContextCompat.getColor(this, R.color.grey));
        }

        labelNumber = mAccountPersonalCare.getBalance().abs().floatValue();
        if(Float.compare(labelNumber, 0.0f) != 0)
        {
            mSpendingEntries.add(new PieEntry(labelNumber, TransactionCategory.CARE.toString()));
            mSpendingColors.add(ContextCompat.getColor(this, R.color.lime));
        }

        labelNumber = mAccountOther.getBalance().abs().floatValue();
        if(Float.compare(labelNumber, 0.0f) != 0)
        {
            mSpendingEntries.add(new PieEntry(labelNumber, TransactionCategory.OTHER.toString()));
            mSpendingColors.add(ContextCompat.getColor(this, R.color.silver));
        }
    }

    private void setupIncomeChart()
    {
        float labelNumber = mAccountSalary.getBalance().abs().floatValue();
        if(Float.compare(labelNumber, 0.0f) != 0)
        {
            mIncomeEntries.add(new PieEntry(labelNumber, TransactionCategory.SALARY.toString()));
            mIncomeColors.add(ContextCompat.getColor(this, R.color.green_salary));
        }

        labelNumber = mAccountTransferIn.getBalance().abs().floatValue();
        if(Float.compare(labelNumber, 0.0f) != 0)
        {
            mIncomeEntries.add(new PieEntry(labelNumber, TransactionCategory.TRANSFER_IN.toString()));
            mIncomeColors.add(ContextCompat.getColor(this, R.color.green_transfer));
        }

        labelNumber = mAccountCash.getBalance().abs().floatValue();
        if(Float.compare(labelNumber, 0.0f) != 0)
        {
            mIncomeEntries.add(new PieEntry(labelNumber, TransactionCategory.CASH.toString()));
            mIncomeColors.add(ContextCompat.getColor(this, R.color.green_cash));
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        populatePageViews(mFromDate, mToDate);
    }

    // =============================================================================================================================================================
    // PROTOTYPE SECTION
    // =============================================================================================================================================================

    private boolean hasValidPreConditions()
    {
        if(!SmsUtils.isValidPhoneNumber(mPhoneNumberEditText.getText().toString()))
        {
            Toast.makeText(getApplicationContext(), R.string.error_invalid_phone_number, Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!hasReadSmsPermission())
        {
            showRequestPermissionsInfoAlertDialog();
            return false;
        }

        return true;
    }

    //Checks if stored SharedPreferences value needs updating and updates it
    private void checkAndUpdateUserPrefNumber()
    {
        if(TextUtils.isEmpty(mNumberToSendFrom) && !mNumberToSendFrom.equals(mPhoneNumberEditText.getText().toString()))
        {
            mSharedPreferences
                    .edit()
                    .putString(PREF_USER_MOBILE_PHONE, mPhoneNumberEditText.getText().toString())
                    .apply();
        }
    }

    // =============================================================================================================================================================
    // OPTIONS MENU
    // =============================================================================================================================================================

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // show date range dialog on calendar button
        if(id == R.id.actions_calendar_range)
        {
            DateRangeDialogFragment dialog = new DateRangeDialogFragment();
            dialog.show(getFragmentManager(), DateRangeDialogFragment.class.getName());
        }

        return super.onOptionsItemSelected(item);
    }

    // =============================================================================================================================================================
    // CLICK LISTENERS (from xml)
    // =============================================================================================================================================================

    // handling clicking the category buttons
    public void onCategoryButtonClick(View view)
    {
    }

    // handling clicking the category transaction list buttons
    public void onCategoryButtonClickList(View view)
    {
    }

    // handle clicking on the previous/next/middle date range button
    public void dateRangeControlClick(View view)
    {
        try
        {
            Calendar cal = Calendar.getInstance();

            switch(mDateRangePreference)
            {
                case "Day":
                    if(view.getId() == R.id.previous_date_button)
                    {
                        mFromDate.setTime(mFromDate.getTime() - ONE_DAY);
                        mToDate.setTime(mFromDate.getTime() + ONE_DAY);
                    }
                    else if(view.getId() == R.id.current_date_button)
                    {
                        mFromDate = mOriginalFromDate;
                        mToDate = mOriginalToDate;
                    }
                    else if(view.getId() == R.id.next_date_button)
                    {
                        mFromDate.setTime(mFromDate.getTime() + ONE_DAY);
                        mToDate.setTime(mFromDate.getTime() + ONE_DAY);
                    }

                    break;

                case "Week":
                    if(view.getId() == R.id.previous_date_button)
                    {
                        mToDate = mFromDate;

                        cal.setTime(mFromDate);
                        cal.add(Calendar.WEEK_OF_YEAR, -1);
                        mFromDate = DATE_FORMAT.parse(DATE_FORMAT.format(cal.getTime()));
                    }
                    else if(view.getId() == R.id.current_date_button)
                    {
                        mFromDate = mOriginalFromDate;
                        mToDate = mOriginalToDate;
                    }
                    else if(view.getId() == R.id.next_date_button)
                    {
                        mFromDate = mToDate;

                        cal.setTime(mToDate);
                        cal.add(Calendar.WEEK_OF_YEAR, +1);
                        mToDate = DATE_FORMAT.parse(DATE_FORMAT.format(cal.getTime()));
                    }

                    break;

                case "Month":
                    if(view.getId() == R.id.previous_date_button)
                    {
                        mToDate = mFromDate;

                        cal.setTime(mFromDate);
                        cal.add(Calendar.MONTH, -1);
                        mFromDate = DATE_FORMAT.parse(DATE_FORMAT.format(cal.getTime()));
                    }
                    else if(view.getId() == R.id.current_date_button)
                    {
                        mFromDate = mOriginalFromDate;
                        mToDate = mOriginalToDate;
                    }
                    else if(view.getId() == R.id.next_date_button)
                    {
                        mFromDate = mToDate;

                        cal.setTime(mToDate);
                        cal.add(Calendar.MONTH, +1);
                        mToDate = DATE_FORMAT.parse(DATE_FORMAT.format(cal.getTime()));
                    }

                    break;

                case "Year":
                    if(view.getId() == R.id.previous_date_button)
                    {
                        mToDate = mFromDate;

                        cal.setTime(mFromDate);
                        cal.add(Calendar.YEAR, -1);
                        mFromDate = DATE_FORMAT.parse(DATE_FORMAT.format(cal.getTime()));
                    }
                    else if(view.getId() == R.id.current_date_button)
                    {
                        mFromDate = mOriginalFromDate;
                        mToDate = mOriginalToDate;
                    }
                    else if(view.getId() == R.id.next_date_button)
                    {
                        mFromDate = mToDate;

                        cal.setTime(mToDate);
                        cal.add(Calendar.YEAR, +1);
                        mToDate = DATE_FORMAT.parse(DATE_FORMAT.format(cal.getTime()));
                    }

                    break;

                default:
                    break;
            }
        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }

        populatePageViews(mFromDate, mToDate);
    }

    // send sms button click listener that will send the sms provided to the number provided
    public void SendSms(View view)
    {
        String smsToSendString = mSmsToSendEditText.getText().toString();

        if(TextUtils.isEmpty(smsToSendString))
        {
            Toast.makeText(MainActivity.this, "Sms to send is empty, won't send.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!hasValidPreConditions())
            return;

        checkAndUpdateUserPrefNumber();

        SmsUtils.sendDebugSms(mPhoneNumberEditText.getText().toString(), mSmsToSendEditText.getText().toString());
        Toast.makeText(getApplicationContext(), R.string.toast_sending_sms, Toast.LENGTH_SHORT).show();
    }

    // =============================================================================================================================================================
    // OBSERVER METHODS
    // =============================================================================================================================================================

    @Override
    public void update(Observable observable, Object data)
    {
        Toast.makeText(this, "observed!", Toast.LENGTH_SHORT).show();
    }

    // =============================================================================================================================================================
    // DATE RANGE DIALOG LISTENER METHODS
    // =============================================================================================================================================================

    @Override
    public void onItemClickDay(DateRangeDialogFragment dialog)
    {
        mSharedPreferences.edit().putString(PREF_USER_DATE_RANGE, "Day").apply();
        mDateRangePreference = "Day";
        setFromAndToDatesBasedOnDateRangePreference(mDateRangePreference);
        populatePageViews(mFromDate, mToDate);
    }

    @Override
    public void onItemClickWeek(DateRangeDialogFragment dialog)
    {
        mSharedPreferences.edit().putString(PREF_USER_DATE_RANGE, "Week").apply();
        mDateRangePreference = "Week";
        setFromAndToDatesBasedOnDateRangePreference(mDateRangePreference);
        populatePageViews(mFromDate, mToDate);
    }

    @Override
    public void onItemClickMonth(DateRangeDialogFragment dialog)
    {
        mSharedPreferences.edit().putString(PREF_USER_DATE_RANGE, "Month").apply();
        mDateRangePreference = "Month";
        setFromAndToDatesBasedOnDateRangePreference(mDateRangePreference);
        populatePageViews(mFromDate, mToDate);
    }

    @Override
    public void onItemClickYear(DateRangeDialogFragment dialog)
    {
        mSharedPreferences.edit().putString(PREF_USER_DATE_RANGE, "Year").apply();
        mDateRangePreference = "Year";
        setFromAndToDatesBasedOnDateRangePreference(mDateRangePreference);
        populatePageViews(mFromDate, mToDate);
    }

    @Override
    public void onItemClickAll(DateRangeDialogFragment dialog)
    {
        mSharedPreferences.edit().putString(PREF_USER_DATE_RANGE, "All").apply();
        mDateRangePreference = "All";
        setFromAndToDatesBasedOnDateRangePreference(mDateRangePreference);
        populatePageViews(mFromDate, mToDate);
    }

    @Override
    public void onItemClickCustom(DateRangeDialogFragment dialog)
    {
        //TODO: awaiting final design
    }

    // =============================================================================================================================================================
    // HANDLING SMS PERMISSIONS
    // =============================================================================================================================================================

    // requests sms read permission if needed (needs to check Android SDK version first)
    private void requestReadSmsPermission()
    {
        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_SMS))
        {
            return;
        }

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS}, SMS_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch(requestCode)
        {
            case SMS_PERMISSION_CODE:
            {
                // If request is cancelled, the result arrays are empty.
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //TODO: figure out what to do here, nothing for now.
                    //checkAndUpdateUserPrefNumber();

                    //SmsUtils.sendDebugSms(mPhoneNumberEditText.getText().toString(), mSmsToSendEditText.getText().toString());
                    //Toast.makeText(getApplicationContext(), R.string.toast_sending_sms, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //TODO: figure out what to do here, nothing for now.
                    Toast.makeText(getApplicationContext(), "PERMISSION DENIED!", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    // shows an informative dialog to why sms permissions are needed in this app
    public void showRequestPermissionsInfoAlertDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.permission_alert_dialog_title);
        builder.setMessage(R.string.permission_dialog_message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // when user hits ok, the dialog dismisses and the app requests for sms read permission
                dialog.dismiss();

                requestReadSmsPermission();
            }
        });

        builder.setCancelable(false);
        builder.show();
    }

    // checks if the app has permissions to read sms
    private boolean hasReadSmsPermission()
    {
        return ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
    }
}
