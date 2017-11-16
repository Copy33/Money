package com.joemerhej.money.account;

import android.util.Log;

import com.joemerhej.money.transaction.Transaction;
import com.joemerhej.money.transaction.TransactionType;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joe Merhej on 11/3/17.
 */

public class Account
{
    private static final String TAG = "Account";

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

        if(!mTransactions.contains(transaction))
            return;

        mTransactions.remove(transaction);

        if(transaction.getType() == TransactionType.DEPOSIT)
            mBalance = mBalance.add(transaction.getAmount());
        else if(transaction.getType() == TransactionType.WITHDRAWAL)
            mBalance = mBalance.subtract(transaction.getAmount());

    }

    public void removeTransactions(List<Transaction> transactions)
    {
        if(transactions.isEmpty())
            return;

        if(!transactions.containsAll(transactions))
        {
            Log.d(TAG, "SKIP removing transactions from account - not all transactions are present");
            return;
        }

        mTransactions.removeAll(transactions);

        for(Transaction t : transactions)
        {
            if(t.getType() == TransactionType.DEPOSIT)
                mBalance = mBalance.subtract(t.getAmount());
            else if(t.getType() == TransactionType.WITHDRAWAL)
                mBalance = mBalance.add(t.getAmount());
        }
    }

    public void clear()
    {
        mTransactions = new ArrayList<>();
        mBalance = BigDecimal.ZERO;
    }

    public void mock()
    {
        clear();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
        try
        {
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(18.00), Currency.AED, sdf.parse("02/11/2017 16:57:34"), "CAREEM NETWORKS FZ LLC", "Transport"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(379.85), Currency.AED, sdf.parse("02/11/2017 16:26:26"), "CARREFOUR BR OF -472603, DUBAI. Avl Cr.", "Groceries"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(21.00), Currency.AED, sdf.parse("02/11/2017 15:52:32"), "CAREEM NETWORKS FZ LLC", "Transport"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(25.00), Currency.AED, sdf.parse("02/11/2017 14:31:43"), "CAREEM NETWORKS FZ LLC", "Transport"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(27.00), Currency.AED, sdf.parse("02/11/2017 10:48:51"), "CAREEM NETWORKS FZ LLC", "Transport"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(31.00), Currency.AED, sdf.parse("02/11/2017 10:13:56"), "www.turnlunchon.com, INTERNET. Avl Cr.", "Food"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(616.30), Currency.AED, sdf.parse("01/11/2017 22:23:05"), "Smart Dubai Government, Dubai. Avl Cr.", "Bills"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(5.14), Currency.AED, sdf.parse("01/11/2017 22:22:32"), "Smart Dubai Government, Dubai. Avl Cr.", "Bills"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(570.00), Currency.AED, sdf.parse("01/11/2017 21:28:00"), "DU (EITC), DUBAI. Avl Cr.", "Bills"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(28.00), Currency.AED, sdf.parse("01/11/2017 20:49:32"), "CAREEM NETWORKS FZ LLC", "Transport"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(34.00), Currency.AED, sdf.parse("01/11/2017 14:12:43"), "BAKEMART PLUS   -739803, DUBAI. Avl Cr.", "Food"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(63.00), Currency.AED, sdf.parse("01/11/2017 13:39:13"), "CAREEM NETWORKS FZ LLC", "Transport"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(370.00), Currency.AED, sdf.parse("01/11/2017 12:44:55"), "BELHASA DRIVING -883401, DUBAI. Avl Cr.", "Bills"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(78.00), Currency.AED, sdf.parse("01/11/2017 11:07:19"), "CAREEM NETWORKS FZ LLC", "Transport"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(34.00), Currency.AED, sdf.parse("31/10/2017 15:04:45"), "CAREEM NETWORKS FZ LLC", "Transport"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(78.00), Currency.AED, sdf.parse("31/10/2017 13:58:54"), "CAREEM NETWORKS FZ LLC", "Transport"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(83.00), Currency.AED, sdf.parse("31/10/2017 12:48:32"), "CAREEM NETWORKS FZ LLC", "Transport"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(35.00), Currency.AED, sdf.parse("31/10/2017 01:15:51"), "CAREEM NETWORKS FZ LLC", "Transport"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(34.00), Currency.AED, sdf.parse("30/10/2017 21:14:13"), "CAREEM NETWORKS FZ LLC", "Transport"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(26.00), Currency.AED, sdf.parse("30/10/2017 16:06:14"), "CAREEM NETWORKS FZ LLC", "Transport"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(84.29), Currency.AED, sdf.parse("30/10/2017 15:22:07"), "Lootah BCGAs LLC, Dubai. Avl Cr.", "Bills"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(28.00), Currency.AED, sdf.parse("30/10/2017 10:20:16"), "CAREEM NETWORKS FZ LLC", "Transport"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(32.00), Currency.AED, sdf.parse("30/10/2017 09:49:53"), "www.turnlunchon.com, INTERNET. Avl Cr.", "Food"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(1040), Currency.AED, sdf.parse("29/10/2017 19:17:25"), null, "Transfer Out"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(80.00), Currency.AED, sdf.parse("29/10/2017 18:12:23"), "NATIONAL TAXI, DUBAI. Avl Cr.", "Transport"));
            applyTransaction(new Transaction(TransactionType.DEPOSIT, new BigDecimal(19176), Currency.AED, sdf.parse("29/10/2017 15:03:20"), null, "Transfer In"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(19176), Currency.AED, sdf.parse("29/10/2017 15:02:55"), null, "Transfer Out"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(1840), Currency.AED, sdf.parse("29/10/2017 15:01:23"), null, "Transfer Out"));
            applyTransaction(new Transaction(TransactionType.DEPOSIT, new BigDecimal(30000), Currency.AED, sdf.parse("29/10/2017 14:54:45"), null, "Salary"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(28.00), Currency.AED, sdf.parse("29/10/2017 10:44:21"), "CAREEM NETWORKS FZ LLC", "Transport"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(30.00), Currency.AED, sdf.parse("29/10/2017 10:00:52"), "www.turnlunchon.com, INTERNET. Avl Cr.", "Food"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(18.00), Currency.AED, sdf.parse("28/10/2017 21:38:03"), "CAREEM NETWORKS FZ LLC", "Transport"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(396.30), Currency.AED, sdf.parse("28/10/2017 21:27:03"), "CARREFOUR BR OF -472603, DUBAI. Avl Cr.", "Groceries"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(22.00), Currency.AED, sdf.parse("28/10/2017 20:29:23"), "CAREEM NETWORKS FZ LLC", "Transport"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(99.00), Currency.AED, sdf.parse("28/10/2017 13:00:21"), "ZOMATO ORDER, DUBAI. Avl Cr.", "Food"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(65.00), Currency.AED, sdf.parse("28/10/2017 02:31:02"), "CAREEM NETWORKS FZ LLC", "Transport"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(29.00), Currency.AED, sdf.parse("27/10/2017 16:23:02"), "CAREEM NETWORKS FZ LLC", "Transport"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(40.00), Currency.AED, sdf.parse("27/10/2017 15:01:04"), "LE MERIDIEN MINA SEYAHI, DUBAI. Avl Cr.", "Food"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(75.00), Currency.AED, sdf.parse("27/10/2017 14:58:52"), "LE MERIDIEN MINA SEYAHI, DUBAI. Avl Cr.", "Food"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(49.00), Currency.AED, sdf.parse("27/10/2017 13:18:03"), "CAREEM NETWORKS FZ LLC", "Transport"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(29.00), Currency.AED, sdf.parse("27/10/2017 01:44:42"), "CAREEM NETWORKS FZ LLC", "Transport"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(33.00), Currency.AED, sdf.parse("26/10/2017 21:16:04"), "CAREEM NETWORKS FZ LLC", "Transport"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(30.00), Currency.AED, sdf.parse("26/10/2017 16:39:33"), "CAREEM NETWORKS FZ LLC", "Transport"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(31.00), Currency.AED, sdf.parse("26/10/2017 13:36:06"), "BAKEMART PLUS   -739803, DUBAI. Avl Cr.", "Food"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(29.00), Currency.AED, sdf.parse("26/10/2017 10:44:12"), "CAREEM NETWORKS FZ LLC", "Transport"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(34.00), Currency.AED, sdf.parse("25/10/2017 20:47:14"), "CAREEM NETWORKS FZ LLC", "Transport"));
            applyTransaction(new Transaction(TransactionType.DEPOSIT, new BigDecimal(2202), Currency.AED, sdf.parse("25/10/2017 20:45:42"), null, "Transfer In"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(27.00), Currency.AED, sdf.parse("25/10/2017 17:59:53"), "CAREEM NETWORKS FZ LLC", "Transport"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(34.00), Currency.AED, sdf.parse("25/10/2017 14:36:14"), "ZOMATO ORDER, DUBAI. Avl Cr.", "Food"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(27.00), Currency.AED, sdf.parse("25/10/2017 10:36:42"), "CAREEM NETWORKS FZ LLC", "Transport"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(33.00), Currency.AED, sdf.parse("25/10/2017 10:32:42"), "AWESOME PLACE", "None"));
            applyTransaction(new Transaction(TransactionType.WITHDRAWAL, new BigDecimal(103.00), Currency.AED, sdf.parse("25/10/2017 10:31:42"), "NEW PLACE", "None"));
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















