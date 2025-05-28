package de.hsos.swa.Pizza.Entity;

import java.util.List;

public interface PizzaCatalog {
    public Pizza getPizza(long id);
    public List<Pizza> getAllPizzas();

    public Pizza createPizza(Pizza pizza);

    public Pizza updatePizza(long id, Pizza pizza);

    public Pizza deletePizza(long id);
}
