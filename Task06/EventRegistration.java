import java.awt.*;
import java.awt.event.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.*;
import javax.swing.*;
import javax.swing.table.*;

public class EventRegistration extends JFrame {
  final JTextField reg = new JTextField(12), name = new JTextField(16);
  final JComboBox<String> event = new JComboBox<>(new String[] {"Coding", "Music", "Quiz"}),
      filter = new JComboBox<>(new String[] {"All", "Coding", "Music", "Quiz"});
  final JButton register = new JButton("Register"),
      prev = new JButton("Prev"),
      next = new JButton("Next");
  final JLabel status = new JLabel("Ready");
  final DefaultTableModel model =
      new DefaultTableModel(new String[] {"ID", "Register number", "Name", "Event"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
          return false;
        }
      };
  final JTable table = new JTable(model);
  final RegistrationService service = new RegistrationService();
  final AtomicLong generation = new AtomicLong();
  volatile boolean busy, loading;
  int page;

  public EventRegistration() {
    super("Campus Event Registration");
    JMenuBar menu = new JMenuBar();
    JMenu file = new JMenu("File");
    JMenuItem export = new JMenuItem("Export filtered list"), close = new JMenuItem("Close");
    file.add(export);
    file.add(close);
    menu.add(file);
    setJMenuBar(menu);
    JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
    form.add(new JLabel("Register number"));
    form.add(reg);
    form.add(new JLabel("Name"));
    form.add(name);
    form.add(event);
    form.add(register);
    JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
    controls.add(new JLabel("Event filter"));
    controls.add(filter);
    controls.add(prev);
    controls.add(next);
    controls.add(status);
    setLayout(new BorderLayout(10, 10));
    add(form, BorderLayout.NORTH);
    add(new JScrollPane(table), BorderLayout.CENTER);
    add(controls, BorderLayout.SOUTH);
    register.addActionListener(e -> register());
    filter.addActionListener(
        e -> {
          page = 0;
          load();
        });
    prev.addActionListener(
        e -> {
          if (page > 0) {
            page--;
            load();
          }
        });
    next.addActionListener(
        e -> {
          page++;
          load();
        });
    export.addActionListener(
        e -> {
          JFileChooser chooser = new JFileChooser();
          if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            Path path = chooser.getSelectedFile().toPath();
            String selected = (String) filter.getSelectedItem();
            new SwingWorker<Void, Void>() {
              protected Void doInBackground() throws Exception {
                service.dao.export(selected, path);
                return null;
              }

              protected void done() {
                try {
                  get();
                  status.setText("Exported filtered list");
                } catch (Exception failure) {
                  status.setText("Export failed: " + failure.getMessage());
                }
              }
            }.execute();
          }
        });
    close.addActionListener(e -> dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            if ((busy || !reg.getText().isBlank() || !name.getText().isBlank())
                && JOptionPane.showConfirmDialog(
                        EventRegistration.this,
                        "Registration in progress. Close?",
                        "Confirm close",
                        JOptionPane.YES_NO_OPTION)
                    != JOptionPane.YES_OPTION) return;
            generation.incrementAndGet();
            dispose();
          }
        });
    setSize(1100, 600);
    setLocation(15, 15);
  }

  void register() {
    if (busy) return;
    String r = reg.getText().trim(),
        n = name.getText().trim(),
        v = (String) event.getSelectedItem();
    busy = true;
    register.setEnabled(false);
    status.setText("Saving registration");
    new SwingWorker<Long, Void>() {
      protected Long doInBackground() throws Exception {
        return service.register(r, n, v);
      }

      protected void done() {
        try {
          long id = get();
          status.setText("Registered ID " + id);
          load();
        } catch (Exception e) {
          status.setText((e.getCause() == null ? e : e.getCause()).getMessage());
        } finally {
          busy = false;
          register.setEnabled(true);
        }
      }
    }.execute();
  }

  void load() {
    long request = generation.incrementAndGet();
    String selected = (String) filter.getSelectedItem();
    int selectedPage = page;
    loading = true;
    new SwingWorker<List<RegistrationDao.Registration>, Void>() {
      protected List<RegistrationDao.Registration> doInBackground() throws Exception {
        return service.dao.list(selected, selectedPage, 25);
      }

      protected void done() {
        if (request != generation.get()) return;
        try {
          List<RegistrationDao.Registration> rows = get();
          model.setRowCount(0);
          for (var r : rows) model.addRow(new Object[] {r.id(), r.regNo(), r.name(), r.event()});
          prev.setEnabled(selectedPage > 0);
          next.setEnabled(rows.size() == 25);
        } catch (Exception e) {
          status.setText("Load failed: " + e.getMessage());
        } finally {
          loading = false;
        }
      }
    }.execute();
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(
        () -> {
          EventRegistration ui = new EventRegistration();
          ui.setVisible(true);
          ui.load();
        });
  }
}
