<img width="1917" height="1017" alt="home screenshot" src="https://github.com/user-attachments/assets/5d0f51ac-b50c-4efa-9cd6-8a5b8ba22c01" />


# MoodyClues
A mental wellness journaling app where users track daily habits and reflect on their emotional patterns and with a counsellor portal for linked support professionals.

View video demonstration [here](https://youtu.be/iGxzENFD-_c).

Java · Spring Boot · React · MySQL · Python · Flask · Docker · AWS EC2 · AWS RDS

## Features
- Journal entries with automatic emotion tagging via a sentiment analysis model (2 emotions per entry)
- Daily habit tracking - sleep, water intake, and custom habits
- Personal dashboard with habit trend charts and an emotion radar graph
- Dual user roles: journal users and counsellors
- Counsellors can send link requests; journal users accept or decline
- Linked counsellors get read-only access to a user's entries and dashboard

## UI Design

Screens were designed by [Dion Yao](https://github.com/Montichiari) in Figma prior to development.

[View Figma prototype](https://www.figma.com/design/qzDiT6ebn91LlPFRUrDtHe/MoodyClues-Web-UI-Screens?node-id=0-1&p=f&t=uMKHUbidIDryjcLk-0)

## Architecture
<img width="1061" height="701" alt="moodycluesarchtecture" src="https://github.com/user-attachments/assets/c4fc9e5b-f8c1-47b2-bf09-2ced93047f2f" />
<br></br>

The app is split across four services:
<br>
- **Backend API** - Spring Boot REST API, handles auth, journal entries, habits, and the counsellor-user link model. Backed by MySQL. Built entirely by [Dion Yao](https://github.com/Montichiari).
- **Web Frontend** - React SPA consuming the backend API. Co-built by [Dion Yao](https://github.com/Montichiari), [Hiroyo](https://github.com/chanhiro07), [Sheng Yi](https://github.com/enjoyyourdailylife).
- **Sentiment analysis ML service** - Python Flask microservice that receives journal entry text and returns two classified emotions. Deployed independently and called by the backend via REST. Built entirely by [Hiroyo](https://github.com/chanhiro07).
- **Android Mobile Application** - Not in this repository; Built in Android Studio in Kotlin by [Ma Li](https://github.com/PIKACHU-mali) with [Thina](https://github.com/tinasqk).

All three services are containerised with **Docker** and were deployed on *separate* **AWS EC2** servers, with MySQL database on **AWS RDS** (servers are no longer live as of 1 September 2025).

## Database Schema

<img width="1322" height="1645" alt="SA60 - Group 3 - Final LDM + Database Schema" src="https://github.com/user-attachments/assets/e8dc2c8b-6ea2-42bf-a5e4-f5417755effc" />
<br></br>

Designed and implemented entirely by [Dion Yao](https://github.com/Montichiari), including entity relationship modelling, schema design, and JPA/ORM mapping in Spring Boot.

## Tech Stack

### Backend
- Java / Spring Boot
- Spring Data JPA (Hibernate)
- MySQL
- Flyway
- Spring Security

### Frontend
- ReactJS
- MaterialUI
- TailwindCSS
- Recharts (habit and emotion visualisations)

### Sentiment service
- Python / Flask
- Sentiment model

### Mobile Application
- Android Studio
- Kotlin

### Infrastructure
- Docker / Docker Compose
- AWS EC2
- AWS RDS

## Running locally
Prerequisites: To be consolidated

```
Coming soon
```

## Contributors
| Name | Role |
|------|------|
| [Dion Yao](https://github.com/Montichiari) | - Backend & Database: Architecture planning, Spring Boot, ERD, MySQL schema, REST API, API testing & documentation, Docker, AWS EC2 + RDS deployment) <br> - Web Frontend: Login, Register, Home, Journal User + Counsellor Dashboard pages, AWS EC2 deployment <br> - Web figma screens (All)
| [Hiroyo](https://github.com/chanhiro07) | - Machine learning model for sentiment analysis (Python) <br> - ML model deployment and CI/CD pipeline implementation (FlaskAPI, AWS EC2 + S3, GitHub Actions) <br> - Web Frontend (Journal User pages)
| [Ma Li](https://github.com/PIKACHU-mali) | - Android journal users’ screens (Login, Register, Home, Notification, Emotion setting, invitation <br> - Android API calls (All)
| [Thina](https://github.com/tinasqk) | - Android Counsellor functionality
| [Sheng Yi](https://github.com/enjoyyourdailylife) | - Web Frontend (Counsellor pages)

## Development Process

Built using an Agile framework over 4 weeks, as a five person team.

**As overall project lead, I was responsible for:**
- Sprint planning and task breakdown
- Coordinating deliverables and managing the team's progress
- Making architectural decisions on the backend and deployment
- Ensuring integration between the backend API and web frontend

**Practices followed:**
- Weekly sprints with defined goals
- Daily standups to track progress and surface blockers
- GitHub for version control and feature branching


## Design notes
Counsellor access is modelled via a LinkRequest entity with an acceptance flag, rather than a full RBAC system. This was intentional for the project scope. A production implementation might use role-based access control with finer-grained permissions.

The sentiment service is called synchronously on journal entry submission. A production version would benefit from async processing to avoid blocking the entry save.
