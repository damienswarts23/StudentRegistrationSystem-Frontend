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
import za.ac.mycput.studentregistrationsystem.frontend.service.BackendApiClient.StudentAdminOption;

public class StudentAdminDetailsGUI extends JFrame implements ActionListener {
    private final int studentIndex;
    private JButton backBtn;

    public StudentAdminDetailsGUI(int studentIndex) {
        this.studentIndex = studentIndex;
    }

    public void setGUI() {
        StudentAdminOption student = StudentAdminData.studentAt(studentIndex);
        JPanel mainPnl = new JPanel(new BorderLayout());
        JPanel headerPnl = new JPanel(new BorderLayout());
        JPanel contentPnl = new JPanel(new BorderLayout(0, 22));
        JPanel cardPnl = GUIStyle.whiteCard();
        JPanel valuesPnl = new JPanel(new GridLayout(18, 2, 18, 14));
        JPanel buttonPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        headerPnl.setBackground(GUIStyle.NAVY);
        headerPnl.setBorder(BorderFactory.createEmptyBorder(22, 34, 22, 34));
        JLabel headerLbl = new JLabel("STUDENT REGISTRATION SYSTEM");
        headerLbl.setFont(new Font("Arial", Font.BOLD, 21));
        headerLbl.setForeground(Color.WHITE);
        headerPnl.add(headerLbl, BorderLayout.WEST);

        contentPnl.setBackground(GUIStyle.BACKGROUND);
        contentPnl.setBorder(BorderFactory.createEmptyBorder(28, 150, 35, 150));
        JPanel titlePnl = new JPanel(new GridLayout(2, 1, 5, 5));
        titlePnl.setBackground(GUIStyle.BACKGROUND);
        titlePnl.add(GUIStyle.heading("STUDENT DETAILS", 25));
        JLabel subtitle = GUIStyle.normalLabel(student.getFullName());
        subtitle.setForeground(GUIStyle.MUTED);
        titlePnl.add(subtitle);

        valuesPnl.setBackground(GUIStyle.WHITE);
        addRow(valuesPnl, "Student ID", String.valueOf(student.getStudentId()));
        addRow(valuesPnl, "Student Number", student.getStudentNumber());
        addRow(valuesPnl, "Student Email", student.getStudentEmail());
        addRow(valuesPnl, "Person ID", String.valueOf(student.getPersonId()));
        addRow(valuesPnl, "Applicant ID", String.valueOf(student.getApplicantId()));
        addRow(valuesPnl, "First Name", student.getFirstName());
        addRow(valuesPnl, "Last Name", student.getLastName());
        addRow(valuesPnl, "Date of Birth", student.getDateOfBirth());
        addRow(valuesPnl, "Gender", student.getGender());
        addRow(valuesPnl, "Race", student.getRace());
        addRow(valuesPnl, "Personal Email", student.getPersonalEmail());
        addRow(valuesPnl, "Phone Number", student.getPhoneNumber());
        addRow(valuesPnl, "Street", student.getStreet());
        addRow(valuesPnl, "Suburb", student.getSuburb());
        addRow(valuesPnl, "City", student.getCity());
        addRow(valuesPnl, "Postal Code", student.getPostalCode());
        addRow(valuesPnl, "Province", student.getProvince());
        addRow(valuesPnl, "Accepted Course", student.getCourseName());

        backBtn = GUIStyle.secondaryButton("Back");
        buttonPnl.setBackground(GUIStyle.WHITE);
        buttonPnl.add(backBtn);
        backBtn.addActionListener(this);
        cardPnl.setLayout(new BorderLayout(0, 20));
        cardPnl.add(GUIStyle.plainScrollPane(valuesPnl), BorderLayout.CENTER);
        cardPnl.add(buttonPnl, BorderLayout.SOUTH);
        contentPnl.add(titlePnl, BorderLayout.NORTH);
        contentPnl.add(cardPnl, BorderLayout.CENTER);
        mainPnl.add(headerPnl, BorderLayout.NORTH);
        mainPnl.add(contentPnl, BorderLayout.CENTER);
        add(mainPnl);
        setTitle("Student Details");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(980, 760));
        setLocationRelativeTo(null);
        GUIStyle.showFrame(this);
    }

    private void addRow(JPanel panel, String labelText, String valueText) {
        JLabel label = GUIStyle.normalLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(label);
        panel.add(GUIStyle.normalLabel(valueText == null ? "" : valueText));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dispose();
        new AllStudentsGUI().setGUI();
    }
}
