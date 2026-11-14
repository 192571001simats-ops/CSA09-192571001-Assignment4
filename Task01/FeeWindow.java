import java.awt.*;
import java.awt.event.*;
import java.math.*;
import java.sql.*;
import javax.swing.*;

class FeeService {
  record Receipt(long id, String regNo, BigDecimal amount) {}

  Receipt pay(String reg, String input) throws SQLException {
    if (!reg.matches("[0-9]{9}"))
      throw new IllegalArgumentException("Enter a nine digit register number");
    BigDecimal amount;
    try {
      amount = new BigDecimal(input.trim());
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Enter a valid amount");
    }
    if (amount.signum() <= 0 || amount.scale() > 2)
      throw new IllegalArgumentException("Amount must be positive with at most two decimals");
    if (SwingUtilities.isEventDispatchThread())
      throw new IllegalStateException("Database operation on EDT");
    try (Connection c = Db.open();
        PreparedStatement p =
            c.prepareStatement(
                "INSERT INTO fees_assignment4(reg_no,amount) VALUES(?,?)",
                Statement.RETURN_GENERATED_KEYS)) {
      p.setString(1, reg);
      p.setBigDecimal(2, amount);
      p.executeUpdate();
      try (ResultSet keys = p.getGeneratedKeys()) {
        if (!keys.next()) throw new SQLException("Receipt ID missing");
        return new Receipt(keys.getLong(1), reg, amount);
      }
    }
  }
}

public class FeeWindow extends JFrame {
  final JTextField regNo = new JTextField(12), amount = new JTextField(10);
  final JLabel status = new JLabel("Enter fee details");
  final JButton pay = new JButton("Pay"),
      clear = new JButton("Clear"),
      receipt = new JButton("Receipt");
  final FeeService service = new FeeService();
  volatile boolean busy;
  FeeService.Receipt last;

  public FeeWindow() {
    super("Student Fee Payment");
    JPanel form = new JPanel(new GridLayout(2, 2, 10, 10));
    form.add(new JLabel("Register number"));
    form.add(regNo);
    form.add(new JLabel("Amount INR"));
    form.add(amount);
    JPanel buttons = new JPanel();
    buttons.add(pay);
    buttons.add(clear);
    buttons.add(receipt);
    JPanel root = new JPanel(new BorderLayout(12, 12));
    root.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    root.add(form, BorderLayout.NORTH);
    root.add(buttons, BorderLayout.CENTER);
    root.add(status, BorderLayout.SOUTH);
    setContentPane(root);
    pay.addActionListener(e -> submit());
    clear.addActionListener(
        e -> {
          if (!busy) {
            regNo.setText("");
            amount.setText("");
            status.setText("Cleared");
          }
        });
    receipt.addActionListener(
        e ->
            status.setText(
                last == null
                    ? "No payment receipt available"
                    : "Receipt " + last.id() + " paid INR " + last.amount()));
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            if (!busy
                || JOptionPane.showConfirmDialog(
                        FeeWindow.this,
                        "Payment in progress. Close?",
                        "Confirm close",
                        JOptionPane.YES_NO_OPTION)
                    == JOptionPane.YES_OPTION) dispose();
          }
        });
    pack();
    setLocation(40, 40);
  }

  void submit() {
    String reg = regNo.getText().trim(), value = amount.getText();
    if (busy) return;
    busy = true;
    pay.setEnabled(false);
    status.setText("Processing payment");
    new SwingWorker<FeeService.Receipt, Void>() {
      protected FeeService.Receipt doInBackground() throws Exception {
        return service.pay(reg, value);
      }

      protected void done() {
        try {
          last = get();
          status.setText("Paid INR " + last.amount() + " receipt " + last.id());
        } catch (Exception e) {
          Throwable cause = e.getCause() == null ? e : e.getCause();
          status.setText(cause.getMessage());
        } finally {
          busy = false;
          pay.setEnabled(true);
        }
      }
    }.execute();
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new FeeWindow().setVisible(true));
  }
}
