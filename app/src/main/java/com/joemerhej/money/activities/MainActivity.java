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
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.data.PieEntry;
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
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements Observer
{
    // general
    private static final String TAG = "MainActivity";
    private static final String PREF_USER_MOBILE_PHONE = "pref_user_mobile_phone";
    private static final int SMS_PERMISSION_CODE = 0;

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

    // views
    private TextView mAccountBalanceTextView;
    private ViewPager mViewPager;
    private MainChartsFragmentPagerAdapter mPagerAdapter;
    private TabLayout mMainChartsTabs;

    private ArrayList<PieEntry> mSpendingEntries = new ArrayList<>();
    private ArrayList<Integer> mSpendingColors = new ArrayList<>();
    private ArrayList<PieEntry> mIncomeEntries = new ArrayList<>();
    private ArrayList<Integer> mIncomeColors = new ArrayList<>();

    // data
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

        // =========================================================================================
        // PROTOTYPE SECTION
        // =========================================================================================
        // define views
        mPhoneNumberEditText = findViewById(R.id.phone_number_edit_text);
        mSmsToSendEditText = findViewById(R.id.sms_to_send_edit_text);

        mPhoneNumberEditText.setVisibility(View.GONE);
        mSmsToSendEditText.setVisibility(View.GONE);

        // auto populate phone number form shared preferences
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
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
        mAccountBalanceTextView = findViewById(R.id.account_balance_text_view);
        mViewPager = findViewById(R.id.main_charts_view_pager);
        mMainChartsTabs = findViewById(R.id.main_charts_tabs);


        // ask for permission to access sms
        if(!hasReadSmsPermission())
        {
            // should always call showRequestPermissionsInfoAlertDialog function and not requestReadSmsPermission directly to give the app dialog first
            showRequestPermissionsInfoAlertDialog();
        }
        else
        {
            // get the list of sms between 2 dates
            List<Sms> smsList = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy");
            try
            {
                Date fromDate = sdf.parse("25/10/2017");
                Date toDate = sdf.parse("03/11/2017");
                smsList = SmsUtils.getAllSms(this, "EmiratesNBD", fromDate, toDate);
            }
            catch(ParseException e)
            {
                e.printStackTrace();
            }

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
        }

        // mock main account
        mMainAccount.mock();

        // set up main account balance
        mAccountBalanceTextView.setText(NumberFormat.getNumberInstance(Locale.US).format(mMainAccount.getBalance().intValue()) + " " + mMainAccount.getCurrency().toString());

        // fill in the transactions by category
        populateCategoryAccounts();

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

    private void populateCategoryAccounts()
    {
        for(Transaction t : mMainAccount.getTransactions())
        {
            if(t.getCategory().compareTo(TransactionCategory.NONE.toString()) == 0)
            {
                mAccountNone.applyTransaction(t);
                continue;
            }
            if(t.getCategory().compareTo(TransactionCategory.SALARY.toString()) == 0)
            {
                mAccountSalary.applyTransaction(t);
                continue;
            }
            if(t.getCategory().compareTo(TransactionCategory.TRANSFER_IN.toString()) == 0)
            {
                mAccountTransferIn.applyTransaction(t);
                continue;
            }
            if(t.getCategory().compareTo(TransactionCategory.CASH.toString()) == 0)
            {
                mAccountCash.applyTransaction(t);
                continue;
            }
            if(t.getCategory().compareTo(TransactionCategory.TRANSFER_OUT.toString()) == 0)
            {
                mAccountTransferOut.applyTransaction(t);
                continue;
            }
            if(t.getCategory().compareTo(TransactionCategory.RENT.toString()) == 0)
            {
                mAccountRent.applyTransaction(t);
                continue;
            }
            if(t.getCategory().compareTo(TransactionCategory.BILLS.toString()) == 0)
            {
                mAccountBills.applyTransaction(t);
                continue;
            }
            if(t.getCategory().compareTo(TransactionCategory.GROCERIES.toString()) == 0)
            {
                mAccountGroceries.applyTransaction(t);
                continue;
            }
            if(t.getCategory().compareTo(TransactionCategory.FOOD.toString()) == 0)
            {
                mAccountFood.applyTransaction(t);
                continue;
            }
            if(t.getCategory().compareTo(TransactionCategory.ENTERTAINMENT.toString()) == 0)
            {
                mAccountEntertainment.applyTransaction(t);
                continue;
            }
            if(t.getCategory().compareTo(TransactionCategory.TRANSPORT.toString()) == 0)
            {
                mAccountTransport.applyTransaction(t);
                continue;
            }
            if(t.getCategory().compareTo(TransactionCategory.SPORTS.toString()) == 0)
            {
                mAccountSports.applyTransaction(t);
                continue;
            }
            if(t.getCategory().compareTo(TransactionCategory.SHOPPING.toString()) == 0)
            {
                mAccountShopping.applyTransaction(t);
                continue;
            }
            if(t.getCategory().compareTo(TransactionCategory.HEALTH.toString()) == 0)
            {
                mAccountHealth.applyTransaction(t);
                continue;
            }
            if(t.getCategory().compareTo(TransactionCategory.TRAVEL.toString()) == 0)
            {
                mAccountTravel.applyTransaction(t);
                continue;
            }
            if(t.getCategory().compareTo(TransactionCategory.PETS.toString()) == 0)
            {
                mAccountPets.applyTransaction(t);
                continue;
            }
            if(t.getCategory().compareTo(TransactionCategory.CARE.toString()) == 0)
            {
                mAccountPersonalCare.applyTransaction(t);
                continue;
            }
            if(t.getCategory().compareTo(TransactionCategory.OTHER.toString()) == 0)
            {
                mAccountOther.applyTransaction(t);
                continue;
            }
        }
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

        //noinspection SimplifiableIfStatement
        if(id == R.id.actions_calendar_range)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void update(Observable observable, Object data)
    {
        Toast.makeText(this, "observed!", Toast.LENGTH_SHORT).show();
    }

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
