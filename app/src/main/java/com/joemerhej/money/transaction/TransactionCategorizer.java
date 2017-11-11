package com.joemerhej.money.transaction;


import android.util.Log;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Joe Merhej on 11/11/17.
 */

public class TransactionCategorizer
{
    private static final String TAG = "Categorizer";

    private static TransactionCategorizer mInstance = null;
    private Map<String, String> mCategoryMap = new HashMap<>();


    private TransactionCategorizer()
    {
    }

    public static TransactionCategorizer getInstance()
    {
        if(mInstance == null)
        {
            mInstance = new TransactionCategorizer();
            mInstance.initWithMockMap();
        }

        return mInstance;
    }

    public void initWithMap(HashMap<String, String> map)
    {
        mCategoryMap = map;
    }

    public void initWithMockMap()
    {
        mCategoryMap.put("du", "Bills");
        mCategoryMap.put("dewa", "Bills");
        mCategoryMap.put("lootah", "Bills");
        mCategoryMap.put("etisalat", "Bills");
        mCategoryMap.put("government", "Bills");
        mCategoryMap.put("belhasa", "Bills");

        mCategoryMap.put("carrefour", "Groceries");
        mCategoryMap.put("spinneys", "Groceries");
        mCategoryMap.put("lifco", "Groceries");
        mCategoryMap.put("waitrose", "Groceries");
        mCategoryMap.put("lulu", "Groceries");
        mCategoryMap.put("aswaaq", "Groceries");
        mCategoryMap.put("market", "Groceries");
        mCategoryMap.put("supermarket", "Groceries");
        mCategoryMap.put("minimarket", "Groceries");
        mCategoryMap.put("hypermarket", "Groceries");

        mCategoryMap.put("food", "Food");
        mCategoryMap.put("restaurant", "Food");
        mCategoryMap.put("eat", "Food");
        mCategoryMap.put("eats", "Food");
        mCategoryMap.put("turnlunchon", "Food");
        mCategoryMap.put("zomato", "Food");
        mCategoryMap.put("deliveroo", "Food");
        mCategoryMap.put("talabat", "Food");
        mCategoryMap.put("bakemart", "Food");
        mCategoryMap.put("mcdonalds", "Food");
        mCategoryMap.put("nutrition", "Food");
        mCategoryMap.put("zaatar", "Food");
        mCategoryMap.put("albahr", "Food");
        mCategoryMap.put("salt", "Food");
        mCategoryMap.put("cheese", "Food");
        mCategoryMap.put("cake", "Food");
        mCategoryMap.put("blacktap", "Food");
        mCategoryMap.put("meridien", "Food");

        mCategoryMap.put("club", "Going out");
        mCategoryMap.put("drink", "Going out");
        mCategoryMap.put("beverage", "Going out");
        mCategoryMap.put("jetty", "Going out");

        mCategoryMap.put("taxi", "Transport");
        mCategoryMap.put("uber", "Transport");
        mCategoryMap.put("careem", "Transport");
        mCategoryMap.put("metro", "Transport");
        mCategoryMap.put("rta", "Transport");

        mCategoryMap.put("sport", "Sports");
        mCategoryMap.put("sports", "Sports");
        mCategoryMap.put("gym", "Sports");
        mCategoryMap.put("fitness", "Sports");
        mCategoryMap.put("football", "Sports");

        mCategoryMap.put("shop", "Shopping");
        mCategoryMap.put("shops", "Shopping");
        mCategoryMap.put("shopping", "Shopping");
        mCategoryMap.put("mall", "Shopping");
        mCategoryMap.put("apparel", "Shopping");
        mCategoryMap.put("cloth", "Shopping");
        mCategoryMap.put("clothes", "Shopping");
        mCategoryMap.put("furniture", "Shopping");
        mCategoryMap.put("souq", "Shopping");
        mCategoryMap.put("noon", "Shopping");

        mCategoryMap.put("health", "Health");
        mCategoryMap.put("hospital", "Health");
        mCategoryMap.put("pharmacy", "Health");
        mCategoryMap.put("life", "Health");

        mCategoryMap.put("emirates", "Travel");
        mCategoryMap.put("mea", "Travel");
        mCategoryMap.put("fly", "Travel");
        mCategoryMap.put("travel", "Travel");
        mCategoryMap.put("airline", "Travel");

        mCategoryMap.put("pet", "Pets");
        mCategoryMap.put("pets", "Pets");
        mCategoryMap.put("veterinary", "Pets");
        mCategoryMap.put("dog", "Pets");
        mCategoryMap.put("cat", "Pets");

        mCategoryMap.put("chaps", "Care");
    }

    public String getCategoryFromString(String text)
    {
        if(text == null)
            return TransactionCategory.NONE.toString();

        // lower case it
        text = text.toLowerCase();

        // split on alphanumericals
        String[] splits = text.split("\\W+");

        for(String s : splits)
        {
            if(mCategoryMap.containsKey(s))
            {
                //TODO: instead of return the first one found, keep track of matches and return highest number
                return mCategoryMap.get(s);
            }
        }

        Log.d(TAG, "Couldn't categorize: " + text);

        return TransactionCategory.NONE.toString();
    }
}































