public class LambdaConverter extends CurrencyView {
  public LambdaConverter() {
    super("Lambda listener converter", true);
    java.awt.event.ActionListener handler = e -> convert();
    button.addActionListener(handler);
    amount.addActionListener(handler);
  }
}
