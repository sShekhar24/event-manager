# 1. Introduction

> **[← Back to Main](../REQUIREMENTS.md)**

---

## 1.1 Purpose

The purpose of the **College Event Management System** is to provide a modern, web-based platform where:

- Students can discover, view, and register for college events
- Administrators can create, manage, approve, and monitor events and participants
- The system streamlines event organization, registration tracking, and communication between students and administrators

This document defines the complete software requirements for the system, covering functional features, UI design, backend API, database schema, and non-functional constraints.

---

## 1.2 Intended Audience

| Audience              | Purpose                                              |
|-----------------------|------------------------------------------------------|
| Development Team      | Understand features to implement                     |
| UI/UX Designers       | Design screens and user flows                        |
| Database Engineers    | Design and implement the database schema             |
| Project Manager       | Track scope and deliverables                         |
| QA / Testers          | Understand expected behavior for test case creation  |
| College Administration| Review and approve system capabilities               |

---

## 1.3 Scope

The **College Event Management System** is a full-stack web application with the following scope:

### In Scope

- Student self-registration and login
- JWT-based authentication and role-based access control
- Event creation by Admin
- Event approval/rejection workflow
- Student event registration with capacity enforcement
- Admin dashboard for managing events, users, and registrations
- Student dashboard for viewing and joining events
- Search and filter for events
- REST API backend built with Spring Boot
- React.js single-page application frontend
- MySQL relational database

### Out of Scope

- Mobile application (iOS / Android)
- Payment gateway integration
- Live streaming or virtual event hosting
- Third-party calendar integrations (Google Calendar, Outlook)
- SMS notifications (may be added as future enhancement)

---

## 1.4 Definitions and Acronyms

| Term / Acronym | Meaning                                                        |
|----------------|----------------------------------------------------------------|
| Admin          | Administrator who manages the system, events, and users        |
| Student / User | A college student who registers and participates in events     |
| Event          | Any college activity such as a workshop, seminar, or fest      |
| Registration   | The process of a student enrolling in an event                 |
| SRS            | Software Requirements Specification                            |
| API            | Application Programming Interface                              |
| REST           | Representational State Transfer                                |
| JWT            | JSON Web Token — used for stateless authentication             |
| SPA            | Single Page Application — the React frontend architecture      |
| JPA            | Java Persistence API — used by Spring Boot for DB access       |
| ORM            | Object Relational Mapping                                      |
| CORS           | Cross-Origin Resource Sharing                                  |
| DTO            | Data Transfer Object — used in API request/response bodies     |

---

## 1.5 References

- Spring Boot Documentation: https://spring.io/projects/spring-boot
- React Documentation: https://react.dev
- MySQL Documentation: https://dev.mysql.com/doc/
- JWT Standard: https://jwt.io
- REST API Best Practices: https://restfulapi.net
