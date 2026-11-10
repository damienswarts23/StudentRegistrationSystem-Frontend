package za.ac.mycput.studentregistrationsystem.frontend.view;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.*;
public final class LecturerLoginDialog {
    private LecturerLoginDialog() {}
    public static String choosePassword(Component parent) { return choosePassword(parent,"lecturer"); }
    public static String choosePassword(Component parent,String role) {
        JPasswordField password=new JPasswordField(24),confirm=new JPasswordField(24);
        JPanel fields=new JPanel(new GridLayout(0,1,6,6));
        fields.add(new JLabel("Set the "+role+"'s initial login password."));
        fields.add(new JLabel("Use 10+ characters with uppercase, lowercase and a number."));
        fields.add(new JLabel("Password"));fields.add(password);
        fields.add(new JLabel("Confirm password"));fields.add(confirm);
        while(JOptionPane.showConfirmDialog(parent,fields,"Set login password",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE)==JOptionPane.OK_OPTION) {
            String value=new String(password.getPassword());
            if(!value.equals(new String(confirm.getPassword()))) {
                JOptionPane.showMessageDialog(parent,"Passwords do not match.");continue;
            }
            if(value.length()<10 || value.getBytes(java.nio.charset.StandardCharsets.UTF_8).length>72
                || value.chars().noneMatch(Character::isUpperCase) || value.chars().noneMatch(Character::isLowerCase)
                || value.chars().noneMatch(Character::isDigit)) {
                JOptionPane.showMessageDialog(parent,"Use 10+ characters with uppercase, lowercase and a number (maximum 72 UTF-8 bytes).");continue;
            }
            password.setText("");confirm.setText("");return value;
        }
        password.setText("");confirm.setText("");return null;
    }
}
