package za.ac.mycput.studentregistrationsystem.frontend.view;

import java.awt.Component;
import javax.swing.JOptionPane;

public class SwingUtilitiesHelper {
    private SwingUtilitiesHelper() {
    }

    public static void showBackendError(Component parent, Exception exception) {
        String message = exception == null || exception.getMessage() == null
                ? "The request could not be completed."
                : exception.getMessage();

        if (message.contains("Backend returned HTTP")) {
            JOptionPane.showMessageDialog(parent,
                    "The backend received the request but could not complete it.\n" + message,
                    "Request Not Completed",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(parent,
                    "The backend could not be reached on port 8080.\n" + message,
                    "Backend Connection Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
