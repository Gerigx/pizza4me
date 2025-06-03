package de.hsos.swa.Bestellungen.Boundry.Ressource;

import de.hsos.swa.Bestellungen.Boundry.DTO.BestellungDTO;
import de.hsos.swa.Bestellungen.Entity.Bestellung;
import de.hsos.swa.Bestellungen.Entity.BestellungKatalog;
import de.hsos.swa.Pizza.Controller.PizzaController;
import de.hsos.swa.Pizza.Entity.Pizza;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.Location;
import io.quarkus.qute.TemplateInstance;

import jakarta.annotation.security.RolesAllowed;
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

    @Inject
    PizzaController pizzaController;  

    @CheckedTemplate
    public static class Templates {
        @Location("BestellungenRessource/list")
        public static native TemplateInstance list(List<BestellungDTO> bestellungen); // Parameter hinzufügen

        @Location("BestellungenRessource/neu") 
        public static native TemplateInstance neu(List<Pizza> verfuegbarePizzen); // ← Parameter hinzufügen
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getBestellungenHTML(@QueryParam("page") @DefaultValue("0") int page,
                                                @QueryParam("size") @DefaultValue("10") int size) {
        List<Bestellung> bestellungen = bestellungKatalog.getAllBestellungen(page, size);
        
        // Daten verarbeiten UND speichern
        List<BestellungDTO> bestellungDTOs = bestellungen.stream()
                .sorted()
                .map(BestellungDTO::toDTO)
                .toList();
        
        // Parameter an Template übergeben
        return BestellungenRessource.Templates.list(bestellungDTOs); // oder bestellungen
    }

    @GET
    @Path("/neu")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance neueBestellungHTML() {
        // Alle Pizzen für die Auswahl laden
        List<Pizza> verfuegbarePizzen = pizzaController.getAllPizzas(0, 100); // Alle Pizzen laden
        
        return BestellungenRessource.Templates.neu(verfuegbarePizzen);
    }

    @GET
    @Path("/neu-test")
    @Produces(MediaType.TEXT_PLAIN)  // Wichtig hinzufügen!
    public String testEndpoint() {
        return "Test works!";
    }

}
