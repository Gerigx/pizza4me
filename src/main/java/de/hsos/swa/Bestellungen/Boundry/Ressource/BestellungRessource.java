package de.hsos.swa.Bestellungen.Boundry.Ressource;

import de.hsos.swa.Bestellungen.Boundry.DTO.BestellungDTO;
import de.hsos.swa.Bestellungen.Entity.Bestellung;
import de.hsos.swa.Bestellungen.Entity.BestellungKatalog;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.Location;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.common.util.RestMediaType;

import java.net.URI;

@Path("/bestellungen/{id}")
@Produces({MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
@Transactional
public class BestellungRessource {

    @Inject
    BestellungKatalog bestellungKatalog;

    @CheckedTemplate
    public static class Templates {
        @Location("BestellungRessource/detail")
        public static native TemplateInstance detail(long id);
        @Location("BestellungRessource/edit")
        public static native TemplateInstance edit(long id);
        @Location("BestellungRessource/create")
        public static native TemplateInstance create();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getBestellungHTML(@PathParam("id") Long id) {
        Bestellung bestellung = bestellungKatalog.getBestellung(id);
        if (bestellung == null) {
            throw new WebApplicationException(404);
        }
        return BestellungRessource.Templates.detail(id);
    }

    @GET
    @Path("/bearbeiten")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance bearbeitenForm(@PathParam("id") Long id) {
        Bestellung bestellung = bestellungKatalog.getBestellung(id);
        if (bestellung == null) {
            throw new WebApplicationException(404);
        }
        return BestellungRessource.Templates.edit(id);
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

        Bestellung updatedBestellung = bestellungKatalog.updateBestellung(id, BestellungDTO.fromDTO(bestellungDTO));
        BestellungDTO updatedBestellungDTO = BestellungDTO.toDTO(updatedBestellung);

        return Response.seeOther(URI.create("/bestellungen")).build();
    }

    @DELETE
    @Retry(maxRetries = 3, delay = 1000)
    @Timeout(value = 2000)
    public RestResponse deleteBestellung(@PathParam("id") Long id) {

        Bestellung existingBestellung = bestellungKatalog.getBestellung(id);
        if (!bestellungKatalog.isExisting(id)) {
            throw new NotFoundException("Bestellung not found for ID: " + id);
        }

        bestellungKatalog.deleteBestellung(id);
        return RestResponse.noContent();
    }

}
