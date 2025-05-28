package de.hsos.swa.Pizza.Controller;

import java.util.List;

import de.hsos.swa.Pizza.Entity.Pizza;
import de.hsos.swa.Pizza.Entity.PizzaCatalog;
import jakarta.inject.Inject;

public class PizzaService implements PizzaController {

    @Inject
    PizzaCatalog pizzaCatalog;

    @Override
    public Pizza getPizza(long id) {
        return pizzaCatalog.getPizza(id);
    }

    @Override
    public List<Pizza> getAllPizzas() {
        return pizzaCatalog.getAllPizzas();
    }

    @Override
    public Pizza createPizza(Pizza pizza) {
        return pizzaCatalog.createPizza(pizza);

    }

    @Override
    public Pizza updatePizza(long id, Pizza pizza) {
        return pizzaCatalog.updatePizza(id, pizza);

    }

    @Override
    public Pizza deletePizza(long id) {
        return pizzaCatalog.deletePizza(id);

    }

}
