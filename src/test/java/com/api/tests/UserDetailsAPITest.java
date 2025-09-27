package com.api.tests;

import org.testng.annotations.Test;

import com.api.pojo.UserCredentials;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

import static com.api.utils.ConfigManager.*;

import static io.restassured.RestAssured.*; // For given(), when(), then() methods
import static org.hamcrest.Matchers.*; // Hamcrest matcher for assertions

/*
 Detailed study notes for UserDetailsAPITest (for learning and reference):

 - Purpose of this file:
   Tests the /userdetails endpoint which returns user profile information.
   Demonstrates sending an authenticated GET request, validating response
   status, JSON body fields, and optional JSON schema validation.

 - Key concepts shown here:
   * Authorization header: a Bearer/JWT token is used to access protected endpoints.
     In tests, tokens are often obtained from a login API rather than hard-coded.
   * JSON schema validation: ensures contract correctness using a schema file
     placed under src/test/resources (classpath). RestAssured's JsonSchemaValidator
     can validate the response structure and types.
   * Assertions: use statusCode(), body() with Hamcrest matchers, and optional
     performance assertions (time()).
   * Logging: .log().body() to print request payloads (for GET typically empty)
     and .log().ifValidationFails() to show response only when assertions fail.

 - Typical improvements and best practices:
   * Do NOT hard-code tokens in tests. Instead, obtain a token from a login flow
     in a @BeforeClass or a helper and reuse it. Store secrets in secured config.
   * Use a shared RequestSpecification (with baseUri, timeouts, common headers)
     to reduce duplication across tests.
   * Place JSON schemas under src/test/resources/response-schema and reference
     them via classpath for JsonSchemaValidator.matchesJsonSchemaInClasspath(...).
   * Add negative tests for authorization failures (401), missing permissions (403),
     and invalid inputs.
   * Use response.jsonPath() to map parts of the response to POJOs when needed.
   * Validate headers (Content-Type) and cache-control if relevant to the API.
   * Keep response-time assertions realistic for CI environments.
   * For flaky network tests, add retries or mock the API in integration tests.

 - Extending this test:
   * Extract token dynamically by calling the login API and reuse it here.
   * Use TestNG @DataProvider to validate multiple user roles or IDs.
   * Assert sensitive fields are masked or omitted as per privacy requirements.
   * Convert this test to use a fluent client wrapper to improve readability.

*/

public class UserDetailsAPITest {

	@Test
	public void userDetailsAPITest() {
		// NOTE: For study/demo purposes a long JWT token is shown below.
		// In real tests do NOT hard-code tokens; instead obtain them programmatically.
		Header authHeader = new Header("Authorization",
				"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NCwiZmlyc3RfbmFtZSI6ImZkIiwibGFzdF9uYW1lIjoiZmQiLCJsb2dpbl9pZCI6ImlhbWZkIiwibW9iaWxlX251bWJlciI6Ijg4OTk3NzY2NTUiLCJlbWFpbF9pZCI6Im1hcmtAZ21haWwuY29tIiwicGFzc3dvcmQiOiI1ZjRkY2MzYjVhYTc2NWQ2MWQ4MzI3ZGViODgyY2Y5OSIsInJlc2V0X3Bhc3N3b3JkX2RhdGUiOm51bGwsImxvY2tfc3RhdHVzIjowLCJpc19hY3RpdmUiOjEsIm1zdF9yb2xlX2lkIjo1LCJtc3Rfc2VydmljZV9sb2NhdGlvbl9pZCI6MSwiY3JlYXRlZF9hdCI6IjIwMjEtMTEtMDNUMDg6MDY6MjMuMDAwWiIsIm1vZGlmaWVkX2F0IjoiMjAyMS0xMS0wM1QwODowNjoyMy4wMDBaIiwicm9sZV9uYW1lIjoiRnJvbnREZXNrIiwic2VydmljZV9sb2NhdGlvbiI6IlNlcnZpY2UgQ2VudGVyIEEiLCJpYXQiOjE3NTgzNzUzOTZ9.bOVhMG87l8LHRlfKHaVIaQ1dmXXHLK18BdD-m53RDtI");

		System.out.println("UserDetailsAPITest");

		// Build request: set baseUri, content type and authorization header.
		// For GET requests, .body() is not required unless you send a payload.
		Response response = given().baseUri(getProperty("BASE_URI")).and().contentType(ContentType.JSON).and()
				.header(authHeader).log().body() // Logs request details; for
				// GET this shows headers
				// and (empty) body
				.when().get("/userdetails").then().log().ifValidationFails() // Show response only if assertions fail to
																				// reduce noise
				.and().statusCode(200) // Expect HTTP 200 OK for a successful request
				.body("data.id", notNullValue()) // Ensure the user id field exists
				// Optional: validate entire response structure against a JSON schema stored on
				// the test classpath
				.body(JsonSchemaValidator
						.matchesJsonSchemaInClasspath("response-schema/UserDetailsResponseSchema.json"))
				.and().extract().response(); // Extract full response for further programmatic checks

		// Helpful for study/debugging: print the pretty JSON response
		System.out.println(response.asPrettyString());

		// Example: use JsonPath to read values if needed
		// int id = response.jsonPath().getInt("data.id");
		// System.out.println("User ID: " + id);

	}

}