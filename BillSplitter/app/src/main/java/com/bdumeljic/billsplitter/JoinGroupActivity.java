package com.bdumeljic.billsplitter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class JoinGroupActivity extends AppCompatActivity {

    @Bind(R.id.new_group_name) TextView mNewGroupName;
    @Bind(R.id.join_group) Button mJoinGroup;
    @Bind(R.id.create_group) Button mCreateGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        ButterKnife.bind(this);


    }
}
