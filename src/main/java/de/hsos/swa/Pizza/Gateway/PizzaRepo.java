package de.hsos.swa.Pizza.Gateway;

import java.util.List;

import de.hsos.swa.Pizza.Entity.Pizza;
import de.hsos.swa.Pizza.Entity.PizzaCatalog;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;

@ApplicationScoped
public class PizzaRepo implements PizzaCatalog, PanacheRepository<Pizza> {

    @Override
    public Pizza getPizza(long id) {
        return findById(id);
    }

    // https://quarkus.io/guides/hibernate-orm-panache#paging
    @Override
    public List<Pizza> getAllPizzas(int page, int size) {
        return findAll()
            .page(Page.of(page,size))
            .stream()
            .toList();
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
