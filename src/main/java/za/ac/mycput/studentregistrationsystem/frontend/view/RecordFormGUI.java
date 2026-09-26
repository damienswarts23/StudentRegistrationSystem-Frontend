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

public class RecordFormGUI extends JFrame implements ActionListener {
    private final String entity;
    private JButton saveBtn;
    private JButton cancelBtn;

    public RecordFormGUI(String entity) {
        this.entity = entity;
    }

    public void setGUI() {
        JPanel mainPnl = new JPanel(new BorderLayout());
        JPanel headerPnl = new JPanel(new BorderLayout());
        JPanel contentPnl = new JPanel(new BorderLayout(0, 20));
        JPanel cardPnl = GUIStyle.whiteCard();
        JPanel formPnl = new JPanel(new GridLayout(fields().length, 2, 20, 14));
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
        titlePnl.add(GUIStyle.heading("ADD " + singular(entity).toUpperCase(), 25));
        JLabel subtitle = GUIStyle.normalLabel("Enter the information below. This preview does not save records.");
        subtitle.setForeground(GUIStyle.MUTED);
        titlePnl.add(subtitle);

        cardPnl.setLayout(new BorderLayout(0, 20));
        formPnl.setBackground(GUIStyle.WHITE);
        for (String field : fields()) {
            formPnl.add(GUIStyle.normalLabel(field + ":"));
            if (field.equals("Gender")) {
                formPnl.add(new JComboBox<>(new String[]{"Select Gender", "MALE", "FEMALE", "OTHER"}));
            } else if (field.equals("Race")) {
                formPnl.add(new JComboBox<>(new String[]{"Select Race", "AFRICAN", "COLOURED", "INDIAN", "WHITE", "OTHER"}));
            } else if (field.equals("Department") || field.equals("Course") || field.equals("Lecturer") || field.equals("Student") || field.equals("Class") || field.equals("Applicant")) {
                formPnl.add(new JComboBox<>(new String[]{"Select " + field, "Sample option one", "Sample option two"}));
            } else {
                JTextField fieldTxt = new JTextField();
                GUIStyle.styleField(fieldTxt);
                formPnl.add(fieldTxt);
            }
        }
        saveBtn = GUIStyle.primaryButton("Save " + singular(entity));
        cancelBtn = GUIStyle.secondaryButton("Cancel");
        buttonPnl.setBackground(GUIStyle.WHITE);
        buttonPnl.add(cancelBtn);
        buttonPnl.add(saveBtn);
        cardPnl.add(formPnl, BorderLayout.CENTER);
        cardPnl.add(buttonPnl, BorderLayout.SOUTH);
        contentPnl.add(titlePnl, BorderLayout.NORTH);
        contentPnl.add(cardPnl, BorderLayout.CENTER);
        saveBtn.addActionListener(this);
        cancelBtn.addActionListener(this);
        mainPnl.add(headerPnl, BorderLayout.NORTH);
        mainPnl.add(contentPnl, BorderLayout.CENTER);
        this.add(mainPnl);
        this.setTitle("Add " + singular(entity));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(980, 720));
        this.setLocationRelativeTo(null);
        GUIStyle.showFrame(this);
    }

    private String singular(String value) {
        if (value.equals("Classes")) return "Class";
        return value.endsWith("s") ? value.substring(0, value.length() - 1) : value;
    }

    private String[] fields() {
        switch (entity) {
            case "Applicants": return new String[]{"First Name", "Last Name", "Date of Birth", "Gender", "Race", "Email", "Phone Number", "Street", "Suburb", "City", "Postal Code", "Province"};
            case "Applications": return new String[]{"Applicant", "Course"};
            case "Students": return new String[]{"Applicant"};
            case "Lecturers": return new String[]{"First Name", "Last Name", "Date of Birth", "Gender", "Race", "Lecturer Email", "Phone Number", "Department"};
            case "Departments": return new String[]{"Department Code", "Department Name"};
            case "Courses": return new String[]{"Course Code", "Course Name", "Department"};
            case "Classes": return new String[]{"Class Code", "Class Name", "Course", "Lecturer"};
            default: return new String[]{"Student", "Class"};
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveBtn) {
            JOptionPane.showMessageDialog(this, "GUI preview only: no record was saved.");
        }
        this.dispose();
        new ManagementGUI(entity).setGUI();
    }
}
