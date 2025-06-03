package de.hsos.swa.Pizza.Boundary.Ressource;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.Location;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.common.util.RestMediaType;


import de.hsos.swa.Pizza.Boundary.DTO.PizzaDTO;
import de.hsos.swa.Pizza.Controller.PizzaController;
import de.hsos.swa.Pizza.Entity.Pizza;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.MediaType;

import java.net.URI;


@Path("/pizzen/{id}")
@Produces({MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
@Transactional
public class PizzaRessource {

    @Inject
    PizzaController pizzaController;

    @CheckedTemplate
    public static class Templates {
        @Location("PizzaRessource/detail")
        public static native TemplateInstance detail(Pizza pizza);
        @Location("PizzaRessource/edit")
        public static native TemplateInstance edit(Pizza pizza);
        @Location("PizzaRessource/create")
        public static native TemplateInstance create();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getPizzaHTML(@PathParam("id") Long id) {
        Pizza pizza = pizzaController.getPizza(id);
        if (pizza == null) {
            throw new WebApplicationException(404);
        }
        return PizzaRessource.Templates.detail(pizza);
    }

    @GET
    @Path("/bearbeiten")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance bearbeitenForm(@PathParam("id") Long id) {
        Pizza pizza = pizzaController.getPizza(id);
        if (pizza==null) {
            throw new WebApplicationException(404);
        }
        return PizzaRessource.Templates.edit(pizza);
    }

    @GET
    @Path("/neu")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance createPizzaForm() {
        return PizzaRessource.Templates.create();
    }


    @POST
    @RolesAllowed("ADMIN")
    //@Path("/bearbeiten")
    @Retry(maxRetries = 3, delay = 1000)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.5)
    @Timeout(value = 5000)
    public Response bearbeitenSave(@PathParam("id") long id, @Valid PizzaDTO pizzaDTO) {
        
        if (pizzaDTO == null) {
            throw new BadRequestException("Request body cannot be null");
        }

        Pizza existingPizza = pizzaController.getPizza(id);
        if (existingPizza == null) {
            throw new NotFoundException("Pizza with id " + id + " not found");
        }

        if (pizzaController.existsByName(pizzaDTO.name) && !existingPizza.getName().equals(pizzaDTO.name)) {
            throw new BadRequestException("Pizza with this name already exists");
        }

        Pizza updatedPizza = pizzaController.updatePizza(id, PizzaDTO.fromDTO(pizzaDTO));
        PizzaDTO updatedPizzaDTO = PizzaDTO.toDTO(updatedPizza);

        return Response.seeOther(URI.create("/pizzen")).build();
    }

    @DELETE
    @Retry(maxRetries = 3, delay = 1000)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.5)
    @Timeout(value = 5000)
    public RestResponse<Void> deletePizza(@PathParam("id") long id) {
        
        Pizza existingPizza = pizzaController.getPizza(id);
        if (existingPizza == null) {
            throw new NotFoundException("Pizza with id " + id + " not found");
        }

        pizzaController.deletePizza(id);
        
        return RestResponse.noContent();
    }

}
