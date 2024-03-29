package com.bdumeljic.billsplitter;

import com.parse.Parse;
import com.parse.ParseObject;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        ParseObject.registerSubclass(Bill.class);
        ParseObject.registerSubclass(Group.class);
        ParseObject.registerSubclass(Shares.class);
        ParseObject.registerSubclass(Balance.class);

        Parse.initialize(this, getResources().getString(R.string.parse_application_id), getResources().getString(R.string.parse_client_key));
    }
}
