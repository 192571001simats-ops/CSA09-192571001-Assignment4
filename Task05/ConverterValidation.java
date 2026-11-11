import javax.swing.*;

public class ConverterValidation {
  public static void main(String[] args) throws Exception {
    SwingUtilities.invokeAndWait(() -> {
      try {
        Converter supplied = new Converter();
        supplied.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        supplied.setLocation(30, 30);
        supplied.setVisible(true);
        var amountField = Converter.class.getDeclaredField("amount");
        var resultField = Converter.class.getDeclaredField("result");
        amountField.setAccessible(true);
        resultField.setAccessible(true);
        JTextField amount = (JTextField) amountField.get(supplied);
        JLabel result = (JLabel) resultField.get(supplied);
        amount.setText("1000");
        amount.postActionEvent();
        Bench.require(result.getText().equals("12.00"), "supplied Converter valid conversion");
        GuiEvidence.capture(supplied, "supplied-converter-valid.png");
        amount.setText("bad");
        amount.postActionEvent();
        Bench.require(result.getText().equals("Invalid amount"), "supplied Converter invalid input");
        GuiEvidence.capture(supplied, "supplied-converter-invalid.png");
        supplied.dispose();
      } catch (ReflectiveOperationException e) { throw new RuntimeException(e); }
    });
    Class<?>[] types = {
      FrameConverter.class, NamedConverter.class, AnonymousConverter.class, LambdaConverter.class
    };
    for (Class<?> type : types) {
      SwingUtilities.invokeAndWait(
          () -> {
            try {
              CurrencyView ui = (CurrencyView) type.getConstructor().newInstance();
              ui.setVisible(true);
              ui.amount.setText("1000");
              GuiEvidence.dialogCapture(type.getSimpleName() + "-valid-dialog.png");
              ui.button.doClick();
              Bench.require(
                  ui.result.getText().equals("12.00 USD"),
                  type.getSimpleName() + " valid button conversion");
              ui.amount.setText("bad");
              GuiEvidence.dialogCapture(type.getSimpleName() + "-invalid-dialog.png");
              ui.amount.postActionEvent();
              Bench.require(
                  ui.result.getText().equals("Invalid amount"),
                  type.getSimpleName() + " Enter reuses same handler");
              GuiEvidence.capture(ui, type.getSimpleName() + "-invalid.png");
              ui.dispose();
            } catch (Exception e) {
              throw new RuntimeException(e);
            }
          });
    }
    System.out.println(
        "All conversion rates are fixed assignment examples; they are not live exchange rates");
  }
}
