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

public class LecturerDashboardGUI extends AppFrame implements ActionListener {
    private final int lecturerIndex;
    private JButton viewStudentsBtn;
    private JButton myClassesBtn;
    private JButton logoutBtn;

    public LecturerDashboardGUI(int lecturerIndex) {
        this.lecturerIndex = lecturerIndex;
    }

    public void setGUI() {
        if (!"LECTURER".equals(za.ac.mycput.studentregistrationsystem.frontend.service.BackendApiClient.currentRole())) {
            dispose(); new LoginGUI().setGUI(); return;
        }
        JPanel mainPnl = new JPanel(new BorderLayout());
        JPanel headerPnl = new JPanel(new BorderLayout());
        JPanel titlePnl = new JPanel(new GridLayout(2, 1, 4, 4));
        JPanel logoutPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel contentPnl = new JPanel(new BorderLayout(0, 24));
        JPanel dashboardPnl = new JPanel(new BorderLayout(0, 24));
        JPanel detailsPnl = GUIStyle.whiteCard();
        JPanel valuesPnl = new JPanel(new GridLayout(6, 2, 18, 15));
        JPanel cardPnl = new JPanel(new GridLayout(1, 2, 20, 20));
        String[] lecturer = LecturerData.lecturerAt(lecturerIndex);

        headerPnl.setBackground(GUIStyle.NAVY);
        headerPnl.setBorder(BorderFactory.createEmptyBorder(18, 32, 18, 32));
        titlePnl.setBackground(GUIStyle.NAVY);
        JLabel systemLbl = new JLabel(za.ac.mycput.studentregistrationsystem.frontend.service.BackendApiClient.currentName());
        systemLbl.setFont(new Font(GUIStyle.FONT_FAMILY, Font.BOLD, 40));
        systemLbl.setForeground(Color.WHITE);
        JLabel roleLbl = new JLabel("LECTURER");
        roleLbl.setForeground(new Color(209, 213, 219));
        titlePnl.add(systemLbl);
        titlePnl.add(roleLbl);
        logoutBtn = GUIStyle.secondaryButton("Logout");
        logoutPnl.setBackground(GUIStyle.NAVY);
        logoutPnl.add(logoutBtn);
        headerPnl.add(titlePnl, BorderLayout.WEST);
        headerPnl.add(logoutPnl, BorderLayout.EAST);

        contentPnl.setBackground(GUIStyle.BACKGROUND);
        contentPnl.setBorder(BorderFactory.createEmptyBorder(18, 32, 18, 32));
        JPanel introPnl = new JPanel(new GridLayout(2, 1, 5, 5));
        introPnl.setBackground(GUIStyle.BACKGROUND);
        introPnl.add(GUIStyle.heading("LECTURER MENU", 27));
        JLabel subtitleLbl = GUIStyle.normalLabel("View lecturer details and assigned class information.");
        subtitleLbl.setForeground(GUIStyle.MUTED);
        introPnl.add(subtitleLbl);

        valuesPnl.setBackground(GUIStyle.WHITE);
        addDetailRow(valuesPnl, "Lecturer ID", lecturer[LecturerData.LECTURER_ID]);
        addDetailRow(valuesPnl, "Lecturer Number", lecturer[LecturerData.LECTURER_NUMBER]);
        addDetailRow(valuesPnl, "Lecturer Email", lecturer[LecturerData.EMAIL]);
        addDetailRow(valuesPnl, "Lecturer Name", LecturerData.fullNameAt(lecturerIndex));
        addDetailRow(valuesPnl, "Department", lecturer[LecturerData.DEPARTMENT]);
        addDetailRow(valuesPnl, "Phone Number", lecturer[LecturerData.PHONE]);
        detailsPnl.setLayout(new BorderLayout());
        detailsPnl.add(GUIStyle.plainScrollPane(valuesPnl), BorderLayout.CENTER);

        viewStudentsBtn = GUIStyle.primaryButton("View Students");
        myClassesBtn = GUIStyle.primaryButton("My Classes");
        cardPnl.setBackground(GUIStyle.BACKGROUND);
        cardPnl.add(menuCard("VIEW STUDENTS", "Open an assigned class and view student details.", viewStudentsBtn));
        cardPnl.add(menuCard("MY CLASSES", "View classes assigned to you.", myClassesBtn));

        javax.swing.JTabbedPane tabs = new javax.swing.JTabbedPane();
        tabs.addTab("Classes", cardPnl);
        tabs.addTab("My details", detailsPnl);
        JPanel introRow = new JPanel(new BorderLayout(24, 0));
        introRow.setOpaque(false);
        introRow.add(introPnl, BorderLayout.CENTER);
        introRow.add(DashboardComponents.profile(() -> { dispose(); new AdminSettingsGUI(lecturerIndex).setGUI(); }), BorderLayout.EAST);
        contentPnl.add(introRow, BorderLayout.NORTH);
        contentPnl.add(tabs, BorderLayout.CENTER);
        viewStudentsBtn.addActionListener(this);
        myClassesBtn.addActionListener(this);
        logoutBtn.addActionListener(this);
        mainPnl.add(headerPnl, BorderLayout.NORTH);
        mainPnl.add(contentPnl, BorderLayout.CENTER);
        this.add(mainPnl);
        this.setTitle("Lecturer Dashboard");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(1100, 700));
        this.setLocationRelativeTo(null);
        GUIStyle.showFrame(this);
    }

    private void addDetailRow(JPanel panel, String labelText, String valueText) {
        JLabel label = GUIStyle.normalLabel(labelText);
        label.setFont(new Font(GUIStyle.FONT_FAMILY, Font.BOLD, 13));
        panel.add(label);
        panel.add(GUIStyle.normalLabel(valueText));
    }

    private JPanel menuCard(String titleText, String infoText, JButton button) {
        return GUIStyle.actionCard(titleText, infoText, button);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == logoutBtn) {
            try { za.ac.mycput.studentregistrationsystem.frontend.service.BackendApiClient.logout(); }
            catch (Exception ex) { javax.swing.JOptionPane.showMessageDialog(this, "Signed out locally. The backend could not confirm logout."); }
            dispose(); new LoginGUI().setGUI(); return;
        }
        this.dispose();
        if (e.getSource() == viewStudentsBtn) {
            new LecturerViewStudentsGUI(lecturerIndex).setGUI();
        } else if (e.getSource() == myClassesBtn) {
            new LecturerMyClassesGUI(lecturerIndex).setGUI();
        } else {
            new HomeGUI().setGUI();
        }
    }
}
