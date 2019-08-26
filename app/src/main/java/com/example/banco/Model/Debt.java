package com.example.banco.Model;

public class Debt {
    private String id;
    private String name;
    private double amount;
    private String transmitter;
    private String receiver;
    private String estate;
    private String value;

    public Debt() {
    }

    public Debt(String id, String name, double amount, String transmitter, String receiver, String estate, String value) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.transmitter = transmitter;
        this.receiver = receiver;
        this.estate = estate;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransmitter() {
        return transmitter;
    }

    public void setTransmitter(String transmitter) {
        this.transmitter = transmitter;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getEstate() {
        return estate;
    }

    public void setEstate(String estate) {
        this.estate = estate;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return name + " - deuda: " + amount;
    }
}
