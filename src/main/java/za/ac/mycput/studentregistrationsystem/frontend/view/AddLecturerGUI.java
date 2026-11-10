package za.ac.mycput.studentregistrationsystem.frontend.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import za.ac.mycput.studentregistrationsystem.frontend.service.BackendApiClient.DepartmentOption;

public class AddLecturerGUI extends AppFrame implements ActionListener {
    private final String[] fields = {"First Name", "Last Name", "Date of Birth", "Gender", "Race",
            "Personal Email", "Phone Number", "Department", "Street", "Suburb", "City", "Postal Code", "Province"};
    private final JTextField[] fieldTxtFlds = new JTextField[fields.length];
    private DatePickerField dateOfBirthPicker;
    private JComboBox<String> genderCmb;
    private JComboBox<String> raceCmb;
    private JComboBox<DepartmentOption> departmentCmb;
    private JButton cancelBtn;
    private JButton assignClassesBtn;

    public void setGUI() {
        JPanel mainPnl = new JPanel(new BorderLayout());
        JPanel headerPnl = new JPanel(new BorderLayout());
        JPanel contentPnl = new JPanel(new BorderLayout(0, 20));
        JPanel cardPnl = GUIStyle.whiteCard();
        JPanel formPnl = new JPanel(new GridLayout(fields.length, 2, 20, 14));
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
        titlePnl.add(GUIStyle.heading("ADD LECTURER", 25));
        JLabel subtitle = GUIStyle.normalLabel("Enter the lecturer's personal details. Lecturer ID, number and CPUT email are generated automatically.");
        subtitle.setForeground(GUIStyle.MUTED);
        titlePnl.add(subtitle);

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
            } else if (fields[i].equals("Department")) {
                departmentCmb = new JComboBox<>();
                loadDepartments();
                formPnl.add(departmentCmb);
            } else {
                fieldTxtFlds[i] = new JTextField();
                GUIStyle.styleField(fieldTxtFlds[i]);
                formPnl.add(fieldTxtFlds[i]);
            }
        }

        cancelBtn = GUIStyle.secondaryButton("Cancel");
        assignClassesBtn = GUIStyle.primaryButton("Assign Classes");
        buttonPnl.setBackground(GUIStyle.WHITE);
        buttonPnl.add(cancelBtn);
        buttonPnl.add(assignClassesBtn);
        cardPnl.setLayout(new BorderLayout(0, 20));
        cardPnl.add(formPnl, BorderLayout.CENTER);
        cardPnl.add(buttonPnl, BorderLayout.SOUTH);
        contentPnl.add(titlePnl, BorderLayout.NORTH);
        contentPnl.add(cardPnl, BorderLayout.CENTER);
        cancelBtn.addActionListener(this);
        assignClassesBtn.addActionListener(this);
        mainPnl.add(headerPnl, BorderLayout.NORTH);
        JScrollPane pageScrollPane = GUIStyle.plainScrollPane(contentPnl);
        pageScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPnl.add(pageScrollPane, BorderLayout.CENTER);
        this.add(mainPnl);
        this.setTitle("Add Lecturer");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(980, 760));
        this.setLocationRelativeTo(null);
        GUIStyle.showFrame(this);
    }

    private void loadDepartments() {
        try {
            List<DepartmentOption> departments = LecturerData.departments();
            departmentCmb.addItem(new DepartmentOption(0, "", "Select Department"));
            for (DepartmentOption department : departments) {
                departmentCmb.addItem(department);
            }
        } catch (Exception exception) {
            departmentCmb.addItem(new DepartmentOption(0, "", "Backend unavailable"));
            SwingUtilitiesHelper.showBackendError(this, exception);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelBtn) {
            this.dispose();
            new AdminDashboardGUI().setGUI();
            return;
        }

        try {
            validateForm();
            DepartmentOption department = (DepartmentOption) departmentCmb.getSelectedItem();
            String address = textValue("Street") + ", " + textValue("Suburb") + ", "
                    + textValue("City") + ", " + textValue("Postal Code") + ", " + textValue("Province");
            LecturerData.setPendingLecturer(new String[]{
                    "", "", "", textValue("First Name"), textValue("Last Name"),
                    dateOfBirthPicker.getDateText(),
                    genderCmb.getSelectedItem().toString(),
                    raceCmb.getSelectedItem().toString(),
                    "", textValue("Phone Number"), department.getName(), address,
                    textValue("Personal Email"), String.valueOf(department.getId()),
                    textValue("Street"), textValue("Suburb"), textValue("City"),
                    textValue("Postal Code"), textValue("Province")
            });
            this.dispose();
            new AssignLecturerClassesGUI().setGUI();
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage());
        }
    }

    private void validateForm() {
        if (textValue("First Name").isBlank() || textValue("Last Name").isBlank()) {
            throw new IllegalArgumentException("Enter the lecturer's first name and last name.");
        }
        if (dateOfBirthPicker.getDate() == null) {
            throw new IllegalArgumentException("Choose the lecturer's date of birth from the calendar.");
        }
        if (!dateOfBirthPicker.getDate().isBefore(java.time.LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth must be before today.");
        }
        if (genderCmb.getSelectedIndex() == 0 || raceCmb.getSelectedIndex() == 0) {
            throw new IllegalArgumentException("Select a gender and race.");
        }
        DepartmentOption department = (DepartmentOption) departmentCmb.getSelectedItem();
        if (department == null || department.getId() == 0) {
            throw new IllegalArgumentException("Select a department from the backend.");
        }
        String[] required = {"Personal Email", "Phone Number", "Street", "Suburb", "City", "Postal Code", "Province"};
        for (String field : required) {
            if (textValue(field).isBlank()) {
                throw new IllegalArgumentException("Enter " + field.toLowerCase() + ".");
            }
        }
        if (!textValue("Personal Email").contains("@")) {
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
