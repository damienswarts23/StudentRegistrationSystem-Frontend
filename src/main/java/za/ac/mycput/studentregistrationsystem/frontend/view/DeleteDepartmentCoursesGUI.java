package za.ac.mycput.studentregistrationsystem.frontend.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import za.ac.mycput.studentregistrationsystem.frontend.service.BackendApiClient;

public class DeleteDepartmentCoursesGUI extends JFrame implements ActionListener {
    private final int departmentIndex;
    private JButton openCourseBtn;
    private JButton deleteCourseBtn;
    private JButton deleteDepartmentBtn;
    private JButton backBtn;
    private JTable courseTable;

    public DeleteDepartmentCoursesGUI(int departmentIndex) {
        this.departmentIndex = departmentIndex;
    }

    public void setGUI() {
        String[] department = DepartmentCreationData.savedDepartmentAt(departmentIndex);
        String[][] courses = DepartmentCreationData.savedCoursesForDepartment(departmentIndex);

        JPanel mainPnl = new JPanel(new BorderLayout());
        JPanel headerPnl = new JPanel(new BorderLayout());
        JPanel contentPnl = new JPanel(new BorderLayout(0, 18));
        JPanel buttonPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel courseActionPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));

        headerPnl.setBackground(GUIStyle.NAVY);
        headerPnl.setBorder(BorderFactory.createEmptyBorder(22, 34, 22, 34));
        JLabel headerLbl = new JLabel("STUDENT REGISTRATION SYSTEM");
        headerLbl.setFont(new Font("Arial", Font.BOLD, 21));
        headerLbl.setForeground(Color.WHITE);
        headerPnl.add(headerLbl, BorderLayout.WEST);

        contentPnl.setBackground(GUIStyle.BACKGROUND);
        contentPnl.setBorder(BorderFactory.createEmptyBorder(32, 50, 35, 50));
        JPanel titlePnl = new JPanel(new GridLayout(3, 1, 5, 5));
        titlePnl.setBackground(GUIStyle.BACKGROUND);
        titlePnl.add(GUIStyle.heading("DELETE DEPARTMENT CONTENT", 25));
        JLabel departmentLbl = GUIStyle.normalLabel(
                "Department: " + department[1] + " - " + department[2]);
        departmentLbl.setForeground(GUIStyle.MUTED);
        titlePnl.add(departmentLbl);
        JLabel instructionLbl = GUIStyle.normalLabel(
                "Open each course and delete its classes. An empty course can then be deleted.");
        instructionLbl.setForeground(GUIStyle.MUTED);
        titlePnl.add(instructionLbl);

        courseTable = new JTable(new DefaultTableModel(rows(courses),
                new String[]{"Course ID", "Course Code", "Course Name", "Department"}));
        courseTable.setRowHeight(30);
        courseTable.setFont(new Font("Arial", Font.PLAIN, 13));
        courseTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        JScrollPane scrollPane = GUIStyle.plainScrollPane(courseTable);

        openCourseBtn = GUIStyle.primaryButton("Open Course");
        deleteCourseBtn = GUIStyle.secondaryButton("Delete Empty Course");
        deleteCourseBtn.setPreferredSize(new Dimension(175, 38));
        courseActionPnl.setBackground(GUIStyle.BACKGROUND);
        courseActionPnl.add(openCourseBtn);
        courseActionPnl.add(deleteCourseBtn);

        backBtn = GUIStyle.secondaryButton("Back");
        deleteDepartmentBtn = GUIStyle.primaryButton("Delete Department");
        deleteDepartmentBtn.setEnabled(courses.length == 0);
        buttonPnl.setBackground(GUIStyle.BACKGROUND);
        buttonPnl.add(backBtn);
        buttonPnl.add(deleteDepartmentBtn);

        JPanel centrePnl = new JPanel(new BorderLayout(0, 12));
        centrePnl.setBackground(GUIStyle.BACKGROUND);
        centrePnl.add(courseActionPnl, BorderLayout.NORTH);
        centrePnl.add(scrollPane, BorderLayout.CENTER);

        contentPnl.add(titlePnl, BorderLayout.NORTH);
        contentPnl.add(centrePnl, BorderLayout.CENTER);
        contentPnl.add(buttonPnl, BorderLayout.SOUTH);
        mainPnl.add(headerPnl, BorderLayout.NORTH);
        mainPnl.add(contentPnl, BorderLayout.CENTER);
        add(mainPnl);

        openCourseBtn.addActionListener(this);
        deleteCourseBtn.addActionListener(this);
        deleteDepartmentBtn.addActionListener(this);
        backBtn.addActionListener(this);

        setTitle("Delete Department Content");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1050, 650));
        setLocationRelativeTo(null);
        GUIStyle.showFrame(this);
    }

    private Object[][] rows(String[][] courses) {
        Object[][] rows = new Object[courses.length][4];
        for (int i = 0; i < courses.length; i++) {
            rows[i][0] = courses[i][0];
            rows[i][1] = courses[i][1];
            rows[i][2] = courses[i][2];
            rows[i][3] = courses[i][3];
        }
        return rows;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backBtn) {
            dispose();
            new DeleteDepartmentGUI().setGUI();
            return;
        }

        if (e.getSource() == openCourseBtn) {
            int row = courseTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a course to open.");
                return;
            }
            dispose();
            new DeleteCourseClassesGUI(departmentIndex, row).setGUI();
            return;
        }

        if (e.getSource() == deleteCourseBtn) {
            int row = courseTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a course to delete.");
                return;
            }
            String[] course = DepartmentCreationData.savedCourseAt(departmentIndex, row);
            if (DepartmentCreationData.savedClassesForCourse(departmentIndex, row).length > 0) {
                JOptionPane.showMessageDialog(this,
                        "Delete all classes inside " + course[1] + " before deleting the course.");
                return;
            }
            int answer = JOptionPane.showConfirmDialog(this,
                    "Delete course " + course[1] + " - " + course[2] + "?",
                    "Confirm Course Deletion", JOptionPane.YES_NO_OPTION);
            if (answer != JOptionPane.YES_OPTION) return;
            try {
                DepartmentCreationData.deleteCourseFromDepartment(departmentIndex, row);
                JOptionPane.showMessageDialog(this, "Course deleted successfully.");
                dispose();
                new DeleteDepartmentCoursesGUI(departmentIndex).setGUI();
            } catch (Exception ex) {
                SwingUtilitiesHelper.showBackendError(this, ex);
            }
            return;
        }

        if (e.getSource() == deleteDepartmentBtn) {
            if (DepartmentCreationData.savedCoursesForDepartment(departmentIndex).length > 0) {
                JOptionPane.showMessageDialog(this, "Delete all courses before deleting the department.");
                return;
            }
            String[] department = DepartmentCreationData.savedDepartmentAt(departmentIndex);
            int answer = JOptionPane.showConfirmDialog(this,
                    "Delete department " + department[1] + " - " + department[2] + "?",
                    "Confirm Department Deletion", JOptionPane.YES_NO_OPTION);
            if (answer != JOptionPane.YES_OPTION) return;
            try {
                BackendApiClient.deleteDepartment(Integer.parseInt(department[0]));
                JOptionPane.showMessageDialog(this, "Department deleted successfully.");
                dispose();
                new DeleteDepartmentGUI().setGUI();
            } catch (Exception ex) {
                SwingUtilitiesHelper.showBackendError(this, ex);
            }
        }
    }
}
