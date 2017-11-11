package com.joemerhej.money.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joemerhej.money.R;

/**
 * Created by Joe Merhej on 11/12/17.
 */

public class MainChartIncomeFragment extends Fragment
{
    private static final String SECTION_NUMBER = "section_number";

    // every fragment requires a default constructor and a newInstance method
    public MainChartIncomeFragment()
    {
    }

    public static MainChartIncomeFragment newInstance(int sectionNumber)
    {
        MainChartIncomeFragment fragment = new MainChartIncomeFragment();

        Bundle args = new Bundle();
        args.putInt(SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_income_chart, container, false);

        TextView textView = rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, getArguments().getInt(SECTION_NUMBER)));

        return rootView;
    }
}
