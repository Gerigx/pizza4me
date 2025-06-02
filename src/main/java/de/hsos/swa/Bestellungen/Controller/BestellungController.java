package de.hsos.swa.Bestellungen.Controller;

import de.hsos.swa.Bestellungen.Entity.Bestellung;

import java.util.List;

public interface BestellungController {
    public Bestellung getBestellung(long id);
    public List<Bestellung> getAllBestellungen(int page, int size);

    public Bestellung createBestellung(Bestellung bestellung);

    public Bestellung updateBestellung(long id, Bestellung bestellung);

    public Bestellung deleteBestellung(long id);

    public boolean isExisting(long id);
}
