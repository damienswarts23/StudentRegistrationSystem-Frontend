package za.ac.mycput.studentregistrationsystem.frontend.view;

import java.util.ArrayList;
import java.util.List;
import za.ac.mycput.studentregistrationsystem.frontend.service.BackendApiClient;

public class LecturerData {
    public static final int PERSON_ID = 0;
    public static final int LECTURER_ID = 1;
    public static final int LECTURER_NUMBER = 2;
    public static final int FIRST_NAME = 3;
    public static final int LAST_NAME = 4;
    public static final int DATE_OF_BIRTH = 5;
    public static final int GENDER = 6;
    public static final int RACE = 7;
    public static final int EMAIL = 8;              // generated lecturer email
    public static final int PHONE = 9;
    public static final int DEPARTMENT = 10;
    public static final int ADDRESS = 11;
    public static final int PERSONAL_EMAIL = 12;
    public static final int DEPARTMENT_ID = 13;
    public static final int STREET = 14;
    public static final int SUBURB = 15;
    public static final int CITY = 16;
    public static final int POSTAL_CODE = 17;
    public static final int PROVINCE = 18;

    private static final ArrayList<String[]> lecturers = new ArrayList<>();
    private static final ArrayList<String[]> lecturerClasses = new ArrayList<>();
    private static String[] pendingLecturer;
    private static String[] pendingClasses = new String[0];
    private static int[] pendingClassIds = new int[0];

    private LecturerData() {
    }

    public static void setPendingLecturer(String[] lecturer) {
        pendingLecturer = lecturer;
        pendingClasses = new String[0];
        pendingClassIds = new int[0];
    }

    public static String[] pendingLecturer() {
        return pendingLecturer;
    }

    public static void setPendingClasses(String[] classes, int[] classIds) {
        pendingClasses = classes;
        pendingClassIds = classIds;
    }

    public static String[] pendingClasses() {
        return pendingClasses;
    }

    public static int savePendingLecturerToBackend() throws Exception {
        if (pendingLecturer == null) {
            throw new IllegalArgumentException("No lecturer details are waiting to be saved.");
        }

        String created = BackendApiClient.createLecturer(
                pendingLecturer[FIRST_NAME], pendingLecturer[LAST_NAME],
                pendingLecturer[DATE_OF_BIRTH], pendingLecturer[GENDER],
                pendingLecturer[RACE], pendingLecturer[PERSONAL_EMAIL],
                pendingLecturer[PHONE], Integer.parseInt(pendingLecturer[DEPARTMENT_ID]),
                pendingLecturer[STREET], pendingLecturer[SUBURB],
                pendingLecturer[CITY], pendingLecturer[POSTAL_CODE],
                pendingLecturer[PROVINCE]);

        int lecturerIndex = useBackendLecturer(created);
        int personId = Integer.parseInt(lecturers.get(lecturerIndex)[PERSON_ID]);
        for (int classId : pendingClassIds) {
            BackendApiClient.assignLecturerToClass(classId, personId);
        }
        lecturerClasses.set(lecturerIndex, pendingClasses);
        pendingLecturer = null;
        pendingClasses = new String[0];
        pendingClassIds = new int[0];
        return lecturerIndex;
    }

    public static void refreshFromBackend() throws Exception {
        String response = BackendApiClient.getLecturersText();
        lecturers.clear();
        lecturerClasses.clear();
        for (String line : response.split("\n")) {
            if (!line.isBlank()) {
                addBackendLecturer(line);
            }
        }
    }

    public static String[][] lecturers() {
        return lecturers.toArray(new String[0][]);
    }

    public static String[] lecturerAt(int index) {
        return lecturers.get(index);
    }

    public static String[] classesAt(int index) {
        String[] lecturer = lecturers.get(index);
        try {
            String response = BackendApiClient.getLecturerClassesText(
                    Integer.parseInt(lecturer[PERSON_ID]));
            ArrayList<String> classes = new ArrayList<>();
            for (String line : response.split("\n")) {
                String[] values = line.split("\\|", -1);
                if (values.length == 3) {
                    classes.add(values[1] + " - " + values[2]);
                }
            }
            String[] result = classes.toArray(new String[0]);
            lecturerClasses.set(index, result);
            return result;
        } catch (Exception exception) {
            return lecturerClasses.get(index);
        }
    }

