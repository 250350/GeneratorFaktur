# Invoice Generator

A full-stack web application for generating, managing, and processing professional invoices with integrated payment support via Stripe.

---

## 🚀 Features

* Create and manage invoices with multiple line items
* Automatic calculation of VAT, IRPF, and totals
* Generate PDF invoices from HTML templates
* Stripe integration for payments and seller onboarding (Stripe Connect)
* PostgreSQL database for persistent storage
* Tax ID validation (NIF/NIE/CIF)
* Multi-language invoice templates (e.g. Spanish invoice format)
* Environment-based configuration for secure deployment
* Cloud deployment ready (Render + Neon PostgreSQL)

---

## 🛠️ Tech Stack

### Backend

* Java 17
* Spring Boot
* Spring MVC
* Spring Data JPA (Hibernate)
* REST API
* Thymeleaf

### Frontend

* HTML5
* CSS3
* JavaScript

### Database

* PostgreSQL (Neon)

### Integrations

* Stripe API
* Stripe Connect (Express onboarding)

### Tools & Build

* Maven
* Git & GitHub
* OpenHTMLtoPDF (PDF generation)

### Deployment

* Render (backend hosting)
* Neon (managed PostgreSQL database)

---

## ⚙️ Configuration

The application uses environment variables for sensitive data.

Create the following variables:

```env
DB_URL=jdbc:postgresql://...
DB_USERNAME=your_username
DB_PASSWORD=your_password

STRIPE_SECRET_KEY=sk_test_...
STRIPE_WEBHOOK_SECRET_KEY=whsec_...
PORT=8080
```

---

## 🧪 Running the Project Locally

1. Clone the repository:

```bash
git clone https://github.com/250350/GeneratorFaktur.git
```

2. Set environment variables (IntelliJ / system / .env equivalent)

3. Run the application:

```bash
mvn spring-boot:run
```

4. Open:

```
http://localhost:8080
```

---

## 📦 Main Functional Flow

1. User creates an invoice
2. Invoice is stored in PostgreSQL
3. PDF version can be generated and downloaded
4. Seller can complete Stripe onboarding
5. Payments are processed via Stripe Connect
6. Invoice status is updated in database

---

## 🔐 Security Notes

* No secrets are stored in source code
* All credentials are injected via environment variables
* Database access is protected via managed Neon credentials
* Stripe keys are stored securely in deployment environment

---

## 📌 Future Improvements

* User authentication (Spring Security)
* Invoice editing & versioning
* Email invoice sending
* Dashboard analytics
* Role-based access control

---

## 👨‍💻 Author

Built by a Java backend developer focused on Spring Boot, APIs, and full-stack integrations.
