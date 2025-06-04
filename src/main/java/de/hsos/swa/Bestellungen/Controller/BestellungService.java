package de.hsos.swa.Bestellungen.Controller;

import de.hsos.swa.Bestellungen.Entity.Bestellung;
import de.hsos.swa.shared.Events.PizzaBestelltEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import java.util.List;

import com.arjuna.ats.internal.jdbc.drivers.modifiers.list;

@ApplicationScoped
public class BestellungService implements BestellungController {
    @Inject
    Event<PizzaBestelltEvent> pizzaEvent;

    @Override
    public Bestellung getBestellung(long id) {
        return null;
    }

    @Override
    public List<Bestellung> getAllBestellungen(int page, int size) {
        return List.of();
    }

    @Override
    public Bestellung createBestellung(Bestellung bestellung) {
        return null;
    }

    @Override
    public Bestellung updateBestellung(long id, Bestellung bestellung) {
        return null;
    }

    @Override
    public Bestellung deleteBestellung(long id) {
        return null;
    }

    @Override
    public boolean isExisting(long id) {
        return false;
    }

    @Override
    public List<Bestellung> getBestellungenByKundeId(long kundeId, int page, int size) {
        return List.of();
    }
}
