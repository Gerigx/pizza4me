package de.hsos.swa.Bestellungen.Entity;

import de.hsos.swa.Pizza.Entity.Pizza;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Entity
public class Bestellung {
    @Id
    @GeneratedValue
    private Long id;

    private Long kundeId;

    private Zustand zustand;

    @OneToMany
    private List<Pizza> pizzas;

    private String hinweis;

    private Timestamp bestellDatum;

    private BigDecimal gesamtPreis;

    public Bestellung() {
    }

    public Bestellung(Long kundeId, Zustand zustand, String hinweis, Timestamp bestellDatum, BigDecimal gesamtPreis, List<Pizza> pizzas) {
        this.pizzas = pizzas;
        this.kundeId = kundeId;
        this.zustand = zustand;
        this.hinweis = hinweis;
        this.bestellDatum = bestellDatum;
        this.gesamtPreis = gesamtPreis;
    }

    public Long getKundeId() {
        return kundeId;
    }
    public void setKundeId(Long kundeId) {
        this.kundeId = kundeId;
    }
    public Zustand getZustand() {
        return zustand;
    }
    public void setZustand(Zustand zustand) {
        this.zustand = zustand;
    }
    public String getHinweis() {
        return hinweis;
    }
    public void setHinweis(String hinweis) {
        this.hinweis = hinweis;
    }
    public Timestamp getBestellDatum() {
        return bestellDatum;
    }
    public void setBestellDatum(Timestamp bestellDatum) {
        this.bestellDatum = bestellDatum;
    }
    public BigDecimal getGesamtPreis() {
        return gesamtPreis;
    }
    public void setGesamtPreis(BigDecimal gesamtPreis) {
        this.gesamtPreis = gesamtPreis;
    }
    public List<Pizza> getPizzas() {
        return pizzas;
    }
    public void setPizzas(List<Pizza> pizzas) {
        this.pizzas = pizzas;
    }

    @Override
    public String toString() {
        return "Bestellung{" +
                "id=" + id +
                ", kundeId=" + kundeId +
                ", zustand=" + zustand +
                ", pizzas=" + pizzas +
                ", hinweis='" + hinweis + '\'' +
                ", bestellDatum=" + bestellDatum +
                ", gesamtPreis=" + gesamtPreis +
                '}';
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
