public class FrameConverter extends CurrencyView implements java.awt.event.ActionListener {
  public FrameConverter() {
    super("Frame listener converter", true);
    button.addActionListener(this);
    amount.addActionListener(this);
  }

  @Override
  public void actionPerformed(java.awt.event.ActionEvent e) {
    convert();
  }
}
