package com.bdumeljic.billsplitter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;


public class DispatchActivity extends AppCompatActivity {
    private static final int LOGIN_REQUEST = 0;
    private static final int JOIN_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        doDispatch();
    }

    private void doDispatch() {
        if (ParseUser.getCurrentUser() != null && ParseUser.getCurrentUser().get("belongsTo") != null) {
            // If there is a user logged in and they are part of a group, then proceed to the main view.
            startActivity(new Intent(this, MainActivity.class));
        } else if (ParseUser.getCurrentUser() != null) {
            // If there is a user logged in, but the user doesn't have a group yet, then let him join one or create a new group
            startActivityForResult(new Intent(this, JoinGroupActivity.class), JOIN_REQUEST);
        } else {
            // If there is no user logged in, then go to the log in or sign up act
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

        if (requestCode == LOGIN_REQUEST || requestCode == JOIN_REQUEST) {
            doDispatch();
        }
    }
}
