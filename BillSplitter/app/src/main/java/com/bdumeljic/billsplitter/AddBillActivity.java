package com.bdumeljic.billsplitter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;
import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddBillActivity extends AppCompatActivity {

    @Bind(R.id.bill_amount) AppCompatEditText mBillAmount;
    @Bind(R.id.bill_description) AppCompatEditText mBillDescription;
    @Bind(R.id.bill_paid_photo) ImageView mPaidPhoto;
    @Bind(R.id.bill_paid_name) TextView mPaidName;
    @Bind(R.id.list_shares) RecyclerView mSharesList;

    private ParseUser mCurrentUser;
    private Group mCurrentGroup;

    private SharesRecyclerViewAdapter mMembersAdapter;
    private static List<ParseUser> mMembers = new ArrayList<ParseUser>();

    private MultiSelector mMultiSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMultiSelector = new MultiSelector();
        mMultiSelector.setSelectable(true);

        mCurrentUser = ParseUser.getCurrentUser();
        try {
            mCurrentGroup = Group.getGroup();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        getGroupMembers();

        ParseFile file = mCurrentUser.getParseFile("photo");
        if (file != null) {
            Glide.with(this)
                    .load(file.getUrl())
                    .centerCrop()
                    .transform(new CircleTransform(this))
                    .into(mPaidPhoto);
        }

        mPaidName.setText(mCurrentUser.getString("name"));

        mSharesList.setLayoutManager(new LinearLayoutManager(this));
        mMembersAdapter = new SharesRecyclerViewAdapter();
        mSharesList.setAdapter(mMembersAdapter);

    }

    public void getGroupMembers() {
        if (mCurrentGroup != null) {
            List list = Collections.emptyList();
            try {
                list = mCurrentGroup.getMembersList().getQuery().find();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            mMembers.addAll(list);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_add_bill) {
            try {
                validateBill();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void validateBill() throws ParseException {
        if (mBillAmount.getText().toString().isEmpty()) {
            mBillAmount.setError(getString(R.string.err_amount));
            Toast.makeText(AddBillActivity.this, R.string.err_amount, Toast.LENGTH_SHORT).show();
        } else if (mBillDescription.getText().toString().isEmpty()) {
            mBillDescription.setError(getString(R.string.err_description));
            Toast.makeText(AddBillActivity.this, R.string.err_description, Toast.LENGTH_SHORT).show();
        } else if (mMultiSelector.getSelectedPositions().size() < 1) {
            Toast.makeText(AddBillActivity.this, R.string.err_shares, Toast.LENGTH_SHORT).show();
        } else {
            saveBill();
        }
    }

    public void saveBill() throws ParseException {
        // Get data from view
        final double amount = Double.parseDouble(mBillAmount.getText().toString());
        String description = mBillDescription.getText().toString();

        // Create a new bill
        final Bill newBill = new Bill();
        newBill.setAmount(amount);
        newBill.setDescription(description);
        newBill.setPayee(mCurrentUser);
        newBill.setGroup(mCurrentGroup);
        newBill.save();

        // Handle individual shares for each member participating in the bill
        saveShares(amount, newBill);

        // Add spent amount for current user to spent and balance
        Balance mCurrentUserBalance = (Balance) mCurrentUser.get("balances");
        mCurrentUserBalance.addSpent(amount);
        mCurrentUserBalance.addBalance(amount);
        mCurrentUserBalance.save();

        // Add amount spent to group total
        mCurrentGroup.addToSpent(amount);
        mCurrentGroup.save();

        setResult(RESULT_OK);
        finish();
    }

    private void saveShares(double amount, Bill newBill) throws ParseException {
        List<ParseUser> sharedAmongst = new ArrayList();
        for (int i = mMembers.size(); i >= 0; i--) {
            if (mMultiSelector.isSelected(i, 0)) {
                ParseUser user = mMembers.get(i);
                sharedAmongst.add(user);
            }
        }

        int sharers = sharedAmongst.size();
        double amount_sharer = amount/sharers;

        for (ParseUser user : sharedAmongst) {
            Shares share = new Shares();
            share.setUser(user);
            share.setGroup(mCurrentGroup);
            share.setAmount(amount_sharer);
            share.setBill(newBill);
            share.save();

            Balance userBalance = user.getParseObject("balances").fetchIfNeeded();
            userBalance.setDebt(amount_sharer);
            userBalance.subBalance(amount_sharer);
            userBalance.save();
        }
    }

    public class ShareHolder extends SwappingHolder implements View.OnClickListener {
        public ParseUser mUser;
        public final View mView;
        public final TextView mUserNameView;
        public final TextView mUserBalance;
        public final ImageView mUserPhoto;

        public ShareHolder(View itemView) {
            super(itemView, mMultiSelector);
            mView = itemView;
            mUserNameView = (TextView) itemView.findViewById(R.id.member_name);
            mUserBalance = (TextView) itemView.findViewById(R.id.member_balance);
            mUserPhoto = (ImageView) itemView.findViewById(R.id.member_photo);

            mView.setOnClickListener(this);
        }

        public void bindShare(ParseUser user) {
            mUser = user;
            mUserNameView.setText(mUser.getString("name"));
            //mUserBalance.setText(String.valueOf(mUser.getInt("balance")));

            ParseFile file = mUser.getParseFile("photo");
            if (file != null) {
                Glide.with(getApplicationContext())
                        .load(file.getUrl())
                        .centerCrop()
                        .transform(new CircleTransform(getApplicationContext()))
                        .into(mUserPhoto);
            }

        }

        @Override
        public void onClick(View v) {
            if (mUser == null) {
                return;
            }

            boolean isSelected = mMultiSelector.isSelected(this.getAdapterPosition(), 0);
            mMultiSelector.setSelected(this, !isSelected);
        }
    }

    public class SharesRecyclerViewAdapter extends RecyclerView.Adapter<ShareHolder> {
        @Override
        public ShareHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.members_list_item, parent, false);
            return new ShareHolder(view);
        }

        @Override
        public void onBindViewHolder(ShareHolder holder, int position) {
            holder.bindShare(mMembers.get(position));
        }

        @Override
        public int getItemCount() {
            return mMembers.size();
        }
    }
}
