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

public class DeleteDepartmentGUI extends JFrame implements ActionListener {
    private JButton backBtn;
    private JButton openBtn;
    private JTable departmentTable;

    public void setGUI() {
        JPanel mainPnl = new JPanel(new BorderLayout());
        JPanel headerPnl = new JPanel(new BorderLayout());
        JPanel contentPnl = new JPanel(new BorderLayout(0, 18));
        JPanel buttonPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));

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
        titlePnl.add(GUIStyle.heading("DELETE DEPARTMENT", 25));
        JLabel subtitle = GUIStyle.normalLabel(
                "Select a department and open it. Delete its classes first, then its courses, then the department.");
        subtitle.setForeground(GUIStyle.MUTED);
        titlePnl.add(subtitle);

        departmentTable = new JTable(new DefaultTableModel(rows(),
                new String[]{"Department ID", "Department Code", "Department Name"}));
        departmentTable.setRowHeight(30);
        departmentTable.setFont(new Font("Arial", Font.PLAIN, 13));
        departmentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        JScrollPane scrollPane = GUIStyle.plainScrollPane(departmentTable);

        backBtn = GUIStyle.secondaryButton("Dashboard");
        openBtn = GUIStyle.primaryButton("Open Department");
        buttonPnl.setBackground(GUIStyle.BACKGROUND);
        buttonPnl.add(backBtn);
        buttonPnl.add(openBtn);
        backBtn.addActionListener(this);
        openBtn.addActionListener(this);

        contentPnl.add(titlePnl, BorderLayout.NORTH);
        contentPnl.add(scrollPane, BorderLayout.CENTER);
        contentPnl.add(buttonPnl, BorderLayout.SOUTH);
        mainPnl.add(headerPnl, BorderLayout.NORTH);
        mainPnl.add(contentPnl, BorderLayout.CENTER);
        add(mainPnl);
        setTitle("Delete Department");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1050, 650));
        setLocationRelativeTo(null);
        GUIStyle.showFrame(this);
    }

    private Object[][] rows() {
        String[][] departments = DepartmentCreationData.savedDepartments();
        Object[][] rows = new Object[departments.length][3];
        for (int i = 0; i < departments.length; i++) {
            rows[i][0] = departments[i][0];
            rows[i][1] = departments[i][1];
            rows[i][2] = departments[i][2];
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

        int row = departmentTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a department first.");
            return;
        }
        dispose();
        new DeleteDepartmentCoursesGUI(row).setGUI();
    }
}
