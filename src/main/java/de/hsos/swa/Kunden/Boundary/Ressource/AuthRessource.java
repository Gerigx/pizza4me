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
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.Location;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.jwt.build.Jwt;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;

@Path("/auth")
@Produces({MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON})
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class AuthRessource {

    
    @Inject
    KundeController kundeController;

    @CheckedTemplate
    public static class Templates {
        @Location("AuthRessource/login")
        public static native TemplateInstance login();

        @Location("AuthRessource/register")  // ← NEU
        public static native TemplateInstance register();
    }

    @GET
    @Path("/login")
    @PermitAll
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance loginPage() {
        return Templates.login();
    }

    @GET
    @Path("/register")
    @PermitAll
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance registerPage() {
        return Templates.register();
    }



    @POST
    @Path("/login") 
    @PermitAll
    public RestResponse<AuthResponseDTO> login(@Valid LoginDTO loginDTO) {

        System.out.println("Test1");
        
        if (loginDTO == null) {
            throw new BadRequestException("Request body cannot be null");
        }

        System.out.println("Test2");

        try {
            Kunde kunde = kundeController.findByUsername(loginDTO.username);
            if (kunde == null) {
                throw new BadRequestException("Ungültige Anmeldedaten");
            }

            System.out.println("Test3");

            if (!kunde.verifyPassword(loginDTO.password)) {
                throw new BadRequestException("Ungültige Anmeldedaten");
            }

            System.out.println("Test4");

            String token = createJwtToken(kunde);
            long expiresAt = Instant.now().plus(Duration.ofHours(24)).getEpochSecond();

            AuthResponseDTO authResponse = new AuthResponseDTO(
                token,
                KundeDTO.toDTO(kunde), 
                kunde.getRole(),
                expiresAt
            );

            System.out.println("Test5");

            // FRAGE SOLL das projekt in production laufen?
            NewCookie jwtCookie = new NewCookie.Builder("jwt")
                .value(token)
                .path("/")
                .maxAge(24 * 60 * 60)  // 24 Stunden
                .httpOnly(true)
                .secure(false)  // Development - Production: true setzen!
                .sameSite(NewCookie.SameSite.STRICT)
                .build();

            return RestResponse.ResponseBuilder.ok(authResponse)
                .cookie(jwtCookie)
                .build();
            
        } catch (BadRequestException e) {
            System.out.println("Test6");
            throw e;
        } catch (Exception e) {
            System.out.println("Test7 - Exception: " + e.getClass().getName());
            System.out.println("Test7 - Message: " + e.getMessage());
            e.printStackTrace();
            throw new BadRequestException("Login fehlgeschlagen");
        }
    }

    @POST
    @Path("/register")
    @PermitAll
    @Retry(maxRetries = 3, delay = 1000)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.5)
    @Timeout(value = 5000)
    public RestResponse<Object> register(@Valid RegistrationDTO registrationDTO) {
        
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

            NewCookie jwtCookie = new NewCookie.Builder("jwt")
                .value(token)
                .path("/")
                .maxAge(24 * 60 * 60)
                .httpOnly(true)
                .secure(false)  // Development
                .sameSite(NewCookie.SameSite.STRICT)
                .build();
            
            return RestResponse.ResponseBuilder
                .created(URI.create("/kunde/" + gespeicherterKunde.getId()))
                .entity(authResponse)
                .cookie(jwtCookie)
                .build();
            
        } catch (Exception e) {
            throw new BadRequestException("Registrierung fehlgeschlagen: " + e.getMessage());
        }
    }


    @POST
    @Path("/logout")
    @PermitAll
    public RestResponse<Object> logout() {
        NewCookie deleteCookie = new NewCookie.Builder("jwt")
            .value("")
            .path("/")
            .maxAge(0) 
            .httpOnly(true)
            .build();

        return RestResponse.ResponseBuilder.ok()
            .cookie(deleteCookie)
            .build();
    }


    // https://quarkus.io/guides/security-jwt-build
    private String createJwtToken(Kunde kunde) {
        return Jwt.issuer("pizza4me")
                  .upn(kunde.getUsername())
                  .groups(Set.of(kunde.getRole()))
                  .claim("kundeId", kunde.getId())
                  .claim("username", kunde.getUsername())
                  .expiresAt(Instant.now().plus(Duration.ofHours(24)))
                  .sign();
    }
}
