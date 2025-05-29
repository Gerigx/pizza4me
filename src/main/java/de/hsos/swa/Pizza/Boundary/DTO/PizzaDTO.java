package de.hsos.swa.Pizza.Boundary.DTO;

import de.hsos.swa.Pizza.Entity.Pizza;

public class PizzaDTO {
    public long id;
    public String name;
    public String beschreibung;
    public float price;
    public int sellCounter;

    public static Pizza fromDTO(PizzaDTO pizzaDTO){
        Pizza pizza = new Pizza(pizzaDTO.name,
                                pizzaDTO.beschreibung,
                                pizzaDTO.price);
        return pizza;
    }

    public static PizzaDTO toDTO(Pizza pizza){
        PizzaDTO pizzaDTO = new PizzaDTO();
        pizzaDTO.id = pizza.getId();
        pizzaDTO.name = pizza.getName();
        pizzaDTO.beschreibung = pizza.getBeschreibung();
        pizzaDTO.price = pizza.getPrice();
        pizzaDTO.sellCounter = pizza.getSellCounter();

        return pizzaDTO;
    }

}
