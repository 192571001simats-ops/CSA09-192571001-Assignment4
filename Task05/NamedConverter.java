public class NamedConverter extends CurrencyView {
  public NamedConverter() {
    super("Named inner listener converter", true);
    Handler handler = new Handler();
    button.addActionListener(handler);
    amount.addActionListener(handler);
  }

  private class Handler implements java.awt.event.ActionListener {
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
      convert();
    }
  }
}
