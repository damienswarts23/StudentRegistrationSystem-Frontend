package za.ac.mycput.studentregistrationsystem.frontend.view;
import java.awt.*;import java.awt.image.BufferedImage;import java.util.*;import javax.swing.*;import javax.imageio.ImageIO;
/** Real licensed photography, bundled for offline use. */
public final class CampusPhotoPanel extends JPanel {
 private static final Map<String,BufferedImage> images=new HashMap<>();
 private final BufferedImage photo;private final boolean shaded;private BufferedImage display;private int displayW,displayH;
 public CampusPhotoPanel(String name,boolean shaded){this.shaded=shaded;photo=images.computeIfAbsent(name,key->{try{return ImageIO.read(Objects.requireNonNull(getClass().getResource("/photos/"+key+".jpg")));}catch(Exception ex){throw new IllegalStateException("Missing campus photograph: "+key,ex);}});setLayout(new BorderLayout());setBackground(GUIStyle.NAVY);getAccessibleContext().setAccessibleName("Students studying in a library");}
 protected void paintComponent(Graphics graphics){super.paintComponent(graphics);Graphics2D g=(Graphics2D)graphics.create();g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);double scale=Math.max(getWidth()/(double)photo.getWidth(),getHeight()/(double)photo.getHeight());int w=(int)Math.ceil(photo.getWidth()*scale),h=(int)Math.ceil(photo.getHeight()*scale);int pixelW=Math.max(1,(int)Math.ceil(getWidth()*Math.abs(g.getTransform().getScaleX()))),pixelH=Math.max(1,(int)Math.ceil(getHeight()*Math.abs(g.getTransform().getScaleY())));
 if(display==null||displayW!=pixelW||displayH!=pixelH){displayW=pixelW;displayH=pixelH;display=new BufferedImage(pixelW,pixelH,BufferedImage.TYPE_INT_RGB);Graphics2D cached=display.createGraphics();cached.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);cached.scale(pixelW/(double)Math.max(1,getWidth()),pixelH/(double)Math.max(1,getHeight()));cached.drawImage(photo,(getWidth()-w)/2,(getHeight()-h)/2,w,h,null);cached.dispose();}
 g.drawImage(display,0,0,getWidth(),getHeight(),null);
 if(shaded){g.setPaint(new GradientPaint(0,0,new Color(15,23,42,165),0,getHeight()/2f,new Color(15,23,42,15)));g.fillRect(0,0,getWidth(),getHeight()/2);g.setPaint(new GradientPaint(0,getHeight()/2f,new Color(15,23,42,15),0,getHeight(),new Color(15,23,42,235)));g.fillRect(0,getHeight()/2,getWidth(),getHeight());}g.dispose();}
}
