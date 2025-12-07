# Specar Application 🚀

This project is a Spring Boot-based application showcasing various features like user management, feedback handling, and secure authentication using JWT. It provides a RESTful API for both normal users and administrative users, with functionalities ranging from user registration and login to feedback submission and user data management. The application leverages Spring Security, Redis for caching and session management, and MyBatis-Plus for efficient database interactions.

## ✨ Key Features

- **User Authentication & Authorization:** Secure user authentication using JWT (JSON Web Tokens) and Spring Security.
- **User Management:** Comprehensive user management functionalities including registration, login (via phone number/password or verification code), profile updates, and data retrieval.
- **Feedback Handling:** Allows users to submit feedback and receive rewards (points), with admin functionalities to query and manage feedback.
- **RESTful API:** Provides a well-defined RESTful API for both user-facing and administrative operations.
- **Caching:** Utilizes Redis for caching user data and session information, improving performance and scalability.
- **Database Integration:** Integrates with a relational database (likely MySQL or PostgreSQL) using MyBatis-Plus for efficient data access.
- **Admin Interface:** Includes administrative endpoints for user management and data querying.
- **Verification Code Handling:** Implements a verification code system for user registration and login.
- **Distributed Locking:** Employs Redisson for distributed locking to prevent race conditions during critical operations.
- **Interceptor-based Request Handling:** Uses Spring MVC interceptors for authentication, authorization, logging, and session management.

## 🛠️ Tech Stack

| Category      | Technology                      | Description                                                                                                |
|---------------|---------------------------------|------------------------------------------------------------------------------------------------------------|
| **Backend**   | Java ☕                         | Primary programming language                                                                               |
|               | Spring Boot 🍃                  | Framework for building the application                                                                     |
|               | Spring Security 🛡️              | Framework for securing the application                                                                     |
|               | Spring Data Redis 💾            | For interacting with Redis                                                                                  |
|               | Spring MVC 🖼️                   | For building RESTful APIs                                                                                  |
|               | MyBatis-Plus ➕                | ORM framework for database interaction                                                                     |
|               | Lombok 🪡                       | Library to reduce boilerplate code                                                                         |
|               | Hutool 🧰                       | Java toolkit that provides a set of utility methods                                                         |
|               | Redisson 🔒                     | Distributed lock implementation                                                                            |
| **Database**  | (Likely MySQL or PostgreSQL) 🗄️ | Relational database for storing application data                                                             |
| **Cache**     | Redis ⚡                       | In-memory data store used for caching and session management                                                |
| **Security**  | JWT (JSON Web Tokens) 🔑       | For secure authentication and authorization                                                                |
|               | BCryptPasswordEncoder ⚙️       | For password hashing                                                                                       |
| **Build Tool**| Maven/Gradle 🏗️                | Dependency management and build automation                                                                 |
| **API Documentation** | Swagger 📝                   | For API documentation and testing                                                                        |
| **Logging**   | SLF4J 🪵                       | Logging facade                                                                                             |

## 📦 Getting Started

### Prerequisites

- Java Development Kit (JDK) 1.8 or higher
- Maven or Gradle
- Redis server
- (Likely) MySQL or PostgreSQL database

### Installation

1.  Clone the repository:

    ```bash
    git clone https://github.com/HarveyBlocks/Specar.git
    ```

2.  Navigate to the project directory:

    ```bash
    cd Specar
    ```

3.  Configure the database connection:

    - Update the `application.properties` or `application.yml` file with your database credentials.

4.  Configure Redis connection:

    - Update the `application.properties` or `application.yml` file with your Redis server details.

5.  Build the project using Maven or Gradle:

    ```bash
    # For Maven
    mvn clean install

    # For Gradle
    gradle clean build
    ```

### Running Locally

1.  Run the application:

    ```bash
    # For Maven
    mvn spring-boot:run

    # For Gradle
    gradle bootRun
    ```

2.  Access the application in your browser at `http://localhost:8080` (or the port configured in your `application.properties` or `application.yml` file).

## 📂 Project Structure

