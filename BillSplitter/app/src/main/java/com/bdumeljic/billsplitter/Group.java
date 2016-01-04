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

    public void addToSpent(double amount) {
        put("spent", getSpent() + amount);
    }

    public void addMember(ParseUser currentUser) {
        getMembersList().add(currentUser);
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

    public ParseQuery<Bill> getBills() {
        return Bill.getQuery().whereEqualTo("partOf", this);
    }

    public int getBillsCount() {
        try {
            return Bill.getQuery().whereEqualTo("partOf", this).count();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static ParseQuery<Group> findGroup(String name) {
        return getQuery().whereEqualTo("name", name);
    }


}
