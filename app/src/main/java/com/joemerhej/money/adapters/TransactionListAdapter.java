package com.joemerhej.money.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joemerhej.money.R;
import com.joemerhej.money.transaction.Transaction;
import java.util.List;

/**
 * Created by Joe Merhej on 1/12/18.
 */

public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.TransactionListViewHolder>
{
    // transactions
    private List<Transaction> mTransactionsList;

    // click listener instance
    TransactionClickListener mClickListener;

    // click listener interface that the parent should implement
    public interface TransactionClickListener
    {
        void onTransactionClick(View view, int position);
    }

    public TransactionListAdapter(List<Transaction> transactionsList)
    {
        this.mTransactionsList = transactionsList;
    }

    // method to update the data used when canceling/discarding changes (like constructor)
    public void updateDataWith(List<Transaction> transactionsList)
    {
        this.mTransactionsList = transactionsList;
    }

    // method to get a transaction based on view position
    public Transaction getTransaction(int position)
    {
        return mTransactionsList.get(position);
    }

    // setter for the click listener
    public void setOnTransactionClickListener(final TransactionClickListener onClickListener)
    {
        mClickListener = onClickListener;
    }

    // -------------------------------------------------------------------------------------------------------------------------
    public class TransactionListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        // the views for every transaction
        private RelativeLayout mContainer;
        private TextView mAmountTextView;
        private TextView mCurrencyTextView;
        private TextView mIssuerTextView;


        public TransactionListViewHolder(View itemView)
        {
            super(itemView);

            mContainer = itemView.findViewById(R.id.recycler_item_transaction_container);
            mAmountTextView = itemView.findViewById(R.id.recycler_item_transaction_amount);
            mCurrencyTextView = itemView.findViewById(R.id.recycler_item_transaction_currency);
            mIssuerTextView = itemView.findViewById(R.id.recycler_item_transaction_issuer);

            mContainer.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            if(mClickListener != null)
                mClickListener.onTransactionClick(view, getLayoutPosition());
        }
    }
    // -------------------------------------------------------------------------------------------------------------------------


    // creates the view holder
    @Override
    public TransactionListViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_transaction, parent, false);
        return new TransactionListViewHolder(inflatedView);
    }

    // called once every time the view holder wants to fill a new row
    @Override
    public void onBindViewHolder(TransactionListViewHolder holder, int position)
    {
        // fill the views with data
        Transaction transaction = mTransactionsList.get(position);

        holder.mAmountTextView.setText(transaction.getAmount().intValue() + "");
        holder.mCurrencyTextView.setText(transaction.getCurrency().toString());
        holder.mIssuerTextView.setText(transaction.getIssuer());

    }

    @Override
    public int getItemCount()
    {
        return mTransactionsList.size();
    }
}

































