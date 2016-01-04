package com.bdumeljic.billsplitter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JoinGroupActivity extends AppCompatActivity {

    @Bind(R.id.text_join_group) AppCompatEditText mJoinGroupName;
    @Bind(R.id.text_create_group) AppCompatEditText mCreateGroupName;

    ParseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        ButterKnife.bind(this);

        currentUser = ParseUser.getCurrentUser();

        if (currentUser.get("balances") == null) {
            Balance balance = new Balance();
            try {
                balance.save();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            currentUser.put("balances", balance);
            try {
                currentUser.save();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.button_join_group)
    public void joinGroup() {
        String name = mJoinGroupName.getText().toString();
        if (name.isEmpty()) {
            mJoinGroupName.setError(getString(R.string.err_name_join));
        } else {
            Group.findGroup(name).getFirstInBackground(new GetCallback<Group>() {
                @Override
                public void done(Group group, ParseException e) {
                    if (group == null) {
                        mJoinGroupName.setError(getString(R.string.err_group_notexist));
                    } else {
                        try {
                            currentUser.put("belongsTo", group);
                            currentUser.save();

                            group.addMember(currentUser);
                            group.save();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                        setResult(RESULT_OK);
                        finish();
                    }
                }
            });
        }
    }

    @OnClick(R.id.button_create_group)
    public void createGroup() {
        String name = mCreateGroupName.getText().toString();
        if (name.isEmpty()) {
            mCreateGroupName.setError(getString(R.string.err_name_create));
        } else {
            Group group = new Group();
            group.setName(name);
            group.addMember(currentUser);
            try {
                group.save();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            currentUser.put("belongsTo", group);
            try {
                currentUser.save();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            setResult(RESULT_OK);
            finish();
        }
    }
}
