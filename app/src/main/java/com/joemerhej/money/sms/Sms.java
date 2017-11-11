package com.joemerhej.money.sms;

import java.util.Date;

/**
 * Created by Joe Merhej on 11/2/17.
 */

public class Sms
{
    private Date mDateReceived;
    private String mThreadId;
    private String mAddress;
    private String mSubject;
    private String mBody;


    public Sms()
    {
    }

    public Sms(Date dateReceived, String threadId, String address, String subject, String body)
    {
        this.mDateReceived = dateReceived;
        this.mThreadId = threadId;
        this.mAddress = address;
        this.mSubject = subject;
        this.mBody = body;
    }

    public Date getDateReceived()
    {
        return mDateReceived;
    }

    public String getThreadId()
    {
        return mThreadId;
    }

    public String getAddress()
    {
        return mAddress;
    }

    public String getSubject()
    {
        return mSubject;
    }

    public String getBody()
    {
        return mBody;
    }
}

