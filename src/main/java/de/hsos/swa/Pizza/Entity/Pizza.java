package de.hsos.swa.Pizza.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Pizza {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String beschreibung;
    private float price;
    private int sellCounter;

    
    


    public Pizza() {
    }

    public Pizza(String name, String beschreibung, float price) {
        this.name = name;
        this.beschreibung = beschreibung;
        this.price = price;
        this.sellCounter = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    

    public int getSellCounter() {
        return sellCounter;
    }

    public void setSellCounter(int sellCounter) {
        this.sellCounter = sellCounter;
    }

    public long getId() {
        return id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pizza other = (Pizza) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Pizza [id=" + id + ", name=" + name + ", beschreibung=" + beschreibung + ", price=" + price + "]";
    }    

}
