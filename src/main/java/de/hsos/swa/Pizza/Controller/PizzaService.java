package de.hsos.swa.Pizza.Controller;

import java.util.List;

import de.hsos.swa.Pizza.Entity.Pizza;
import de.hsos.swa.Pizza.Entity.PizzaCatalog;
import de.hsos.swa.shared.Events.PizzaBestelltEvent;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.ObservesAsync;
import jakarta.inject.Inject;

@ApplicationScoped
public class PizzaService implements PizzaController {

    @Inject
    PizzaCatalog pizzaCatalog;

    // Events
    public void onPizzaBestellt(@ObservesAsync PizzaBestelltEvent event){
        Pizza pizza = getPizza(event.getPizzaId());
        pizza.setSellCounter(pizza.getSellCounter() + 1);
        this.updatePizza(pizza.getId(), pizza);
    }


    @Override
    public Pizza getPizza(long id) {
        return pizzaCatalog.getPizza(id);
    }

    @Override
    public List<Pizza> getAllPizzas(int page, int size) {
        return pizzaCatalog.getAllPizzas(page, size);
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


    @Override
    public boolean existsByName(String name) {
        return pizzaCatalog.isExisting(name);
    }

}
