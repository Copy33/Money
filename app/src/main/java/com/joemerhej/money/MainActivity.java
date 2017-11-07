package com.joemerhej.money;

/**
 * Created by Joe Merhej on 11/2/17.
 */

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer
{
    // general
    private static final String TAG = "MainActivity";
    private static final String PREF_USER_MOBILE_PHONE = "pref_user_mobile_phone";
    private static final int SMS_PERMISSION_CODE = 0;

    // views
    private EditText mPhoneNumberEditText;
    private EditText mSmsToSendEditText;

    // properties
    private String mUserMobilePhone;
    private SharedPreferences mSharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // define views
        mPhoneNumberEditText = findViewById(R.id.phone_number_text_view);
        mSmsToSendEditText = findViewById(R.id.sms_to_send_edit_text);

        // auto populate phone number form shared preferences
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUserMobilePhone = mSharedPreferences.getString(PREF_USER_MOBILE_PHONE, "");

        if(!TextUtils.isEmpty(mUserMobilePhone))
            mPhoneNumberEditText.setText(mUserMobilePhone);

        // add this activity as an observer for our SMS observable
        SmsObservable.getInstance().addObserver(this);


//        if(!hasReadSmsPermission())
//        {
//            // should always call showRequestPermissionsInfoAlertDialog function and not requestReadSmsPermission directly to give the app dialog first
//            showRequestPermissionsInfoAlertDialog();
//        }
//        else
//        {
//            // get the list of sms between 2 dates
//            List<Sms> smsList = new ArrayList<>();
//            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy");
//            try
//            {
//                Date fromDate = sdf.parse("25/10/2017");
//                Date toDate = sdf.parse("03/11/2017");
//                smsList = SmsUtils.getAllSms(this, "EmiratesNBD", fromDate, toDate);
//            }
//            catch(ParseException e)
//            {
//                e.printStackTrace();
//            }
//
//            // create an account and go through every sms, retrieve transaction, and add it to the account
//            Account myAccount = new Account(Currency.AED);
//            String str = "";
//
//            for(Sms sms : smsList)
//            {
//                Transaction transaction = Transaction.from(sms);
//                myAccount.applyTransaction(transaction);
//
//                if(transaction != null)
//                {
//                    str += Transaction.from(sms).toString() + "\n";
//                }
//            }
//
//            Log.d(TAG, str);
//        }

        Account myAccount = new Account();
        myAccount.mock();

        Log.d(TAG, "asd");
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
        if(TextUtils.isEmpty(mUserMobilePhone) && !mUserMobilePhone.equals(mPhoneNumberEditText.getText().toString()))
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

    public void sync(View view)
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
