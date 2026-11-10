package za.ac.mycput.studentregistrationsystem.frontend.view;
import java.awt.*;
import java.nio.file.Files;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import za.ac.mycput.studentregistrationsystem.frontend.service.BackendApiClient;
public class AdminSettingsGUI extends AppFrame {
 private final String role=BackendApiClient.currentRole();
 private final int lecturerIndex;
 public AdminSettingsGUI(){this(-1);}
 public AdminSettingsGUI(int lecturerIndex){this.lecturerIndex=lecturerIndex;}
 private void dashboard(){
  dispose();
  if("ADMIN".equals(role))new AdminDashboardGUI().setGUI();
  else if("STUDENT".equals(role))new StudentDashboardGUI().setGUI();
  else new LecturerDashboardGUI(lecturerIndex).setGUI();
 }
 private final JTextField name=new JTextField(BackendApiClient.currentName(),24);
 private final JPanel photoHolder=new JPanel(new FlowLayout(FlowLayout.LEFT));
 private final JLabel status=new JLabel(" ");
 private JPanel root;
 public void setGUI() {
  if(!java.util.Set.of("ADMIN","STUDENT","LECTURER").contains(role)) {dispose();new LoginGUI().setGUI();return;}
  root=new JPanel(new BorderLayout(20,20));root.setBackground(GUIStyle.BACKGROUND);
  root.setBorder(BorderFactory.createEmptyBorder(18, 32, 18, 32));
  JPanel heading=new JPanel(new BorderLayout());heading.setOpaque(false);
  heading.add(GUIStyle.heading("ACCOUNT SETTINGS",26),BorderLayout.WEST);
  JButton back=GUIStyle.secondaryButton("Back to dashboard");
  back.addActionListener(e->dashboard());heading.add(back,BorderLayout.EAST);
  root.add(heading,BorderLayout.NORTH);
  JTabbedPane tabs=new JTabbedPane();tabs.addTab("My profile",profile());tabs.addTab("Password",password());if("ADMIN".equals(role))tabs.addTab("Create admin account",accounts());
  else tabs.addTab("Personal details",new PersonalSettingsPanel(lecturerIndex));
  root.add(tabs,BorderLayout.CENTER);root.add(status,BorderLayout.SOUTH);add(root);
  setTitle("Account settings");setDefaultCloseOperation(EXIT_ON_CLOSE);setMinimumSize(new Dimension(1000,720));setLocationRelativeTo(null);GUIStyle.showFrame(this);
 }
 private JPanel card() {
  JPanel p=new JPanel(){@Override protected void addImpl(Component component,Object constraints,int index){
   if(component instanceof JComponent child)child.setAlignmentX(Component.LEFT_ALIGNMENT);
   super.addImpl(component,constraints,index);
  }};
  p.setBackground(Color.WHITE);p.setBorder(GUIStyle.whiteCard().getBorder());p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));return p;
 }
 private void field(JPanel p,String label,JComponent field) {
  JPanel row=new JPanel(new BorderLayout(12,0));row.setOpaque(false);row.setMaximumSize(new Dimension(800,44));
  JLabel l=GUIStyle.normalLabel(label);l.setPreferredSize(new Dimension(210,34));row.add(l,BorderLayout.WEST);row.add(field,BorderLayout.CENTER);p.add(row);p.add(Box.createVerticalStrut(14));
 }
 private JPanel profile() {
  JPanel p=card();field(p,"Signed in as",new JLabel(BackendApiClient.currentUsername()));field(p,"Name",name);name.setEditable("ADMIN".equals(role));name.setBackground(Color.WHITE);
  JButton save=GUIStyle.primaryButton("Save name");save.addActionListener(e->{String value=name.getText();runTask(()->{BackendApiClient.saveName(value);return "Name saved.";},null);});JPanel nameAction=new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));nameAction.setOpaque(false);nameAction.setMaximumSize(new Dimension(800,36));nameAction.setPreferredSize(new Dimension(800,36));
  if("ADMIN".equals(role))nameAction.add(save);else nameAction.add(new JLabel("Your name is managed by an administrator."));
  p.add(nameAction);p.add(Box.createVerticalStrut(22));
  photoHolder.setOpaque(false);photoHolder.add(DashboardComponents.portrait());photoHolder.setMaximumSize(new Dimension(800,160));p.add(photoHolder);
  JPanel actions=new JPanel(new FlowLayout(FlowLayout.LEFT));actions.setOpaque(false);
  JButton upload=GUIStyle.primaryButton("Choose profile photo");JButton remove=GUIStyle.secondaryButton("Remove photo");
  upload.addActionListener(e->{JFileChooser chooser=new JFileChooser();chooser.setFileFilter(new FileNameExtensionFilter("PNG / JPEG photo","png","jpg","jpeg"));
   if(chooser.showOpenDialog(this)!=JFileChooser.APPROVE_OPTION)return;
   var path=chooser.getSelectedFile().toPath();
   runTask(()->{if(Files.size(path)>2*1024*1024)throw new Exception("Choose a photo smaller than 2 MB.");BackendApiClient.savePhoto(Files.readAllBytes(path));return "Photo saved.";},this::refreshPhoto);
  });
  remove.addActionListener(e->runTask(()->{BackendApiClient.removePhoto();return "Photo removed.";},this::refreshPhoto));
  actions.add(upload);actions.add(remove);actions.setMaximumSize(new Dimension(800,60));p.add(actions);
  p.add(new JLabel("PNG or JPEG, up to 2 MB. Your photo is saved with your account."));return p;
 }
 private void refreshPhoto(){photoHolder.removeAll();photoHolder.add(DashboardComponents.portrait());photoHolder.revalidate();photoHolder.repaint();}
 private JPanel password() {
  JPanel p=card();JPasswordField current=new JPasswordField(),next=new JPasswordField(),confirm=new JPasswordField();
  field(p,"Current password",current);field(p,"New password",next);field(p,"Confirm new password",confirm);
  p.add(new JLabel("Use 10+ characters with uppercase, lowercase and a number."));p.add(Box.createVerticalStrut(20));
  JButton save=GUIStyle.primaryButton("Change password");save.addActionListener(e->{
   String oldValue=new String(current.getPassword()),newValue=new String(next.getPassword());
   if(!newValue.equals(new String(confirm.getPassword()))){JOptionPane.showMessageDialog(this,"New passwords do not match.");return;}
   runTask(()->{BackendApiClient.changePassword(oldValue,newValue);return "Password changed.";},()->{
    current.setText("");next.setText("");confirm.setText("");JOptionPane.showMessageDialog(this,"Password changed. Sign in with your new password.");dispose();new LoginGUI().setGUI();
   });
  });p.add(save);return p;
 }
 private JPanel accounts() {
  JPanel p=card();JTextField email=new JTextField(),displayName=new JTextField();
  JPasswordField password=new JPasswordField(),confirm=new JPasswordField();
  field(p,"Admin email",email);field(p,"Admin full name",displayName);
  field(p,"Initial password",password);field(p,"Confirm password",confirm);
  p.add(new JLabel("This account will have administrator access to the system."));p.add(Box.createVerticalStrut(12));
  p.add(new JLabel("Use 10+ characters with uppercase, lowercase and a number."));p.add(Box.createVerticalStrut(20));
  JButton create=GUIStyle.primaryButton("Create admin account");create.addActionListener(e->{
   String u=email.getText().trim(),n=displayName.getText().trim(),pw=new String(password.getPassword());
   if(!pw.equals(new String(confirm.getPassword()))){JOptionPane.showMessageDialog(this,"Passwords do not match.");return;}
   runTask(()->"Admin account created: "+BackendApiClient.createAccount("ADMIN","0",u,n,pw),()->{
    password.setText("");confirm.setText("");email.setText("");displayName.setText("");
   });
  });p.add(create);return p;
 }
 @FunctionalInterface private interface Task {String run() throws Exception;}
 private void runTask(Task task,Runnable success) {
  java.util.Map<Component,Boolean> enabled=new java.util.IdentityHashMap<>();rememberEnabled(root,enabled);
  enabled.keySet().forEach(c->c.setEnabled(false));status.setText("Saving…");
  new SwingWorker<String,Void>() {
   protected String doInBackground() throws Exception {return task.run();}
   protected void done() {
    enabled.forEach(Component::setEnabled);
    try {status.setText(get());if(success!=null)success.run();}
    catch(Exception ex){Throwable cause=ex.getCause()==null?ex:ex.getCause();status.setText("Changes were not saved.");JOptionPane.showMessageDialog(AdminSettingsGUI.this,cause.getMessage(),"Unable to save",JOptionPane.ERROR_MESSAGE);}
   }
  }.execute();
 }
 private void rememberEnabled(Container parent,java.util.Map<Component,Boolean> map){for(Component c:parent.getComponents()){map.put(c,c.isEnabled());if(c instanceof Container child)rememberEnabled(child,map);}}
}
