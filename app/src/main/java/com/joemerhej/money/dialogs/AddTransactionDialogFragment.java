package com.joemerhej.money.dialogs;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.joemerhej.money.R;
import com.joemerhej.money.account.Currency;
import com.joemerhej.money.transaction.Transaction;
import com.joemerhej.money.transaction.TransactionType;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Joe Merhej on 11/24/17.
 */

public class AddTransactionDialogFragment extends DialogFragment
{
    // listener interface that users should implement
    AddTransactionDialogListener mListener;

    // views
    private EditText mAmountEditText;
    private EditText mDateEditText;
    private EditText mDescriptionEditText;

    // properties
    private Transaction mTransaction;

    public interface AddTransactionDialogListener
    {
        void onAddTransactionClick(AddTransactionDialogFragment dialog);
    }


    public static AddTransactionDialogFragment newInstance(String title, String type, String category, String currency)
    {
        AddTransactionDialogFragment frag = new AddTransactionDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("type", type);
        args.putString("category", category);
        args.putString("currency", currency);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        try
        {
            mListener = (AddTransactionDialogListener) activity;
        }
        catch(ClassCastException e)
        {
            throw new ClassCastException(activity.toString() + " must implement AddTransactionDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        // get some properties from args
        String title = getArguments().getString("title");
        final TransactionType transactionType = TransactionType.fromString(getArguments().getString("type"));
        final String transactionCategory = getArguments().getString("category");
        final Currency transactionCurrency = Currency.fromString(getArguments().getString("currency"));

        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy");

        // create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_transaction, null);

        // set up the views
        mAmountEditText = v.findViewById(R.id.dialog_add_transaction_amount_edit_text);
        mDateEditText = v.findViewById(R.id.dialog_add_transaction_date_edit_text);
        mDescriptionEditText = v.findViewById(R.id.dialog_add_transaction_description_edit_text);

        Calendar cal = Calendar.getInstance();

        // auto-populate today's date
        try
        {
            final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyy", Locale.US);
            Date today = DATE_FORMAT.parse(DATE_FORMAT.format(cal.getTime()));
            mDateEditText.setText(DATE_FORMAT.format(today));
        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }


        builder.setTitle(title)
               .setView(v)
               .setPositiveButton("Add", new DialogInterface.OnClickListener()
               {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int id)
                   {
                       try
                       {
                           mTransaction = new Transaction(transactionType, new BigDecimal(mAmountEditText.getText().toString()), transactionCurrency,
                                   sdf.parse(mDateEditText.getText().toString()), mDescriptionEditText.getText().toString(), transactionCategory);
                       }
                       catch(ParseException e)
                       {
                           e.printStackTrace();
                       }

                       mListener.onAddTransactionClick(AddTransactionDialogFragment.this);
                   }
               })
               .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
               {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int id)
                   {
                       if(dialogInterface != null)
                           dialogInterface.dismiss();
                   }
               });

        mAmountEditText.requestFocus();

        AlertDialog d = builder.create();
        d.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return d;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {

    }

    public Transaction getTransaction()
    {
        return mTransaction;
    }
}
