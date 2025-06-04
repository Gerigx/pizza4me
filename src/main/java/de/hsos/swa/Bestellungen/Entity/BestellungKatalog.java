package de.hsos.swa.Bestellungen.Entity;

import java.util.List;

public interface BestellungKatalog {
    public Bestellung getBestellung(long id);
    public List<Bestellung> getAllBestellungen(int page, int size);
    public List<Bestellung> getBestellungenByKundeId(long kundeId, int page, int size);

    public Bestellung createBestellung(Bestellung bestellung);

    public Bestellung updateBestellung(long id, Bestellung bestellung);

    public Bestellung deleteBestellung(long id);

    public boolean isExisting(long id);
}
