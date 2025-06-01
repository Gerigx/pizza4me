package de.hsos.swa.Kunden.Controller;

import java.util.List;

import de.hsos.swa.Kunden.Entity.Kunde;
import de.hsos.swa.Kunden.Entity.KundeCatalog;
import jakarta.inject.Inject;

public class KundeService implements KundeController{

    @Inject
    KundeCatalog kundeCatalog;

    @Override
    public Kunde getKunde(Long id) {
        return kundeCatalog.getKunde(id);
    }

    @Override
    public List<Kunde> getAllKunden(int page, int size) {
        return kundeCatalog.getAllKunden(page, size);
    }

    @Override
    public Kunde updateKunde(Long id, Kunde kunde) {
        return kundeCatalog.updateKunde(id, kunde);
    }

    @Override
    public Kunde addKunde(Kunde kunde) {
        return kundeCatalog.addKunde(kunde);

    }

    @Override
    public Kunde deleteKunde(Long id) {
        return kundeCatalog.deleteKunde(id);
    }

}
