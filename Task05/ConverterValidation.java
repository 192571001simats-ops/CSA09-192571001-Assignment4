import javax.swing.*;

public class ConverterValidation {
  public static void main(String[] args) throws Exception {
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
