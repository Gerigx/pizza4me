package de.hsos.swa.Bestellungen.Boundry.Ressource;

import de.hsos.swa.Bestellungen.Boundry.DTO.BestellungDTO;
import de.hsos.swa.Bestellungen.Boundry.DTO.CartItemDTO;
import de.hsos.swa.Bestellungen.Boundry.DTO.CartRequestDTO;
import de.hsos.swa.Bestellungen.Entity.Bestellung;
import de.hsos.swa.Bestellungen.Entity.BestellungKatalog;
import de.hsos.swa.Bestellungen.Entity.Zustand;
import de.hsos.swa.Pizza.Controller.PizzaController;
import de.hsos.swa.Pizza.Entity.Pizza;
import de.hsos.swa.shared.Events.BestellungCreatedEvent;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.Location;
import io.quarkus.qute.TemplateInstance;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.common.util.RestMediaType;

import java.math.BigDecimal;
import java.net.URI;
import java.sql.Timestamp;
import java.util.ArrayList;
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

    @Inject
    JsonWebToken jwt;

    @Inject
    Event<BestellungCreatedEvent> bestellungCreatedEvent;

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

    @POST
    @RolesAllowed({"USER", "ADMIN"})
    @Retry(maxRetries = 3, delay = 1000)
    @Timeout(value = 5000)
    public RestResponse<BestellungDTO> createBestellung(@Valid CartRequestDTO cartRequest) {
        
        if (cartRequest == null || cartRequest.items == null || cartRequest.items.isEmpty()) {
            throw new BadRequestException("Warenkorb ist leer");
        }

        // Kunde ID aus JWT Token holen
        Long kundeId = getCurrentKundeId();
        if (kundeId == null) {
            throw new BadRequestException("Kunde ID nicht im Token gefunden");
        }

        // Pizzen laden und validieren
        List<Pizza> bestelltePizzen = new ArrayList<>();
        BigDecimal gesamtPreis = BigDecimal.ZERO;

        for (CartItemDTO item : cartRequest.items) {
            Pizza pizza = pizzaController.getPizza(item.pizzaId);
            if (pizza == null) {
                throw new BadRequestException("Pizza mit ID " + item.pizzaId + " nicht gefunden");
            }

            // Mehrere Kopien der Pizza für die Anzahl hinzufügen
            for (int i = 0; i < item.quantity; i++) {
                bestelltePizzen.add(pizza);
            }

            // Preis berechnen
            BigDecimal itemPreis = pizza.getPrice().multiply(new BigDecimal(item.quantity));
            gesamtPreis = gesamtPreis.add(itemPreis);
        }

        // BestellungDTO erstellen (nutzt dein existierendes DTO!)
        BestellungDTO bestellungDTO = new BestellungDTO(
            kundeId,
            Zustand.Neu.name(),
            cartRequest.hinweis != null ? cartRequest.hinweis : "",
            new Timestamp(System.currentTimeMillis()).toString(),
            gesamtPreis.toString(),
            bestelltePizzen
        );

        // Zu Bestellung Entity konvertieren und speichern
        Bestellung bestellung = BestellungDTO.fromDTO(bestellungDTO);
        Bestellung gespeicherteBestellung = bestellungKatalog.createBestellung(bestellung);
        
        // *** EVENT FEUERN (ASYNC) ***
        bestellungCreatedEvent.fireAsync(new BestellungCreatedEvent(
            gespeicherteBestellung.getId(), 
            kundeId
        ));
        
        System.out.println("🚀 BestellungCreatedEvent gefeuert: ID=" + gespeicherteBestellung.getId() + 
                          ", KundeId=" + kundeId);
        
        // Zurück zu DTO konvertieren für Response
        BestellungDTO responseDTO = BestellungDTO.toDTO(gespeicherteBestellung);

        return RestResponse.created(URI.create("/bestellung/" + gespeicherteBestellung.getId()));
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

}
