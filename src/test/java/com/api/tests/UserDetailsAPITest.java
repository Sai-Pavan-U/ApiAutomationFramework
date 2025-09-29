package com.api.tests;

import static com.api.utils.ConfigManager.getProperty;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

import org.testng.annotations.Test;

import com.api.utils.AuthTokenProvider;
import com.api.utils.Role;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

public class UserDetailsAPITest {

    @Test
    public void userDetailsAPITest() {
        Header authHeader = new Header("Authorization",
                AuthTokenProvider.getAuthToken(Role.FRONT_DESK));

        System.out.println("UserDetailsAPITest");

        Response response = given().baseUri(getProperty("BASE_URI")).and().contentType(ContentType.JSON).and()
                .header(authHeader).log().body()
                .when().get("/userdetails").then().log().ifValidationFails()
                .and().statusCode(200)
                .body("data.id", notNullValue())
                .body(JsonSchemaValidator
                        .matchesJsonSchemaInClasspath("response-schema/UserDetailsResponseSchema.json"))
                .and().extract().response();

        System.out.println(response.asPrettyString());
    }
}