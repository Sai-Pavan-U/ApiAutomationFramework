package com.api.tests;

import org.testng.annotations.Test;

import com.api.pojo.UserCredentials;
import com.api.utils.ConfigManager;

import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*; // For given(), when(), then() methods
import static org.hamcrest.Matchers.*; // Hamcrest matcher for assertions

import java.io.FileNotFoundException;
import static com.api.utils.ConfigManager.*;

/*
 Detailed study notes (for learning and reference):

 - Purpose of this file:
   This is a TestNG test class that demonstrates a simple API automation test
   for a login endpoint using RestAssured. It builds a request, sends it, asserts
   on the response, and extracts the Response object for further inspection.

 - Key components:
   * UserCredentials POJO: a simple Java object used as the request body. RestAssured
     will serialize this POJO to JSON when ContentType.JSON is specified.
   * RestAssured fluent API: uses static imports for given(), when(), then() to
     construct readable BDD-style requests.
   * Hamcrest matchers: used inside .body(...) to perform expressive assertions
     on JSON response fields (e.g., notNullValue(), equalTo()).
   * Response extraction: .extract().response() lets you capture the entire
     response for additional assertions, logging or debugging.

 - The typical BDD flow:
   given()  -> set up request (baseUri, headers, content type, body, query params)
   when()   -> make the HTTP call (get/post/put/delete...)
   then()   -> assert on the response (status code, body, headers, time)

 - Common assertions shown here:
   * statusCode(200): verifies the HTTP status
   * body("data.token", notNullValue()): checks a JSON path exists and is not null
   * time(lessThan(5000L)): ensures response time is under threshold (in ms)
   * body("message", equalTo("Success")): validates specific response message

 - Logging:
   * .log().body() before the request prints the serialized request body to stdout
   * .log().ifValidationFails() in then() prints the response only if an assertion fails

 - Best practices & tips:
   * Avoid hard-coding baseUri and endpoints in tests; use a configuration file
     or TestNG parameters and a common RequestSpecification to reuse settings.
   * Use dedicated assertion libraries (Hamcrest, AssertJ) for more readable checks.
   * Add negative tests (invalid credentials, missing fields) to validate error handling.
   * Validate JSON schema for contract testing (RestAssured supports .body(matchesXsd) or using json-schema-validator).
   * For authentication flows, extract tokens once and reuse them in subsequent calls using a shared context or test setup.
   * Make time assertions realistic: CI environments may be slower than local runs. Use generous thresholds or SLAs.
   * Use .log().all() sparingly; prefer conditional logging to avoid noisy test output.
   * When serializing POJOs, ensure they have proper getters/setters or use annotations (Jackson/Gson) if needed.
   * If tests flake due to network/time, add retries or increase timeouts, but investigate root cause first.

 - Extending this test: 
   * Externalize credentials and sensitive data; don't commit plain passwords.
   * Implement data-driven tests with TestNG's @DataProvider for multiple credential sets.
   * Add setup/teardown methods (@BeforeClass/@AfterClass) to prepare test data or cleanup.
   * Use response.jsonPath() to parse and reuse values programmatically.

*/

public class LoginAPITest {

	@Test(description = "Login API Test")
	public void loginAPITest() {
		// Create request payload using a POJO. RestAssured will serialize this to JSON.
		UserCredentials creds = new UserCredentials("iamfd", "password");
		

		// Build and execute the request using RestAssured's BDD-style API.
		Response res;

		res = given().baseUri(ConfigManager.getProperty("BASE_URI")) // Consider externalizing
				.and().contentType(ContentType.JSON).and().body(creds) // The POJO is serialized to JSON
				.and().log().body() // Logs the request body (useful for debugging)
				.when().post("/login").then().log().ifValidationFails() // Log response only if assertions fail
				.and().statusCode(200) // Assert HTTP 200 OK
				.body("data.token", notNullValue()) // Ensure token present at JSON path
				.time(lessThan(5000L)) // Response time assertion (ms)
				.and().body("message", equalTo("Success")) // Assert response message
				.and()
				.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("response-schema/LoginResponseSchema.json")) // Optional:
																													// JSON
																													// schema
																													// validation
				.extract().response();
		System.out.println(res.asPrettyString());
		// Print pretty JSON response (helpful during learning/debugging)

	}

}