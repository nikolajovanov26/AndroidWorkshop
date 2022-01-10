package com.example.androidworkshop;

public class User {
    public String tip, ime, prezime, broj, email, password, rejting;

    public User(){

    }

    public User(String tip, String ime, String prezime, String broj, String email, String password, String rejting) {
        this.tip = tip;
        this.ime = ime;
        this.prezime = prezime;
        this.broj = broj;
        this.email = email;
        this.password = password;
        this.rejting = password;
    }

    public User(String tip, String ime, String prezime, String broj, String email, String password) {
        this.tip = tip;
        this.ime = ime;
        this.prezime = prezime;
        this.broj = broj;
        this.email = email;
        this.password = password;
        this.rejting = "Nema zapis";
    }
}
