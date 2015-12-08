package com.bdumeljic.billsplitter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class RecentBillsRecyclerViewAdapter extends RecyclerView.Adapter<RecentBillsRecyclerViewAdapter.ViewHolder> {
    private List<Bill> mValues = Collections.emptyList();

    public RecentBillsRecyclerViewAdapter(List<Bill> bills) {
        this.mValues = bills;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recent_bills_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mBill = mValues.get(position);
        holder.mBillNameView.setText(holder.mBill.getName());
        holder.mBillAmountView.append(String.valueOf(holder.mBill.getAmount()));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mBillNameView;
        public final TextView mBillAmountView;
        public Bill mBill;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mBillNameView = (TextView) view.findViewById(R.id.bill_name);
            mBillAmountView = (TextView) view.findViewById(R.id.bill_amount);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mBillNameView.getText() + "'";
        }
    }
}
