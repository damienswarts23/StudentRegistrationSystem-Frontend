package za.ac.mycput.studentregistrationsystem.frontend.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Window;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * Small Swing-only date picker. No external libraries are required.
 * The selected value is returned in yyyy-MM-dd format so that it maps
 * directly to the backend LocalDate field.
 */
public class DatePickerField extends JPanel {
    private final JTextField dateTxtFld;
    private LocalDate selectedDate;

    public DatePickerField() {
        setLayout(new BorderLayout(8, 0));
        setOpaque(false);

        dateTxtFld = new JTextField();
        dateTxtFld.setEditable(false);
        dateTxtFld.setToolTipText("Choose a date");
        GUIStyle.styleField(dateTxtFld);

        JButton calendarBtn = GUIStyle.secondaryButton("Calendar");
        calendarBtn.addActionListener(e -> openCalendar());

        add(dateTxtFld, BorderLayout.CENTER);
        add(calendarBtn, BorderLayout.EAST);
    }

    public String getDateText() {
        return selectedDate == null ? "" : selectedDate.toString();
    }

    public LocalDate getDate() {
        return selectedDate;
    }

    public void setDate(LocalDate date) {
        selectedDate = date;
        dateTxtFld.setText(date == null ? "" : date.toString());
    }

    private void openCalendar() {
        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog;
        if (owner instanceof Frame) {
            dialog = new JDialog((Frame) owner, "Choose Date of Birth", true);
        } else if (owner instanceof Dialog) {
            dialog = new JDialog((Dialog) owner, "Choose Date of Birth", true);
        } else {
            dialog = new JDialog((Frame) null, "Choose Date of Birth", true);
        }

        LocalDate startingDate = selectedDate == null
                ? LocalDate.now().minusYears(18)
                : selectedDate;

        JComboBox<Month> monthCmb = new JComboBox<>(Month.values());
        monthCmb.setSelectedItem(startingDate.getMonth());
        JSpinner yearSpn = new JSpinner(new SpinnerNumberModel(
                startingDate.getYear(), 1900, LocalDate.now().getYear(), 1));

        JPanel navigationPnl = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        navigationPnl.add(new JLabel("Month:"));
        navigationPnl.add(monthCmb);
        navigationPnl.add(new JLabel("Year:"));
        navigationPnl.add(yearSpn);

        JPanel daysPnl = new JPanel();
        daysPnl.setBorder(BorderFactory.createEmptyBorder(8, 10, 10, 10));

        Runnable refreshDays = () -> buildDays(dialog, daysPnl,
                (Month) monthCmb.getSelectedItem(), (Integer) yearSpn.getValue());
        monthCmb.addActionListener(e -> refreshDays.run());
        yearSpn.addChangeListener(e -> refreshDays.run());

        dialog.setLayout(new BorderLayout());
        dialog.add(navigationPnl, BorderLayout.NORTH);
        dialog.add(daysPnl, BorderLayout.CENTER);
        refreshDays.run();

        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo((Component) owner);
        dialog.setVisible(true);
    }

    private void buildDays(JDialog dialog, JPanel daysPnl, Month month, int year) {
        daysPnl.removeAll();
        daysPnl.setLayout(new GridLayout(0, 7, 5, 5));

        String[] headings = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (String heading : headings) {
            JLabel label = new JLabel(heading, SwingConstants.CENTER);
            daysPnl.add(label);
        }

        YearMonth yearMonth = YearMonth.of(year, month);
        int firstDayColumn = yearMonth.atDay(1).getDayOfWeek().getValue() - 1;
        for (int i = 0; i < firstDayColumn; i++) {
            daysPnl.add(new JLabel());
        }

        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            final int chosenDay = day;
            JButton dayBtn = new JButton(String.valueOf(day));
            dayBtn.addActionListener(e -> {
                setDate(LocalDate.of(year, month, chosenDay));
                dialog.dispose();
            });
            daysPnl.add(dayBtn);
        }

        daysPnl.revalidate();
        daysPnl.repaint();
        dialog.pack();
    }
}
