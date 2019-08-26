package com.example.banco.Model;

public class Action {
    private String id;
    private String number;
    private double amount;
    private String type;
    private String transmitter;
    private String receiver;

    public Action() {
    }

    public Action(String id, String number, double amount, String type, String transmitter, String receiver) {
        this.id = id;
        this.number = number;
        this.amount = amount;
        this.type = type;
        this.transmitter = transmitter;
        this.receiver = receiver;
    }

    public Action(String id, String number, double amount, String type, String transmitter) {
        this.id = id;
        this.number = number;
        this.amount = amount;
        this.type = type;
        this.transmitter = transmitter;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
}
