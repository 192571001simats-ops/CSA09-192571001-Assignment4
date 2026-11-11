import javax.swing.*;

public class FeeValidation {
  public static void main(String[] args) throws Exception {
    Db.init();
    final FeeWindow[] box = new FeeWindow[1];
    SwingUtilities.invokeAndWait(
        () -> {
          box[0] = new FeeWindow();
          box[0].setVisible(true);
          box[0].regNo.setText("192571001");
          box[0].amount.setText("1250.00");
          box[0].pay.doClick();
        });
    FeeWindow ui = box[0];
    GuiEvidence.await(() -> !ui.busy);
    SwingUtilities.invokeAndWait(
        () -> {
          Bench.require(
              ui.status.getText().startsWith("Paid INR 1250.00"),
              "fee payment reached service and JDBC off EDT");
          GuiEvidence.capture(ui, "fee-valid.png");
          ui.receipt.doClick();
          Bench.require(
              ui.status.getText().startsWith("Receipt"), "Receipt has independent handler");
          ui.amount.setText("abc");
          ui.pay.doClick();
        });
    GuiEvidence.await(() -> !ui.busy);
    SwingUtilities.invokeAndWait(
        () -> {
          Bench.require(ui.status.getText().contains("valid amount"), "invalid fee shown to user");
          GuiEvidence.capture(ui, "fee-invalid.png");
          ui.clear.doClick();
          Bench.require(ui.amount.getText().isEmpty(), "Clear does not make payment");
          GuiEvidence.finish();
        });
  }
}
