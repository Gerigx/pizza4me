package de.hsos.swa.Bestellungen.Boundry.DTO;

import de.hsos.swa.Bestellungen.Entity.BestellungItem;
import java.math.BigDecimal;

public class BestellungItemDTO {
    public Long id;
    public Long pizzaId;
    public String pizzaName;        
    public String pizzaBeschreibung; 
    public Integer quantity;
    public String einzelpreis;      
    public String gesamtpreis;      
    
    public BestellungItemDTO() {}
    
    public BestellungItemDTO(Long pizzaId, String pizzaName, String pizzaBeschreibung, 
                           Integer quantity, String einzelpreis, String gesamtpreis) {
        this.pizzaId = pizzaId;
        this.pizzaName = pizzaName;
        this.pizzaBeschreibung = pizzaBeschreibung;
        this.quantity = quantity;
        this.einzelpreis = einzelpreis;
        this.gesamtpreis = gesamtpreis;
    }
    
    // Entity zu DTO
    public static BestellungItemDTO toDTO(BestellungItem item) {
        BestellungItemDTO dto = new BestellungItemDTO();
        dto.id = item.getId();
        dto.pizzaId = item.getPizza().getId();
        dto.pizzaName = item.getPizza().getName();
        dto.pizzaBeschreibung = item.getPizza().getBeschreibung();
        dto.quantity = item.getQuantity();
        dto.einzelpreis = item.getEinzelpreis().toString();
        dto.gesamtpreis = item.getGesamtpreis().toString();
        return dto;
    }
    
    public static BestellungItem fromDTO(BestellungItemDTO dto) {
        BestellungItem item = new BestellungItem();
        item.setQuantity(dto.quantity);
        item.setEinzelpreis(new BigDecimal(dto.einzelpreis));
        return item;
    }
}