package za.ac.mycput.studentregistrationsystem.frontend.view;
import java.awt.*;
import javax.swing.*;
import za.ac.mycput.studentregistrationsystem.frontend.service.BackendApiClient;
public class PersonalSettingsPanel extends JPanel {
 private final JTextField[] fields=new JTextField[7];
 private final JButton save=GUIStyle.primaryButton("Save personal details");
 private final JLabel status=new JLabel("Loading personal details…");
 private final boolean student="STUDENT".equals(BackendApiClient.currentRole());
 private final int lecturerIndex;
 public PersonalSettingsPanel(int lecturerIndex){
  super(new BorderLayout(16,16));this.lecturerIndex=lecturerIndex;
  setBackground(Color.WHITE);setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));
  JPanel form=new JPanel(new GridLayout(0,2,12,14));form.setOpaque(false);
  String[] labels={"Personal email","Phone number","Street","Suburb","City","Postal code","Province"};
  for(int i=0;i<7;i++){fields[i]=new JTextField();GUIStyle.styleField(fields[i]);form.add(new JLabel(labels[i]));form.add(fields[i]);}
  add(form,BorderLayout.NORTH);JPanel bottom=new JPanel(new BorderLayout());bottom.setOpaque(false);bottom.add(status,BorderLayout.CENTER);bottom.add(save,BorderLayout.EAST);add(bottom,BorderLayout.SOUTH);
  save.setEnabled(false);save.addActionListener(e->save());
  new SwingWorker<String[],Void>(){
   protected String[] doInBackground() throws Exception {
    if(student)return java.util.Arrays.copyOfRange(BackendApiClient.getStudentPersonalDetails(ApplicationPreviewData.studentId()),5,12);
    String[] record=BackendApiClient.findLecturerByEmail(BackendApiClient.currentUsername()).split("\\|",-1);
    return new String[]{record[LecturerData.PERSONAL_EMAIL],record[LecturerData.PHONE],record[LecturerData.STREET],record[LecturerData.SUBURB],record[LecturerData.CITY],record[LecturerData.POSTAL_CODE],record[LecturerData.PROVINCE]};
   }
   protected void done(){try{String[] values=get();for(int i=0;i<7;i++)fields[i].setText(values[i]);save.setEnabled(true);status.setText("Your institutional email, IDs and academic details cannot be changed here.");}catch(Exception ex){status.setText("Unable to load details. Reopen settings to retry.");}}
  }.execute();
 }
 private void save(){
  String[] v=new String[7];for(int i=0;i<7;i++){v[i]=fields[i].getText().trim();if(v[i].isBlank()){JOptionPane.showMessageDialog(this,"Complete all fields.");return;}}
  if(!v[0].matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")){JOptionPane.showMessageDialog(this,"Enter a valid email.");return;}
  save.setEnabled(false);status.setText("Saving…");
  new SwingWorker<Void,Void>(){
   protected Void doInBackground() throws Exception {
    if(student){String[] u=BackendApiClient.updateStudentPersonalDetails(ApplicationPreviewData.studentId(),v[0],v[1],v[2],v[3],v[4],v[5],v[6]);
     ApplicationPreviewData.updatePersonalDetails(u[0],u[1],u[2],u[3],u[4],u[5],u[6],u[7],u[8],u[9],u[10],u[11]);
    }else LecturerData.useBackendLecturer(BackendApiClient.updateOwnLecturerDetails(v));return null;
   }
   protected void done(){save.setEnabled(true);try{get();status.setText("Personal details saved.");}catch(Exception ex){status.setText("Could not save details.");JOptionPane.showMessageDialog(PersonalSettingsPanel.this,ex.getCause()==null?ex.getMessage():ex.getCause().getMessage());}}
  }.execute();
 }
}
