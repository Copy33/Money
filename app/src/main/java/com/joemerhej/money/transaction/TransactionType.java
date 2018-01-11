package com.joemerhej.money.transaction;

/**
 * Created by Joe Merhej on 11/3/17.
 */

public enum TransactionType
{
    DEPOSIT("DEPOSIT"),
    WITHDRAWAL("WITHDRAWAL");

    private final String mValue;

    TransactionType(String type)
    {
        this.mValue = type;
    }

    @Override
    public String toString()
    {
        return mValue;
    }

    public static TransactionType fromString(String text)
    {
        for(TransactionType c : TransactionType.values())
        {
            if(c.mValue.equalsIgnoreCase(text))
                return c;
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}
