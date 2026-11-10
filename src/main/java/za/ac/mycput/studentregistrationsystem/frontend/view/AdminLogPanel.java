package za.ac.mycput.studentregistrationsystem.frontend.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import za.ac.mycput.studentregistrationsystem.frontend.service.BackendApiClient;

/** Loads shared history without blocking Swing's event thread. */
public class AdminLogPanel extends JPanel {
    private final DefaultTableModel model=new DefaultTableModel(
        new String[]{"Date / time (SA)","Administrator","Admin email","Change","Record / action path"},0) {
        @Override public boolean isCellEditable(int row,int column){return false;}
    };
    private final JButton refresh=GUIStyle.secondaryButton("Refresh");
    private final JLabel status=GUIStyle.normalLabel(" ");
    private boolean loading;
    public AdminLogPanel() {
        super(new BorderLayout(12,12));setBackground(GUIStyle.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        JPanel top=new JPanel(new BorderLayout());top.setOpaque(false);
        top.add(GUIStyle.heading("RECENT ADMIN CHANGES",20),BorderLayout.WEST);
        JPanel actions=new JPanel(new FlowLayout(FlowLayout.RIGHT));actions.setOpaque(false);
        actions.add(refresh);top.add(actions,BorderLayout.EAST);add(top,BorderLayout.NORTH);
        JTable table=new JTable(model);table.setRowHeight(32);table.setAutoCreateRowSorter(true);
        table.getTableHeader().setReorderingAllowed(false);table.setFillsViewportHeight(true);
        add(new JScrollPane(table),BorderLayout.CENTER);add(status,BorderLayout.SOUTH);
        refresh.addActionListener(event->refresh());
        refresh();
    }
    public void refresh() {
        if(loading)return;loading=true;refresh.setEnabled(false);status.setText("Loading admin activity…");
        new SwingWorker<String,Void>() {
            @Override protected String doInBackground() throws Exception {return BackendApiClient.adminLog();}
            @Override protected void done() {
                loading=false;refresh.setEnabled(true);
                try {
                    String response=get();model.setRowCount(0);
                    var format=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Africa/Johannesburg"));
                    for(String line:response.split("\n")) {
                        if(line.isBlank())continue;
                        String[] row=line.split("\t",-1);
                        if(row.length!=5)throw new IllegalStateException("Unexpected activity response.");
                        row[0]=format.format(Instant.parse(row[0]));model.addRow(row);
                    }
                    status.setText(model.getRowCount()==0?"No admin changes recorded yet.":
                        "Latest "+model.getRowCount()+" changes (up to 200), newest first. Shared by all administrators.");
                } catch(Exception error) {
                    status.setText("Could not load admin activity. Click Refresh to retry.");
                }
            }
        }.execute();
    }
}
