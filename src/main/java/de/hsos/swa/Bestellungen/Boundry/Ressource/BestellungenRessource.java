package de.hsos.swa.Bestellungen.Boundry.Ressource;

import de.hsos.swa.Bestellungen.Boundry.DTO.BestellungDTO;
import de.hsos.swa.Bestellungen.Entity.Bestellung;
import de.hsos.swa.Bestellungen.Entity.BestellungKatalog;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.Location;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.resteasy.reactive.links.InjectRestLinks;
import io.quarkus.resteasy.reactive.links.RestLink;
import io.quarkus.resteasy.reactive.links.RestLinkType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.common.util.RestMediaType;

import java.util.List;

@Path("/bestellungen")
@Produces({MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
@Transactional
public class BestellungenRessource {
    @Inject
    BestellungKatalog bestellungKatalog;

    @CheckedTemplate
    public static class Templates {
        @Location("BestellungenRessource/list")
        public static native TemplateInstance list();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getBestellungenHTML(@QueryParam("page") @DefaultValue("0") int page,
                                                @QueryParam("size") @DefaultValue("10") int size) {
        List<Bestellung> bestellungen = bestellungKatalog.getAllBestellungen(page, size);
        bestellungen.stream()
                .sorted()
                .map(BestellungDTO::toDTO)
                .toList();
        return BestellungenRessource.Templates.list();
    }

}
