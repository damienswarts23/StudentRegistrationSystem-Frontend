package za.ac.mycput.studentregistrationsystem.frontend.view;
import javax.swing.*;
import java.util.*;
/** Retain the old page until its replacement is ready, avoiding desktop flashes. */
public class AppFrame extends JFrame {
 private static final Set<AppFrame> pending=new HashSet<>();
 private TimerCleanup cleanup;
 @Override public void dispose(){
  if(!isShowing()){super.dispose();return;}
  if(cleanup!=null)return;
  PageMotion.capture(this);setEnabled(false);pending.add(this);cleanup=new TimerCleanup(this);cleanup.timer.start();
 }
 private static class TimerCleanup {
  final javax.swing.Timer timer;
  TimerCleanup(AppFrame frame){timer=new javax.swing.Timer(5000,e->frame.finishDispose());timer.setRepeats(false);}
 }
 private void finishDispose(){if(cleanup!=null)cleanup.timer.stop();pending.remove(this);super.dispose();}
 static void replacementReady(AppFrame next){for(AppFrame frame:new ArrayList<>(pending))if(frame!=next)frame.finishDispose();}
}
