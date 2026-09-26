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
import javax.swing.JTextField;

public class EditAddDepartmentCourseGUI extends JFrame implements ActionListener {
    private final int departmentIndex;
    private JTextField courseCodeTxtFld;
    private JTextField courseNameTxtFld;
    private JButton cancelBtn;
    private JButton doneBtn;

    public EditAddDepartmentCourseGUI(int departmentIndex) {
        this.departmentIndex = departmentIndex;
    }

    public void setGUI() {
        JPanel mainPnl = new JPanel(new BorderLayout());
        JPanel headerPnl = new JPanel(new BorderLayout());
        JPanel contentPnl = new JPanel(new BorderLayout(0, 22));
        JPanel cardPnl = GUIStyle.whiteCard();
        JPanel formPnl = new JPanel(new GridLayout(2, 2, 20, 16));
        JPanel buttonPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        headerPnl.setBackground(GUIStyle.NAVY);
        headerPnl.setBorder(BorderFactory.createEmptyBorder(22, 34, 22, 34));
        JLabel headerLbl = new JLabel("STUDENT REGISTRATION SYSTEM");
        headerLbl.setFont(new Font("Arial", Font.BOLD, 21));
        headerLbl.setForeground(Color.WHITE);
        headerPnl.add(headerLbl, BorderLayout.WEST);

        contentPnl.setBackground(GUIStyle.BACKGROUND);
        contentPnl.setBorder(BorderFactory.createEmptyBorder(60, 210, 60, 210));
        JPanel titlePnl = new JPanel(new GridLayout(2, 1, 5, 5));
        titlePnl.setBackground(GUIStyle.BACKGROUND);
        titlePnl.add(GUIStyle.heading("ADD COURSE", 25));
        JLabel subtitle = GUIStyle.normalLabel("Add another course to this department.");
        subtitle.setForeground(GUIStyle.MUTED);
        titlePnl.add(subtitle);

        courseCodeTxtFld = new JTextField();
        courseNameTxtFld = new JTextField();
        GUIStyle.styleField(courseCodeTxtFld);
        GUIStyle.styleField(courseNameTxtFld);
        formPnl.setBackground(GUIStyle.WHITE);
        formPnl.add(GUIStyle.normalLabel("Course Code:"));
        formPnl.add(courseCodeTxtFld);
        formPnl.add(GUIStyle.normalLabel("Course Name:"));
        formPnl.add(courseNameTxtFld);

        cancelBtn = GUIStyle.secondaryButton("Cancel");
        doneBtn = GUIStyle.primaryButton("Done");
        buttonPnl.setBackground(GUIStyle.WHITE);
        buttonPnl.add(cancelBtn);
        buttonPnl.add(doneBtn);
        cardPnl.setLayout(new BorderLayout(0, 24));
        cardPnl.add(formPnl, BorderLayout.CENTER);
        cardPnl.add(buttonPnl, BorderLayout.SOUTH);
        contentPnl.add(titlePnl, BorderLayout.NORTH);
        contentPnl.add(cardPnl, BorderLayout.CENTER);
        cancelBtn.addActionListener(this);
        doneBtn.addActionListener(this);
        mainPnl.add(headerPnl, BorderLayout.NORTH);
        mainPnl.add(contentPnl, BorderLayout.CENTER);
        this.add(mainPnl);
        this.setTitle("Add Course");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(980, 650));
        this.setLocationRelativeTo(null);
        GUIStyle.showFrame(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.dispose();
        if (e.getSource() == doneBtn) {
            DepartmentCreationData.addCourseToDepartment(
                    departmentIndex,
                    courseCodeTxtFld.getText(),
                    courseNameTxtFld.getText());
        }
        new EditDepartmentCoursesGUI(departmentIndex).setGUI();
    }
}
