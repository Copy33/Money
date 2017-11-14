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
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.mikephil.charting.data.PieEntry;
import com.joemerhej.money.R;
import com.joemerhej.money.account.Account;
import com.joemerhej.money.account.Currency;
import com.joemerhej.money.adapters.MainChartsFragmentPagerAdapter;
import com.joemerhej.money.sms.Sms;
import com.joemerhej.money.sms.SmsObservable;
import com.joemerhej.money.sms.SmsUtils;
import com.joemerhej.money.transaction.Transaction;
import com.joemerhej.money.transaction.TransactionCategory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
    private ViewPager mViewPager;
    private MainChartsFragmentPagerAdapter mPagerAdapter;
    private TabLayout mMainChartsTabs;

    // data
    private Account mMainAccount = new Account();

    private Account mAccountIncome = new Account();
    private Account mAccountBills = new Account();
    private Account mAccountGroceries = new Account();
    private Account mAccountFood = new Account();
    private Account mAccountGoingOut = new Account();
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

        // set up views
        mViewPager = findViewById(R.id.main_charts_view_pager);
        mMainChartsTabs = findViewById(R.id.main_charts_tabs);


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
                Date fromDate = sdf.parse("25/10/2016");
                Date toDate = sdf.parse("03/11/2018");
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

//        // setup main account
//        mMainAccount.mock();
        
        // fill in the transactions by category
        populateAccounts(mMainAccount.getTransactions());

        // set up pie chart
        ArrayList<PieEntry> entries = new ArrayList<>();

        float labelNumber = mAccountBills.getBalance().abs().floatValue();
        if(Float.compare(labelNumber, 0.0f) != 0)
            entries.add(new PieEntry(labelNumber, TransactionCategory.BILLS.toString()));

        labelNumber = mAccountGroceries.getBalance().abs().floatValue();
        if(Float.compare(labelNumber, 0.0f) != 0)
            entries.add(new PieEntry(labelNumber, TransactionCategory.GROCERIES.toString()));

        labelNumber = mAccountFood.getBalance().abs().floatValue();
        if(Float.compare(labelNumber, 0.0f) != 0)
            entries.add(new PieEntry(labelNumber, TransactionCategory.FOOD.toString()));

        labelNumber = mAccountGoingOut.getBalance().abs().floatValue();
        if(Float.compare(labelNumber, 0.0f) != 0)
            entries.add(new PieEntry(labelNumber, TransactionCategory.GOING_OUT.toString()));

        labelNumber = mAccountTransport.getBalance().abs().floatValue();
        if(Float.compare(labelNumber, 0.0f) != 0)
            entries.add(new PieEntry(labelNumber, TransactionCategory.TRANSPORT.toString()));

        labelNumber = mAccountSports.getBalance().abs().floatValue();
        if(Float.compare(labelNumber, 0.0f) != 0)
            entries.add(new PieEntry(labelNumber, TransactionCategory.SPORTS.toString()));

        labelNumber = mAccountShopping.getBalance().abs().floatValue();
        if(Float.compare(labelNumber, 0.0f) != 0)
            entries.add(new PieEntry(labelNumber, TransactionCategory.SHOPPING.toString()));

        labelNumber = mAccountHealth.getBalance().abs().floatValue();
        if(Float.compare(labelNumber, 0.0f) != 0)
            entries.add(new PieEntry(labelNumber, TransactionCategory.HEALTH.toString()));

        labelNumber = mAccountTravel.getBalance().abs().floatValue();
        if(Float.compare(labelNumber, 0.0f) != 0)
            entries.add(new PieEntry(labelNumber, TransactionCategory.TRAVEL.toString()));

        labelNumber = mAccountPets.getBalance().abs().floatValue();
        if(Float.compare(labelNumber, 0.0f) != 0)
            entries.add(new PieEntry(labelNumber, TransactionCategory.PETS.toString()));

        labelNumber = mAccountPersonalCare.getBalance().abs().floatValue();
        if(Float.compare(labelNumber, 0.0f) != 0)
            entries.add(new PieEntry(labelNumber, TransactionCategory.CARE.toString()));

        labelNumber = mAccountOther.getBalance().abs().floatValue();
        if(Float.compare(labelNumber, 0.0f) != 0)
            entries.add(new PieEntry(labelNumber, TransactionCategory.OTHER.toString()));

        mPagerAdapter = new MainChartsFragmentPagerAdapter(getSupportFragmentManager(), entries);
        mViewPager.setAdapter(mPagerAdapter);
        mMainChartsTabs.setupWithViewPager(mViewPager);
        mMainChartsTabs.getTabAt(0).setText(getResources().getString(R.string.spending_title));
        mMainChartsTabs.getTabAt(1).setText(getResources().getString(R.string.income_title));

    }

    private void populateAccounts(List<Transaction> allTransactions)
    {
        for(Transaction t : allTransactions)
        {
            if(t.getCategory().compareTo(TransactionCategory.NONE.toString()) == 0)
            {
                mAccountNone.applyTransaction(t);
                continue;
            }
            if(t.getCategory().compareTo(TransactionCategory.INCOME.toString()) == 0)
            {
                mAccountIncome.applyTransaction(t);
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
            if(t.getCategory().compareTo(TransactionCategory.GOING_OUT.toString()) == 0)
            {
                mAccountGoingOut.applyTransaction(t);
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
