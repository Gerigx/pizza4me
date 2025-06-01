package de.hsos.swa.Kunden.Entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="Kunde")
public class Kunde {
    @Id
    @GeneratedValue
    private long id;

    private List<Long> orders;

    private String vorname;
    private String nachname;

    @Embedded
    private Adresse adresse;


    public Kunde() {
        orders = new ArrayList<>();
    }

    public Kunde(List<Long> orders, String vorname, String nachname, Adresse adresse) {
        this.orders = orders;
        this.vorname = vorname;
        this.nachname = nachname;
        this.adresse = adresse;
    }

    public List<Long> getOrders() {
        return orders;
    }

    public void setOrders(List<Long> orders) {
        this.orders = orders;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public long getId() {
        return id;
    }

    
}
