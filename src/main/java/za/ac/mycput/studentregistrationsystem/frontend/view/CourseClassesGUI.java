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

public class CourseClassesGUI extends JFrame implements ActionListener {
    private final int courseIndex;
    private JButton addClassBtn;
    private JButton doneBtn;

    public CourseClassesGUI(int courseIndex) {
        this.courseIndex = courseIndex;
    }

    public void setGUI() {
        JPanel mainPnl = new JPanel(new BorderLayout());
        JPanel headerPnl = new JPanel(new BorderLayout());
        JPanel contentPnl = new JPanel(new BorderLayout(0, 18));
        JPanel topActionPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel buttonPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        String[] course = DepartmentCreationData.courseAt(courseIndex);

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
        titlePnl.add(GUIStyle.heading("ADD CLASSES", 25));
        JLabel subtitle = GUIStyle.normalLabel("Course: " + course[1] + " - " + course[2] + ".");
        subtitle.setForeground(GUIStyle.MUTED);
        titlePnl.add(subtitle);

        addClassBtn = GUIStyle.primaryButton("Add Class");
        topActionPnl.setBackground(GUIStyle.BACKGROUND);
        topActionPnl.add(addClassBtn);

        JTable classTable = new JTable(new DefaultTableModel(rows(), new String[]{"Class ID", "Class Code", "Class Name", "Course"}));
        classTable.setRowHeight(30);
        classTable.setFont(new Font("Arial", Font.PLAIN, 13));
        classTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        JScrollPane scrollPane = GUIStyle.plainScrollPane(classTable);

        doneBtn = GUIStyle.primaryButton("Done");
        buttonPnl.setBackground(GUIStyle.BACKGROUND);
        buttonPnl.add(doneBtn);
        JPanel centrePnl = new JPanel(new BorderLayout(0, 12));
        centrePnl.setBackground(GUIStyle.BACKGROUND);
        centrePnl.add(topActionPnl, BorderLayout.NORTH);
        centrePnl.add(scrollPane, BorderLayout.CENTER);
        contentPnl.add(titlePnl, BorderLayout.NORTH);
        contentPnl.add(centrePnl, BorderLayout.CENTER);
        contentPnl.add(buttonPnl, BorderLayout.SOUTH);
        addClassBtn.addActionListener(this);
        doneBtn.addActionListener(this);
        mainPnl.add(headerPnl, BorderLayout.NORTH);
        mainPnl.add(contentPnl, BorderLayout.CENTER);
        this.add(mainPnl);
        this.setTitle("Add Classes");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(1050, 650));
        this.setLocationRelativeTo(null);
        GUIStyle.showFrame(this);
    }

    private Object[][] rows() {
        String[][] classes = DepartmentCreationData.classesForCourse(courseIndex);
        Object[][] rows = new Object[classes.length][4];
        for (int i = 0; i < classes.length; i++) {
            rows[i][0] = classes[i][0];
            rows[i][1] = classes[i][1];
            rows[i][2] = classes[i][2];
            rows[i][3] = classes[i][3];
        }
        return rows;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.dispose();
        if (e.getSource() == addClassBtn) {
            new AddCourseClassGUI(courseIndex).setGUI();
        } else {
            new DepartmentCoursesGUI().setGUI();
        }
    }
}
