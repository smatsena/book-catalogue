# 📚 Book Catalogue System  
*A modular Spring Boot application demonstrating clean architecture, service decoupling, and secure CRUD operations.*

---

## 🧭 Overview  

The **Book Catalogue System** is a two-service solution designed to manage a collection of books through a RESTful backend and a lightweight web interface.  

It’s structured to highlight best practices in **service separation**, **secure API design**, and **Spring Boot application architecture**.

### Services  
1. **Management Service** – A Spring Boot REST API exposing CRUD operations backed by an H2 in-memory database.  
2. **Web Service** – A JSP-based frontend consuming the API via an internal HTTP client.

---

## 🧰 Postman Collection

You can test all API endpoints using the ready-to-import Postman collection below:

👉 **[Book Catalogue API Collection (Postman)]**

(https://smatsena-63b4ef1d-7877888.postman.co/workspace/Sarah's-Workspace~8564e7a7-599b-4a66-8f9f-c361c05dbd9a/collection/49653187-bc0e1581-79dc-4611-ae84-7c7c645c81e3?action=share&creator=49653187)

This collection includes:
- Preconfigured authentication (Basic Auth)
- Sample payloads for all CRUD endpoints
- Environment variables for `base_url`, `username`, and `password`

## 🏗️ Architecture  

```

┌──────────────────────────┐        HTTP (REST)        ┌──────────────────────────┐
│        Web Service       │ ───────────────────────▶  │     Management Service    │
│  Port 8082 (Frontend)    │                          │  Port 8081 (Backend API)  │
│  JSP Views + Controllers │                          │  JPA + H2 + Validation    │
└──────────────────────────┘                          └──────────────────────────┘
│
▼
┌──────────────────────┐
│ H2 In-Memory Database │
└──────────────────────┘

```

Each service is packaged as an executable JAR, allowing them to run independently or together via helper scripts.

---

## ⚙️ Technology Stack  

| Category | Technology |
|-----------|-------------|
| Framework | Spring Boot 2.7.18 |
| Language | Java 8 |
| Build Tool | Maven |
| Database | H2 (in-memory) |
| Frontend | JSP (JavaServer Pages) |
| Security | Spring Security (Basic Auth) |
| ORM | Spring Data JPA / Hibernate |
| Testing | JUnit 5 / MockMvc |

---

## 🧩 Project Structure  

```

book-catalogue/
├── management-service/           # REST API backend
│   ├── config/                   # Security + App config
│   ├── controller/               # REST endpoints
│   ├── dto/                      # Data transfer objects
│   ├── exception/                # Centralized error handling
│   ├── model/                    # JPA entities
│   ├── repository/               # Data persistence
│   └── service/                  # Business logic layer
│
├── web-service/                  # Frontend (JSP)
│   ├── config/                   # JAX-RS Client setup
│   ├── controller/               # MVC controllers
│   ├── dto/                      # API communication objects
│   └── WEB-INF/views/            # JSP pages
│
└── pom.xml                       # Parent POM

````

---

## 🚀 Getting Started  

### **Prerequisites**
- Java 8+
- Maven 3.6+

---

### **Build**

```bash
# Build entire project
mvn clean install

# Build a specific service
cd management-service
mvn clean package
````

---

### **Run**

#### 🪟 Windows

```bash
run-services.bat
```

The scripts will:

1. Build both services
2. Start **Management Service** on `:8081`
3. Start **Web Service** on `:8082`

---

### **Access**

| Service    | URL                                                                  |
| ---------- | -------------------------------------------------------------------- |
| Web UI     | [http://localhost:8082/books](http://localhost:8082/books)           |
| REST API   | [http://localhost:8081/api/books](http://localhost:8081/api/books)   |
| H2 Console | [http://localhost:8081/h2-console](http://localhost:8081/h2-console) |

**H2 Login**

* JDBC URL: `jdbc:h2:mem:catalogue`
* Username: `admin`
* Password: `admin123`

---

## 🔐 Authentication

| Role          | Username    | Password    | Permissions                          |
| ------------- | ----------- | ----------- | ------------------------------------ |
| **Admin**     | `admin`     | `admin123`  | Full CRUD (GET, POST, PATCH, DELETE) |
| **Librarian** | `librarian` | `b00k-rock` | Read + Create (GET, POST)            |

---

## 🧠 API Reference

**Base URL:** `http://localhost:8081/api/books`

| Method   | Endpoint              | Description      | Auth             |
| -------- | --------------------- | ---------------- | ---------------- |
| `GET`    | `/all`                | List all books   | Admin, Librarian |
| `GET`    | `/?isbn={isbn}`       | Get book by ISBN | All              |
| `GET`    | `/search?name={name}` | Search by name   | All              |
| `POST`   | `/`                   | Create book      | All              |
| `PATCH`  | `/{isbn}`             | Update book      | Admin only       |
| `DELETE` | `/{isbn}`             | Delete book      | Admin only       |

📘 **Note:** ISBNs are auto-generated server-side; clients cannot override them.

---

## 🗃️ Database Schema

| Column         | Type          | Constraints                 |
| -------------- | ------------- | --------------------------- |
| `id`           | BIGINT        | Primary Key, Auto-Increment |
| `isbn`         | VARCHAR(13)   | Unique, Not Null            |
| `name`         | VARCHAR(255)  | Not Null                    |
| `author`       | VARCHAR(255)  | Not Null                    |
| `publish_date` | DATE          | Not Null                    |
| `price`        | DECIMAL(10,2) | Not Null                    |
| `book_type`    | VARCHAR(16)   | Not Null                    |

**Unique Indexes**

* ISBN
* Combination (name, author, publish_date)

---

## 🎯 Key Features

✅ CRUD operations
✅ Server-side ISBN generation
✅ Search by name/author
✅ Role-based access control
✅ Duplicate-entry prevention
✅ Form-based JSP UI
✅ RESTful API with proper status codes
✅ Centralized exception handling
✅ Input validation (`javax.validation`)
✅ Pre-loaded demo data

---

## 🧪 Testing

```bash
# Run all tests
mvn test

# Or run for a single module
cd management-service
mvn test
```

Includes basic **MockMvc** integration tests and service-level unit tests.

---

## 🧰 Configuration Summary

| Service    | File              | Port | Key Configs                                |
| ---------- | ----------------- | ---- | ------------------------------------------ |
| Management | `application.yml` | 8081 | H2 DB, Spring Security, JPA                |
| Web        | `application.yml` | 8082 | Base URL for Management API, Thymeleaf/JSP |

---

## 🧩 Development Notes

* H2 DB resets on restart (in-memory).
* ISBN generation uses pseudo-random 13-digit sequences.
* Duplicate detection based on (name + author + publish_date).
* PATCH endpoints allow partial updates.
* The Web Service internally authenticates against the Management API.

---

## 🚀 Future Enhancements

* Docker Compose support
* Swagger UI (API Docs)
* PostgreSQL persistence
* Spring Security JWT
* CI/CD pipeline (GitHub Actions)

---

## 👩‍💻 Author

**Sarah Matsena**
*Java Backend Engineer*

---

### 🏁 TL;DR for Reviewers

> **Run `./run.sh` or `start-all.bat` → visit `http://localhost:8082/books` → log in → CRUD your way through the catalogue.**
> Everything else is exactly where you’d expect it in a clean, scalable Spring Boot project.

```
