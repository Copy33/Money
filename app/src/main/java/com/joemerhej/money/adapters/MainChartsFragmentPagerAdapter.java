package com.joemerhej.money.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.joemerhej.money.fragments.MainChartIncomeFragment;
import com.joemerhej.money.fragments.MainChartSpendingFragment;

/**
 * Created by Joe Merhej on 11/11/17.
 */

public class MainChartsFragmentPagerAdapter extends FragmentPagerAdapter
{
    public MainChartsFragmentPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        switch(position)
        {
            case 0:
                return MainChartSpendingFragment.newInstance(0);
            case 1:
                return MainChartIncomeFragment.newInstance(1);
            default:
                return null;
        }
    }

    // returns total number of tabs
    @Override
    public int getCount()
    {
        return 2;
    }
}
