package za.ac.mycput.studentregistrationsystem.frontend.service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import za.ac.mycput.studentregistrationsystem.frontend.view.ApplicationPreviewData;

public class BackendApiClient {
    private static final String BASE_URL = "http://localhost:8080/api";
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    private BackendApiClient() {
    }

    public static List<CourseOption> getCourses() throws Exception {
        return parseCourses(get("/courses/course-list"));
    }

    public static List<CourseOption> getCoursesForDepartment(int departmentId) throws Exception {
        return parseCourses(get("/courses/department/" + departmentId + "/course-list"));
    }

    public static List<DepartmentOption> getDepartments() throws Exception {
        String response = get("/departments/department-list");
        List<DepartmentOption> departments = new ArrayList<>();
        for (String line : response.split("\\n")) {
            String[] values = line.split("\\|", -1);
            if (values.length == 3 && !values[0].isBlank()) {
                departments.add(new DepartmentOption(
                        Integer.parseInt(values[0]), values[1], values[2]));
            }
        }
        return departments;
    }

    public static List<ClassOption> getAvailableClassesForDepartment(int departmentId) throws Exception {
        String response = get("/classes/available/department/" + departmentId + "/class-list");
        List<ClassOption> classes = new ArrayList<>();
        for (String line : response.split("\\n")) {
            String[] values = line.split("\\|", -1);
            if (values.length == 3 && !values[0].isBlank()) {
                classes.add(new ClassOption(Integer.parseInt(values[0]), values[1], values[2],
                        0, "", "Unassigned"));
            }
        }
        return classes;
    }

    public static List<ClassOption> getClassesForCourse(int courseId) throws Exception {
        return parseClasses(get("/classes/course/" + courseId + "/class-list"));
    }

    public static DepartmentOption createDepartment(String code, String name) throws Exception {
        String text = post("/departments/create-text",
                "{\"departmentId\":0,\"departmentCode\":" + quote(code)
                        + ",\"departmentName\":" + quote(name) + "}");
        String[] v = text.trim().split("\\|", -1);
        if (v.length != 3) {
            throw new IOException("Unexpected department response from backend");
        }
        return new DepartmentOption(Integer.parseInt(v[0]), v[1], v[2]);
    }

    public static CourseOption createCourse(String code, String name, int departmentId) throws Exception {
        String text = post("/courses/create-text",
                "{\"courseId\":0,\"courseCode\":" + quote(code)
                        + ",\"courseName\":" + quote(name)
                        + ",\"department\":{\"departmentId\":" + departmentId + "}}");
        String[] v = text.trim().split("\\|", -1);
        if (v.length != 5) {
            throw new IOException("Unexpected course response from backend");
        }
        return new CourseOption(Integer.parseInt(v[0]), v[1], v[2],
                Integer.parseInt(v[3]), v[4]);
    }

    public static ClassOption createClass(String code, String name, int courseId) throws Exception {
        String text = post("/classes/create-text",
                "{\"classId\":0,\"classCode\":" + quote(code)
                        + ",\"className\":" + quote(name)
                        + ",\"course\":{\"courseId\":" + courseId + "},\"lecturer\":null}");
        List<ClassOption> parsed = parseClasses(text);
        if (parsed.isEmpty()) {
            throw new IOException("Unexpected class response from backend");
        }
        return parsed.get(0);
    }

    public static void deleteCourse(int courseId) throws Exception {
        delete("/courses/" + courseId);
    }

    public static void deleteClass(int classId) throws Exception {
        delete("/classes/" + classId);
    }

    public static void deleteDepartment(int departmentId) throws Exception {
        delete("/departments/" + departmentId);
    }

