package de.hsos.swa.Bestellungen.Boundry.DTO;

import de.hsos.swa.Bestellungen.Entity.Bestellung;
import de.hsos.swa.Bestellungen.Entity.Zustand;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class BestellungDTO {
    private Long kundeId;
    private String zustand;
    private String hinweis;
    private String bestellDatum;
    private String gesamtPreis;
    
    private List<BestellungItemDTO> items;

    public BestellungDTO() {
    }

    public BestellungDTO(Long kundeId, String zustand, String hinweis, String bestellDatum, String gesamtPreis, List<BestellungItemDTO> items) {
        this.kundeId = kundeId;
        this.zustand = zustand;
        this.hinweis = hinweis;
        this.bestellDatum = bestellDatum;
        this.gesamtPreis = gesamtPreis;
        this.items = items;
    }

    public static BestellungDTO toDTO(Bestellung bestellung) {
        List<BestellungItemDTO> itemDTOs = bestellung.getBestellungItems().stream()
                .map(BestellungItemDTO::toDTO)
                .collect(Collectors.toList());

        return new BestellungDTO(
            bestellung.getKundeId(),
            bestellung.getZustand().name(),
            bestellung.getHinweis(),
            bestellung.getBestellDatum().toString(),
            bestellung.getGesamtPreis().toString(),
            itemDTOs
        );
    }

    public static Bestellung fromDTO(BestellungDTO dto) {
        Bestellung bestellung = new Bestellung();
        bestellung.setKundeId(dto.getKundeId());
        bestellung.setZustand(Zustand.valueOf(dto.getZustand()));
        bestellung.setHinweis(dto.getHinweis());
        bestellung.setBestellDatum(Timestamp.valueOf(dto.getBestellDatum()));
        bestellung.setGesamtPreis(new BigDecimal(dto.getGesamtPreis()));
        
        
        return bestellung;
    }

    public List<BestellungItemDTO> getItems() { return items; }
    public void setItems(List<BestellungItemDTO> items) { this.items = items; }

    public Long getKundeId() { return kundeId; }
    public void setKundeId(Long kundeId) { this.kundeId = kundeId; }

    public String getZustand() { return zustand; }
    public void setZustand(String zustand) { this.zustand = zustand; }

    public String getHinweis() { return hinweis; }
    public void setHinweis(String hinweis) { this.hinweis = hinweis; }

    public String getBestellDatum() { return bestellDatum; }
    public void setBestellDatum(String bestellDatum) { this.bestellDatum = bestellDatum; }

    public String getGesamtPreis() { return gesamtPreis; }
    public void setGesamtPreis(String gesamtPreis) { this.gesamtPreis = gesamtPreis; }
}