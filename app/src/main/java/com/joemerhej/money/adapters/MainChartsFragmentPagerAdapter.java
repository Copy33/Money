package com.joemerhej.money.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.github.mikephil.charting.data.PieEntry;
import com.joemerhej.money.fragments.MainChartIncomeFragment;
import com.joemerhej.money.fragments.MainChartSpendingFragment;

import java.util.ArrayList;

/**
 * Created by Joe Merhej on 11/11/17.
 */

public class MainChartsFragmentPagerAdapter extends FragmentPagerAdapter
{
    ArrayList<PieEntry> mEntries = new ArrayList<>();


    public MainChartsFragmentPagerAdapter(FragmentManager fm, ArrayList<PieEntry> entries)
    {
        super(fm);
        mEntries = entries;
    }

    @Override
    public Fragment getItem(int position)
    {
        switch(position)
        {
            case 0:
                return MainChartSpendingFragment.newInstance(mEntries);
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
