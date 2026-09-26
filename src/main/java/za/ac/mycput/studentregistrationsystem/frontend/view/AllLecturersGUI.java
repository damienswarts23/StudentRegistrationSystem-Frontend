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

public class AllLecturersGUI extends JFrame implements ActionListener {
    private JButton backBtn;
    private JTable lecturerTable;

    public void setGUI() {
        try {
            LecturerData.refreshFromBackend();
        } catch (Exception exception) {
            SwingUtilitiesHelper.showBackendError(this, exception);
        }

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
        titlePnl.add(GUIStyle.heading("ALL LECTURERS", 25));
        JLabel subtitle = GUIStyle.normalLabel("Click a lecturer to view their details.");
        subtitle.setForeground(GUIStyle.MUTED);
        titlePnl.add(subtitle);

        lecturerTable = new JTable(new DefaultTableModel(rows(), new String[]{"Lecturer ID", "Lecturer Number", "Name", "Email", "Department"}));
        lecturerTable.setRowHeight(30);
        lecturerTable.setFont(new Font("Arial", Font.PLAIN, 13));
        lecturerTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        lecturerTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = lecturerTable.getSelectedRow();
                if (row >= 0) {
                    dispose();
                    new LecturerDetailsGUI(row).setGUI();
                }
            }
        });
        JScrollPane scrollPane = GUIStyle.plainScrollPane(lecturerTable);

        backBtn = GUIStyle.secondaryButton("Dashboard");
        buttonPnl.setBackground(GUIStyle.BACKGROUND);
        buttonPnl.add(backBtn);
        contentPnl.add(titlePnl, BorderLayout.NORTH);
        contentPnl.add(scrollPane, BorderLayout.CENTER);
        contentPnl.add(buttonPnl, BorderLayout.SOUTH);
        backBtn.addActionListener(this);
        mainPnl.add(headerPnl, BorderLayout.NORTH);
        mainPnl.add(contentPnl, BorderLayout.CENTER);
        this.add(mainPnl);
        this.setTitle("All Lecturers");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(1050, 650));
        this.setLocationRelativeTo(null);
        GUIStyle.showFrame(this);
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
        this.dispose();
        new AdminDashboardGUI().setGUI();
    }
}
