package com.bdumeljic.billsplitter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bdumeljic.billsplitter.dummy.DummyContent.DummyItem;

import java.util.Collections;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link BillsFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class BillsRecyclerViewAdapter extends RecyclerView.Adapter<BillsRecyclerViewAdapter.ViewHolder> {

    private List<Bill> mValues = Collections.emptyList();
    private final BillsFragment.OnListFragmentInteractionListener mListener;

    public BillsRecyclerViewAdapter(List<Bill> bills, BillsFragment.OnListFragmentInteractionListener listener) {
        this.mValues = bills;
        mListener = listener;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bills_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mBill = mValues.get(position);
        holder.mBillNameView.setText(mValues.get(position).getName());
        holder.mBillAmountView.append(String.valueOf(mValues.get(position).getAmount()));
        holder.mBillPayeeView.setText(mValues.get(position).getPayee());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mBill);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mBillNameView;
        public final TextView mBillAmountView;
        public final TextView mBillPayeeView;
        public Bill mBill;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mBillPayeeView = (TextView) view.findViewById(R.id.bill_payee);
            mBillNameView = (TextView) view.findViewById(R.id.bill_name);
            mBillAmountView = (TextView) view.findViewById(R.id.bill_amount);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mBillAmountView.getText() + "'";
        }
    }
}
