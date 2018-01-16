package com.joemerhej.money.account;

/**
 * Created by Joe Merhej on 1/16/18.
 */

public class AccountManager
{
    public static AccountManager mInstance = null;


    private AccountManager()
    {

    }

    public static AccountManager getInstance()
    {
        if(mInstance == null)
        {
            mInstance = new AccountManager();
        }

        return mInstance;
    }
}
