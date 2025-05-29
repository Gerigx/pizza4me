package de.hsos.swa;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import io.quarkus.test.TestTransaction;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;

import de.hsos.swa.Pizza.Controller.PizzaController;
import de.hsos.swa.Pizza.Entity.Pizza;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

@QuarkusTest
@DisplayName("PizzenRessource Tests")
class PizzenRessourceTest {

    @BeforeEach
    void setUp() {
        // Reset Circuit Breaker state before each test
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Test
    @DisplayName("GET /pizzen - Erfolgreiche Abfrage aller Pizzen")
    void getAllPizzen_Success() {
        given()
            .when()
                .get("/pizzen")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", greaterThan(0))
                .body("[0].id", notNullValue())
                .body("[0].name", notNullValue())
                .body("[0].beschreibung", notNullValue())
                .body("[0].price", notNullValue())
                .body("[0].sellCounter", notNullValue());
    }

    @Test
    @DisplayName("GET /pizzen - Mit Pagination Parametern")
    void getAllPizzen_WithPagination() {
        given()
            .queryParam("page", 0)
            .queryParam("size", 5)
            .when()
                .get("/pizzen")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", lessThanOrEqualTo(5));
    }

    @Test
    @DisplayName("GET /pizzen - Ungültige Pagination Parameter")
    void getAllPizzen_InvalidPaginationParams() {
        given()
            .queryParam("page", -1)
            .when()
                .get("/pizzen")
            .then()
                .statusCode(400);

        given()
            .queryParam("size", 0)
            .when()
                .get("/pizzen")
            .then()
                .statusCode(400);

        given()
            .queryParam("size", 101)
            .when()
                .get("/pizzen")
            .then()
                .statusCode(400);
    }

    @Test
    @TestTransaction
    @DisplayName("POST /pizzen - Erfolgreiche Pizza-Erstellung")
    void createPizza_Success() {
        String uniqueName = "Test Pizza " + System.currentTimeMillis();
        String pizzaJson = String.format("""
            {
                "name": "%s",
                "beschreibung": "Eine köstliche Test-Pizza",
                "price": 12.50
            }
            """, uniqueName);

        given()
            .contentType(ContentType.JSON)
            .body(pizzaJson)
            .when()
                .post("/pizzen")
            .then()
                .statusCode(201)
                .header("location", containsString("/pizzen/"));
    }

    @Test
    @DisplayName("POST /pizzen - Null Request Body")
    void createPizza_NullBody_BadRequest() {
        given()
            .contentType(ContentType.JSON)
            .when()
                .post("/pizzen")
            .then()
                .statusCode(400);
    }

    @Test
    @TestTransaction
    @DisplayName("POST /pizzen - Pizza mit existierendem Namen")
    void createPizza_ExistingName_BadRequest() {
        String pizzaJson = """
            {
                "name": "Margherita",
                "beschreibung": "Noch eine Margherita",
                "price": 8.50
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(pizzaJson)
            .when()
                .post("/pizzen")
            .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("POST /pizzen - Validierungsfehler bei ungültigen Daten")
    void createPizza_ValidationErrors() {
        // Leerer Name
        String invalidNameJson = """
            {
                "name": "",
                "beschreibung": "Beschreibung",
                "price": 10.00
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(invalidNameJson)
            .when()
                .post("/pizzen")
            .then()
                .statusCode(400);

        // Name zu kurz
        String shortNameJson = """
            {
                "name": "A",
                "beschreibung": "Beschreibung",
                "price": 10.00
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(shortNameJson)
            .when()
                .post("/pizzen")
            .then()
                .statusCode(400);

        // Preis zu niedrig
        String lowPriceJson = """
            {
                "name": "Test Pizza Low",
                "beschreibung": "Beschreibung",
                "price": 0.00
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(lowPriceJson)
            .when()
                .post("/pizzen")
            .then()
                .statusCode(400);

        // Preis zu hoch
        String highPriceJson = """
            {
                "name": "Test Pizza High",
                "beschreibung": "Beschreibung",
                "price": 1000.00
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(highPriceJson)
            .when()
                .post("/pizzen")
            .then()
                .statusCode(400);

        // Beschreibung fehlt
        String missingDescriptionJson = """
            {
                "name": "Test Pizza",
                "price": 10.00
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(missingDescriptionJson)
            .when()
                .post("/pizzen")
            .then()
                .statusCode(400);

        // Beschreibung zu lang
        String longDescriptionJson = String.format("""
            {
                "name": "Test Pizza Long Desc",
                "beschreibung": "%s",
                "price": 10.00
            }
            """, "A".repeat(201));

        given()
            .contentType(ContentType.JSON)
            .body(longDescriptionJson)
            .when()
                .post("/pizzen")
            .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("POST /pizzen - Ungültiges JSON Format")
    void createPizza_InvalidJson() {
        given()
            .contentType(ContentType.JSON)
            .body("{ \"invalid\": json }")
            .when()
                .post("/pizzen")
            .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("POST /pizzen - Fehlender Content-Type")
    void createPizza_MissingContentType() {
        String pizzaJson = """
            {
                "name": "Test Pizza",
                "beschreibung": "Test Beschreibung",
                "price": 10.00
            }
            """;

        given()
            .body(pizzaJson)
            .when()
                .post("/pizzen")
            .then()
                .statusCode(415); // Unsupported Media Type
    }

    @Test
    @DisplayName("GET /pizzen - Mit verschiedenen Accept Headers")
    void getAllPizzen_AcceptHeaders() {
        // JSON Accept Header
        given()
            .accept(ContentType.JSON)
            .when()
                .get("/pizzen")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON);

        // HAL+JSON Accept Header
        given()
            .accept("application/hal+json")
            .when()
                .get("/pizzen")
            .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("GET /pizzen - Teste spezifische Pizza-Eigenschaften")
    void getAllPizzen_CheckPizzaProperties() {
        given()
            .queryParam("size", 1)
            .when()
                .get("/pizzen")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("[0].name", isA(String.class))
                .body("[0].beschreibung", isA(String.class))
                .body("[0].price", isA(Float.class))
                .body("[0].sellCounter", isA(Integer.class))
                .body("[0].id", isA(Integer.class));
    }
}
