package za.ac.mycput.studentregistrationsystem.frontend.view;
import javax.swing.*;
import java.awt.*;
/** Logo-only startup with a gentle pulse and an overlapping handoff to the home page. */
public final class RegiLearnSplash {
 private RegiLearnSplash(){}
 private static boolean showing;
 static boolean isShowing(){return showing;}
 public static void show(Runnable openApplication){
  showing=true;JWindow window=new JWindow();window.setName("RegiLearn startup");window.setIconImage(RegiLearnBrand.appIcon(64));
  boolean transparent=window.getGraphicsConfiguration().getDevice().isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSLUCENT);
  if(transparent)window.setBackground(new Color(0,0,0,0));
  LogoPanel panel=new LogoPanel();panel.setOpaque(!transparent);panel.setBackground(GUIStyle.BACKGROUND);
  window.setContentPane(panel);window.setSize(220,220);window.setLocationRelativeTo(null);window.setAlwaysOnTop(true);
  if(!transparent && window.getGraphicsConfiguration().getDevice().isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSPARENT))window.setShape(new java.awt.geom.Ellipse2D.Double(0,0,220,220));
  long start=System.nanoTime();boolean[] opened={false};long[] handoff={0};
  Runnable open=()->{if(!opened[0]){opened[0]=true;handoff[0]=System.nanoTime();window.setAlwaysOnTop(false);openApplication.run();}};
  javax.swing.Timer timer=new javax.swing.Timer(16,null);
  timer.addActionListener(e->{
   double elapsed=(System.nanoTime()-start)/1e9;
   if(elapsed>=3.2)open.run();
   panel.seconds=elapsed;panel.alpha=(float)Math.min(1,elapsed/.4);
   if(opened[0]){double out=(System.nanoTime()-handoff[0])/650_000_000.0;panel.alpha=(float)Math.max(0,1-out);if(out>=1){timer.stop();showing=false;window.setAlwaysOnTop(false);window.dispose();SwingUtilities.invokeLater(GUIStyle::foregroundApplication);return;}}
   panel.repaint();
  });
  panel.addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent e){open.run();}});
  panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"),"skip");
  panel.getActionMap().put("skip",new AbstractAction(){public void actionPerformed(java.awt.event.ActionEvent e){open.run();}});
  window.setVisible(true);timer.start();
 }
 static class LogoPanel extends JPanel {
  double seconds;float alpha;
  LogoPanel(){getAccessibleContext().setAccessibleName("RegiLearn is starting");setOpaque(false);}
  @Override protected void paintComponent(Graphics graphics){super.paintComponent(graphics);Graphics2D g=(Graphics2D)graphics.create();g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);g.setComposite(AlphaComposite.SrcOver.derive(alpha));
   boolean reduced=Boolean.getBoolean("regilearn.reduceMotion");double wave=reduced?0:Math.sin(seconds*Math.PI*1.6);double scale=1+.035*wave;
   g.translate(getWidth()/2.0,getHeight()/2.0+wave*5);g.scale(scale,scale);g.rotate(wave*.025);RegiLearnBrand.icon(150,false).paintIcon(this,g,-75,-75);g.dispose();
  }
 }
}
