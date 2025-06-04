package de.hsos.swa.Bestellungen.Entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Bestellung implements Comparable<Bestellung> {
    @Id
    @GeneratedValue
    private Long id;

    private Long kundeId;
    private Zustand zustand;
    private String hinweis;
    private Timestamp bestellDatum;
    private BigDecimal gesamtPreis;

    @OneToMany(mappedBy = "bestellung", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<BestellungItem> bestellungItems = new ArrayList<>();

    public Bestellung() {
    }

    public Bestellung(Long kundeId, Zustand zustand, String hinweis, Timestamp bestellDatum, BigDecimal gesamtPreis, List<BestellungItem> bestellungItems) {
        this.kundeId = kundeId;
        this.zustand = zustand;
        this.hinweis = hinweis;
        this.bestellDatum = bestellDatum;
        this.gesamtPreis = gesamtPreis;
        this.bestellungItems = bestellungItems != null ? bestellungItems : new ArrayList<>();
        
        // Bidirektionale Referenz setzen
        for (BestellungItem item : this.bestellungItems) {
            item.setBestellung(this);
        }
    }

    public void addBestellungItem(BestellungItem item) {
        if (this.bestellungItems == null) {
            this.bestellungItems = new ArrayList<>();
        }
        this.bestellungItems.add(item);
        item.setBestellung(this);  // Bidirektionale Referenz
    }

    public void removeBestellungItem(BestellungItem item) {
        if (this.bestellungItems != null) {
            this.bestellungItems.remove(item);
            item.setBestellung(null);
        }
    }

    public BigDecimal calculateGesamtPreis() {
        if (bestellungItems == null || bestellungItems.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        return bestellungItems.stream()
                .map(BestellungItem::getGesamtpreis)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public List<BestellungItem> getBestellungItems() {
        return bestellungItems != null ? new ArrayList<>(bestellungItems) : new ArrayList<>();
    }

    public void setBestellungItems(List<BestellungItem> bestellungItems) {
        if (this.bestellungItems != null) {
            for (BestellungItem item : this.bestellungItems) {
                item.setBestellung(null);
            }
        }
        
        this.bestellungItems = bestellungItems != null ? new ArrayList<>(bestellungItems) : new ArrayList<>();
        
        for (BestellungItem item : this.bestellungItems) {
            item.setBestellung(this);
        }
    }

        @Override
    public int compareTo(Bestellung other) {
        if (this.id == null && other.id == null) return 0;
        if (this.id == null) return 1;
        if (other.id == null) return -1;
        return other.id.compareTo(this.id); 
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getKundeId() { return kundeId; }
    public void setKundeId(Long kundeId) { this.kundeId = kundeId; }

    public Zustand getZustand() { return zustand; }
    public void setZustand(Zustand zustand) { this.zustand = zustand; }

    public String getHinweis() { return hinweis; }
    public void setHinweis(String hinweis) { this.hinweis = hinweis; }

    public Timestamp getBestellDatum() { return bestellDatum; }
    public void setBestellDatum(Timestamp bestellDatum) { this.bestellDatum = bestellDatum; }

    public BigDecimal getGesamtPreis() { return gesamtPreis; }
    public void setGesamtPreis(BigDecimal gesamtPreis) { this.gesamtPreis = gesamtPreis; }

    @Override
    public String toString() {
        return "Bestellung{" +
                "id=" + id +
                ", kundeId=" + kundeId +
                ", zustand=" + zustand +
                ", bestellungItems=" + (bestellungItems != null ? bestellungItems.size() + " items" : "0 items") +
                ", hinweis='" + hinweis + '\'' +
                ", bestellDatum=" + bestellDatum +
                ", gesamtPreis=" + gesamtPreis +
                '}';
    }
}