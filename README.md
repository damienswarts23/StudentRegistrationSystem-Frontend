# RegiLearn - Student Registration System Frontend

RegiLearn is a Java Swing desktop application developed for the ADP372S Capstone Project. It brings student applications, class registration, lecturer access, and academic administration into one interface.

This repository contains the frontend. It communicates with the separate Student Registration System Spring Boot backend through REST API calls. The backend handles authentication, permissions, and persistent data.

## Features

### Applicants

- Complete an application with personal, contact, and address details.
- Select a course, review the application, and submit it.
- Track an application using an email address, name, surname, and date of birth.
- Set a student login password when a student record is available.

### Students

- Sign in with an account email and password.
- View their dashboard and student details.
- Browse available classes, register, deregister, and view registered classes.
- Update personal contact and address details in settings.
- Manage their account profile and password.

### Lecturers

- Sign in with a lecturer account and view assigned classes.
- View students registered for their classes.
- View lecturer details and update personal contact and address information.
- Manage their account profile and password.

### Administrators

- Create, view, edit, and delete departments, courses, and classes.
- Create lecturers with an initial login password and manage lecturer records.
- Assign lecturers to classes and remove assignments.
- View, edit, and delete student records.
- Enable student and lecturer login accounts and reset student login passwords.
- Create additional administrator accounts.
- View recent administrator activity in a shared log.

Student records are created through the application process rather than a separate administrator form for adding students.

## Interface and technology

The interface uses hand-written Java Swing and AWT components, shared styling, and reusable layouts. It includes RegiLearn branding, a startup splash screen, page transitions, dashboard components, Lucide icons, and bundled stock photography.

Backend requests use Java's built-in `HttpClient`. The Maven project targets **Java 17** and declares no external application dependencies. A graphical desktop environment is required to run the application.

## Getting started

### 1. Start the backend

Run the compatible Student Registration System backend separately, following its instructions for database setup and account configuration. The default API address is:

```text
http://localhost:8080/api
```

Sign in with an account configured in the backend. Administrators can create lecturer accounts, and students can set a login password through the application flow when their student record is available.

### 2. Build and launch

Install a JDK (17 or later) and Maven, then run these commands from the project root:

```shell
mvn compile
java -cp target/classes za.ac.mycput.studentregistrationsystem.frontend.StudentRegistrationSystemFrontend
```

Alternatively, open `pom.xml` as a Maven project in IntelliJ IDEA or another Java IDE, select JDK 17 or later, and run the `StudentRegistrationSystemFrontend` main class. Ensure `src/main/resources` is included on the runtime classpath so icons and photographs load.

### Use a different backend address

Set the `srs.api.url` JVM property when launching:

```shell
java -Dsrs.api.url=https://your-server.example/api -cp target/classes za.ac.mycput.studentregistrationsystem.frontend.StudentRegistrationSystemFrontend
```

In an IDE, add `-Dsrs.api.url=https://your-server.example/api` to the run configuration's VM options. Remote backend addresses must use HTTPS. HTTP is allowed for loopback addresses such as `localhost` and `127.0.0.1`.

## Authentication and sessions

`BackendApiClient` manages session cookies, CSRF tokens, and the signed-in user's profile. The frontend opens a student, lecturer, or administrator dashboard based on the role returned by the backend. Permission checks remain the backend's responsibility.

Signing out clears the local session. Changing a password requires signing in again. Authentication and permission failures are displayed through user-facing error messages.

## Project structure

```text
src/
  main/
    java/za/ac/mycput/studentregistrationsystem/frontend/
      StudentRegistrationSystemFrontend.java   Application entry point
      service/
        BackendApiClient.java                  API calls and session handling
      view/                                    Swing screens and shared components
    resources/
      icons/                                   PNG and SVG icons
      photos/                                  Bundled photography
  test/java/
    BackendApiClientSessionTest.java           Standalone session regression test
pom.xml                                        Maven build configuration
LUCIDE-LICENSE.txt                              Icon license
PHOTO-CREDITS.md                                Photography credits
```

The `view` package includes applicant forms, role-specific dashboards, management screens, settings, and shared components such as `GUIStyle`, `AppFrame`, `DashboardComponents`, and `DatePickerField`.

## Session regression test

The standalone JDK-based test starts a local mock HTTP server and checks login, cookies and CSRF token rotation, profile updates, form encoding, permission errors, logout, and incorrect passwords. It does not require the real backend or a database.

```shell
mvn test-compile
java -cp "target/classes;target/test-classes" BackendApiClientSessionTest
```

The classpath above is for Windows. On macOS or Linux, replace the semicolon with a colon. Run the test's `main` method explicitly; it is not a JUnit test automatically executed by `mvn test`.

## Asset credits

Icon licensing is included in [LUCIDE-LICENSE.txt](LUCIDE-LICENSE.txt). Photography sources and usage details are listed in [PHOTO-CREDITS.md](PHOTO-CREDITS.md).
