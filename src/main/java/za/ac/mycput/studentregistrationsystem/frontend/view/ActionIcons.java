package za.ac.mycput.studentregistrationsystem.frontend.view;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
/** Bundled Lucide icons. Original SVGs and license accompany the application. */
public final class ActionIcons {
 private static final Map<String,BufferedImage> cache=new HashMap<>();
 private ActionIcons(){}
 public static String forAction(String text){
  String s=text.toLowerCase(Locale.ROOT);
  if(s.matches(".*(delete|remove|deregister|unassign).*"))return "trash-2";
  if(s.matches(".*(edit|reset|change).*"))return "pencil";
  if(s.contains("logout"))return "log-out";
  if(s.matches(".*(login|sign in).*"))return "log-in";
  if(s.matches(".*(back|cancel|home|close).*"))return "arrow-left";
  if(s.matches(".*(save|done|confirm).*"))return "save";
  if(s.contains("photo"))return "camera";
  if(s.contains("track")||s.contains("search"))return "search";
  if(s.contains("department"))return "building-2";
  if(s.matches(".*(add|create|enable).*"))return s.matches(".*(lecturer|student|admin|account).*")?"user-plus":"plus";
  if(s.matches(".*(student|lecturer).*"))return "users";
  if(s.matches(".*(class|course).*"))return "book-open";
  if(s.contains("apply"))return "file-plus";
  if(s.contains("setting"))return "settings";
  return "arrow-right";
 }
 public static Icon icon(String name,int size,Color color){return new Icon(){
  public int getIconWidth(){return size;}public int getIconHeight(){return size;}
  public void paintIcon(Component c,Graphics graphics,int x,int y){
   Graphics2D g=(Graphics2D)graphics.create();g.translate(x,y);g.scale(size/24.0,size/24.0);
   g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
   g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE);
   g.setColor(color);g.setStroke(new BasicStroke(1.8f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));LucidePaths.draw(g,name);g.dispose();
  }
 };}
}
