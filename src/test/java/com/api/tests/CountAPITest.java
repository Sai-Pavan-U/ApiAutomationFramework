package com.api.tests;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.*;
import static org.hamcrest.Matchers.*;
import org.testng.annotations.*;

import com.api.constants.Roles;
import static com.api.utils.AuthTokenProvider.*;
import static com.api.utils.ConfigManager.*;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.module.jsv.JsonSchemaValidator;

public class CountAPITest {

    @Test(description = "Test that the status code of count api is 200", enabled = true)
    public void testCountApiStatus() {

        given()
                .baseUri(getProperty("BASE_URI"))
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", getAuthToken(Roles.FD)))
                .when()
                .get("dashboard/count")
                .then()
                .log()
                .all()
                .statusCode(200)
                .time(lessThan(1500L))
                .body("message", equalTo("Success"))
                .body("data", notNullValue())
                .body("data.items", notNullValue())
                .body("data.label", everyItem(not(blankOrNullString())))
                .body("data.key[0]", equalTo("pending_for_delivery"))
                .body(matchesJsonSchemaInClasspath("response-schema/CountResponseSchema.json"));

    }

    @Test(description = "Test the invalid Header", enabled = true)
    public void testInvalidHeader() {
        given()
                .baseUri(getProperty("BASE_URI"))
                .and()
                .contentType(ContentType.JSON)
                .and()
                .when()
                .get("dashboard/count")
                .then()
                .log()
                .all()
                .statusCode(401); // ← Expect 401 Unauthorized
    }

    @Test(description = "Test the invalid auth token", enabled = false)
    public void testInvalidToken() {

        given()
                .baseUri(getProperty("BASE_URI"))
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer invalid_token"))
                .when()
                .get("dashboard/count")
                .then()
                .log().all()
                .statusCode(500)
                .body("message", equalTo("jwt malformed"))
                .and();
        // .body("data.key", equalTo("null"));
    }

}
