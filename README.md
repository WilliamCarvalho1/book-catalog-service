## Technologies

- Java 17
- Spring Boot 3 (Web, Validation)
- Spring Security with JWT (JJWT 0.11.x)
- Spring Data (via custom ports/adapters)
- PostgreSQL (via Docker Compose)
- Flyway (database migrations)
- springdoc-openapi (Swagger UI)
- JUnit 5, Mockito, AssertJ (tests)
- Maven (build tool)

## How to Run

1. **Clone the repository**
  - `git clone https://github.com/WilliamCarvalho1/book-catalog-service.git`
  - `cd book-catalog-service`

2. **Start dependencies (PostgreSQL) with Docker Compose**
  - `docker-compose up -d`

3. **Build the application**
  - `mvn clean package`

4. **Run the application**
  - `mvn spring-boot:run`
  - Or run the generated JAR:
    - `java -jar target/book-catalog-0.0.1-SNAPSHOT.jar`

5. **Access the API documentation (Swagger UI)**
  - Open `http://localhost:8080/swagger-ui.html`

6. **Authentication**
  - First call `/api/auth/login` with valid credentials to obtain a JWT.
  - Use the returned token as `Authorization: Bearer <token>` in subsequent requests.

# Hexagonal Architecture
Also known as the Ports and Adapters pattern, is a software design approach that emphasizes loose coupling between the core business logic and external concerns like databases, user interfaces, and other systems. This separation allows for easier testing, maintenance, and modification of the application's core logic without impacting external components, and vice versa.

It can be used, and often is, with both Domain-Driven Design (DDD) and SOLID principles. In fact, Hexagonal Architecture relies on the Dependency Inversion Principle.

### Key concepts
- **Core Logic (Application):**
  This is where the business rules and logic of the application reside.
- **Ports:**
  These are interfaces that define how the application interacts with the outside world. They act as contracts between the core logic and external components.
- **Adapters:**
  These are implementations of the ports, handling the specific details of interacting with external systems. For example, a database adapter might implement the port for data persistence, while a UI adapter would handle user input and display.

### DDD and Hexagonal Architecture
DDD defines the domain model and business rules while Hexagonal architecture enforces the separation of concerns highlighted by DDD by isolating domain logic from external dependencies, therefore protecting and structuring that domain logic.

### SOLID and Hexagonal Architecture
Hexagonal architecture relies heavily on the Dependency Inversion Principle (the "D" in SOLID), which is fundamental to its design. By defining ports and adapters, it promotes loose coupling and makes it easier to apply other SOLID principles within the domain and infrastructure layers.

### Clean Architecture vs Hexagonal Architecture
- Clean Architecture essentially provides a blueprint for the "inside the hexagon" in Hexagonal Architecture.
- The core concepts of ports and adapters are present in Clean Architecture as well, but it adds more structure to the core itself.
- Think of Hexagonal Architecture as a general approach to decoupling, while Clean Architecture is a more specific implementation that builds on that approach.

### Benefits of Hexagonal Architecture
- **Testability:**
The core logic can be tested in isolation, without needing to set up complex external dependencies like databases or UI frameworks.
- **Maintainability:**
Changes to external systems (e.g., a new database version, a different UI framework) can be made without impacting the core logic.
- **Flexibility:**
Different technologies can be used for different parts of the application without affecting other parts, allowing for easier technology upgrades or migrations.
- **Reduced Technology Lock-in:**
The core logic is not tied to specific technologies, making it easier to switch technologies in the future.

### Hexagonal Architecture is particularly beneficial for
- **Large-scale applications** with multiple external dependencies.
- **Mature projects** that need long-term maintainability.
- **Systems requiring flexibility** to swap external components like databases or messaging services.

### Challenges and Considerations
- **Added Complexity:** Introducing ports and adapters increases the codebase, requiring a steeper learning curve.
- **Development Overhead:** Setting up and maintaining multiple abstractions requires extra effort.
- **Performance Considerations:** Increased abstraction layers may introduce latency, requiring optimization.

In essence, Hexagonal Architecture promotes a design where the core business logic is isolated and can be interacted with through well-defined interfaces (ports) by various external components (adapters).

Hexagonal Architecture is not the silver bullet for all applications. It involves a certain level of complexity that, when properly implemented and paired with other methodologies, like Domain-Driven Design, can ensure an application’s long term stability and extensibility, bringing a great deal of value to the system.
But it might cause a lot of headaches if not followed accordingly.

## Architectural Diagram
![HexArchDiagram.png](src/main/resources/img/HexArchDiagram.png)
## Ports and Adapters interactions with the Application Diagram
![HexArchPortsAndAdaptersFlows.png](src/main/resources/img/HexArchPortsAndAdaptersFlows.png)

## Links
- [Hexagonal Architecture](https://romanglushach.medium.com/hexagonal-architecture-the-secret-to-scalable-and-maintainable-code-for-modern-software-d345fdb47347)
- [Another Hexagonal Architecture link](https://scalastic.io/en/hexagonal-architecture-domain/)
- [SOLID](https://www.digitalocean.com/community/conceptual-articles/s-o-l-i-d-the-first-five-principles-of-object-oriented-design)
- [DDD](https://dev.to/lovestaco/domain-driven-design-3i2j)