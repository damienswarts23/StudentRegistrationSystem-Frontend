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
    public static final Color NAVY = new Color(31, 41, 55);
    public static final Color BLUE = new Color(37, 99, 235);
    public static final Color LIGHT_BLUE = new Color(219, 234, 254);
    public static final Color BACKGROUND = new Color(245, 247, 250);
    public static final Color WHITE = Color.WHITE;
    public static final Color TEXT = new Color(17, 24, 39);
    public static final Color MUTED = new Color(107, 114, 128);
    public static final Color BORDER = new Color(209, 213, 219);

    public static void installApplicationStyle() {
        Font regular = new Font("Arial", Font.PLAIN, 14);
        UIManager.put("Label.font", regular);
        UIManager.put("Button.font", new Font("Arial", Font.BOLD, 13));
        UIManager.put("TextField.font", regular);
        UIManager.put("PasswordField.font", regular);
        UIManager.put("ComboBox.font", regular);
        UIManager.put("Table.font", new Font("Arial", Font.PLAIN, 13));
        UIManager.put("TableHeader.font", new Font("Arial", Font.BOLD, 13));
        UIManager.put("OptionPane.messageFont", regular);
        UIManager.put("Button.focus", false);
        UIManager.put("TextField.caretForeground", TEXT);
        UIManager.put("PasswordField.caretForeground", TEXT);
    }

    public static JLabel heading(String text, int size) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, size));
        label.setForeground(TEXT);
        return label;
    }

    public static JLabel normalLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(TEXT);
        return label;
    }

    public static JButton primaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setForeground(WHITE);
        button.setBackground(BLUE);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setHorizontalAlignment(JButton.CENTER);
        button.setMargin(new java.awt.Insets(10, 18, 10, 18));
        int width = Math.max(148, text.length() * 8 + 38);
        button.setPreferredSize(new Dimension(width, 42));
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        return button;
    }

    public static JButton secondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 13));
        button.setForeground(TEXT);
        button.setBackground(WHITE);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createLineBorder(BORDER));
        int width = Math.max(105, text.length() * 8 + 30);
        button.setPreferredSize(new Dimension(width, 40));
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        return button;
    }

    public static JPanel whiteCard() {
        JPanel panel = new JPanel();
        panel.setBackground(WHITE);
        Border line = BorderFactory.createLineBorder(new Color(229, 231, 235));
        Border space = BorderFactory.createEmptyBorder(24, 24, 24, 24);
        panel.setBorder(BorderFactory.createCompoundBorder(line, space));
        return panel;
    }

    public static void styleField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 14));
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
        polishComponents(frame.getContentPane());

        Rectangle usable = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        int width = Math.min(1180, Math.max(920, usable.width - 80));
        int height = Math.min(720, Math.max(620, usable.height - 60));
        frame.setMinimumSize(new Dimension(Math.min(900, width), Math.min(600, height)));
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        SwingUtilities.invokeLater(() -> {
            frame.revalidate();
            frame.repaint();
        });
    }

    private static void polishComponents(Component component) {
        if (component instanceof JTextField || component instanceof JPasswordField) {
            javax.swing.JComponent field = (javax.swing.JComponent) component;
            field.setPreferredSize(new Dimension(Math.max(210, field.getPreferredSize().width), 38));
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
            if (pane.getBorder() == null) {
                pane.setBorder(BorderFactory.createLineBorder(BORDER));
            }
        }

        if (component instanceof Container && !(component instanceof JViewport)) {
            for (Component child : ((Container) component).getComponents()) {
                polishComponents(child);
            }
        }
    }
}
