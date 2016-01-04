package com.bdumeljic.billsplitter;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Balance")
public class Balance extends ParseObject {

    public double getDebt() {
        return getDouble("debt");
    }

    public double getBalance() {
        return getDouble("balance");
    }

    public double getSpent() {
        return getDouble("spent");
    }

    public void addSpent(double amount) {
        put("spent", getSpent() + amount);
    }

    public void setDebt(double amount) {
        put("debt", getDebt() + amount);
    }

    public void subBalance(double amount) {
        put("balance", getBalance() - amount);
    }

    public void addBalance(double amount) {
        put("balance", getBalance() + amount);
    }
}
