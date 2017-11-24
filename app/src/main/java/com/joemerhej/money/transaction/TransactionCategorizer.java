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
        mCategoryMap.put("du", TransactionCategory.BILLS.toString());
        mCategoryMap.put("dewa", TransactionCategory.BILLS.toString());
        mCategoryMap.put("lootah", TransactionCategory.BILLS.toString());
        mCategoryMap.put("etisalat", TransactionCategory.BILLS.toString());

        mCategoryMap.put("carrefour", TransactionCategory.GROCERIES.toString());
        mCategoryMap.put("spinneys", TransactionCategory.GROCERIES.toString());
        mCategoryMap.put("lifco", TransactionCategory.GROCERIES.toString());
        mCategoryMap.put("waitrose", TransactionCategory.GROCERIES.toString());
        mCategoryMap.put("lulu", TransactionCategory.GROCERIES.toString());
        mCategoryMap.put("aswaaq", TransactionCategory.GROCERIES.toString());
        mCategoryMap.put("market", TransactionCategory.GROCERIES.toString());
        mCategoryMap.put("supermarket", TransactionCategory.GROCERIES.toString());
        mCategoryMap.put("minimarket", TransactionCategory.GROCERIES.toString());
        mCategoryMap.put("hypermarket", TransactionCategory.GROCERIES.toString());
        mCategoryMap.put("grocer", TransactionCategory.GROCERIES.toString());
        mCategoryMap.put("elgrocer", TransactionCategory.GROCERIES.toString());
        mCategoryMap.put("instashop", TransactionCategory.GROCERIES.toString());

        mCategoryMap.put("food", TransactionCategory.FOOD.toString());
        mCategoryMap.put("restaurant", TransactionCategory.FOOD.toString());
        mCategoryMap.put("eat", TransactionCategory.FOOD.toString());
        mCategoryMap.put("eats", TransactionCategory.FOOD.toString());
        mCategoryMap.put("turnlunchon", TransactionCategory.FOOD.toString());
        mCategoryMap.put("zomato", TransactionCategory.FOOD.toString());
        mCategoryMap.put("deliveroo", TransactionCategory.FOOD.toString());
        mCategoryMap.put("talabat", TransactionCategory.FOOD.toString());
        mCategoryMap.put("bakemart", TransactionCategory.FOOD.toString());
        mCategoryMap.put("mcdonalds", TransactionCategory.FOOD.toString());
        mCategoryMap.put("zaatar", TransactionCategory.FOOD.toString());
        mCategoryMap.put("albahr", TransactionCategory.FOOD.toString());
        mCategoryMap.put("salt", TransactionCategory.FOOD.toString());
        mCategoryMap.put("cheese", TransactionCategory.FOOD.toString());
        mCategoryMap.put("cake", TransactionCategory.FOOD.toString());
        mCategoryMap.put("blacktap", TransactionCategory.FOOD.toString());
        mCategoryMap.put("meridien", TransactionCategory.FOOD.toString());
        mCategoryMap.put("pai", TransactionCategory.FOOD.toString());
        mCategoryMap.put("pizza", TransactionCategory.FOOD.toString());
        mCategoryMap.put("burger", TransactionCategory.FOOD.toString());
        mCategoryMap.put("pasta", TransactionCategory.FOOD.toString());
        mCategoryMap.put("cafe", TransactionCategory.FOOD.toString());

        mCategoryMap.put("club", TransactionCategory.ENTERTAINMENT.toString());
        mCategoryMap.put("hotel", TransactionCategory.ENTERTAINMENT.toString());
        mCategoryMap.put("drink", TransactionCategory.ENTERTAINMENT.toString());
        mCategoryMap.put("beverage", TransactionCategory.ENTERTAINMENT.toString());
        mCategoryMap.put("jetty", TransactionCategory.ENTERTAINMENT.toString());

        mCategoryMap.put("taxi", TransactionCategory.TRANSPORT.toString());
        mCategoryMap.put("uber", TransactionCategory.TRANSPORT.toString());
        mCategoryMap.put("careem", TransactionCategory.TRANSPORT.toString());
        mCategoryMap.put("metro", TransactionCategory.TRANSPORT.toString());
        mCategoryMap.put("rta", TransactionCategory.TRANSPORT.toString());

        mCategoryMap.put("sport", TransactionCategory.SPORTS.toString());
        mCategoryMap.put("sports", TransactionCategory.SPORTS.toString());
        mCategoryMap.put("gym", TransactionCategory.SPORTS.toString());
        mCategoryMap.put("fitness", TransactionCategory.SPORTS.toString());
        mCategoryMap.put("football", TransactionCategory.SPORTS.toString());

        mCategoryMap.put("shop", TransactionCategory.SHOPPING.toString());
        mCategoryMap.put("shops", TransactionCategory.SHOPPING.toString());
        mCategoryMap.put("shopping", TransactionCategory.SHOPPING.toString());
        mCategoryMap.put("mall", TransactionCategory.SHOPPING.toString());
        mCategoryMap.put("apparel", TransactionCategory.SHOPPING.toString());
        mCategoryMap.put("cloth", TransactionCategory.SHOPPING.toString());
        mCategoryMap.put("clothes", TransactionCategory.SHOPPING.toString());
        mCategoryMap.put("furniture", TransactionCategory.SHOPPING.toString());
        mCategoryMap.put("souq", TransactionCategory.SHOPPING.toString());
        mCategoryMap.put("noon", TransactionCategory.SHOPPING.toString());
        mCategoryMap.put("nike", TransactionCategory.SHOPPING.toString());
        mCategoryMap.put("adidas", TransactionCategory.SHOPPING.toString());
        mCategoryMap.put("retail", TransactionCategory.SHOPPING.toString());

        mCategoryMap.put("health", TransactionCategory.HEALTH.toString());
        mCategoryMap.put("hospital", TransactionCategory.HEALTH.toString());
        mCategoryMap.put("pharmacy", TransactionCategory.HEALTH.toString());
        mCategoryMap.put("life", TransactionCategory.HEALTH.toString());
        mCategoryMap.put("nutrition", TransactionCategory.HEALTH.toString());

        mCategoryMap.put("emirates", TransactionCategory.TRAVEL.toString());
        mCategoryMap.put("mea", TransactionCategory.TRAVEL.toString());
        mCategoryMap.put("fly", TransactionCategory.TRAVEL.toString());
        mCategoryMap.put("travel", TransactionCategory.TRAVEL.toString());
        mCategoryMap.put("airline", TransactionCategory.TRAVEL.toString());

        mCategoryMap.put("pet", TransactionCategory.PETS.toString());
        mCategoryMap.put("pets", TransactionCategory.PETS.toString());
        mCategoryMap.put("veterinary", TransactionCategory.PETS.toString());
        mCategoryMap.put("dog", TransactionCategory.PETS.toString());
        mCategoryMap.put("cat", TransactionCategory.PETS.toString());

        mCategoryMap.put("chaps", TransactionCategory.CARE.toString());

        mCategoryMap.put("government", TransactionCategory.OTHER.toString());
        mCategoryMap.put("belhasa", TransactionCategory.OTHER.toString());
    }

    public String getCategoryFromString(String text)
    {
        if(text == null)
            return TransactionCategory.NONE.toString();

        // lower case it
        text = text.toLowerCase();

        // split on alphanumerical characters
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































