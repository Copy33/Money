package com.joemerhej.money.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.joemerhej.money.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joe Merhej on 11/12/17.
 */

public class MainChartIncomeFragment extends Fragment
{
    private static final String ENTRIES_KEY = "entries";
    private static final String COLORS_KEY = "colors";

    // views
    private PieChart mSpendingChart;


    // every fragment requires a default constructor and a newInstance method
    public MainChartIncomeFragment()
    {
    }

    public static MainChartIncomeFragment newInstance(ArrayList<PieEntry> entries, ArrayList<Integer> colors)
    {
        MainChartIncomeFragment fragment = new MainChartIncomeFragment();

        Bundle args = new Bundle();
        args.putParcelableArrayList(ENTRIES_KEY, entries);
        args.putIntegerArrayList(COLORS_KEY, colors);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_income_chart, container, false);

        // get the arguments
        List<PieEntry> entries = getArguments().getParcelableArrayList(ENTRIES_KEY);
        ArrayList<Integer> colors = getArguments().getIntegerArrayList(COLORS_KEY);

        // set up views
        mSpendingChart = rootView.findViewById(R.id.income_chart);

        mSpendingChart.getDescription().setEnabled(false);
        mSpendingChart.setExtraOffsets(35, 40, 35, 40);
        mSpendingChart.setDragDecelerationFrictionCoef(0.5f);
        mSpendingChart.setRotationEnabled(true);
        mSpendingChart.setTransparentCircleColor(Color.rgb(105, 105, 105));
        mSpendingChart.setTransparentCircleRadius(65);
        mSpendingChart.setHoleRadius(55);
        mSpendingChart.setEntryLabelColor(Color.BLACK);

        mSpendingChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);

        PieDataSet dataSet = new PieDataSet(entries, "Categories");

        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueLinePart1OffsetPercentage(100.f);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueLineColor(Color.BLACK);
        dataSet.setValueLinePart1Length(0.8f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setHighlightEnabled(true);

        mSpendingChart.getLegend().setEnabled(false);

        dataSet.setColors(colors);
        dataSet.setSliceSpace(0f);

        PieData data = new PieData(dataSet);

        data.setValueTextSize(10f);


        mSpendingChart.setData(data);
        mSpendingChart.invalidate();

        return rootView;
    }
}
