package com.joemerhej.money.dialogs;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.joemerhej.money.R;

/**
 * Created by Joe Merhej on 11/24/17.
 */

public class DateRangeDialogFragment extends DialogFragment
{
    DateRangeDialogListener mListener;

    public interface DateRangeDialogListener
    {
        void onItemClickDay(DateRangeDialogFragment dialog);

        void onItemClickWeek(DateRangeDialogFragment dialog);

        void onItemClickMonth(DateRangeDialogFragment dialog);

        void onItemClickYear(DateRangeDialogFragment dialog);

        void onItemClickAll(DateRangeDialogFragment dialog);

        void onItemClickCustom(DateRangeDialogFragment dialog);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        // Verify that the host activity implements the callback interface
        try
        {
            // Instantiate the DateRangeDialogListener so we can send events to the host
            mListener = (DateRangeDialogListener) activity;
        }
        catch(ClassCastException e)
        {
            throw new ClassCastException(activity.toString() + " must implement DateRangeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.date_range_dialog_title)
               .setItems(R.array.date_range_array, new DialogInterface.OnClickListener()
               {
                   public void onClick(DialogInterface dialog, int index)
                   {
                       String[] dateRanges = getResources().getStringArray(R.array.date_range_array);

                       // TODO: find a better way to index the elements, through enum maybe...
                       switch(dateRanges[index])
                       {
                           case "Day":
                               mListener.onItemClickDay(DateRangeDialogFragment.this);
                               break;
                           case "Week":
                               mListener.onItemClickWeek(DateRangeDialogFragment.this);
                               break;
                           case "Month":
                               mListener.onItemClickMonth(DateRangeDialogFragment.this);
                               break;
                           case "Year":
                               mListener.onItemClickYear(DateRangeDialogFragment.this);
                               break;
                           case "All":
                               mListener.onItemClickAll(DateRangeDialogFragment.this);
                               break;
                           case "Custom":
                               mListener.onItemClickCustom(DateRangeDialogFragment.this);
                               break;
                       }
                   }
               });

        return builder.create();
    }
}
