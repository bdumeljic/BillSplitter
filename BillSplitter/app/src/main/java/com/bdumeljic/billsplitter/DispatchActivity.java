package com.bdumeljic.billsplitter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseClassName;
import com.parse.ParseException;

import java.io.Console;


public class DispatchActivity extends AppCompatActivity {
    private static final int LOGIN_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        doDispatch();
    }

    private void doDispatch() {

        ParseUser user = ParseUser.getCurrentUser();
        ParseRelation<ParseObject> relation = user.getRelation("belongsTo");
        int query = 0;
        try {
            query = relation.getQuery().count();
            Log.d("shit", "Query result" + query);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Integer numberOfGroups = query.count();

        if (ParseUser.getCurrentUser() != null && query > 0 ) {
            Log.d("User check","The user doesn't have a group yet,= 0 let him join one or create a new group");
            startActivity(new Intent(this, JoinGroupActivity.class));
        } else if (ParseUser.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            ParseLoginBuilder builder = new ParseLoginBuilder(DispatchActivity.this);
            Intent parseLoginIntent = builder.setAppLogo(R.drawable.logo)
                    .setParseLoginEnabled(true)
                    .setParseLoginButtonText("Log in")
                    .setParseSignupButtonText("Register")
                    .setParseLoginHelpText("Forgot password?")
                    .setParseLoginInvalidCredentialsToastText("You email and/or password is not correct")
                    .setParseLoginEmailAsUsername(true)
                    .setParseSignupSubmitButtonText("Sign Up")
                    .build();
            startActivityForResult(parseLoginIntent, LOGIN_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOGIN_REQUEST) {
            doDispatch();
        }
    }
}
