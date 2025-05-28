package de.hsos.swa.Pizza.Controller;

import java.util.List;

import de.hsos.swa.Pizza.Entity.Pizza;

public interface PizzaController {
    public Pizza getPizza(long id);
    public List<Pizza> getAllPizzas();

    public Pizza createPizza(Pizza pizza);

    public Pizza updatePizza(long id, Pizza pizza);

    public Pizza deletePizza(long id);

}
