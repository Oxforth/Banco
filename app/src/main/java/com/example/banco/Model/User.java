package com.example.banco.Model;

public class User {
    private String id;
    private String nombre;
    private String appaterno;
    private String apmaterno;
    private String dni;
    private String acountnumber;

    public User() {
    }

    public User(String id, String nombre, String appaterno, String apmaterno, String dni, String acountnumber) {
        this.id = id;
        this.nombre = nombre;
        this.appaterno = appaterno;
        this.apmaterno = apmaterno;
        this.dni = dni;
        this.acountnumber = acountnumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAppaterno() {
        return appaterno;
    }

    public void setAppaterno(String appaterno) {
        this.appaterno = appaterno;
    }

    public String getApmaterno() {
        return apmaterno;
    }

    public void setApmaterno(String apmaterno) {
        this.apmaterno = apmaterno;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getAcountnumber() {
        return acountnumber;
    }

    public void setAcountnumber(String acountnumber) {
        this.acountnumber = acountnumber;
    }
}
