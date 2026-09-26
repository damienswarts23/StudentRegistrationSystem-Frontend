package za.ac.mycput.studentregistrationsystem.frontend.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import za.ac.mycput.studentregistrationsystem.frontend.service.BackendApiClient.StudentAdminOption;

public class EditStudentAdminDetailsGUI extends JFrame implements ActionListener {
    private final int studentIndex;
    private JTextField firstNameTxtFld;
    private JTextField lastNameTxtFld;
    private DatePickerField dobField;
    private JComboBox<String> genderCmb;
    private JComboBox<String> raceCmb;
    private JTextField personalEmailTxtFld;
    private JTextField phoneTxtFld;
    private JTextField streetTxtFld;
    private JTextField suburbTxtFld;
    private JTextField cityTxtFld;
    private JTextField postalTxtFld;
    private JTextField provinceTxtFld;
    private JButton cancelBtn;
    private JButton saveBtn;

    public EditStudentAdminDetailsGUI(int studentIndex) {
        this.studentIndex = studentIndex;
    }

    public void setGUI() {
        StudentAdminOption student = StudentAdminData.studentAt(studentIndex);
        JPanel mainPnl = new JPanel(new BorderLayout());
        JPanel headerPnl = new JPanel(new BorderLayout());
        JPanel contentPnl = new JPanel(new BorderLayout(0, 20));
        JPanel cardPnl = GUIStyle.whiteCard();
        JPanel formPnl = new JPanel(new GridLayout(14, 2, 18, 14));
        JPanel buttonPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        headerPnl.setBackground(GUIStyle.NAVY);
        headerPnl.setBorder(BorderFactory.createEmptyBorder(22, 34, 22, 34));
        JLabel headerLbl = new JLabel("STUDENT REGISTRATION SYSTEM");
        headerLbl.setFont(new Font("Arial", Font.BOLD, 21));
        headerLbl.setForeground(Color.WHITE);
        headerPnl.add(headerLbl, BorderLayout.WEST);

        contentPnl.setBackground(GUIStyle.BACKGROUND);
        contentPnl.setBorder(BorderFactory.createEmptyBorder(28, 120, 35, 120));
        JPanel titlePnl = new JPanel(new GridLayout(2, 1, 5, 5));
        titlePnl.setBackground(GUIStyle.BACKGROUND);
        titlePnl.add(GUIStyle.heading("EDIT STUDENT DETAILS", 25));
        JLabel subtitle = GUIStyle.normalLabel(
                "The student number and institutional email are generated and cannot be changed.");
        subtitle.setForeground(GUIStyle.MUTED);
        titlePnl.add(subtitle);

        formPnl.setBackground(GUIStyle.WHITE);
        addValue(formPnl, "Student Number", student.getStudentNumber());
        addValue(formPnl, "Student Email", student.getStudentEmail());
        firstNameTxtFld = field(student.getFirstName());
        lastNameTxtFld = field(student.getLastName());
        dobField = new DatePickerField();
        dobField.setDate(LocalDate.parse(student.getDateOfBirth()));
        genderCmb = new JComboBox<>(new String[]{"MALE", "FEMALE", "OTHER"});
        genderCmb.setSelectedItem(student.getGender());
        raceCmb = new JComboBox<>(new String[]{"AFRICAN", "COLOURED", "INDIAN", "WHITE", "OTHER"});
        raceCmb.setSelectedItem(student.getRace());
        personalEmailTxtFld = field(student.getPersonalEmail());
        phoneTxtFld = field(student.getPhoneNumber());
        streetTxtFld = field(student.getStreet());
        suburbTxtFld = field(student.getSuburb());
        cityTxtFld = field(student.getCity());
        postalTxtFld = field(student.getPostalCode());
        provinceTxtFld = field(student.getProvince());

        addField(formPnl, "First Name", firstNameTxtFld);
        addField(formPnl, "Last Name", lastNameTxtFld);
        formPnl.add(GUIStyle.normalLabel("Date of Birth:"));
        formPnl.add(dobField);
        formPnl.add(GUIStyle.normalLabel("Gender:"));
        formPnl.add(genderCmb);
        formPnl.add(GUIStyle.normalLabel("Race:"));
        formPnl.add(raceCmb);
        addField(formPnl, "Personal Email", personalEmailTxtFld);
        addField(formPnl, "Phone Number", phoneTxtFld);
        addField(formPnl, "Street", streetTxtFld);
        addField(formPnl, "Suburb", suburbTxtFld);
        addField(formPnl, "City", cityTxtFld);
        addField(formPnl, "Postal Code", postalTxtFld);
        addField(formPnl, "Province", provinceTxtFld);

        cancelBtn = GUIStyle.secondaryButton("Cancel");
        saveBtn = GUIStyle.primaryButton("Save Changes");
        buttonPnl.setBackground(GUIStyle.WHITE);
        buttonPnl.add(cancelBtn);
        buttonPnl.add(saveBtn);
        cancelBtn.addActionListener(this);
        saveBtn.addActionListener(this);

        cardPnl.setLayout(new BorderLayout(0, 18));
        cardPnl.add(formPnl, BorderLayout.CENTER);
        cardPnl.add(buttonPnl, BorderLayout.SOUTH);
        contentPnl.add(titlePnl, BorderLayout.NORTH);
        contentPnl.add(GUIStyle.plainScrollPane(cardPnl), BorderLayout.CENTER);
        mainPnl.add(headerPnl, BorderLayout.NORTH);
        mainPnl.add(contentPnl, BorderLayout.CENTER);
        add(mainPnl);
        setTitle("Edit Student Details");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1050, 800));
        setLocationRelativeTo(null);
        GUIStyle.showFrame(this);
    }

    private JTextField field(String value) {
        JTextField field = new JTextField(value == null ? "" : value);
        GUIStyle.styleField(field);
        return field;
    }

    private void addField(JPanel panel, String label, JTextField field) {
        panel.add(GUIStyle.normalLabel(label + ":"));
        panel.add(field);
    }

    private void addValue(JPanel panel, String label, String value) {
        JLabel name = GUIStyle.normalLabel(label + ":");
        name.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(name);
        JLabel data = GUIStyle.normalLabel(value == null ? "" : value);
        data.setForeground(GUIStyle.MUTED);
        panel.add(data);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelBtn) {
            dispose();
            new EditStudentsGUI().setGUI();
            return;
        }
        try {
            validateForm();
            StudentAdminData.updateStudent(studentIndex,
                    firstNameTxtFld.getText().trim(),
                    lastNameTxtFld.getText().trim(),
                    dobField.getDateText(),
                    String.valueOf(genderCmb.getSelectedItem()),
                    String.valueOf(raceCmb.getSelectedItem()),
                    personalEmailTxtFld.getText().trim(),
                    phoneTxtFld.getText().trim(),
                    streetTxtFld.getText().trim(),
                    suburbTxtFld.getText().trim(),
                    cityTxtFld.getText().trim(),
                    postalTxtFld.getText().trim(),
                    provinceTxtFld.getText().trim());
            JOptionPane.showMessageDialog(this, "Student details updated successfully.");
            dispose();
            new EditStudentsGUI().setGUI();
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage());
        } catch (Exception exception) {
            SwingUtilitiesHelper.showBackendError(this, exception);
        }
    }

    private void validateForm() {
        JTextField[] fields = {firstNameTxtFld, lastNameTxtFld, personalEmailTxtFld,
            phoneTxtFld, streetTxtFld, suburbTxtFld, cityTxtFld, postalTxtFld, provinceTxtFld};
        for (JTextField field : fields) {
            if (field.getText().trim().isBlank()) {
                throw new IllegalArgumentException("All student detail fields are required.");
            }
        }
        if (dobField.getDateText().isBlank()) {
            throw new IllegalArgumentException("Choose the student's date of birth.");
        }
        if (!personalEmailTxtFld.getText().contains("@")) {
            throw new IllegalArgumentException("Enter a valid personal email address.");
        }
    }
}
