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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class ApplicantFormGUI extends JFrame implements ActionListener {
    private final String[] fields = {"First Name", "Last Name", "Date of Birth", "Gender", "Race", "Email", "Phone Number", "Street", "Suburb", "City", "Postal Code", "Province"};
    private final JTextField[] fieldTxtFlds = new JTextField[fields.length];
    private DatePickerField dateOfBirthPicker;
    private JComboBox<String> genderCmb;
    private JComboBox<String> raceCmb;
    private JButton chooseCourseBtn;
    private JButton homeBtn;

    public void setGUI() {
        JPanel mainPnl = new JPanel(new BorderLayout());
        JPanel headerPnl = new JPanel(new BorderLayout());
        JPanel contentPnl = new JPanel(new BorderLayout(0, 20));
        JPanel cardPnl = GUIStyle.whiteCard();
        JPanel formPnl = new JPanel(new GridLayout(fields.length, 2, 20, 14));
        JPanel buttonPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        headerPnl.setBackground(GUIStyle.NAVY);
        headerPnl.setBorder(BorderFactory.createEmptyBorder(22, 34, 22, 34));
        JLabel headerLbl = new JLabel("STUDENT REGISTRATION SYSTEM");
        headerLbl.setFont(new Font("Arial", Font.BOLD, 21));
        headerLbl.setForeground(Color.WHITE);
        headerPnl.add(headerLbl, BorderLayout.WEST);

        contentPnl.setBackground(GUIStyle.BACKGROUND);
        contentPnl.setBorder(BorderFactory.createEmptyBorder(28, 170, 35, 170));
        JPanel titlePnl = new JPanel(new GridLayout(2, 1, 5, 5));
        titlePnl.setBackground(GUIStyle.BACKGROUND);
        titlePnl.add(GUIStyle.heading("APPLICANT DETAILS", 25));
        JLabel subtitle = GUIStyle.normalLabel("Enter the applicant information required by the database.");
        subtitle.setForeground(GUIStyle.MUTED);
        titlePnl.add(subtitle);

        cardPnl.setLayout(new BorderLayout(0, 20));
        formPnl.setBackground(GUIStyle.WHITE);
        for (int i = 0; i < fields.length; i++) {
            formPnl.add(GUIStyle.normalLabel(fields[i] + ":"));
            if (fields[i].equals("Date of Birth")) {
                dateOfBirthPicker = new DatePickerField();
                formPnl.add(dateOfBirthPicker);
            } else if (fields[i].equals("Gender")) {
                genderCmb = new JComboBox<>(new String[]{"Select Gender", "MALE", "FEMALE", "OTHER"});
                formPnl.add(genderCmb);
            } else if (fields[i].equals("Race")) {
                raceCmb = new JComboBox<>(new String[]{"Select Race", "AFRICAN", "COLOURED", "INDIAN", "WHITE", "OTHER"});
                formPnl.add(raceCmb);
            } else {
                fieldTxtFlds[i] = new JTextField();
                GUIStyle.styleField(fieldTxtFlds[i]);
                formPnl.add(fieldTxtFlds[i]);
            }
        }

        homeBtn = GUIStyle.secondaryButton("Home");
        chooseCourseBtn = GUIStyle.primaryButton("Choose Course");
        buttonPnl.setBackground(GUIStyle.WHITE);
        buttonPnl.add(homeBtn);
        buttonPnl.add(chooseCourseBtn);
        cardPnl.add(formPnl, BorderLayout.CENTER);
        cardPnl.add(buttonPnl, BorderLayout.SOUTH);
        contentPnl.add(titlePnl, BorderLayout.NORTH);
        contentPnl.add(cardPnl, BorderLayout.CENTER);
        homeBtn.addActionListener(this);
        chooseCourseBtn.addActionListener(this);
        mainPnl.add(headerPnl, BorderLayout.NORTH);
        JScrollPane pageScrollPane = GUIStyle.plainScrollPane(contentPnl);
        pageScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPnl.add(pageScrollPane, BorderLayout.CENTER);
        this.add(mainPnl);
        this.setTitle("Applicant Details");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(980, 720));
        this.setLocationRelativeTo(null);
        GUIStyle.showFrame(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == homeBtn) {
            this.dispose();
            new HomeGUI().setGUI();
        } else {
            try {
                validateForm();
                ApplicationPreviewData.saveApplicant(
                        textValue("First Name"), textValue("Last Name"),
                        dateOfBirthPicker.getDateText(),
                        genderCmb.getSelectedItem().toString(),
                        raceCmb.getSelectedItem().toString(), textValue("Email"),
                        textValue("Phone Number"), textValue("Street"),
                        textValue("Suburb"), textValue("City"),
                        textValue("Postal Code"), textValue("Province"));
                this.dispose();
                new CourseSelectionGUI().setGUI();
            } catch (IllegalArgumentException exception) {
                JOptionPane.showMessageDialog(this, exception.getMessage());
            }
        }
    }

    private void validateForm() {
        if (textValue("First Name").isBlank() || textValue("Last Name").isBlank()) {
            throw new IllegalArgumentException("Enter the applicant's first name and last name.");
        }
        if (dateOfBirthPicker.getDate() == null) {
            throw new IllegalArgumentException("Choose the applicant's date of birth from the calendar.");
        }
        if (!dateOfBirthPicker.getDate().isBefore(java.time.LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth must be before today.");
        }
        if (genderCmb.getSelectedIndex() == 0 || raceCmb.getSelectedIndex() == 0) {
            throw new IllegalArgumentException("Select a gender and race.");
        }
        String[] required = {"Email", "Phone Number", "Street", "Suburb", "City", "Postal Code", "Province"};
        for (String field : required) {
            if (textValue(field).isBlank()) {
                throw new IllegalArgumentException("Enter " + field.toLowerCase() + ".");
            }
        }
        if (!textValue("Email").contains("@")) {
            throw new IllegalArgumentException("Enter a valid personal email address.");
        }
    }

    private String textValue(String fieldName) {
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].equals(fieldName) && fieldTxtFlds[i] != null) {
                return fieldTxtFlds[i].getText().trim();
            }
        }
        return "";
    }
}
