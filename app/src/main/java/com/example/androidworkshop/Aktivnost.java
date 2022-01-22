package com.example.androidworkshop;

import org.w3c.dom.CDATASection;

public class Aktivnost {

    public String vozrasnoliceID;
    public String volonterID;
    public String ime;
    public String itnost;
    public String povtorlivost;
    public String lokacija;
    public String datum;
    public String vreme;
    public String status;
    public String rejting;
    public String izvestaj;
    public String kluc;
    public Double lat;
    public Double lon;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Aktivnost(){

    }

    public Aktivnost(String vozrasnoliceID, String itnost, String lokacija, String datum, String povtorlivost, String ime,  String vreme, String kluc, Double lat, Double lon){
        this.vozrasnoliceID = vozrasnoliceID;
        this.volonterID = "0";
        this.itnost = itnost;
        this.lokacija = lokacija;
        this.datum = datum;
        this.status = "Aktivno";
        this.rejting = "Neoceneto";
        this.vreme = vreme;
        this.kluc = kluc;
        this.ime = ime;
        this.povtorlivost = povtorlivost;
        this.izvestaj = " ";
        this.lat = lat;
        this.lon = lon;
    }




    public Aktivnost(String vozrasnoliceID, String volonterID, String itnost, String lokacija, String datum, String status, String rejting, String izvestaj, String povtorlivost, String ime, String vreme, String kluc){
        this.vozrasnoliceID = vozrasnoliceID;
        this.volonterID = volonterID;
        this.itnost = itnost;
        this.lokacija = lokacija;
        this.datum = datum;
        this.kluc = kluc;
        this.status = status;
        this.povtorlivost = povtorlivost;
        this.rejting = rejting;
        this.ime = ime;
        this.vreme = vreme;
        this.izvestaj = izvestaj;
        this.lat = lat;
        this.lon = lon;
    }

    public String getVozrasnoliceID() {
        return vozrasnoliceID;
    }

    public void setVozrasnoliceID(String vozrasnoliceID) {
        this.vozrasnoliceID = vozrasnoliceID;
    }

    public String getVolonterID() {
        return volonterID;
    }

    public void setVolonterID(String volonterID) {
        this.volonterID = volonterID;
    }

    public String getItnost() {
        return itnost;
    }

    public void setItnost(String itnost) {
        this.itnost = itnost;
    }

    public String getPovtorlivost() {
        return povtorlivost;
    }

    public void setPovtorlivost(String povtorlivost) {
        this.povtorlivost = povtorlivost;
    }

    public String getLokacija() {
        return lokacija;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }

    public String getVreme() {
        return vreme;
    }

    public void setVreme(String vreme) {
        this.vreme = vreme;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRejting() {
        return rejting;
    }

    public void setRejting(String rejting) {
        this.rejting = rejting;
    }

    public String getIzvestaj() {
        return izvestaj;
    }

    public void setIzvestaj(String izvestaj) {
        this.izvestaj = izvestaj;
    }

    public String getIme() {
        return ime;
    }

    public String getKluc() {
        return kluc;
    }

    public void setKluc(String kluc) {
        this.kluc = kluc;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }
}
