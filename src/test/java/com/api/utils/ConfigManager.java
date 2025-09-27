/*
 * Tutor notes - ConfigManager.java
 *
 * What this class does (high level):
 * - Demonstrates locating and loading a properties file from the project filesystem
 * - Prints the computed path and the BASE_URI property
 *
 * Core Java concepts used:
 * - main method: program entry point (public static void main)
 * - System properties: System.getProperty("user.dir") for current working dir
 * - java.io.File, FileReader for file I/O
 * - java.util.Properties to load key/value configuration
 * - Exception handling with try/catch and throws declaration
 *
 * Pitfalls and improvements:
 * - user.dir depends on how the JVM is launched (IDE, Maven, CI). Prefer classpath loading for test resources.
 * - FileReader uses platform default charset; prefer specifying UTF-8 via InputStreamReader.
 * - The FileReader is not closed in the current code; use try-with-resources to avoid leaks.
 * - Prefer using ClassLoader.getResourceAsStream("config/config.properties") for src/test/resources files.
 * - Use an explicit maven-compiler-plugin <release> (e.g. 17) to match your JDK.
 * - Add an SLF4J binder (logback-classic or slf4j-simple) to avoid the SLF4J NOP warning during tests.
 *
 * Minimal recommended pattern (conceptual):
 * try (InputStream in = Thread.currentThread().getContextClassLoader()
 *                 .getResourceAsStream("config/config.properties")) {
 *     Properties prop = new Properties();
 *     prop.load(new InputStreamReader(in, StandardCharsets.UTF_8));
 *     String base = prop.getProperty("BASE_URI", "http://localhost");
 * }
 *
 * When to use which approach:
 * - ClassLoader.getResourceAsStream: resources packaged on classpath (recommended for src/test/resources)
 * - java.nio.file.Path / Files: when reading files from disk outside the classpath
 *
 * Interview questions (short answers):
 * 1) How do you load test configuration in Java and why prefer classpath?
 *    - Use ClassLoader.getResourceAsStream for files in src/test/resources; it's portable and works when tests are run from IDE, Maven or CI because resources are on the classpath.
 *
 * 2) Why use try-with-resources and InputStreamReader with UTF-8?
 *    - try-with-resources ensures streams are closed (no resource leaks). Specify UTF-8 explicitly to avoid platform-dependent encoding issues.
 *
 * 3) Why make properties static and load them in a static initializer?
 *    - Static loading caches configuration once per JVM, reducing I/O. But be cautious: static init runs at class load time which can make tests harder to isolate; consider lazy-loading or injectable configuration for test isolation.
 *
 * 4) How do you handle missing or malformed properties at runtime?
 *    - Provide sensible defaults via Properties.getProperty(key, default). Fail-fast for required properties by throwing a clear exception during startup or test setup.
 *
 * 5) How to override properties for CI or different environments?
 *    - Use system properties (-Dkey=value), environment variables, or profile-specific resource files. Merge with config precedence: CLI > env > file > defaults.
 *
 * 6) How to make this thread-safe and test-friendly?
 *    - Properties is thread-safe for simple get operations. For greater control, keep configuration immutable after load and expose unmodifiable wrappers or use dependency injection so tests can supply alternate configs.
 *
 * 7) How would you unit-test this class?
 *    - Use temporary classpath resources or set system property to point to a temp file. For static initializers consider adding package-private hooks or refactor to an instance-based loader to inject streams in tests.
 *
 * 8) What logging considerations exist?
 *    - Avoid System.out/err; use SLF4J with a binder. Add meaningful log levels (info/warn/error) and include enough context to debug missing config.
 *
 * 9) How to validate configuration values?
 *    - Validate values after loading (URL format, numeric ranges). Fail early with clear messages to aid debugging.
 *
 * 10) What are alternatives for configuration management in larger projects?
 *    - Use libraries: Typesafe Config, Apache Commons Configuration, Spring Boot's @ConfigurationProperties, or environment-specific property resolvers.
 *
 * Next actions I can take for you (pick one):
 * - Edit this class to use try-with-resources and classpath loading (I can apply the change)
 * - Add UTF-8 reading and close resources properly
 * - Update pom.xml maven-compiler-plugin to <release>17
 * - Add SLF4J binder dependency to pom.xml
 */

package com.api.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    // Load properties once at class initialization from test classpath:
    // Supports environment-specific configs via system property: -Denv=qa
    private static final Properties PROPS = new Properties();

    static {
        // Get environment from system property (default to "dev" if not specified)
        String env = System.getProperty("env", "dev");
        String configFile = "config/config." + env + ".properties";

        // Try environment-specific config first
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(configFile);

        // Fallback to default config.properties if environment-specific not found
        if (in == null) {
            System.out.println("ConfigManager: Environment-specific config '" + configFile
                    + "' not found, trying default config.properties");
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/config.properties");
        } else {
            System.out.println("ConfigManager: Loading configuration from " + configFile);
        }

        if (in == null) {
            throw new RuntimeException(
                    "ConfigManager: could not find config.properties or " + configFile + " in classpath");
        }

        try {
            PROPS.load(in);
            in.close();
        } catch (IOException e) {
            System.err.println(
                    "ConfigManager: could not load configuration file: " + e.getMessage());
            throw new RuntimeException("ConfigManager: Failed to load configuration", e);
        }
    }

    // Prevent instantiation
    private ConfigManager() {
    }

    public static String getProperty(String key) {
        return PROPS.getProperty(key);
    }

    // Keep a simple main for manual debugging if needed
    public static void main(String[] args) {
        String env = System.getProperty("env", "dev");
        System.out.println("Current environment: " + env);
        System.out.println("Loaded config properties: ");
        System.out.println("BASE_URI=" + getProperty("BASE_URI"));
    }

}