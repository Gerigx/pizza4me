package de.hsos.swa.Pizza.Boundary.Ressource;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.common.util.RestMediaType;


import de.hsos.swa.Pizza.Boundary.DTO.PizzaDTO;
import de.hsos.swa.Pizza.Controller.PizzaController;
import de.hsos.swa.Pizza.Entity.Pizza;
import io.quarkus.resteasy.reactive.links.RestLink;
import io.quarkus.resteasy.reactive.links.RestLinkType;
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
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/pizza/{id}")
@Produces({MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON})
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class PizzaRessource {

    @Inject
    PizzaController pizzaController;

    @GET
    @RestLink(rel = "self") 
    public RestResponse<PizzaDTO> getPizza(@PathParam("id") long id) {
        Pizza pizza = pizzaController.getPizza(id);
        return RestResponse.ok(PizzaDTO.toDTO(pizza));
    }

    @PUT
    @Retry(maxRetries = 3, delay = 1000)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.5)
    @Timeout(value = 5000)
    public RestResponse<PizzaDTO> updatePizza(@PathParam("id") long id, @Valid PizzaDTO pizzaDTO) {
        
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

        return RestResponse.ok(updatedPizzaDTO);
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
