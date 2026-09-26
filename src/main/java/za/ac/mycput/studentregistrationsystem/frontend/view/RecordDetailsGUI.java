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

public class RecordDetailsGUI extends JFrame implements ActionListener {
    private final String entity;
    private JButton backBtn;

    public RecordDetailsGUI(String entity) {
        this.entity = entity;
    }

    public void setGUI() {
        JPanel mainPnl = new JPanel(new BorderLayout());
        JPanel headerPnl = new JPanel(new BorderLayout());
        JPanel contentPnl = new JPanel(new BorderLayout(0, 22));
        JPanel detailsPnl = GUIStyle.whiteCard();
        JPanel valuesPnl = new JPanel(new GridLayout(labels().length, 2, 18, 15));
        JPanel buttonPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        headerPnl.setBackground(GUIStyle.NAVY);
        headerPnl.setBorder(BorderFactory.createEmptyBorder(22, 34, 22, 34));
        JLabel headerLbl = new JLabel("STUDENT REGISTRATION SYSTEM");
        headerLbl.setFont(new Font("Arial", Font.BOLD, 21));
        headerLbl.setForeground(Color.WHITE);
        headerPnl.add(headerLbl, BorderLayout.WEST);
        contentPnl.setBackground(GUIStyle.BACKGROUND);
        contentPnl.setBorder(BorderFactory.createEmptyBorder(32, 190, 40, 190));
        JPanel titlePnl = new JPanel(new GridLayout(2, 1, 5, 5));
        titlePnl.setBackground(GUIStyle.BACKGROUND);
        titlePnl.add(GUIStyle.heading(singular(entity).toUpperCase() + " DETAILS", 25));
        JLabel sub = GUIStyle.normalLabel("Complete information for the selected record");
        sub.setForeground(GUIStyle.MUTED);
        titlePnl.add(sub);
        valuesPnl.setBackground(GUIStyle.WHITE);
        String[] labels = labels();
        String[] values = values();
        for (int i = 0; i < labels.length; i++) {
            JLabel name = GUIStyle.normalLabel(labels[i]);
            name.setFont(new Font("Arial", Font.BOLD, 13));
            valuesPnl.add(name);
            valuesPnl.add(GUIStyle.normalLabel(values[i]));
        }
        backBtn = GUIStyle.secondaryButton("Back");
        buttonPnl.setBackground(GUIStyle.WHITE);
        buttonPnl.add(backBtn);
        detailsPnl.setLayout(new BorderLayout(0, 20));
        detailsPnl.add(GUIStyle.plainScrollPane(valuesPnl), BorderLayout.CENTER);
        detailsPnl.add(buttonPnl, BorderLayout.SOUTH);
        contentPnl.add(titlePnl, BorderLayout.NORTH);
        contentPnl.add(detailsPnl, BorderLayout.CENTER);
        backBtn.addActionListener(this);
        mainPnl.add(headerPnl, BorderLayout.NORTH);
        mainPnl.add(contentPnl, BorderLayout.CENTER);
        this.add(mainPnl);
        this.setTitle(singular(entity) + " Details");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(920, 650));
        this.setLocationRelativeTo(null);
        GUIStyle.showFrame(this);
    }

    private String singular(String value) {
        if (value.equals("Classes")) return "Class";
        return value.endsWith("s") ? value.substring(0, value.length() - 1) : value;
    }

    private String[] labels() {
        if (entity.equals("Applicants")) return new String[]{"Person ID", "Applicant ID", "First Name", "Last Name", "Date of Birth", "Gender", "Race", "Email", "Phone Number", "Address"};
        if (entity.equals("Students")) return new String[]{"Student ID", "Student Number", "Student Email", "Applicant"};
        if (entity.equals("Lecturers")) return new String[]{"Lecturer ID", "Lecturer Number", "Name", "Lecturer Email", "Department"};
        return new String[]{"Record Type", "Record ID", "Primary Information", "Related Information"};
    }

    private String[] values() {
        if (entity.equals("Applicants")) return new String[]{"1001", "1", "Damien", "Swarts", "2002-11-18", "MALE", "COLOURED", "damien@example.com", "081 234 5678", "18 Ottawa Lane, Cape Town"};
        if (entity.equals("Students")) return new String[]{"1", "222868791", "222868791@mycput.ac.za", "Damien Swarts"};
        if (entity.equals("Lecturers")) return new String[]{"1", "EMP001", "A. Jacobs", "jacobsa@cput.ac.za", "Information Technology"};
        return new String[]{singular(entity), "1", "Sample preview information", "Sample related record"};
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.dispose();
        new ManagementGUI(entity).setGUI();
    }
}
