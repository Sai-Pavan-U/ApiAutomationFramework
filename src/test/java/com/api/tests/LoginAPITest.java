package com.api.tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.Test;

import com.api.pojo.UserCredentials;
import com.api.utils.ConfigManager;

import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

public class LoginAPITest {

    @Test(description = "Login API Test")
    public void loginAPITest() {
        UserCredentials creds = new UserCredentials("iamfd", "password");

        Response res;

        res = given().baseUri(ConfigManager.getProperty("BASE_URI"))
                .and().contentType(ContentType.JSON).and().body(creds)
                .and().log().body()
                .when().post("/login").then().log().ifValidationFails()
                .and().statusCode(200)
                .body("data.token", notNullValue())
                .time(lessThan(5000L))
                .and().body("message", equalTo("Success"))
                .and()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("response-schema/LoginResponseSchema.json"))
                .extract().response();
        System.out.println(res.asPrettyString());

    }
}