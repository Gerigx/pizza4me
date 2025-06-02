package de.hsos.swa.Kunden.Boundary.DTO;

import de.hsos.swa.Kunden.Entity.Kunde;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistrationDTO {
        
    @NotBlank(message = "Vorname ist erforderlich")
    @Size(max = 50)
    public String vorname;
    
    @NotBlank(message = "Nachname ist erforderlich")  
    @Size(max = 50)
    public String nachname;
    
    @NotBlank(message = "Username ist erforderlich")
    @Size(min = 3, max = 30)
    public String username;
    
    @NotBlank(message = "Password ist erforderlich")
    @Size(min = 8, message = "Password muss mindestens 8 Zeichen haben")
    public String password;
    
    public String role = "USER";

    public AdresseDTO adresse;

    public static Kunde fromDTO(RegistrationDTO registrationDTO) {
        Kunde kunde = new Kunde(
            registrationDTO.vorname,
            registrationDTO.nachname,
            registrationDTO.username,
            registrationDTO.password,
            registrationDTO.role
        );
        
        if (registrationDTO.adresse != null) {
            kunde.setAdresse(AdresseDTO.fromDTO(registrationDTO.adresse));
        }
        
        return kunde;
    }

}
