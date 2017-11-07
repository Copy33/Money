package com.joemerhej.money;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    public Account(final Currency mCurrency)
    {
        this.mTransactions = new ArrayList<>();
        this.mBalance = BigDecimal.ZERO;
        this.mCurrency = mCurrency;
    }

    public static Account from(final List<Transaction> transactions)
    {
        Account newAccount = new Account();
        for(Transaction t : transactions)
        {
            newAccount.applyTransaction(t);
        }
        return newAccount;
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

    public void populateWithMockData()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
        try
        {
            applyTransaction( new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(18.00), Currency.AED, sdf.parse("02/11/2017 16:57:34"), "CAREEM NETWORKS FZ LLC", null));
            applyTransaction( new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(379.85), Currency.AED, sdf.parse("02/11/2017 16:26:26"), "CARREFOUR BR OF -472603, DUBAI. Avl Cr.", null));
            applyTransaction( new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(21.00), Currency.AED, sdf.parse("02/11/2017 15:52:32"), "CAREEM NETWORKS FZ LLC", null));
            applyTransaction( new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(25.00), Currency.AED, sdf.parse("02/11/2017 14:31:43"), "CAREEM NETWORKS FZ LLC", null));
            applyTransaction( new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(27.00), Currency.AED, sdf.parse("02/11/2017 10:48:51"), "CAREEM NETWORKS FZ LLC", null));
            applyTransaction( new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(31.00), Currency.AED, sdf.parse("02/11/2017 10:13:56"), "www.turnlunchon.com, INTERNET. Avl Cr.", null));
            applyTransaction( new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(616.30), Currency.AED, sdf.parse("01/11/2017 22:23:05"), "Smart Dubai Government, Dubai. Avl Cr.", null));
            applyTransaction( new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(5.14), Currency.AED, sdf.parse("01/11/2017 22:22:32"), "Smart Dubai Government, Dubai. Avl Cr.", null));
            applyTransaction( new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(570.00), Currency.AED, sdf.parse("01/11/2017 21:28:00"), "DU (EITC), DUBAI. Avl Cr.", null));
            applyTransaction( new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(28.00), Currency.AED, sdf.parse("01/11/2017 20:49:32"), "CAREEM NETWORKS FZ LLC", null));
            applyTransaction( new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(34.00), Currency.AED, sdf.parse("01/11/2017 14:12:43"), "BAKEMART PLUS   -739803, DUBAI. Avl Cr.", null));
            applyTransaction( new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(63.00), Currency.AED, sdf.parse("01/11/2017 13:39:13"), "CAREEM NETWORKS FZ LLC", null));
            applyTransaction( new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(370.00), Currency.AED, sdf.parse("01/11/2017 12:44:55"), "BELHASA DRIVING -883401, DUBAI. Avl Cr.", null));
            applyTransaction( new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(78.00), Currency.AED, sdf.parse("01/11/2017 11:07:19"), "CAREEM NETWORKS FZ LLC", null));
            applyTransaction( new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(34.00), Currency.AED, sdf.parse("31/10/2017 15:04:45"), "CAREEM NETWORKS FZ LLC", null));
            applyTransaction( new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(78.00), Currency.AED, sdf.parse("31/10/2017 13:58:54"), "CAREEM NETWORKS FZ LLC", null));
            applyTransaction( new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(83.00), Currency.AED, sdf.parse("31/10/2017 12:48:32"), "CAREEM NETWORKS FZ LLC", null));
            applyTransaction( new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(35.00), Currency.AED, sdf.parse("31/10/2017 01:15:51"), "CAREEM NETWORKS FZ LLC", null));
            applyTransaction( new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(34.00), Currency.AED, sdf.parse("30/10/2017 21:14:13"), "CAREEM NETWORKS FZ LLC", null));
            applyTransaction( new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(26.00), Currency.AED, sdf.parse("30/10/2017 16:06:14"), "CAREEM NETWORKS FZ LLC", null));
            applyTransaction( new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(84.29), Currency.AED, sdf.parse("30/10/2017 15:22:07"), "Lootah BCGAs LLC, Dubai. Avl Cr.", null));
            applyTransaction( new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(28.00), Currency.AED, sdf.parse("30/10/2017 10:20:16"), "CAREEM NETWORKS FZ LLC", null));
            applyTransaction( new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(32.00), Currency.AED, sdf.parse("30/10/2017 09:49:53"), "www.turnlunchon.com, INTERNET. Avl Cr.", null));
            applyTransaction( new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(1040), Currency.AED, sdf.parse("29/10/2017 19:17:25"), null, null));
            applyTransaction( new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(80.00), Currency.AED, sdf.parse("29/10/2017 18:12:23"), "NATIONAL TAXI, DUBAI. Avl Cr.", null));

        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }


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















