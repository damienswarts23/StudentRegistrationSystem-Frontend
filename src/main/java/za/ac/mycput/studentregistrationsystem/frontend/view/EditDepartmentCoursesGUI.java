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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class EditDepartmentCoursesGUI extends JFrame implements ActionListener {
    private final int departmentIndex;
    private JButton addCourseBtn;
    private JButton deleteCourseBtn;
    private JButton nextBtn;
    private JButton backBtn;
    private JTable courseTable;

    public EditDepartmentCoursesGUI(int departmentIndex) {
        this.departmentIndex = departmentIndex;
    }

    public void setGUI() {
        JPanel mainPnl = new JPanel(new BorderLayout());
        JPanel headerPnl = new JPanel(new BorderLayout());
        JPanel contentPnl = new JPanel(new BorderLayout(0, 18));
        JPanel topActionPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel buttonPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        String[] department = DepartmentCreationData.savedDepartmentAt(departmentIndex);

        headerPnl.setBackground(GUIStyle.NAVY);
        headerPnl.setBorder(BorderFactory.createEmptyBorder(22, 34, 22, 34));
        JLabel headerLbl = new JLabel("STUDENT REGISTRATION SYSTEM");
        headerLbl.setFont(new Font("Arial", Font.BOLD, 21));
        headerLbl.setForeground(Color.WHITE);
        headerPnl.add(headerLbl, BorderLayout.WEST);

        contentPnl.setBackground(GUIStyle.BACKGROUND);
        contentPnl.setBorder(BorderFactory.createEmptyBorder(32, 50, 35, 50));
        JPanel titlePnl = new JPanel(new GridLayout(2, 1, 5, 5));
        titlePnl.setBackground(GUIStyle.BACKGROUND);
        titlePnl.add(GUIStyle.heading("EDIT COURSES", 25));
        JLabel subtitle = GUIStyle.normalLabel("Department: " + department[1] + " - " + department[2] + ". Click a course to edit classes.");
        subtitle.setForeground(GUIStyle.MUTED);
        titlePnl.add(subtitle);

        addCourseBtn = GUIStyle.primaryButton("Add Course");
        deleteCourseBtn = GUIStyle.secondaryButton("Delete Course");
        deleteCourseBtn.setPreferredSize(new Dimension(135, 38));
        topActionPnl.setBackground(GUIStyle.BACKGROUND);
        topActionPnl.add(addCourseBtn);
        topActionPnl.add(deleteCourseBtn);

        courseTable = new JTable(new DefaultTableModel(rows(), new String[]{"Course ID", "Course Code", "Course Name", "Department"}));
        courseTable.setRowHeight(30);
        courseTable.setFont(new Font("Arial", Font.PLAIN, 13));
        courseTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        JScrollPane scrollPane = GUIStyle.plainScrollPane(courseTable);

        nextBtn = GUIStyle.primaryButton("Next");
        backBtn = GUIStyle.secondaryButton("Back");
        buttonPnl.setBackground(GUIStyle.BACKGROUND);
        buttonPnl.add(backBtn);
        buttonPnl.add(nextBtn);
        JPanel centrePnl = new JPanel(new BorderLayout(0, 12));
        centrePnl.setBackground(GUIStyle.BACKGROUND);
        centrePnl.add(topActionPnl, BorderLayout.NORTH);
        centrePnl.add(scrollPane, BorderLayout.CENTER);
        contentPnl.add(titlePnl, BorderLayout.NORTH);
        contentPnl.add(centrePnl, BorderLayout.CENTER);
        contentPnl.add(buttonPnl, BorderLayout.SOUTH);
        addCourseBtn.addActionListener(this);
        deleteCourseBtn.addActionListener(this);
        nextBtn.addActionListener(this);
        backBtn.addActionListener(this);
        mainPnl.add(headerPnl, BorderLayout.NORTH);
        mainPnl.add(contentPnl, BorderLayout.CENTER);
        this.add(mainPnl);
        this.setTitle("Edit Courses");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(1050, 650));
        this.setLocationRelativeTo(null);
        GUIStyle.showFrame(this);
    }

    private Object[][] rows() {
        String[][] courses = DepartmentCreationData.savedCoursesForDepartment(departmentIndex);
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
        if (e.getSource() == addCourseBtn) {
            this.dispose();
            new EditAddDepartmentCourseGUI(departmentIndex).setGUI();
        } else if (e.getSource() == deleteCourseBtn) {
            int selectedRow = courseTable.getSelectedRow();
            if (selectedRow >= 0) {
                DepartmentCreationData.deleteCourseFromDepartment(departmentIndex, selectedRow);
            }
            this.dispose();
            new EditDepartmentCoursesGUI(departmentIndex).setGUI();
        } else if (e.getSource() == nextBtn) {
            int selectedRow = courseTable.getSelectedRow();
            if (selectedRow >= 0) {
                this.dispose();
                new EditCourseClassesGUI(departmentIndex, selectedRow).setGUI();
            }
        } else {
            this.dispose();
            new EditDepartmentsGUI().setGUI();
        }
    }
}
