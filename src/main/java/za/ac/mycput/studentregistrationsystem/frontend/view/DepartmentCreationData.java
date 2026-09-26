package za.ac.mycput.studentregistrationsystem.frontend.view;

import java.util.ArrayList;
import java.util.List;
import za.ac.mycput.studentregistrationsystem.frontend.service.BackendApiClient;

public class DepartmentCreationData {
    private static String[] department = {"", "", ""};
    private static final ArrayList<String[]> courses = new ArrayList<>();
    private static final ArrayList<ArrayList<String[]>> classesByCourse = new ArrayList<>();
    private static final ArrayList<String[]> savedDepartments = new ArrayList<>();
    private static final ArrayList<ArrayList<String[]>> savedCoursesByDepartment = new ArrayList<>();
    private static final ArrayList<ArrayList<ArrayList<String[]>>> savedClassesByDepartment = new ArrayList<>();

    private DepartmentCreationData() {
    }

    public static void startDepartment(String departmentCode, String departmentName) {
        department = new String[]{"", value(departmentCode), value(departmentName)};
        courses.clear();
        classesByCourse.clear();
    }

    public static String[] department() { return department; }

    public static void addCourse(String courseCode, String courseName) {
        courses.add(new String[]{"", value(courseCode), value(courseName), department[2]});
        classesByCourse.add(new ArrayList<>());
    }

    public static String[][] courses() { return courses.toArray(new String[0][]); }
    public static String[] courseAt(int index) { return courses.get(index); }

    public static void addClass(int courseIndex, String classCode, String className) {
        String[] course = courses.get(courseIndex);
        classesByCourse.get(courseIndex).add(new String[]{"", value(classCode), value(className), course[2]});
    }

    public static String[][] classesForCourse(int courseIndex) {
        return classesByCourse.get(courseIndex).toArray(new String[0][]);
    }

    public static void completeDepartment() {
        try {
            BackendApiClient.DepartmentOption createdDepartment =
                    BackendApiClient.createDepartment(department[1], department[2]);
            for (int i = 0; i < courses.size(); i++) {
                String[] course = courses.get(i);
                BackendApiClient.CourseOption createdCourse = BackendApiClient.createCourse(
                        course[1], course[2], createdDepartment.getId());
                for (String[] classRow : classesByCourse.get(i)) {
                    BackendApiClient.createClass(classRow[1], classRow[2], createdCourse.getId());
                }
            }
            courses.clear();
            classesByCourse.clear();
            refreshFromBackend();
        } catch (Exception e) {
            throw new IllegalStateException("Could not save department to the backend: " + e.getMessage(), e);
        }
    }

    public static void refreshFromBackend() {
        try {
            savedDepartments.clear();
            savedCoursesByDepartment.clear();
            savedClassesByDepartment.clear();
            List<BackendApiClient.DepartmentOption> departments = BackendApiClient.getDepartments();
            for (BackendApiClient.DepartmentOption dep : departments) {
                savedDepartments.add(new String[]{String.valueOf(dep.getId()), dep.getCode(), dep.getName()});
                ArrayList<String[]> depCourses = new ArrayList<>();
                ArrayList<ArrayList<String[]>> depClasses = new ArrayList<>();
                for (BackendApiClient.CourseOption course : BackendApiClient.getCoursesForDepartment(dep.getId())) {
                    depCourses.add(new String[]{String.valueOf(course.getId()), course.getCode(),
                            course.getName(), dep.getName()});
                    ArrayList<String[]> courseClasses = new ArrayList<>();
                    for (BackendApiClient.ClassOption item : BackendApiClient.getClassesForCourse(course.getId())) {
                        courseClasses.add(new String[]{String.valueOf(item.getId()), item.getCode(),
                                item.getName(), course.getName()});
                    }
                    depClasses.add(courseClasses);
                }
                savedCoursesByDepartment.add(depCourses);
                savedClassesByDepartment.add(depClasses);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Could not load department data from the backend: " + e.getMessage(), e);
        }
    }

    public static String[][] savedDepartments() {
        refreshFromBackend();
        return savedDepartments.toArray(new String[0][]);
    }

    public static String[] savedDepartmentAt(int departmentIndex) {
        if (savedDepartments.isEmpty()) refreshFromBackend();
        return savedDepartments.get(departmentIndex);
    }

    public static String[][] savedCoursesForDepartment(int departmentIndex) {
        if (savedDepartments.isEmpty()) refreshFromBackend();
        return savedCoursesByDepartment.get(departmentIndex).toArray(new String[0][]);
    }

    public static String[] savedCourseAt(int departmentIndex, int courseIndex) {
        if (savedDepartments.isEmpty()) refreshFromBackend();
        return savedCoursesByDepartment.get(departmentIndex).get(courseIndex);
    }

    public static String[][] savedClassesForCourse(int departmentIndex, int courseIndex) {
        if (savedDepartments.isEmpty()) refreshFromBackend();
        return savedClassesByDepartment.get(departmentIndex).get(courseIndex).toArray(new String[0][]);
    }

    public static void addCourseToDepartment(int departmentIndex, String courseCode, String courseName) {
        try {
            String[] dep = savedDepartmentAt(departmentIndex);
            BackendApiClient.createCourse(value(courseCode), value(courseName), Integer.parseInt(dep[0]));
            refreshFromBackend();
        } catch (Exception e) {
            throw new IllegalStateException("Could not add course: " + e.getMessage(), e);
        }
    }

    public static void deleteCourseFromDepartment(int departmentIndex, int courseIndex) {
        try {
            int courseId = Integer.parseInt(savedCourseAt(departmentIndex, courseIndex)[0]);
            BackendApiClient.deleteCourse(courseId);
            refreshFromBackend();
        } catch (Exception e) {
            throw new IllegalStateException("Could not delete course: " + e.getMessage(), e);
        }
    }

    public static void addClassToSavedCourse(int departmentIndex, int courseIndex,
            String classCode, String className) {
        try {
            int courseId = Integer.parseInt(savedCourseAt(departmentIndex, courseIndex)[0]);
            BackendApiClient.createClass(value(classCode), value(className), courseId);
            refreshFromBackend();
        } catch (Exception e) {
            throw new IllegalStateException("Could not add class: " + e.getMessage(), e);
        }
    }

    public static void deleteClassFromSavedCourse(int departmentIndex, int courseIndex, int classIndex) {
        try {
            int classId = Integer.parseInt(savedClassesForCourse(departmentIndex, courseIndex)[classIndex][0]);
            BackendApiClient.deleteClass(classId);
            refreshFromBackend();
        } catch (Exception e) {
            throw new IllegalStateException("Could not delete class: " + e.getMessage(), e);
        }
    }

    private static String value(String text) {
        return text == null ? "" : text.trim();
    }
}
