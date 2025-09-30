package com.api.tests;

import static com.api.utils.AuthTokenProvider.getAuthToken;
import static com.api.utils.ConfigManager.getProperty;
import static io.restassured.RestAssured.given;

import com.api.constants.Roles;

import static org.hamcrest.Matchers.*;

import org.testng.annotations.Test;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.module.jsv.JsonSchemaValidator;

public class MasterAPITest {

    @Test(description = "Test the positive cases like status code and data values")
    public void testMasterAPI() {
        given()
                .baseUri(getProperty("BASE_URI"))
                .and()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", getAuthToken(Roles.FD)))
                .when()
                .post("/master")
                .then()
                .log()
                .all()
                .statusCode(200)
                .time(lessThan(200L))
                .body("data", hasKey("mst_oem"))
                .body("data.mst_oem[0].id", equalTo(1))
                .body("data.mst_oem.id", everyItem(notNullValue()))
                .body("message", equalTo("Success")) // <--- Updated here to match actual response message 
                .body("$", hasKey("data"))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("response-schema/MasterResponseSchema.json"));

    }

}
