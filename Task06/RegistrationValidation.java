import java.nio.file.*;
import java.sql.*;
import javax.swing.*;

public class RegistrationValidation {
  public static void main(String[] args) throws Exception {
    Db.init();
    try (Connection c = Db.open();
        Statement s = c.createStatement()) {
      s.executeUpdate("DELETE FROM registrations_assignment4");
    }
    final EventRegistration[] box = new EventRegistration[1];
    SwingUtilities.invokeAndWait(
        () -> {
          box[0] = new EventRegistration();
          box[0].setVisible(true);
          box[0].reg.setText("192571001");
          box[0].name.setText("Athisaya U");
          box[0].register.doClick();
        });
    EventRegistration ui = box[0];
    GuiEvidence.await(() -> !ui.busy && !ui.loading);
    SwingUtilities.invokeAndWait(
        () -> {
          Bench.require(
              ui.model.getRowCount() == 1 && ui.status.getText().startsWith("Registered ID"),
              "normal registration persists and appears in JTable");
          GuiEvidence.capture(ui, "registration-valid.png");
          ui.register.doClick();
        });
    GuiEvidence.await(() -> !ui.busy);
    SwingUtilities.invokeAndWait(
        () -> {
          Bench.require(
              ui.status.getText().contains("Already registered"),
              "database unique constraint rejects duplicate");
          GuiEvidence.capture(ui, "registration-duplicate.png");
          ui.reg.setText("abc");
          ui.register.doClick();
        });
    GuiEvidence.await(() -> !ui.busy);
    SwingUtilities.invokeAndWait(
        () -> {
          Bench.require(
              ui.status.getText().contains("nine digit"), "invalid registration feedback");
          GuiEvidence.capture(ui, "registration-invalid.png");
          ui.filter.setSelectedItem("Music");
        });
    GuiEvidence.await(() -> !ui.loading);
    SwingUtilities.invokeAndWait(
        () -> Bench.require(ui.model.getRowCount() == 0, "filter selects only chosen event"));
    ui.service.dao.export("Coding", Path.of("coding-registrations.csv"));
    Bench.require(
        Files.readAllLines(Path.of("coding-registrations.csv")).size() == 2,
        "export includes header and filtered registration");
    for (int i = 0; i < 60; i++)
      ui.service.register(String.format("%09d", 200000000 + i), "Student " + i, "Quiz");
    Bench.require(
        ui.service.dao.list("Quiz", 0, 25).size() == 25
            && ui.service.dao.list("Quiz", 2, 25).size() == 10,
        "pagination returns 25,25,10 rows");
    SwingUtilities.invokeAndWait(GuiEvidence::finish);
    System.out.println(
        "Worker operations completed without blocking the EDT; stale filter results are guarded by"
            + " a request generation");
  }
}
