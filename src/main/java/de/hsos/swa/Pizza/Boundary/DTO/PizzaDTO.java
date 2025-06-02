package de.hsos.swa.Pizza.Boundary.DTO;

import java.math.BigDecimal;

import de.hsos.swa.Pizza.Entity.Pizza;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.FormParam;

public class PizzaDTO {


    public long id;

    @FormParam("name")
    @NotBlank(message = "Name ist erforderlich")
    @Size(min = 2, max = 50, message = "Name muss zwischen 2 und 50 Zeichen haben")
    public String name;

    @FormParam("beschreibung")
    @NotBlank(message = "Beschreibung ist erforderlich")
    @Size(max = 200, message = "Beschreibung zu lang")
    public String beschreibung;

    @FormParam("preis")
    @DecimalMin(value = "0.01", message = "Preis muss größer als 0 sein")
    @DecimalMax(value = "999.99", message = "Preis zu hoch")
    public BigDecimal price;

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
