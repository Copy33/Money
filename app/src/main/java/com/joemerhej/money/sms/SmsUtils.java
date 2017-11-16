package com.joemerhej.money.sms;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.util.Log;

import com.joemerhej.money.account.Currency;
import com.joemerhej.money.transaction.Transaction;
import com.joemerhej.money.transaction.TransactionCategorizer;
import com.joemerhej.money.transaction.TransactionCategory;
import com.joemerhej.money.transaction.TransactionType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Joe Merhej on 11/2/17.
 */

public class SmsUtils
{
    private static final String TAG = "SmsUtils";

    // ============================================================================================================================================================
    // checks if valid phone number
    // ============================================================================================================================================================
    public static boolean isValidPhoneNumber(final String phoneNumber)
    {
        return android.util.Patterns.PHONE.matcher(phoneNumber).matches();
    }

    // ============================================================================================================================================================
    // sends an Sms to the specified number with the specified sms body
    // ============================================================================================================================================================
    public static void sendDebugSms(final String number, final String smsBody)
    {
        SmsManager smsManager = SmsManager.getDefault();

        ArrayList<String> smsParts = smsManager.divideMessage(smsBody);
        smsManager.sendMultipartTextMessage(number, null, smsParts, null, null);
    }

    // ============================================================================================================================================================
    // parses an sms body and creates a transaction
    // ============================================================================================================================================================
    public static Transaction getTransactionFromSms(final Sms sms)
    {
        Transaction transaction = new Transaction();

        String message = sms.getBody().toLowerCase();

        if(message.isEmpty())
            return null;

        // transaction date
        transaction.setDate(sms.getDateReceived());

        if(message.startsWith("payment of"))
        {
            // transaction type (depending on start of message)
            transaction.setType(TransactionType.WITHDRAWAL);

            // transaction amount (first number in the message)
            Pattern p = Pattern.compile("\\d*([.,]?\\d+)");
            Matcher m = p.matcher(message);

            if(m.find())
            {
                String firstNumber = m.group();
                transaction.setAmount(new BigDecimal(firstNumber.replaceAll(",", "")));

                // transaction currency (between first "of" and first number)
                transaction.setCurrency(Currency.valueOf(message.substring(message.indexOf("of") + 2, message.indexOf(firstNumber)).trim().toUpperCase()));

                // transaction issuer (between first "to" and first "with"
                transaction.setIssuer(message.substring(message.indexOf("to") + 2, message.indexOf("with")).trim());

                // set category using categorizer
                transaction.setCategory(TransactionCategorizer.getInstance().getCategoryFromString(transaction.getIssuer()));

                return transaction;
            }
        }
        else if(message.startsWith("purchase of"))
        {
            // transaction type (depending on start of message)
            transaction.setType(TransactionType.WITHDRAWAL);

            // transaction amount (first number in the message)
            Pattern p = Pattern.compile("\\d*([.,]?\\d+)");
            Matcher m = p.matcher(message);

            if(m.find())
            {
                String firstNumber = m.group();
                transaction.setAmount(new BigDecimal(firstNumber.replaceAll(",", "")));

                // transaction currency (between first "of" and first number)
                transaction.setCurrency(Currency.valueOf(message.substring(message.indexOf("of") + 2, message.indexOf(firstNumber)).trim().toUpperCase()));

                // transaction issuer (between first "at" and "Limit is {currency}"
                transaction.setIssuer(message.substring(message.indexOf("at") + 2, message.indexOf("limit is " + transaction.getCurrency().toString())).trim());

                // set category using categorizer
                transaction.setCategory(TransactionCategorizer.getInstance().getCategoryFromString(transaction.getIssuer()));

                return transaction;
            }
        }
        else if(message.contains("has been deducted"))
        {
            // transaction type (depending on start of message)
            transaction.setType(TransactionType.WITHDRAWAL);

            // transaction amount (first number in the message)
            Pattern p = Pattern.compile("\\d*([.,]?\\d+)");
            Matcher m = p.matcher(message);

            if(m.find())
            {
                String firstNumber = m.group();
                transaction.setAmount(new BigDecimal(firstNumber.replaceAll(",", "")));

                // transaction currency (between 0 and first number)
                transaction.setCurrency(Currency.valueOf(message.substring(0, message.indexOf(firstNumber)).trim().toUpperCase()));

                // category transfer out
                transaction.setCategory(TransactionCategory.TRANSFER_OUT.toString());

                return transaction;
            }
        }
        else if(message.contains("has been credited"))
        {
            // transaction type (depending on start of message)
            transaction.setType(TransactionType.DEPOSIT);

            // transaction amount (first number in the message)
            Pattern p = Pattern.compile("\\d*([.,]?\\d+)");
            Matcher m = p.matcher(message);

            if(m.find())
            {
                String firstNumber = m.group();
                transaction.setAmount(new BigDecimal(firstNumber.replaceAll(",", "")));

                // transaction currency (between 0 and first number)
                transaction.setCurrency(Currency.valueOf(message.substring(0, message.indexOf(firstNumber)).trim().toUpperCase()));

                if(message.contains("mepay"))
                    transaction.setCategory(TransactionCategory.TRANSFER_IN.toString());
                else
                    transaction.setCategory(TransactionCategory.SALARY.toString());

                return transaction;
            }
        }
        else if(message.contains("has been transferred to"))
        {
            // transaction type (depending on start of message)
            transaction.setType(TransactionType.DEPOSIT);

            // transaction amount (first number in the message)
            Pattern p = Pattern.compile("\\d*([.,]?\\d+)");
            Matcher m = p.matcher(message);

            if(m.find())
            {
                String firstNumber = m.group();
                transaction.setAmount(new BigDecimal(firstNumber.replaceAll(",", "")));

                // transaction currency (between 0 and first number)
                transaction.setCurrency(Currency.valueOf(message.substring(0, message.indexOf(firstNumber)).trim().toUpperCase()));

                // category transfer in
                transaction.setCategory(TransactionCategory.TRANSFER_IN.toString());

                return transaction;
            }
        }

        return null;
    }

