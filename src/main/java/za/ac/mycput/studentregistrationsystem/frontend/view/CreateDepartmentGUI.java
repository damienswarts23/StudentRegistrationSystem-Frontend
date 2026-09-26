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

public class CreateDepartmentGUI extends JFrame implements ActionListener {
    private JTextField departmentCodeTxtFld;
    private JTextField departmentNameTxtFld;
    private JButton cancelBtn;
    private JButton addCoursesBtn;

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
        titlePnl.add(GUIStyle.heading("CREATE DEPARTMENT", 25));
        JLabel subtitle = GUIStyle.normalLabel("Enter department information, then add courses.");
        subtitle.setForeground(GUIStyle.MUTED);
        titlePnl.add(subtitle);

        departmentCodeTxtFld = new JTextField();
        departmentNameTxtFld = new JTextField();
        GUIStyle.styleField(departmentCodeTxtFld);
        GUIStyle.styleField(departmentNameTxtFld);
        formPnl.setBackground(GUIStyle.WHITE);
        formPnl.add(GUIStyle.normalLabel("Department Code:"));
        formPnl.add(departmentCodeTxtFld);
        formPnl.add(GUIStyle.normalLabel("Department Name:"));
        formPnl.add(departmentNameTxtFld);

        cancelBtn = GUIStyle.secondaryButton("Cancel");
        addCoursesBtn = GUIStyle.primaryButton("Add Courses");
        buttonPnl.setBackground(GUIStyle.WHITE);
        buttonPnl.add(cancelBtn);
        buttonPnl.add(addCoursesBtn);
        cardPnl.setLayout(new BorderLayout(0, 24));
        cardPnl.add(formPnl, BorderLayout.CENTER);
        cardPnl.add(buttonPnl, BorderLayout.SOUTH);
        contentPnl.add(titlePnl, BorderLayout.NORTH);
        contentPnl.add(cardPnl, BorderLayout.CENTER);
        cancelBtn.addActionListener(this);
        addCoursesBtn.addActionListener(this);
        mainPnl.add(headerPnl, BorderLayout.NORTH);
        mainPnl.add(contentPnl, BorderLayout.CENTER);
        this.add(mainPnl);
        this.setTitle("Create Department");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(980, 650));
        this.setLocationRelativeTo(null);
        GUIStyle.showFrame(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.dispose();
        if (e.getSource() == cancelBtn) {
            new AdminDashboardGUI().setGUI();
        } else {
            DepartmentCreationData.startDepartment(
                    departmentCodeTxtFld.getText(),
                    departmentNameTxtFld.getText());
            new DepartmentCoursesGUI().setGUI();
        }
    }
}
