package de.hsos.swa.Kunden.Controller;

import java.util.List;

import de.hsos.swa.Kunden.Entity.Adresse;
import de.hsos.swa.Kunden.Entity.Kunde;
import de.hsos.swa.Kunden.Entity.KundeCatalog;
import de.hsos.swa.shared.Events.BestellungCreatedEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.ObservesAsync;
import jakarta.inject.Inject;

@ApplicationScoped
public class KundeService implements KundeController{

    @Inject
    KundeCatalog kundeCatalog;

    // ausgaben von ai verschönert
    public void onBestellungCreated(@ObservesAsync BestellungCreatedEvent event) {
        
        try {
            Kunde kunde = kundeCatalog.getKunde(event.getKundeId());
            if (kunde == null) {
                System.err.println("❌ Kunde mit ID " + event.getKundeId() + " nicht gefunden!");
                return;
            }

            kunde.addOrder(event.getBestellungId());
            
            kundeCatalog.updateKunde(kunde.getId(), kunde);
            
            System.out.println("✅ Bestellung " + event.getBestellungId() + 
                             " zu Kunde " + kunde.getFullName() + " hinzugefügt (async)");
                             
        } catch (Exception e) {
            System.err.println("❌ Fehler beim async Verarbeiten des BestellungCreatedEvent: " + e.getMessage());
            e.printStackTrace();
        }
    }

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

    @Override
    public Adresse getKundeAdresse(Long kundeId) {
        return kundeCatalog.getKundeAdresse(kundeId);
    }

    @Override
    public Kunde updateKundeAdresse(Long kundeId, Adresse adresse) {
        return kundeCatalog.updateKundeAdresse(kundeId, adresse);
    }

    @Override
    public Kunde deleteKundeAdresse(Long kundeId) {
        return kundeCatalog.deleteKundeAdresse(kundeId);
    }

        @Override
    public Kunde findByUsername(String username) {
        return kundeCatalog.findByUsername(username);
    }

    @Override
    public boolean existsByUsername(String username) {
        return kundeCatalog.existsByUsername(username);
    }

}