    // ============================================================================================================================================================
    // returns a list of all sms on the device
    // ============================================================================================================================================================
    public static List<Sms> getAllSms(Context context)
    {
        List<Sms> smsList = new ArrayList<Sms>();

        Uri smsUri = Uri.parse("content://sms/inbox");

        Cursor cursor = context.getContentResolver().query(smsUri, null, null, null, null);

        while(cursor != null && cursor.moveToNext())
        {
            Long dateMillis = cursor.getLong(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.DATE));
            Date date = new Date(dateMillis);
            String threadId = cursor.getString(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.THREAD_ID));
            String address = cursor.getString(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.ADDRESS));
            String subject = cursor.getString(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.SUBJECT));
            String body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.TextBasedSmsColumns.BODY));

            Sms j = new Sms(date, threadId, address, subject, body);

            smsList.add(j);
        }

        if(cursor != null)
        {
            cursor.close();
        }

        return smsList;
    }

    // ============================================================================================================================================================
    // returns a list of all sms on the device by a specific address, could be a number or a name if number doesn't exist
    // ============================================================================================================================================================
    public static List<Sms> getAllSms(Context context, final String fromAddress)
    {
        List<Sms> smsList = new ArrayList<Sms>();

        Uri SmsUri = Uri.parse("content://sms/inbox");

        // filter only for provided address
        String filter = "address='" + fromAddress + "'";

        Cursor cursor = context.getContentResolver().query(SmsUri, null, filter, null, null);

        while(cursor != null && cursor.moveToNext())
        {
            Long dateMillis = cursor.getLong(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.DATE));
            Date date = new Date(dateMillis);
            String threadId = cursor.getString(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.THREAD_ID));
            String subject = cursor.getString(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.SUBJECT));
            String body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.TextBasedSmsColumns.BODY));


            smsList.add(new Sms(date, threadId, fromAddress, subject, body));
        }

        if(cursor != null)
        {
            cursor.close();
        }

        return smsList;
    }

    // ============================================================================================================================================================
    // returns a list of all sms on the device within a certain date range (excluding toDate)
    // ============================================================================================================================================================
    public static List<Sms> getAllSms(Context context, final Date fromDate, final Date toDate)
    {
        List<Sms> smsList = new ArrayList<Sms>();

        Uri smsUri = Uri.parse("content://sms/inbox");
        String filter;

        // if fromDate comes after toDate
        if(fromDate.compareTo(toDate) > 0)
        {
            Log.e(TAG, "Incorrect date range, fromDate should be before toDate.");
            return null;
        }

        // if fromDate is the same as toDate
        else if(fromDate.compareTo(toDate) == 0)
        {
            Long one_day = Long.valueOf(24 * 60 * 60 * 1000);
            Date oneDayLater = new Date(fromDate.getTime() + one_day);

            filter = "date between '" + fromDate.getTime() + "' and '" + oneDayLater.getTime() + "'";
        }

        // if fromDate comes before toDate
        else
        {
            filter = "date between '" + fromDate.getTime() + "' and '" + toDate.getTime() + "'";
        }

        Cursor cursor = context.getContentResolver().query(smsUri, null, filter, null, null);

        while(cursor != null && cursor.moveToNext())
        {
            Long dateMillis = cursor.getLong(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.DATE));
            Date date = new Date(dateMillis);
            String threadId = cursor.getString(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.THREAD_ID));
            String address = cursor.getString(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.ADDRESS));
            String subject = cursor.getString(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.SUBJECT));
            String body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.TextBasedSmsColumns.BODY));

            Sms j = new Sms(date, threadId, address, subject, body);

            smsList.add(j);
        }

        if(cursor != null)
        {
            cursor.close();
        }

        return smsList;
    }

    // ============================================================================================================================================================
    // returns a list of all sms on the device from a specific address received between a date range
    // ============================================================================================================================================================
    public static List<Sms> getAllSms(Context context, final String fromAddress, final Date fromDate, final Date toDate)
    {
        List<Sms> smsList = new ArrayList<Sms>();

        Uri smsUri = Uri.parse("content://sms/inbox");
        String filter;

        // if fromDate comes after toDate
        if(fromDate.compareTo(toDate) > 0)
        {
            Log.e(TAG, "Incorrect date range, fromDate should be before toDate.");
            return null;
        }

        // if fromDate is the same as toDate
        else if(fromDate.compareTo(toDate) == 0)
        {
            Long one_day = Long.valueOf(24 * 60 * 60 * 1000);
            Date oneDayLater = new Date(fromDate.getTime() + one_day);

            filter = "address='" + fromAddress + "' and date between '" + fromDate.getTime() + "' and '" + oneDayLater.getTime() + "'";
        }

        // if fromDate comes before toDate
        else
        {
            filter = "address='" + fromAddress + "' and date between '" + fromDate.getTime() + "' and '" + toDate.getTime() + "'";
        }

        Cursor cursor = context.getContentResolver().query(smsUri, null, filter, null, null);

        while(cursor != null && cursor.moveToNext())
        {
            Long dateMillis = cursor.getLong(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.DATE));
            Date date = new Date(dateMillis);
            String threadId = cursor.getString(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.THREAD_ID));
            String address = cursor.getString(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.ADDRESS));
            String subject = cursor.getString(cursor.getColumnIndex(Telephony.TextBasedSmsColumns.SUBJECT));
            String body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.TextBasedSmsColumns.BODY));

            Sms j = new Sms(date, threadId, address, subject, body);

            smsList.add(j);
        }

        if(cursor != null)
        {
            cursor.close();
        }

        return smsList;
    }
}




























