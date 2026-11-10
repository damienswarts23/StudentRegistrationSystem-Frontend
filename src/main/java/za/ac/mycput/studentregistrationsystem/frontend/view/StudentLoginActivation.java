package za.ac.mycput.studentregistrationsystem.frontend.view;
import javax.swing.*;
import java.awt.Component;
import za.ac.mycput.studentregistrationsystem.frontend.service.BackendApiClient;
public final class StudentLoginActivation {
    private StudentLoginActivation() {}
    public static void enable(Component parent,JButton button,int studentId) { activate(parent,button,studentId,false); }
    public static void reset(Component parent,JButton button,int studentId) {
        if(JOptionPane.showConfirmDialog(parent,"Set a new login password for this student? Any existing password will stop working.",
            "Set/reset student login",JOptionPane.OK_CANCEL_OPTION)!=JOptionPane.OK_OPTION)return;
        activate(parent,button,studentId,true);
    }
    private static void activate(Component parent,JButton button,int studentId,boolean reset) {
        String password=LecturerLoginDialog.choosePassword(parent,"student");
        if(password==null)return;
        button.setEnabled(false);
        new SwingWorker<String,Void>() {
            protected String doInBackground() throws Exception {return reset?BackendApiClient.resetStudentLogin(studentId,password):BackendApiClient.enableStudentLogin(studentId,password);}
            protected void done() {
                button.setEnabled(true);
                try {JOptionPane.showMessageDialog(parent,"Student login enabled.\nUsername: "+get()+
                    "\nSign in using the password you just entered, not the student number.");}
                catch(Exception error) {
                    Throwable cause=error.getCause()==null?error:error.getCause();
                    JOptionPane.showMessageDialog(parent,cause.getMessage(),"Unable to enable login",JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }
}
