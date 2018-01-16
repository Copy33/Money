package com.joemerhej.money.storage;

/**
 * Created by Joe Merhej on 1/16/18.
 */

public class StorageManager
{
    public static StorageManager mInstance = null;


    private StorageManager()
    {

    }

    public static StorageManager getInstance()
    {
        if(mInstance == null)
        {
            mInstance = new StorageManager();
        }

        return mInstance;
    }
}
