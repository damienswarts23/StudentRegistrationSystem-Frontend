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

public class DeleteLecturerGUI extends JFrame implements ActionListener {
    private JButton backBtn;
    private JButton deleteBtn;
    private JTable lecturerTable;

    public void setGUI() {
        try {
            LecturerData.refreshFromBackend();
        } catch (Exception exception) {
            SwingUtilitiesHelper.showBackendError(this, exception);
        }

        JPanel mainPnl = new JPanel(new BorderLayout());
        JPanel headerPnl = header();
        JPanel contentPnl = new JPanel(new BorderLayout(0, 18));
        JPanel buttonPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        contentPnl.setBackground(GUIStyle.BACKGROUND);
        contentPnl.setBorder(BorderFactory.createEmptyBorder(32, 50, 35, 50));
        JPanel titlePnl = new JPanel(new GridLayout(2, 1, 5, 5));
        titlePnl.setBackground(GUIStyle.BACKGROUND);
        titlePnl.add(GUIStyle.heading("DELETE LECTURER", 25));
        JLabel subtitle = GUIStyle.normalLabel("Select a lecturer, then confirm the deletion.");
        subtitle.setForeground(GUIStyle.MUTED);
        titlePnl.add(subtitle);

        lecturerTable = new JTable(new DefaultTableModel(rows(),
                new String[]{"Lecturer ID", "Lecturer Number", "Name", "CPUT Email", "Department"}));
        lecturerTable.setRowHeight(30);
        lecturerTable.setFont(new Font("Arial", Font.PLAIN, 13));
        lecturerTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        JScrollPane scrollPane = GUIStyle.plainScrollPane(lecturerTable);

        backBtn = GUIStyle.secondaryButton("Dashboard");
        deleteBtn = GUIStyle.primaryButton("Delete Selected Lecturer");
        buttonPnl.setBackground(GUIStyle.BACKGROUND);
        buttonPnl.add(backBtn);
        buttonPnl.add(deleteBtn);
        backBtn.addActionListener(this);
        deleteBtn.addActionListener(this);

        contentPnl.add(titlePnl, BorderLayout.NORTH);
        contentPnl.add(scrollPane, BorderLayout.CENTER);
        contentPnl.add(buttonPnl, BorderLayout.SOUTH);
        mainPnl.add(headerPnl, BorderLayout.NORTH);
        mainPnl.add(contentPnl, BorderLayout.CENTER);
        add(mainPnl);
        setTitle("Delete Lecturer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1050, 650));
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
        String[][] lecturers = LecturerData.lecturers();
        Object[][] rows = new Object[lecturers.length][5];
        for (int i = 0; i < lecturers.length; i++) {
            rows[i][0] = lecturers[i][LecturerData.LECTURER_ID];
            rows[i][1] = lecturers[i][LecturerData.LECTURER_NUMBER];
            rows[i][2] = lecturers[i][LecturerData.FIRST_NAME] + " " + lecturers[i][LecturerData.LAST_NAME];
            rows[i][3] = lecturers[i][LecturerData.EMAIL];
            rows[i][4] = lecturers[i][LecturerData.DEPARTMENT];
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

        int row = lecturerTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a lecturer to delete.");
            return;
        }

        String[] lecturer = LecturerData.lecturerAt(row);
        String name = lecturer[LecturerData.FIRST_NAME] + " " + lecturer[LecturerData.LAST_NAME];
        int answer = JOptionPane.showConfirmDialog(this,
                "Delete " + name + " (" + lecturer[LecturerData.LECTURER_NUMBER] + ")?\n"
                + "They will also be removed from all assigned classes.",
                "Confirm Lecturer Deletion", JOptionPane.YES_NO_OPTION);
        if (answer != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            BackendApiClient.deleteLecturer(Integer.parseInt(lecturer[LecturerData.PERSON_ID]));
            JOptionPane.showMessageDialog(this, "Lecturer deleted successfully.");
            dispose();
            new DeleteLecturerGUI().setGUI();
        } catch (Exception exception) {
            SwingUtilitiesHelper.showBackendError(this, exception);
        }
    }
}
