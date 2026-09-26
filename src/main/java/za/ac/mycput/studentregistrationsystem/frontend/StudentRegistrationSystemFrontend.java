package za.ac.mycput.studentregistrationsystem.frontend;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import za.ac.mycput.studentregistrationsystem.frontend.view.HomeGUI;

public class StudentRegistrationSystemFrontend {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            za.ac.mycput.studentregistrationsystem.frontend.view.GUIStyle.installApplicationStyle();
            new HomeGUI().setGUI();
        });
    }
}
