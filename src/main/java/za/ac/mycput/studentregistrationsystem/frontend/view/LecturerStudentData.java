package za.ac.mycput.studentregistrationsystem.frontend.view;

import za.ac.mycput.studentregistrationsystem.frontend.service.BackendApiClient;

public class LecturerStudentData {
    private LecturerStudentData() {
    }

    public static String[][] studentsForClass(String classCode) {
        try {
            return BackendApiClient.getStudentsForClass(classCode);
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Could not load students from the backend: " + e.getMessage(), e);
        }
    }
}
