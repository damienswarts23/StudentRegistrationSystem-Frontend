package za.ac.mycput.studentregistrationsystem.frontend.view;
import java.awt.*;import java.awt.image.BufferedImage;import javax.swing.*;
/** Brief fade through the application surface, with no movement or overlapping page text. */
final class PageMotion extends JPanel {
 private static BufferedImage previous;
 private BufferedImage outgoing;private float progress=0;private javax.swing.Timer timer;
 static void capture(JFrame frame){
  if(Boolean.getBoolean("regilearn.reduceMotion"))return;
  Container page=frame.getContentPane();if(page.getWidth()<1||page.getHeight()<1)return;
  previous=new BufferedImage(page.getWidth(),page.getHeight(),BufferedImage.TYPE_INT_RGB);
  Graphics2D g=previous.createGraphics();page.printAll(g);g.dispose();
 }
 PageMotion(Container content){super(new BorderLayout());setBackground(GUIStyle.BACKGROUND);add(content,BorderLayout.CENTER);outgoing=previous;previous=null;}
 void start(){
  if(timer!=null)timer.stop();
  if(Boolean.getBoolean("regilearn.reduceMotion")){progress=1;outgoing=null;repaint();return;}
  progress=0;long started=System.nanoTime();
  timer=new javax.swing.Timer(16,e->{progress=Math.min(1,(System.nanoTime()-started)/320_000_000f);repaint();if(progress>=1){timer.stop();outgoing=null;}});timer.setCoalesce(true);timer.start();
 }
 @Override protected void paintChildren(Graphics graphics){
  Graphics2D g=(Graphics2D)graphics.create();
  g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
  if(outgoing!=null && progress<0.35f){
   float phase=progress/0.35f;float fade=phase*phase*(3-2*phase);
   g.setComposite(AlphaComposite.SrcOver.derive(1-fade));
   g.drawImage(outgoing,0,0,getWidth(),getHeight(),null);
  }else{
   float phase=outgoing==null?progress:Math.max(0,(progress-0.35f)/0.65f);
   float fade=phase*phase*(3-2*phase);
   g.setComposite(AlphaComposite.SrcOver.derive(Math.min(1,fade)));
   super.paintChildren(g);
  }
  g.dispose();
 }
 @Override public void removeNotify(){if(timer!=null)timer.stop();outgoing=null;super.removeNotify();}
}
