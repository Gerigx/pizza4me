package de.hsos.swa.Kunden.Boundary.Ressource;

import java.net.URI;
import java.util.List;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.common.util.RestMediaType;

import de.hsos.swa.Kunden.Boundary.DTO.KundeDTO;
import de.hsos.swa.Kunden.Controller.KundeController;
import de.hsos.swa.Kunden.Entity.Kunde;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.Location;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.resteasy.reactive.links.InjectRestLinks;
import io.quarkus.resteasy.reactive.links.RestLink;
import io.quarkus.resteasy.reactive.links.RestLinkType;

import jakarta.annotation.security.RolesAllowed;
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
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/kunden")
@Produces({MediaType.APPLICATION_JSON, RestMediaType.APPLICATION_HAL_JSON})
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class KundenRessource {
    
    @Inject
    KundeController kundeController;

    @Inject
    JsonWebToken jwt;

    @CheckedTemplate
    public static class Templates {
        @Location("KundenRessource/list")
        public static native TemplateInstance list(List<Kunde> kunden);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @RolesAllowed("ADMIN")  
    public TemplateInstance getAlleKundenHTML(
            @QueryParam("page") @DefaultValue("0") @Min(0) int page,
            @QueryParam("size") @DefaultValue("20") @Min(1) @Max(100) int size
    ) {
        List<Kunde> kunden = kundeController.getAllKunden(page, size);
        return Templates.list(kunden);
    }

    @GET
    @RestLink(rel = "list")
    @InjectRestLinks(RestLinkType.TYPE)
    @RolesAllowed("ADMIN") 
    public RestResponse<List<KundeDTO>> getAllKunden(
            @QueryParam("page") @DefaultValue("0") @Min(0) int page,
            @QueryParam("size") @DefaultValue("20") @Min(1) @Max(100) int size
    ) {

        List<Kunde> kunden = kundeController.getAllKunden(page, size);

        if (kunden.size() <= 0) {
            throw new NotFoundException("Keine Kunden gefunden");
        }

        List<KundeDTO> kundenDTOs = kunden.stream()
            .map(KundeDTO::toDTO)  
            .toList();

        return RestResponse.ok(kundenDTOs);
    }

    @POST
    @Retry(maxRetries = 3, delay = 1000)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.5)
    @Timeout(value = 5000)
    @RolesAllowed("ADMIN") 
    public RestResponse<KundeDTO> createKunde(@Valid KundeDTO kundeDTO) {

        if (kundeDTO == null) {
            throw new BadRequestException("Request body cannot be null");
        }

        Kunde kunde = KundeDTO.fromDTO(kundeDTO);
        kunde.setUsername("admin_created_" + System.currentTimeMillis()); 
        kunde.setPassword("temp_password"); 
        kunde.setRole("USER"); 

        if (kundeController.existsByUsername(kunde.getUsername())) {
            throw new BadRequestException("Kunde mit diesem Username existiert bereits");
        }

        Kunde createdKunde = kundeController.addKunde(kunde);
        KundeDTO createdKundeDTO = KundeDTO.toDTO(createdKunde);

        return RestResponse.created(URI.create("/kunde/" + createdKundeDTO.id));
    }




}