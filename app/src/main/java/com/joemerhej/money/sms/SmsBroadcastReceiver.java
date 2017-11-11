package com.joemerhej.money.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;

/**
 * Created by Joe Merhej on 11/2/17.
 */

public class SmsBroadcastReceiver extends BroadcastReceiver
{
    private static final String TAG = "SmsBroadcastReceiver";


    @Override
    public void onReceive(Context context, Intent intent)
    {
        // if the app receives an sms received action
        if(intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION))
        {
            String smsSender = "";
            String smsBody = "";

            for(SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent))
            {
                smsBody += smsMessage.getMessageBody();
            }

            if(smsBody.startsWith("1234"))
            {
                SmsObservable.getInstance().updateValue(intent);
            }
        }
    }
}
