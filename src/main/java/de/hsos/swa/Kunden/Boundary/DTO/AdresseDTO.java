package de.hsos.swa.Kunden.Boundary.DTO;

import de.hsos.swa.Kunden.Entity.Adresse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AdresseDTO {

    @NotBlank(message = "Straße ist erforderlich")
    @Size(max = 100, message = "Straße zu lang")
    public String strasse;

    @NotBlank(message = "Hausnummer ist erforderlich")
    @Size(max = 10, message = "Hausnummer zu lang")
    public String hausnummer;

    @NotBlank(message = "PLZ ist erforderlich")
    @Size(min = 5, max = 5, message = "PLZ muss 5 Zeichen haben")
    public String plz;

    @NotBlank(message = "Ort ist erforderlich")
    @Size(max = 50, message = "Ort zu lang")
    public String ort;

    @Size(max = 50, message = "Land zu lang")
    public String land = "Deutschland";

    // Korrigiertes Mapping - jetzt passend zu deiner Adresse.java
    public static AdresseDTO toDTO(Adresse adresse) {
        if (adresse == null) return null;
        
        AdresseDTO adresseDTO = new AdresseDTO();
        adresseDTO.strasse = adresse.getStrasse();      // ✅ Jetzt richtig
        adresseDTO.hausnummer = adresse.getHausnummer(); // ✅ War vergessen
        adresseDTO.plz = adresse.getPlz();              // ✅ Jetzt richtig
        adresseDTO.ort = adresse.getOrt();              // ✅ Jetzt richtig
        adresseDTO.land = adresse.getLand();            // ✅ Jetzt richtig
        return adresseDTO;
    }

    public static Adresse fromDTO(AdresseDTO adresseDTO) {
        if (adresseDTO == null) return null;
        
        Adresse adresse = new Adresse();
        adresse.setStrasse(adresseDTO.strasse);
        adresse.setHausnummer(adresseDTO.hausnummer);   // ✅ War vergessen
        adresse.setPlz(adresseDTO.plz);
        adresse.setOrt(adresseDTO.ort);
        adresse.setLand(adresseDTO.land != null ? adresseDTO.land : "Deutschland");
        return adresse;
    }
}