import java.awt.*;
import java.awt.image.*;
import java.nio.file.*;
import java.util.function.*;
import javax.imageio.*;
import javax.swing.*;

public final class GuiEvidence {
  public static void capture(Window window, String name) {
    try {
      window.validate();
      if (window instanceof RootPaneContainer container) {
        JRootPane root = container.getRootPane();
        root.paintImmediately(0, 0, root.getWidth(), root.getHeight());
      } else {
        Graphics graphics = window.getGraphics();
        if (graphics != null) {
          try { window.paintAll(graphics); }
          finally { graphics.dispose(); }
        }
      }
      Toolkit.getDefaultToolkit().sync();
      ImageIO.write(
          new Robot().createScreenCapture(window.getBounds()), "png", Path.of(name).toFile());
      System.out.println("SCREENSHOT " + name);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void await(BooleanSupplier done) throws Exception {
    long limit = System.nanoTime() + 10000000000L;
    while (!done.getAsBoolean()) {
      if (System.nanoTime() > limit) throw new AssertionError("GUI worker timeout");
      Thread.sleep(10);
    }
    SwingUtilities.invokeAndWait(() -> {});
  }

  public static void dialogCapture(String name) {
    Timer timer =
        new Timer(
            250,
            e -> {
              for (Window w : Window.getWindows())
                if (w instanceof JDialog && w.isShowing()) {
                  capture(w, name);
                  w.dispose();
                }
            });
    timer.setRepeats(false);
    timer.start();
  }

  public static void finish() {
    for (Window w : Window.getWindows()) w.dispose();
  }
}
