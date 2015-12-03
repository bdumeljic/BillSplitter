package com.bdumeljic.billsplitter;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

@ParseClassName("Group")
public class Group extends ParseObject {
    public static ParseQuery<Group> getQuery() {
        return ParseQuery.getQuery(Group.class);
    }

    public String getName() { return getString("name"); }

    public static Group getGroup() throws ParseException {
        return getQuery().whereEqualTo("membersList", ParseUser.getCurrentUser()).getFirst();
    }

    public double getSpent() {
        return getDouble("spent");
    }

    public ParseRelation<ParseUser> getMembersList() {
        return getRelation("membersList");
    }

    public int getMembersCount() {
        int result = 0;

        try {
             result = getRelation("membersList").getQuery().count();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    public ParseRelation<Bill> getBillsList() {
        return getRelation("billsList");
    }

    public int getBillsCount() {
        int result = 0;

        try {
            result = getRelation("billsList").getQuery().count();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void addBill(Bill bill) {
        getBillsList().add(bill);
        saveInBackground();
    }
}
