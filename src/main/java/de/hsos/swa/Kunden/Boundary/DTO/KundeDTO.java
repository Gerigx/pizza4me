package de.hsos.swa.Kunden.Boundary.DTO;

import de.hsos.swa.Kunden.Entity.Kunde;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class KundeDTO {
    public Long id;

    @NotBlank(message = "Vorname ist erforderlich")
    @Size(max = 50, message = "Vorname zu lang")
    public String vorname;

    @NotBlank(message = "Nachname ist erforderlich") 
    @Size(max = 50, message = "Nachname zu lang")
    public String nachname;

    @Valid
    public AdresseDTO adresse;

    // Wie dein PizzaDTO.fromDTO() - Entity zu DTO
    public static KundeDTO toDTO(Kunde kunde) {
        KundeDTO kundeDTO = new KundeDTO();
        kundeDTO.id = kunde.getId();
        kundeDTO.vorname = kunde.getVorname();
        kundeDTO.nachname = kunde.getNachname();
        
        if (kunde.getAdresse() != null) {
            kundeDTO.adresse = AdresseDTO.toDTO(kunde.getAdresse());
        }
        
        return kundeDTO;
    }

    // Wie dein PizzaDTO.fromDTO() - DTO zu Entity (für neue Entities)
    public static Kunde fromDTO(KundeDTO kundeDTO) {
        Kunde kunde = new Kunde();
        kunde.setVorname(kundeDTO.vorname);
        kunde.setNachname(kundeDTO.nachname);
        
        if (kundeDTO.adresse != null) {
            kunde.setAdresse(AdresseDTO.fromDTO(kundeDTO.adresse));
        }
        
        return kunde;
    }

    // Für Updates - bestehende Entity aktualisieren
    public static void updateEntity(Kunde kunde, KundeDTO kundeDTO) {
        if (kundeDTO.vorname != null) {
            kunde.setVorname(kundeDTO.vorname);
        }
        if (kundeDTO.nachname != null) {
            kunde.setNachname(kundeDTO.nachname);
        }
        if (kundeDTO.adresse != null) {
            kunde.setAdresse(AdresseDTO.fromDTO(kundeDTO.adresse));
        }
    }

}
