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

public class LoginGUI extends AppFrame implements ActionListener {
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
        JPanel leftPnl = RegiLearnBrand.welcomePanel(true);
        JPanel rightPnl = new JPanel(new BorderLayout());
        JPanel formPnl = GUIStyle.whiteCard();
        JPanel fieldsPnl = new JPanel(new GridLayout(8, 1, 8, 8));
        JPanel buttonPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel bottomPnl = new JPanel(new BorderLayout());
        JPanel homePnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));

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
        fieldsPnl.add(new JLabel("Use your account email and password"));
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
        rightPnl.setBorder(BorderFactory.createEmptyBorder(18, 32, 18, 32));
        rightPnl.setLayout(new java.awt.LayoutManager(){
            public void addLayoutComponent(String n,java.awt.Component c){} public void removeLayoutComponent(java.awt.Component c){}
            public Dimension preferredLayoutSize(java.awt.Container c){return new Dimension(540,520);}public Dimension minimumLayoutSize(java.awt.Container c){return new Dimension(420,470);}
            public void layoutContainer(java.awt.Container c){java.awt.Insets in=c.getInsets();int w=Math.min(480,c.getWidth()-in.left-in.right),h=Math.min(470,c.getHeight()-in.top-in.bottom);formPnl.setBounds((c.getWidth()-w)/2,(c.getHeight()-h)/2,w,h);}
        });
        formPnl.setPreferredSize(new Dimension(480,470));
        rightPnl.add(formPnl);
        mainPnl.add(leftPnl);
        mainPnl.add(rightPnl);
        homeBtn.addActionListener(this);
        loginBtn.addActionListener(this);
        getRootPane().setDefaultButton(loginBtn);
        this.add(mainPnl);
        this.setTitle("RegiLearn · Sign in");
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
        if (username.isBlank() || password.isBlank()) {
            JOptionPane.showMessageDialog(this, "Enter your email and password.");
            return;
        }
        loginBtn.setEnabled(false);
        homeBtn.setEnabled(false);
        new javax.swing.SwingWorker<String, Void>() {
            private String record;
            @Override protected String doInBackground() throws Exception {
                String role = BackendApiClient.login(username, password);
                try {
                if ("STUDENT".equals(role)) record = BackendApiClient.findStudentByEmail(BackendApiClient.currentUsername());
                if ("LECTURER".equals(role)) record = BackendApiClient.findLecturerByEmail(BackendApiClient.currentUsername());
                if (!"ADMIN".equals(role) && record == null) throw new java.io.IOException("Your linked record is unavailable. Contact an administrator.");
                } catch(Exception loadError) {
                    throw new java.io.IOException("Your password was accepted, but your account details could not be loaded: "+loadError.getMessage(),loadError);
                }
                return role;
            }
            @Override protected void done() {
                passwordTxtFld.setText("");
                try {
                    String role = get();
                    switch (role) {
                        case "ADMIN" -> new AdminDashboardGUI().setGUI();
                        case "STUDENT" -> { ApplicationPreviewData.loadBackendResult(record); new StudentDashboardGUI().setGUI(); }
                        case "LECTURER" -> new LecturerDashboardGUI(LecturerData.useBackendLecturer(record)).setGUI();
                        default -> throw new IllegalStateException("This account has no supported role.");
                    }
                    dispose();
                } catch (Exception exception) {
                    BackendApiClient.clearSession();
                    Throwable cause = exception.getCause() == null ? exception : exception.getCause();
                    JOptionPane.showMessageDialog(LoginGUI.this, cause.getMessage(), "Sign in failed", JOptionPane.ERROR_MESSAGE);
                } finally { loginBtn.setEnabled(true); homeBtn.setEnabled(true); }
            }
        }.execute();
    }
}
