package za.ac.mycput.studentregistrationsystem.frontend.view;

public class ApplicationPreviewData {
    private static String firstName = "";
    private static String lastName = "";
    private static String dateOfBirth = "";
    private static String email = "";
    private static int studentId = 0;
    private static String studentNumber = "";
    private static String studentEmail = "";
    private static String[] courses = {"", "", ""};
    private static int[] courseIds = {0, 0, 0};
    private static int acceptedCourseIndex = 0;
    private static int personId = 0;
    private static int applicantId = 0;
    private static String gender = "OTHER";
    private static String race = "OTHER";
    private static String phoneNumber = "";
    private static String street = "";
    private static String suburb = "";
    private static String city = "";
    private static String postalCode = "";
    private static String province = "";

    private ApplicationPreviewData() {
    }

    public static void saveApplicant(String applicantFirstName, String applicantLastName,
            String applicantDateOfBirth, String applicantGender, String applicantRace,
            String applicantEmail, String applicantPhone, String applicantStreet,
            String applicantSuburb, String applicantCity, String applicantPostalCode,
            String applicantProvince) {
        firstName = applicantFirstName.trim();
        lastName = applicantLastName.trim();
        dateOfBirth = applicantDateOfBirth.trim();
        email = applicantEmail.trim();
        gender = applicantGender;
        race = applicantRace;
        phoneNumber = applicantPhone.trim();
        street = applicantStreet.trim();
        suburb = applicantSuburb.trim();
        city = applicantCity.trim();
        postalCode = applicantPostalCode.trim();
        province = applicantProvince.trim();
    }

    public static void saveCourses(String firstCourse, String secondCourse, String thirdCourse) {
        courses = new String[]{firstCourse, secondCourse, thirdCourse};
        courseIds = new int[]{0, 0, 0};
        acceptedCourseIndex = 0;
    }

    public static String firstName() { return firstName; }
    public static String lastName() { return lastName; }
    public static String dateOfBirth() { return dateOfBirth; }
    public static String email() { return email; }
    public static int personId() { return personId; }
    public static int studentId() { return studentId; }
    public static int acceptedCourseId() { return courseIds[acceptedCourseIndex]; }

    public static void saveGeneratedIds(int newPersonId, int newApplicantId) {
        personId = newPersonId;
        applicantId = newApplicantId;
    }

    public static String studentNumber() { return studentNumber; }
    public static String studentEmail() { return studentEmail; }
    public static String fullName() { return firstName + " " + lastName; }

    public static String courseStatus(int index) {
        if (index < 0 || index >= courses.length) {
            return "";
        }
        return courses[index] + " - " + (index == acceptedCourseIndex ? "ACCEPTED" : "PENDING");
    }

    public static String acceptedCourse() {
        return courses[acceptedCourseIndex];
    }

    public static void updatePersonalDetails(String newFirstName, String newLastName,
            String newDateOfBirth, String newGender, String newRace, String newEmail,
            String newPhoneNumber, String newStreet, String newSuburb, String newCity,
            String newPostalCode, String newProvince) {
        firstName = newFirstName.trim();
        lastName = newLastName.trim();
        dateOfBirth = newDateOfBirth.trim();
        gender = newGender;
        race = newRace;
        email = newEmail.trim();
        phoneNumber = newPhoneNumber.trim();
        street = newStreet.trim();
        suburb = newSuburb.trim();
        city = newCity.trim();
        postalCode = newPostalCode.trim();
        province = newProvince.trim();
    }

    public static String gender() { return gender; }
    public static String race() { return race; }
    public static String phoneNumber() { return phoneNumber; }
    public static String street() { return street; }
    public static String suburb() { return suburb; }
    public static String city() { return city; }
    public static String postalCode() { return postalCode; }
    public static String province() { return province; }

    public static String applicantJson() {
        return "{\"personId\":0"
                + ",\"applicantId\":0"
                + ",\"firstName\":" + quote(firstName)
                + ",\"lastName\":" + quote(lastName)
                + ",\"dateOfBirth\":" + quote(dateOfBirth)
                + ",\"gender\":" + quote(gender)
                + ",\"race\":" + quote(race)
                + ",\"contactDetails\":{\"contactId\":0"
                + ",\"email\":" + quote(email)
                + ",\"phoneNumber\":" + quote(phoneNumber) + "}"
                + ",\"address\":{\"addressId\":0"
                + ",\"street\":" + quote(street)
                + ",\"suburb\":" + quote(suburb)
                + ",\"city\":" + quote(city)
                + ",\"postalCode\":" + quote(postalCode)
                + ",\"province\":" + quote(province) + "}}";
    }

    public static void loadBackendResult(String result) {
        java.util.ArrayList<String> courseList = new java.util.ArrayList<>();
        java.util.ArrayList<Integer> courseIdList = new java.util.ArrayList<>();
        java.util.ArrayList<String> statusList = new java.util.ArrayList<>();
        acceptedCourseIndex = 0;
        for (String line : result.split("\\n")) {
            String[] values = line.split("\\|", -1);
            if (values.length == 5 && values[0].equals("APPLICANT")) {
                firstName = values[1];
                lastName = values[2];
                dateOfBirth = values[3];
                email = values[4];
            } else if (values.length == 4 && values[0].equals("STUDENT")) {
                studentId = Integer.parseInt(values[1]);
                studentNumber = values[2];
                studentEmail = values[3];
            } else if (values.length == 3 && values[0].equals("STUDENT")) {
                studentNumber = values[1];
                studentEmail = values[2];
            } else if (values.length == 4 && values[0].equals("APPLICATION")) {
                courseIdList.add(Integer.parseInt(values[1]));
                courseList.add(values[2]);
                statusList.add(values[3]);
            } else if (values.length == 3 && values[0].equals("APPLICATION")) {
                courseIdList.add(0);
                courseList.add(values[1]);
                statusList.add(values[2]);
            }
        }
        if (!courseList.isEmpty()) {
            courses = courseList.toArray(new String[0]);
            courseIds = new int[courseIdList.size()];
            for (int i = 0; i < courseIdList.size(); i++) {
                courseIds[i] = courseIdList.get(i);
                if (statusList.get(i).equals("ACCEPTED")) {
                    acceptedCourseIndex = i;
                }
            }
        }
    }

    private static String quote(String value) {
        String safe = value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
        return "\"" + safe + "\"";
    }
}
