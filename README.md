# ADP372S Capstone Project – StudentRegistrationsSystem-Frontend

## Overview
This project was developed by our group as part of the ADP372S Capstone Project.

This repository contains the frontend for the Student Registration System. The frontend was developed using **Java Swing** and communicates with the Student Registration System Spring Boot backend through REST API calls.

The frontend provides different screens for applicants, students, lecturers and administrators.

## Frontend Structure
The frontend mainly consists of:

- **View** – contains the Java Swing screens used by the system
- **Service** – contains the connection between the frontend and backend API
- **GUIStyle** – contains reusable styling used across the Swing screens
- **DatePickerField** – provides the date selection component used in forms

The frontend was created manually using Java Swing components and does not use JavaFX or the NetBeans GUI Builder.

## Main System Flow
The frontend allows users to interact with the Student Registration System through different dashboards and forms.

### Applicant
An applicant can:

- Enter their personal details
- Apply for available courses
- Submit their applications
- Track the status of their applications
- View their student details after being accepted

### Student
A student can:

- Log in using their student email
- View their dashboard
- View available classes for their course
- Register for classes
- Deregister from classes
- View their registered classes
- Update selected personal contact and address details

### Lecturer
A lecturer can:

- Log in using their lecturer email
- View the classes assigned to them
- View students registered for their classes
- Access their lecturer dashboard

### Administrator
The administrator can manage the main information in the system.

The administrator can:

- Create lecturers
- View lecturers
- Edit lecturer details
- Assign lecturers to multiple classes
- Delete lecturers
- Create departments
- View and edit departments
- Add courses to departments
- Add classes to courses
- Delete classes, courses and departments
- View all students
- Edit student details
- Delete students

Students are created through the application process and therefore cannot be manually added by the administrator.

## Backend Connection
The frontend connects to the Spring Boot backend using Java's built-in `HttpClient`.

The backend API is expected to run at:

```text
http://localhost:8080/api
