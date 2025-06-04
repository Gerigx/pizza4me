package de.hsos.swa.Bestellungen.Boundry.Ressource;

import de.hsos.swa.Bestellungen.Boundry.DTO.BestellungDTO;
import de.hsos.swa.Bestellungen.Entity.Bestellung;
import de.hsos.swa.Bestellungen.Entity.BestellungKatalog;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.Location;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.common.util.RestMediaType;

import java.net.URI;
import java.util.Set;

@Path("/bestellung/{id}")
@Produces({MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
@Transactional
public class BestellungRessource {

    @Inject
    BestellungKatalog bestellungKatalog;

    @Inject
    JsonWebToken jwt;

    @CheckedTemplate
    public static class Templates {
        @Location("BestellungRessource/detail")
        public static native TemplateInstance detail(Bestellung bestellung);
        @Location("BestellungRessource/edit")
        public static native TemplateInstance edit(Bestellung bestellung);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getBestellungHTML(@PathParam("id") Long id) {
        Bestellung bestellung = bestellungKatalog.getBestellung(id);
        if (bestellung == null) {
            throw new WebApplicationException(404);
        }

        if (!canAccessBestellung(bestellung)) {
            throw new WebApplicationException("Zugriff verweigert", 403);
        }
        return BestellungRessource.Templates.detail(bestellung);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON})
    @RolesAllowed({"USER", "ADMIN"})
    public RestResponse<BestellungDTO> getBestellungJSON(@PathParam("id") Long id) {
        Bestellung bestellung = bestellungKatalog.getBestellung(id);
        if (bestellung == null) {
            throw new NotFoundException("Bestellung mit ID " + id + " nicht gefunden");
        }

        if (!canAccessBestellung(bestellung)) {
            throw new BadRequestException("Zugriff verweigert");
        }

        BestellungDTO bestellungDTO = BestellungDTO.toDTO(bestellung);
        return RestResponse.ok(bestellungDTO);
    }

    @GET
    @Path("/bearbeiten")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance bearbeitenForm(@PathParam("id") Long id) {
        Bestellung bestellung = bestellungKatalog.getBestellung(id);
        if (bestellung == null) {
            throw new WebApplicationException(404);
        }
        return BestellungRessource.Templates.edit(bestellung);
    }

    @POST
    @Retry(maxRetries = 3, delay = 1000)
    @Timeout(value = 2000)
    public Response updateBestellung(@PathParam("id") Long id, @Valid BestellungDTO bestellungDTO) {
        if (bestellungDTO == null || !bestellungKatalog.isExisting(id)) {
            throw new BadRequestException("Invalid Bestellung data or ID does not exist.");
        }
        
        Bestellung existingBestellung = bestellungKatalog.getBestellung(id);
        if (existingBestellung == null) {
            throw new NotFoundException("Bestellung not found for ID: " + id);
        }

        existingBestellung.setZustand(de.hsos.swa.Bestellungen.Entity.Zustand.valueOf(bestellungDTO.getZustand()));
        existingBestellung.setHinweis(bestellungDTO.getHinweis());
        
        Bestellung updatedBestellung = bestellungKatalog.updateBestellung(id, existingBestellung);

        return Response.seeOther(URI.create("/bestellungen")).build();
    }

    @DELETE
    @Retry(maxRetries = 3, delay = 1000)
    @Timeout(value = 2000)
    public RestResponse deleteBestellung(@PathParam("id") Long id) {

        if (!bestellungKatalog.isExisting(id)) {
            throw new NotFoundException("Bestellung not found for ID: " + id);
        }

        bestellungKatalog.deleteBestellung(id);
        return RestResponse.noContent();
    }


    // sec
    private boolean canAccessBestellung(Bestellung bestellung) {
        if (isAdmin()) {
            return true;
        }
        
        Long currentKundeId = getCurrentKundeId();
        return currentKundeId != null && currentKundeId.equals(bestellung.getKundeId());
    }

    private Long getCurrentKundeId() {
        try {
            Object kundeIdClaim = jwt.getClaim("kundeId");
            if (kundeIdClaim != null) {
                return Long.parseLong(kundeIdClaim.toString());
            }
            return null;
        } catch (Exception e) {
            System.out.println("Fehler beim Lesen der kundeId aus JWT: " + e.getMessage());
            return null;
        }
    }

    private boolean isAdmin() {
        try {
            Set<String> groups = jwt.getGroups();
            return groups.contains("ADMIN");
        } catch (Exception e) {
            return false;
        }
    }
}