    public static String createLecturer(String firstName, String lastName,
            String dateOfBirth, String gender, String race,
            String personalEmail, String phoneNumber, int departmentId,
            String street, String suburb, String city, String postalCode,
            String province) throws Exception {

        String json = "{\"personId\":0,\"lecturerId\":0"
                + ",\"employeeNumber\":\"\",\"lecturerEmail\":\"\""
                + ",\"firstName\":" + quote(firstName)
                + ",\"lastName\":" + quote(lastName)
                + ",\"dateOfBirth\":" + quote(dateOfBirth)
                + ",\"gender\":" + quote(gender)
                + ",\"race\":" + quote(race)
                + ",\"contactDetails\":{\"contactId\":0"
                + ",\"email\":" + quote(personalEmail)
                + ",\"phoneNumber\":" + quote(phoneNumber) + "}"
                + ",\"address\":{\"addressId\":0"
                + ",\"street\":" + quote(street)
                + ",\"suburb\":" + quote(suburb)
                + ",\"city\":" + quote(city)
                + ",\"postalCode\":" + quote(postalCode)
                + ",\"province\":" + quote(province) + "}"
                + ",\"department\":{\"departmentId\":" + departmentId + "}}";
        return post("/lecturers/create-text", json);
    }

    public static void assignLecturerToClass(int classId, int lecturerPersonId) throws Exception {
        put("/classes/" + classId + "/lecturer/" + lecturerPersonId);
    }

    public static void unassignLecturerFromClass(int classId, int lecturerPersonId) throws Exception {
        put("/classes/" + classId + "/unassign-lecturer/" + lecturerPersonId);
    }

    public static void deleteLecturer(int personId) throws Exception {
        delete("/lecturers/" + personId);
    }

    public static String getLecturersText() throws Exception {
        return get("/lecturers/lecturer-list");
    }

    public static String getLecturerClassesText(int personId) throws Exception {
        return get("/lecturers/" + personId + "/classes-text");
    }

    public static List<ClassOption> getLecturerClasses(int personId) throws Exception {
        return parseClasses(getLecturerClassesText(personId));
    }

    public static String updateLecturerPersonalDetails(int personId, String lastName,
            String personalEmail, String phoneNumber, String street, String suburb,
            String city, String postalCode, String province) throws Exception {
        String path = "/lecturers/" + personId + "/personal-details"
                + "?lastName=" + encode(lastName)
                + "&personalEmail=" + encode(personalEmail)
                + "&phoneNumber=" + encode(phoneNumber)
                + "&street=" + encode(street)
                + "&suburb=" + encode(suburb)
                + "&city=" + encode(city)
                + "&postalCode=" + encode(postalCode)
                + "&province=" + encode(province);
        return putAndGet(path);
    }

    public static void submitApplications(List<Integer> courseIds) throws Exception {
        String applicant = post("/applicants", ApplicationPreviewData.applicantJson());
        int personId = numberFromJson(applicant, "personId");
        int applicantId = numberFromJson(applicant, "applicantId");
        ApplicationPreviewData.saveGeneratedIds(personId, applicantId);
        for (Integer courseId : courseIds) {
            String application = "{\"applicationId\":0,\"applicant\":{" 
                    + "\"personId\":" + ApplicationPreviewData.personId()
                    + "},\"course\":{\"courseId\":" + courseId + "}}";
            post("/applications", application);
        }
    }

    public static String getApplicationResult(String email) throws Exception {
        return get("/applications/result-text?email=" + encode(email));
    }

    public static String findStudentByEmail(String email) throws Exception {
        return getOrNull("/applications/result-text?email=" + encode(email));
    }

    public static String findLecturerByEmail(String email) throws Exception {
        return getOrNull("/lecturers/email?email=" + encode(email));
    }

    public static String[] getStudentPersonalDetails(int studentId) throws Exception {
        String response = get("/students/" + studentId + "/personal-details-text").trim();
        String[] values = response.split("\\|", -1);
        if (values.length != 12) {
            throw new IOException("Unexpected student details response from backend");
        }
        return values;
    }

    public static String[] updateStudentPersonalDetails(
            int studentId,
            String personalEmail,
            String phoneNumber,
            String street,
            String suburb,
            String city,
            String postalCode,
            String province) throws Exception {

        String path = "/students/" + studentId + "/personal-details"
                + "?personalEmail=" + encode(personalEmail)
                + "&phoneNumber=" + encode(phoneNumber)
                + "&street=" + encode(street)
                + "&suburb=" + encode(suburb)
                + "&city=" + encode(city)
                + "&postalCode=" + encode(postalCode)
                + "&province=" + encode(province);

        String response = putAndGet(path).trim();
        String[] values = response.split("\\|", -1);
        if (values.length != 12) {
            throw new IOException("Unexpected student update response from backend");
        }
        return values;
    }

