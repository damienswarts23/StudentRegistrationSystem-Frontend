package za.ac.mycput.studentregistrationsystem.frontend.view;

import java.awt.BorderLayout;
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
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import za.ac.mycput.studentregistrationsystem.frontend.service.BackendApiClient;

public class LoginGUI extends JFrame implements ActionListener {
    private static final String ADMIN_EMAIL = "admin@cput.ac.za";
    private JTextField usernameTxtFld;
    private JPasswordField passwordTxtFld;
    private JButton loginBtn;
    private JButton homeBtn;

    public LoginGUI() {
        usernameTxtFld = new JTextField();
        passwordTxtFld = new JPasswordField();
        usernameTxtFld.setPreferredSize(new Dimension(290, 40));
        passwordTxtFld.setPreferredSize(new Dimension(290, 40));
        loginBtn = GUIStyle.primaryButton("SIGN IN");
        homeBtn = GUIStyle.secondaryButton("Home");
    }

    public void setGUI() {
        JPanel mainPnl = new JPanel(new GridLayout(1, 2));
        JPanel leftPnl = new JPanel(new GridLayout(4, 1, 10, 10));
        JPanel rightPnl = new JPanel(new BorderLayout());
        JPanel formPnl = GUIStyle.whiteCard();
        JPanel fieldsPnl = new JPanel(new GridLayout(8, 1, 8, 8));
        JPanel buttonPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel bottomPnl = new JPanel(new BorderLayout());
        JPanel homePnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        leftPnl.setBackground(GUIStyle.NAVY);
        leftPnl.setBorder(BorderFactory.createEmptyBorder(115, 55, 115, 55));
        JLabel systemLbl = new JLabel("STUDENT REGISTRATION", SwingConstants.LEFT);
        systemLbl.setFont(new Font("Arial", Font.BOLD, 30));
        systemLbl.setForeground(GUIStyle.WHITE);
        JLabel systemLbl2 = new JLabel("SYSTEM", SwingConstants.LEFT);
        systemLbl2.setFont(new Font("Arial", Font.BOLD, 30));
        systemLbl2.setForeground(GUIStyle.WHITE);
        JLabel infoLbl = new JLabel("Manage students, lecturers and academic records.");
        infoLbl.setForeground(new java.awt.Color(209, 213, 219));
        leftPnl.add(systemLbl);
        leftPnl.add(systemLbl2);
        leftPnl.add(infoLbl);
        leftPnl.add(new JLabel());

        formPnl.setLayout(new BorderLayout(0, 25));
        JPanel titlePnl = new JPanel(new GridLayout(2, 1, 5, 5));
        titlePnl.setBackground(GUIStyle.WHITE);
        titlePnl.add(GUIStyle.heading("WELCOME BACK", 26));
        JLabel subtitleLbl = GUIStyle.normalLabel("Sign in to continue");
        subtitleLbl.setForeground(GUIStyle.MUTED);
        titlePnl.add(subtitleLbl);

        fieldsPnl.setBackground(GUIStyle.WHITE);
        fieldsPnl.add(GUIStyle.normalLabel("Username"));
        fieldsPnl.add(usernameTxtFld);
        fieldsPnl.add(GUIStyle.normalLabel("Password"));
        fieldsPnl.add(passwordTxtFld);
        fieldsPnl.add(new JLabel("Admin: admin@cput.ac.za"));
        fieldsPnl.add(new JLabel("Students: use the generated student email"));
        fieldsPnl.add(new JLabel("Lecturers: use the lecturer email"));
        buttonPnl.setBackground(GUIStyle.WHITE);
        buttonPnl.add(loginBtn);
        homePnl.setBackground(GUIStyle.WHITE);
        homePnl.add(homeBtn);
        bottomPnl.setBackground(GUIStyle.WHITE);
        bottomPnl.add(buttonPnl, BorderLayout.WEST);
        bottomPnl.add(homePnl, BorderLayout.EAST);
        formPnl.add(titlePnl, BorderLayout.NORTH);
        formPnl.add(fieldsPnl, BorderLayout.CENTER);
        formPnl.add(bottomPnl, BorderLayout.SOUTH);

        rightPnl.setBackground(GUIStyle.BACKGROUND);
        rightPnl.setBorder(BorderFactory.createEmptyBorder(90, 75, 90, 75));
        rightPnl.add(formPnl, BorderLayout.CENTER);
        mainPnl.add(leftPnl);
        mainPnl.add(rightPnl);
        homeBtn.addActionListener(this);
        loginBtn.addActionListener(this);
        this.add(mainPnl);
        this.setTitle("Student Registration System - Login");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(1050, 680));
        this.setLocationRelativeTo(null);
        GUIStyle.showFrame(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == homeBtn) {
            this.dispose();
            new HomeGUI().setGUI();
            return;
        }
        String username = usernameTxtFld.getText().trim();
        String password = new String(passwordTxtFld.getPassword());
        if (password.isBlank()) {
            JOptionPane.showMessageDialog(this, "Enter any non-empty password for this static login.");
            return;
        }
        if (username.equalsIgnoreCase(ADMIN_EMAIL)) {
            this.dispose();
            new AdminDashboardGUI().setGUI();
        } else {
            try {
                String student = BackendApiClient.findStudentByEmail(username);
                if (student != null) {
                    ApplicationPreviewData.loadBackendResult(student);
                    this.dispose();
                    new StudentDashboardGUI().setGUI();
                    return;
                }
                String lecturer = BackendApiClient.findLecturerByEmail(username);
                if (lecturer != null) {
                    int lecturerIndex = LecturerData.useBackendLecturer(lecturer);
                    this.dispose();
                    new LecturerDashboardGUI(lecturerIndex).setGUI();
                    return;
                }
                JOptionPane.showMessageDialog(this, "No student or lecturer was found for that email.");
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(this,
                        "The backend could not be reached. Start it on port 8080 and try again.\n"
                        + exception.getMessage());
            }
        }
    }
}
