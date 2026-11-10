package za.ac.mycput.studentregistrationsystem.frontend.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class DepartmentCoursesGUI extends AppFrame implements ActionListener {
    private JButton addCourseBtn;
    private JButton completeDepartmentBtn;
    private JTable courseTable;

    public void setGUI() {
        JPanel mainPnl = new JPanel(new BorderLayout());
        JPanel headerPnl = new JPanel(new BorderLayout());
        JPanel contentPnl = new JPanel(new BorderLayout(0, 18));
        JPanel topActionPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel buttonPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        String[] department = DepartmentCreationData.department();

        headerPnl.setBackground(GUIStyle.NAVY);
        headerPnl.setBorder(BorderFactory.createEmptyBorder(18, 32, 18, 32));
        JLabel headerLbl = new JLabel("STUDENT REGISTRATION SYSTEM");
        headerLbl.setFont(new Font(GUIStyle.FONT_FAMILY, Font.BOLD, 21));
        headerLbl.setForeground(Color.WHITE);
        headerPnl.add(headerLbl, BorderLayout.WEST);

        contentPnl.setBackground(GUIStyle.BACKGROUND);
        contentPnl.setBorder(BorderFactory.createEmptyBorder(18, 32, 18, 32));
        JPanel titlePnl = new JPanel(new GridLayout(2, 1, 5, 5));
        titlePnl.setBackground(GUIStyle.BACKGROUND);
        titlePnl.add(GUIStyle.heading("ADD COURSES", 25));
        JLabel subtitle = GUIStyle.normalLabel("Department: " + department[1] + " - " + department[2] + ". Click a course to add classes.");
        subtitle.setForeground(GUIStyle.MUTED);
        titlePnl.add(subtitle);

        addCourseBtn = GUIStyle.primaryButton("Add Course");
        topActionPnl.setBackground(GUIStyle.BACKGROUND);
        topActionPnl.add(addCourseBtn);

        courseTable = new JTable(new DefaultTableModel(rows(), new String[]{"Course ID", "Course Code", "Course Name", "Department"}));
        courseTable.setRowHeight(30);
        courseTable.setFont(new Font(GUIStyle.FONT_FAMILY, Font.PLAIN, 13));
        courseTable.getTableHeader().setFont(new Font(GUIStyle.FONT_FAMILY, Font.BOLD, 13));
        courseTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = courseTable.getSelectedRow();
                if (row >= 0) {
                    dispose();
                    new CourseClassesGUI(row).setGUI();
                }
            }
        });
        JScrollPane scrollPane = GUIStyle.plainScrollPane(courseTable);

        completeDepartmentBtn = GUIStyle.primaryButton("Complete Department Creation");
        completeDepartmentBtn.setPreferredSize(new Dimension(230, 38));
        buttonPnl.setBackground(GUIStyle.BACKGROUND);
        buttonPnl.add(completeDepartmentBtn);
        JPanel centrePnl = new JPanel(new BorderLayout(0, 12));
        centrePnl.setBackground(GUIStyle.BACKGROUND);
        centrePnl.add(topActionPnl, BorderLayout.NORTH);
        centrePnl.add(scrollPane, BorderLayout.CENTER);
        contentPnl.add(titlePnl, BorderLayout.NORTH);
        contentPnl.add(centrePnl, BorderLayout.CENTER);
        contentPnl.add(buttonPnl, BorderLayout.SOUTH);
        addCourseBtn.addActionListener(this);
        completeDepartmentBtn.addActionListener(this);
        mainPnl.add(headerPnl, BorderLayout.NORTH);
        mainPnl.add(contentPnl, BorderLayout.CENTER);
        this.add(mainPnl);
        this.setTitle("Add Courses");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(1050, 650));
        this.setLocationRelativeTo(null);
        GUIStyle.showFrame(this);
    }

    private Object[][] rows() {
        String[][] courses = DepartmentCreationData.courses();
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
        this.dispose();
        if (e.getSource() == addCourseBtn) {
            new AddDepartmentCourseGUI().setGUI();
        } else {
            DepartmentCreationData.completeDepartment();
            new AdminDashboardGUI().setGUI();
        }
    }
}
