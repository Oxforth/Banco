package com.example.banco.Model;

public class Action {
    private int id;
    private String number;
    private Double amount;
    private String type;
    private String transmitter;
    private String receiver;

    public Action() {
    }

    public Action(int id,   String number, Double amount, String type, String transmitter, String receiver) {
        this.id = id;
        this.number = number;
        this.amount = amount;
        this.type = type;
        this.transmitter = transmitter;
        this.receiver = receiver;
    }

    public Action(int id, String number, Double amount, String type, String transmitter) {
        this.id = id;
        this.number = number;
        this.amount = amount;
        this.type = type;
        this.transmitter = transmitter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
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

    @Override
    public String toString() {
        return id + " - " +number + " - Tipo: " + type + " - Monto: " + amount;
    }
}
