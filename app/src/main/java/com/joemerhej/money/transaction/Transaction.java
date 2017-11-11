package com.joemerhej.money.transaction;

import com.joemerhej.money.account.Currency;
import com.joemerhej.money.sms.Sms;
import com.joemerhej.money.sms.SmsUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Joe Merhej on 11/3/17.
 */

public class Transaction
{
    private TransactionType mType;
    private BigDecimal mAmount;
    private Currency mCurrency;
    private Date mDate;
    private String mIssuer;
    private String mCategory;


    public Transaction()
    {
        mCurrency = mCurrency.AED;
        mAmount = BigDecimal.ZERO;
        mCategory = TransactionCategory.NONE.toString();
    }

    public Transaction(TransactionType mType, BigDecimal mAmount, Currency mCurrency, Date mDate, String mIssuer, String mCategory)
    {
        this.mType = mType;
        this.mAmount = mAmount;
        this.mCurrency = mCurrency;
        this.mDate = mDate;
        this.mIssuer = mIssuer;

        if(mCategory != null)
            this.mCategory = mCategory;
        else
            this.mCategory = TransactionCategory.NONE.toString();
    }

    public Transaction(Transaction transaction)
    {
        this.mType = transaction.mType;
        this.mAmount = transaction.mAmount;
        this.mCurrency = transaction.mCurrency;
        this.mDate = transaction.mDate;
        this.mIssuer = transaction.mIssuer;
        this.mCategory = transaction.mCategory;
    }

    public static Transaction from(Sms sms)
    {
        return SmsUtils.getTransactionFromSms(sms);
    }

    @Override
    public String toString()
    {
        return "Transaction{" +
                "mType=" + mType +
                ", mAmount=" + mAmount +
                ", mCurrency=" + mCurrency +
                ", mDate=" + mDate +
                ", mIssuer='" + mIssuer + '\'' +
                ", mCategory='" + mCategory + '\'' +
                '}';
    }

    public TransactionType getType()
    {
        return mType;
    }

    public void setType(TransactionType type)
    {
        mType = type;
    }

    public BigDecimal getAmount()
    {
        return mAmount;
    }

    public void setAmount(BigDecimal amount)
    {
        mAmount = amount;
    }

    public Currency getCurrency()
    {
        return mCurrency;
    }

    public void setCurrency(Currency currency)
    {
        mCurrency = currency;
    }

    public java.util.Date getDate()
    {
        return mDate;
    }

    public void setDate(java.util.Date date)
    {
        mDate = date;
    }

    public String getIssuer()
    {
        return mIssuer;
    }

    public void setIssuer(String issuer)
    {
        mIssuer = issuer;
    }

    public String getCategory()
    {
        return mCategory;
    }

    public void setCategory(String category)
    {
        mCategory = category;
    }
}
