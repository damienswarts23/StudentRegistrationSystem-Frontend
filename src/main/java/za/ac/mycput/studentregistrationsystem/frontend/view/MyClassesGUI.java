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

public class MyClassesGUI extends AppFrame implements ActionListener {
    private JButton backBtn;

    public void setGUI() {
        JPanel mainPnl = new JPanel(new BorderLayout());
        JPanel headerPnl = new JPanel(new BorderLayout());
        JPanel contentPnl = new JPanel(new BorderLayout(0, 22));
        JPanel cardPnl = GUIStyle.whiteCard();
        JPanel classPnl = new JPanel(new GridLayout(0, 4, 18, 15));
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
        titlePnl.add(GUIStyle.heading("MY CLASSES", 25));
        JLabel subtitle = GUIStyle.normalLabel("Classes currently registered to " + ApplicationPreviewData.fullName() + ".");
        subtitle.setForeground(GUIStyle.MUTED);
        titlePnl.add(subtitle);

        classPnl.setBackground(GUIStyle.WHITE);
        addHeaderRow(classPnl);
        String[][] registeredClasses = StudentClassData.registeredClasses();
        if (registeredClasses.length == 0) {
            addEmptyRow(classPnl);
        } else {
            for (String[] classRow : registeredClasses) {
                addClassRow(classPnl, classRow);
            }
        }

        backBtn = GUIStyle.secondaryButton("Dashboard");
        buttonPnl.setBackground(GUIStyle.WHITE);
        buttonPnl.add(backBtn);
        cardPnl.setLayout(new BorderLayout(0, 24));
        cardPnl.add(GUIStyle.plainScrollPane(classPnl), BorderLayout.CENTER);
        cardPnl.add(buttonPnl, BorderLayout.SOUTH);
        contentPnl.add(titlePnl, BorderLayout.NORTH);
        contentPnl.add(cardPnl, BorderLayout.CENTER);
        backBtn.addActionListener(this);
        mainPnl.add(headerPnl, BorderLayout.NORTH);
        mainPnl.add(contentPnl, BorderLayout.CENTER);
        this.add(mainPnl);
        this.setTitle("My Classes");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(1000, 620));
        this.setLocationRelativeTo(null);
        GUIStyle.showFrame(this);
    }

    private void addHeaderRow(JPanel panel) {
        panel.add(boldLabel("Class Code"));
        panel.add(boldLabel("Class Name"));
        panel.add(boldLabel("Course"));
        panel.add(boldLabel("Lecturer"));
    }

    private void addClassRow(JPanel panel, String[] classRow) {
        panel.add(GUIStyle.normalLabel(classRow[0]));
        panel.add(GUIStyle.normalLabel(classRow[1]));
        panel.add(GUIStyle.normalLabel(classRow[2]));
        panel.add(GUIStyle.normalLabel(classRow[3]));
    }

    private void addEmptyRow(JPanel panel) {
        panel.add(GUIStyle.normalLabel("No classes registered"));
        panel.add(GUIStyle.normalLabel(""));
        panel.add(GUIStyle.normalLabel(""));
        panel.add(GUIStyle.normalLabel(""));
    }

    private JLabel boldLabel(String text) {
        JLabel label = GUIStyle.normalLabel(text);
        label.setFont(new Font(GUIStyle.FONT_FAMILY, Font.BOLD, 13));
        return label;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.dispose();
        new StudentDashboardGUI().setGUI();
    }
}
