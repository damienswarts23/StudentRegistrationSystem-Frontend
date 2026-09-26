package za.ac.mycput.studentregistrationsystem.frontend.view;

import java.util.ArrayList;
import java.util.List;
import za.ac.mycput.studentregistrationsystem.frontend.service.BackendApiClient;

public class StudentClassData {
    private static final ArrayList<BackendApiClient.ClassOption> available = new ArrayList<>();
    private static final ArrayList<BackendApiClient.RegistrationOption> registered = new ArrayList<>();

    private StudentClassData() {
    }

    public static String[][] classesForAcceptedCourse() {
        available.clear();
        try {
            if (ApplicationPreviewData.acceptedCourseId() <= 0) {
                return new String[0][];
            }
            List<BackendApiClient.ClassOption> classes =
                    BackendApiClient.getClassesForCourse(ApplicationPreviewData.acceptedCourseId());
            refreshRegistered();
            for (BackendApiClient.ClassOption item : classes) {
                if (!isRegistered(item.getId())) {
                    available.add(item);
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("Could not load classes from the backend: " + e.getMessage(), e);
        }
        String[][] rows = new String[available.size()][4];
        for (int i = 0; i < available.size(); i++) {
            BackendApiClient.ClassOption item = available.get(i);
            rows[i] = new String[]{item.getCode(), item.getName(), item.getCourseName(), item.getLecturerName()};
        }
        return rows;
    }

    public static String[][] registeredClasses() {
        try {
            refreshRegistered();
        } catch (Exception e) {
            throw new IllegalStateException("Could not load registrations from the backend: " + e.getMessage(), e);
        }
        String[][] rows = new String[registered.size()][4];
        for (int i = 0; i < registered.size(); i++) {
            BackendApiClient.RegistrationOption item = registered.get(i);
            rows[i] = new String[]{item.getClassCode(), item.getClassName(),
                    item.getCourseName(), item.getLecturerName()};
        }
        return rows;
    }

    public static void registerClass(String classCode) {
        BackendApiClient.ClassOption chosen = null;
        for (BackendApiClient.ClassOption item : available) {
            if (item.getCode().equals(classCode)) {
                chosen = item;
                break;
            }
        }
        if (chosen == null) {
            throw new IllegalArgumentException("Class was not found");
        }
        if (ApplicationPreviewData.studentId() <= 0) {
            throw new IllegalStateException("Student ID was not loaded. Sign in again.");
        }
        try {
            BackendApiClient.registerStudentClass(ApplicationPreviewData.studentId(), chosen.getId());
            refreshRegistered();
        } catch (Exception e) {
            throw new IllegalStateException("Could not register class: " + e.getMessage(), e);
        }
    }

    public static void deregisterClass(String classCode) {
        try {
            refreshRegistered();
            for (BackendApiClient.RegistrationOption item : new ArrayList<>(registered)) {
                if (item.getClassCode().equals(classCode)) {
                    BackendApiClient.deleteRegistration(item.getRegistrationId());
                    refreshRegistered();
                    return;
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("Could not deregister class: " + e.getMessage(), e);
        }
    }

    private static void refreshRegistered() throws Exception {
        registered.clear();
        if (ApplicationPreviewData.studentId() > 0) {
            registered.addAll(BackendApiClient.getStudentRegistrations(ApplicationPreviewData.studentId()));
        }
    }

    private static boolean isRegistered(int classId) {
        for (BackendApiClient.RegistrationOption item : registered) {
            if (item.getClassId() == classId) {
                return true;
            }
        }
        return false;
    }
}
