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

public class LecturerMyClassesGUI extends AppFrame implements ActionListener {
    private final int lecturerIndex;
    private JButton backBtn;

    public LecturerMyClassesGUI(int lecturerIndex) {
        this.lecturerIndex = lecturerIndex;
    }

    public void setGUI() {
        JPanel mainPnl = new JPanel(new BorderLayout());
        JPanel headerPnl = new JPanel(new BorderLayout());
        JPanel contentPnl = new JPanel(new BorderLayout(0, 18));
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
        JLabel subtitle = GUIStyle.normalLabel("Classes assigned to " + LecturerData.fullNameAt(lecturerIndex) + ".");
        subtitle.setForeground(GUIStyle.MUTED);
        titlePnl.add(subtitle);

        JTable classTable = new JTable(new DefaultTableModel(rows(), new String[]{"Class Code", "Class Name"}));
        classTable.setRowHeight(30);
        classTable.setFont(new Font(GUIStyle.FONT_FAMILY, Font.PLAIN, 13));
        classTable.getTableHeader().setFont(new Font(GUIStyle.FONT_FAMILY, Font.BOLD, 13));
        JScrollPane scrollPane = GUIStyle.plainScrollPane(classTable);

        backBtn = GUIStyle.secondaryButton("Dashboard");
        buttonPnl.setBackground(GUIStyle.BACKGROUND);
        buttonPnl.add(backBtn);
        contentPnl.add(titlePnl, BorderLayout.NORTH);
        contentPnl.add(scrollPane, BorderLayout.CENTER);
        contentPnl.add(buttonPnl, BorderLayout.SOUTH);
        backBtn.addActionListener(this);
        mainPnl.add(headerPnl, BorderLayout.NORTH);
        mainPnl.add(contentPnl, BorderLayout.CENTER);
        this.add(mainPnl);
        this.setTitle("Lecturer Classes");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(900, 620));
        this.setLocationRelativeTo(null);
        GUIStyle.showFrame(this);
    }

    private Object[][] rows() {
        String[] classes = LecturerData.classesAt(lecturerIndex);
        Object[][] rows = new Object[classes.length][2];
        for (int i = 0; i < classes.length; i++) {
            rows[i][0] = LecturerData.classCode(classes[i]);
            rows[i][1] = LecturerData.className(classes[i]);
        }
        return rows;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.dispose();
        new LecturerDashboardGUI(lecturerIndex).setGUI();
    }
}
