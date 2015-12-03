package com.bdumeljic.billsplitter;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("Bill")
public class Bill extends ParseObject {

    public static ParseQuery<Bill> getQuery() {
        return ParseQuery.getQuery(Bill.class);
    }

    public String getName() {
        return getString("name");
    }

    public double getAmount() {
        return (double) getNumber("amount");
    }

    public String getPayee() {
        ParseUser user = getParseUser("payedBy");
        String payee = "";
        try {
            payee = user.fetchIfNeeded().getString("name");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (payee.equals("")) {
            return "";
        } else {
            return "paid by " + payee;
        }
    }
}