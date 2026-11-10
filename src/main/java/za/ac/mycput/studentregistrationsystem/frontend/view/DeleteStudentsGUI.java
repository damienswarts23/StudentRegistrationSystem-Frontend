package za.ac.mycput.studentregistrationsystem.frontend.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import za.ac.mycput.studentregistrationsystem.frontend.service.BackendApiClient.StudentAdminOption;

public class DeleteStudentsGUI extends AppFrame implements ActionListener {
    private JButton backBtn;
    private JButton deleteBtn;
    private JTable studentTable;

    public void setGUI() {
        try {
            StudentAdminData.refreshFromBackend();
        } catch (Exception exception) {
            SwingUtilitiesHelper.showBackendError(this, exception);
        }

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
        JPanel titlePnl = new JPanel(new GridLayout(2, 1, 5, 5));
        titlePnl.setBackground(GUIStyle.BACKGROUND);
        titlePnl.add(GUIStyle.heading("DELETE STUDENT", 25));
        JLabel subtitle = GUIStyle.normalLabel(
                "Deleting a student also removes their class registrations. Their application history is kept.");
        subtitle.setForeground(GUIStyle.MUTED);
        titlePnl.add(subtitle);

        studentTable = new JTable(new DefaultTableModel(rows(),
                new String[]{"Student ID", "Student Number", "Name", "Student Email", "Course"}));
        studentTable.setRowHeight(30);
        studentTable.setFont(new Font(GUIStyle.FONT_FAMILY, Font.PLAIN, 13));
        studentTable.getTableHeader().setFont(new Font(GUIStyle.FONT_FAMILY, Font.BOLD, 13));

        backBtn = GUIStyle.secondaryButton("Dashboard");
        deleteBtn = GUIStyle.primaryButton("Delete Selected Student");
        buttonPnl.setBackground(GUIStyle.BACKGROUND);
        buttonPnl.add(backBtn);
        buttonPnl.add(deleteBtn);
        backBtn.addActionListener(this);
        deleteBtn.addActionListener(this);
        contentPnl.add(titlePnl, BorderLayout.NORTH);
        contentPnl.add(GUIStyle.plainScrollPane(studentTable), BorderLayout.CENTER);
        contentPnl.add(buttonPnl, BorderLayout.SOUTH);
        mainPnl.add(headerPnl, BorderLayout.NORTH);
        mainPnl.add(contentPnl, BorderLayout.CENTER);
        add(mainPnl);
        setTitle("Delete Student");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 670));
        setLocationRelativeTo(null);
        GUIStyle.showFrame(this);
    }

    private Object[][] rows() {
        List<StudentAdminOption> students = StudentAdminData.students();
        Object[][] rows = new Object[students.size()][5];
        for (int i = 0; i < students.size(); i++) {
            StudentAdminOption student = students.get(i);
            rows[i] = new Object[]{student.getStudentId(), student.getStudentNumber(),
                student.getFullName(), student.getStudentEmail(), student.getCourseName()};
        }
        return rows;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backBtn) {
            dispose();
            new AdminDashboardGUI().setGUI();
            return;
        }
        int row = studentTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a student to delete.");
            return;
        }
        StudentAdminOption student = StudentAdminData.studentAt(row);
        int answer = JOptionPane.showConfirmDialog(this,
                "Delete " + student.getFullName() + " (" + student.getStudentNumber() + ")?\n"
                + "Their current class registrations will also be removed.",
                "Confirm Student Deletion", JOptionPane.YES_NO_OPTION);
        if (answer != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            StudentAdminData.deleteStudent(row);
            JOptionPane.showMessageDialog(this, "Student deleted successfully.");
            dispose();
            new DeleteStudentsGUI().setGUI();
        } catch (Exception exception) {
            SwingUtilitiesHelper.showBackendError(this, exception);
        }
    }
}
