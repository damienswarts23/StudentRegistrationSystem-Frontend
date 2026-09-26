package za.ac.mycput.studentregistrationsystem.frontend.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import za.ac.mycput.studentregistrationsystem.frontend.service.BackendApiClient.ClassOption;

public class EditLecturerDetailsGUI extends JFrame implements ActionListener {
    private final int lecturerIndex;
    private JTextField surnameTxtFld;
    private JTextField personalEmailTxtFld;
    private JTextField phoneTxtFld;
    private JTextField streetTxtFld;
    private JTextField suburbTxtFld;
    private JTextField cityTxtFld;
    private JTextField postalCodeTxtFld;
    private JTextField provinceTxtFld;
    private final List<ClassOption> classOptions = new ArrayList<>();
    private final List<Integer> originalClassIds = new ArrayList<>();
    private JCheckBox[] classCheckBoxes = new JCheckBox[0];
    private JButton cancelBtn;
    private JButton doneBtn;

    public EditLecturerDetailsGUI(int lecturerIndex) {
        this.lecturerIndex = lecturerIndex;
    }

    public void setGUI() {
        JPanel mainPnl = new JPanel(new BorderLayout());
        JPanel headerPnl = new JPanel(new BorderLayout());
        JPanel contentPnl = new JPanel(new BorderLayout(0, 22));
        JPanel cardPnl = GUIStyle.whiteCard();
        JPanel bodyPnl = new JPanel(new BorderLayout(0, 24));
        JPanel formPnl = new JPanel(new GridLayout(8, 2, 18, 15));
        JPanel classesSection = new JPanel(new BorderLayout(0, 12));
        JPanel classPnl = new JPanel(new GridLayout(0, 3, 18, 12));
        JPanel buttonPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        String[] lecturer = LecturerData.lecturerAt(lecturerIndex);

        headerPnl.setBackground(GUIStyle.NAVY);
        headerPnl.setBorder(BorderFactory.createEmptyBorder(22, 34, 22, 34));
        JLabel headerLbl = new JLabel("STUDENT REGISTRATION SYSTEM");
        headerLbl.setFont(new Font("Arial", Font.BOLD, 21));
        headerLbl.setForeground(Color.WHITE);
        headerPnl.add(headerLbl, BorderLayout.WEST);

        contentPnl.setBackground(GUIStyle.BACKGROUND);
        contentPnl.setBorder(BorderFactory.createEmptyBorder(28, 110, 35, 110));
        JPanel titlePnl = new JPanel(new GridLayout(2, 1, 5, 5));
        titlePnl.setBackground(GUIStyle.BACKGROUND);
        titlePnl.add(GUIStyle.heading("EDIT LECTURER DETAILS", 25));
        JLabel subtitle = GUIStyle.normalLabel(
                "Update personal details and change the classes assigned to this lecturer.");
        subtitle.setForeground(GUIStyle.MUTED);
        titlePnl.add(subtitle);

        surnameTxtFld = field(lecturer[LecturerData.LAST_NAME]);
        personalEmailTxtFld = field(lecturer[LecturerData.PERSONAL_EMAIL]);
        phoneTxtFld = field(lecturer[LecturerData.PHONE]);
        streetTxtFld = field(lecturer[LecturerData.STREET]);
        suburbTxtFld = field(lecturer[LecturerData.SUBURB]);
        cityTxtFld = field(lecturer[LecturerData.CITY]);
        postalCodeTxtFld = field(lecturer[LecturerData.POSTAL_CODE]);
        provinceTxtFld = field(lecturer[LecturerData.PROVINCE]);

        formPnl.setBackground(GUIStyle.WHITE);
        addField(formPnl, "Surname", surnameTxtFld);
        addField(formPnl, "Personal Email", personalEmailTxtFld);
        addField(formPnl, "Phone Number", phoneTxtFld);
        addField(formPnl, "Street", streetTxtFld);
        addField(formPnl, "Suburb", suburbTxtFld);
        addField(formPnl, "City", cityTxtFld);
        addField(formPnl, "Postal Code", postalCodeTxtFld);
        addField(formPnl, "Province", provinceTxtFld);

        classesSection.setBackground(GUIStyle.WHITE);
        classPnl.setBackground(GUIStyle.WHITE);
        JLabel classesLbl = GUIStyle.heading("ASSIGNED CLASSES", 17);
        JLabel classesHint = GUIStyle.normalLabel(
                "Checked classes are taught by this lecturer. A class may also be taught by other lecturers. Tick or untick classes to change this lecturer's assignments.");
        classesHint.setForeground(GUIStyle.MUTED);
        JPanel classesTitlePnl = new JPanel(new GridLayout(2, 1, 3, 3));
        classesTitlePnl.setBackground(GUIStyle.WHITE);
        classesTitlePnl.add(classesLbl);
        classesTitlePnl.add(classesHint);
        classesSection.add(classesTitlePnl, BorderLayout.NORTH);

        classPnl.add(boldLabel("Class Code"));
        classPnl.add(boldLabel("Class Name"));
        classPnl.add(boldLabel("Assigned"));
        loadClassOptions(classPnl, lecturer);
        JScrollPane classScrollPane = GUIStyle.plainScrollPane(classPnl);
        classScrollPane.setPreferredSize(new Dimension(760, 220));
        classScrollPane.getVerticalScrollBar().setUnitIncrement(14);
        classesSection.add(classScrollPane, BorderLayout.CENTER);

        cancelBtn = GUIStyle.secondaryButton("Cancel");
        doneBtn = GUIStyle.primaryButton("Save Changes");
        buttonPnl.setBackground(GUIStyle.WHITE);
        buttonPnl.add(cancelBtn);
        buttonPnl.add(doneBtn);

        bodyPnl.setBackground(GUIStyle.WHITE);
        bodyPnl.add(formPnl, BorderLayout.NORTH);
        bodyPnl.add(classesSection, BorderLayout.CENTER);
        cardPnl.setLayout(new BorderLayout(0, 24));
        cardPnl.add(bodyPnl, BorderLayout.CENTER);
        cardPnl.add(buttonPnl, BorderLayout.SOUTH);
        contentPnl.add(titlePnl, BorderLayout.NORTH);
        contentPnl.add(cardPnl, BorderLayout.CENTER);

        cancelBtn.addActionListener(this);
        doneBtn.addActionListener(this);
        mainPnl.add(headerPnl, BorderLayout.NORTH);
        JScrollPane pageScrollPane = GUIStyle.plainScrollPane(contentPnl);
        pageScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPnl.add(pageScrollPane, BorderLayout.CENTER);
        this.add(mainPnl);
        this.setTitle("Edit Lecturer Details");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(1100, 820));
        this.setLocationRelativeTo(null);
        GUIStyle.showFrame(this);
    }

    private void loadClassOptions(JPanel classPnl, String[] lecturer) {
        try {
            List<ClassOption> assigned = LecturerData.assignedClasses(lecturerIndex);
            List<ClassOption> available = LecturerData.availableClasses(
                    Integer.parseInt(lecturer[LecturerData.DEPARTMENT_ID]));

            Map<Integer, ClassOption> combined = new LinkedHashMap<>();
            for (ClassOption courseClass : assigned) {
                combined.put(courseClass.getId(), courseClass);
                originalClassIds.add(courseClass.getId());
            }
            for (ClassOption courseClass : available) {
                combined.putIfAbsent(courseClass.getId(), courseClass);
            }
            classOptions.addAll(combined.values());
            classCheckBoxes = new JCheckBox[classOptions.size()];

            for (int i = 0; i < classOptions.size(); i++) {
                ClassOption courseClass = classOptions.get(i);
                JCheckBox checkBox = new JCheckBox();
                checkBox.setBackground(GUIStyle.WHITE);
                checkBox.setSelected(originalClassIds.contains(courseClass.getId()));
                classCheckBoxes[i] = checkBox;
                classPnl.add(GUIStyle.normalLabel(courseClass.getCode()));
                classPnl.add(GUIStyle.normalLabel(courseClass.getName()));
                classPnl.add(checkBox);
            }

            if (classOptions.isEmpty()) {
                classPnl.add(GUIStyle.normalLabel("No classes are currently available in this department."));
                classPnl.add(new JLabel());
                classPnl.add(new JLabel());
            }
        } catch (Exception exception) {
            classOptions.clear();
            originalClassIds.clear();
            classCheckBoxes = new JCheckBox[0];
            classPnl.add(GUIStyle.normalLabel("Classes could not be loaded from the backend."));
            classPnl.add(new JLabel());
            classPnl.add(new JLabel());
            SwingUtilitiesHelper.showBackendError(this, exception);
        }
    }

    private JTextField field(String value) {
        JTextField textField = new JTextField(value == null ? "" : value);
        GUIStyle.styleField(textField);
        return textField;
    }

    private void addField(JPanel panel, String label, JTextField field) {
        panel.add(GUIStyle.normalLabel(label + ":"));
        panel.add(field);
    }

    private JLabel boldLabel(String text) {
        JLabel label = GUIStyle.normalLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        return label;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelBtn) {
            this.dispose();
            new EditLecturerGUI().setGUI();
            return;
        }

        try {
            validateForm();
            LecturerData.updateLecturer(lecturerIndex,
                    surnameTxtFld.getText().trim(),
                    personalEmailTxtFld.getText().trim(),
                    phoneTxtFld.getText().trim(),
                    streetTxtFld.getText().trim(),
                    suburbTxtFld.getText().trim(),
                    cityTxtFld.getText().trim(),
                    postalCodeTxtFld.getText().trim(),
                    provinceTxtFld.getText().trim());

            List<Integer> selectedClassIds = new ArrayList<>();
            for (int i = 0; i < classCheckBoxes.length; i++) {
                if (classCheckBoxes[i].isSelected()) {
                    selectedClassIds.add(classOptions.get(i).getId());
                }
            }
            LecturerData.updateLecturerClasses(
                    lecturerIndex, new ArrayList<>(originalClassIds), selectedClassIds);

            JOptionPane.showMessageDialog(this,
                    "Lecturer details and class assignments updated successfully.");
            this.dispose();
            new EditLecturerGUI().setGUI();
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage());
        } catch (Exception exception) {
            SwingUtilitiesHelper.showBackendError(this, exception);
        }
    }

    private void validateForm() {
        JTextField[] required = {surnameTxtFld, personalEmailTxtFld, phoneTxtFld,
                streetTxtFld, suburbTxtFld, cityTxtFld, postalCodeTxtFld, provinceTxtFld};
        for (JTextField field : required) {
            if (field.getText().trim().isBlank()) {
                throw new IllegalArgumentException(
                        "All lecturer personal detail fields are required.");
            }
        }
        if (!personalEmailTxtFld.getText().contains("@")) {
            throw new IllegalArgumentException("Enter a valid personal email address.");
        }
    }
}
