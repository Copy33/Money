package com.joemerhej.money;

/**
 * Created by Joe Merhej on 11/3/17.
 */

public enum Currency
{
    AED("AED");

    private final String mValue;

    Currency(String aed)
    {
        this.mValue = aed;
    }

    @Override
    public String toString()
    {
        return mValue;
    }
}
