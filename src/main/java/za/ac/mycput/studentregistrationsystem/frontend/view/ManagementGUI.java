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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class ManagementGUI extends JFrame implements ActionListener {
    private final String entity;
    private JButton backBtn;
    private JButton addBtn;
    private JButton viewBtn;

    public ManagementGUI(String entity) {
        this.entity = entity;
    }

    public void setGUI() {
        JPanel mainPnl = new JPanel(new BorderLayout());
        JPanel headerPnl = new JPanel(new BorderLayout());
        JPanel contentPnl = new JPanel(new BorderLayout(0, 18));
        JPanel toolbarPnl = new JPanel(new BorderLayout());
        JPanel searchPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel actionPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        headerPnl.setBackground(GUIStyle.NAVY);
        headerPnl.setBorder(BorderFactory.createEmptyBorder(21, 32, 21, 32));
        JLabel systemLbl = new JLabel("STUDENT REGISTRATION SYSTEM");
        systemLbl.setFont(new Font("Arial", Font.BOLD, 21));
        systemLbl.setForeground(Color.WHITE);
        backBtn = GUIStyle.secondaryButton("Dashboard");
        headerPnl.add(systemLbl, BorderLayout.WEST);
        headerPnl.add(backBtn, BorderLayout.EAST);

        contentPnl.setBackground(GUIStyle.BACKGROUND);
        contentPnl.setBorder(BorderFactory.createEmptyBorder(28, 32, 32, 32));
        JPanel topPnl = new JPanel(new GridLayout(2, 1, 5, 5));
        topPnl.setBackground(GUIStyle.BACKGROUND);
        topPnl.add(GUIStyle.heading(entity.substring(0, entity.length() - (entity.endsWith("s") ? 1 : 0)) + " Management", 26));
        JLabel subtitle = GUIStyle.normalLabel("Preview, search and manage records.");
        subtitle.setForeground(GUIStyle.MUTED);
        topPnl.add(subtitle);

        toolbarPnl.setBackground(GUIStyle.WHITE);
        toolbarPnl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GUIStyle.BORDER),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        searchPnl.setBackground(GUIStyle.WHITE);
        JTextField searchTxtFld = new JTextField(24);
        GUIStyle.styleField(searchTxtFld);
        searchPnl.add(GUIStyle.normalLabel("Search:"));
        searchPnl.add(searchTxtFld);
        searchPnl.add(GUIStyle.secondaryButton("Refresh"));
        actionPnl.setBackground(GUIStyle.WHITE);
        viewBtn = GUIStyle.secondaryButton("View Details");
        addBtn = GUIStyle.primaryButton("Add " + singular(entity));
        actionPnl.add(viewBtn);
        actionPnl.add(addBtn);
        toolbarPnl.add(searchPnl, BorderLayout.WEST);
        toolbarPnl.add(actionPnl, BorderLayout.EAST);

        JTable table = new JTable(new DefaultTableModel(rows(), columns()));
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        JScrollPane scrollPane = GUIStyle.plainScrollPane(table);
        JPanel centrePnl = new JPanel(new BorderLayout(0, 15));
        centrePnl.setBackground(GUIStyle.BACKGROUND);
        centrePnl.add(toolbarPnl, BorderLayout.NORTH);
        centrePnl.add(scrollPane, BorderLayout.CENTER);
        contentPnl.add(topPnl, BorderLayout.NORTH);
        contentPnl.add(centrePnl, BorderLayout.CENTER);
        backBtn.addActionListener(this);
        addBtn.addActionListener(this);
        viewBtn.addActionListener(this);
        mainPnl.add(headerPnl, BorderLayout.NORTH);
        mainPnl.add(contentPnl, BorderLayout.CENTER);
        this.add(mainPnl);
        this.setTitle(singular(entity) + " Management");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(1100, 680));
        this.setLocationRelativeTo(null);
        GUIStyle.showFrame(this);
    }

    private String singular(String value) {
        if (value.equals("Classes")) return "Class";
        return value.endsWith("s") ? value.substring(0, value.length() - 1) : value;
    }

    private String[] columns() {
        switch (entity) {
            case "Applicants": return new String[]{"Person ID", "Applicant ID", "First Name", "Last Name", "Email"};
            case "Applications": return new String[]{"Application ID", "Applicant ID", "Applicant", "Course Code", "Course"};
            case "Students": return new String[]{"Student ID", "Student Number", "First Name", "Last Name", "Student Email"};
            case "Lecturers": return new String[]{"Lecturer ID", "Lecturer Number", "Name", "Lecturer Email", "Department"};
            case "Departments": return new String[]{"Department ID", "Department Code", "Department Name"};
            case "Courses": return new String[]{"Course ID", "Course Code", "Course Name", "Department"};
            case "Classes": return new String[]{"Class ID", "Class Code", "Class Name", "Course", "Lecturer"};
            default: return new String[]{"Registration ID", "Student Number", "Student", "Class Code", "Class"};
        }
    }

    private Object[][] rows() {
        switch (entity) {
            case "Applicants": return new Object[][]{{1001, 1, "Damien", "Swarts", "damien@example.com"}, {1002, 2, "Lerato", "Mokoena", "lerato@example.com"}};
            case "Applications": return new Object[][]{{1, 1, "Damien Swarts", "DIPICT", "Diploma in ICT"}, {2, 2, "Lerato Mokoena", "DIPACC", "Diploma in Accountancy"}};
            case "Students": return new Object[][]{{1, "222868791", "Damien", "Swarts", "222868791@mycput.ac.za"}, {2, "223456789", "Lerato", "Mokoena", "223456789@mycput.ac.za"}};
            case "Lecturers": return new Object[][]{{1, "EMP001", "A. Jacobs", "jacobsa@cput.ac.za", "Information Technology"}, {2, "EMP002", "N. Dube", "duben@cput.ac.za", "Business"}};
            case "Departments": return new Object[][]{{1, "DIT", "Information Technology"}, {2, "DBS", "Business Studies"}};
            case "Courses": return new Object[][]{{1, "DIPICT", "Diploma in ICT", "Information Technology"}, {2, "DIPACC", "Diploma in Accountancy", "Business Studies"}};
            case "Classes": return new Object[][]{{1, "ADT372S", "Applications Development Practice", "DIPICT", "A. Jacobs"}, {2, "ITS362S", "Information Systems", "DIPICT", "N. Dube"}};
            default: return new Object[][]{{1, "222868791", "Damien Swarts", "ADT372S", "Applications Development Practice"}, {2, "223456789", "Lerato Mokoena", "ITS362S", "Information Systems"}};
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.dispose();
        if (e.getSource() == backBtn) {
            new AdminDashboardGUI().setGUI();
        } else if (e.getSource() == addBtn) {
            new RecordFormGUI(entity).setGUI();
        } else {
            new RecordDetailsGUI(entity).setGUI();
        }
    }
}
