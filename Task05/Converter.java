import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Converter extends JFrame {
  private final JTextField amount = new JTextField(10);
  private final JComboBox<String> target = new JComboBox<>(new String[] {"USD", "EUR"});
  private final JLabel result = new JLabel(" ");

  private static double rate(String currency) {
    return currency.equals("USD") ? 0.012 : 0.011;
  }

  public Converter() {
    super("Currency Converter");
    setLayout(new FlowLayout());
    JButton convertButton = new JButton("Convert");
    add(new JLabel("INR"));
    add(amount);
    add(target);
    add(convertButton);
    add(result);
    ActionListener listener = e -> convert();
    convertButton.addActionListener(listener);
    amount.addActionListener(listener);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    pack();
  }

  private void convert() {
    try {
      double value = Double.parseDouble(amount.getText());
      double converted = value * rate((String) target.getSelectedItem());
      if (!Double.isFinite(value) || !Double.isFinite(converted)) throw new NumberFormatException();
      result.setText(String.format(java.util.Locale.ROOT, "%.2f", converted));
    } catch (NumberFormatException e) {
      result.setText("Invalid amount");
    }
    pack();
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new Converter().setVisible(true));
  }
}
