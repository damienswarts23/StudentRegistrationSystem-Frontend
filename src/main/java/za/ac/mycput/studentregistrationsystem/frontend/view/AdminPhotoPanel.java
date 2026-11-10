package za.ac.mycput.studentregistrationsystem.frontend.view;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;
import javax.swing.*;
import za.ac.mycput.studentregistrationsystem.frontend.service.BackendApiClient;
/** Shows only the current account's photo; a neutral silhouette until uploaded. */
public class AdminPhotoPanel extends JPanel {
 private BufferedImage photo;
 public AdminPhotoPanel() {
  setPreferredSize(new Dimension(72,72));
  setToolTipText("Profile photo — change in settings");
  getAccessibleContext().setAccessibleName("Current account profile photo");
  try {byte[] bytes=BackendApiClient.currentPhoto();if(bytes!=null)photo=ImageIO.read(new ByteArrayInputStream(bytes));}
  catch(Exception ignored) {photo=null;}
 }
 @Override protected void paintComponent(Graphics graphics) {
  super.paintComponent(graphics);Graphics2D g=(Graphics2D)graphics.create();
  g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
  int w=getWidth(),h=getHeight();g.setColor(new Color(229,234,241));g.fillRoundRect(0,0,w-1,h-1,10,10);
  if(photo!=null) {
   double scale=Math.min((w-4.0)/photo.getWidth(),(h-4.0)/photo.getHeight());
   int iw=(int)(photo.getWidth()*scale),ih=(int)(photo.getHeight()*scale);
   g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
   g.drawImage(photo,(w-iw)/2,(h-ih)/2,iw,ih,null);
  } else {
   g.setColor(new Color(130,145,164));ActionIcons.icon("user",40,GUIStyle.MUTED).paintIcon(this,g,(w-40)/2,(h-40)/2);
  }
  g.setColor(new Color(205,213,224));g.drawRoundRect(0,0,w-1,h-1,10,10);g.dispose();
 }
}
