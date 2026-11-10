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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import za.ac.mycput.studentregistrationsystem.frontend.service.BackendApiClient;
import za.ac.mycput.studentregistrationsystem.frontend.service.BackendApiClient.CourseOption;

public class CourseSelectionGUI extends AppFrame implements ActionListener {
    private JComboBox<CourseOption> firstCourseCmb;
    private JComboBox<CourseOption> secondCourseCmb;
    private JComboBox<CourseOption> thirdCourseCmb;
    private JButton backBtn;
    private JButton submitApplicationBtn;

    public void setGUI() {
        JPanel mainPnl = new JPanel(new BorderLayout());
        JPanel headerPnl = new JPanel(new BorderLayout());
        JPanel contentPnl = new JPanel(new BorderLayout(0, 22));
        JPanel cardPnl = GUIStyle.whiteCard();
        JPanel formPnl = new JPanel(new GridLayout(3, 2, 20, 16));
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
        titlePnl.add(GUIStyle.heading("CHOOSE COURSES", 25));
        JLabel subtitle = GUIStyle.normalLabel("Choose three courses for this application.");
        subtitle.setForeground(GUIStyle.MUTED);
        titlePnl.add(subtitle);

        CourseOption[] courseOptions = loadCourseOptions();
        firstCourseCmb = new JComboBox<>(courseOptions);
        secondCourseCmb = new JComboBox<>(courseOptions);
        thirdCourseCmb = new JComboBox<>(courseOptions);
        formPnl.setBackground(GUIStyle.WHITE);
        formPnl.add(GUIStyle.normalLabel("First Course:"));
        formPnl.add(firstCourseCmb);
        formPnl.add(GUIStyle.normalLabel("Second Course:"));
        formPnl.add(secondCourseCmb);
        formPnl.add(GUIStyle.normalLabel("Third Course:"));
        formPnl.add(thirdCourseCmb);

        backBtn = GUIStyle.secondaryButton("Back");
        submitApplicationBtn = GUIStyle.primaryButton("Submit Application");
        buttonPnl.setBackground(GUIStyle.WHITE);
        buttonPnl.add(backBtn);
        buttonPnl.add(submitApplicationBtn);
        cardPnl.setLayout(new BorderLayout(0, 24));
        cardPnl.add(formPnl, BorderLayout.CENTER);
        cardPnl.add(buttonPnl, BorderLayout.SOUTH);
        contentPnl.add(titlePnl, BorderLayout.NORTH);
        contentPnl.add(cardPnl, BorderLayout.CENTER);
        backBtn.addActionListener(this);
        submitApplicationBtn.addActionListener(this);
        mainPnl.add(headerPnl, BorderLayout.NORTH);
        mainPnl.add(contentPnl, BorderLayout.CENTER);
        this.add(mainPnl);
        this.setTitle("Choose Courses");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(900, 620));
        this.setLocationRelativeTo(null);
        GUIStyle.showFrame(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backBtn) {
            this.dispose();
            new ApplicantFormGUI().setGUI();
        } else {
            try {
                List<Integer> courseIds = List.of(
                        selectedCourseId(firstCourseCmb),
                        selectedCourseId(secondCourseCmb),
                        selectedCourseId(thirdCourseCmb));
                if (courseIds.contains(0) || new HashSet<>(courseIds).size() != 3) {
                    throw new IllegalArgumentException("Select three different courses.");
                }
                BackendApiClient.submitApplications(courseIds);
                JOptionPane.showMessageDialog(this,
                        "Application submitted successfully.\n"
                        + "Your application result is not shown on this confirmation screen.\n"
                        + "Use Track Application from the home screen when you want to view the result.");
                this.dispose();
                new HomeGUI().setGUI();
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(this,
                        "Could not submit the application. Make sure the backend is running and the IDs are unique.\n"
                        + exception.getMessage());
            }
        }
    }

    private int selectedCourseId(JComboBox<CourseOption> comboBox) {
        CourseOption selected = (CourseOption) comboBox.getSelectedItem();
        return selected == null ? 0 : selected.getId();
    }

    private CourseOption[] loadCourseOptions() {
        List<CourseOption> options = new ArrayList<>();
        options.add(new CourseOption(0, "", "Select Course"));
        try {
            options.addAll(BackendApiClient.getCourses());
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this,
                    "Courses could not be loaded. Start the backend and reopen this screen.");
        }
        return options.toArray(new CourseOption[0]);
    }
}
