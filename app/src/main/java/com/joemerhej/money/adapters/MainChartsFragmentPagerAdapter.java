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
    private ArrayList<PieEntry> mSpendingEntries = new ArrayList<>();
    private ArrayList<Integer> mSpendingColors = new ArrayList<>();
    private ArrayList<PieEntry> mIncomeEntries = new ArrayList<>();
    private ArrayList<Integer> mIncomeColors = new ArrayList<>();



    public MainChartsFragmentPagerAdapter(FragmentManager fm, ArrayList<PieEntry> spendingEntries, ArrayList<Integer> spendingColors,
                                          ArrayList<PieEntry> incomeEntries, ArrayList<Integer> incomeColors)
    {
        super(fm);
        mSpendingEntries = spendingEntries;
        mSpendingColors = spendingColors;
        mIncomeEntries = incomeEntries;
        mIncomeColors = incomeColors;
    }

    @Override
    public Fragment getItem(int position)
    {
        switch(position)
        {
            case 0:
                return MainChartSpendingFragment.newInstance(mSpendingEntries, mSpendingColors);
            case 1:
                return MainChartIncomeFragment.newInstance(mIncomeEntries, mIncomeColors);
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
