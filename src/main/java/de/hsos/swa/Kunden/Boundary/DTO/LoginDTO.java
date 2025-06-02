package de.hsos.swa.Kunden.Boundary.DTO;

import jakarta.validation.constraints.NotBlank;

public class LoginDTO {        
    @NotBlank(message = "Username ist erforderlich")
    public String username;

    @NotBlank(message = "Password ist erforderlich")  
    public String password;

}
