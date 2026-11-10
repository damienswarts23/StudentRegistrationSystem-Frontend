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
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import za.ac.mycput.studentregistrationsystem.frontend.service.BackendApiClient.ClassOption;

public class AssignLecturerClassesGUI extends AppFrame implements ActionListener {
    private List<ClassOption> classes = new ArrayList<>();
    private JCheckBox[] classCheckBoxes;
    private JButton backBtn;
    private JButton doneBtn;

    public void setGUI() {
        JPanel mainPnl = new JPanel(new BorderLayout());
        JPanel headerPnl = new JPanel(new BorderLayout());
        JPanel contentPnl = new JPanel(new BorderLayout(0, 22));
        JPanel cardPnl = GUIStyle.whiteCard();
        JPanel classPnl = new JPanel(new GridLayout(0, 3, 18, 15));
        JPanel buttonPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        String[] lecturer = LecturerData.pendingLecturer();

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
        titlePnl.add(GUIStyle.heading("ASSIGN CLASSES", 25));
        JLabel subtitle = GUIStyle.normalLabel("Select any classes from "
                + lecturer[LecturerData.DEPARTMENT] + ". A lecturer can teach multiple classes.");
        subtitle.setForeground(GUIStyle.MUTED);
        titlePnl.add(subtitle);

        classPnl.setBackground(GUIStyle.WHITE);
        classPnl.add(boldLabel("Class Code"));
        classPnl.add(boldLabel("Class Name"));
        classPnl.add(boldLabel("Select"));
        loadClasses(classPnl, Integer.parseInt(lecturer[LecturerData.DEPARTMENT_ID]));

        backBtn = GUIStyle.secondaryButton("Back");
        doneBtn = GUIStyle.primaryButton("Done");
        buttonPnl.setBackground(GUIStyle.WHITE);
        buttonPnl.add(backBtn);
        buttonPnl.add(doneBtn);
        cardPnl.setLayout(new BorderLayout(0, 24));
        cardPnl.add(GUIStyle.plainScrollPane(classPnl), BorderLayout.CENTER);
        cardPnl.add(buttonPnl, BorderLayout.SOUTH);
        contentPnl.add(titlePnl, BorderLayout.NORTH);
        contentPnl.add(cardPnl, BorderLayout.CENTER);
        backBtn.addActionListener(this);
        doneBtn.addActionListener(this);
        mainPnl.add(headerPnl, BorderLayout.NORTH);
        mainPnl.add(contentPnl, BorderLayout.CENTER);
        this.add(mainPnl);
        this.setTitle("Assign Classes");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(900, 620));
        this.setLocationRelativeTo(null);
        GUIStyle.showFrame(this);
    }

    private void loadClasses(JPanel classPnl, int departmentId) {
        try {
            classes = LecturerData.availableClasses(departmentId);
            classCheckBoxes = new JCheckBox[classes.size()];
            for (int i = 0; i < classes.size(); i++) {
                ClassOption courseClass = classes.get(i);
                JCheckBox checkBox = new JCheckBox();
                checkBox.setBackground(GUIStyle.WHITE);
                classCheckBoxes[i] = checkBox;
                classPnl.add(GUIStyle.normalLabel(courseClass.getCode()));
                classPnl.add(GUIStyle.normalLabel(courseClass.getName()));
                classPnl.add(checkBox);
            }
            if (classes.isEmpty()) {
                classPnl.add(GUIStyle.normalLabel("No classes are available in this department."));
                classPnl.add(new JLabel());
                classPnl.add(new JLabel());
            }
        } catch (Exception exception) {
            classes = new ArrayList<>();
            classCheckBoxes = new JCheckBox[0];
            classPnl.add(GUIStyle.normalLabel("Backend unavailable."));
            classPnl.add(new JLabel());
            classPnl.add(new JLabel());
            SwingUtilitiesHelper.showBackendError(this, exception);
        }
    }

    private JLabel boldLabel(String text) {
        JLabel label = GUIStyle.normalLabel(text);
        label.setFont(new Font(GUIStyle.FONT_FAMILY, Font.BOLD, 13));
        return label;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backBtn) {
            this.dispose();
            new AddLecturerGUI().setGUI();
        } else {
            ArrayList<String> selectedClasses = new ArrayList<>();
            ArrayList<Integer> selectedClassIds = new ArrayList<>();
            for (int i = 0; i < classCheckBoxes.length; i++) {
                if (classCheckBoxes[i].isSelected()) {
                    ClassOption courseClass = classes.get(i);
                    selectedClasses.add(courseClass.getCode() + " - " + courseClass.getName());
                    selectedClassIds.add(courseClass.getId());
                }
            }
            int[] ids = new int[selectedClassIds.size()];
            for (int i = 0; i < ids.length; i++) {
                ids[i] = selectedClassIds.get(i);
            }
            LecturerData.setPendingClasses(selectedClasses.toArray(new String[0]), ids);
            this.dispose();
            new LecturerSavePreviewGUI().setGUI();
        }
    }
}
