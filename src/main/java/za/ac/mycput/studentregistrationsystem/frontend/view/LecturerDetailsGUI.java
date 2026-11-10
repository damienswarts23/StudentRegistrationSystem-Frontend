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

public class LecturerDetailsGUI extends AppFrame implements ActionListener {
    private final int lecturerIndex;
    private JButton backBtn;

    public LecturerDetailsGUI(int lecturerIndex) {
        this.lecturerIndex = lecturerIndex;
    }

    public void setGUI() {
        JPanel mainPnl = new JPanel(new BorderLayout());
        JPanel headerPnl = new JPanel(new BorderLayout());
        JPanel contentPnl = new JPanel(new BorderLayout(0, 22));
        JPanel cardPnl = GUIStyle.whiteCard();
        JPanel sectionsPnl = new JPanel(new GridLayout(2, 1, 0, 22));
        JPanel personalPnl = new JPanel(new GridLayout(0, 2, 18, 15));
        JPanel lecturerPnl = new JPanel(new GridLayout(0, 2, 18, 15));
        JPanel buttonPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        String[] lecturer = LecturerData.lecturerAt(lecturerIndex);

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
        titlePnl.add(GUIStyle.heading("LECTURER DETAILS", 25));
        JLabel subtitle = GUIStyle.normalLabel("Personal details and lecturer details.");
        subtitle.setForeground(GUIStyle.MUTED);
        titlePnl.add(subtitle);

        sectionsPnl.setBackground(GUIStyle.WHITE);
        personalPnl.setBackground(GUIStyle.WHITE);
        lecturerPnl.setBackground(GUIStyle.WHITE);
        addSectionTitle(personalPnl, "Personal Details");
        addRow(personalPnl, "Person ID", lecturer[LecturerData.PERSON_ID]);
        addRow(personalPnl, "Name", lecturer[LecturerData.FIRST_NAME] + " " + lecturer[LecturerData.LAST_NAME]);
        addRow(personalPnl, "Date of Birth", lecturer[LecturerData.DATE_OF_BIRTH]);
        addRow(personalPnl, "Gender", lecturer[LecturerData.GENDER]);
        addRow(personalPnl, "Race", lecturer[LecturerData.RACE]);
        addRow(personalPnl, "Personal Email", lecturer[LecturerData.PERSONAL_EMAIL]);
        addRow(personalPnl, "Phone Number", lecturer[LecturerData.PHONE]);
        addRow(personalPnl, "Address", lecturer[LecturerData.ADDRESS]);
        addSectionTitle(lecturerPnl, "Lecturer Details");
        addRow(lecturerPnl, "Lecturer ID", lecturer[LecturerData.LECTURER_ID]);
        addRow(lecturerPnl, "Lecturer Number", lecturer[LecturerData.LECTURER_NUMBER]);
        addRow(lecturerPnl, "Lecturer Email", lecturer[LecturerData.EMAIL]);
        addRow(lecturerPnl, "Department", lecturer[LecturerData.DEPARTMENT]);
        sectionsPnl.add(personalPnl);
        sectionsPnl.add(lecturerPnl);

        backBtn = GUIStyle.secondaryButton("Back");
        buttonPnl.setBackground(GUIStyle.WHITE);
        JButton enableLogin=GUIStyle.primaryButton("Enable lecturer login");
        enableLogin.addActionListener(event -> {
            String password=LecturerLoginDialog.choosePassword(this);
            if(password==null)return;
            enableLogin.setEnabled(false);
            new javax.swing.SwingWorker<String,Void>() {
                protected String doInBackground() throws Exception {
                    return za.ac.mycput.studentregistrationsystem.frontend.service.BackendApiClient.enableLecturerLogin(
                        lecturer[LecturerData.PERSON_ID],password);
                }
                protected void done() {
                    enableLogin.setEnabled(true);
                    try {javax.swing.JOptionPane.showMessageDialog(LecturerDetailsGUI.this,
                        "Lecturer login enabled.\nUsername: "+get()+"\nUse the password you just entered.");}
                    catch(Exception error) {SwingUtilitiesHelper.showBackendError(LecturerDetailsGUI.this,
                        error.getCause() instanceof Exception cause ? cause : error);}
                }
            }.execute();
        });
        buttonPnl.add(enableLogin);
        buttonPnl.add(backBtn);
        cardPnl.setLayout(new BorderLayout(0, 24));
        cardPnl.add(GUIStyle.plainScrollPane(sectionsPnl), BorderLayout.CENTER);
        cardPnl.add(buttonPnl, BorderLayout.SOUTH);
        contentPnl.add(titlePnl, BorderLayout.NORTH);
        contentPnl.add(cardPnl, BorderLayout.CENTER);
        backBtn.addActionListener(this);
        mainPnl.add(headerPnl, BorderLayout.NORTH);
        mainPnl.add(contentPnl, BorderLayout.CENTER);
        this.add(mainPnl);
        this.setTitle("Lecturer Details");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(960, 720));
        this.setLocationRelativeTo(null);
        GUIStyle.showFrame(this);
    }

    private void addRow(JPanel panel, String labelText, String valueText) {
        JLabel label = GUIStyle.normalLabel(labelText);
        label.setFont(new Font(GUIStyle.FONT_FAMILY, Font.BOLD, 13));
        panel.add(label);
        panel.add(GUIStyle.normalLabel(valueText));
    }

    private void addSectionTitle(JPanel panel, String titleText) {
        JLabel title = GUIStyle.heading(titleText, 17);
        panel.add(title);
        panel.add(new JLabel());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.dispose();
        new AllLecturersGUI().setGUI();
    }
}
