package za.ac.mycput.studentregistrationsystem.frontend.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JPasswordField;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;

public class GUIStyle {
    private static javax.swing.JFrame activeFrame;
    static void foregroundApplication(){
        javax.swing.JFrame frame=activeFrame;if(frame==null||!frame.isDisplayable())return;
        frame.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        // Windows may restore focus to the IDE when the ownerless splash closes.
        frame.setAlwaysOnTop(true);frame.toFront();frame.requestFocus();
        javax.swing.Timer releaseTop=new javax.swing.Timer(180,event->frame.setAlwaysOnTop(false));
        releaseTop.setRepeats(false);releaseTop.start();
        if(frame.getContentPane() instanceof PageMotion motion)motion.start();
    }

    public static final String FONT_FAMILY = chooseFont();
    private static String chooseFont(){
        java.util.Set<String> fonts=new java.util.HashSet<>(java.util.Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
        for(String name:new String[]{"Inter","Segoe UI","DejaVu Sans","SansSerif"})if(fonts.contains(name))return name;
        return "SansSerif";
    }
    public static final Color NAVY = new Color(30, 41, 59);
    public static final Color BLUE = new Color(79, 70, 229);
    public static final Color LIGHT_BLUE = new Color(238, 242, 255);
    public static final Color BACKGROUND = new Color(248, 250, 255);
    public static final Color WHITE = Color.WHITE;
    public static final Color TEXT = new Color(17, 24, 39);
    public static final Color MUTED = new Color(107, 114, 128);
    public static final Color BORDER = new Color(209, 213, 219);

    public static void installApplicationStyle() {
        Font regular = new Font(FONT_FAMILY, Font.PLAIN, 14);
        UIManager.put("Label.font", regular);
        UIManager.put("Button.font", new Font(FONT_FAMILY, Font.BOLD, 13));
        UIManager.put("TextField.font", regular);
        UIManager.put("PasswordField.font", regular);
        UIManager.put("ComboBox.font", regular);
        UIManager.put("Table.font", new Font(FONT_FAMILY, Font.PLAIN, 13));
        UIManager.put("TableHeader.font", new Font(FONT_FAMILY, Font.BOLD, 13));
        UIManager.put("OptionPane.messageFont", regular);
        UIManager.put("Button.focus", BLUE);
        UIManager.put("TabbedPane.selected", LIGHT_BLUE);
        UIManager.put("TabbedPane.contentAreaColor", WHITE);
        UIManager.put("Table.selectionBackground", LIGHT_BLUE);
        UIManager.put("ToolTip.background", NAVY);
        UIManager.put("ToolTip.foreground", WHITE);
        UIManager.put("TextField.caretForeground", TEXT);
        UIManager.put("PasswordField.caretForeground", TEXT);
    }

    public static JLabel heading(String text, int size) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(FONT_FAMILY, Font.BOLD, size));
        label.setForeground(TEXT);
        return label;
    }

    public static JLabel normalLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(FONT_FAMILY, Font.PLAIN, 14));
        label.setForeground(TEXT);
        return label;
    }

    public static final Color SUCCESS = new Color(34, 197, 94);
    public static final Color DANGER = new Color(185, 43, 56);
    public static final Color EDIT = new Color(146, 87, 14);

    public static JButton primaryButton(String text) { return button(text, false); }
    public static JButton secondaryButton(String text) { return button(text, true); }
    private static Color actionColor(String text) {
        String value=text.toLowerCase(java.util.Locale.ROOT);
        if(value.matches(".*(delete|remove|deregister|unassign).*"))return DANGER;
        return BLUE;
    }
    private static JButton button(String text, boolean secondary) {
        JButton button=new JButton(text);
        button.putClientProperty("srs.secondary",secondary);
        styleButton(button);
        return button;
    }
    private static void styleButton(JButton button) {
        String text=button.getText()==null?"":button.getText();
        boolean secondary=Boolean.TRUE.equals(button.getClientProperty("srs.secondary")) || text.toLowerCase(java.util.Locale.ROOT).matches("(cancel|back|home|logout|close|back to .*)");
        Color tone=actionColor(text);
        boolean caution= tone.equals(DANGER) || text.equalsIgnoreCase("logout");
        button.setFont(new Font(FONT_FAMILY,Font.BOLD,13));
        button.setForeground(secondary?(caution?DANGER:NAVY):WHITE);
        button.setBackground(secondary?WHITE:tone);
        button.setOpaque(false);button.setContentAreaFilled(false);button.setBorderPainted(false);
        button.setFocusPainted(false);button.setRolloverEnabled(true);
        button.setBorder(BorderFactory.createEmptyBorder(7,12,7,12));
        if(!text.isBlank()){button.setIcon(ActionIcons.icon(ActionIcons.forAction(text),17,button.getForeground()));button.setIconTextGap(8);}
        button.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        int width=Math.max(108,button.getFontMetrics(button.getFont()).stringWidth(text)+52);
        button.setPreferredSize(new Dimension(width,36));
        button.setMinimumSize(new Dimension(width,36));
        button.setMaximumSize(new Dimension(width,36));
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI(){
            @Override public void paint(java.awt.Graphics graphics,javax.swing.JComponent component){
                java.awt.Graphics2D g=(java.awt.Graphics2D)graphics.create();
                g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                Color fill=button.getBackground();
                if(!button.isEnabled())fill=new Color(222,228,236);
                else if(button.getModel().isPressed())fill=secondary?(caution?new Color(254,226,226):LIGHT_BLUE):fill.darker();
                else if(button.getModel().isRollover())fill=secondary?(caution?new Color(255,241,242):new Color(235,241,249)):fill.brighter();
                g.setColor(fill);g.fillRoundRect(1,1,button.getWidth()-3,button.getHeight()-3,12,12);
                if(secondary){g.setColor(caution?DANGER:BORDER);g.drawRoundRect(1,1,button.getWidth()-3,button.getHeight()-3,12,12);}
                if(button.hasFocus()){g.setColor(secondary?(caution?DANGER:BLUE):WHITE);g.setStroke(new java.awt.BasicStroke(2));g.drawRoundRect(4,4,button.getWidth()-9,button.getHeight()-9,8,8);}
                g.dispose();super.paint(graphics,component);
            }
        });
    }

    public static JPanel actionCard(String title,String description,JButton button) {
        JPanel card=whiteCard();card.setLayout(new java.awt.BorderLayout(16,0));
        JPanel copy=new JPanel();copy.setOpaque(false);copy.setLayout(new javax.swing.BoxLayout(copy,javax.swing.BoxLayout.Y_AXIS));
        JLabel heading=heading(title,15);heading.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel detail=normalLabel("<html>"+description+"</html>");detail.setForeground(MUTED);
        detail.setAlignmentX(Component.LEFT_ALIGNMENT);detail.setMaximumSize(new Dimension(600,52));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        copy.add(heading);copy.add(javax.swing.Box.createVerticalStrut(6));copy.add(detail);
        copy.add(javax.swing.Box.createVerticalStrut(12));copy.add(button);
        JLabel icon=new JLabel(ActionIcons.icon(ActionIcons.forAction(button.getText()),48,actionColor(button.getText())));
        icon.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        card.add(copy,java.awt.BorderLayout.CENTER);card.add(icon,java.awt.BorderLayout.EAST);
        card.putClientProperty("regilearn.actionCard",true);
        card.setPreferredSize(new Dimension(360,170));card.setMinimumSize(new Dimension(250,158));
        return card;
    }

    public static JPanel whiteCard() {
        JPanel panel = new JPanel();
        panel.setBackground(WHITE);
        Border line = BorderFactory.createLineBorder(new Color(229, 231, 235));
        Border space = BorderFactory.createEmptyBorder(16, 18, 16, 18);
        panel.setBorder(BorderFactory.createCompoundBorder(line, space));
        return panel;
    }

    public static void styleField(JTextField field) {
        field.setFont(new Font(FONT_FAMILY, Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(260, 38));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(6, 9, 6, 9)));
    }

    public static JScrollPane plainScrollPane(Component component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setBorder(null);
        scrollPane.setViewportBorder(null);
        scrollPane.setBackground(WHITE);
        scrollPane.getViewport().setBackground(WHITE);
        return scrollPane;
    }

    public static void showFrame(javax.swing.JFrame frame) {
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.setIconImages(java.util.List.of(RegiLearnBrand.appIcon(16),RegiLearnBrand.appIcon(32),RegiLearnBrand.appIcon(64),RegiLearnBrand.appIcon(128)));
        if(!frame.getTitle().contains("RegiLearn"))frame.setTitle("RegiLearn · "+frame.getTitle());
        polishComponents(frame.getContentPane());

        Rectangle usable = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        frame.setMinimumSize(new Dimension(960,600));
        frame.setBounds(usable);
        frame.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        Container original=frame.getContentPane();
        PageMotion motion=new PageMotion(original);frame.setContentPane(motion);
        activeFrame=frame;frame.setVisible(true);
        SwingUtilities.invokeLater(() -> {
            frame.validate();if(!RegiLearnSplash.isShowing())motion.start();
            if(frame instanceof AppFrame page)AppFrame.replacementReady(page);
        });
    }

    static void polishComponents(Component component) {
        if(component instanceof JLabel label){Font old=label.getFont();label.setFont(new Font(FONT_FAMILY,old.getStyle(),old.getSize()));}
        if(component instanceof JLabel label && "STUDENT REGISTRATION SYSTEM".equals(label.getText())){
            label.setText("RegiLearn");label.setIcon(RegiLearnBrand.icon(36,true));label.setIconTextGap(12);
            label.setFont(new Font(FONT_FAMILY,Font.BOLD,25));
        }
        if(component instanceof JPanel panel && panel.getLayout() instanceof java.awt.GridLayout grid) {
            boolean cards=panel.getComponentCount()>0;
            for(Component child:panel.getComponents())cards &= child instanceof javax.swing.JComponent jc && Boolean.TRUE.equals(jc.getClientProperty("regilearn.actionCard"));
            if(cards)panel.setLayout(new CardGrid(grid));
            boolean form=false;
            for(Component child:panel.getComponents())if(child instanceof JTextField || child instanceof JComboBox || child instanceof DatePickerField)form=true;
            if(grid.getColumns()==2 && panel.getComponentCount()>=10)panel.setLayout(new PairGrid(form));
            else if(form && !(grid instanceof FormGrid))panel.setLayout(new FormGrid(grid));
            if(!form && grid.getColumns()==1 && panel.getComponentCount()==2 && panel.getComponent(0) instanceof JLabel && panel.getComponent(1) instanceof JLabel){
                panel.setLayout(new javax.swing.BoxLayout(panel,javax.swing.BoxLayout.Y_AXIS));
            }
        }
        if(component instanceof JButton button && button.getParent() instanceof JPanel) {
            boolean icon=button.getIcon()!=null && (button.getText()==null || button.getText().isBlank());
            Dimension size=button.getPreferredSize();styleButton(button);
            if(icon){button.setPreferredSize(size);button.setMinimumSize(size);button.setMaximumSize(size);}
        }
        if(component instanceof javax.swing.JTabbedPane tabs){tabs.setFont(new Font(FONT_FAMILY,Font.BOLD,13));tabs.setBackground(LIGHT_BLUE);tabs.setForeground(TEXT);
            if(!Boolean.TRUE.equals(tabs.getClientProperty("regilearn.motion"))){
                tabs.putClientProperty("regilearn.motion",true);
                for(int i=0;i<tabs.getTabCount();i++){Component child=tabs.getComponentAt(i);if(child instanceof javax.swing.JComponent jc)jc.setBorder(BorderFactory.createEmptyBorder(12,0,0,0));}
            }
        }

        if (component instanceof JTextField || component instanceof JPasswordField) {
            javax.swing.JComponent field = (javax.swing.JComponent) component;
            field.setPreferredSize(new Dimension(Math.max(210, field.getPreferredSize().width), 38));
            field.setMaximumSize(new Dimension(Integer.MAX_VALUE,38));
            field.setFont(new Font(FONT_FAMILY,Font.PLAIN,14));
            field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER),
                    BorderFactory.createEmptyBorder(6, 9, 6, 9)));
        } else if (component instanceof JComboBox) {
            component.setPreferredSize(new Dimension(Math.max(210, component.getPreferredSize().width), 38));
        } else if (component instanceof JTable) {
            JTable table = (JTable) component;
            table.setRowHeight(34);
            table.setShowVerticalLines(false);
            table.setGridColor(new Color(229, 231, 235));
            table.setSelectionBackground(LIGHT_BLUE);
            table.setSelectionForeground(TEXT);
            table.getTableHeader().setPreferredSize(new Dimension(0, 38));
            table.getTableHeader().setBackground(new Color(249, 250, 251));
            table.getTableHeader().setForeground(TEXT);
        } else if (component instanceof JScrollPane) {
            JScrollPane pane = (JScrollPane) component;
            pane.getVerticalScrollBar().setUnitIncrement(16);

        }

        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                polishComponents(child);
            }
        }
    }
    /** GridLayout otherwise expands two input rows to fill an entire tall card. */
    static class FormGrid extends java.awt.GridLayout {
        FormGrid(java.awt.GridLayout original){super(original.getRows(),original.getColumns(),original.getHgap(),original.getVgap());}
        @Override public void layoutContainer(Container parent){
            synchronized(parent.getTreeLock()){
                int count=parent.getComponentCount();if(count==0)return;
                int rows=getRows(),cols=getColumns();
                if(rows>0)cols=(count+rows-1)/rows;else rows=(count+cols-1)/cols;
                java.awt.Insets in=parent.getInsets();
                int width=Math.max(1,(parent.getWidth()-in.left-in.right-(cols-1)*getHgap())/cols);
                int rowHeight=38;
                for(Component child:parent.getComponents())rowHeight=Math.max(rowHeight,Math.min(44,child.getPreferredSize().height));
                for(int i=0;i<count;i++){
                    Component child=parent.getComponent(i);
                    int h=child instanceof JLabel?rowHeight:Math.min(38,rowHeight);
                    child.setBounds(in.left+(i%cols)*(width+getHgap()),in.top+(i/cols)*(rowHeight+getVgap())+(rowHeight-h)/2,width,h);
                }
            }
        }
    }
    /** Action cards keep a human-sized height, even on large monitors. */
    static class CardGrid extends java.awt.GridLayout {
        CardGrid(java.awt.GridLayout g){super(g.getRows(),g.getColumns(),16,16);}
        @Override public void layoutContainer(Container c){super.layoutContainer(c);for(Component child:c.getComponents()){Rectangle b=child.getBounds();int row=(b.y-c.getInsets().top)/Math.max(1,b.height+getVgap());child.setBounds(b.x,c.getInsets().top+row*(Math.min(170,b.height)+getVgap()),b.width,Math.min(170,b.height));}}
    }
    /** Label/value pairs arranged as two columns of fields on wide pages. */
    static class PairGrid implements java.awt.LayoutManager {
        final boolean fields;PairGrid(boolean fields){this.fields=fields;}
        public void addLayoutComponent(String n,Component c){}public void removeLayoutComponent(Component c){}
        public Dimension minimumLayoutSize(Container c){return new Dimension(600,preferredLayoutSize(c).height);}
        public Dimension preferredLayoutSize(Container c){int pairs=(c.getComponentCount()+1)/2;int rows=(pairs+1)/2;java.awt.Insets in=c.getInsets();return new Dimension(820,rows*(fields?44:30)+in.top+in.bottom);}
        public void layoutContainer(Container c){
            int pairs=(c.getComponentCount()+1)/2;java.awt.Insets in=c.getInsets();int width=c.getWidth()-in.left-in.right;
            int columns=width>=720?2:1,rows=(pairs+columns-1)/columns;int gap=28,group=(width-gap*(columns-1))/columns;
            int step=Math.min(fields?44:30,Math.max(1,(c.getHeight()-in.top-in.bottom)/rows));int itemHeight=Math.max(1,Math.min(fields?36:26,step-4));int label=Math.min(145,(int)(group*.36));
            for(int pair=0;pair<pairs;pair++){int col=pair/rows,row=pair%rows,x=in.left+col*(group+gap),y=in.top+row*step;
                c.getComponent(pair*2).setBounds(x,y,label,itemHeight);
                if(pair*2+1<c.getComponentCount())c.getComponent(pair*2+1).setBounds(x+label+10,y,Math.max(1,group-label-10),itemHeight);
            }
        }
    }
}
