import javax.swing.*;

public class AppointmentValidation {
  public static void main(String[] args) throws Exception {
    SwingUtilities.invokeAndWait(
        () -> {
          AppointmentDesk ui = new AppointmentDesk();
          ui.setVisible(true);
          ui.department.setSelectedItem("Neurology");
          Bench.require(
              ui.doctor.getItemCount() == 1 && ui.doctor.getSelectedItem().equals("Dr Meena"),
              "department refresh removes stale doctors");
          ui.patient.setText("P1001");
          ui.book.doClick();
          Bench.require(ui.status.getText().startsWith("Confirmed"), "normal appointment");
          GuiEvidence.capture(ui, "appointment-confirmed.png");
          ui.book.doClick();
          Bench.require(ui.status.getText().contains("already booked"), "doctor time conflict");
          ui.patient.setText("bad");
          ui.book.doClick();
          Bench.require(ui.status.getText().contains("four digits"), "invalid patient feedback");
          GuiEvidence.capture(ui, "appointment-invalid.png");
          GuiEvidence.finish();
        });
  }
}
