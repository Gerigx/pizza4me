package de.hsos.swa.shared.Events;

/*
 * Was will ich hier machen?
 * Wenn eine Pizza bestellt worden ist soll der Sales wert inkrementiert werden.
 * 
 * 
 */
public class PizzaBestelltEvent {
    private final long pizzaId;

    public PizzaBestelltEvent(long pizzaId) {
        this.pizzaId = pizzaId;
    }

    public long getPizzaId() {
        return pizzaId;
    }

    

}
