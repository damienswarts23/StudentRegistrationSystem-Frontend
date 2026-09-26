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
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import za.ac.mycput.studentregistrationsystem.frontend.service.BackendApiClient.StudentAdminOption;

public class AllStudentsGUI extends JFrame implements ActionListener {
    private JButton backBtn;
    private JTable studentTable;

    public void setGUI() {
        try {
            StudentAdminData.refreshFromBackend();
        } catch (Exception exception) {
            SwingUtilitiesHelper.showBackendError(this, exception);
        }

        JPanel mainPnl = new JPanel(new BorderLayout());
        JPanel headerPnl = header();
        JPanel contentPnl = new JPanel(new BorderLayout(0, 18));
        JPanel buttonPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        contentPnl.setBackground(GUIStyle.BACKGROUND);
        contentPnl.setBorder(BorderFactory.createEmptyBorder(32, 42, 35, 42));
        JPanel titlePnl = new JPanel(new GridLayout(2, 1, 5, 5));
        titlePnl.setBackground(GUIStyle.BACKGROUND);
        titlePnl.add(GUIStyle.heading("ALL STUDENTS", 25));
        JLabel subtitle = GUIStyle.normalLabel("Click a student to view their full details.");
        subtitle.setForeground(GUIStyle.MUTED);
        titlePnl.add(subtitle);

        studentTable = new JTable(new DefaultTableModel(rows(),
                new String[]{"Student ID", "Student Number", "Name", "Student Email",
                    "Personal Email", "Course"}));
        studentTable.setRowHeight(30);
        studentTable.setFont(new Font("Arial", Font.PLAIN, 13));
        studentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        studentTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = studentTable.getSelectedRow();
                if (row >= 0) {
                    dispose();
                    new StudentAdminDetailsGUI(row).setGUI();
                }
            }
        });

        backBtn = GUIStyle.secondaryButton("Dashboard");
        buttonPnl.setBackground(GUIStyle.BACKGROUND);
        buttonPnl.add(backBtn);
        backBtn.addActionListener(this);

        contentPnl.add(titlePnl, BorderLayout.NORTH);
        contentPnl.add(GUIStyle.plainScrollPane(studentTable), BorderLayout.CENTER);
        contentPnl.add(buttonPnl, BorderLayout.SOUTH);
        mainPnl.add(headerPnl, BorderLayout.NORTH);
        mainPnl.add(contentPnl, BorderLayout.CENTER);
        add(mainPnl);
        setTitle("All Students");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1120, 680));
        setLocationRelativeTo(null);
        GUIStyle.showFrame(this);
    }

    private JPanel header() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(GUIStyle.NAVY);
        panel.setBorder(BorderFactory.createEmptyBorder(22, 34, 22, 34));
        JLabel label = new JLabel("STUDENT REGISTRATION SYSTEM");
        label.setFont(new Font("Arial", Font.BOLD, 21));
        label.setForeground(Color.WHITE);
        panel.add(label, BorderLayout.WEST);
        return panel;
    }

    private Object[][] rows() {
        List<StudentAdminOption> students = StudentAdminData.students();
        Object[][] rows = new Object[students.size()][6];
        for (int i = 0; i < students.size(); i++) {
            StudentAdminOption student = students.get(i);
            rows[i][0] = student.getStudentId();
            rows[i][1] = student.getStudentNumber();
            rows[i][2] = student.getFullName();
            rows[i][3] = student.getStudentEmail();
            rows[i][4] = student.getPersonalEmail();
            rows[i][5] = student.getCourseName();
        }
        return rows;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dispose();
        new AdminDashboardGUI().setGUI();
    }
}
