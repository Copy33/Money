package com.joemerhej.money.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
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

        mSpendingChart.getDescription().setEnabled(false);
        mSpendingChart.setExtraOffsets(15, 20, 15, 20);
        mSpendingChart.setDragDecelerationFrictionCoef(0.5f);
        mSpendingChart.setRotationEnabled(true);
        mSpendingChart.setTransparentCircleColor(Color.rgb(105, 105, 105));
        mSpendingChart.setTransparentCircleRadius(65);
        mSpendingChart.setHoleRadius(55);
        mSpendingChart.setEntryLabelColor(Color.BLACK);

        mSpendingChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);


        List<PieEntry> entries = getArguments().getParcelableArrayList("entries");

        PieDataSet dataSet = new PieDataSet(entries, "Categories");

        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueLinePart1OffsetPercentage(100.f);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueLineColor(Color.BLACK);
        dataSet.setValueLinePart1Length(0.7f);
        dataSet.setValueLinePart2Length(0.3f);
        dataSet.setHighlightEnabled(true);

        mSpendingChart.getLegend().setEnabled(false);

        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setSliceSpace(0f);

        PieData data = new PieData(dataSet);

        data.setValueTextSize(10f);


        mSpendingChart.setData(data);
        mSpendingChart.invalidate();

        return rootView;
    }

    public static int rgb(String hex)
    {
        int color = (int) Long.parseLong(hex.replace("#", ""), 16);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        return Color.rgb(r, g, b);
    }
}



























