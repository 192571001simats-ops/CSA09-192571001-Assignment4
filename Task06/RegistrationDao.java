import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;

public class RegistrationDao {
  record Registration(long id, String regNo, String name, String event) {}

  long insert(String reg, String name, String event) throws SQLException {
    try (Connection c = Db.open();
        PreparedStatement p =
            c.prepareStatement(
                "INSERT INTO registrations_assignment4(reg_no,name,event_name) VALUES(?,?,?)",
                Statement.RETURN_GENERATED_KEYS)) {
      p.setString(1, reg);
      p.setString(2, name);
      p.setString(3, event);
      p.executeUpdate();
      try (ResultSet r = p.getGeneratedKeys()) {
        r.next();
        return r.getLong(1);
      }
    }
  }

  List<Registration> list(String event, int page, int size) throws SQLException {
    if (page < 0 || size < 1 || size > 1000) throw new IllegalArgumentException("Invalid page");
    List<Registration> rows = new ArrayList<>();
    try (Connection c = Db.open();
        PreparedStatement p =
            c.prepareStatement(
                "SELECT id,reg_no,name,event_name FROM registrations_assignment4 WHERE (?='All' OR"
                    + " event_name=?) ORDER BY id LIMIT ? OFFSET ?")) {
      p.setString(1, event);
      p.setString(2, event);
      p.setInt(3, size);
      p.setInt(4, Math.multiplyExact(page, size));
      try (ResultSet r = p.executeQuery()) {
        while (r.next())
          rows.add(new Registration(r.getLong(1), r.getString(2), r.getString(3), r.getString(4)));
      }
    }
    return rows;
  }

  static String csv(String s) {
    if (!s.isEmpty() && "=+-@".indexOf(s.charAt(0)) >= 0) s = "'" + s;
    return "\"" + s.replace("\"", "\"\"") + "\"";
  }

  void export(String event, Path file) throws SQLException, IOException {
    Path absolute = file.toAbsolutePath(),
        temp = Files.createTempFile(absolute.getParent(), "registration-export-", ".tmp");
    boolean complete = false;
    try {
      try (BufferedWriter out = Files.newBufferedWriter(temp);
          Connection c = Db.open();
          PreparedStatement p =
              c.prepareStatement(
                  "SELECT reg_no,name,event_name FROM registrations_assignment4 WHERE (?='All' OR"
                      + " event_name=?) ORDER BY id")) {
        p.setString(1, event);
        p.setString(2, event);
        out.write("register_number,name,event\n");
        try (ResultSet r = p.executeQuery()) {
          while (r.next())
            out.write(
                csv(r.getString(1)) + "," + csv(r.getString(2)) + "," + csv(r.getString(3)) + "\n");
        }
      }
      Files.move(
          temp, absolute, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
      complete = true;
    } finally {
      if (!complete) Files.deleteIfExists(temp);
    }
  }
}
