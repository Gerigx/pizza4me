package de.hsos.swa.Bestellungen.Entity;

import de.hsos.swa.Pizza.Entity.Pizza;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "bestellung_item")
public class BestellungItem {
    
    @Id
    @GeneratedValue
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bestellung_id", nullable = false)
    private Bestellung bestellung;
    
    @ManyToOne(fetch = FetchType.EAGER)  // EAGER weil wir Pizza-Details oft brauchen
    @JoinColumn(name = "pizza_id", nullable = false)
    private Pizza pizza;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(name = "einzelpreis", nullable = false, precision = 10, scale = 2)
    private BigDecimal einzelpreis;  
    
    @Column(name = "gesamtpreis", nullable = false, precision = 10, scale = 2)  
    private BigDecimal gesamtpreis;  
    
    // Konstruktoren
    public BestellungItem() {}
    
    public BestellungItem(Bestellung bestellung, Pizza pizza, Integer quantity, BigDecimal einzelpreis) {
        this.bestellung = bestellung;
        this.pizza = pizza;
        this.quantity = quantity;
        this.einzelpreis = einzelpreis;
        this.gesamtpreis = einzelpreis.multiply(new BigDecimal(quantity));
    }
    
    public static BestellungItem create(Bestellung bestellung, Pizza pizza, Integer quantity) {
        return new BestellungItem(bestellung, pizza, quantity, pizza.getPrice());
    }
    
    public Long getId() { return id; }
    
    public Bestellung getBestellung() { return bestellung; }
    public void setBestellung(Bestellung bestellung) { this.bestellung = bestellung; }
    
    public Pizza getPizza() { return pizza; }
    public void setPizza(Pizza pizza) { this.pizza = pizza; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { 
        this.quantity = quantity;
        if (this.einzelpreis != null) {
            this.gesamtpreis = this.einzelpreis.multiply(new BigDecimal(quantity));
        }
    }
    
    public BigDecimal getEinzelpreis() { return einzelpreis; }
    public void setEinzelpreis(BigDecimal einzelpreis) { 
        this.einzelpreis = einzelpreis;
        if (this.quantity != null) {
            this.gesamtpreis = einzelpreis.multiply(new BigDecimal(this.quantity));
        }
    }
    
    public BigDecimal getGesamtpreis() { return gesamtpreis; }
    public void setGesamtpreis(BigDecimal gesamtpreis) { this.gesamtpreis = gesamtpreis; }
    
    @Override
    public String toString() {
        return "BestellungItem{" +
                "id=" + id +
                ", pizza=" + (pizza != null ? pizza.getName() : "null") +
                ", quantity=" + quantity +
                ", einzelpreis=" + einzelpreis +
                ", gesamtpreis=" + gesamtpreis +
                '}';
    }
}