    public static List<StudentAdminOption> getStudents() throws Exception {
        String response = get("/students/student-list");
        List<StudentAdminOption> students = new ArrayList<>();
        for (String line : response.split("\n")) {
            String[] values = line.split("\\|", -1);
            if (values.length == 18 && !values[0].isBlank()) {
                students.add(new StudentAdminOption(
                        Integer.parseInt(values[0]), values[1], values[2],
                        Integer.parseInt(values[3]), Integer.parseInt(values[4]),
                        values[5], values[6], values[7], values[8], values[9],
                        values[10], values[11], values[12], values[13],
                        values[14], values[15], values[16], values[17]));
            }
        }
        return students;
    }

    public static StudentAdminOption updateStudentAsAdmin(
            int studentId, String firstName, String lastName, String dateOfBirth,
            String gender, String race, String personalEmail, String phoneNumber,
            String street, String suburb, String city, String postalCode,
            String province) throws Exception {

        String path = "/students/" + studentId + "/admin-details"
                + "?firstName=" + encode(firstName)
                + "&lastName=" + encode(lastName)
                + "&dateOfBirth=" + encode(dateOfBirth)
                + "&gender=" + encode(gender)
                + "&race=" + encode(race)
                + "&personalEmail=" + encode(personalEmail)
                + "&phoneNumber=" + encode(phoneNumber)
                + "&street=" + encode(street)
                + "&suburb=" + encode(suburb)
                + "&city=" + encode(city)
                + "&postalCode=" + encode(postalCode)
                + "&province=" + encode(province);

        String[] values = putAndGet(path).trim().split("\\|", -1);
        if (values.length != 18) {
            throw new IOException("Unexpected student update response from backend");
        }
        return new StudentAdminOption(
                Integer.parseInt(values[0]), values[1], values[2],
                Integer.parseInt(values[3]), Integer.parseInt(values[4]),
                values[5], values[6], values[7], values[8], values[9],
                values[10], values[11], values[12], values[13],
                values[14], values[15], values[16], values[17]);
    }

    public static void deleteStudent(int studentId) throws Exception {
        delete("/students/" + studentId);
    }

    public static List<RegistrationOption> getStudentRegistrations(int studentId) throws Exception {
        return parseRegistrations(get("/registrations/student/" + studentId + "/registration-list"));
    }

    public static RegistrationOption registerStudentClass(int studentId, int classId) throws Exception {
        List<RegistrationOption> items = parseRegistrations(
                postNoBody("/registrations/student/" + studentId + "/class/" + classId));
        if (items.isEmpty()) {
            throw new IOException("Unexpected registration response from backend");
        }
        return items.get(0);
    }

    public static void deleteRegistration(int registrationId) throws Exception {
        delete("/registrations/" + registrationId);
    }

    public static String[][] getStudentsForClass(String classCode) throws Exception {
        String response = get("/registrations/class-code/" + encode(classCode) + "/student-list");
        ArrayList<String[]> students = new ArrayList<>();
        for (String line : response.split("\n")) {
            String[] values = line.split("\\|", -1);
            if (values.length == 5) {
                students.add(values);
            }
        }
        return students.toArray(new String[0][]);
    }

    private static List<CourseOption> parseCourses(String response) {
        List<CourseOption> courses = new ArrayList<>();
        for (String line : response.split("\\n")) {
            String[] values = line.split("\\|", -1);
            if (values.length >= 3 && !values[0].isBlank()) {
                int departmentId = values.length >= 4 && !values[3].isBlank()
                        ? Integer.parseInt(values[3]) : 0;
                String departmentName = values.length >= 5 ? values[4] : "";
                courses.add(new CourseOption(Integer.parseInt(values[0]), values[1], values[2],
                        departmentId, departmentName));
            }
        }
        return courses;
    }

