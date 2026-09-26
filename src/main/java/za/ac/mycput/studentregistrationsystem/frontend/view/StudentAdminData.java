package za.ac.mycput.studentregistrationsystem.frontend.view;

import java.util.ArrayList;
import java.util.List;
import za.ac.mycput.studentregistrationsystem.frontend.service.BackendApiClient;
import za.ac.mycput.studentregistrationsystem.frontend.service.BackendApiClient.StudentAdminOption;

public class StudentAdminData {
    private static final ArrayList<StudentAdminOption> students = new ArrayList<>();

    private StudentAdminData() {
    }

    public static void refreshFromBackend() throws Exception {
        students.clear();
        students.addAll(BackendApiClient.getStudents());
    }

    public static List<StudentAdminOption> students() {
        return new ArrayList<>(students);
    }

    public static StudentAdminOption studentAt(int index) {
        return students.get(index);
    }

    public static void updateStudent(int index, String firstName, String lastName,
            String dateOfBirth, String gender, String race, String personalEmail,
            String phoneNumber, String street, String suburb, String city,
            String postalCode, String province) throws Exception {
        StudentAdminOption current = students.get(index);
        StudentAdminOption updated = BackendApiClient.updateStudentAsAdmin(
                current.getStudentId(), firstName, lastName, dateOfBirth,
                gender, race, personalEmail, phoneNumber, street, suburb,
                city, postalCode, province);
        students.set(index, updated);
    }

    public static void deleteStudent(int index) throws Exception {
        StudentAdminOption current = students.get(index);
        BackendApiClient.deleteStudent(current.getStudentId());
        students.remove(index);
    }
}
