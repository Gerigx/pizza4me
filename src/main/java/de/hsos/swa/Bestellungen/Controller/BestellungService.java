package de.hsos.swa.Bestellungen.Controller;

import de.hsos.swa.shared.Events.PizzaBestelltEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

@ApplicationScoped
public class BestellungService {
    @Inject
    Event<PizzaBestelltEvent> pizzaEvent;

}
