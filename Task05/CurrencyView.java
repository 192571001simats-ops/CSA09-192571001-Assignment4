import java.awt.*;
import java.awt.event.*;
import java.math.*;
import java.util.*;
import javax.swing.*;

public class CurrencyView extends JFrame {
  final JTextField amount = new JTextField(10);
  final JComboBox<String> from = new JComboBox<>(new String[] {"INR", "USD", "EUR"}),
      to = new JComboBox<>(new String[] {"USD", "EUR", "INR"});
  final JButton button = new JButton("Convert");
  final JLabel result = new JLabel("Ready");
  final boolean dialogs;

  CurrencyView(String title, boolean dialogs) {
    super(title);
    this.dialogs = dialogs;
    setLayout(new BorderLayout(12, 12));
    JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 15));
    form.add(new JLabel("Amount"));
    form.add(amount);
    form.add(from);
    form.add(new JLabel("to"));
    form.add(to);
    form.add(button);
    result.setBorder(BorderFactory.createEmptyBorder(8, 16, 16, 16));
    add(form, BorderLayout.NORTH);
    add(result, BorderLayout.CENTER);
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            dispose();
          }
        });
    pack();
    setMinimumSize(new Dimension(620, 130));
    setSize(620, 130);
    setLocation(30, 30);
  }

  void convert() {
    String text;
    try {
      BigDecimal value = new BigDecimal(amount.getText().trim());
      if (value.signum() < 0) throw new NumberFormatException();
      Map<String, BigDecimal> perInr =
          Map.of(
              "INR",
              BigDecimal.ONE,
              "USD",
              new BigDecimal("0.012"),
              "EUR",
              new BigDecimal("0.011"));
      BigDecimal converted =
          value
              .multiply(perInr.get((String) to.getSelectedItem()))
              .divide(perInr.get((String) from.getSelectedItem()), 2, RoundingMode.HALF_UP);
      text = converted + " " + to.getSelectedItem();
    } catch (NumberFormatException | ArithmeticException e) {
      text = "Invalid amount";
    }
    result.setText(text);
    if (dialogs)
      JOptionPane.showMessageDialog(
          this, text, "Conversion result", JOptionPane.INFORMATION_MESSAGE);
  }
}
