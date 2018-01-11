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

    public static Currency fromString(String text)
    {
        for(Currency c : Currency.values())
        {
            if(c.mValue.equalsIgnoreCase(text))
                return c;
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}
