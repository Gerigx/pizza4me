package de.hsos.swa.Kunden.Boundary.Ressource;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.Set;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.common.util.RestMediaType;

import de.hsos.swa.Kunden.Boundary.DTO.AuthResponseDTO;
import de.hsos.swa.Kunden.Boundary.DTO.KundeDTO;
import de.hsos.swa.Kunden.Boundary.DTO.LoginDTO;
import de.hsos.swa.Kunden.Boundary.DTO.RegistrationDTO;
import de.hsos.swa.Kunden.Controller.KundeController;
import de.hsos.swa.Kunden.Entity.Kunde;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/auth")
@Produces({MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON})
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class AuthRessource {

    
    @Inject
    KundeController kundeController;

// Nur die kritischen Fixes für deine AuthRessource:

@POST
@Path("/login") 
@PermitAll  // ← DAS HAT GEFEHLT!
public RestResponse<AuthResponseDTO> login(@Valid LoginDTO loginDTO) {

    System.out.println("Test1");
    
    if (loginDTO == null) {
        throw new BadRequestException("Request body cannot be null");
    }

    System.out.println("Test2");

    try {
        // User laden 
        Kunde kunde = kundeController.findByUsername(loginDTO.username);
        if (kunde == null) {
            throw new BadRequestException("Ungültige Anmeldedaten");
        }

        System.out.println("Test3");

        // Password prüfen - OHNE getPassword()!
        if (!kunde.verifyPassword(loginDTO.password)) {
            throw new BadRequestException("Ungültige Anmeldedaten");
        }

        System.out.println("Test4");

        // JWT Token erstellen
        String token = createJwtToken(kunde);
        long expiresAt = Instant.now().plus(Duration.ofHours(24)).getEpochSecond();

        AuthResponseDTO authResponse = new AuthResponseDTO(
            token,
            KundeDTO.toDTO(kunde), 
            kunde.getRole(),
            expiresAt
        );

        System.out.println("Test5");

        return RestResponse.ok(authResponse);
        
    } catch (BadRequestException e) {
        System.out.println("Test6");
        throw e;
    } catch (Exception e) {
        throw new BadRequestException("Login fehlgeschlagen");
    }
}

// Und register-Response fixen:
@POST
@Path("/register")
@PermitAll
@Retry(maxRetries = 3, delay = 1000)
@CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.5)
@Timeout(value = 5000)
public RestResponse<AuthResponseDTO> register(@Valid RegistrationDTO registrationDTO) {
    
    if (registrationDTO == null) {
        throw new BadRequestException("Request body cannot be null");
    }

    if (kundeController.existsByUsername(registrationDTO.username)) {
        throw new BadRequestException("Username bereits vergeben: " + registrationDTO.username);
    }

    try {
        Kunde neuerKunde = RegistrationDTO.fromDTO(registrationDTO);
        Kunde gespeicherterKunde = kundeController.addKunde(neuerKunde);
        
        String token = createJwtToken(gespeicherterKunde);
        long expiresAt = Instant.now().plus(Duration.ofHours(24)).getEpochSecond();
        
        AuthResponseDTO authResponse = new AuthResponseDTO(
            token,
            KundeDTO.toDTO(gespeicherterKunde),
            gespeicherterKunde.getRole(),
            expiresAt
        );
        
        // ← HIER WAR DER FEHLER: authResponse war auskommentiert!
        return RestResponse.created(URI.create("/kunde/" + gespeicherterKunde.getId())); //authResponse
        
    } catch (Exception e) {
        throw new BadRequestException("Registrierung fehlgeschlagen: " + e.getMessage());
    }
}

    // Helper-Methode für JWT-Erstellung (wie deine Business-Logic)
    private String createJwtToken(Kunde kunde) {
        return Jwt.issuer("https://pizza4me.de")
                  .upn(kunde.getUsername())  // User Principal Name
                  .groups(Set.of(kunde.getRole()))  // Rollen für @RolesAllowed
                  .claim("kundeId", kunde.getId())
                  .claim("username", kunde.getUsername())
                  .expiresAt(Instant.now().plus(Duration.ofHours(24)))
                  .sign();
    }

}
