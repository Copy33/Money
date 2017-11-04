package com.joemerhej.money;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joe Merhej on 11/3/17.
 */

public class Account
{
    private List<Transaction> mTransactions;
    private BigDecimal mBalance;
    private Currency mCurrency;


    public Account()
    {
        this.mTransactions = new ArrayList<>();
        this.mBalance = BigDecimal.ZERO;
        this.mCurrency = Currency.AED;
    }

    public Account(Currency mCurrency)
    {
        this.mTransactions = new ArrayList<>();
        this.mBalance = BigDecimal.ZERO;
        this.mCurrency = mCurrency;
    }

    public void applyTransaction(final Transaction transaction)
    {
        //TODO: check for transaction currency here and do the conversion to the account currency

        if(transaction == null)
            return;


        if(transaction.getType() == TransactionType.WITHDRAWAL)
        {
            mBalance = mBalance.subtract(transaction.getAmount());
        }
        else if(transaction.getType() == TransactionType.DEPOSIT)
        {
            mBalance = mBalance.add(transaction.getAmount());
        }

        mTransactions.add(transaction);
    }

    public void removeTransaction(Transaction transaction)
    {
        if(transaction == null)
            return;

        Transaction newTransaction = new Transaction(transaction);

        if(transaction.getType() == TransactionType.DEPOSIT)
            newTransaction.setType(TransactionType.WITHDRAWAL);
        else if(transaction.getType() == TransactionType.WITHDRAWAL)
            newTransaction.setType(TransactionType.DEPOSIT);

        applyTransaction(newTransaction);
    }

    public void clear()
    {
        mTransactions = new ArrayList<>();
        mBalance = BigDecimal.ZERO;
    }

    public List<Transaction> getTransactions()
    {
        return mTransactions;
    }

    public BigDecimal getBalance()
    {
        return mBalance;
    }

    public Currency getCurrency()
    {
        return mCurrency;
    }

}















