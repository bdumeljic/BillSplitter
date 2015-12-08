package com.bdumeljic.billsplitter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
        if (ParseUser.getCurrentUser() != null && ParseUser.getCurrentUser().getRelation("belongsTo") == null ) {
            
        } if else(ParseUser.getCurrentUser() != null) {
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

            // Check if the user has a relation with a group


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
