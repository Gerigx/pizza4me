package de.hsos.swa.Pizza.Boundary.Ressource;

import java.net.URI;
import java.util.List;
import java.util.stream.Stream;

// resillienz
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;

// hal
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.common.util.RestMediaType;

import de.hsos.swa.Pizza.Boundary.DTO.PizzaDTO;
import de.hsos.swa.Pizza.Controller.PizzaController;
import de.hsos.swa.Pizza.Entity.Pizza;

import io.quarkus.resteasy.reactive.links.InjectRestLinks;
import io.quarkus.resteasy.reactive.links.RestLink;
import io.quarkus.resteasy.reactive.links.RestLinkType;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/pizzen")
@Produces({MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON})
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class PizzenRessource {

    @Inject
    PizzaController pizzaController;

    @GET
    @RestLink(rel = "list")
    @InjectRestLinks(RestLinkType.TYPE)
    public RestResponse<List<PizzaDTO>> getAllPizzen(
            @QueryParam("page") @DefaultValue("0") @Min(0) int page,
            @QueryParam("size") @DefaultValue("20") @Min(1) @Max(100) int size
    ){

        List<Pizza> pizzen = pizzaController.getAllPizzas(page, size);

        if (pizzen.size() <= 0) {
            throw new NotFoundException();            
        }

        List<PizzaDTO> pizzaDTOs = pizzen.stream()
            .map(PizzaDTO::toDTO)
            .toList();

        return RestResponse.ok(pizzaDTOs);        
    }


    @POST
    @Retry(maxRetries = 3, delay = 1000)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.5)
    @Timeout(value = 5000)
    public RestResponse<PizzaDTO> createPizza(@Valid PizzaDTO pizzaDTO){

        if (pizzaDTO == null) {
            throw new BadRequestException("Request body cannot be null");
        }
        
        if (pizzaController.existsByName(pizzaDTO.name)) {
            throw new BadRequestException("Pizza with this name already exists");
        }

        Pizza pizza = pizzaController.createPizza(pizzaDTO.fromDTO(pizzaDTO));
        PizzaDTO createdPizza = PizzaDTO.toDTO(pizza);

        return RestResponse.created(URI.create("/pizzen/" + createdPizza.id));        
    }



}
