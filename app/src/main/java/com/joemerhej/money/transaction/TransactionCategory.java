package com.joemerhej.money.transaction;

/**
 * Created by Joe Merhej on 11/7/17.
 */

public enum TransactionCategory
{
    NONE("None"),                   // black

    SALARY("Salary"),               // green_salary
    TRANSFER_IN("Transfer In"),     // green_transfer
    CASH("Cash"),                   // green_cash

    TRANSFER_OUT("Transfer Out"),   // blue_transfer
    RENT("Rent"),                   // blue
    TRANSPORT("Transport"),         // yellow
    ENTERTAINMENT("Entertainment"), // pink
    FOOD("Food"),                   // green
    BILLS("Bills"),                 // red
    TRAVEL("Travel"),               // brown
    SHOPPING("Shopping"),           // orange
    GROCERIES("Groceries"),         // turquoise
    CARE("Care"),                   // lime
    SPORTS("Sports"),               // purple
    HEALTH("Health"),               // navy
    PETS("Pets"),                   // grey
    OTHER("Other");                 // silver

    private final String mValue;

    TransactionCategory(final String value)
    {
        mValue = value;
    }

    @Override
    public String toString()
    {
        return mValue;
    }

    public static TransactionCategory fromString(String text)
    {
        for(TransactionCategory c : TransactionCategory.values())
        {
            if(c.mValue.equalsIgnoreCase(text))
                return c;
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}
