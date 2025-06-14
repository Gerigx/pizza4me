package de.hsos.swa.Pizza.Controller;

import java.util.List;

import de.hsos.swa.Pizza.Entity.Pizza;

public interface PizzaController {
    public Pizza getPizza(long id);
    public List<Pizza> getAllPizzas(int page, int size);

    public Pizza createPizza(Pizza pizza);

    public Pizza updatePizza(long id, Pizza pizza);

    public Pizza deletePizza(long id);

    
    public boolean existsByName(String name);

}
