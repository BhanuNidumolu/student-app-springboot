# 🎓 Student Management System (Spring Boot + Thymeleaf)

[![Java](https://img.shields.io/badge/Java-17%2B-orange?logo=openjdk)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-Templates-blue?logo=thymeleaf)](https://www.thymeleaf.org/)
[![MySQL](https://img.shields.io/badge/MySQL-Database-blue?logo=mysql)](https://www.mysql.com/)
[![AWS](https://img.shields.io/badge/AWS-Cloud-orange?logo=amazon-aws)](https://aws.amazon.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow?logo=opensourceinitiative)](LICENSE)

# Student Management REST API

This is a production-ready, secure REST API for a multi-role Student Management System. The project is built with Spring Boot and follows modern, enterprise-grade Java development practices, including full test coverage, containerization, and token-based authentication.

The core purpose of this API is to provide a single, secure source of truth that connects different user roles (Admins, Teachers, and Students) and automates their workflows, eliminating the need for disconnected, insecure data.

---

## 🚀 Key Features

* **Role-Based Access Control (RBAC):** Secure, multi-tenant API with three distinct roles:
    * `ROLE_ADMIN`: Full control over student/teacher records (CRUD).
    * `ROLE_TEACHER`: Can upload course materials, manage attendance.
    * `ROLE_STUDENT`: Can download materials, view/edit their own profile.
* **Stateless JWT Authentication:** Secured using JSON Web Tokens (JWT) for a modern, scalable, and stateless authentication standard.
* **Full API Documentation:** Automatically generated, interactive API documentation via **Swagger/OpenAPI**.
* **Production-Ready Error Handling:** A `GlobalExceptionHandler` provides clean, predictable JSON error responses for all API failures.
* **Database Migrations:** Database schema is fully managed and version-controlled using **Flyway**.
* **Full Test Coverage:**
    * **Unit Tests (JUnit & Mockito):** Service-layer logic is fully tested in isolation.
    * **Integration Tests (Testcontainers):** The entire application is tested end-to-end against a *real*, containerized MySQL database.
* **Containerized:** Fully containerized with a `Dockerfile` for the application and a `docker-compose.yml` for one-command deployment of both the app and the database.

---

## 🛠️ Tech Stack

* **Framework:** Spring Boot 3
* **Security:** Spring Security 6 (with JWT Authentication)
* **Data:** Spring Data JPA (Hibernate)
* **Database:** MySQL
* **Migrations:** Flyway
* **API Docs:** SpringDoc (Swagger-UI)
* **Testing:** JUnit 5, Mockito, Testcontainers
* **Build:** Maven
* **Deployment:** Docker / Docker Compose
* **Utilities:** Lombok, `jakarta.validation`

---

## 🏁 Getting Started

You can run the application in two ways: locally with Maven or via Docker Compose (recommended).

### Prerequisites

* Java 17+
* Apache Maven 3.8+
* Docker & Docker Compose

---

### Method 1: Run with Docker Compose (Recommended)

This is the fastest and easiest way to get the entire application stack (API + Database) running in one command.

1.  **Build the application:**
    ```bash
    mvn clean install -DskipTests
    ```
    *(This builds the `.jar` file that Docker needs. We skip tests for a faster build, but you can run them with `mvn clean test`)*

2.  **Run Docker Compose:**
    ```bash
    docker-compose up --build
    ```

The API will be running at `http://localhost:8080`.

---

### Method 2: Run Locally (with IDE or Maven)

1.  **Start a MySQL Database:**
    Ensure you have a local MySQL instance running. You can use Docker for this:
    ```bash
    docker run --name mysql-db -p 3306:3306 -e MYSQL_ROOT_PASSWORD=yourpassword -e MYSQL_DATABASE=userdb -d mysql:8.0
    ```

2.  **Configure the Application:**
    In `src/main/resources/application.yaml`, make sure the database credentials match your local instance.
    ```yaml
    spring:
      datasource:
        username: root
        password: yourpassword # Or set the DB_PASSWORD environment variable
    ```

3.  **Run the Application:**
    ```bash
    mvn spring-boot:run
    ```

The API will be running at `http://localhost:8080`.

---

## 🧪 Testing the API

### API Documentation

Once the application is running, you can access the full, interactive API documentation here:

➡️ **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

You can test all endpoints, including file uploads, directly from this page.

### Test Credentials

The `CommandLineRunner` automatically seeds the database with three test users.
*(Note: This seeder only runs in the main application, not during tests).*

| Role | Username | Password |
| :--- | :--- | :--- |
| **Admin** | `admin` | `admin123` |
| **Teacher**| `teacher`| `teacher123`|
| **Student**| `student`| `student123`|

### Using Postman / Swagger UI

1.  **Login (Get Token):**
    First, you must authenticate to get your JWT.
    * **Endpoint:** `POST /api/auth/login`
    * **Body (JSON):**
        ```json
        {
          "username": "admin",
          "password": "admin123"
        }
        ```
    * **Response:** You will get a JSON object with an `accessToken`.

2.  **Use the Token:**
    Copy the `accessToken` value. For all other requests, add an `Authorization` header:
    * **Key:** `Authorization`
    * **Value:** `Bearer <your_token_here>`

    *(In Swagger UI, click the "Authorize" button and paste `Bearer <token>` into the `jwtAuth` field).*

---

## 🔐 API Endpoints & Security

| Endpoint | HTTP Method | Description | Allowed Roles |
| :--- | :--- | :--- | :--- |
| `/api/auth/login` | `POST` | Get a JWT access token. | **Public** |
| `/api/students` | `POST` | Create a new student. | **Admin** |
| `/api/students` | `GET` | Get a list of all students. | **Admin, Teacher, Student** |
| `/api/students/{id}` | `GET` | Get a single student. | **Admin, Teacher, Student** |
| `/api/students/{id}` | `PUT` | Update a student's info. | **Admin** |
| `/api/students/{id}` | `DELETE` | Delete a student. | **Admin** |
| `/api/materials/upload`| `POST` | Upload a new course document. | **Teacher** |
| `/api/materials` | `GET` | List all course documents. | **Student, Teacher, Admin** |
| `/api/materials/download/{id}` | `GET` | Download a course document. | **Student, Teacher, Admin** |
| `/api/profile/me` | `PUT` | Update your *own* profile. | **Student** |
📜 License
This project is licensed under the MIT License.

⭐ Acknowledgements
Built with ❤️ using Spring Boot, Thymeleaf, MySQL, and AWS EC2.

📂 Additional Notes
Ensure that port 8080 (or 80) is open in the EC2 instance's security group.

Consider using a reverse proxy like Nginx for production setups.

For database security, restrict access and use environment variables for credentials.
