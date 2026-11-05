# LUMINA — Luxury Event Vendor Marketplace
## B.Tech Semester 5 Integrated Assignment Capstone Project

> **"Where Every Detail Matters"**  
> Luxury Event Management Marketplace connecting discerning event hosts with verified, elite event service providers across India.

---

## 1. Executive Summary & Subject-Wise Assignment Mapping

This project combines five core subjects under an integrated startup formulation:

| Subject | Academic Requirement | Lumina Implementation Evidence |
| :--- | :--- | :--- |
| **Java Programming** | OOP (Inheritance, Polymorphism, Encapsulation), Custom Exceptions, Multithreading, File I/O Persistence, REST APIs | `com.lumina.model.*`, `User` abstract base class with polymorphic `HostUser` and `VendorUser`, `NotificationDispatcher` (daemon worker pool), `FileDataManager` JSON persistence, `LuminaApiServer`. |
| **E-Commerce** | Marketplace Workflow, Discovery, Booking, Escrow Payment (Sandbox), 10% Spread Model, Anti-Fraud | End-to-end booking simulator, 10% commission engine (`₹10k vendor fee + ₹1k Lumina spread = ₹11k host charge`), anti-fake review cryptographic linking, transparent zero-drip pricing. |
| **Software Testing** | Test Plan & Strategy, 30+ Documented Test Cases, BVA, Equivalence Partitioning, Decision Tables, Automated Runner, Defect Log | `LuminaTestSuite.java` running 30 automated test cases, live web test dashboard (`testing-dashboard.html`) showing pass rates, execution time, and defect resolutions. |
| **Cloud Computing** | Cloud Architecture, Docker Containerization, Multi-Stage Dockerfile, GitHub Actions CI/CD, Cost Modeling | Multi-stage `Dockerfile` (JDK 21 build -> JRE 21 slim runtime), `docker-compose.yml`, `.github/workflows/ci.yml`, 100/1k/10k monthly cost model. |
| **Startup Policies** | Lean Canvas, Business Model Canvas, SWOT, PESTEL, DPIIT Startup Recognition Roadmap, Financials | Comprehensive presentation and interactive documentation in `academic-hub.html`, Unit Economics, TAM/SAM/SOM market analysis. |

---

## 2. Founding Team & Role Allocation (Section 6 Compliance)

- **Yash Thakkar (242466):** Team Lead & Product Development (Project architecture, frontend integration, and system coordination)
- **Dhruvsinh Rathod (241204):** Java Backend Developer (Java 21 OOP Core, Multithreading, REST Microservice, and file persistence)
- **Harsh Chauhan (241163):** Testing & QA Lead (Test strategy, 30+ automated test cases, BVA, Equivalence Partitioning, and defect management)
- **Rajdeep Gadhvi (241167):** Cloud & DevOps Lead (Multi-stage Dockerfile, CI/CD pipeline, and cloud cost modeling)
- **Aryan Prajapati (241200):** E-Commerce & Business Lead (Marketplace workflow, 10% escrow spread model, Lean Canvas, and DPIIT readiness)
- **Vansh Patel (242428):** UI/UX & Documentation Lead (Luxury branding preservation, responsive layouts, and submission reports)

---

## 3. Technology Stack

- **Frontend:** Vanilla HTML5, CSS3 Custom Properties (Luxury Walnut & Sand theme), Responsive Grid/Flexbox, Native Asynchronous JavaScript (Fetch API).
- **Backend Core:** Java 21 LTS (Object-Oriented Architecture, Standard Library HTTP/REST Engine, Multi-threading Executor Pool).
- **Persistence:** Local JSON State Repository (`lumina_marketplace_data.json`) with auto-save & crash recovery.
- **DevOps & Cloud:** Docker, Alpine Linux, GitHub Actions CI/CD, RESTful APIs on port `8080`.

---

## 4. How to Run the Project Locally

### Step 1: Start the Java Backend Server
Ensure Java 21 is installed. In PowerShell or Command Prompt:

```powershell
cd "c:\Users\yasht\OneDrive\Desktop\new lumina"

# Compile all Java sources
javac -d backend-java/bin (Get-ChildItem -Path backend-java/src -Filter *.java -Recurse | Select-Object -ExpandProperty FullName)

# Start the REST API Server (runs on http://localhost:8080)
java -cp backend-java/bin com.lumina.server.LuminaApiServer
```

### Step 2: Open the Frontend
Simply double-click or open `index.html` in your browser (e.g. Chrome / Edge).
You can also launch:
- `host-dashboard.html`: Manage bookings, simulate new reservations, release escrow funds.
- `vendor-dashboard.html`: View vendor catalog, payouts, and anti-fraud verified reviews.
- `testing-dashboard.html`: Live automated runner for all 30 test cases.
- `academic-hub.html`: Full integrated assignment presentation and startup canvases.

---

## 5. How to Run Automated Software Tests

Execute the Java test runner directly from CLI:

```powershell
java -cp backend-java/bin com.lumina.test.LuminaTestSuite
```

Or open `testing-dashboard.html` in your web browser and click **"⚡ Run All 30 Tests Live"** to execute against the live Java REST API (`/api/tests/run`).

---

## 6. Docker Containerization

To build and run via Docker:

```bash
docker build -t lumina-marketplace .
docker run -p 8080:8080 lumina-marketplace
```

Or with Docker Compose:
```bash
docker compose up -d
```

---

## 7. Cloud Architecture & Cost Projections

```
[ User Browser / Mobile ]
         │
         ▼
[ Cloudflare / CDN (Static Assets & TLS 1.3) ]
         │
         ▼
[ AWS ALB / Google Cloud Load Balancer ]
         │
         ▼
[ Lumina Java 21 REST Engine (AWS ECS / Cloud Run Container) ]
         │
    ┌────┴────────────────────────┬───────────────────────┐
    ▼                             ▼                       ▼
[ Managed PostgreSQL ]   [ AWS S3 Object Bucket ]   [ CloudWatch / APM Logs ]
 (Bookings & Users)       (Vendor Portfolios)        (Health & Metrics)
```

- **100 Users (MVP):** ₹40 / month (Free tier compute & serverless DB)
- **1,000 Users (Growth):** ₹3,600 / month (App Runner + RDS db.t4g.micro)
- **10,000 Users (Scale):** ₹18,800 / month (Auto-scaled ECS + Multi-AZ RDS + CDN)

