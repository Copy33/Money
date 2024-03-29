package com.joemerhej.money.sms;

import java.util.Observable;

/**
 * Created by Joe Merhej on 11/3/17.
 */

public class SmsObservable extends Observable
{
    private static SmsObservable mInstance = new SmsObservable();


    private SmsObservable()
    {
    }

    public static SmsObservable getInstance()
    {
        return mInstance;
    }

    // methods to call
    public void updateValue(Object data)
    {
        synchronized(this)
        {
            setChanged();
            notifyObservers(data);
        }
    }

}