    public static int findLecturerIndex(String email, String lecturerNumber) {
        for (int i = 0; i < lecturers.size(); i++) {
            String[] lecturer = lecturers.get(i);
            if (lecturer[EMAIL].equalsIgnoreCase(email)
                    && lecturer[LECTURER_NUMBER].equalsIgnoreCase(lecturerNumber)) {
                return i;
            }
        }
        return -1;
    }

    public static int useBackendLecturer(String lecturerText) {
        String[] data = normalizeLecturerData(lecturerText.split("\\|", -1));
        for (int i = 0; i < lecturers.size(); i++) {
            if (lecturers.get(i)[PERSON_ID].equals(data[PERSON_ID])) {
                lecturers.set(i, data);
                return i;
            }
        }
        lecturers.add(data);
        lecturerClasses.add(new String[0]);
        return lecturers.size() - 1;
    }

    private static void addBackendLecturer(String lecturerText) {
        String[] data = normalizeLecturerData(lecturerText.split("\\|", -1));
        lecturers.add(data);
        lecturerClasses.add(new String[0]);
    }

    private static String[] normalizeLecturerData(String[] source) {
        String[] data = new String[19];
        for (int i = 0; i < data.length; i++) {
            data[i] = "";
        }
        int count = Math.min(source.length, data.length);
        System.arraycopy(source, 0, data, 0, count);
        if (source.length == 12) {
            data[PERSONAL_EMAIL] = "";
            data[DEPARTMENT_ID] = "";
        }
        return data;
    }

    public static String fullNameAt(int index) {
        String[] lecturer = lecturers.get(index);
        return lecturer[FIRST_NAME] + " " + lecturer[LAST_NAME];
    }

    public static String classCode(String assignedClass) {
        int separatorIndex = assignedClass.indexOf(" - ");
        return separatorIndex >= 0 ? assignedClass.substring(0, separatorIndex) : assignedClass;
    }

    public static String className(String assignedClass) {
        int separatorIndex = assignedClass.indexOf(" - ");
        return separatorIndex >= 0 ? assignedClass.substring(separatorIndex + 3) : assignedClass;
    }

    public static void updateLecturer(int index, String lastName, String personalEmail,
            String phone, String street, String suburb, String city,
            String postalCode, String province) throws Exception {
        String[] lecturer = lecturers.get(index);
        String updated = BackendApiClient.updateLecturerPersonalDetails(
                Integer.parseInt(lecturer[PERSON_ID]), lastName, personalEmail,
                phone, street, suburb, city, postalCode, province);
        String[] data = normalizeLecturerData(updated.split("\\|", -1));
        lecturers.set(index, data);
    }


    public static List<BackendApiClient.ClassOption> assignedClasses(int index) throws Exception {
        String[] lecturer = lecturers.get(index);
        return BackendApiClient.getLecturerClasses(
                Integer.parseInt(lecturer[PERSON_ID]));
    }

    public static void updateLecturerClasses(int index, List<Integer> originalClassIds,
            List<Integer> selectedClassIds) throws Exception {
        String[] lecturer = lecturers.get(index);
        int personId = Integer.parseInt(lecturer[PERSON_ID]);

        for (Integer classId : originalClassIds) {
            if (!selectedClassIds.contains(classId)) {
                BackendApiClient.unassignLecturerFromClass(classId, personId);
            }
        }

        for (Integer classId : selectedClassIds) {
            if (!originalClassIds.contains(classId)) {
                BackendApiClient.assignLecturerToClass(classId, personId);
            }
        }

        classesAt(index);
    }

    public static List<BackendApiClient.DepartmentOption> departments() throws Exception {
        return BackendApiClient.getDepartments();
    }

    public static List<BackendApiClient.ClassOption> availableClasses(int departmentId) throws Exception {
        return BackendApiClient.getAvailableClassesForDepartment(departmentId);
    }
}
