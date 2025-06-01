package de.hsos.swa.Kunden.Controller;

import java.util.List;

import de.hsos.swa.Kunden.Entity.Kunde;

public interface KundeController {

    public Kunde getKunde(Long id);
    public List<Kunde> getAllKunden(int page, int size);

    public Kunde updateKunde(Long id, Kunde kunde);

    public Kunde addKunde(Kunde kunde);

    public Kunde deleteKunde(Long id);

}
