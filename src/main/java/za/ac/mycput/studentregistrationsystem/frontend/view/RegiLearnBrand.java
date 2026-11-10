package za.ac.mycput.studentregistrationsystem.frontend.view;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import javax.swing.*;

/** Scalable vector interpretation of the supplied RegiLearn cap and stacked pages. */
public final class RegiLearnBrand {
 private RegiLearnBrand() {}
 public static Icon icon(int size,boolean light){return new Icon(){
  public int getIconWidth(){return size;}public int getIconHeight(){return size;}
  public void paintIcon(Component c,Graphics graphics,int x,int y){Graphics2D g=(Graphics2D)graphics.create();g.translate(x,y);g.scale(size/100.0,size/100.0);mark(g,light);g.dispose();}
 };}
 private static void polygon(Graphics2D g,Color color,double... points){Path2D p=new Path2D.Double();p.moveTo(points[0],points[1]);for(int i=2;i<points.length;i+=2)p.lineTo(points[i],points[i+1]);p.closePath();g.setColor(color);g.fill(p);}
 private static void mark(Graphics2D g,boolean light){
  g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
  Color cap=light?Color.WHITE:GUIStyle.NAVY;
  polygon(g,GUIStyle.BLUE,14,56,50,74,84,56,87,66,50,86,11,67);
  polygon(g,light?new Color(165,180,252):new Color(99,102,241),14,72,50,90,85,72,88,82,50,100,11,83);
  polygon(g,cap,2,36,50,11,96,36,50,61);
  g.setColor(cap);g.setStroke(new BasicStroke(3.5f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));g.drawLine(94,37,94,62);g.fillRoundRect(90,60,8,17,6,6);
 }
 public static BufferedImage appIcon(int size){BufferedImage image=new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);Graphics2D g=image.createGraphics();g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);g.setColor(GUIStyle.NAVY);g.fillRoundRect(0,0,size,size,size/4,size/4);int pad=size/7;icon(size-pad*2,true).paintIcon(null,g,pad,pad-2);g.dispose();return image;}
 public static JLabel wordmark(int size,boolean light){JLabel label=new JLabel("RegiLearn",icon(size+12,light),SwingConstants.LEFT);label.setIconTextGap(14);label.setFont(new Font(GUIStyle.FONT_FAMILY,Font.BOLD,size));label.setForeground(light?Color.WHITE:GUIStyle.NAVY);return label;}
 public static JPanel welcomePanel(boolean dark){
  if(dark){
   JPanel panel=new CampusPhotoPanel("study",true);panel.setBorder(BorderFactory.createEmptyBorder(32,32,36,32));panel.add(wordmark(32,true),BorderLayout.NORTH);
   JLabel message=new JLabel("<html><div style='width:280px'>Your next chapter<br>starts here.<br><br><span style='font-size:14px;font-weight:normal'>Applications, registration and academic records.</span></div></html>");message.setFont(new Font(GUIStyle.FONT_FAMILY,Font.BOLD,30));message.setForeground(Color.WHITE);panel.add(message,BorderLayout.SOUTH);return panel;
  }
  JPanel panel=new JPanel(new GridLayout(1,2,24,0));panel.setBackground(GUIStyle.BACKGROUND);
  JPanel copy=new JPanel(new BorderLayout(0,16));copy.setOpaque(false);copy.setBorder(BorderFactory.createEmptyBorder(24,12,24,12));
  copy.add(wordmark(30,false),BorderLayout.NORTH);JLabel text=new JLabel("<html>Your next chapter<br>starts here.</html>");text.setFont(new Font(GUIStyle.FONT_FAMILY,Font.BOLD,30));text.setForeground(GUIStyle.NAVY);copy.add(text,BorderLayout.CENTER);
  JLabel subtitle=new JLabel("Apply, track your application or sign in.");subtitle.setForeground(GUIStyle.MUTED);copy.add(subtitle,BorderLayout.SOUTH);
  panel.add(copy);panel.add(new CampusPhotoPanel("library",false));return panel;
 }
}
