package de.hsos.swa.Bestellungen.Boundry.DTO;

import de.hsos.swa.Bestellungen.Entity.Bestellung;
import de.hsos.swa.Bestellungen.Entity.Zustand;
import de.hsos.swa.Pizza.Entity.Pizza;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.SequencedCollection;

public class BestellungDTO {
    private Long kundeId;
    private String zustand;
    private String hinweis;
    private String bestellDatum;
    private String gesamtPreis;
    private List<Pizza> pizzas;

    public BestellungDTO() {
    }

    public BestellungDTO(Long kundeId, String zustand, String hinweis, String bestellDatum, String gesamtPreis, List<Pizza> pizzas) {
        this.kundeId = kundeId;
        this.pizzas = pizzas;
        this.zustand = zustand;
        this.hinweis = hinweis;
        this.bestellDatum = bestellDatum;
        this.gesamtPreis = gesamtPreis;
    }

    public List<Pizza> getPizzas() {
        return pizzas;
    }

    public void setPizzas(List<Pizza> pizzas) {
        this.pizzas = pizzas;
    }

    public Long getKundeId() {
        return kundeId;
    }

    public void setKundeId(Long kundeId) {
        this.kundeId = kundeId;
    }

    public String getZustand() {
        return zustand;
    }

    public void setZustand(String zustand) {
        this.zustand = zustand;
    }

    public String getHinweis() {
        return hinweis;
    }

    public void setHinweis(String hinweis) {
        this.hinweis = hinweis;
    }

    public String getBestellDatum() {
        return bestellDatum;
    }

    public void setBestellDatum(String bestellDatum) {
        this.bestellDatum = bestellDatum;
    }

    public String getGesamtPreis() {
        return gesamtPreis;
    }

    public void setGesamtPreis(String gesamtPreis) {
        this.gesamtPreis = gesamtPreis;
    }

    public static BestellungDTO toDTO(Bestellung bestellung) {
        return new BestellungDTO(
            bestellung.getKundeId(),
            bestellung.getZustand().name(),
            bestellung.getHinweis(),
            bestellung.getBestellDatum().toString(),
            bestellung.getGesamtPreis().toString(),
            bestellung.getPizzas()
        );
    }

    public static Bestellung fromDTO(BestellungDTO dto) {
        return new Bestellung(
            dto.getKundeId(),
            Zustand.valueOf(dto.getZustand()),
            dto.getHinweis(),
            Timestamp.valueOf(dto.getBestellDatum()),
            new BigDecimal(dto.getGesamtPreis()),
            dto.getPizzas()
        );
    }
}
