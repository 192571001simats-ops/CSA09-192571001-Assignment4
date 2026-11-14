import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class AppointmentDesk extends JFrame {
  final JTextField patient = new JTextField(12);
  final JComboBox<String> department = new JComboBox<>(new String[] {"Cardiology", "Neurology"}),
      doctor = new JComboBox<>(),
      slot = new JComboBox<>(new String[] {"09:00", "09:30", "10:00"});
  final JButton book = new JButton("Book appointment");
  final JLabel status = new JLabel("Enter patient and appointment details");
  final Map<String, List<String>> doctors =
      Map.of("Cardiology", List.of("Dr Arun", "Dr Divya"), "Neurology", List.of("Dr Meena"));
  final Set<String> bookings = new HashSet<>();
  boolean dirty;

  public AppointmentDesk() {
    super("Hospital Outpatient Appointment Desk");
    JPanel form = new JPanel(new GridLayout(5, 2, 12, 12));
    form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    form.add(new JLabel("Patient ID"));
    form.add(patient);
    form.add(new JLabel("Department"));
    form.add(department);
    form.add(new JLabel("Doctor"));
    form.add(doctor);
    form.add(new JLabel("Time slot"));
    form.add(slot);
    form.add(book);
    form.add(status);
    setContentPane(form);
    department.addActionListener(
        e -> {
          doctor.removeAllItems();
          for (String name : doctors.get((String) department.getSelectedItem()))
            doctor.addItem(name);
          dirty = true;
        });
    patient
        .getDocument()
        .addDocumentListener(
            new javax.swing.event.DocumentListener() {
              public void insertUpdate(javax.swing.event.DocumentEvent e) {
                dirty = true;
              }

              public void removeUpdate(javax.swing.event.DocumentEvent e) {
                dirty = true;
              }

              public void changedUpdate(javax.swing.event.DocumentEvent e) {
                dirty = true;
              }
            });
    book.addActionListener(
        e -> {
          String id = patient.getText().trim();
          if (!id.matches("P[0-9]{4}")) {
            status.setText("Patient ID must be P and four digits");
            return;
          }
          String key = doctor.getSelectedItem() + "|" + slot.getSelectedItem();
          if (!bookings.add(key)) {
            status.setText("Doctor slot already booked");
            return;
          }
          status.setText("Confirmed " + id + " " + slot.getSelectedItem());
          dirty = false;
        });
    addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            if (!dirty
                || JOptionPane.showConfirmDialog(
                        AppointmentDesk.this,
                        "Discard booking in progress?",
                        "Confirm close",
                        JOptionPane.YES_NO_OPTION)
                    == JOptionPane.YES_OPTION) dispose();
          }
        });
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    department.setSelectedIndex(0);
    dirty = false;
    pack();
    setLocation(30, 30);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new AppointmentDesk().setVisible(true));
  }
}
