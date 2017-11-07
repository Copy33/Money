package com.joemerhej.money;

/**
 * Created by Joe Merhej on 11/7/17.
 */

public enum TransactionCategory
{
    PAYCHECK("Paycheck"),
    BILLS("Bills"),
    GROCERIES("Groceries"),
    FOOD("Food"),
    GOING_OUT("Going Out"),
    TRANSPORT("Transport"),
    SPORTS("Sports"),
    SHOPPING("Shopping"),
    HEALTH("Health"),
    GIFTS("Gifts"),
    TRAVEL("Travel"),
    PETS("Pets"),
    OTHER("Other"),
    NONE("None");

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
