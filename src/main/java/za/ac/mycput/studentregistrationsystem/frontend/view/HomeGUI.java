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
import javax.swing.SwingConstants;

public class HomeGUI extends JFrame implements ActionListener {
    private JButton applyBtn;
    private JButton trackApplicationBtn;
    private JButton loginBtn;

    public HomeGUI() {
        applyBtn = GUIStyle.primaryButton("Apply");
        trackApplicationBtn = GUIStyle.primaryButton("Track Application");
        loginBtn = GUIStyle.secondaryButton("Login");
    }

    public void setGUI() {
        JPanel mainPnl = new JPanel(new BorderLayout());
        JPanel headerPnl = new JPanel(new BorderLayout());
        JPanel titlePnl = new JPanel(new GridLayout(2, 1, 4, 4));
        JPanel contentPnl = new JPanel(new BorderLayout(0, 24));
        JPanel cardPnl = new JPanel(new GridLayout(1, 3, 20, 20));

        headerPnl.setBackground(GUIStyle.NAVY);
        headerPnl.setBorder(BorderFactory.createEmptyBorder(24, 35, 24, 35));
        titlePnl.setBackground(GUIStyle.NAVY);
        JLabel systemLbl = new JLabel("STUDENT REGISTRATION SYSTEM");
        systemLbl.setFont(new Font("Arial", Font.BOLD, 24));
        systemLbl.setForeground(Color.WHITE);
        JLabel roleLbl = new JLabel("APPLICATION PORTAL");
        roleLbl.setForeground(new Color(209, 213, 219));
        titlePnl.add(systemLbl);
        titlePnl.add(roleLbl);
        headerPnl.add(titlePnl, BorderLayout.WEST);

        contentPnl.setBackground(GUIStyle.BACKGROUND);
        contentPnl.setBorder(BorderFactory.createEmptyBorder(44, 35, 44, 35));
        JPanel introPnl = new JPanel(new GridLayout(2, 1, 5, 5));
        introPnl.setBackground(GUIStyle.BACKGROUND);
        JLabel welcomeLbl = GUIStyle.heading("WELCOME", 28);
        welcomeLbl.setHorizontalAlignment(SwingConstants.LEFT);
        introPnl.add(welcomeLbl);
        JLabel subtitleLbl = GUIStyle.normalLabel("Select where you want to continue.");
        subtitleLbl.setForeground(GUIStyle.MUTED);
        introPnl.add(subtitleLbl);

        cardPnl.setBackground(GUIStyle.BACKGROUND);
        cardPnl.add(menuCard("APPLY", "Enter applicant information and choose courses.", applyBtn));
        cardPnl.add(menuCard("TRACK APPLICATION", "Preview application status information.", trackApplicationBtn));
        cardPnl.add(menuCard("LOGIN", "Administrator access to the dashboard.", loginBtn));

        contentPnl.add(introPnl, BorderLayout.NORTH);
        contentPnl.add(cardPnl, BorderLayout.CENTER);
        applyBtn.addActionListener(this);
        trackApplicationBtn.addActionListener(this);
        loginBtn.addActionListener(this);
        mainPnl.add(headerPnl, BorderLayout.NORTH);
        mainPnl.add(contentPnl, BorderLayout.CENTER);
        this.add(mainPnl);
        this.setTitle("Student Registration System");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(1050, 680));
        this.setLocationRelativeTo(null);
        GUIStyle.showFrame(this);
    }

    private JPanel menuCard(String titleText, String infoText, JButton button) {
        JPanel card = GUIStyle.whiteCard();
        JPanel buttonPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        card.setLayout(new BorderLayout(0, 15));
        JLabel title = GUIStyle.heading(titleText, 17);
        JLabel info = GUIStyle.normalLabel(infoText);
        buttonPnl.setBackground(GUIStyle.WHITE);
        buttonPnl.add(button);
        card.add(title, BorderLayout.NORTH);
        card.add(info, BorderLayout.CENTER);
        card.add(buttonPnl, BorderLayout.SOUTH);
        return card;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.dispose();
        if (e.getSource() == applyBtn) {
            new ApplicantFormGUI().setGUI();
        } else if (e.getSource() == trackApplicationBtn) {
            new TrackApplicationGUI().setGUI();
        } else {
            new LoginGUI().setGUI();
        }
    }
}
