package de.hsos.swa.Pizza.Gateway;

import java.util.List;

import de.hsos.swa.Pizza.Entity.Pizza;
import de.hsos.swa.Pizza.Entity.PizzaCatalog;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

public class PizzaRepo implements PizzaCatalog, PanacheRepository<Pizza> {

    @Override
    public Pizza getPizza(long id) {
        return findById(id);
    }

    @Override
    public List<Pizza> getAllPizzas() {
        return listAll();
    }

    @Override
    public Pizza createPizza(Pizza pizza) {
        this.persist(pizza);
        return pizza;
    }

    @Override
    public Pizza updatePizza(long id, Pizza pizza) {
        Pizza pizzaNeu = findById(id);
        pizzaNeu.setBeschreibung(pizza.getBeschreibung());
        pizzaNeu.setPrice(pizza.getPrice());
        pizzaNeu.setName(pizza.getName());

        this.persist(pizzaNeu);
        return pizzaNeu;
    }

    @Override
    public Pizza deletePizza(long id) {
        Pizza pizza = findById(id);
        this.delete(pizza);

        return pizza;
    }

}
