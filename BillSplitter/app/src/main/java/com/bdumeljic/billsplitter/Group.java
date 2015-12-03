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

    public int getMembersAmount() {
        int result = 0;

        try {
             result = getRelation("membersList").getQuery().count();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    public ParseRelation<ParseUser> getMembersList() {
        return getRelation("membersList");
    }
}
