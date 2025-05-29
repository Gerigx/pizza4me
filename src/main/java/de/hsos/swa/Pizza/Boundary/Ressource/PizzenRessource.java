package de.hsos.swa.Pizza.Boundary.Ressource;

import java.util.List;
import java.util.stream.Stream;

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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
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



}