```
📂 se-demo
├── 📂 src
│   ├── 📂 main
│   │   ├── 📂 java
│   │   │   ├── 📂 com
│   │   │   │   ├── 📂 harvey
│   │   │   │   │   ├── 📂 se
│   │   │   │   │   │   ├── 📂 config        # Configuration files (Spring Security, MVC)
│   │   │   │   │   │   │   ├── MvcConfig.java
│   │   │   │   │   │   │   ├── SecurityConfig.java
│   │   │   │   │   │   │   ├── ...
│   │   │   │   │   │   ├── 📂 controller    # REST controllers
│   │   │   │   │   │   │   ├── 📂 admin       # Admin controllers
│   │   │   │   │   │   │   │   ├── AdminUserController.java
│   │   │   │   │   │   │   ├── 📂 normal      # Normal user controllers
│   │   │   │   │   │   │   │   ├── UserController.java
│   │   │   │   │   │   │   │   ├── FeedbackController.java
│   │   │   │   │   │   ├── 📂 dao           # MyBatis mapper interfaces
│   │   │   │   │   │   │   ├── UserMapper.java
│   │   │   │   │   │   │   ├── FeedbackMapper.java
│   │   │   │   │   │   ├── 📂 exception     # Custom exception classes
│   │   │   │   │   │   │   ├── BadRequestException.java
│   │   │   │   │   │   │   ├── ...
│   │   │   │   │   │   ├── 📂 interceptor   # Interceptors for request handling
│   │   │   │   │   │   │   ├── ExpireInterceptor.java
│   │   │   │   │   │   │   ├── ...
│   │   │   │   │   │   ├── 📂 pojo          # Plain Old Java Objects
│   │   │   │   │   │   │   ├── 📂 dto       # Data Transfer Objects
│   │   │   │   │   │   │   │   ├── UserDto.java
│   │   │   │   │   │   │   │   ├── ...
│   │   │   │   │   │   │   ├── 📂 entity    # Database entity classes
│   │   │   │   │   │   │   │   ├── User.java
│   │   │   │   │   │   │   │   ├── Feedback.java
│   │   │   │   │   │   │   ├── 📂 vo       # Value Objects
│   │   │   │   │   │   │   │   ├── Result.java
│   │   │   │   │   │   ├── 📂 properties    # Configuration properties
│   │   │   │   │   │   │   ├── JwtProperties.java
│   │   │   │   │   │   │   ├── ConstantsProperties.java
│   │   │   │   │   │   ├── 📂 service       # Service interfaces
│   │   │   │   │   │   │   ├── UserService.java
│   │   │   │   │   │   │   ├── FeedbackService.java
│   │   │   │   │   │   ├── 📂 service.impl  # Service implementations
│   │   │   │   │   │   │   ├── UserServiceImpl.java
│   │   │   │   │   │   │   ├── FeedbackServiceImpl.java
│   │   │   │   │   │   ├── 📂 util          # Utility classes
│   │   │   │   │   │   │   ├── JwtTool.java
│   │   │   │   │   │   │   ├── RedisConstants.java
│   │   │   │   │   │   │   ├── UserHolder.java
│   │   │   │   │   │   ├── SeDemoApplication.java # Main application class
│   │   ├── 📂 resources
│   │   │   ├── application.properties     # Application configuration
│   │   │   ├── mapper                   # MyBatis mapper XML files
│   │   │   ├── static                   # Static resources (e.g., HTML, CSS, JavaScript)
│   │   │   ├── templates                # Thymeleaf templates
│   ├── 📂 test
│   │   ├── 📂 java
│   │   │   ├── 📂 com
│   │   │   │   ├── 📂 harvey
│   │   │   │   │   ├── 📂 se
│   │   │   │   │   │   ├── ...          # Test classes
├── pom.xml                           # Maven project file
├── build.gradle                      # Gradle project file (if using Gradle)
└── README.md                         # This file
```

## 📸 Screenshots

(Add screenshots of the application here)

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1.  Fork the repository.
2.  Create a new branch for your feature or bug fix.
3.  Make your changes and commit them with descriptive messages.
4.  Push your changes to your fork.
5.  Submit a pull request to the main repository.

## 📝 License

This project is licensed under the [APACHE License](LICENSE).

## 📬 Contact

[HarveyBlocks] - [HarveyBlocks@outlook.com]

## 💖 Thanks

Thank you for checking out this project! We hope it's helpful.
