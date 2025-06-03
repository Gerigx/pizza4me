package de.hsos.swa.Kunden.Boundary.DTO;

public class AuthResponseDTO {
    public String token;
    public KundeDTO kunde;  
    public String role;
    public long expiresAt;

    public AuthResponseDTO(String token, KundeDTO kunde, String role, long expiresAt) {
        this.token = token;
        this.kunde = kunde;
        this.role = role;
        this.expiresAt = expiresAt;
    }

}
