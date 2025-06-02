package de.hsos.swa.Kunden.Entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class Adresse {
    
    private String strasse;        
    private String hausnummer;     // ← War vergessen!
    private String plz;            // postalCode → plz
    private String ort;            // city → ort
    private String land;           // country → land
    
    public Adresse() {}
    
    public Adresse(String strasse, String hausnummer, String plz, String ort, String land) {
        this.strasse = strasse;
        this.hausnummer = hausnummer;
        this.plz = plz;
        this.ort = ort;
        this.land = land;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getHausnummer() {
        return hausnummer;
    }

    public void setHausnummer(String hausnummer) {
        this.hausnummer = hausnummer;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }
}