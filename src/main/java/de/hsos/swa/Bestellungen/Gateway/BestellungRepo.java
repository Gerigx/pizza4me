package de.hsos.swa.Bestellungen.Gateway;

import de.hsos.swa.Bestellungen.Entity.Bestellung;
import de.hsos.swa.Bestellungen.Entity.BestellungKatalog;
import de.hsos.swa.Pizza.Entity.Pizza;
import de.hsos.swa.Pizza.Entity.PizzaCatalog;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;

import java.util.List;

public class BestellungRepo implements BestellungKatalog, PanacheRepository<Bestellung> {


    @Override
    public Bestellung getBestellung(long id) {
        return findById(id);
    }

    @Override
    public List<Bestellung> getAllBestellungen(int page, int size) {
        return findAll()
            .page(Page.of(page, size))
            .stream()
            .toList();
    }

    @Override
    public Bestellung createBestellung(Bestellung bestellung) {
        this.persist(bestellung);
        return bestellung;
    }

    @Override
    public Bestellung updateBestellung(long id, Bestellung bestellung) {
        Bestellung bestellungNeu = findById(id);
        bestellungNeu.setBestellDatum(bestellung.getBestellDatum());
        bestellungNeu.setPizzas(bestellung.getPizzas());
        bestellungNeu.setZustand(bestellung.getZustand());
        bestellungNeu.setKundeId(bestellung.getKundeId());
        bestellungNeu.setGesamtPreis(bestellung.getGesamtPreis());
        bestellungNeu.setHinweis(bestellung.getHinweis());


        this.persist(bestellungNeu);
        return bestellungNeu;
    }

    @Override
    public Bestellung deleteBestellung(long id) {
        Bestellung bestellung = findById(id);
        this.delete(bestellung);
        return bestellung;
    }

    @Override
    public boolean isExisting(long id) {
        if (findById(id) == null) {
            return false;
        }
        return true;
    }
}
