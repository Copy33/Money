package com.joemerhej.money.account;

/**
 * Created by Joe Merhej on 11/3/17.
 */

public enum Currency
{
    AED("aed");

    private final String mValue;

    Currency(String value)
    {
        this.mValue = value;
    }

    @Override
    public String toString()
    {
        return mValue;
    }
}