    private static List<ClassOption> parseClasses(String response) {
        List<ClassOption> classes = new ArrayList<>();
        for (String line : response.split("\\n")) {
            String[] values = line.split("\\|", -1);
            if (values.length >= 3 && !values[0].isBlank()) {
                int courseId = values.length >= 4 && !values[3].isBlank()
                        ? Integer.parseInt(values[3]) : 0;
                String courseName = values.length >= 5 ? values[4] : "";
                String lecturerName = values.length >= 6 ? values[5] : "Unassigned";
                classes.add(new ClassOption(Integer.parseInt(values[0]), values[1], values[2],
                        courseId, courseName, lecturerName));
            }
        }
        return classes;
    }

    private static List<RegistrationOption> parseRegistrations(String response) {
        List<RegistrationOption> registrations = new ArrayList<>();
        for (String line : response.split("\\n")) {
            String[] values = line.split("\\|", -1);
            if (values.length == 6 && !values[0].isBlank()) {
                registrations.add(new RegistrationOption(Integer.parseInt(values[0]),
                        Integer.parseInt(values[1]), values[2], values[3], values[4], values[5]));
            }
        }
        return registrations;
    }

    private static String get(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + path))
                .timeout(Duration.ofSeconds(10)).GET().build();
        return sendForBody(request);
    }

    private static String getOrNull(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + path))
                .timeout(Duration.ofSeconds(10)).GET().build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 404) {
            return null;
        }
        checkResponse(response);
        return response.body();
    }

    private static String post(String path, String body) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + path))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body)).build();
        return sendForBody(request);
    }

    private static String postNoBody(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + path))
                .timeout(Duration.ofSeconds(10))
                .POST(HttpRequest.BodyPublishers.noBody()).build();
        return sendForBody(request);
    }

    private static void put(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + path))
                .timeout(Duration.ofSeconds(10)).PUT(HttpRequest.BodyPublishers.noBody()).build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        checkResponse(response);
    }

    private static String putAndGet(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + path))
                .timeout(Duration.ofSeconds(10)).PUT(HttpRequest.BodyPublishers.noBody()).build();
        return sendForBody(request);
    }

    private static void delete(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + path))
                .timeout(Duration.ofSeconds(10)).DELETE().build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        checkResponse(response);
    }

    private static String sendForBody(HttpRequest request) throws Exception {
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        checkResponse(response);
        return response.body();
    }

    private static void checkResponse(HttpResponse<String> response) throws IOException {
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("Backend returned HTTP " + response.statusCode()
                    + (response.body().isBlank() ? "" : ": " + response.body()));
        }
    }

    private static String encode(String value) {
        return URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8);
    }

    private static int numberFromJson(String json, String field) {
        String search = "\"" + field + "\":";
        int start = json.indexOf(search);
        if (start < 0) {
            throw new IllegalArgumentException("Backend did not return " + field);
        }
        start += search.length();
        while (start < json.length() && Character.isWhitespace(json.charAt(start))) {
            start++;
        }
        int end = start;
        while (end < json.length() && Character.isDigit(json.charAt(end))) {
            end++;
        }
        return Integer.parseInt(json.substring(start, end));
    }

    private static String quote(String value) {
        String safe = value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
        return "\"" + safe + "\"";
    }

    public static class CourseOption {
        private final int id;
        private final String code;
        private final String name;
        private final int departmentId;
        private final String departmentName;

        public CourseOption(int id, String code, String name) {
            this(id, code, name, 0, "");
        }

        public CourseOption(int id, String code, String name, int departmentId, String departmentName) {
            this.id = id;
            this.code = code;
            this.name = name;
            this.departmentId = departmentId;
            this.departmentName = departmentName;
        }

        public int getId() { return id; }
        public String getCode() { return code; }
        public String getName() { return name; }
        public int getDepartmentId() { return departmentId; }
        public String getDepartmentName() { return departmentName; }

        @Override
        public String toString() {
            return id == 0 ? name : code + " - " + name;
        }
    }

    public static class DepartmentOption {
        private final int id;
        private final String code;
        private final String name;

        public DepartmentOption(int id, String code, String name) {
            this.id = id;
            this.code = code;
            this.name = name;
        }

        public int getId() { return id; }
        public String getCode() { return code; }
        public String getName() { return name; }

        @Override
        public String toString() {
            return id == 0 ? name : code + " - " + name;
        }
    }

    public static class ClassOption {
        private final int id;
        private final String code;
        private final String name;
        private final int courseId;
        private final String courseName;
        private final String lecturerName;

        public ClassOption(int id, String code, String name) {
            this(id, code, name, 0, "", "Unassigned");
        }

        public ClassOption(int id, String code, String name, int courseId,
                String courseName, String lecturerName) {
            this.id = id;
            this.code = code;
            this.name = name;
            this.courseId = courseId;
            this.courseName = courseName;
            this.lecturerName = lecturerName;
        }

        public int getId() { return id; }
        public String getCode() { return code; }
        public String getName() { return name; }
        public int getCourseId() { return courseId; }
        public String getCourseName() { return courseName; }
        public String getLecturerName() { return lecturerName; }

        @Override
        public String toString() {
            return code + " - " + name;
        }
    }

    public static class StudentAdminOption {
        private final int studentId;
        private final String studentNumber;
        private final String studentEmail;
        private final int personId;
        private final int applicantId;
        private final String firstName;
        private final String lastName;
        private final String dateOfBirth;
        private final String gender;
        private final String race;
        private final String personalEmail;
        private final String phoneNumber;
        private final String street;
        private final String suburb;
        private final String city;
        private final String postalCode;
        private final String province;
        private final String courseName;

        public StudentAdminOption(int studentId, String studentNumber, String studentEmail,
                int personId, int applicantId, String firstName, String lastName,
                String dateOfBirth, String gender, String race, String personalEmail,
                String phoneNumber, String street, String suburb, String city,
                String postalCode, String province, String courseName) {
            this.studentId = studentId;
            this.studentNumber = studentNumber;
            this.studentEmail = studentEmail;
            this.personId = personId;
            this.applicantId = applicantId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.dateOfBirth = dateOfBirth;
            this.gender = gender;
            this.race = race;
            this.personalEmail = personalEmail;
            this.phoneNumber = phoneNumber;
            this.street = street;
            this.suburb = suburb;
            this.city = city;
            this.postalCode = postalCode;
            this.province = province;
            this.courseName = courseName;
        }

        public int getStudentId() { return studentId; }
        public String getStudentNumber() { return studentNumber; }
        public String getStudentEmail() { return studentEmail; }
        public int getPersonId() { return personId; }
        public int getApplicantId() { return applicantId; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getDateOfBirth() { return dateOfBirth; }
        public String getGender() { return gender; }
        public String getRace() { return race; }
        public String getPersonalEmail() { return personalEmail; }
        public String getPhoneNumber() { return phoneNumber; }
        public String getStreet() { return street; }
        public String getSuburb() { return suburb; }
        public String getCity() { return city; }
        public String getPostalCode() { return postalCode; }
        public String getProvince() { return province; }
        public String getCourseName() { return courseName; }
        public String getFullName() { return firstName + " " + lastName; }
        public String getAddress() {
            return street + ", " + suburb + ", " + city + ", " + postalCode + ", " + province;
        }
    }

    public static class RegistrationOption {
        private final int registrationId;
        private final int classId;
        private final String classCode;
        private final String className;
        private final String courseName;
        private final String lecturerName;

        public RegistrationOption(int registrationId, int classId, String classCode,
                String className, String courseName, String lecturerName) {
            this.registrationId = registrationId;
            this.classId = classId;
            this.classCode = classCode;
            this.className = className;
            this.courseName = courseName;
            this.lecturerName = lecturerName;
        }

        public int getRegistrationId() { return registrationId; }
        public int getClassId() { return classId; }
        public String getClassCode() { return classCode; }
        public String getClassName() { return className; }
        public String getCourseName() { return courseName; }
        public String getLecturerName() { return lecturerName; }
    }
}
