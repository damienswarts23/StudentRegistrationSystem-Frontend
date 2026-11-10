package za.ac.mycput.studentregistrationsystem.frontend.view;
import java.awt.*;import javax.swing.*;
/** Shared profile sizing and placement for administrator, lecturer and student dashboards. */
final class DashboardComponents {
 private DashboardComponents(){}
 static AdminPhotoPanel portrait(){
  AdminPhotoPanel photo=new AdminPhotoPanel();
  int available=GraphicsEnvironment.isHeadless()?800:GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
  int height=Math.min(140,Math.max(88,available-560));
  photo.setPreferredSize(new Dimension((int)(height*.86),height));return photo;
 }
 static JPanel profile(Runnable openSettings){
  JPanel panel=new JPanel(new GridBagLayout());panel.setOpaque(false);
  JButton settings=GUIStyle.secondaryButton("");settings.setIcon(ActionIcons.icon("settings",38,GUIStyle.NAVY));settings.setPreferredSize(new Dimension(72,72));
  settings.setToolTipText("Account settings");settings.getAccessibleContext().setAccessibleName("Account settings");settings.addActionListener(e->openSettings.run());
  GridBagConstraints c=new GridBagConstraints();c.anchor=GridBagConstraints.CENTER;c.insets=new Insets(0,0,0,24);panel.add(settings,c);
  c.gridx=1;c.insets=new Insets(0,0,0,0);panel.add(portrait(),c);return panel;
 }
}
