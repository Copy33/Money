package com.joemerhej.money.transaction;

/**
 * Created by Joe Merhej on 11/7/17.
 */

public enum TransactionCategory
{
    NONE("None"),
    INCOME("Income"),
    BILLS("Bills"),
    GROCERIES("Groceries"),
    FOOD("Food"),
    GOING_OUT("Going Out"),
    TRANSPORT("Transport"),
    SPORTS("Sports"),
    SHOPPING("Shopping"),
    HEALTH("Health"),
    TRAVEL("Travel"),
    PETS("Pets"),
    CARE("Personal Care"),
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
