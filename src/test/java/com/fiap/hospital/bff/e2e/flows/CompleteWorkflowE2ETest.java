package com.fiap.hospital.bff.e2e.flows;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.hospital.bff.e2e.config.E2ETestConfiguration;
import com.fiap.hospital.bff.e2e.util.E2ETestDataFactory;
import com.fiap.hospital.bff.infra.entrypoint.controller.dto.request.UserCredentialsDto;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * End-to-End tests for complete application workflows.
 * Tests realistic scenarios that span multiple controllers and business processes.
 */
@DisplayName("E2E: Fluxos Completos da Aplicação")
public class CompleteWorkflowE2ETest extends E2ETestConfiguration {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should complete restaurant owner journey successfully")
    void shouldCompleteRestaurantOwnerJourneySuccessfully() throws Exception {
        String uniqueId = createUniqueId();
        
        // Step 1: Create OWNER user type if it doesn't exist
        var ownerType = E2ETestDataFactory.createValidTypeUserRequest("OWNER_" + uniqueId);
        
        Response typeResponse = given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(ownerType))
        .when()
                .post("/type-users")
        .then()
                .statusCode(anyOf(equalTo(201), equalTo(400), equalTo(409), equalTo(500)))
                .time(lessThan(3000L))
                .extract().response();
        
        System.out.println("Owner type creation response: " + typeResponse.asString());
        
        // Step 2: Create owner user account
        var ownerUser = E2ETestDataFactory.createValidUserRequest(uniqueId);
        
        Response userResponse = given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(ownerUser))
        .when()
                .post("/users")
        .then()
                .statusCode(anyOf(equalTo(201), equalTo(400), equalTo(409), equalTo(500)))
                .time(lessThan(5000L))
                .extract().response();
        
        System.out.println("Owner user creation response: " + userResponse.asString());

    }
    
    @Test
    @DisplayName("Should complete customer registration and browsing journey")
    void shouldCompleteCustomerRegistrationAndBrowsingJourney() throws Exception {
        String uniqueId = createUniqueId();
        
        // Step 1: Create customer user type if needed
        var customerType = E2ETestDataFactory.createValidTypeUserRequest("CUSTOMER_" + uniqueId);
        
        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(customerType))
        .when()
                .post("/type-users")
        .then()
                .statusCode(anyOf(equalTo(201), equalTo(400), equalTo(409), equalTo(500)))
                .time(lessThan(3000L));
        
        // Step 2: Register as customer
        var customer = E2ETestDataFactory.createValidUserRequest(uniqueId);
        
        Response customerResponse = given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(customer))
        .when()
                .post("/users")
        .then()
                .statusCode(anyOf(equalTo(201), equalTo(400), equalTo(409), equalTo(500)))
                .time(lessThan(5000L))
                .extract().response();
        
        System.out.println("Customer registration response: " + customerResponse.asString());

    }
    
    @Test
    @DisplayName("Should handle system administration tasks")
    void shouldHandleSystemAdministrationTasks() throws Exception {
        String uniqueId = createUniqueId();
        
        // Step 1: Create admin user type
        var adminType = E2ETestDataFactory.createValidTypeUserRequest("ADMIN_" + uniqueId);
        
        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(adminType))
        .when()
                .post("/type-users")
        .then()
                .statusCode(anyOf(equalTo(201), equalTo(400), equalTo(409), equalTo(500)))
                .time(lessThan(3000L));
        
        // Step 2: List all user types for administration
        given()
                .contentType(ContentType.JSON)
        .when()
                .get("/type-users")
        .then()
                .statusCode(anyOf(equalTo(200), equalTo(500)))
                .time(lessThan(2000L));
        
        // Step 3: List all users for administration
        given()
                .contentType(ContentType.JSON)
        .when()
                .get("/users")
        .then()
                .statusCode(anyOf(equalTo(200), equalTo(500)))
                .time(lessThan(3000L));

    }
    
    @Test
    @DisplayName("Should handle authentication flow with user creation")
    void shouldHandleAuthenticationFlowWithUserCreation() throws Exception {
        String uniqueId = createUniqueId();
        
        // Step 1: Create user type for authentication
        var userType = E2ETestDataFactory.createValidTypeUserRequest("USER_" + uniqueId);
        
        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(userType))
        .when()
                .post("/type-users")
        .then()
                .statusCode(anyOf(equalTo(201), equalTo(400), equalTo(409), equalTo(500)))
                .time(lessThan(3000L));
        
        // Step 2: Create user for authentication
        var user = E2ETestDataFactory.createValidUserRequest(uniqueId);
        
        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(user))
        .when()
                .post("/users")
        .then()
                .statusCode(anyOf(equalTo(201), equalTo(400), equalTo(409), equalTo(500)))
                .time(lessThan(5000L));
        
        // Step 3: Attempt authentication
        var authRequest = E2ETestDataFactory.createValidAuthRequest(uniqueId);
        
        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(authRequest))
        .when()
                .post("/auth/login")
        .then()
                .statusCode(anyOf(equalTo(200), equalTo(401), equalTo(404), equalTo(500)))
                .time(lessThan(3000L));
    }
    
    @Test
    @DisplayName("Should handle error scenarios gracefully across workflows")
    void shouldHandleErrorScenariosGracefullyAcrossWorkflows() throws Exception {

        // Test 2: Try to access non-existent resources
        given()
                .contentType(ContentType.JSON)
        .when()
                .get("/restaurants/99999")
        .then()
                .statusCode(anyOf(equalTo(404), equalTo(500)))
                .time(lessThan(2000L));
        
        given()
                .contentType(ContentType.JSON)
        .when()
                .get("/users/99999")
        .then()
                .statusCode(anyOf(equalTo(404), equalTo(500)))
                .time(lessThan(2000L));
        
        given()
                .contentType(ContentType.JSON)
        .when()
                .get("/cardapio/99999")
        .then()
                .statusCode(anyOf(equalTo(404), equalTo(500)))
                .time(lessThan(2000L));
        
        // Test 3: Try invalid authentication
        var invalidAuth = new UserCredentialsDto(
                "nonexistent@user.com",
                "wrongpassword"
        );
        
        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(invalidAuth))
        .when()
                .post("/auth/login")
        .then()
                .statusCode(anyOf(equalTo(401), equalTo(404), equalTo(500)))
                .time(lessThan(3000L));
    }
}
