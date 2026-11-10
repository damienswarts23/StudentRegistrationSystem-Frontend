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

public class StudentDashboardGUI extends AppFrame implements ActionListener {
    private JButton registerBtn;
    private JButton myClassesBtn;
    private JButton deregisterBtn;
    private JButton logoutBtn;

    public void setGUI() {
        if (!"STUDENT".equals(za.ac.mycput.studentregistrationsystem.frontend.service.BackendApiClient.currentRole())) {
            dispose(); new LoginGUI().setGUI(); return;
        }
        JPanel mainPnl = new JPanel(new BorderLayout());
        JPanel headerPnl = new JPanel(new BorderLayout());
        JPanel titlePnl = new JPanel(new GridLayout(2, 1, 4, 4));
        JPanel logoutPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel contentPnl = new JPanel(new BorderLayout(0, 24));
        JPanel dashboardPnl = new JPanel(new BorderLayout(0, 24));
        JPanel detailsPnl = GUIStyle.whiteCard();
        JPanel valuesPnl = new JPanel(new GridLayout(5, 2, 18, 15));
        JPanel cardPnl = new JPanel(new GridLayout(1, 3, 20, 20));

        headerPnl.setBackground(GUIStyle.NAVY);
        headerPnl.setBorder(BorderFactory.createEmptyBorder(18, 32, 18, 32));
        titlePnl.setBackground(GUIStyle.NAVY);
        JLabel systemLbl = new JLabel(za.ac.mycput.studentregistrationsystem.frontend.service.BackendApiClient.currentName());
        systemLbl.setFont(new Font(GUIStyle.FONT_FAMILY, Font.BOLD, 40));
        systemLbl.setForeground(Color.WHITE);
        JLabel roleLbl = new JLabel("STUDENT");
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
        introPnl.add(GUIStyle.heading("STUDENT MENU", 27));
        JLabel subtitleLbl = GUIStyle.normalLabel("View your student details and manage class registration.");
        subtitleLbl.setForeground(GUIStyle.MUTED);
        introPnl.add(subtitleLbl);

        valuesPnl.setBackground(GUIStyle.WHITE);
        addDetailRow(valuesPnl, "Student Number", ApplicationPreviewData.studentNumber());
        addDetailRow(valuesPnl, "Student Email", ApplicationPreviewData.studentEmail());
        addDetailRow(valuesPnl, "Student Name", ApplicationPreviewData.fullName());
        addDetailRow(valuesPnl, "Date of Birth", ApplicationPreviewData.dateOfBirth());
        addDetailRow(valuesPnl, "Accepted Course", ApplicationPreviewData.acceptedCourse());
        detailsPnl.setLayout(new BorderLayout());
        detailsPnl.add(GUIStyle.plainScrollPane(valuesPnl), BorderLayout.CENTER);

        registerBtn = GUIStyle.primaryButton("Register");
        myClassesBtn = GUIStyle.primaryButton("My Classes");
        deregisterBtn = GUIStyle.primaryButton("Deregister");
        cardPnl.setBackground(GUIStyle.BACKGROUND);
        cardPnl.add(menuCard("REGISTER", "Choose a class under your accepted course.", registerBtn));
        cardPnl.add(menuCard("MY CLASSES", "View classes you have registered for.", myClassesBtn));
        cardPnl.add(menuCard("DEREGISTER", "Remove one of your registered classes.", deregisterBtn));

        javax.swing.JTabbedPane tabs = new javax.swing.JTabbedPane();
        tabs.addTab("Classes", cardPnl);
        tabs.addTab("My details", detailsPnl);
        JPanel introRow = new JPanel(new BorderLayout(24, 0));
        introRow.setOpaque(false);
        introRow.add(introPnl, BorderLayout.CENTER);
        introRow.add(DashboardComponents.profile(() -> { dispose(); new AdminSettingsGUI().setGUI(); }), BorderLayout.EAST);
        contentPnl.add(introRow, BorderLayout.NORTH);
        contentPnl.add(tabs, BorderLayout.CENTER);
        registerBtn.addActionListener(this);
        myClassesBtn.addActionListener(this);
        deregisterBtn.addActionListener(this);
        logoutBtn.addActionListener(this);
        mainPnl.add(headerPnl, BorderLayout.NORTH);
        mainPnl.add(contentPnl, BorderLayout.CENTER);
        this.add(mainPnl);
        this.setTitle("Student Dashboard");
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
        if (e.getSource() == registerBtn) {
            new RegisterClassGUI().setGUI();
        } else if (e.getSource() == myClassesBtn) {
            new MyClassesGUI().setGUI();
        } else if (e.getSource() == deregisterBtn) {
            new DeregisterClassGUI().setGUI();

        } else {
            new HomeGUI().setGUI();
        }
    }
}
