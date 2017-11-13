package com.joemerhej.money.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.joemerhej.money.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joe Merhej on 11/12/17.
 */

public class MainChartSpendingFragment extends Fragment
{
    private static final String ENTRIES_KEY = "entries";

    // views
    private PieChart mSpendingChart;


    // every fragment requires a default constructor and a newInstance method
    public MainChartSpendingFragment()
    {
    }

    public static MainChartSpendingFragment newInstance(ArrayList<PieEntry> entries)
    {
        MainChartSpendingFragment fragment = new MainChartSpendingFragment();

        Bundle args = new Bundle();
        args.putParcelableArrayList(ENTRIES_KEY, entries);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_spending_chart, container, false);

        // set up views
        mSpendingChart = rootView.findViewById(R.id.spending_chart);

        List<PieEntry> entries = getArguments().getParcelableArrayList("entries");

        PieDataSet set = new PieDataSet(entries, "Categories");
        PieData data = new PieData(set);

        mSpendingChart.setData(data);
        mSpendingChart.setUsePercentValues(true);
        mSpendingChart.invalidate();

        return rootView;
    }
}



























