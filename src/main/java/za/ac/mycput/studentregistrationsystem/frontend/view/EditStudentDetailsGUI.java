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
import javax.swing.JTextField;
import za.ac.mycput.studentregistrationsystem.frontend.service.BackendApiClient;

public class EditStudentDetailsGUI extends AppFrame implements ActionListener {
    private JLabel nameValueLbl;
    private JLabel dateOfBirthValueLbl;
    private JLabel genderValueLbl;
    private JLabel raceValueLbl;
    private JLabel studentNumberValueLbl;
    private JLabel studentEmailValueLbl;

    private JTextField personalEmailTxtFld;
    private JTextField phoneTxtFld;
    private JTextField streetTxtFld;
    private JTextField suburbTxtFld;
    private JTextField cityTxtFld;
    private JTextField postalCodeTxtFld;
    private JTextField provinceTxtFld;
    private JButton cancelBtn;
    private JButton saveBtn;

    public void setGUI() {
        JPanel mainPnl = new JPanel(new BorderLayout());
        JPanel headerPnl = new JPanel(new BorderLayout());
        JPanel contentPnl = new JPanel(new BorderLayout(0, 22));
        JPanel sectionsPnl = new JPanel(new GridLayout(2, 1, 0, 20));
        JPanel identityCard = GUIStyle.whiteCard();
        JPanel contactCard = GUIStyle.whiteCard();
        JPanel identityPnl = new JPanel(new GridLayout(6, 2, 18, 13));
        JPanel formPnl = new JPanel(new GridLayout(7, 2, 18, 15));
        JPanel buttonPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));

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
        titlePnl.add(GUIStyle.heading("EDIT MY DETAILS", 25));
        JLabel subtitle = GUIStyle.normalLabel(
                "You can update your contact information and home address. Identity and student details are read-only.");
        subtitle.setForeground(GUIStyle.MUTED);
        titlePnl.add(subtitle);

        nameValueLbl = valueLabel("");
        dateOfBirthValueLbl = valueLabel("");
        genderValueLbl = valueLabel("");
        raceValueLbl = valueLabel("");
        studentNumberValueLbl = valueLabel(ApplicationPreviewData.studentNumber());
        studentEmailValueLbl = valueLabel(ApplicationPreviewData.studentEmail());

        identityPnl.setBackground(GUIStyle.WHITE);
        addReadOnlyRow(identityPnl, "Full Name", nameValueLbl);
        addReadOnlyRow(identityPnl, "Date of Birth", dateOfBirthValueLbl);
        addReadOnlyRow(identityPnl, "Gender", genderValueLbl);
        addReadOnlyRow(identityPnl, "Race", raceValueLbl);
        addReadOnlyRow(identityPnl, "Student Number", studentNumberValueLbl);
        addReadOnlyRow(identityPnl, "Student Email", studentEmailValueLbl);

        JLabel identityTitle = GUIStyle.heading("Student information", 17);
        JLabel identityNote = GUIStyle.normalLabel("These details cannot be changed from the student dashboard.");
        identityNote.setForeground(GUIStyle.MUTED);
        JPanel identityHeader = new JPanel(new GridLayout(2, 1, 4, 4));
        identityHeader.setBackground(GUIStyle.WHITE);
        identityHeader.add(identityTitle);
        identityHeader.add(identityNote);
        identityCard.setLayout(new BorderLayout(0, 16));
        identityCard.add(identityHeader, BorderLayout.NORTH);
        identityCard.add(identityPnl, BorderLayout.CENTER);

        personalEmailTxtFld = field("");
        phoneTxtFld = field("");
        streetTxtFld = field("");
        suburbTxtFld = field("");
        cityTxtFld = field("");
        postalCodeTxtFld = field("");
        provinceTxtFld = field("");

        formPnl.setBackground(GUIStyle.WHITE);
        addField(formPnl, "Personal Email", personalEmailTxtFld);
        addField(formPnl, "Phone Number", phoneTxtFld);
        addField(formPnl, "Street", streetTxtFld);
        addField(formPnl, "Suburb", suburbTxtFld);
        addField(formPnl, "City", cityTxtFld);
        addField(formPnl, "Postal Code", postalCodeTxtFld);
        addField(formPnl, "Province", provinceTxtFld);

        cancelBtn = GUIStyle.secondaryButton("Cancel");
        saveBtn = GUIStyle.primaryButton("Save Changes");
        buttonPnl.setBackground(GUIStyle.WHITE);
        buttonPnl.add(cancelBtn);
        buttonPnl.add(saveBtn);

        contactCard.setLayout(new BorderLayout(0, 18));
        contactCard.add(GUIStyle.heading("Contact and address details", 17), BorderLayout.NORTH);
        contactCard.add(formPnl, BorderLayout.CENTER);
        contactCard.add(buttonPnl, BorderLayout.SOUTH);

        sectionsPnl.setBackground(GUIStyle.BACKGROUND);
        sectionsPnl.add(identityCard);
        sectionsPnl.add(contactCard);
        contentPnl.add(titlePnl, BorderLayout.NORTH);
        contentPnl.add(sectionsPnl, BorderLayout.CENTER);

        cancelBtn.addActionListener(this);
        saveBtn.addActionListener(this);
        mainPnl.add(headerPnl, BorderLayout.NORTH);
        JScrollPane pageScrollPane = GUIStyle.plainScrollPane(contentPnl);
        pageScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPnl.add(pageScrollPane, BorderLayout.CENTER);

        this.add(mainPnl);
        this.setTitle("Edit My Details");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(1100, 760));
        this.setLocationRelativeTo(null);
        loadCurrentDetails();
        GUIStyle.showFrame(this);
    }

    private void loadCurrentDetails() {
        try {
            String[] details = BackendApiClient.getStudentPersonalDetails(
                    ApplicationPreviewData.studentId());
            applyDetails(details);
        } catch (Exception exception) {
            SwingUtilitiesHelper.showBackendError(this, exception);
            loadCachedDetails();
        }
    }

    private void applyDetails(String[] details) {
        nameValueLbl.setText(details[0] + " " + details[1]);
        dateOfBirthValueLbl.setText(details[2]);
        genderValueLbl.setText(details[3]);
        raceValueLbl.setText(details[4]);
        personalEmailTxtFld.setText(details[5]);
        phoneTxtFld.setText(details[6]);
        streetTxtFld.setText(details[7]);
        suburbTxtFld.setText(details[8]);
        cityTxtFld.setText(details[9]);
        postalCodeTxtFld.setText(details[10]);
        provinceTxtFld.setText(details[11]);
    }

    private void loadCachedDetails() {
        nameValueLbl.setText(ApplicationPreviewData.fullName());
        dateOfBirthValueLbl.setText(ApplicationPreviewData.dateOfBirth());
        genderValueLbl.setText(ApplicationPreviewData.gender());
        raceValueLbl.setText(ApplicationPreviewData.race());
        personalEmailTxtFld.setText(ApplicationPreviewData.email());
        phoneTxtFld.setText(ApplicationPreviewData.phoneNumber());
        streetTxtFld.setText(ApplicationPreviewData.street());
        suburbTxtFld.setText(ApplicationPreviewData.suburb());
        cityTxtFld.setText(ApplicationPreviewData.city());
        postalCodeTxtFld.setText(ApplicationPreviewData.postalCode());
        provinceTxtFld.setText(ApplicationPreviewData.province());
    }

    private JTextField field(String value) {
        JTextField textField = new JTextField(value);
        GUIStyle.styleField(textField);
        return textField;
    }

    private JLabel valueLabel(String value) {
        JLabel label = GUIStyle.normalLabel(value);
        label.setForeground(GUIStyle.MUTED);
        return label;
    }

    private void addReadOnlyRow(JPanel panel, String label, JLabel value) {
        JLabel name = GUIStyle.normalLabel(label + ":");
        name.setFont(new Font(GUIStyle.FONT_FAMILY, Font.BOLD, 13));
        panel.add(name);
        panel.add(value);
    }

    private void addField(JPanel panel, String label, JTextField field) {
        panel.add(GUIStyle.normalLabel(label + ":"));
        panel.add(field);
    }

    private void validateForm() {
        if (personalEmailTxtFld.getText().trim().isBlank()
                || phoneTxtFld.getText().trim().isBlank()
                || streetTxtFld.getText().trim().isBlank()
                || suburbTxtFld.getText().trim().isBlank()
                || cityTxtFld.getText().trim().isBlank()
                || postalCodeTxtFld.getText().trim().isBlank()
                || provinceTxtFld.getText().trim().isBlank()) {
            throw new IllegalArgumentException("Please complete all contact and address fields.");
        }

        String email = personalEmailTxtFld.getText().trim();
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Enter a valid personal email address.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelBtn) {
            this.dispose();
            new StudentDashboardGUI().setGUI();
            return;
        }

        try {
            validateForm();
            String[] updated = BackendApiClient.updateStudentPersonalDetails(
                    ApplicationPreviewData.studentId(),
                    personalEmailTxtFld.getText().trim(),
                    phoneTxtFld.getText().trim(),
                    streetTxtFld.getText().trim(),
                    suburbTxtFld.getText().trim(),
                    cityTxtFld.getText().trim(),
                    postalCodeTxtFld.getText().trim(),
                    provinceTxtFld.getText().trim());

            ApplicationPreviewData.updatePersonalDetails(
                    updated[0], updated[1], updated[2], updated[3], updated[4], updated[5],
                    updated[6], updated[7], updated[8], updated[9], updated[10], updated[11]);

            JOptionPane.showMessageDialog(this, "Your contact and address details were updated successfully.");
            this.dispose();
            new StudentDashboardGUI().setGUI();
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage());
        } catch (Exception exception) {
            SwingUtilitiesHelper.showBackendError(this, exception);
        }
    }
}
