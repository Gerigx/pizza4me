package de.hsos.swa.Kunden.Controller;

import java.util.List;

import de.hsos.swa.Kunden.Entity.Adresse;
import de.hsos.swa.Kunden.Entity.Kunde;

public interface KundeController {

    public Kunde getKunde(Long id);
    public List<Kunde> getAllKunden(int page, int size);

    public Kunde updateKunde(Long id, Kunde kunde);

    public Kunde addKunde(Kunde kunde);

    public Kunde deleteKunde(Long id);

    // adresse

    public Adresse getKundeAdresse(Long kundeId);
    public Kunde updateKundeAdresse(Long kundeId, Adresse adresse);
    public Kunde deleteKundeAdresse(Long kundeId);

    public Kunde findByUsername(String username);
    
    // Wie dein existsByName für Pizza
    public boolean existsByUsername(String username);

}
