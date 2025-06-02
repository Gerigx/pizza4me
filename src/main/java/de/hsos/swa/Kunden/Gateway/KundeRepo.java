package de.hsos.swa.Kunden.Gateway;

import java.util.List;

import de.hsos.swa.Kunden.Entity.Adresse;
import de.hsos.swa.Kunden.Entity.Kunde;
import de.hsos.swa.Kunden.Entity.KundeCatalog;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;


@ApplicationScoped
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
    @Transactional
    public Kunde updateKunde(Long id, Kunde kunde) {
        Kunde existingKunde = findById(id);
        if (existingKunde == null) {
            throw new IllegalArgumentException("Kunde mit ID " + id + " nicht gefunden");
        }
        
        // Nur Geschäftsdaten updaten, NICHT Security-Felder!
        if (kunde.getVorname() != null) {
            existingKunde.setVorname(kunde.getVorname());
        }
        if (kunde.getNachname() != null) {
            existingKunde.setNachname(kunde.getNachname());
        }
        if (kunde.getAdresse() != null) {
            existingKunde.setAdresse(kunde.getAdresse());
        }
        if (kunde.getOrders() != null) {
            existingKunde.setOrders(kunde.getOrders());
        }
        
        
        persist(existingKunde);
        return existingKunde;
    }

    @Override
    @Transactional
    public Kunde addKunde(Kunde kunde) {
        if (kunde == null) {
            throw new IllegalArgumentException("Kunde darf nicht null sein");
        }
        
        // Prüfen ob Username bereits existiert
        if (find("username", kunde.getUsername()).firstResult() != null) {
            throw new IllegalArgumentException("Username bereits vergeben: " + kunde.getUsername());
        }
        
        persist(kunde);
        return kunde;
    }

    @Override
    @Transactional
    public Kunde deleteKunde(Long id) {
        Kunde kunde = findById(id);
        if (kunde == null) {
            throw new IllegalArgumentException("Kunde mit ID " + id + " nicht gefunden");
        }
        
        delete(kunde);
        return kunde;
    }

    // Adress

    @Override
    public Adresse getKundeAdresse(Long kundeId) {
        Kunde kunde = findById(kundeId);
        if (kunde == null) {
            throw new IllegalArgumentException("Kunde mit ID " + kundeId + " nicht gefunden");
        }
        return kunde.getAdresse();
    }

    @Override
    @Transactional
    public Kunde updateKundeAdresse(Long kundeId, Adresse adresse) {
        Kunde kunde = findById(kundeId);
        if (kunde == null) {
            throw new IllegalArgumentException("Kunde mit ID " + kundeId + " nicht gefunden");
        }
        
        kunde.setAdresse(adresse);
        persist(kunde);
        return kunde;
    }

    @Override
    @Transactional
    public Kunde deleteKundeAdresse(Long kundeId) {
        Kunde kunde = findById(kundeId);
        if (kunde == null) {
            throw new IllegalArgumentException("Kunde mit ID " + kundeId + " nicht gefunden");
        }
        
        kunde.setAdresse(null);
        persist(kunde);
        return kunde;
    }

    // security

    @Override
    public Kunde findByUsername(String username) {
        return find("username", username).firstResult();
    }

    @Override
    public boolean existsByUsername(String username) {
        return find("username", username).firstResult() != null;
    }

    @Transactional
    public boolean changePassword(Long kundeId, String newPassword) {
        Kunde kunde = findById(kundeId);
        if (kunde == null) {
            return false;
        }
        
        kunde.changePassword(newPassword);
        persist(kunde);
        return true;
    }

    @Transactional
    public boolean changeRole(Long kundeId, String newRole) {
        Kunde kunde = findById(kundeId);
        if (kunde == null) {
            return false;
        }
        
        kunde.setRole(newRole);
        persist(kunde);
        return true;
    }

}
