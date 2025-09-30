package com.api.utils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import com.api.constants.Roles;
import com.api.pojo.UserCredentials;

import io.restassured.http.ContentType;

public class AuthTokenProvider {

    private AuthTokenProvider() {
    }

    public static String getAuthToken(Roles role) {
        UserCredentials user;

        // 🎯 Using switch case for role-based credential selection
        switch (role) {
            case FD:
                user = new UserCredentials("iamfd", "password");
                break;
            case SUP:
                user = new UserCredentials("iamsup", "password");
                break;
            case ENG:
                user = new UserCredentials("iameng", "password");
                break;
            case QC:
                user = new UserCredentials("iamqa", "password");
                break;
            default:
                throw new IllegalArgumentException("Unsupported role: " + role);
        }

        String authToken = given()
                .baseUri(ConfigManager.getProperty("BASE_URI"))
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("login")
                .then()
                .statusCode(200)
                .body("message", equalTo("Success"))
                .log()
                .all()
                .extract()
                .jsonPath()
                .getString("data.token");

        return authToken;
    }

    @Deprecated
    public static String getAuthToken(String role) {
        return getAuthToken(Roles.valueOf(role.toUpperCase()));
    }

    public static void main(String[] args) {
        System.out.println("Auth Token: " + getAuthToken(Roles.FD));
    }
}
