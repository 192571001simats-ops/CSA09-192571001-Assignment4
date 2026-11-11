public class AnonymousConverter extends CurrencyView {
  public AnonymousConverter() {
    super("Anonymous listener converter", true);
    java.awt.event.ActionListener handler =
        new java.awt.event.ActionListener() {
          @Override
          public void actionPerformed(java.awt.event.ActionEvent e) {
            convert();
          }
        };
    button.addActionListener(handler);
    amount.addActionListener(handler);
  }
}
