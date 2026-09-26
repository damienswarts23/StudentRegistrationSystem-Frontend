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
import javax.swing.table.DefaultTableModel;

public class LecturerClassStudentsGUI extends JFrame implements ActionListener {
    private final int lecturerIndex;
    private final String classCode;
    private final String className;
    private JButton backBtn;

    public LecturerClassStudentsGUI(int lecturerIndex, String classCode, String className) {
        this.lecturerIndex = lecturerIndex;
        this.classCode = classCode;
        this.className = className;
    }

    public void setGUI() {
        JPanel mainPnl = new JPanel(new BorderLayout());
        JPanel headerPnl = new JPanel(new BorderLayout());
        JPanel contentPnl = new JPanel(new BorderLayout(0, 18));
        JPanel buttonPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        headerPnl.setBackground(GUIStyle.NAVY);
        headerPnl.setBorder(BorderFactory.createEmptyBorder(22, 34, 22, 34));
        JLabel headerLbl = new JLabel("STUDENT REGISTRATION SYSTEM");
        headerLbl.setFont(new Font("Arial", Font.BOLD, 21));
        headerLbl.setForeground(Color.WHITE);
        headerPnl.add(headerLbl, BorderLayout.WEST);

        contentPnl.setBackground(GUIStyle.BACKGROUND);
        contentPnl.setBorder(BorderFactory.createEmptyBorder(32, 50, 35, 50));
        JPanel titlePnl = new JPanel(new GridLayout(2, 1, 5, 5));
        titlePnl.setBackground(GUIStyle.BACKGROUND);
        titlePnl.add(GUIStyle.heading(classCode + " STUDENTS", 25));
        JLabel subtitle = GUIStyle.normalLabel(className + " student details.");
        subtitle.setForeground(GUIStyle.MUTED);
        titlePnl.add(subtitle);

        JTable studentTable = new JTable(new DefaultTableModel(LecturerStudentData.studentsForClass(classCode), new String[]{"Student Number", "First Name", "Last Name", "Student Email", "Date of Birth"}));
        studentTable.setRowHeight(30);
        studentTable.setFont(new Font("Arial", Font.PLAIN, 13));
        studentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        JScrollPane scrollPane = GUIStyle.plainScrollPane(studentTable);

        backBtn = GUIStyle.secondaryButton("Back");
        buttonPnl.setBackground(GUIStyle.BACKGROUND);
        buttonPnl.add(backBtn);
        contentPnl.add(titlePnl, BorderLayout.NORTH);
        contentPnl.add(scrollPane, BorderLayout.CENTER);
        contentPnl.add(buttonPnl, BorderLayout.SOUTH);
        backBtn.addActionListener(this);
        mainPnl.add(headerPnl, BorderLayout.NORTH);
        mainPnl.add(contentPnl, BorderLayout.CENTER);
        this.add(mainPnl);
        this.setTitle("Class Students");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(1050, 650));
        this.setLocationRelativeTo(null);
        GUIStyle.showFrame(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.dispose();
        new LecturerViewStudentsGUI(lecturerIndex).setGUI();
    }
}
