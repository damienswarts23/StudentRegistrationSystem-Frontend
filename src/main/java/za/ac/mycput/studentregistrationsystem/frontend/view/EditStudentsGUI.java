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
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import za.ac.mycput.studentregistrationsystem.frontend.service.BackendApiClient.StudentAdminOption;

public class EditStudentsGUI extends AppFrame implements ActionListener {
    private JButton backBtn;
    private JButton editBtn;
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
        titlePnl.add(GUIStyle.heading("EDIT STUDENT", 25));
        JLabel subtitle = GUIStyle.normalLabel("Select a student to change their personal information.");
        subtitle.setForeground(GUIStyle.MUTED);
        titlePnl.add(subtitle);

        studentTable = new JTable(new DefaultTableModel(rows(),
                new String[]{"Student ID", "Student Number", "Name", "Student Email", "Course"}));
        studentTable.setRowHeight(30);
        studentTable.setFont(new Font(GUIStyle.FONT_FAMILY, Font.PLAIN, 13));
        studentTable.getTableHeader().setFont(new Font(GUIStyle.FONT_FAMILY, Font.BOLD, 13));
        studentTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && studentTable.getSelectedRow() >= 0) {
                    openSelected();
                }
            }
        });

        backBtn = GUIStyle.secondaryButton("Dashboard");
        editBtn = GUIStyle.primaryButton("Edit Selected Student");
        buttonPnl.setBackground(GUIStyle.BACKGROUND);
        buttonPnl.add(backBtn);
        buttonPnl.add(editBtn);
        backBtn.addActionListener(this);
        editBtn.addActionListener(this);
        contentPnl.add(titlePnl, BorderLayout.NORTH);
        contentPnl.add(GUIStyle.plainScrollPane(studentTable), BorderLayout.CENTER);
        contentPnl.add(buttonPnl, BorderLayout.SOUTH);
        mainPnl.add(headerPnl, BorderLayout.NORTH);
        mainPnl.add(contentPnl, BorderLayout.CENTER);
        add(mainPnl);
        setTitle("Edit Students");
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

    private void openSelected() {
        int row = studentTable.getSelectedRow();
        if (row < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Select a student to edit.");
            return;
        }
        dispose();
        new EditStudentAdminDetailsGUI(row).setGUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backBtn) {
            dispose();
            new AdminDashboardGUI().setGUI();
        } else {
            openSelected();
        }
    }
}
