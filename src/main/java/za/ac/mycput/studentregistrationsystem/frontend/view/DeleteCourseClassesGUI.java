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

public class DeleteCourseClassesGUI extends AppFrame implements ActionListener {
    private final int departmentIndex;
    private final int courseIndex;
    private JButton deleteClassBtn;
    private JButton backBtn;
    private JTable classTable;

    public DeleteCourseClassesGUI(int departmentIndex, int courseIndex) {
        this.departmentIndex = departmentIndex;
        this.courseIndex = courseIndex;
    }

    public void setGUI() {
        String[] course = DepartmentCreationData.savedCourseAt(departmentIndex, courseIndex);

        JPanel mainPnl = new JPanel(new BorderLayout());
        JPanel headerPnl = new JPanel(new BorderLayout());
        JPanel contentPnl = new JPanel(new BorderLayout(0, 18));
        JPanel buttonPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        headerPnl.setBackground(GUIStyle.NAVY);
        headerPnl.setBorder(BorderFactory.createEmptyBorder(18, 32, 18, 32));
        JLabel headerLbl = new JLabel("STUDENT REGISTRATION SYSTEM");
        headerLbl.setFont(new Font(GUIStyle.FONT_FAMILY, Font.BOLD, 21));
        headerLbl.setForeground(Color.WHITE);
        headerPnl.add(headerLbl, BorderLayout.WEST);

        contentPnl.setBackground(GUIStyle.BACKGROUND);
        contentPnl.setBorder(BorderFactory.createEmptyBorder(18, 32, 18, 32));
        JPanel titlePnl = new JPanel(new GridLayout(3, 1, 5, 5));
        titlePnl.setBackground(GUIStyle.BACKGROUND);
        titlePnl.add(GUIStyle.heading("DELETE CLASSES", 25));
        JLabel courseLbl = GUIStyle.normalLabel("Course: " + course[1] + " - " + course[2]);
        courseLbl.setForeground(GUIStyle.MUTED);
        titlePnl.add(courseLbl);
        JLabel instructionLbl = GUIStyle.normalLabel(
                "Delete every class in this course before returning to delete the course.");
        instructionLbl.setForeground(GUIStyle.MUTED);
        titlePnl.add(instructionLbl);

        classTable = new JTable(new DefaultTableModel(rows(),
                new String[]{"Class ID", "Class Code", "Class Name", "Course"}));
        classTable.setRowHeight(30);
        classTable.setFont(new Font(GUIStyle.FONT_FAMILY, Font.PLAIN, 13));
        classTable.getTableHeader().setFont(new Font(GUIStyle.FONT_FAMILY, Font.BOLD, 13));
        JScrollPane scrollPane = GUIStyle.plainScrollPane(classTable);

        backBtn = GUIStyle.secondaryButton("Back to Courses");
        deleteClassBtn = GUIStyle.primaryButton("Delete Selected Class");
        buttonPnl.setBackground(GUIStyle.BACKGROUND);
        buttonPnl.add(backBtn);
        buttonPnl.add(deleteClassBtn);
        backBtn.addActionListener(this);
        deleteClassBtn.addActionListener(this);

        contentPnl.add(titlePnl, BorderLayout.NORTH);
        contentPnl.add(scrollPane, BorderLayout.CENTER);
        contentPnl.add(buttonPnl, BorderLayout.SOUTH);
        mainPnl.add(headerPnl, BorderLayout.NORTH);
        mainPnl.add(contentPnl, BorderLayout.CENTER);
        add(mainPnl);

        setTitle("Delete Classes");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1050, 650));
        setLocationRelativeTo(null);
        GUIStyle.showFrame(this);
    }

    private Object[][] rows() {
        String[][] classes = DepartmentCreationData.savedClassesForCourse(departmentIndex, courseIndex);
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
        if (e.getSource() == backBtn) {
            dispose();
            new DeleteDepartmentCoursesGUI(departmentIndex).setGUI();
            return;
        }

        int row = classTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a class to delete.");
            return;
        }
        String[] courseClass = DepartmentCreationData.savedClassesForCourse(departmentIndex, courseIndex)[row];
        int answer = JOptionPane.showConfirmDialog(this,
                "Delete class " + courseClass[1] + " - " + courseClass[2] + "?\n"
                + "Any student registrations for this class will also be removed.",
                "Confirm Class Deletion", JOptionPane.YES_NO_OPTION);
        if (answer != JOptionPane.YES_OPTION) return;

        try {
            DepartmentCreationData.deleteClassFromSavedCourse(departmentIndex, courseIndex, row);
            JOptionPane.showMessageDialog(this, "Class deleted successfully.");
            dispose();
            new DeleteCourseClassesGUI(departmentIndex, courseIndex).setGUI();
        } catch (Exception ex) {
            SwingUtilitiesHelper.showBackendError(this, ex);
        }
    }
}
