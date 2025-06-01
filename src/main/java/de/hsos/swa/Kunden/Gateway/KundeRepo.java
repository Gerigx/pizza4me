package de.hsos.swa.Kunden.Gateway;

import java.util.List;

import de.hsos.swa.Kunden.Entity.Kunde;
import de.hsos.swa.Kunden.Entity.KundeCatalog;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;

public class KundeRepo implements KundeCatalog, PanacheRepository<Kunde>{

    @Override
    public Kunde getKunde(Long id) {
        return findById(id);
    }

    @Override
    public List<Kunde> getAllKunden(int page, int size) {
        return findAll()
            .page(Page.of(page, size))
            .stream()
            .toList();
    }

    @Override
    public Kunde updateKunde(Long id, Kunde kunde) {
        Kunde kundeNeu = findById(id);
        
        // Hier die entsprechenden Setter für die Kunde-Entität
        // Je nach deinen Kunde-Attributen anpassen:
        kundeNeu.setVorname(kunde.getVorname());
        kundeNeu.setNachname(kunde.getNachname());
        kundeNeu.setAdresse(kunde.getAdresse());
        kundeNeu.setOrders(kunde.getOrders());
        
        this.persist(kundeNeu);
        return kundeNeu;
    }

    @Override
    public Kunde addKunde(Kunde kunde) {
        this.persist(kunde);
        return kunde;
    }

    @Override
    public Kunde deleteKunde(Long id) {
        Kunde kunde = findById(id);
        this.delete(kunde);
        return kunde;
    }

}
