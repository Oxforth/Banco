package com.example.banco.Model;

public class Acount {
    private String number;
    private String pass;
    private double amount;

    public Acount() {
    }

    public Acount(String number, String pass, double amount) {
        this.number = number;
        this.pass = pass;
        this.amount = amount;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Acount{" +
                "number='" + number + '\'' +
                ", pass='" + pass + '\'' +
                ", amount=" + amount +
                '}';
    }
}
