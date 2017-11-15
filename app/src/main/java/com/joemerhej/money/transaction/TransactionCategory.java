package com.joemerhej.money.transaction;

/**
 * Created by Joe Merhej on 11/7/17.
 */

public enum TransactionCategory
{
    NONE("None"),

    SALARY("Salary"),
    TRANSFER_IN("Transfer In"),
    CASH("Cash"),

    TRANSFER_OUT("Transfer Out"),
    RENT("Rent"),
    TRANSPORT("Transport"),
    ENTERTAINMENT("Entertainment"),
    FOOD("Food"),
    BILLS("Bills"),
    TRAVEL("Travel"),
    SHOPPING("Shopping"),
    GROCERIES("Groceries"),
    CARE("Care"),
    SPORTS("Sports"),
    HEALTH("Health"),
    PETS("Pets"),
    OTHER("Other");

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
}
