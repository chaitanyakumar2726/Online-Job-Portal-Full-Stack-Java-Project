Online Job Portal – Full Stack Web Application

A modern full-stack Job Portal built using Java, Spring Boot, React/JavaScript, REST APIs, Oracle/MySQL, designed to streamline job searching, recruitment, and hiring workflow.
This project follows industry-level architecture, clean coding standards, and real-world features used by actual hiring platforms.

🚀 Features
👤 For Job Seekers

Create and update profile

Upload resume (PDF/DOCX)

Search jobs by title, skills, category, location

Apply for jobs instantly

Track application status (Applied / Under Review / Shortlisted / Rejected)

🧑‍💼 For Recruiters / Companies

Company registration & login

Post new job openings

Manage job listings

View applicant list for each job

Download applicant resumes

Update hiring status

🔐 Authentication & Security

JWT-based login (Spring Security)

Role-based access: Admin / Recruiter / Job Seeker

Input validation with error handling

Secure password hashing with BCrypt

📊 Admin Panel

Manage all users (enable/disable)

View analytics:

Total job seekers

Registered companies

Jobs posted

Applications received

🏗️ Tech Stack
Backend

Java 17

Spring Boot (Web, Data JPA, Security, Validation)

Spring Security + JWT

Oracle / MySQL Database

Frontend

React JS / HTML / CSS / Vanilla JavaScript

Axios for API calls

Redux Toolkit (optional)

Tools & Build

Maven

Postman (API testing)

Git & GitHub

Swagger UI (API documentation)

📁 Project Structure (Backend – Spring Boot)
src/
 ├── main/
 │   ├── java/com/jobportal/
 │   │   ├── controller/
 │   │   ├── service/
 │   │   ├── repository/
 │   │   ├── model/
 │   │   ├── config/
 │   │   └── JobPortalApplication.java
 │   └── resources/
 │       ├── application.properties
 │       └── schema.sql / data.sql

🗄️ Database Schema (Example)
Users Table
Column	Type	Description
user_id	NUMBER	Primary Key
name	VARCHAR2	Full Name
email	VARCHAR2	Unique
password	VARCHAR2	Hashed
role	VARCHAR2	ADMIN / RECRUITER / JOB_SEEKER
Jobs Table
Column	Type	Description
job_id	NUMBER	Primary Key
title	VARCHAR2	Job Title
company_id	NUMBER	FK
location	VARCHAR2	City/Country
description	CLOB	Full JD
⚙️ How to Run the Project
▶️ Backend (Spring Boot)

Clone the repository

git clone https://github.com/yourname/job-portal.git


Open in IntelliJ / Eclipse

Configure Oracle/MySQL credentials in application.properties

Run the project

mvn spring-boot:run


Access API Docs

http://localhost:8080/swagger-ui.html

▶️ Frontend (React JS)

Navigate to /frontend

Install dependencies

npm install


Start the project

npm start


App runs on

http://localhost:3000

🧪 API Endpoints (Sample)
Authentication
Method	Endpoint	Description
POST	/auth/register	Register a new user
POST	/auth/login	Login & get JWT
Jobs
Method	Endpoint	Description
GET	/jobs	List all jobs
POST	/jobs	Recruiter adds a job
GET	/jobs/{id}	Get job details
Applications

| POST | /applications/apply/{jobId} | Apply for a job |
| GET | /applications/by-user | View my applications |

🎯 Key Highlights

Designed using real industry hiring workflows

Follows REST standards & clean code architecture

Highly scalable & production-ready

Perfect full-stack project for placements and internships

📸 Screenshots (Optional)

Add UI screenshots here (Login page, Dashboard, Job listing)

🧑‍🏫 Future Enhancements

Resume Ranking using ML

Chat Module (Candidate ↔ HR)

Video Interview Scheduling

Job Recommendation System

Microservices with Spring Cloud
