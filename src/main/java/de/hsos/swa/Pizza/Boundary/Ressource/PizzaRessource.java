package de.hsos.swa.Pizza.Boundary.Ressource;

import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.common.util.RestMediaType;


import de.hsos.swa.Pizza.Boundary.DTO.PizzaDTO;
import de.hsos.swa.Pizza.Controller.PizzaController;
import de.hsos.swa.Pizza.Entity.Pizza;
import io.quarkus.resteasy.reactive.links.RestLink;
import io.quarkus.resteasy.reactive.links.RestLinkType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
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

}
