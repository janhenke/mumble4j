# Mumble4J Development Guidelines

This project is a Java-based implementation of the Mumble/Murmur VoIP protocol. It follows a multi-module Maven structure.

## Project Structure

- **`core`**: Contains the core protocol implementation, message definitions (Protobuf), and low-level networking.
  - Targets **Java 21**.
  - Protobuf definitions are located in `core/src/main/protobuf`.
- **`server`**: A Quarkus-based server implementation.
  - Targets **Java 25**.
  - Uses Hibernate ORM, PostgreSQL, and Quarkus extensions for observability and containerization.
  - Configuration is in `server/src/main/resources/application.yaml`.

## Tech Stack

- **Languages**: Java 21/25, Protocol Buffers.
- **Frameworks**: [Quarkus](https://quarkus.io/), Hibernate ORM.
- **Database**: PostgreSQL (managed via Liquibase).
- **Testing**: JUnit 5, AssertJ, Mockito.
- **Build Tool**: Maven (`mvnw` provided).

## Development Workflow

### Building the Project

Use the Maven wrapper to build the entire project:
```bash
./mvnw clean install
```

To build a specific module:
```bash
./mvnw clean install -pl core
```

### Running Tests

Run all unit tests:
```bash
./mvnw test
```

For integration tests in the `server` module:
```bash
./mvnw verify -pl server
```

### Running the Server

Run the server in Quarkus development mode:
```bash
./mvnw quarkus:dev -pl server
```

### Protocol Changes

If you modify `.proto` files in `core/src/main/protobuf`, rebuild the `core` module to regenerate Java classes:
```bash
./mvnw generate-sources -pl core
```

## Coding Standards

- **Java Version**: Use Java 21 for `core` and Java 25 for `server`. Prefer modern Java features (records, pattern matching).
- **Null Safety**: Use `@NotNull` from `org.jetbrains.annotations` for parameters and return types where applicable.
- **Code Style**: 
  - Follow existing indentation (tabs) and bracing style (new line for opening brace).
  - Use `final` for local variables and parameters where they are not reassigned.
  - Comments: Use `///` for single-line documentation or standard Javadoc `/** ... */`.
- **Logging**: Use SLF4J API for logging.
- **Naming**: 
  - Standard Java naming conventions (PascalCase for classes, camelCase for methods/variables).
  - Protobuf messages should follow PascalCase.

## Containerization

The server is configured to use **JIB** for container image creation. 
To build a container image:
```bash
./mvnw install -Dquarkus.container-image.build=true -pl server
```
