# 🚀 API Automation Testing - Runtime Implementation Notes

> **Actual Implementation Coverage in ApiAutomationFramework Project**

---

## 📋 Project Structure & Coverage

| 📁 **Class File**                     | 🎯 **Concepts Implemented**                                   | ✅ **Status** |
| ------------------------------------- | -------------------------------------------------------------- | ------------- |
| `ConfigManager.java`                  | [Static Initialization & Environment Config](#configmanager)  | Implemented   |
| `Roles.java`                          | [Simple Enum Constants](#roles-enum)                          | Implemented   |
| `AuthTokenProvider.java`              | [Switch Case & Token Management](#authtokenprovider)          | Refactored    |
| `UserCredentials.java`                | [POJO Pattern](#usercredentials-pojo)                         | Implemented   |
| `LoginAPITest.java`                   | [Basic API Testing](#loginapitest)                            | Implemented   |
| `UserDetailsAPITest.java`             | [Authenticated API Testing](#userdetailsapitest)              | Implemented   |
| `CountAPITest.java`                   | [Dashboard API Testing with Multiple Scenarios](#countapitest) | Enhanced      |
| `MasterAPITest.java`                  | [Master Data API Testing](#masterapitest)                     | New Addition  |

---

## 🎯 What We've Built So Far

Our project demonstrates these core API testing concepts through actual working code:

### 📊 Current Framework Coverage

- **8 Class Files** implementing core API testing patterns
- **12+ Concepts** demonstrated with real implementations  
- **6 Test Methods** across 4 test classes
- **3 Different API Endpoints** (login, dashboard/count, master, userdetails)
- **Multiple Testing Scenarios** (positive, negative, security, performance)

### 🚀 Key Achievements

✅ **Environment Configuration** - Multi-environment support (dev/qa/uat)  
✅ **Type-Safe Authentication** - Enum-based role management with switch case  
✅ **Comprehensive Validation** - Status, performance, schema, security testing  
✅ **Advanced Patterns** - Static imports, POJO serialization, BDD syntax  
✅ **Error Handling** - Negative test cases and boundary testing  
✅ **Schema Validation** - JSON schema compliance verification

---

## ConfigManager

<details>
<summary><strong>📖 Static Initialization & Environment Configuration</strong></summary>

**Location:** `src/test/java/com/api/utils/ConfigManager.java`

### 🎯 What's Actually Implemented

Our `ConfigManager` class demonstrates these key concepts in practice:

#### 1️⃣ Static Initialization Block

```java
private static final Properties PROPS = new Properties();

static {
    String env = System.getProperty("env", "dev").toLowerCase().trim();
    String configFile = "config/config." + env + ".properties";
    // ... loading logic
}
```

**Runtime Behavior:**
- Executes once when class is first loaded
- Creates single `Properties` instance for entire application
- Environment detection via system property: `mvn test -Denv=qa`

#### 2️⃣ Fallback Configuration Strategy

```java
if (in == null) {
    System.out.println("Environment-specific config not found, trying default");
    in = Thread.currentThread().getContextClassLoader()
        .getResourceAsStream("config/config.properties");
}
```

**What This Does:**
- ✅ Try environment-specific config first (`config.qa.properties`)
- ✅ Fallback to default `config.properties` if environment config missing
- ❌ Throw runtime exception if no config found at all

#### 3️⃣ Resource Management

```java
try {
    PROPS.load(in);
    in.close();
} catch (IOException e) {
    throw new RuntimeException("Failed to load configuration", e);
}
```

**Implementation Notes:**
- Proper stream closure after loading
- Fail-fast on configuration errors
- Runtime exception propagation for critical failures

#### 4️⃣ Utility Pattern

```java
private ConfigManager() {
    // Prevents instantiation
}

public static String getProperty(String key) {
    return PROPS.getProperty(key);
}
```

**Current Usage in Tests:**
```java
// From LoginAPITest.java
.baseUri(ConfigManager.getProperty("BASE_URI"))

// From UserDetailsAPITest.java  
.baseUri(getProperty("BASE_URI"))  // Static import
```

### 🏗️ File Structure We Support

```
src/test/resources/config/
├── config.properties      # Default config
├── config.dev.properties  # Development environment
├── config.qa.properties   # QA environment
└── config.uat.properties  # UAT environment
```

### 🎤 Interview Questions & Real-Time Scenarios

<details>
<summary><strong>Comprehensive Interview Q&A and Situational Scenarios</strong></summary>

#### 🏗️ Architecture & Design Questions

**Q1: "How would you handle configuration management in a multi-environment API testing framework?"**

**Your Answer:** "I implemented a ConfigManager class with static initialization that automatically detects the environment using system properties. For example, when running `mvn test -Denv=qa`, it loads `config.qa.properties`. If the environment-specific config isn't found, it falls back to `config.properties`. This ensures our tests can run seamlessly across dev, QA, and UAT environments without code changes."

**Q2: "What happens if your configuration file is missing or corrupted?"**

**Your Answer:** "Our ConfigManager has a two-tier fallback strategy. First, it tries to load the environment-specific config. If that fails, it attempts to load the default config.properties. If both fail, it throws a RuntimeException with a clear error message. This fail-fast approach ensures configuration issues are caught early rather than causing mysterious test failures later."

#### 🔧 Technical Implementation Questions

**Q3: "How do you ensure thread safety in your configuration management?"**

**Your Answer:** "We use a static initialization block that executes only once when the class is first loaded. The Properties object is marked as `static final`, making it immutable after initialization. Since all our config values are read-only during test execution, we don't need additional synchronization mechanisms."

**Q4: "Walk me through your approach to handling different base URIs across environments."**

**Your Answer:** "Each environment has its own properties file: `config.dev.properties` has `BASE_URI=http://dev-api.company.com`, while `config.qa.properties` has `BASE_URI=http://qa-api.company.com`. The ConfigManager automatically loads the correct URI based on the `-Denv` parameter, so the same test code works across all environments without hardcoding URLs."

#### 🎯 Real-Time Problem Solving Scenarios

**Scenario 1: Emergency Production Issue**
**Interviewer:** "Your production deployment failed at 2 AM. The team suspects configuration issues. How would your ConfigManager help troubleshoot this?"

**Your Answer:** "First, I'd check if our ConfigManager's fallback mechanism activated by looking for the 'Environment-specific config not found, trying default' message. Then I'd verify the properties file integrity and compare production config with our tested QA config. Our static initialization would have failed fast if there were syntax errors, so I'd look for network-related configuration mismatches like wrong database URLs or API endpoints."

**Scenario 2: New Environment Setup**
**Interviewer:** "Your company is adding a new staging environment. How would you integrate it into your existing framework?"

**Your Answer:** "I'd create a new `config.staging.properties` file with staging-specific values. The beauty of our ConfigManager is that no code changes are needed - just run `mvn test -Denv=staging` and it automatically picks up the new configuration. I'd copy from `config.qa.properties` as a template and update BASE_URI, database connections, and any staging-specific settings."

**Scenario 3: Configuration Encryption**
**Interviewer:** "Security team mandates that passwords in config files must be encrypted. How would you modify ConfigManager?"

**Your Answer:** "I'd extend ConfigManager with a decryption method. The static initialization would remain the same, but I'd add a `getSecureProperty()` method that decrypts values marked with a prefix like `ENC()`. For example: `DB_PASSWORD=ENC(encrypted_value)`. This maintains backward compatibility while adding security for sensitive values."

#### 🚨 Troubleshooting Scenarios

**Scenario 4: Race Condition Investigation**
**Interviewer:** "Tests are failing intermittently with NullPointerException on getProperty(). What's your investigation approach?"

**Your Answer:** "Since our ConfigManager uses static initialization, this shouldn't happen after the class loads. I'd check if multiple threads are somehow triggering class loading simultaneously, or if there's a classpath issue causing the properties file to not load. I'd add debug logging in the static block and verify the PROPS object is properly initialized before any test execution begins."

**Scenario 5: Performance Optimization**
**Interviewer:** "Your test suite takes too long to start. Each test method calls getProperty() multiple times. How would you optimize?"

**Your Answer:** "I'd implement property caching in ConfigManager. Instead of accessing the Properties object every time, I'd create static final String constants for frequently used values like BASE_URI during initialization. For dynamic properties, I'd keep the current approach, but for static ones, I'd pre-load them: `private static final String BASE_URI = PROPS.getProperty('BASE_URI');`"

#### 🔄 Maintenance & Evolution Questions

**Q5: "How would you handle configuration versioning as your API framework grows?"**

**Your Answer:** "I'd implement a configuration validation mechanism in the static block that checks for required properties and their formats. I'd also add a CONFIG_VERSION property to track schema changes. If a config file is missing required properties or has an incompatible version, the framework would fail with a detailed error message listing what's missing or incompatible."

**Q6: "Describe a scenario where your fallback configuration saved the day."**

**Your Answer:** "During a critical deployment, our QA environment config file got corrupted due to a deployment script error. Instead of all tests failing with unclear errors, our ConfigManager automatically fell back to the default configuration, allowing most tests to continue running. This gave us time to fix the QA config while maintaining continuous integration. The fallback mechanism provided us with a 4-hour window to resolve the issue without blocking the entire development team."

#### 💡 Advanced Scenarios

**Scenario 6: Multi-Region Testing**
**Interviewer:** "Your company expands to multiple AWS regions. How would you handle region-specific configurations?"

**Your Answer:** "I'd extend the environment concept to include regions: `config.qa-us-east.properties`, `config.qa-eu-west.properties`. The ConfigManager would parse a compound environment parameter like `-Denv=qa-us-east`. I'd implement a hierarchical fallback: region-specific → environment-specific → default, ensuring maximum flexibility while maintaining simplicity."

**Scenario 7: Dynamic Configuration Updates**
**Interviewer:** "What if you need to update configuration during test execution without restarting?"

**Your Answer:** "For dynamic updates, I'd implement a `refreshConfiguration()` method that reloads the properties file. However, this breaks the immutability principle, so I'd use it sparingly and ensure thread safety with synchronized blocks. For most scenarios, I'd recommend environment variables for truly dynamic values and keep the static initialization for stable configuration."

</details>

</details>

---

## Roles Enum

<details>
<summary><strong>📖 Simple Enum Constants</strong></summary>

**Location:** `src/test/java/com/api/constants/Roles.java`

### 🎯 What's Actually Implemented

Our `Roles` enum demonstrates simple enum constant pattern:

#### 1️⃣ Simple Enum Constants

```java
public enum Roles {
    FD, SUP, QC, ENG;
}
```

**Key Features:**
- **FD** - Front Desk role
- **SUP** - Supervisor role  
- **QC** - Quality Control role
- **ENG** - Engineer role

#### 2️⃣ Benefits Over String Constants

| Benefit | String Constants | Roles Enum |
|---|---|---|
| **Type Safety** | ❌ Runtime errors | ✅ Compile-time validation |
| **IDE Support** | ❌ No autocomplete | ✅ Full IntelliSense |
| **Refactoring** | ❌ Manual updates | ✅ Automatic refactoring |
| **Performance** | ❌ String comparison | ✅ Object comparison |

#### 3️⃣ Usage in Tests

```java
// From CountAPITest.java
AuthTokenProvider.getAuthToken(Roles.FD)

// From AuthTokenProvider.java switch case
switch (role) {
    case FD -> user = new UserCredentials("iamfd", "password");
    case SUP -> user = new UserCredentials("iamsup", "password");
    // ... other cases
}
```

### 🎤 Interview Questions & Real-Time Scenarios

<details>
<summary><strong>Comprehensive Interview Q&A and Situational Scenarios</strong></summary>

#### 🔧 Enum Design & Implementation Questions

**Q1: "Why did you choose enums over string constants for roles?"**

**Your Answer:** "Enums provide compile-time type safety. If I mistype a role like `Roles.FDD`, the compiler catches it immediately. With string constants, `"FDD"` would only fail at runtime. Enums also provide better IDE support with autocomplete and refactoring capabilities."

**Q2: "How would you add a new role to your system?"**

**Your Answer:** "I'd add the new role to the Roles enum, like `ADMIN`, then update the switch case in AuthTokenProvider to map it to appropriate credentials. The enum ensures I can't forget to handle the new role - if I miss it in the switch case, I'll get a compilation warning about incomplete switch statements."

#### 🎯 Real-Time Problem Solving Scenarios

**Scenario 1: Production Role Addition Emergency**
**Interviewer:** "It's Friday evening, and urgent requirement comes in to add a new MANAGER role for weekend testing. Walk me through your process."

**Your Answer:** "First, I'd add `MANAGER` to the Roles enum. The compiler would immediately show me all places needing updates - mainly the switch case in AuthTokenProvider. I'd add the new case: `case MANAGER -> user = new UserCredentials('iammanager', 'password');`. I'd run a quick test to ensure the new role authenticates properly, then commit and deploy. The enum pattern makes this a 5-minute change instead of hunting through string constants."

**Scenario 2: Role Consolidation Project**
**Interviewer:** "Management wants to consolidate roles - merge QC and ENG into a single QUALITY role. How do you handle this without breaking existing tests?"

**Your Answer:** "I'd implement a phased approach. First, add the new QUALITY role to the enum. In AuthTokenProvider, I'd map QUALITY to appropriate credentials. Then I'd update tests gradually from QC/ENG to QUALITY. The enum approach allows me to deprecate QC and ENG by keeping them in the enum but adding documentation comments, ensuring existing tests continue working during the transition."

#### 🏗️ Architecture & Design Questions

**Q3: "What if you need roles with additional properties like permissions?"**

**Your Answer:** "Currently, our Roles enum uses simple constants for basic role identification. If we needed additional properties, I'd refactor to include fields and constructors: `FD('Front Desk', 'basic_permissions')`. However, for our current authentication needs, simple constants work perfectly and keep the code clean."

**Q4: "How would you handle role hierarchy or inheritance?"**

**Your Answer:** "I'd add a hierarchy method to the enum: `public boolean hasPermission(Permission permission)`. Each role would define its permissions, and I could check `if (role.hasPermission(Permission.READ_USERS))`. This keeps the enum pattern while adding sophisticated permission checking."

#### 🚨 Troubleshooting Scenarios

**Scenario 3: Authentication Failure Investigation**
**Interviewer:** "Tests are failing with 'Unsupported role' exceptions. The logs show the role exists in the enum. What's your debugging approach?"

**Your Answer:** "This suggests a case sensitivity or enum comparison issue. I'd first verify the exact role being passed - maybe there's whitespace or case mismatch. I'd add debug logging in the switch case to see exactly what role value is received. I'd also check if there's any enum serialization/deserialization happening that might corrupt the role value. The switch case with enum should be bulletproof, so the issue is likely in how the role is being passed to the method."

**Scenario 4: Performance Optimization Challenge**
**Interviewer:** "Your test suite creates thousands of tokens. Each call to getAuthToken() goes through the switch case. How would you optimize?"

**Your Answer:** "I'd implement role-to-credentials caching using a static Map initialized during class loading: `private static final Map<Roles, UserCredentials> ROLE_CREDENTIALS = Map.of(FD, new UserCredentials('iamfd', 'password'), ...);`. Then the switch case becomes `UserCredentials user = ROLE_CREDENTIALS.get(role);`. This maintains the clarity while improving performance for high-volume test execution."

#### 🔄 Evolution & Maintenance Questions

**Q5: "How would you handle external role management integration?"**

**Your Answer:** "If roles needed to come from an external system, I'd keep the enum for type safety but modify AuthTokenProvider to fetch credentials dynamically. The enum would become role identifiers, and I'd add a `RoleCredentialsProvider` interface that could be implemented for database, LDAP, or API-based credential resolution. This maintains compile-time safety while adding runtime flexibility."

**Q6: "Describe a scenario where enum type safety prevented a bug."**

**Your Answer:** "During development, a teammate tried to pass `'fd'` (lowercase) as a role parameter. Since our method expects `Roles.FD`, the compiler immediately flagged this as a type mismatch. With string constants, this would have compiled but caused a runtime authentication failure in production. The enum caught this during code review, preventing a potential production authentication issue."

#### 💡 Advanced Design Scenarios

**Scenario 5: Multi-Tenant Application Support**
**Interviewer:** "Your application needs to support multiple tenants with different role structures. How would you evolve the Roles enum?"

**Your Answer:** "I'd create a tenant-aware role system. Each enum constant would include tenant context: `FD_TENANT_A`, `FD_TENANT_B`, or use a composite pattern with `Role(RoleType.FD, Tenant.A)`. The AuthTokenProvider switch case would handle tenant-specific credential mapping. This maintains type safety while supporting multi-tenancy."

**Scenario 6: Role-Based Test Categorization**
**Interviewer:** "You need to run different test suites based on roles - admin tests, user tests, etc. How would you leverage the Roles enum?"

**Your Answer:** "I'd add TestNG groups to tests and use the Roles enum to determine groups: `@Test(groups = {Roles.ADMIN.getTestGroup()})`. The enum could have a `getTestGroup()` method returning appropriate group names. This would allow running `mvn test -Dgroups=admin-tests` to execute only admin-related tests, using the enum as the source of truth for role categorization."

</details>

</details>

---

## AuthTokenProvider

<details>
<summary><strong>📖 Switch Case & Token Management</strong></summary>

**Location:** `src/test/java/com/api/utils/AuthTokenProvider.java`

### 🎯 What's Actually Implemented

Refactored utility class demonstrating switch case pattern with enum:

#### 1️⃣ Switch Case Implementation

```java
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
    // ... RestAssured login call
}
```

#### 2️⃣ Refactoring Benefits

**Before (Enum Method Approach):**
```java
UserCredentials user = role.getUserCredentials();
```

**After (Switch Case Approach):**
```java
switch (role) {
    case FD -> user = new UserCredentials("iamfd", "password");
    // ... explicit credential mapping
}
```

**Advantages:**
- ✅ **Explicit Mapping**: Clear credential assignment per role
- ✅ **Centralized Logic**: All authentication logic in one place
- ✅ **Easy Debugging**: Visible username/password mapping
- ✅ **Error Handling**: Default case for unsupported roles

#### 3️⃣ Integration with RestAssured

```java
String authToken = given()
    .baseUri(ConfigManager.getProperty("BASE_URI"))
    .contentType(ContentType.JSON)
    .body(user)
.when()
    .post("login")
.then()
    .statusCode(200)
    .body("message", equalTo("Success"))
    .log().all()
    .extract()
    .jsonPath()
    .getString("data.token");
```

#### 4️⃣ Current Usage

```java
// From CountAPITest.java
.header(new Header("Authorization", AuthTokenProvider.getAuthToken(Roles.FD)))
```

**Authentication Flow:**
1. `Roles.FD` passed to `getAuthToken()`
2. Switch case maps to `UserCredentials("iamfd", "password")`
3. POST request to `/login` endpoint
4. JWT token extracted from response
5. Token returned for API authorization

### 🎤 Interview Questions & Scenarios

<details>
<summary><strong>Scenario-Based Interview Questions</strong></summary>

**Q1: "Walk me through your authentication strategy for API tests."**

**Your Answer:** "I implemented a centralized AuthTokenProvider that uses a switch case pattern. When I call `getAuthToken(Roles.FD)`, it maps the role to specific credentials, makes a login API call, extracts the JWT token from the response, and returns it. This ensures every test gets a fresh, valid token without duplicating authentication logic."

**Q2: "How do you handle token expiration in your tests?"**

**Your Answer:** "Each test method calls `getAuthToken()` independently, so we get a fresh token for every test. This approach is simple and reliable - we don't have to worry about token caching or expiration issues. For performance optimization in larger suites, we could implement token caching with expiration checking."

**Q3: "What if the login API fails during test execution?"**

**Your Answer:** "Our AuthTokenProvider will throw an exception if the login fails, which immediately stops the test with a clear error message. This fail-fast approach helps distinguish between authentication issues and actual API bugs. We log the full response to help debug login failures quickly."

**Q4: "Why did you refactor from enum methods to switch case?"**

**Your Answer:** "The switch case approach centralizes all credential mapping in one place, making it easier to debug and maintain. Before, credentials were scattered across enum definitions. Now, I can see all username/password mappings in the AuthTokenProvider switch case, and adding new roles only requires updating one location."

**Q5: "How would you handle different authentication types (API key, OAuth, Basic Auth)?"**

**Your Answer:** "I'd extend the switch case to include authentication type as a parameter, or create separate methods like `getBearerToken()`, `getApiKey()`, `getBasicAuth()`. For our current JWT-based system, the single method works perfectly, but the pattern scales easily for multiple auth types."

</details>

</details>

---

## CountAPITest

<details>
<summary><strong>📖 Dashboard API Testing with Multiple Test Scenarios</strong></summary>

**Location:** `src/test/java/com/api/tests/CountAPITest.java`

### 🎯 What's Actually Implemented

Enhanced test class demonstrating comprehensive dashboard endpoint testing with multiple scenarios:

#### 1️⃣ Positive Test Case - Complete Validation

```java
@Test(description = "Test that the status code of count api is 200", enabled = true)
public void testCountApiStatus() {
```

**Features:**
- ✅ **Descriptive annotation** with meaningful test description
- ✅ **Test enablement control** via `enabled = true`
- ✅ **Performance testing** with response time validation
- ✅ **Schema validation** using JSON Schema

#### 2️⃣ Enhanced Authentication & Request Setup

```java
given()
    .baseUri(getProperty("BASE_URI"))
    .contentType(ContentType.JSON)
    .header(new Header("Authorization", getAuthToken(Roles.FD)))
.when()
    .get("dashboard/count")
```

**Implementation Details:**
- Uses static imports for cleaner code
- ConfigManager integration for environment flexibility
- Role-based authentication with `Roles.FD`
- Clean BDD syntax (Given-When-Then)

#### 3️⃣ Comprehensive Response Validation

```java
.then()
    .log().all()
    .statusCode(200)
    .time(lessThan(1500L))
    .body("message", equalTo("Success"))
    .body("data", notNullValue())
    .body("data.items", notNullValue())
    .body("data.label", everyItem(not(blankOrNullString())))
    .body("data.key[0]", equalTo("pending_for_repair"))
    .body(matchesJsonSchemaInClasspath("response-schema/CountResponseSchema.json"));
```

**Validation Types:**
- **HTTP Status**: 200 OK response verification
- **Performance**: Response time under 1.5 seconds
- **Message Validation**: Success confirmation
- **Data Structure**: Non-null data and items validation
- **Array Validation**: Every label must not be blank/null
- **Specific Data**: First key validation for expected value
- **Schema Validation**: JSON schema compliance check

#### 4️⃣ Negative Test Case - Missing Authentication

```java
@Test(description = "Test the invalid Header", enabled = true)
public void testInvalidHeader() {
    given()
        .baseUri(getProperty("BASE_URI"))
        .contentType(ContentType.JSON)
    .when()
        .get("dashboard/count")
    .then()
        .log().all()
        .statusCode(401); // Expect 401 Unauthorized
}
```

**Security Testing:**
- ✅ **Missing Authorization header** validation
- ✅ **Expected 401 Unauthorized** response
- ✅ **Security boundary testing** for protected endpoints

#### 5️⃣ Edge Case - Invalid Token Testing

```java
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
        .body("message", equalTo("jwt malformed"));
}
```

**Token Validation Testing:**
- ✅ **Invalid JWT token** scenarios
- ✅ **500 error handling** for malformed tokens
- ✅ **Error message validation** for debugging
- ⚠️ **Currently disabled** (`enabled = false`) for selective test execution

#### 6️⃣ Advanced Import Strategy

```java
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.*;
import static org.hamcrest.Matchers.*;
import static com.api.utils.AuthTokenProvider.*;
import static com.api.utils.ConfigManager.*;
```

**Benefits:**
- Clean, readable test methods without class prefixes
- All static utilities imported for easy access
- Schema validation capabilities
- Comprehensive matcher library

#### 7️⃣ Test Coverage Analysis

| Test Scenario | Purpose | Status | Validation Type |
|---|---|---|---|
| `testCountApiStatus()` | Happy path validation | ✅ Active | Complete validation |
| `testInvalidHeader()` | Security boundary testing | ✅ Active | Authentication check |
| `testInvalidToken()` | Error handling validation | ⚠️ Disabled | Token validation |

### 🔗 Integration Points

This enhanced test demonstrates integration between:
1. **ConfigManager** → Environment-specific base URI
2. **Roles enum** → Type-safe role constants for authentication
3. **AuthTokenProvider** → Switch case JWT token generation
4. **RestAssured** → BDD API testing framework
5. **Hamcrest** → Rich assertion matchers
6. **JSON Schema Validator** → Response structure validation
7. **TestNG** → Test execution and control

### 🎤 Interview Questions & Scenarios

<details>
<summary><strong>Scenario-Based Interview Questions</strong></summary>

**Q1: "How do you ensure comprehensive API validation in your tests?"**

**Your Answer:** "I implement multiple validation layers. For the CountAPI, I validate HTTP status (200), performance (under 1.5 seconds), message fields, data structure, array content validation, and JSON schema compliance. I also include negative tests for missing authentication (401) and invalid tokens (500) to ensure security boundaries work correctly."

**Q2: "Explain your approach to testing API security."**

**Your Answer:** "I use a three-pronged approach: First, positive tests with valid JWT tokens to ensure authorized access works. Second, I test missing authentication headers expecting 401 Unauthorized. Third, I test invalid/malformed tokens expecting appropriate error responses. This covers the main authentication attack vectors."

**Q3: "How do you handle performance testing in API automation?"**

**Your Answer:** "I include response time assertions in every test. For the CountAPI, I set a 1.5-second limit using `.time(lessThan(1500L))`. Different endpoints have different performance expectations - our MasterAPI has a stricter 200ms limit. This catches performance regressions early in the development cycle."

**Q4: "What's your strategy for maintaining test reliability?"**

**Your Answer:** "I use selective test execution with the `enabled` parameter. Critical tests always run, but flaky tests can be temporarily disabled without removing them. I also implement schema validation to catch breaking API changes, and comprehensive logging with `.log().all()` for debugging failures."

**Q5: "Describe a scenario where schema validation caught a real issue."**

**Your Answer:** "During a release, the development team added a new field to the count response but changed the data type of an existing field from string to integer. Our schema validation immediately caught this breaking change during automated testing, preventing it from reaching QA and potentially production."

**Q6: "How do you handle test data validation for dynamic content?"**

**Your Answer:** "For dynamic data like counts that change frequently, I focus on structure validation rather than exact values. I check that counts are numbers, labels aren't empty, and required fields exist. For the first array element, I validate the key format (`pending_for_repair`) to ensure data consistency."

</details>

</details>

---

## MasterAPITest

<details>
<summary><strong>📖 Master Data API Testing Implementation</strong></summary>

**Location:** `src/test/java/com/api/tests/MasterAPITest.java`

### 🎯 What's Actually Implemented

New test class demonstrating master data endpoint testing with comprehensive validation:

#### 1️⃣ POST Request Implementation

```java
@Test(description = "Test the positive cases like status code and data values")
public void testMasterAPI() {
```

**Features:**
- ✅ **POST endpoint testing** (different from GET patterns)
- ✅ **Master data validation** for OEM information
- ✅ **Comprehensive response verification**

#### 2️⃣ Complete Request Structure

```java
given()
    .baseUri(getProperty("BASE_URI"))
    .contentType(ContentType.JSON)
    .header(new Header("Authorization", getAuthToken(Roles.FD)))
.when()
    .post("/master")
.then()
    .log().all()
    .statusCode(200)
    .time(lessThan(200L))
```

**Implementation Details:**
- **Static imports** for clean syntax
- **ConfigManager integration** for base URI
- **Role-based authentication** with FD role
- **Performance validation** under 200ms
- **POST method** for master data retrieval

#### 3️⃣ Nested Data Structure Validation

```java
.body("data", hasKey("mst_oem"))
.body("data.mst_oem[0].id", equalTo(1))
.body("message", equalTo("Success"))
.body("$", hasKey("data"))
.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("response-schema/MasterResponseSchema.json"));
```

**Advanced Validation Features:**
- **Nested Object Access**: `data.mst_oem[0].id` path validation
- **Key Existence Check**: `hasKey("mst_oem")` validation
- **Array Element Validation**: First element ID verification
- **Root Level Validation**: `$` for top-level structure
- **Schema Compliance**: Full JSON schema validation

#### 4️⃣ Expected Response Structure

```json
{
    "message": "Success",
    "data": {
        "mst_oem": [
            {
                "id": 1,
                "name": "OEM Name",
                // ... other OEM data
            }
        ]
    }
}
```

#### 5️⃣ Import Pattern

```java
import static com.api.utils.AuthTokenProvider.getAuthToken;
import static com.api.utils.ConfigManager.getProperty;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import io.restassured.module.jsv.JsonSchemaValidator;
```

**Import Strategy:**
- **Selective static imports** for specific methods
- **Full wildcard import** for Hamcrest matchers
- **Explicit import** for JsonSchemaValidator class

### 🔗 Master Data Testing Concepts

This test demonstrates:
1. **POST endpoint testing** vs GET patterns
2. **Nested JSON validation** with deep object paths
3. **Array element access** for list data
4. **Performance assertions** with strict time limits
5. **Schema validation** for complex data structures
6. **Master data integrity** verification

### 🎤 Interview Questions & Scenarios

<details>
<summary><strong>Scenario-Based Interview Questions</strong></summary>

**Q1: "How do you handle testing of POST endpoints differently from GET endpoints?"**

**Your Answer:** "POST endpoints often require request bodies and different validation approaches. In MasterAPITest, I use POST to `/master` which returns master data rather than creating it. I focus on validating the returned data structure, nested objects like `data.mst_oem[0].id`, and ensuring the response schema matches expectations. The key difference is validating complex nested responses rather than simple data retrieval."

**Q2: "Explain your approach to validating nested JSON structures."**

**Your Answer:** "I use JsonPath expressions to validate nested data. For example, `data.mst_oem[0].id` validates the ID of the first OEM record in the nested array. I also use `hasKey()` to verify object structure and `equalTo()` for specific values. This ensures both the structure and content are correct at every level."

**Q3: "How do you ensure master data integrity in your tests?"**

**Your Answer:** "I validate multiple aspects: first, the response structure using schema validation; second, specific data points like the first OEM ID being 1; third, the presence of required nested objects; and fourth, performance boundaries since master data queries should be fast. This multi-layered approach catches both structural and data integrity issues."

**Q4: "What's your strategy for performance testing on different endpoints?"**

**Your Answer:** "Different endpoints have different performance expectations based on their complexity. Master data endpoints should be fast since they're reference data, so I set a strict 200ms limit. Dashboard count endpoints can be slower due to aggregation, so I allow 1.5 seconds. Login endpoints fall in between at reasonable response times for user experience."

**Q5: "How would you handle master data that changes infrequently?"**

**Your Answer:** "For relatively static master data, I can validate specific values like the first OEM ID. However, I'd also implement flexible validation using schema checking and structural validation that doesn't break when new master data is added. The combination ensures both stability and flexibility as the dataset grows."

**Q6: "Describe how you'd debug a failed master data test."**

**Your Answer:** "I use `.log().all()` to capture the full request and response. For nested JSON failures, I'd examine the actual structure returned versus expected. I'd check if the issue is missing data, incorrect nesting, or schema changes. The comprehensive logging helps isolate whether it's a data issue, API change, or test assertion problem."

</details>

</details>

---

## UserCredentials POJO

<details>
<summary><strong>📖 Plain Old Java Object for Request Data</strong></summary>

**Location:** `src/test/java/com/api/pojo/UserCredentials.java`

### 🎯 What's Actually Implemented

Simple POJO demonstrating serialization patterns:

#### 1️⃣ Private Fields

```java
private String username;
private String password;
```

**Why Private:**
- Encapsulation principle
- Controlled access via getters/setters
- RestAssured uses reflection for JSON serialization

#### 2️⃣ Constructor

```java
public UserCredentials(String username, String password) {
    super();
    this.username = username;
    this.password = password;
}
```

**Usage Pattern:**
```java
// From LoginAPITest.java
UserCredentials creds = new UserCredentials("iamfd", "password");

// From Role.java
return new UserCredentials(username, password);
```

#### 3️⃣ Getters and Setters

```java
public String getUsername() { return username; }
public void setUsername(String username) { this.username = username; }
public String getPassword() { return password; }
public void setPassword(String password) { this.password = password; }
```

**RestAssured Integration:**
- Automatic JSON serialization when used in `.body(creds)`
- No manual JSON string building required
- Type-safe request data structure

#### 4️⃣ JSON Serialization Example

When `UserCredentials("iamfd", "password")` is passed to RestAssured:

```json
{
    "username": "iamfd",
    "password": "password"
}
```

**Benefits in Our Tests:**
- ✅ Compile-time validation of field names
- ✅ Reusable across multiple test methods
- ✅ Clear documentation of expected request structure
- ✅ IDE autocompletion for field access

### 🎤 Interview Questions & Scenarios

<details>
<summary><strong>Scenario-Based Interview Questions</strong></summary>

**Q1: "Why do you use POJOs instead of direct JSON strings in your API tests?"**

**Your Answer:** "POJOs provide compile-time safety and better maintainability. Instead of building JSON strings manually like `'{\"username\":\"iamfd\"}'`, I create `new UserCredentials('iamfd', 'password')`. RestAssured automatically serializes it to JSON. This prevents typos in field names and makes the code more readable and IDE-friendly."

**Q2: "How does RestAssured handle POJO serialization?"**

**Your Answer:** "RestAssured uses reflection to automatically convert POJOs to JSON when you pass them to `.body()`. It reads the getter methods or field names to create the JSON structure. So `UserCredentials('iamfd', 'password')` becomes `{\"username\":\"iamfd\",\"password\":\"password\"}` automatically."

**Q3: "What happens if your POJO structure doesn't match the API expectations?"**

**Your Answer:** "The API would likely return a 400 Bad Request or similar error. This is actually beneficial because it forces us to keep our POJOs in sync with the API contract. If the API expects a 'user_name' field but our POJO has 'username', we'll get immediate feedback during testing."

**Q4: "How would you handle complex nested request objects?"**

**Your Answer:** "I'd create nested POJOs that mirror the expected JSON structure. For example, if I need to send user data with an embedded address, I'd create both UserData and Address POJOs. RestAssured handles nested object serialization automatically, maintaining the proper JSON hierarchy."

**Q5: "Describe a scenario where POJOs helped you catch an API contract change."**

**Your Answer:** "When the API team changed the login request from 'username/password' to 'email/password', our UserCredentials POJO continued sending 'username', causing 400 errors. This immediately alerted us to the contract change, and we updated our POJO accordingly. With raw JSON strings, this might have been missed longer."

</details>

</details>

---

## LoginAPITest

<details>
<summary><strong>📖 Basic API Testing Implementation</strong></summary>

**Location:** `src/test/java/com/api/tests/LoginAPITest.java`

### 🎯 What's Actually Implemented

Our first test class demonstrates basic API testing patterns:

#### 1️⃣ Static Imports in Action

```java
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
```

**Result:** Clean, readable test code without class prefixes

#### 2️⃣ TestNG Test Method

```java
@Test(description = "Login API Test")
public void loginAPITest() {
```

**What This Provides:**
- Test method identification for TestNG
- Descriptive name for test reports
- Executable test case

#### 3️⃣ POJO Usage

```java
UserCredentials creds = new UserCredentials("iamfd", "password");
```

**Instead of:** Manual JSON string building
**Benefit:** Type-safe, reusable request data

#### 4️⃣ RestAssured BDD Pattern

```java
Response res = given()
    .baseUri(ConfigManager.getProperty("BASE_URI"))
    .and().contentType(ContentType.JSON)
    .and().body(creds)
    .and().log().body()
.when()
    .post("/login")
.then()
    .log().ifValidationFails()
    .and().statusCode(200)
    .body("data.token", notNullValue())
    .time(lessThan(5000L))
    .and().body("message", equalTo("Success"))
    .and().body(JsonSchemaValidator.matchesJsonSchemaInClasspath(
        "response-schema/LoginResponseSchema.json"))
    .extract().response();
```

**Step-by-Step Implementation:**

1. **Given (Setup):**
   - Base URI from configuration
   - JSON content type
   - POJO as request body
   - Request logging enabled

2. **When (Action):**
   - POST request to `/login` endpoint

3. **Then (Validation):**
   - 200 status code verification
   - Token existence check
   - Response time assertion (< 5 seconds)
   - Success message validation
   - JSON schema validation
   - Response extraction for further use

#### 5️⃣ Multiple Assertion Types

| Assertion Type | Code | Purpose |
|---|---|---|
| **Status** | `.statusCode(200)` | HTTP response validation |
| **Existence** | `.body("data.token", notNullValue())` | Required field check |
| **Performance** | `.time(lessThan(5000L))` | Response time SLA |
| **Content** | `.body("message", equalTo("Success"))` | Business logic validation |
| **Contract** | `JsonSchemaValidator.matchesJsonSchema...` | API structure validation |

#### 6️⃣ Response Handling

```java
System.out.println(res.asPrettyString());
```

**What This Does:**
- Pretty-prints the JSON response
- Useful for debugging and verification
- Shows actual response structure

### 🎤 Interview Questions & Scenarios

<details>
<summary><strong>Scenario-Based Interview Questions</strong></summary>

**Q1: "Walk me through your basic API test structure and explain your choices."**

**Your Answer:** "I use RestAssured's BDD syntax for clarity. In LoginAPITest, I start with `given()` to set up base URI, content type, and request body using a UserCredentials POJO. The `when()` section makes the actual POST call to the login endpoint. The `then()` section validates the 200 status code and success message. This structure is readable and follows testing best practices."

**Q2: "How do you ensure your login tests are reliable?"**

**Your Answer:** "I use several strategies: First, I use a POJO for request data to avoid JSON string errors. Second, I validate both status code and response message to ensure complete success. Third, I use static imports for clean, readable code. Fourth, I extract and verify the JWT token to ensure authentication actually worked, not just that the API responded."

**Q3: "What would you do if the login API started returning 500 errors intermittently?"**

**Your Answer:** "I'd first add comprehensive logging with `.log().all()` to capture full request/response details. I'd then implement retry logic for transient failures, but also add assertions to distinguish between different types of failures. If it's a server issue, I'd want the test to fail fast. If it's a network issue, retry might be appropriate."

**Q4: "How do you handle different user credentials in your login tests?"**

**Your Answer:** "I use the Roles enum with our AuthTokenProvider. Instead of hardcoding credentials in the test, I can call `getAuthToken(Roles.FD)` or `getAuthToken(Roles.SUP)` to test different user types. This makes the tests more maintainable and allows me to test role-based access controls systematically."

**Q5: "Describe how you'd extend this login test for additional validation."**

**Your Answer:** "I'd add token format validation using regex patterns, response time assertions for performance, schema validation for the response structure, and negative test cases for invalid credentials. I might also add tests for edge cases like empty credentials, SQL injection attempts, or very long password strings to ensure robust security handling."

**Q6: "Explain your approach to handling multiple assertion types in a single test."**

**Your Answer:** "I layer different types of validations: HTTP status for basic success, response time for performance, field existence for required data, content validation for business logic, and schema validation for contract compliance. This multi-layered approach catches different types of issues at appropriate levels."

</details>

</details>

---

## UserDetailsAPITest

<details>
<summary><strong>📖 Authenticated API Testing Implementation</strong></summary>

**Location:** `src/test/java/com/api/tests/UserDetailsAPITest.java`

### 🎯 What's Actually Implemented

This test demonstrates authenticated API calls:

#### 1️⃣ Static Import Pattern

```java
import static com.api.utils.ConfigManager.getProperty;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
```

**Benefits Shown:**
- Direct access to `getProperty()` method
- Clean RestAssured syntax
- Focused import of specific matchers

#### 2️⃣ Authentication Header Creation

```java
Header authHeader = new Header("Authorization", 
    AuthTokenProvider.getAuthToken(Role.FRONT_DESK));
```

**Implementation Flow:**
1. `AuthTokenProvider.getAuthToken()` calls login API
2. Extracts JWT token from response
3. Wraps in `Authorization` header with Bearer format
4. Returns reusable Header object

#### 3️⃣ GET Request with Authentication

```java
Response response = given()
    .baseUri(getProperty("BASE_URI"))
    .and().contentType(ContentType.JSON)
    .and().header(authHeader)
    .log().body()
.when()
    .get("/userdetails")
.then()
    .log().ifValidationFails()
    .and().statusCode(200)
    .body("data.id", notNullValue())
    .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(
        "response-schema/UserDetailsResponseSchema.json"))
    .and().extract().response();
```

**Key Differences from Login Test:**
- Uses GET instead of POST
- Includes authentication header
- No request body needed
- Different endpoint (`/userdetails`)
- Different schema validation file

#### 4️⃣ Schema Validation Implementation

```java
.body(JsonSchemaValidator.matchesJsonSchemaInClasspath(
    "response-schema/UserDetailsResponseSchema.json"))
```

**What This Validates:**
- Response structure matches expected format
- Required fields are present
- Data types are correct
- Prevents breaking changes in API

#### 5️⃣ Debug Output

```java
System.out.println("UserDetailsAPITest");
System.out.println(response.asPrettyString());
```

**Runtime Information:**
- Test method identification
- Full response body for verification
- Helps troubleshoot API issues

### 🔗 Integration Points

This test demonstrates how our components work together:

1. **ConfigManager** → Provides base URI
2. **Role enum** → Supplies user credentials  
3. **AuthTokenProvider** → Generates authentication token
4. **RestAssured** → Executes HTTP requests
5. **TestNG** → Manages test execution
6. **JSON Schema** → Validates response structure

### 🎤 Interview Questions & Scenarios

<details>
<summary><strong>Scenario-Based Interview Questions</strong></summary>

**Q1: "How do you handle authenticated API endpoints in your test framework?"**

**Your Answer:** "I use a two-step approach: first, I call `AuthTokenProvider.getAuthToken(Roles.FD)` to get a valid JWT token, then I include it in the Authorization header for subsequent API calls. This ensures each test has fresh authentication and I can test different user roles by changing the role parameter."

**Q2: "What's your strategy for testing role-based access controls?"**

**Your Answer:** "I test with different roles using our Roles enum. For example, I might test that FD role can access user details but QC role cannot. I'd create separate test methods with different roles and validate both successful access for authorized roles and 403 Forbidden for unauthorized roles."

**Q3: "How do you handle token expiration during test execution?"**

**Your Answer:** "Each test method calls `getAuthToken()` independently, ensuring fresh tokens for every test. This eliminates token expiration issues during test runs. For performance optimization in large test suites, I could implement token caching with expiration checking, but the fresh token approach is more reliable."

**Q4: "Describe how you'd debug authentication failures in your tests."**

**Your Answer:** "I use comprehensive logging with `.log().ifValidationFails()` and debug output with `System.out.println(response.asPrettyString())`. I'd check if the issue is in token generation, token format, or API-side authentication. The logs show both the request headers and response details, making authentication issues easy to isolate."

**Q5: "How do you ensure your authenticated tests remain maintainable?"**

**Your Answer:** "I centralize authentication logic in AuthTokenProvider and use static imports for clean test code. The header creation is abstracted away, so test methods focus on business logic validation. If authentication requirements change, I only need to update the AuthTokenProvider, not every test method."

**Q6: "What would you do if user details started returning inconsistent data?"**

**Your Answer:** "I'd first check if it's a data issue or API issue using the debug output. I'd implement additional validations for data consistency, possibly adding database validation or API contract tests. I might also add retry logic for transient data issues while ensuring the core functionality test fails appropriately for real problems."

</details>

</details>

---

## 🎯 Current Implementation Summary

### ✅ What We've Successfully Built

| Component | Implementation Status | Key Features |
|---|---|---|
| **Configuration** | ✅ Complete | Environment-specific configs, fallback strategy |
| **Authentication** | ✅ Refactored | Switch case pattern, role-based tokens, centralized management |
| **Request Data** | ✅ Complete | POJO serialization, type-safe structures |
| **API Testing** | ✅ Enhanced | BDD pattern, multiple assertion types, comprehensive validation |
| **Schema Validation** | ✅ Complete | Contract testing, structure verification |
| **Error Handling** | ✅ Complete | Graceful fallbacks, meaningful errors |
| **Security Testing** | ✅ Implemented | Authentication validation, token verification |
| **Performance Testing** | ✅ Added | Response time assertions, timeout validation |

### 📊 Test Coverage Overview

| Test Class | Endpoints Covered | Test Scenarios | Key Validations |
|---|---|---|---|
| **LoginAPITest** | `POST /login` | Basic authentication | Status, token extraction, POJO usage |
| **UserDetailsAPITest** | `GET /userDetails` | Authenticated requests | Auth headers, schema validation, debug output |
| **CountAPITest** | `GET /dashboard/count` | Positive, negative, security | Performance, schema, authentication, error handling |
| **MasterAPITest** | `POST /master` | Master data retrieval | Nested objects, array validation, performance |

### 🎓 Concepts Demonstrated

1. **Static Initialization** - One-time configuration loading with environment detection
2. **Simple Enum Constants** - Type-safe role definitions (FD, SUP, QC, ENG)
3. **Switch Case Pattern** - Role-based credential mapping in AuthTokenProvider
4. **POJO Serialization** - Automatic JSON conversion for request/response
5. **BDD Testing Pattern** - Given-When-Then syntax for readable tests
6. **Multiple Validation Types** - Status, performance, schema, security testing
7. **Static Imports** - Clean, readable test code without class prefixes
8. **Negative Testing** - Security boundary testing and error validation
9. **Schema Validation** - JSON contract testing for API compliance
10. **Authentication Integration** - Role-based JWT token management
11. **Environment Management** - Multi-environment configuration support
12. **Performance Assertions** - Response time validation and timeout testing

### 🚀 Advanced Patterns Implemented

- **Comprehensive Error Handling** - 401, 500, malformed token scenarios
- **Deep Object Validation** - Nested JSON path assertions (`data.mst_oem[0].id`)
- **Array Validation** - Every item validation with Hamcrest matchers
- **Performance Boundaries** - Different time limits per endpoint (200ms vs 1500ms)
- **Test Control** - Selective test execution with `enabled` parameter
- **Multiple HTTP Methods** - GET and POST endpoint testing
- **Security Validation** - Missing auth headers and invalid token testing
5. **BDD Testing** - Given-When-Then structure
6. **Utility Classes** - Centralized common functionality
7. **Hamcrest Matchers** - Enhanced assertion validation
8. **Configuration Management** - Environment-specific settings
9. **Dashboard API Testing** - GET endpoint with authentication

### 🚀 Current Test Coverage

| Test Class | Endpoint | Purpose | Authentication |
|---|---|---|---|
| **LoginAPITest** | `POST /login` | Basic authentication testing | None (login endpoint) |
| **UserDetailsAPITest** | `GET /userdetails` | User profile data retrieval | Required (JWT token) |
| **CountAPITest** | `GET /dashboard/count` | Dashboard metrics display | Required (Roles.FD) |

### 🔄 Recent Refactoring

#### **AuthTokenProvider Enhancement:**
- **Before**: `role.getUserCredentials()` method dependency
- **After**: Switch case with explicit credential mapping
- **Benefit**: Clear, maintainable, centralized authentication logic

```java
// New switch case pattern
switch (role) {
    case FD -> user = new UserCredentials("iamfd", "password");
    case SUP -> user = new UserCredentials("iamsup", "password");
    // ... explicit mapping for each role
}
```

### 🚀 How to Run Our Implementation

```bash
# Run all tests with default (dev) config
mvn test

# Run tests in QA environment
mvn test -Denv=qa

# Run specific test class
mvn test -Dtest=LoginAPITest

# Run with verbose output
mvn test -Denv=qa -X
```

### 🚀 How to Run Our Implementation

```bash
# Run all tests with default (dev) config
mvn test

# Run tests in QA environment
mvn test -Denv=qa

# Run specific test class
mvn test -Dtest=LoginAPITest

# Run with verbose output
mvn test -Denv=qa -X
```

---

## 🔧 Troubleshooting Our Current Setup

<details>
<summary><strong>📖 Common Issues with Our Implementation</strong></summary>

### 🚨 Configuration Issues

| Problem | Symptoms | Solution |
|---|---|---|
| **Config not found** | `RuntimeException: could not find config.properties` | Check `src/test/resources/config/` folder exists |
| **Wrong environment** | Tests using wrong BASE_URI | Verify `mvn test -Denv=qa` parameter |
| **Properties not loading** | `NullPointerException` on getProperty | Ensure properties file has correct format |

### 🔐 Authentication Problems

| Problem | Symptoms | Our Solution |
|---|---|---|
| **401 Unauthorized** | Login fails in AuthTokenProvider | Check credentials in Role enum constants |
| **Token format error** | JSON path extraction fails | Verify response structure matches `data.token` |
| **Base URI wrong** | Connection refused | Check BASE_URI in config properties |

### 📋 Test Execution Issues

| Problem | Symptoms | Fix in Our Code |
|---|---|---|
| **Tests not found** | TestNG can't find test methods | Ensure `@Test` annotation present |
| **Schema validation fails** | JSON schema mismatch | Check schema files in `response-schema/` folder |
| **Maven compilation error** | Build fails | Run `mvn clean compile` first |

### 🛠️ Quick Debug Commands

```bash
# Verify configuration loading
mvn test -Dtest=ConfigManager#main -Denv=qa

# Test authentication separately  
mvn test -Dtest=AuthTokenProvider#main

# Run with debug output
mvn test -X -Dtest=LoginAPITest
```

</details>

---

## 📚 What's Next for Our Framework?

### 🎯 Immediate Improvements We Could Add

1. **Error Handling**: Add try-catch in AuthTokenProvider
2. **Logging**: Implement proper logging framework  
3. **Test Data**: External test data files (CSV/JSON)
4. **Parallel Execution**: TestNG parallel configuration
5. **Reporting**: Custom TestNG listeners

### 🚀 Advanced Features We Could Implement

1. **Database Validation**: JDBC integration for data verification
2. **Mock Services**: WireMock for service virtualization  
3. **Performance Testing**: JMeter integration
4. **CI/CD Integration**: Jenkins/GitHub Actions pipeline
5. **Docker Support**: Containerized test execution

---

<div align="center">

**🎉 Current Implementation Complete! 🎉**

_Our framework demonstrates core API testing concepts with working, production-ready code._

**Files Implemented:** 7 • **Concepts Covered:** 9 • **Test Methods:** 3 • **APIs Tested:** 3

### 🆕 **Latest Updates:**
- ✅ **Refactored AuthTokenProvider** - Switch case pattern with Roles enum
- ✅ **Added CountAPITest** - Dashboard endpoint testing with authentication  
- ✅ **Enhanced Test Coverage** - Login, UserDetails, and Dashboard APIs
- ✅ **Type-Safe Roles** - Simple enum constants (FD, SUP, QC, ENG)

</div>

---

<div align="center">

**Made with ❤️ for Learning API Testing**

_Implementation Notes - September 29, 2025_

</div>
