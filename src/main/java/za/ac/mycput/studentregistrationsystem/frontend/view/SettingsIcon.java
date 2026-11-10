package za.ac.mycput.studentregistrationsystem.frontend.view;
import java.awt.*;import javax.swing.*;
public class SettingsIcon implements Icon {
 private final Icon icon=ActionIcons.icon("settings",28,GUIStyle.NAVY);
 public int getIconWidth(){return 28;}public int getIconHeight(){return 28;}
 public void paintIcon(Component c,Graphics g,int x,int y){icon.paintIcon(c,g,x,y);}
}
