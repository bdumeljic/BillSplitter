package com.bdumeljic.billsplitter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.Collections;
import java.util.List;

public class MembersRecyclerViewAdapter extends RecyclerView.Adapter<MembersRecyclerViewAdapter.ViewHolder> {
    private List<ParseUser> mUsers = Collections.emptyList();
    private Context mContext;

    public MembersRecyclerViewAdapter(Context context, List<ParseUser> users) {
        this.mContext = context;
        this.mUsers = users;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.members_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mUser = mUsers.get(position);
        holder.mUserNameView.setText(mUsers.get(position).getString("name"));

        Balance userBalance = null;
        try {
            userBalance = holder.mUser.getParseObject("balances").fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.mUserBalance.setText(String.valueOf(userBalance.getBalance()));

        ParseFile file = mUsers.get(position).getParseFile("photo");
        if (file != null) {
            Glide.with(mContext)
                    .load(file.getUrl())
                    .centerCrop()
                    .transform(new CircleTransform(mContext))
                    .into(holder.mUserPhoto);
        }
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ParseUser mUser;
        public final View mView;
        public final TextView mUserNameView;
        public final TextView mUserBalance;
        public final ImageView mUserPhoto;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mUserNameView = (TextView) itemView.findViewById(R.id.member_name);
            mUserBalance = (TextView) itemView.findViewById(R.id.member_balance);
            mUserPhoto = (ImageView) itemView.findViewById(R.id.member_photo);
        }
    }
}
