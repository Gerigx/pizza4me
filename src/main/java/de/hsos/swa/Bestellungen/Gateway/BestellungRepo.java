package de.hsos.swa.Bestellungen.Gateway;

import de.hsos.swa.Bestellungen.Entity.Bestellung;
import de.hsos.swa.Bestellungen.Entity.BestellungItem;
import de.hsos.swa.Bestellungen.Entity.BestellungKatalog;
import de.hsos.swa.shared.Events.PizzaBestelltEvent;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class BestellungRepo implements BestellungKatalog, PanacheRepository<Bestellung> {

    @Inject
    Event<PizzaBestelltEvent> pizzaBestelltEvent;  // kinde optional, wird nicht so ganz genutzt

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
    @Transactional
    public Bestellung createBestellung(Bestellung bestellung) {
        this.persist(bestellung);
        
        if (bestellung.getBestellungItems() != null) {
            for (BestellungItem item : bestellung.getBestellungItems()) {
                for (int i = 0; i < item.getQuantity(); i++) {
                    pizzaBestelltEvent.fireAsync(new PizzaBestelltEvent(item.getPizza().getId()));
                }
                
                System.out.println("🍕 PizzaBestelltEvent gefeuert für Pizza ID: " + 
                                 item.getPizza().getId() + " (" + item.getQuantity() + "x)");
            }
        }
        
        return bestellung;
    }

    @Override
    @Transactional
    public Bestellung updateBestellung(long id, Bestellung bestellung) {
        Bestellung existingBestellung = findById(id);
        if (existingBestellung == null) {
            throw new IllegalArgumentException("Bestellung mit ID " + id + " nicht gefunden");
        }

        existingBestellung.setBestellDatum(bestellung.getBestellDatum());
        existingBestellung.setZustand(bestellung.getZustand());
        existingBestellung.setKundeId(bestellung.getKundeId());
        existingBestellung.setHinweis(bestellung.getHinweis());
        
        if (bestellung.getGesamtPreis() != null) {
            existingBestellung.setGesamtPreis(bestellung.getGesamtPreis());
        }

        
        this.persist(existingBestellung);
        return existingBestellung;
    }

    @Override
    @Transactional
    public Bestellung deleteBestellung(long id) {
        Bestellung bestellung = findById(id);
        if (bestellung == null) {
            throw new IllegalArgumentException("Bestellung mit ID " + id + " nicht gefunden");
        }
        
        this.delete(bestellung);
        return bestellung;
    }

    @Override
    public boolean isExisting(long id) {
        return findById(id) != null;
    }

    
    @Transactional
    public BestellungItem addBestellungItem(long bestellungId, BestellungItem item) {
        Bestellung bestellung = findById(bestellungId);
        if (bestellung == null) {
            throw new IllegalArgumentException("Bestellung mit ID " + bestellungId + " nicht gefunden");
        }
        
        bestellung.addBestellungItem(item);
        
        bestellung.setGesamtPreis(bestellung.calculateGesamtPreis());
        
        this.persist(bestellung);
        
        for (int i = 0; i < item.getQuantity(); i++) {
            pizzaBestelltEvent.fireAsync(new PizzaBestelltEvent(item.getPizza().getId()));
        }
        
        return item;
    }

    @Transactional
    public boolean removeBestellungItem(long bestellungId, long bestellungItemId) {
        Bestellung bestellung = findById(bestellungId);
        if (bestellung == null) {
            return false;
        }
        
        BestellungItem toRemove = bestellung.getBestellungItems().stream()
                .filter(item -> item.getId().equals(bestellungItemId))
                .findFirst()
                .orElse(null);
                
        if (toRemove == null) {
            return false;
        }
        
        bestellung.removeBestellungItem(toRemove);
        
        bestellung.setGesamtPreis(bestellung.calculateGesamtPreis());
        
        this.persist(bestellung);
        return true;
    }

    public List<Bestellung> findByKundeId(long kundeId, int page, int size) {
        return find("kundeId", kundeId)
                .page(Page.of(page, size))
                .stream()
                .toList();
    }

    public List<Bestellung> findByZustand(String zustand, int page, int size) {
        return find("zustand", zustand)
                .page(Page.of(page, size))
                .stream()
                .toList();
    }
}