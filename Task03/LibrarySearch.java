import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class LibrarySearch extends JFrame {
  final JTextField query = new JTextField(20);
  final JComboBox<String> category = new JComboBox<>(new String[] {"All", "Computing", "Biology"});
  final JButton search = new JButton("Search"),
      prev = new JButton("Prev"),
      next = new JButton("Next"),
      reserve = new JButton("Reserve");
  final JLabel status = new JLabel("Ready");
  final JTable results =
      new JTable(
          new DefaultTableModel(
              new Object[][] {
                {"B001", "Java Fundamentals", "Available"}, {"B002", "Cell Biology", "Available"}
              },
              new Object[] {"Book ID", "Title", "Status"}));
  final JPanel top = new JPanel(new BorderLayout(8, 8)),
      searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT)),
      bottom = new JPanel(new BorderLayout());
  final JScrollPane center = new JScrollPane(results);

  public LibrarySearch() {
    super("Campus Library Search");
    top.add(new JLabel("Campus Library", SwingConstants.CENTER), BorderLayout.NORTH);
    searchRow.add(query);
    searchRow.add(category);
    searchRow.add(search);
    top.add(searchRow, BorderLayout.SOUTH);
    JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
    buttons.add(prev);
    buttons.add(next);
    buttons.add(reserve);
    bottom.add(buttons, BorderLayout.NORTH);
    bottom.add(status, BorderLayout.SOUTH);
    setLayout(new BorderLayout(10, 10));
    add(top, BorderLayout.NORTH);
    add(center, BorderLayout.CENTER);
    add(bottom, BorderLayout.SOUTH);
    search.addActionListener(
        e ->
            status.setText(
                "Search: " + query.getText() + " category " + category.getSelectedItem()));
    query.addActionListener(e -> search.doClick());
    prev.addActionListener(e -> status.setText("First page"));
    next.addActionListener(e -> status.setText("Last page"));
    reserve.addActionListener(
        e ->
            status.setText(
                results.getSelectedRow() < 0 ? "Select a book" : "Reservation requested"));
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setSize(900, 600);
    setLocation(20, 20);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new LibrarySearch().setVisible(true));
  }
}
