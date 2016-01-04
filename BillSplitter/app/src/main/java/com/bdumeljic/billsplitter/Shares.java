package com.bdumeljic.billsplitter;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("Shares")
public class Shares extends ParseObject {
    public static ParseQuery<Shares>  getQuery() {
        return ParseQuery.getQuery(Shares.class);
    }

    public ParseUser getParticipant() {
        return getParseUser("participant");
    }

    public Bill getBill() {
        try {
            return (Bill) getRelation("participantIn").getQuery().getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Group getGroup() {
        try {
            return (Group) getRelation("partOf").getQuery().getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public double getAmount() {
        return getDouble("amount");
    }


    public void setUser(ParseUser user) {
        put("participant", user);
    }

    public void setGroup(Group mCurrentGroup) {
        put("partOf", mCurrentGroup);
    }

    public void setAmount(double amount) {
        put("amount", amount);
    }

    public void setBill(Bill newBill) {
        put("participantIn", newBill);
    }
}
