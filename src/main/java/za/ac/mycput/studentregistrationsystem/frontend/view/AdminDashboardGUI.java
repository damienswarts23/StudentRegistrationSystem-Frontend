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
import javax.swing.JTabbedPane;

public class AdminDashboardGUI extends JFrame implements ActionListener {
    private JButton addLecturerBtn;
    private JButton allLecturersBtn;
    private JButton editLecturerBtn;
    private JButton deleteLecturerBtn;
    private JButton departmentsBtn;
    private JButton viewDepartmentsBtn;
    private JButton editDepartmentsBtn;
    private JButton deleteDepartmentBtn;
    private JButton allStudentsBtn;
    private JButton editStudentBtn;
    private JButton deleteStudentBtn;
    private JButton logoutBtn;

    public void setGUI() {
        JPanel mainPnl = new JPanel(new BorderLayout());
        JPanel headerPnl = new JPanel(new BorderLayout());
        JPanel titlePnl = new JPanel(new GridLayout(2, 1, 4, 4));
        JPanel logoutPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel contentPnl = new JPanel(new BorderLayout(0, 24));
        JTabbedPane tabbedPane = new JTabbedPane();

        headerPnl.setBackground(GUIStyle.NAVY);
        headerPnl.setBorder(BorderFactory.createEmptyBorder(22, 35, 22, 35));
        titlePnl.setBackground(GUIStyle.NAVY);
        JLabel systemLbl = new JLabel("STUDENT REGISTRATION SYSTEM");
        systemLbl.setFont(new Font("Arial", Font.BOLD, 23));
        systemLbl.setForeground(Color.WHITE);
        JLabel roleLbl = new JLabel("ADMINISTRATOR");
        roleLbl.setForeground(new Color(209, 213, 219));
        titlePnl.add(systemLbl);
        titlePnl.add(roleLbl);
        logoutBtn = GUIStyle.secondaryButton("Logout");
        logoutPnl.setBackground(GUIStyle.NAVY);
        logoutPnl.add(logoutBtn);
        headerPnl.add(titlePnl, BorderLayout.WEST);
        headerPnl.add(logoutPnl, BorderLayout.EAST);

        contentPnl.setBackground(GUIStyle.BACKGROUND);
        contentPnl.setBorder(BorderFactory.createEmptyBorder(32, 35, 35, 35));
        JPanel introPnl = new JPanel(new GridLayout(2, 1, 5, 5));
        introPnl.setBackground(GUIStyle.BACKGROUND);
        introPnl.add(GUIStyle.heading("ADMIN MENU", 27));
        JLabel subtitleLbl = GUIStyle.normalLabel("Select an admin area to continue.");
        subtitleLbl.setForeground(GUIStyle.MUTED);
        introPnl.add(subtitleLbl);

        tabbedPane.addTab("Lecturers", lecturerTab());
        tabbedPane.addTab("Departments", departmentTab());
        tabbedPane.addTab("Students", studentTab());
        contentPnl.add(introPnl, BorderLayout.NORTH);
        contentPnl.add(tabbedPane, BorderLayout.CENTER);
        logoutBtn.addActionListener(this);
        mainPnl.add(headerPnl, BorderLayout.NORTH);
        mainPnl.add(contentPnl, BorderLayout.CENTER);
        this.add(mainPnl);
        this.setTitle("Administrator Dashboard");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(1150, 720));
        this.setLocationRelativeTo(null);
        GUIStyle.showFrame(this);
    }

    private JPanel lecturerTab() {
        JPanel tabPnl = new JPanel(new GridLayout(2, 2, 20, 20));
        tabPnl.setBackground(GUIStyle.BACKGROUND);
        addLecturerBtn = GUIStyle.primaryButton("Add Lecturer");
        allLecturersBtn = GUIStyle.primaryButton("All Lecturers");
        editLecturerBtn = GUIStyle.primaryButton("Edit Lecturer");
        deleteLecturerBtn = GUIStyle.primaryButton("Delete Lecturer");
        addLecturerBtn.addActionListener(this);
        allLecturersBtn.addActionListener(this);
        editLecturerBtn.addActionListener(this);
        deleteLecturerBtn.addActionListener(this);
        tabPnl.add(menuCard("ADD LECTURER", "Create a lecturer and assign classes.", addLecturerBtn));
        tabPnl.add(menuCard("ALL LECTURERS", "View lecturer personal and lecturer details.", allLecturersBtn));
        tabPnl.add(menuCard("EDIT LECTURER", "Update lecturer details and class assignments.", editLecturerBtn));
        tabPnl.add(menuCard("DELETE LECTURER", "Remove a lecturer from the system.", deleteLecturerBtn));
        return tabPnl;
    }

    private JPanel departmentTab() {
        JPanel tabPnl = new JPanel(new GridLayout(2, 2, 20, 20));
        tabPnl.setBackground(GUIStyle.BACKGROUND);
        departmentsBtn = GUIStyle.primaryButton("Create Department");
        viewDepartmentsBtn = GUIStyle.primaryButton("View Department");
        editDepartmentsBtn = GUIStyle.primaryButton("Edit Department");
        deleteDepartmentBtn = GUIStyle.primaryButton("Delete Department");
        departmentsBtn.addActionListener(this);
        viewDepartmentsBtn.addActionListener(this);
        editDepartmentsBtn.addActionListener(this);
        deleteDepartmentBtn.addActionListener(this);
        tabPnl.add(menuCard("CREATE DEPARTMENT", "Create a department with courses and classes.", departmentsBtn));
        tabPnl.add(menuCard("VIEW DEPARTMENT", "View department, course and class details.", viewDepartmentsBtn));
        tabPnl.add(menuCard("EDIT DEPARTMENT", "Add or delete courses and classes.", editDepartmentsBtn));
        tabPnl.add(menuCard("DELETE DEPARTMENT", "Delete classes, then courses, then the department.", deleteDepartmentBtn));
        return tabPnl;
    }

    private JPanel studentTab() {
        JPanel tabPnl = new JPanel(new GridLayout(1, 3, 20, 20));
        tabPnl.setBackground(GUIStyle.BACKGROUND);
        allStudentsBtn = GUIStyle.primaryButton("View All Students");
        editStudentBtn = GUIStyle.primaryButton("Edit Student");
        deleteStudentBtn = GUIStyle.primaryButton("Delete Student");
        allStudentsBtn.addActionListener(this);
        editStudentBtn.addActionListener(this);
        deleteStudentBtn.addActionListener(this);
        tabPnl.add(menuCard("VIEW ALL STUDENTS", "View all registered students and their details.", allStudentsBtn));
        tabPnl.add(menuCard("EDIT STUDENT", "Change a student's personal details.", editStudentBtn));
        tabPnl.add(menuCard("DELETE STUDENT", "Remove a student and their class registrations.", deleteStudentBtn));
        return tabPnl;
    }

    private JPanel menuCard(String titleText, String infoText, JButton button) {
        JPanel card = GUIStyle.whiteCard();
        JPanel btnPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        card.setLayout(new BorderLayout(0, 15));
        btnPnl.setBackground(GUIStyle.WHITE);
        btnPnl.add(button);
        card.add(GUIStyle.heading(titleText, 17), BorderLayout.NORTH);
        card.add(GUIStyle.normalLabel(infoText), BorderLayout.CENTER);
        card.add(btnPnl, BorderLayout.SOUTH);
        return card;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.dispose();
        if (e.getSource() == logoutBtn) {
            new LoginGUI().setGUI();
        } else if (e.getSource() == addLecturerBtn) {
            new AddLecturerGUI().setGUI();
        } else if (e.getSource() == allLecturersBtn) {
            new AllLecturersGUI().setGUI();
        } else if (e.getSource() == editLecturerBtn) {
            new EditLecturerGUI().setGUI();
        } else if (e.getSource() == deleteLecturerBtn) {
            new DeleteLecturerGUI().setGUI();
        } else if (e.getSource() == departmentsBtn) {
            new DepartmentsCreationGUI().setGUI();
        } else if (e.getSource() == viewDepartmentsBtn) {
            new ViewDepartmentsGUI().setGUI();
        } else if (e.getSource() == editDepartmentsBtn) {
            new EditDepartmentsGUI().setGUI();
        } else if (e.getSource() == deleteDepartmentBtn) {
            new DeleteDepartmentGUI().setGUI();
        } else if (e.getSource() == allStudentsBtn) {
            new AllStudentsGUI().setGUI();
        } else if (e.getSource() == editStudentBtn) {
            new EditStudentsGUI().setGUI();
        } else if (e.getSource() == deleteStudentBtn) {
            new DeleteStudentsGUI().setGUI();
        } else {
            new AdminDashboardGUI().setGUI();
        }
    }
}
