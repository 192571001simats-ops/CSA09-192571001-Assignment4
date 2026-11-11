import java.sql.*;
import java.util.*;

public class RegistrationService {
  final RegistrationDao dao = new RegistrationDao();
  static final Set<String> EVENTS = Set.of("Coding", "Music", "Quiz");

  long register(String reg, String name, String event) throws SQLException {
    if (!reg.matches("[0-9]{9}")
        || name.isBlank()
        || name.length() > 100
        || !EVENTS.contains(event))
      throw new IllegalArgumentException("Enter nine digit register number, name and valid event");
    try {
      return dao.insert(reg, name.trim(), event);
    } catch (SQLIntegrityConstraintViolationException e) {
      throw new IllegalArgumentException("Already registered for this event", e);
    }
  }
}
