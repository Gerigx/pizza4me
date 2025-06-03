package de.hsos.swa.Bestellungen.Boundry.Ressource;

import de.hsos.swa.Bestellungen.Boundry.DTO.BestellungDTO;
import de.hsos.swa.Bestellungen.Boundry.DTO.CartItemDTO;
import de.hsos.swa.Bestellungen.Boundry.DTO.CartRequestDTO;
import de.hsos.swa.Bestellungen.Entity.Bestellung;
import de.hsos.swa.Bestellungen.Entity.BestellungItem;
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
        public static native TemplateInstance list(List<BestellungDTO> bestellungen);

        @Location("BestellungenRessource/neu") 
        public static native TemplateInstance neu(List<Pizza> verfuegbarePizzen);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getBestellungenHTML(@QueryParam("page") @DefaultValue("0") int page,
                                                @QueryParam("size") @DefaultValue("10") int size) {
        List<Bestellung> bestellungen = bestellungKatalog.getAllBestellungen(page, size);
        
        List<BestellungDTO> bestellungDTOs = bestellungen.stream()
                .sorted()
                .map(BestellungDTO::toDTO)
                .toList();
        
        return BestellungenRessource.Templates.list(bestellungDTOs);
    }

    @GET
    @Path("/neu")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance neueBestellungHTML() {
        List<Pizza> verfuegbarePizzen = pizzaController.getAllPizzas(0, 100);
        return BestellungenRessource.Templates.neu(verfuegbarePizzen);
    }

    // debugs per ai
    @POST
    @RolesAllowed({"USER", "ADMIN"})
    @Retry(maxRetries = 3, delay = 1000)
    @Timeout(value = 5000)
    public RestResponse<BestellungDTO> createBestellung(@Valid CartRequestDTO cartRequest) {
        
        if (cartRequest == null || cartRequest.items == null || cartRequest.items.isEmpty()) {
            throw new BadRequestException("Warenkorb ist leer");
        }

        Long kundeId = getCurrentKundeId();
        if (kundeId == null) {
            throw new BadRequestException("Kunde ID nicht im Token gefunden");
        }

        List<BestellungItem> bestellungItems = new ArrayList<>();
        BigDecimal gesamtPreis = BigDecimal.ZERO;

        Bestellung bestellung = new Bestellung();
        bestellung.setKundeId(kundeId);
        bestellung.setZustand(Zustand.Neu);
        bestellung.setHinweis(cartRequest.hinweis != null ? cartRequest.hinweis : "");
        bestellung.setBestellDatum(new Timestamp(System.currentTimeMillis()));

        for (CartItemDTO cartItem : cartRequest.items) {
            Pizza pizza = pizzaController.getPizza(cartItem.pizzaId);
            if (pizza == null) {
                throw new BadRequestException("Pizza mit ID " + cartItem.pizzaId + " nicht gefunden");
            }

            if (cartItem.quantity <= 0) {
                throw new BadRequestException("Quantity muss größer als 0 sein");
            }

            BestellungItem bestellungItem = BestellungItem.create(bestellung, pizza, cartItem.quantity);
            bestellungItems.add(bestellungItem);

            gesamtPreis = gesamtPreis.add(bestellungItem.getGesamtpreis());

            System.out.println("🍕 BestellungItem erstellt: " + pizza.getName() + 
                              " x" + cartItem.quantity + 
                              " = " + bestellungItem.getGesamtpreis() + "€");
        }

        for (BestellungItem item : bestellungItems) {
            bestellung.addBestellungItem(item);
        }


        bestellung.setGesamtPreis(gesamtPreis);

        System.out.println("💰 Bestellung Gesamtpreis: " + gesamtPreis + "€");

        Bestellung gespeicherteBestellung = bestellungKatalog.createBestellung(bestellung);
        
        System.out.println("✅ Bestellung gespeichert mit ID: " + gespeicherteBestellung.getId());

        bestellungCreatedEvent.fireAsync(new BestellungCreatedEvent(
            gespeicherteBestellung.getId(), 
            kundeId
        ));
        
        System.out.println("🚀 BestellungCreatedEvent gefeuert: ID=" + gespeicherteBestellung.getId() + 
                          ", KundeId=" + kundeId);
        
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