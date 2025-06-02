package de.hsos.swa.Kunden.Entity;

import java.util.List;

public interface KundeCatalog {

    public Kunde getKunde(Long id);
    public List<Kunde> getAllKunden(int page, int size);

    public Kunde updateKunde(Long id, Kunde kunde);

    public Kunde addKunde(Kunde kunde);

    public Kunde deleteKunde(Long id);

    public Adresse getKundeAdresse(Long kundeId);
    public Kunde updateKundeAdresse(Long kundeId, Adresse adresse);
    public Kunde deleteKundeAdresse(Long kundeId);

    public Kunde findByUsername(String username);
    public boolean existsByUsername(String username);

}
