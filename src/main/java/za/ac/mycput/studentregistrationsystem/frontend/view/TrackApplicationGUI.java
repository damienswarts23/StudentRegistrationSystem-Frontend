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
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import za.ac.mycput.studentregistrationsystem.frontend.service.BackendApiClient;

public class TrackApplicationGUI extends JFrame implements ActionListener {
    private JTextField emailTxtFld;
    private JTextField nameTxtFld;
    private JTextField surnameTxtFld;
    private JTextField dateOfBirthTxtFld;
    private JButton homeBtn;
    private JButton checkStatusBtn;
    private boolean showStatus;

    public TrackApplicationGUI() {
        this(false);
    }

    public TrackApplicationGUI(boolean showStatus) {
        this.showStatus = showStatus;
    }

    public void setGUI() {
        JPanel mainPnl = new JPanel(new BorderLayout());
        JPanel headerPnl = new JPanel(new BorderLayout());
        JPanel contentPnl = new JPanel(new BorderLayout(0, 22));
        JPanel cardPnl = GUIStyle.whiteCard();
        JPanel searchPnl = new JPanel(new GridLayout(4, 2, 16, 14));
        JPanel buttonPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        headerPnl.setBackground(GUIStyle.NAVY);
        headerPnl.setBorder(BorderFactory.createEmptyBorder(22, 34, 22, 34));
        JLabel headerLbl = new JLabel("STUDENT REGISTRATION SYSTEM");
        headerLbl.setFont(new Font("Arial", Font.BOLD, 21));
        headerLbl.setForeground(Color.WHITE);
        headerPnl.add(headerLbl, BorderLayout.WEST);

        contentPnl.setBackground(GUIStyle.BACKGROUND);
        contentPnl.setBorder(BorderFactory.createEmptyBorder(30, 75, 35, 75));
        JPanel titlePnl = new JPanel(new GridLayout(2, 1, 5, 5));
        titlePnl.setBackground(GUIStyle.BACKGROUND);
        titlePnl.add(GUIStyle.heading(showStatus ? "APPLICATION DETAILS" : "TRACK APPLICATION", 25));
        JLabel subtitle = GUIStyle.normalLabel("Enter applicant details to check the application status.");
        subtitle.setForeground(GUIStyle.MUTED);
        titlePnl.add(subtitle);

        emailTxtFld = new JTextField(ApplicationPreviewData.email());
        nameTxtFld = new JTextField(ApplicationPreviewData.firstName());
        surnameTxtFld = new JTextField(ApplicationPreviewData.lastName());
        dateOfBirthTxtFld = new JTextField(ApplicationPreviewData.dateOfBirth());
        GUIStyle.styleField(emailTxtFld);
        GUIStyle.styleField(nameTxtFld);
        GUIStyle.styleField(surnameTxtFld);
        GUIStyle.styleField(dateOfBirthTxtFld);
        searchPnl.setBackground(GUIStyle.WHITE);
        searchPnl.add(GUIStyle.normalLabel("Email:"));
        searchPnl.add(emailTxtFld);
        searchPnl.add(GUIStyle.normalLabel("Name:"));
        searchPnl.add(nameTxtFld);
        searchPnl.add(GUIStyle.normalLabel("Surname:"));
        searchPnl.add(surnameTxtFld);
        searchPnl.add(GUIStyle.normalLabel("Date of Birth:"));
        searchPnl.add(dateOfBirthTxtFld);

        homeBtn = GUIStyle.secondaryButton("Home");
        checkStatusBtn = GUIStyle.primaryButton("Check Application Status");
        buttonPnl.setBackground(GUIStyle.WHITE);
        buttonPnl.add(homeBtn);
        buttonPnl.add(checkStatusBtn);

        cardPnl.setLayout(new BorderLayout(0, 24));
        cardPnl.add(searchPnl, BorderLayout.NORTH);
        if (showStatus) {
            JPanel statusPnl = statusPanel();
            statusPnl.setPreferredSize(new Dimension(860, 320));
            cardPnl.add(statusPnl, BorderLayout.CENTER);
        }
        cardPnl.add(buttonPnl, BorderLayout.SOUTH);
        contentPnl.add(titlePnl, BorderLayout.NORTH);
        contentPnl.add(cardPnl, BorderLayout.CENTER);
        homeBtn.addActionListener(this);
        checkStatusBtn.addActionListener(this);
        mainPnl.add(headerPnl, BorderLayout.NORTH);
        JScrollPane pageScrollPane = GUIStyle.plainScrollPane(contentPnl);
        pageScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPnl.add(pageScrollPane, BorderLayout.CENTER);
        this.add(mainPnl);
        this.setTitle("Track Application");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(1100, 820));
        this.setLocationRelativeTo(null);
        GUIStyle.showFrame(this);
    }

    private JPanel statusPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 18));
        JPanel valuesPnl = new JPanel(new GridLayout(8, 2, 18, 15));
        panel.setBackground(GUIStyle.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 8, 8, 8));
        valuesPnl.setBackground(GUIStyle.WHITE);
        addStatusRow(valuesPnl, "Application Status", "Processed");
        addStatusRow(valuesPnl, "Student Number", ApplicationPreviewData.studentNumber());
        addStatusRow(valuesPnl, "Student Email", ApplicationPreviewData.studentEmail());
        addStatusRow(valuesPnl, "Student Name", ApplicationPreviewData.fullName());
        addStatusRow(valuesPnl, "Course One", ApplicationPreviewData.courseStatus(0));
        addStatusRow(valuesPnl, "Course Two", ApplicationPreviewData.courseStatus(1));
        addStatusRow(valuesPnl, "Course Three", ApplicationPreviewData.courseStatus(2));
        addStatusRow(valuesPnl, "Next Step", "Login with student email and student number.");
        JPanel messagePnl = new JPanel(new GridLayout(2, 1, 4, 4));
        JLabel messageLbl = GUIStyle.normalLabel("Once you have been accepted and want to register for that course,");
        JLabel messageLbl2 = GUIStyle.normalLabel("log in using your student email address and your student number as a temporary password.");
        messagePnl.setBackground(GUIStyle.WHITE);
        messageLbl.setForeground(GUIStyle.MUTED);
        messageLbl2.setForeground(GUIStyle.MUTED);
        messagePnl.add(messageLbl);
        messagePnl.add(messageLbl2);
        panel.add(valuesPnl, BorderLayout.CENTER);
        panel.add(messagePnl, BorderLayout.SOUTH);
        return panel;
    }

    private void addStatusRow(JPanel panel, String labelText, String valueText) {
        JLabel label = GUIStyle.normalLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(label);
        panel.add(GUIStyle.normalLabel(valueText));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == homeBtn) {
            this.dispose();
            new HomeGUI().setGUI();
        } else {
            try {
                String result = BackendApiClient.getApplicationResult(
                        emailTxtFld.getText().trim());
                ApplicationPreviewData.loadBackendResult(result);
                this.dispose();
                new TrackApplicationGUI(true).setGUI();
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(this,
                        "No processed application was found for that email.\n"
                        + "Make sure the backend is running and the application was submitted.");
            }
        }
    }
}
