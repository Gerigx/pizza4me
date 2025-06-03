package de.hsos.swa.Kunden.Boundary.Ressource;

import java.util.Set;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestResponse;

import de.hsos.swa.Kunden.Boundary.DTO.AdresseDTO;
import de.hsos.swa.Kunden.Boundary.DTO.KundeDTO;
import de.hsos.swa.Kunden.Controller.KundeController;
import de.hsos.swa.Kunden.Entity.Adresse;
import de.hsos.swa.Kunden.Entity.Kunde;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.Location;

import io.quarkus.qute.TemplateInstance;
import io.quarkus.resteasy.reactive.links.RestLink;
import io.quarkus.security.ForbiddenException;
import jakarta.ws.rs.Produces;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.common.util.RestMediaType;

import jakarta.servlet.http.HttpServletRequest;


@Path("/kunde/{id}")
@Produces({MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON})
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class KundeRessource {

    @Inject
    KundeController kundeController;

    @Inject
    JsonWebToken jwt;

    @Context
    HttpServletRequest request;


    @CheckedTemplate
    public static class Templates {
        @Location("KundeRessource/detail")
        public static native TemplateInstance detail(Kunde kunde);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @RolesAllowed({"USER", "ADMIN"})
    public TemplateInstance getKundeHTML(@PathParam("id") Long id) {
        
        if (!canAccessKunde(id)) {
            throw new WebApplicationException(403);
        }

        Kunde kunde = kundeController.getKunde(id);
        if (kunde == null) {
            throw new WebApplicationException(404);
        }
        return Templates.detail(kunde);
    }

    @GET
    @RestLink(rel = "self")
    @RolesAllowed({"USER", "ADMIN"})
    public RestResponse<KundeDTO> getKunde(@PathParam("id") Long id) {
        
        if (!canAccessKunde(id)) {
            throw new ForbiddenException("Zugriff verweigert");
        }

        Kunde kunde = kundeController.getKunde(id);
        if (kunde == null) {
            throw new NotFoundException("Kunde mit ID " + id + " nicht gefunden");
        }

        return RestResponse.ok(KundeDTO.toDTO(kunde));
    }

    @PUT
    @Retry(maxRetries = 3, delay = 1000)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.5)
    @Timeout(value = 5000)
    @RolesAllowed({"USER", "ADMIN"})
    public RestResponse<KundeDTO> updateKunde(@PathParam("id") Long id, @Valid KundeDTO kundeDTO) {
        
        if (kundeDTO == null) {
            throw new BadRequestException("Request body cannot be null");
        }

        if (!canAccessKunde(id)) {
            throw new ForbiddenException("Zugriff verweigert");
        }

        Kunde existingKunde = kundeController.getKunde(id);
        if (existingKunde == null) {
            throw new NotFoundException("Kunde mit ID " + id + " nicht gefunden");
        }

        KundeDTO.updateEntity(existingKunde, kundeDTO);

        Kunde updatedKunde = kundeController.updateKunde(id, existingKunde);
        KundeDTO updatedKundeDTO = KundeDTO.toDTO(updatedKunde);

        return RestResponse.ok(updatedKundeDTO);
    }

    @DELETE
    @Retry(maxRetries = 3, delay = 1000)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.5)
    @Timeout(value = 5000)
    @RolesAllowed("ADMIN")  
    public RestResponse<Void> deleteKunde(@PathParam("id") Long id) {
        
        Kunde existingKunde = kundeController.getKunde(id);
        if (existingKunde == null) {
            throw new NotFoundException("Kunde mit ID " + id + " nicht gefunden");
        }

        kundeController.deleteKunde(id);
        
        return RestResponse.noContent();
    }

    @GET
    @Path("/adresse")
    @RolesAllowed({"USER", "ADMIN"})
    public RestResponse<AdresseDTO> getKundeAdresse(@PathParam("id") Long id) {
        
        if (!canAccessKunde(id)) {
            throw new ForbiddenException("Zugriff verweigert");
        }

        Adresse adresse = kundeController.getKundeAdresse(id);
        if (adresse == null) {
            throw new NotFoundException("Adresse für Kunde " + id + " nicht gefunden");
        }

        return RestResponse.ok(AdresseDTO.toDTO(adresse));
    }

    @PUT
    @Path("/adresse")
    @Retry(maxRetries = 3, delay = 1000)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.5)
    @Timeout(value = 5000)
    @RolesAllowed({"USER", "ADMIN"})
    public RestResponse<AdresseDTO> updateKundeAdresse(@PathParam("id") Long id, @Valid AdresseDTO adresseDTO) {
        
        if (adresseDTO == null) {
            throw new BadRequestException("Request body cannot be null");
        }

        if (!canAccessKunde(id)) {
            throw new ForbiddenException("Zugriff verweigert");
        }

        Kunde existingKunde = kundeController.getKunde(id);
        if (existingKunde == null) {
            throw new NotFoundException("Kunde mit ID " + id + " nicht gefunden");
        }

        Adresse adresse = AdresseDTO.fromDTO(adresseDTO);
        Kunde updatedKunde = kundeController.updateKundeAdresse(id, adresse);
        
        return RestResponse.ok(AdresseDTO.toDTO(updatedKunde.getAdresse()));
    }

    @DELETE
    @Path("/adresse")
    @Retry(maxRetries = 3, delay = 1000)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.5)
    @Timeout(value = 5000)
    @RolesAllowed({"USER", "ADMIN"})
    public RestResponse<Void> deleteKundeAdresse(@PathParam("id") Long id) {
        
        if (!canAccessKunde(id)) {
            throw new ForbiddenException("Zugriff verweigert");
        }

        Kunde existingKunde = kundeController.getKunde(id);
        if (existingKunde == null) {
            throw new NotFoundException("Kunde mit ID " + id + " nicht gefunden");
        }

        kundeController.deleteKundeAdresse(id);
        
        return RestResponse.noContent();
    }

    // debugs von ai
    private boolean canAccessKunde(Long kundeId) {
        System.out.println("🔍 canAccessKunde() Debug:");
        System.out.println("  - Requested kundeId: " + kundeId);
        
        boolean admin = isAdmin();
        System.out.println("  - isAdmin(): " + admin);
        
        if (admin) {
            System.out.println("  - ✅ Admin access granted");
            return true;
        }
        
        Long currentKundeId = getCurrentKundeId();
        System.out.println("  - getCurrentKundeId(): " + currentKundeId);
        System.out.println("  - kundeId.equals(): " + (currentKundeId != null && currentKundeId.equals(kundeId)));
        
        return currentKundeId != null && currentKundeId.equals(kundeId);
    }

    private Long getCurrentKundeId() {
        try {
            Object kundeIdClaim = jwt.getClaim("kundeId");
            System.out.println("  - JWT kundeId claim type: " + kundeIdClaim.getClass().getName());
            System.out.println("  - JWT kundeId claim value: " + kundeIdClaim);
            
            if (kundeIdClaim != null) {
                String valueStr = kundeIdClaim.toString();
                System.out.println("  - Converting string: " + valueStr);
                return Long.parseLong(valueStr);
            }
            
            System.out.println("  - kundeId claim is null");
            return null;
        } catch (Exception e) {
            System.out.println("  - JWT ERROR: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private boolean isAdmin() {
        try {
            Set<String> groups = jwt.getGroups();
            System.out.println("  - JWT groups: " + groups);
            boolean admin = groups.contains("ADMIN");
            System.out.println("  - contains ADMIN: " + admin);
            return admin;
        } catch (Exception e) {
            System.out.println("  - Groups ERROR: " + e.getMessage());
            return false;
        }
    }

}
